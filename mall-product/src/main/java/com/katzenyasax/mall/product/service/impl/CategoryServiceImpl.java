package com.katzenyasax.mall.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.aliyuncs.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.katzenyasax.common.utils.PageUtils;
import com.katzenyasax.common.utils.Query;
import com.katzenyasax.mall.product.dao.CategoryDao;
import com.katzenyasax.mall.product.entity.CategoryEntity;
import com.katzenyasax.mall.product.service.CategoryService;
import com.katzenyasax.mall.product.vo.catalogVO.Catalog2VO;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Service("categoryService")
@Slf4j
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {


    @Autowired
    StringRedisTemplate redisTemplate;

    @Autowired
    RedissonClient redissonClient;


    /**
     * @param params
     * @return
     *
     * mybatis plus自动生成的方法
     * 查所有
     *
     */
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }









    /**
     *
     * @param list
     *
     * 逻辑删除
     * 不删除category本身，只将其show_status改成0
     * 相当于变相的update
     *
     * 当然这是经过配置的操作
     *
     */
    @CacheEvict(value = {"product-category"},key="'listOne'") //删除缓存
    @Override
    public void hideByIds(List<Long> list) {
        //TODO 1.判断数据是否被引用
        //2.隐藏数据
        //  直接删除就可以了
        baseMapper.deleteBatchIds(list);
    }





    //用于排序
    @Override
    public void Sort(CategoryEntity[] category) {
        //TODO 批量排序

    }


    /**
     * @param id
     * @return
     *
     * 回显分类的完整路径
     * （专供品牌修改的数据回显）
     *
     *
     * 可重复使用
     *
     */
    public Long[] getCategoryPath(Long id) {
        Long[] pathF=new Long[3];
        //最多三级，因此数组长度为3
        pathF[2]=id;
        //最后一个数就是该元素（孙子）的id
        CategoryEntity son = this.getById(id);
        //儿子对象
        pathF[1]=son.getParentCid();
        //数组第二个元素为儿子的id
        CategoryEntity father=this.getById(son.getParentCid());
        //父亲对象
        pathF[0]=father.getParentCid();
        //数组第一个元素为父亲的id
        return pathF;
        //直接返回
    }

/**
 * ========================================================== 商城首页业务 ==============================================================================================
 * ============================================================================================================================================================
 */

    /**
     *
     * @return
     *
     * 获取一级分类
     */
    @Cacheable(value = {"product-category"},key="#root.method.name")
    @Override
    public List<CategoryEntity> listOne() {
        System.out.println("缓存：获取到了一级分类...");
        List<CategoryEntity> listAll=baseMapper.selectList(null);
        List<CategoryEntity> finale=listAll.stream().filter(c->c.getCatLevel()==1).collect(Collectors.toList());
        return finale;
    }



