package com.katzenyasax.mall.order.service.impl;

import cn.hutool.core.date.DateTime;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.katzenyasax.common.constant.CartConstant;
import com.katzenyasax.common.constant.OrderConstant;
import com.katzenyasax.common.constant.OrderTokenConstant;
import com.katzenyasax.common.to.MemberTO;
import com.katzenyasax.common.to.OrderItemTO;
import com.katzenyasax.common.to.SpuInfoTO;
import com.katzenyasax.common.to.WareOrderDetailTO;
import com.katzenyasax.common.utils.PageUtils;
import com.katzenyasax.common.utils.Query;
import com.katzenyasax.common.utils.R;
import com.katzenyasax.mall.order.dao.OrderDao;
import com.katzenyasax.mall.order.dao.OrderItemDao;
import com.katzenyasax.mall.order.entity.OrderEntity;
import com.katzenyasax.mall.order.entity.OrderItemEntity;
import com.katzenyasax.mall.order.feign.MemberFeign;
import com.katzenyasax.mall.order.feign.ProductFeign;
import com.katzenyasax.mall.order.feign.WareFeign;
import com.katzenyasax.mall.order.interceptor.OrderInterceptor;
import com.katzenyasax.mall.order.service.OrderService;
import com.katzenyasax.mall.order.vo.*;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

//@RabbitListener(queues = {"spring.test01.queue01"})
@Service("orderService")
public class OrderServiceImpl extends ServiceImpl<OrderDao, OrderEntity> implements OrderService {

    public static ThreadLocal<OrderSubmitVo> submitVoThreadLocal =new ThreadLocal<>();

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    MemberFeign memberFeign;

    @Autowired
    ProductFeign productFeign;

    @Autowired
    WareFeign wareFeign;

    @Autowired
    OrderItemDao orderItemDao;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    OrderDao orderDao;


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OrderEntity> page = this.page(
                new Query<OrderEntity>().getPage(params),
                new QueryWrapper<OrderEntity>()
        );