/**
 * ================================================ 指令三级分类 ===============================================================
 */


    /**
     * 获取三级分类
     * 封装为单独方法
     */
    public List<CategoryEntity> getListTreeDB(){
        /**
         * 虽然只需要和数据库连接一次
         * 但是循环很多次
         * 所以性能不算好
         */
                /* //查出所有分类
                        List<CategoryEntity> entities=baseMapper.selectList(null);
                        //获取一级子类
                        List<CategoryEntity> oneCategory=entities.stream().filter(categoryEntity -> categoryEntity.getCatLevel()==1).toList();
                        //获取所有二级子类
                        List<CategoryEntity> twoCategory=entities.stream().filter(categoryEntity -> categoryEntity.getCatLevel()==2).toList();
                        //获取所有三级子类
                        List<CategoryEntity> threeCategory=entities.stream().filter(categoryEntity -> categoryEntity.getCatLevel()==3).toList();
                        //从三级子类开始遍历，将三级子类组装到二级子类
                        for(CategoryEntity category3:threeCategory){
                            for(CategoryEntity category2:twoCategory){
                                if(category3.getParentCid()==category2.getCatId()){
                                    category2.getChildren().add(category3);
                                }
                            }
                        }        //从二级子类开始遍历，将二级子类组装到一级子类
                        for(CategoryEntity category2:twoCategory){
                            for(CategoryEntity category1:oneCategory){
                                if(category2.getParentCid()==category1.getCatId()){
                                    category1.getChildren().add(category2);
                                }
                            }
                        }
                        return oneCategory;*/
        /**
         * 效率低下，有多少条数据就进行多少次连接
         */
                /*//查出所有一级菜单：
                        List<CategoryEntity> listI=this.listOne();
                        return listI.stream().map(
                                I->{
                                    List<CategoryEntity> listII=baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid",I.getCatId()));
                                    List<CategoryEntity> ii = listII.stream().map(
                                            II->{
                                                List<CategoryEntity> listIII=baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid",II.getCatId()));
                                                List<CategoryEntity> iii = listIII.stream().map(
                                                        III -> {
                                                            III.setChildren(null);
                                                            return III;
                                                        }
                                                ).collect(Collectors.toList());
                                                II.setChildren(iii);
                                                return II;
                                            }
                                    ).collect(Collectors.toList());
                                    I.setChildren(ii);
                                    return I;
                                }
                        ).collect(Collectors.toList());*/

        /**
         * 只需要连接一次数据库
         * 性能好
         */
        //查出所有菜单
        List<CategoryEntity> listAll = baseMapper.selectList(null);
        //查出所有一级菜单：
        List<CategoryEntity> listI = listAll.stream().filter(c -> c.getParentCid() == 0).collect(Collectors.toList());
        List<CategoryEntity> finale= listI.stream().map(
                I -> {
                    List<CategoryEntity> listII = listAll.stream().filter(c -> c.getParentCid() == I.getCatId()).collect(Collectors.toList());
                    List<CategoryEntity> ii = listII.stream().map(
                            II -> {
                                List<CategoryEntity> listIII = listAll.stream().filter(c -> c.getParentCid() == II.getCatId()).collect(Collectors.toList());
                                List<CategoryEntity> iii = listIII.stream().map(
                                        III -> {
                                            III.setChildren(null);
                                            return III;
                                        }).collect(Collectors.toList());
                                II.setChildren(iii);
                                return II;
                            }).collect(Collectors.toList());
                    I.setChildren(ii);
                    return I;
                }).collect(Collectors.toList());

        return finale;
    }


    /**
     * @return
     *
     * 本地锁
     * 进程查不到redis中数据时，通过该本地锁查数据库
     *
     */
    public List<CategoryEntity> listAsTree_LocalLock() {
        synchronized (this) {
            //让连接器创建一个操作杠杆，用于直接操作redis
            ValueOperations<String, String> ops = redisTemplate.opsForValue();
            //获取json字符串，redis中名为：CatalogJson
            String catalogListAsTree = ops.get("CatalogListAsTree");
            if (StringUtils.isEmpty(catalogListAsTree)) {
                System.out.println("本地锁内，redis中无数据，查库");

                List<CategoryEntity> finale=this.getListTreeDB();
                //将方法封装，从库中获取三级分类，非必要不调用该方法

                if(finale==null){
                    //若从数据库中获取来的也为空
                    finale=new ArrayList<>();
                    //直接将finale赋为空内容对象
                }
                ops.set("CatalogListAsTree",JSON.toJSONString(finale),300+(new Random().nextInt(150)), TimeUnit.SECONDS);
                //存入
                return finale;
            }
            else{
                System.out.println("本地锁内，redis中有数据，直接返回");
                //若不为空，则直接将redis中获取的json字符串反序列化为对象，返回对象
                List<CategoryEntity> finale=JSON.parseObject(catalogListAsTree,new TypeReference<List<CategoryEntity>>(){});
                return finale;
            }
        }
        /**
         * 锁
         */
    }


    /**
     * 分布式锁
     * 进程查不到redis中数据时，进该分布式锁查库
     *
     */
    public List<CategoryEntity> listAsTree_RedisLock() {
        //让连接器创建一个操作杠杆ops，用于直接操作redis
        ValueOperations<String, String> ops = redisTemplate.opsForValue();

        while (true) {
            //1.setNX
            String uuid=UUID.randomUUID().toString();
            Boolean ifSet = ops.setIfAbsent("lock", uuid,10,TimeUnit.SECONDS);
            //给锁设置过期时间，防止死锁

            //2.判断锁
            if (ifSet) {
                //加锁成功
                //则查库
                String catalogJson = ops.get("CatalogListAsTree");
                //从redis获取数据
                if (StringUtils.isEmpty(catalogJson)) {
                    System.out.println("分布式锁内，redis无数据，查库");
                    List<CategoryEntity> finale=this.getListTreeDB();
                    //封装的方法，从库中获取三级分类，非必要不调用该方法
                    if(finale==null){
                        //若从数据库中获取来的也为空
                        finale=new ArrayList<>();
                        //直接将finale赋为空内容对象
                    }
                    ops.set("CatalogListAsTree",JSON.toJSONString(finale),300+(new Random().nextInt(150)), TimeUnit.SECONDS);
                    //数据存入redis
                    /**
                     *      if(ops.get("lock").equals(uuid)) {
                     *      //若lock的值是uuid，也即是自己的锁
                     *      stringRedisTemplate.delete("lock");
                     *      //删除锁
                     * }
                     *
                     * 替换传统方法，使用lua脚本方法，原子解锁
                     *
                     */

                    String script="if redis.call('get',KEYS[1])==ARGV[1] then return redis.call('del',KEYS[1]) else return 0 end";
                    Long lock= redisTemplate.execute(new DefaultRedisScript<Long>(script, Long.class),Arrays.asList("lock"),uuid);
                    /**
                     * 使用连接器execute方法，调用lua脚本script，
                     * 该脚本用于原子删除数据，
                     * 我们要删除的数据为lock
                     * 且只有当其值为uuid时才执行删除
                     *
                     * lua中
                     * 传入的Arrays.asList("lock")就是KEYS
                     * lock的值就是KEYS[1]，因为只传入了一个key
                     * uuid就是ARGV[1]
                     *
                     */

                    return finale;
                }
                else{
                    System.out.println("分布式锁内，redis中有数据，直接返回");
                    //若不为空，则直接将redis中获取的json字符串反序列化为对象，返回对象
                    List<CategoryEntity> finale=JSON.parseObject(catalogJson, new TypeReference<List<CategoryEntity>> (){});
                    return finale;
                }
            }
        }
    }

    /**
     * 分布式锁
     * 进程查不到redis中数据时，进该分布式锁查库
     *
     * 使用redisson分布式锁
     * 仅有一个进程可以进入，也即是一旦进入进程说明redis中一定没有想要的数据
     * 故不需要针对进来的多余进程进行二次判断redis
     *
     */
    public List<CategoryEntity> listAsTree_RedissonLock() {
        System.out.println("redisson锁内...");
        List<CategoryEntity> finale;  //返回值实例
        ValueOperations<String, String> ops = redisTemplate.opsForValue();  //让连接器创建一个操作杠杆ops，用于直接操作redis
        RLock lock=redissonClient.getLock("CatalogListAsTree_Lock");    //获取可重入锁，锁整个进程
        lock.lock(30,TimeUnit.SECONDS);     //上锁，自定义30秒
        try {

            finale = this.getListTreeDB();  //封装的方法，从库中获取三级分类
            /**if (finale == null) {       //若从数据库中获取来的也为空
                finale = new ArrayList<>();     //直接将finale赋为空内容对象
            }

            RReadWriteLock readWriteLock = redissonClient.getReadWriteLock("CatalogListAsTree_ReadWriteLock");  //读写锁
            readWriteLock.writeLock().lock(30, TimeUnit.SECONDS);    //写锁上锁
            ops.set("CatalogListAsTree", JSON.toJSONString(finale), 300 + (new Random().nextInt(150)), TimeUnit.SECONDS);     //数据存入redis
            readWriteLock.writeLock().unlock();  //写锁解锁*/
        } finally {
            lock.unlock();  //解锁进程
        }
        System.out.println("获取到数据库中数据，是否为空？"+!finale.isEmpty());
        return finale;
    }


    /**
     *
     * @return
     *
     * 经过redis缓存判断的三级菜单
     *
     */
    @Cacheable(value = {"product-category"},key="'CatalogListAsTree'")
    @Override
    public List<CategoryEntity> listAsTree(){
        ValueOperations<String,String> ops= redisTemplate.opsForValue();    //让连接器创建一个操作杠杆，用于直接操作redis
        RReadWriteLock readWriteLock = redissonClient.getReadWriteLock("CatalogListAsTree_ReadWriteLock");  //读写锁

        readWriteLock.readLock().lock(30,TimeUnit.SECONDS);     //读锁
        String catalogListAsTree=ops.get("CatalogListAsTree");  //从redis获取catalogListAsTree数据
        readWriteLock.readLock().unlock();//读锁解锁

        if(StringUtils.isEmpty(catalogListAsTree)){         //判断catalogJson是否为空。
            System.out.println("redis中无数据，将进锁查库...");

            //List<CategoryEntity> finale=this.listAsTree_LocalLock();  //进本地锁查数据
            //List<CategoryEntity> finale=this.listAsTree_RedisLock();  //进分布式锁查数据
            List<CategoryEntity> finale=this.listAsTree_RedissonLock();     //使用redisson分布式锁

            /**
             *
             *
             * if(finale==null){   //若从数据库中获取来的也为空
             *     finale=new ArrayList<>();   //直接将finale赋为空内容对象
             * }
 *
             * readWriteLock.readLock().lock(30,TimeUnit.SECONDS); //写锁上锁
             * ops.set("CatalogListAsTree",JSON.toJSONString(finale),300+(new Random().nextInt(150)), TimeUnit.SECONDS);   //设置过期时间，标准过期时间300s，在此基础上加上0-149秒的随机时间
             * readWriteLock.readLock().unlock();  //写锁过期*/

            return finale;
        }
        else{
            System.out.println("redis中有数据，直接返回");
            List<CategoryEntity> finale=JSON.parseObject(catalogListAsTree,new TypeReference<List<CategoryEntity>>(){});                //若不为空，则直接将redis中获取的json字符串反序列化为对象，返回对象
            return finale;
        }
    }


/**
 * ======================================================= 首页三级分类 ===========================================================================
  */


    /**
     *
     * @return
     *
     * 查询一级分类下所有的二三级菜单，返回一级分类的Map
     *
     * 思路是：
     * 遍历一级菜单：
     * {
     *      遍历到单个一级菜单时，获取其所有二级菜单
     *      遍历二级菜单：
     *      {
     *          遍历到单个二级菜单时，获取其所有三级菜单
     *          遍历三级菜单：
     *           {
     *               将三级菜单信息封装
     *               将封装信息返回上一级，作为本单个二级菜单下三级菜单集合的一部分
     *           }
     *           将遍历结果封装为集合
     *           将封装信息返回上一级，作为本单个一级菜单下二级菜单集合的一部分
     *      }
     *      将遍历结果封装为集合
     *      将封装信息直接赋给本单个一级菜单，作为本当一级菜单下二级菜单集合
     * }
     */
    public Map<String, List<Catalog2VO>> getCatalogsDB(){
        /**
         * 效率低下，有多少数据就要和数据库连接多少次
         */
        /*//查出所有一级菜单：
        List<CategoryEntity> listI=this.listOne();
        Map<String, List<Catalog2VO>> finale = listI.stream().collect(Collectors.toMap(
                k -> k.getCatId().toString(),
                //遍历到单个一级菜单
                I -> {
                    //查出该一级菜单下所有二级菜单：
                    List<CategoryEntity> listII = baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", I.getCatId()));
                    List<Catalog2VO> catalogII = listII.stream().map(
                            //遍历到单个二级菜单
                            II -> {
                                //查出该二级菜单下所有三级菜单：
                                List<CategoryEntity> listIII = baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", II.getCatId()));
                                List<Catalog2VO.Catalog3VO> catalogIII = listIII.stream().map(
                                        //遍历到单个三级菜单
                                        III -> {
                                            Catalog2VO.Catalog3VO iii = new Catalog2VO.Catalog3VO();
                                            //iii是该三级菜单的封装对象
                                            iii.setCatalog2Id(II.getCatId().toString());
                                            iii.setId(III.getCatId().toString());
                                            iii.setName(III.getName());
                                            return iii;
                                        }
                                ).collect(Collectors.toList());
                                //此时catalogIII就是该二级菜单下面的所有三级菜单

                                Catalog2VO ii = new Catalog2VO();
                                //ii是该二级菜单的封装对象
                                ii.setCatalog1Id(I.getCatId().toString());
                                ii.setId(II.getCatId().toString());
                                ii.setName(II.getName());
                                ii.setCatalog3List(catalogIII);
                                return ii;
                            }
                    ).collect(Collectors.toList());
                    //此时catalogII就是一级菜单下的所有二级菜单

                    return catalogII;
                }
        ));
        return finale;*/

        /**
         * 效率高，只查一遍数据库
         */
        //查出所有数据
        List<CategoryEntity> listAll = baseMapper.selectList(new QueryWrapper<>());
        //查出所有一级分类
        List<CategoryEntity> listI = listAll.stream().filter(c -> c.getParentCid() == 0).collect(Collectors.toList());
        Map<String, List<Catalog2VO>> finale = listI.stream().collect(Collectors.toMap(
                k -> k.getCatId().toString(),
                //遍历到单个一级菜单
                I -> {
                    //查出该一级菜单下所有二级菜单：
                    List<CategoryEntity> listII = listAll.stream().filter(c -> c.getParentCid() == I.getCatId()).collect(Collectors.toList());
                    List<Catalog2VO> catalogII = listII.stream().map(
                            //遍历到单个二级菜单
                            II -> {
                                //查出该二级菜单下所有三级菜单：
                                List<CategoryEntity> listIII = listAll.stream().filter(c -> c.getParentCid() == II.getCatId()).collect(Collectors.toList());
                                List<Catalog2VO.Catalog3VO> catalogIII = listIII.stream().map(
                                        //遍历到单个三级菜单
                                        III -> {
                                            Catalog2VO.Catalog3VO iii = new Catalog2VO.Catalog3VO();
                                            //iii是该三级菜单的封装对象
                                            iii.setCatalog2Id(II.getCatId().toString());
                                            iii.setId(III.getCatId().toString());
                                            iii.setName(III.getName());
                                            return iii;
                                        }).collect(Collectors.toList());
                                //此时catalogIII就是该二级菜单下面的所有三级菜单
                                Catalog2VO ii = new Catalog2VO();
                                //ii是该二级菜单的封装对象
                                ii.setCatalog1Id(I.getCatId().toString());
                                ii.setId(II.getCatId().toString());
                                ii.setName(II.getName());
                                ii.setCatalog3List(catalogIII);
                                return ii;
                            }).collect(Collectors.toList());
                    //此时catalogII就是一级菜单下的所有二级菜单
                    return catalogII;
                }));


        return finale;
    }


    /**
     * @return
     *
     * 本地锁
     * 进程查不到redis中数据时，通过该本地锁查库
     *
     */
    public Map<String, List<Catalog2VO>> getCatalogs_LocalLock() {
        /**
         * 上锁
         * 先从redis读取，没有才进行查库
         * 查到库后，不管是哪一个进程都要存进redis
         * 后续进来的就不用再查库了
         */
        synchronized (this) {
            //让连接器创建一个操作杠杆，用于直接操作redis
            ValueOperations<String, String> ops = redisTemplate.opsForValue();
            //获取json字符串，redis中名为：CatalogJson
            String catalogJson = ops.get("CatalogJson");
            if (StringUtils.isEmpty(catalogJson)) {

                System.out.println("本地锁内，redis无数据，查库");

                Map<String, List<Catalog2VO>> finale=this.getCatalogsDB();
                //封装的方法，从库中获取三级分类，非必要不调用该方法

                if(finale==null){
                    //若从数据库中获取来的也为空
                    finale=new HashMap<>();
                    //直接将finale赋为空内容对象
                }
                ops.set("CatalogJson",JSON.toJSONString(finale),300+(new Random().nextInt(150)), TimeUnit.SECONDS);
                //存入
                return finale;
            }
            else{
                System.out.println("本地锁内，redis中有数据，直接返回");
                //若不为空，则直接将redis中获取的json字符串反序列化为对象，返回对象
                Map<String, List<Catalog2VO>> finale=JSON.parseObject(catalogJson, new TypeReference<Map<String, List<Catalog2VO>>> (){});
                return finale;
            }
        }
        /**
         * 锁
         */
    }


    /**
     * 分布式锁
     * 进程查不到redis中数据时，通过该分布式锁查库
     *
     * 进程进锁时在redis中setNX一个lock
     * setNX就是只有k不存在时才set，返回值为boolean类型，表示有没有set上
     * 若set成功则锁住
     * 若set失败则等待
     * 锁释放后lock删除
     *
     */
    public Map<String, List<Catalog2VO>> getCatalogs_RedisLock() {
        //让连接器创建一个操作杠杆ops，用于直接操作redis
        ValueOperations<String, String> ops = redisTemplate.opsForValue();

        while (true) {
            //1.setNX
            String uuid=UUID.randomUUID().toString();
            Boolean ifSet = ops.setIfAbsent("lock", uuid,10,TimeUnit.SECONDS);
            //给锁设置过期时间，防止死锁

            //2.判断锁
            if (ifSet) {
                //加锁成功
                //则查库
                String catalogJson = ops.get("CatalogJson");
                //从redis获取数据
                if (StringUtils.isEmpty(catalogJson)) {
                    System.out.println("分布式锁内，redis无数据，查库");
                    Map<String, List<Catalog2VO>> finale=this.getCatalogsDB();
                    //封装的方法，从库中获取三级分类，非必要不调用该方法
                    if(finale==null){
                        //若从数据库中获取来的也为空
                        finale=new HashMap<>();
                        //直接将finale赋为空内容对象
                    }
                    ops.set("CatalogJson",JSON.toJSONString(finale),300+(new Random().nextInt(150)), TimeUnit.SECONDS);
                    //数据存入redis
                    /**
                     *      if(ops.get("lock").equals(uuid)) {
                     *      //若lock的值是uuid，也即是自己的锁
                     *      stringRedisTemplate.delete("lock");
                     *      //删除锁
                     * }
                     *
                     * 替换传统方法，使用lua脚本方法，原子解锁
                     *
                     */

                    String script="if redis.call('get',KEYS[1])==ARGV[1] then return redis.call('del',KEYS[1]) else return 0 end";
                    redisTemplate.execute(new DefaultRedisScript<Long>(script, Long.class),Arrays.asList("lock"),uuid);
                    /**
                     * 使用连接器execute方法，调用lua脚本script，
                     * 该脚本用于原子删除数据，
                     * 我们要删除的数据为lock
                     * 且只有当其值为uuid时才执行删除
                     *
                     * lua中
                     * 传入的Arrays.asList("lock")就是KEYS
                     * lock的值就是KEYS[1]，因为只传入了一个key
                     * uuid就是ARGV[1]
                     *
                     */

                    return finale;
                }
                else{
                    System.out.println("分布式锁内，redis中有数据，直接返回");
                    //若不为空，则直接将redis中获取的json字符串反序列化为对象，返回对象
                    Map<String, List<Catalog2VO>> finale=JSON.parseObject(catalogJson, new TypeReference<Map<String, List<Catalog2VO>>> (){});
                    return finale;
                }
            }
        }
    }



    /**
     * 分布式锁
     * 进程查不到redis中数据时，通过该分布式锁查库
     *
     * 使用redisson分布式锁
     * 仅有一个进程可以进入，也即是一旦进入进程说明redis中一定没有想要的数据
     * 故不需要针对进来的多余进程进行二次判断redis
     *
     */
    public Map<String, List<Catalog2VO>> getCatalogs_RedissonLock() {
        System.out.println("redisson锁内...");
        Map<String, List<Catalog2VO>> finale;       //返回值实例化对象
        ValueOperations<String, String> ops = redisTemplate.opsForValue();      //让连接器创建一个操作杠杆ops，用于直接操作redis
        RLock lock=redissonClient.getLock("CatalogJson_Lock");    //获取可重入锁，锁整个进程
        lock.lock(30,TimeUnit.SECONDS);     //上锁，自定义30秒
        try {

            finale = this.getCatalogsDB();      //封装的方法，从库中获取三级分类
            /**
             *
             * 手动存缓存
             *
             * if (finale == null) {           //若从数据库中获取来的也为空
             *     finale = new HashMap<>();       //直接将finale赋为空内容对象
             * }
 *
             * RReadWriteLock readWriteLock = redissonClient.getReadWriteLock("CatalogJson_ReadWriteLock");  //读写锁
             * readWriteLock.writeLock().lock(30, TimeUnit.SECONDS);    //写锁上锁
             * ops.set("CatalogJson", JSON.toJSONString(finale), 300 + (new Random().nextInt(150)), TimeUnit.SECONDS); //数据存入redis
             * readWriteLock.writeLock().unlock();         //写锁解锁*/
        } finally {
            lock.unlock();      //解锁进程
        }
        System.out.println("获取到数据库中数据，是否为空？"+!finale.isEmpty());
        return finale;

    }





    /**
     *
     * @return
     *
     * 使用redis改写的新·首页三级分类方法
     */
    @Cacheable(value = {"product-category"},key="'CatalogJson'")
    @Override
    public Map<String, List<Catalog2VO>> getCatalogJson() {
        /**
         * 1.从redis中拿取数据
         * 2.判断数据是否为空
         * 3.1.若为空，则从数据库中调取，并存入redis
         * 3.2.若不为空，则无需查库，直接返回
         */
        RReadWriteLock readWriteLock=redissonClient.getReadWriteLock("CatalogJson_ReadWriteLock");      //读写锁
        ValueOperations<String,String> ops= redisTemplate.opsForValue();        //让连接器创建一个操作杠杆，用于直接操作redis

        readWriteLock.readLock().lock(30,TimeUnit.SECONDS);     //读锁上锁
        String catalogJson=ops.get("CatalogJson");  //获取json字符串，redis中名为：CatalogJson
        readWriteLock.readLock().unlock();          //读锁解锁

        if(StringUtils.isEmpty(catalogJson)){               //判断catalogJson是否为空。
            System.out.println("redis中无数据，将进锁查库...");

            //Map<String, List<Catalog2VO>> finale=this.getCatalogs_LocalLock();    //进锁查数据
            //Map<String, List<Catalog2VO>> finale=this.getCatalogs_RedisLock();    //使用分布式锁
            Map<String, List<Catalog2VO>> finale=this.getCatalogs_RedissonLock();   //使用redisson分布式锁

            /**
             *
             *
             *
             * if(finale==null){   //若从数据库中获取来的也为空
             *     finale=new HashMap<>(); //直接将finale赋为空内容对象
             * }
 *
             * readWriteLock.writeLock().lock(30,TimeUnit.SECONDS);        //写锁上锁
             * ops.set("CatalogJson",JSON.toJSONString(finale),300+(new Random().nextInt(150)), TimeUnit.SECONDS);     //设置过期时间，标准过期时间300s，在此基础上加上0-149秒的随机时间
             * readWriteLock.writeLock().unlock();     //写锁解锁*/

            return finale;
        }
        else{
            System.out.println("redis中有数据，直接返回");
            Map<String, List<Catalog2VO>> finale=JSON.parseObject(catalogJson, new TypeReference<Map<String, List<Catalog2VO>>>(){});               //若不为空，则直接将redis中获取的json字符串反序列化为对象，返回对象
            return finale;
        }
    }
}