        return new PageUtils(page);
    }


    /**
     *
     * @param id
     * @return
     *
     * 根据传来的用户id，从redis中抽取check字段为true的商品为确认商品
     *
     * 随后获取该用户一系列确认页面要用的信息并返回封装的对象
     *
     */
    @Override
    public OrderConfirmVo orderConfirm(Long id) {
        //初始化
        OrderConfirmVo finale=new OrderConfirmVo();
        finale.setMemberAddressVos(new ArrayList<>());
        finale.setItems(new ArrayList<>());
        finale.setStocks(new HashMap<>());
        finale.setCount(0);
        finale.setTotal(BigDecimal.ZERO);


        /**
         * 1.选中的商品
         *
         * 使用redis读取该用户购物车中check=true的商品，得到其id
         * 参考cart模块service下的getCart方法
         */
        //获取该用户的cart操作柄
        String redis_key= CartConstant.CART_USER_PREFIX+id;
        BoundHashOperations<String,Object,Object> ops = redisTemplate.boundHashOps(redis_key);
        //获取数据key的集合
        Set<Object> keys = ops.keys();
        //items
        List<OrderItemVo> items=new ArrayList<>();
        //所有商品
        Map<String, BigDecimal> weights = productFeign.allSpuWeights();
        //遍历keys找寻
        keys.forEach(key->{
            OrderItemVo thisVo= JSON.parseObject(
                    ops.get(key).toString()
                    , OrderItemVo.class
            );
            if(thisVo.getCheck()==true){
                //若为选中，则获取重量
                thisVo.setWeight(weights.get(thisVo.getSkuId().toString()));
                //新价格
                thisVo.setPrice(productFeign.getPrice(thisVo.getSkuId()));
                items.add(thisVo);
            }
        });
        finale.setItems(items);


        /**
         * 2.会员地址列表
         *
         * 远程调用member服务
         */
        List<MemberAddressVo> memberAddressVos = new ArrayList<>();
        memberFeign.getByMemberId(id.toString())
                .forEach(address->{
                    memberAddressVos.add(JSON.parseObject(JSON.toJSONString(address),MemberAddressVo.class));
                }
        );
        finale.setMemberAddressVos(memberAddressVos);


        /**
         * 3.令牌
         */
        //获取一个uuid令牌
        String orderToken = UUID.randomUUID().toString();
        finale.setOrderToken(orderToken);
        //将令牌存入redis，格式为k:orderToken:: 用户id     v:token
        redisTemplate.opsForValue().set(
                OrderTokenConstant.ORDER_TOKEN+id.toString()
                ,orderToken
                ,30
                , TimeUnit.MINUTES
        );


        /**
         * 4.商品总数 & 商品总价
         */
        items.forEach(item->{
            finale.setCount(finale.getCount()+ item.getCount());
            finale.setTotal(finale.getTotal().add(item.getTotalPrice()));
        });
        finale.setPayPrice(finale.getTotal());


        /**
         * 5.是否有货
         */
        Map<Long, Boolean> stocks = wareFeign.getSkuStocks();
        items.forEach(item->{
            if(stocks.get(item.getSkuId())==null){
                stocks.put(item.getSkuId(),Boolean.FALSE);
            }
        });
        finale.setStocks(stocks);


        return finale;
    }


    /**
     *
     * @param vo
     * @return
     *
     * 提交订单，接收生成订单的必要信息（OrderSubmitVo）
     * 返回一个回应消息，即订单（OrderEntity）和提交状态（code）
     */
    @Override
    @Transactional
    //@GlobalTransactional
    public SubmitOrderResponseVo submitOrder(OrderSubmitVo vo) {
        /**
         * 代理对象
         * 专用于本地事务间的互相调用
         * 不调用事务就没有用了
         */
        //OrderServiceImpl orderService = (OrderServiceImpl) AopContext.currentProxy();



        //将vo设置为threadLocal，因此可以在整个Service线程内使用
        submitVoThreadLocal.set(vo);
        /**
         * vo内有用的信息为：
         *      地址id：addrId
         *      支付方式：type（无用）
         *      令牌：orderToken=bb8742a8-ff40-471a-8bc2-2cd186e3d0a1
         *      支付价格：payPrice=461
         *      remarks=null（无用）
         */

        //结果封装，初始化
        SubmitOrderResponseVo finale=new SubmitOrderResponseVo();
        finale.setOrder(new OrderEntity());
        finale.setCode(1);

        //登录用户;
        Long userId = OrderInterceptor.orderThreadLocal.get().getId();


        /**
         * 1.验证令牌
         * 此过程需要保证原子性
         */
        String script="if redis.call('get',KEYS[1])==ARGV[1] then return redis.call('del',KEYS[1]) else return 0 end";      //lua脚本
        Long ifTokenCorrect = (Long) redisTemplate.execute(
                new DefaultRedisScript<Long>(script, Long.class)            //返回值类型
                , Arrays.asList(OrderTokenConstant.ORDER_TOKEN + userId)       //验证的数据redis中的名字
                , vo.getOrderToken()                                         //要验证的数据
        );
        if (ifTokenCorrect.equals(1l)){
            //令牌正确，开始执行业务
            finale.setCode(0);

            /**
             * 开始构造OrderEntity，这个会作为回显到前台的数据
             */
            OrderEntity order = this.buildOrderEntity();

            /**
             * 开始构造表单项List<OrderItemEntity>，这些是后台操作数据库要用的数据
             *
             * 表单项数据的主体为单个商品，通过订单id和sn来和订单进行绑定
             *
             * 订单id为后台方便调取数据存入的数据，而sn则是前台用户可以看到的数据
             *
             */
            List<OrderItemEntity> items = this.buildOrderItemEntity(order.getDeliverySn());

            /**
             * 订单状态，0表示未支付
             */
            order.setSourceType(0);

            /**
             * 所有商品的价格，和原先的总价进行验证
             * 如果两者差价小于0.01，代表两者至少小数点后两位之前是相等的，则可以接受
             */
            BigDecimal newestPayPrice=this.buildNewestPayPrice(items);
            if(Math.abs(vo.getPayPrice().subtract(newestPayPrice.add(order.getFreightAmount())).doubleValue())<0.01) {
                //若二者差值小于0.01，可以接受
                order.setPayAmount(newestPayPrice.add(order.getFreightAmount()));
                finale.setCode(0);
            } else {
                //则表示前后价格不一，直接打回前端要求重新提交
                finale.setCode(2);
                return finale;
            }

            /**
             * 此时可以认为已经验价成功，order数据应当写到数据库中
             */
            this.saveOrderEntity(order);
            //拿到存入的id
            Long newId = baseMapper.selectOne(
                    new QueryWrapper<OrderEntity>().eq("order_sn", order.getOrderSn())
            ).getId();
            /**
             * 存order到redis
             */
            /*redisTemplate.opsForValue().set(
                    OrderConstant.ORDER_TEMP+order.getId()
                    ,JSON.toJSONString(order.getStatus())
            );*/
            /**
             * 锁定库存
             */
            //专供远程调用的to
            List<OrderItemTO> tos = items.stream().map(item -> {
                OrderItemTO to = new OrderItemTO();
                BeanUtils.copyProperties(item, to);
                return to;
            }).collect(Collectors.toList());
            //获取锁库存响应
            Map<Long,Long> resp = wareFeign.lockWare(tos);
            if(resp==null){
                //锁库存失败
                baseMapper.deleteById(order.getId());   //要删掉提交失败的订单id
                finale.setCode(1);
                return finale;
            } else {
                //锁库存成功
                /**
                 * 存orderItems
                 */
                this.saveOrderItemEntities(items,newId);    //不会出错

                /**
                 *
                 * 存ware_order_task和ware_order_task_detail
                 */
                //这个是sku存在哪个ware，前一个是skuId，后一个是wareId
                //锁库存失败时，给的是null
                List<WareOrderDetailTO> taskDetailList=new ArrayList<>();
                for (OrderItemEntity item : items) {
                    WareOrderDetailTO to=new WareOrderDetailTO();
                    //构建数据
                    to.setOrderId(order.getId());
                    to.setOrderSn(order.getOrderSn());
                    to.setSkuId(item.getSkuId());
                    to.setSkuNum(Long.parseLong(item.getSkuQuantity().toString()));
                    Long wareId = resp.get(item.getSkuId());
                    to.setWareId(wareId);      //skuId为key，可以直接拿到对应的value
                    //封装
                    taskDetailList.add(to);
                }

                /**
                 * 保存task
                 */
                wareFeign.saveTasks(taskDetailList);

                /**
                 * 完成回显数据的封装，并结束该业务
                 */
                finale.setOrder(order);
                System.out.println(finale);
                /**
                 * 往ware发消息
                 */
                try {
                    rabbitTemplate.convertAndSend(
                            "stock.exchange.top"
                            , "stock.key.locked"
                            , taskDetailList);
                    System.out.println("向stock.exchange.top发送了消息：" + taskDetailList);
                }catch (Exception e){
                    System.out.println("向stock.exchange.top发送消息时发生了错误");
                }
                /**
                 * 往order发消息
                 */
                try {
                    rabbitTemplate.convertAndSend(
                            "order.exchange.top"
                            , "order.key.created"
                            , order.getId());
                    System.out.println("向order.exchange.top发送了消息：" + order.getId());
                }catch (Exception e){
                    System.out.println("向order.exchange.top发送消息时发生错误");
                }
            }
        }
        else {
            //令牌不一致
            finale.setCode(3);
        }


        return finale;
    }


    /**
     *
     * @param order
     *
     * 处理订单状态，此时订单若订单不为0或其他，为1，2，3，则缓存中的改成1
     * 否则不做处理
     */
    @Override
    @Transactional
    public void dealWithOrderStatus(Long order) {
        OrderEntity thisOrder = orderDao.selectById(order);
                /*模拟订单已支付的情况，人为做出已支付的情况
                 thisOrder.setStatus(1);
                 orderDao.updateById(thisOrder);*/
        switch (thisOrder.getStatus()) {
            case 1, 2, 3: {
                /*redisTemplate.delete(OrderConstant.ORDER_TEMP + order);
                redisTemplate.opsForValue().set(
                        OrderConstant.ORDER_TEMP + order
                        , "1"
                );*/
                System.out.println("该订单已支付");
                break;
            }
            default: {
                System.out.println(order + "号订单未及时付款，已失效");
                thisOrder.setStatus(4);
                baseMapper.updateById(thisOrder);
                /*redisTemplate.delete(OrderConstant.ORDER_TEMP + order);
                redisTemplate.opsForValue().set(
                        OrderConstant.ORDER_TEMP + order
                        , "4"
                );*/
            }
        }
    }


    /**
     * @param pageNum
     * @param memberId
     * @return 获取用户订单，传入的是要去的页码
     */
    @Override
    public PageUtils getMemberOrder(Long pageNum, Long memberId) {
        System.out.println("     OrderService::getMemberOrder : \"memberId\":"+memberId);
        Map<String, Object> param = new HashMap<>();
        param.put("page",pageNum.toString());
        IPage<OrderEntity> page=this.page(
                new Query<OrderEntity>().getPage(param)
                ,new QueryWrapper<OrderEntity>()
                        .eq("member_id",memberId)
                        .orderByDesc("id")
        );
        page.getRecords().stream().map(order -> {
            List<OrderItemEntity> items = orderItemDao.selectList(new QueryWrapper<OrderItemEntity>().eq("order_id", order.getId()));
            order.setOrderItemEntityList(items);
            return order;
        }).collect(Collectors.toList());
        return new PageUtils(page);
    }


    /**
     * 支付宝支付
     *
     * @return
     */
    @Override
    public Boolean aliPayOrder(String orderSn) {
        OrderEntity thisOrder = baseMapper.selectOne(new QueryWrapper<OrderEntity>().eq("order_sn", orderSn));
        if (thisOrder.getStatus().equals(0)) {
            thisOrder.setStatus(1);
            baseMapper.updateById(thisOrder);
            //redisTemplate.opsForValue().set(OrderConstant.ORDER_TEMP + thisOrder.getId(), "1");
            return true;
        }else {
            System.out.println("     OrderService::aliPayOrder : 订单已过期");
            return false;
        }
    }

    /**
     * @param items
     * @param newId
     * 保存订单项信息
     */
    private void saveOrderItemEntities(List<OrderItemEntity> items, Long newId) {
        items.forEach(item->{
            item.setOrderId(newId);
            orderItemDao.insert(item);
        });
    }


    /**
     *
     * @param order
     *
     * 保存orderEntity，直接使用baseMapper存
     */
    private void saveOrderEntity(OrderEntity order) {
        baseMapper.insert(order);
    }

    /**
     *
     * @param orderItems
     * @return
     *
     * 创建最新价格
     */
    private BigDecimal buildNewestPayPrice(List<OrderItemEntity> orderItems) {
        BigDecimal finale = BigDecimal.ZERO;
        for (OrderItemEntity item : orderItems) {
            BigDecimal thisPrice = item.getSkuPrice().multiply(
                    BigDecimal.valueOf(item.getSkuQuantity())
            );
            System.out.println(thisPrice);
            finale = finale.add(thisPrice);
        }
        return finale;
    }


    /**
     *
     * @return
     *
     * 创建订单
     */
    public OrderEntity buildOrderEntity(){

        OrderSubmitVo vo = submitVoThreadLocal.get();
        Long userId=OrderInterceptor.orderThreadLocal.get().getId();

        //1.基础信息
        OrderEntity order=new OrderEntity();
        order.setMemberId(userId);                  //提交用户
        order.setOrderSn(IdWorker.getTimeId());     //订单号
        order.setCreateTime(new DateTime());            //创建时间
        //order.setPayAmount(vo.getPayPrice());           //应付金额
        order.setStatus(0);                                 //未支付状态
        //该金额就是确认时直接拿取数据库的介个

        //2.用户信息
        MemberTO thisMember = JSON.parseObject(
                JSON.toJSONString(memberFeign.info(userId).get("member"))
                , MemberTO.class
        );
        order.setMemberUsername(thisMember.getUsername());

        //3.优惠信息
        //跳过先 todo

        //4.地址和运费
        R fare = wareFeign.getFare(vo.getAddrId());
        FareVo addressAndFare=JSON.parseObject(JSON.toJSONString(fare.get("data")),FareVo.class);
        //4.1运费
        order.setFreightAmount(addressAndFare.getFare());           //运费
        MemberAddressVo addr = addressAndFare.getAddress();
        //4.2收货人信息
        order.setBillReceiverPhone(addr.getPhone());        //收票人电话
        order.setReceiverName(addr.getName());              //收货人姓名
        order.setReceiverPostCode(addr.getPostCode());      //收货人邮编
        //4.3详细地址
        order.setReceiverProvince(addr.getProvince());      //省
        order.setReceiverCity(addr.getCity());              //城市
        order.setReceiverRegion(addr.getRegion());          //区
        order.setReceiverDetailAddress(addr.getDetailAddress());        //详细地址

        return order;
    }


    /**
     *
     * @param sn
     * @return
     *
     * 再次从cart内得到skuId的set，并从数据库中读取数据
     */
    public List<OrderItemEntity> buildOrderItemEntity(String sn){
        //本地线程共享的数据
        Long userId=OrderInterceptor.orderThreadLocal.get().getId();
        //获取该用户的cart操作柄
        String redis_key= CartConstant.CART_USER_PREFIX+userId;
        BoundHashOperations<String,Object,Object> ops = redisTemplate.boundHashOps(redis_key);
        //获取数据key的集合，是sku的id
        Set<Object> keys = ops.keys();
        List<OrderItemEntity> finale=new ArrayList<>();
        for (Object key : keys) {
            OrderItemVo thisItemVO = JSON.parseObject(ops.get(key).toString(), OrderItemVo.class);
            if (thisItemVO.getCheck()) {
                //只有被选中的才是订单商品
                OrderItemEntity entity = new OrderItemEntity();
                //1.订单信息
                entity.setOrderSn(sn);

                //2.sku信息
                entity.setSkuId(thisItemVO.getSkuId());
                entity.setSpuName(thisItemVO.getTitle());
                entity.setSkuPic(thisItemVO.getImage());
                entity.setSkuPrice(thisItemVO.getPrice());
                entity.setSkuQuantity(thisItemVO.getCount());
                entity.setSkuAttrsVals(String.join(",", thisItemVO.getSkuAttrValues()));

                //3.spu信息
                //从product模块里拿一个SpuInfoTO
                SpuInfoTO thisSpu = productFeign.getSpuBySkuId(thisItemVO.getSkuId());
                entity.setSpuId(thisSpu.getId());               //spuId
                entity.setSpuName(thisSpu.getSpuName());        //spuName
                entity.setSpuPic(thisSpu.getSpuDescription());          //描述图
                entity.setSpuBrand(thisSpu.getBrandId().toString());        //品牌id
                entity.setCategoryId(thisSpu.getCatalogId());               //分类id

                //4.优惠信息
                //todo 暂时不做

                //5.积分、成长值等
                entity.setGiftIntegration(
                        entity.getSkuPrice().divideAndRemainder(BigDecimal.ONE)[0].intValue()
                );     //积分
                entity.setGiftGrowth(
                        entity.getSkuPrice().divideAndRemainder(BigDecimal.ONE)[0].intValue() + new Random().nextInt(10)
                );        //成长值

                //保存
                finale.add(entity);
            }
        }

        return finale;
    }


    //==========消息队列的测试方法========================================================
    /**
     * 监听OrderEntity对象消息
     *
     * 加上了手动确认ack的
     */
    //@RabbitHandler
    //@RabbitListener(queues = {"spring.test01.queue01"})
    public void listenOrderMessage(Message message, OrderEntity body, Channel channel) {
        System.out.println("接收到OrderEntity对象消息："+body);
        try {
            channel.basicAck(
                    message.getMessageProperties().getDeliveryTag()     //消息的tag
                    , false                                         //是否批量确认
            );

            /*
             * 拒绝确认消息
             * channel.basicNack(
             *             message.getMessageProperties().getDeliveryTag()     //消息的tag
             *             , false                                         //是否批量确认
             *             , true                                          //退回队列，或是删除消息
             *     );
             */

        }catch (Exception e){
            System.out.println(message.getMessageProperties().getDeliveryTag()+"号消息异常");
        }
    }

    /**
     * 接收String对象消息
     */
    //@RabbitHandler
    public void listenOrderMessage(String message,Channel channel){
        System.out.println("接受到String对象消息："+message);
    }

}