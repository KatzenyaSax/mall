<!--
 *                   江城子 . 程序员之歌
 * 
 *               十年生死两茫茫，写程序，到天亮。
 *                   千行代码，Bug何处藏。
 *               纵使上线又怎样，朝令改，夕断肠。
 * 
 *               领导每天新想法，天天改，日日忙。
 *                   相顾无言，惟有泪千行。
 *               每晚灯火阑珊处，夜难寐，加班狂。
 * 
 *
 *
 *                        _oo0oo_
 *                       o8888888o
 *                       88" . "88
 *                       (| -_- |)
 *                       0\  =  /0
 *                     ___/`---'\___
 *                   .' \\|     |// '.
 *                  / \\|||  :  |||// \
 *                 / _||||| -:- |||||- \
 *                |   | \\\  - /// |   |
 *                | \_|  ''\---/''  |_/ |
 *                \  .-\__  '-'  ___/-. /
 *              ___'. .'  /--.--\  `. .'___
 *           ."" '<  `.___\_<|>_/___.' >' "".
 *          | | :  `- \`.;`\ _ /`;.`/ - ` : | |
 *          \  \ `_.   \_ __\ /__ _/   .-` /  /
 *      =====`-.____`.___ \_____/___.-`___.-'=====
 *                        `=---='
 * 
 * 
 *      ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * 
 *            佛祖保佑     永不宕机     永无BUG
 * 
 *        佛曰:  
 *                写字楼里写字间，写字间里程序员；  
 *                程序人员写程序，又拿程序换酒钱。  
 *                酒醒只在网上坐，酒醉还来网下眠；  
 *                酒醉酒醒日复日，网上网下年复年。  
 *                但愿老死电脑间，不愿鞠躬老板前；  
 *                奔驰宝马贵者趣，公交自行程序员。  
 *                别人笑我忒疯癫，我笑自己命太贱；  
 *                不见满街漂亮妹，哪个归得程序员？
 -->




# springboot版本：3.1.3























# 关于数据库

使用linux环境部署






# docker


## docker安装

I.安装

            1.网址：https://zhuanlan.zhihu.com/p/143156163
    
            2.运行docker：

                    systemctl start docker;

            3.查看docker状态（注意该窗口会阻塞）：

                    systemctl status docker

            4.查看docker现有镜像：

                    docker images

            5.设置docker开机自启动：

                    systemctl enable docker
            
            6.至此安装完成





II.配置docker阿里云镜像下载地址

            阿里云镜像加速器，按顺序执行以下操作：

                    sudo mkdir -p /etc/docker
                    sudo tee /etc/docker/daemon.json <<-'EOF'
                    {
                    "registry-mirrors": ["https://e6jcuw6y.mirror.aliyuncs.com"]
                    }
                    EOF
                    sudo systemctl daemon-reload

[参考链接](https://developer.aliyun.com/article/1245481?spm=a2c6h.14164896.0.0.1a9a47c5JPNQiV&scm=20140722.S_community@@%E6%96%87%E7%AB%A0@@1245481._.ID_1245481-RL_docker%E9%98%BF%E9%87%8C%E4%BA%91%E9%95%9C%E5%83%8F-LOC_search~UND~community~UND~item-OR_ser-V_3-)

                    














## docker安装mysql
            

I.安装

            docker pull mysql

默认安装最新版mysql.

            





II.创建mysql实例并运行.

            docker run -itd --name mysql-test -p 3306:3306 -v /mydata/mysql/log:/var/log/mysql -v /mydata/mysql/data:/var/data/mysql -v /mydata/mysql/conf.d:/etc/mysql.d -e MYSQL_ROOT_PASSWORD=123456 mysql

此时，外部服务器可以通过3306端口访问mysql，其密码为123456.
将mysql的日志log挂载到/mydata/mysql/log/mysql.
将mysql的数据data挂载到/mydata/mysql/data/mysql.
将mysql的配置文件conf.d挂载到/mydata/mysql/conf.d.



III.连接远程数据库.
由于本虚拟机ip地址为192.168.74.130，故远程连接时的ip地址要选用此ip.
    
            192.168.74.130:3306
            root
            123456

IV.在linux用docker命令进入mysql.

            docker exec -it [ID] /bin/bash

其中-it表示交互方法.
[ID]为该容器id开头三个字母.
/bin/bash表示进入mysql的内部.

随后便可进入mysql的内部文件夹：
            
            /var/mysql/log
            /var/mysql/data
            /etc/mysql.d



V.在挂载文件夹写入配置文件my.cnf:

            [mysqld]
            user=mysql
            character-set-server=utf8
            default_authentication_plugin=mysql_native_password
            secure_file_priv=/var/lib/mysql
            expire_logs_days=7
            sql_mode=STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION
            max_connections=1000
            ​
            [client]
            default-character-set=utf8
            ​
            [mysql]
            default-character-set=utf8

主要是把编码格式改为utf8.
配置过后要重启容器.


进入mysql内部后，cd /etc/mysql.d
随后cat my.cnf就可以查看配置文件了.












## docker安装redis

I.直接pull redis，创建实例并运行：

            docker run -itd --name redis-test -p 6379:6379 -v /mydata/redis/data:/data -v /mydata/redis/conf/redis.conf:/etc/redis/redis.conf -d redis redis-server /etc/redis/redis.conf

此时，redis的端口6379映射到docker的6379，/data和redis.conf映射到指定位置.
-d代表后台运行，以redis镜像文件启动redis服务，并加载后面的配置文件.




II.进入redis内部，然后可以使用redis的指令.

            docker exec -it redis redis-指令



III.持久化
在redis.conf里加上一行：

            appendonly yes

随后要重启redis
此时redis设置的数据，可以直接持久化.






IV.连接
使用的ip是虚拟机的ip地址.







# git

安装git

在git bash中配置：

            git config --global user.name "InnerSekiro"
            git config --global user.email "a18290531268@163.com"
            
生成免密连接密钥：

            ssh-keygen -t rsa -C "a18290531268@163.com"

密钥位置会给出提示.







# 搭建微服务总框架



## 创建微服务模块

最先要导入的是：Web/SpringWeb、SpringCloudRoutine/OpenFeign.
所有模块的父包名（组织名）都应该是：com.katzenyasax.mall.

要创建的模块有：

            product：商品
            order：订单
            member：人员
            coupon：优惠券
            ware：

注意外面应当有一个大型mall模块聚合上述所有模块.
可以在创建之时创建一个空项目，但是选上maven，创建之后就有pom.xml了，把packaging改为pom.
然后用<module>所有模块.
注意之后要在maven里添加mall作为总服务，此时mall会包含所有微服务.




在总服务的.gitignore添加：

            **/.mvn
            **/mvnw
            **/mvnw.cmd
            **/target/
            .idea
            **/.gitignore

表示上传git时忽略这些无用文件








## 数据库初始化 

连接docker部署的mysql，都创建数据库：

            mall_oms：order
            mall_pms：product
            mall_sms：coupon
            mall_ums：member
            mall_wms：ware

ms为manage system的缩写







## 使用开源项目模板创建后台管理系统

人人开源：renren-fast、renren-fats-vue、renren-generator

renren-fast删掉.git，直接加入mall的包下，并在mall的pom.xml里加入module.
根据fast目录下db里的sql创建数据库：

            mall_admin

在application.yml中设置默认环境为test，并将application-test.yml的连接设置做好.
比如url、username、password啥的：

            driver-class-name: com.mysql.cj.jdbc.Driver
            url: jdbc:mysql://192.168.74.128:3306/mall_admin?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai
            username: root
            password: 123456



运行人人fast的启动类，在浏览器输入localhost:8080/renren-fast，应该会返回：

            {"msg":"invalid token","code":401}

表示运行成功了.








renren-fast-vue，前端工程用vscode打开

安装node.js
配置npm淘宝镜像：

            npm config set registry http://r.cnpmjs.org/

下载会快很多.
然后到vscode控制台终端（因为是首次运行vue项目），安装npm：

            先安装chormedriver：npm install chromedriver --chromedriver_cdnurl=http://cdn.npm.taobao.org/dist/chromedriver
            再安装node-sass：npm install node-sass
            最后安装剩余依赖：npm install

他是根据前端文件下package.json的目录下载的.
随后运行：

            npm run dev

会自动打开一个8001端口的浏览器，并显示：

            <% if (process.env.NODE_ENV === 'production') { %> <% }else { %> <% } %>



要点：

            1.node.js：10.16.3
            2.node-sass：4.9.2
            3.sass-loader：7.3.1

            4.先安装chormedriver：npm install chromedriver --chromedriver_cdnurl=http://cdn.npm.taobao.org/dist/chromedriver
            5.再安装node-sass：npm install node-sass
            6.最后安装剩余依赖：npm install







## 逆向工程

克隆renren-fast-generator，删除.git，导入mall总服务，标记为模块.
加入mall的pom.xml中
注意再generator的pom文件中，parent springboot工程下添加：

            <relativePath/> <!-- lookup parent from repository -->
            


随后根据不同的数据库生成不同的代码，例如生成mall_pms的代码.
首先更改application.yml文件：

            driver-class-name: com.mysql.cj.jdbc.Driver
            url: jdbc:mysql://192.168.74.128:3306/mall_pms?useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=Asia/Shanghai
            username: root
            password: 123456

随后在generator.properties中配置：

            mainPath=com.katzenyasax            主包名
            package=com.katzenyasax.mall        包名
            moduleName=product                  模块名，因为mall_pms是product模块的数据库
            author=KatzenyaSax                  作者名
            email=a18290531268@163.com          邮箱
            tablePrefix=pms_                    表前缀，加上就是了

随后运行，比较慢.

springboot遇到循环依赖问题而无法启动时：
在yml文件中添加：

            spring:
                main:
                    allow-circular-references: true



生成后下载文件，直接把文件里/main的java和resource复制到项目的product里.

但是下载的包里面好多依赖都没有，因此创建一个公共的类，其作用应当是为所有微服务提供公共依赖.
创建maven模块：mall-common.

因此此时的mall-product需要依赖mall-common：

            <dependency>
                <groupId>com.katzenyasax</groupId>
                <artifactId>mall-commom</artifactId>
                <version>0.0.1-SNAPSHOT</version>
            </dependency>

然后到common里加上公共依赖.
目前已知所需的公共依赖为：

            mybatis-plus
            lombok

随后在common模块加入com.katzenyasax.common.utils.R等工具包：
从renren fast复制需要的工具包，需要的是Query、R和PageUtils.
复制过去后不再报错.
            






测试一下，

            1.测试之前还要在common中加入mysql驱动的依赖

                    <!-- https://mvnrepository.com/artifact/mysql/mysql-connector-java -->
                    <dependency>
                        <groupId>mysql</groupId>
                        <artifactId>mysql-connector-java</artifactId>
                        <version>8.0.33</version>
                    </dependency>


              注意一下，用了mybatis plus就不能再用mybatis的任何依赖了
              除此之外，一定要注意，只需要mybatis-plus-boot-starter这个包，也即是：

                    <!-- https://mvnrepository.com/artifact/com.baomidou/mybatis-plus-boot-starter -->
                    <!-- 这个是controller里的getById等方法要用的，也是mybatis plus的核心依赖 -->
                    <!-- 同时处理"Property 'sqlSessionFactory' or 'sqlSessionTemplate' are required"的问题 -->
                    <!-- 出现找不到xml中方法的情况时，第一时间应想到是否只依赖了该包 -->
                    <dependency>
                        <groupId>com.baomidou</groupId>
                        <artifactId>mybatis-plus-boot-starter</artifactId>
                        <version>3.5.3.2</version>
                    </dependency>

              因为这个包是mybatis-plus包的强化版
    


            2.在product模块的resource下面添加application.yml:

                    spring:
                        datasource:
                            username: root
                            password: 123456
                            driver-class-name: com.mysql.cj.jdbc.Driver
                            url: jdbc:mysql://192.168.74.128:3306/mall_pms
    
            3.在product的启动类上加上注解MapperScan：
    
                    @MapperScan("com.katzenyasax.mall.product.dao")

            4.在application.yml配置mybatis plus：

                    mybatis-plus:
                        mapper-locations: classpath*:/mapper/**/*.xml

            5.yml中增加主键自增：

                    mybatis-plus:
                        global-config:
                            db-config:
                                id-type: auto

            6.开始测试，使用@Test注解进行

                    @Autowired
                    BrandService brandService;
                    @Test
                    void test01(){
                        BrandEntity brandEntity=new BrandEntity();
                        brandEntity.setName("华为！");
                        brandService.save(brandEntity);
                        System.out.println("保存成功");
                
                    }

              运行后，pms_brand里面多出一项：

                    1,华为！,,,,,

              即表示成功运行














之后对其他模块也进行逆向工程.
基本上就是在yml中添加：

            spring:
                datasource:
                    username: root
                    password: 123456
                    driver-class-name: com.mysql.cj.jdbc.Driver
                    url: jdbc:mysql://192.168.74.128:3306/数据库
                
                mybatis-plus:
                    mapper-locations: classpath*:/mapper/**/*.xml
                    global-config:
                        db-config:
                            id-type: auto

然后启动类添加@MapperScan.

            @MapperScan("com.katzenyasax.mall.模块名.dao")








运行：


coupon：

            http://localhost:6600/coupon/coupon/list

            {
                "msg": "success",
                "code": 0,
                "page": {
                    "totalCount": 0,
                    "pageSize": 10,
                    "totalPage": 0,
                    "currPage": 1,
                    "list": []
                }
            }


member:

            http://localhost:7700/member/member/list

            {
                "msg": "success",
                "code": 0,
                "page": {
                    "totalCount": 0,
                    "pageSize": 10,
                    "totalPage": 0,
                    "currPage": 1,
                    "list": []
                }
            }

如上.
注意不要用7000、8000等作为端口号，浏览器会认为这是不安全的端口而自动屏蔽.








# Spring Cloud

使用spring cloud alibaba.

主要使用：

            Alibaba         Nacos           注册中心、配置中心
            Spring          Ribbon          负载均衡
            Spring          Feign           声明式HTTP客户端，远程调用
            Alibaba         Sentinel        限流、降级、熔断
            Spring          Gateway         网关
            Spring          Sleuth          调用链监控
            Alibaba         Seata           分布式解决案

在common引入依赖：

           <dependencyManagement>
                <dependencies>
                    <dependency>
                        <groupId>com.alibaba.cloud</groupId>
                        <artifactId>spring-cloud-alibaba-dependencies</artifactId>
                        <version>2022.0.0.0</version>
                        <type>pom</type>
                        <scope>import</scope>
                    </dependency>
                </dependencies>
            </dependencyManagement>

这是依赖管理，以后用这个包的组件不用再指定版本号








# nacos 


## 配置nacos

            1.引入nacos依赖：

                    <!-- 引入 SpringMVC 相关依赖，并实现对其的自动配置 -->
                    <dependency>
                        <groupId>org.springframework.boot</groupId>
                        <artifactId>spring-boot-starter-web</artifactId>
                        <version>3.1.3</version>
                    </dependency>
                    <!-- 引入 Spring Cloud Alibaba Nacos Discovery 相关依赖，将 Nacos 作为注册中心，并实现对其的自动配置 -->
                    <dependency>
                        <groupId>com.alibaba.cloud</groupId>
                        <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
                    </dependency>

            2.下载nacos server
              可以部署在docker上
            
                2.1.安装：
    
                        docker pull nacos/nacos-server:v2.1.1

                2.2.创建网络容器

                        docker network create nacos_network

                2.3.启动

                        docker run --name nacos -e NACOS_AUTH_ENABLE=true -d -p 9848:9848 -p 8848:8848 -p 9849:9849 --network nacos_network -e MODE=standalone nacos/nacos-server:v2.1.1

    
                  通过名为nacos_network的网络容器，在端口8848上开启了一个nacos服务器，容器模式为standable
                  注意，虚拟机的ip地址仍为：192.168.74.130

                2.4.开启鉴权

                        docker exec -it ID /bin/bash
        
                        cd /home/nacos/conf

                        vim application.properties

                    在application.properties加入：

                        


                2.5.远程开启nacos客户端
                    在浏览器访问：    

                        http://192.168.74.128:8848/nacos

                2.6.将所有微服务加入配置中心
                    在yml文件中添加：

                        spring:
                            cloud:
                                nacos:
                                  discovery:
                                    server-addr: 192.168.74.128:8848                        这是连接nacos中心的地址
                                    password: nacos                                         
                                    username: nacos
                                    namespace: 311853ea-26c0-46e5-83e9-5d5923e1a333         切记这里应当是命名空间的id，而不是名称
                            application:
                                name: mall-coupon                                           该服务的名称，这个不加上nacos不过注册



                2.7.在所有微服务启动类加入注解：

                        @EnableDiscoveryClient

                    表示允许客户端发现该服务







注意开启linux的8848端口：

            sudo ufw allow 8848/tcp








## open feign 微服务间远程调用

依赖为spring-cloud-open-feign，创建项目时引入就行
还要引入一个依赖：

            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-starter-loadbalancer</artifactId>
                <version>3.1.3</version>
            </dependency>
    
这个是新版本必须的依赖，是nacos的负载均衡

原理就是在一个微服务内通过注解指定要调用的是哪个微服务的哪个方法，指定之后交给open feign让其实现远程调用就可以了
例如我创建一个TEST服务用于进行测试，这个TEST需要被注册进nacos，和要调用的微服务处于同一命名空间
也即是在其yml中添加：

            server:
                port: 4869
            spring:
                cloud:
                    nacos:
                        discovery:
                            server-addr: 192.168.74.128:8848
                            username: nacos
                            password: nacos
                            namespace: 311853ea-26c0-46e5-83e9-5d5923e1a333
                application:
                    name: mall-TEST

并且启动类上还要加上
    
            @EnableDiscoveryClient

注意TEST不需要数据库，因此在@SpringBootApplication加上：

            (exclude= {DataSourceAutoConfiguration.class})

即排除MybatisPlus强制要求连接数据源的傻逼规定


调用流程为：

            1.创建feign接口，定义在包下的feign子包内：

                    @FeignClient("mall-coupon")
                    public interface Feign_ShowCoupon {
                        @RequestMapping("/coupon/coupon/")
                        public String show();
                    }

              其中@FeignClient中的是，在nacos注册中心中的服务名，注意不是项目内微服务的名称
              而@RequestMapping内的则是要调用的方法的全路径名

            2.在TEST启动类上打开open feign功能，即添加注解：

                    @EnableFeignClients(basePackages = "com.katzenyasax.test.feign")

              括号内的是feign包的位置

            3.定义controller，远程调用方法

                @RestController
                public class Controller_ShowCoupon {
                    @Resource
                    Feign_ShowCoupon showCoupon;
                    @RequestMapping(value = "/showCoupon")
                    public R GetAllCoupons(){
                        return R.ok(showCoupon.show());
                    }
                }
                
            4.测试：
              输入 localhost:4869/showCoupon，浏览器返回：

                    {"msg":"Coupon","code":0}

              测试成功














## nacos 作为配置中心

引入依赖：

            <!-- 将 Nacos 作为注册配置中心 -->
            <dependency>
                <groupId>com.alibaba.cloud</groupId>
                <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
            </dependency>

流程：

            1.在微服务的resource加入bootstrap.yml
              比application.yml的优先级更高，优先读取
              还要添加读取bootstrap的依赖：

                    <!-- https://mvnrepository.com/artifact/org.webjars/bootstrap -->
                    <!-- https://mvnrepository.com/artifact/org.springframework.cloud/spring-cloud-starter-bootstrap -->
                    <dependency>
                        <groupId>org.springframework.cloud</groupId>
                        <artifactId>spring-cloud-starter-bootstrap</artifactId>
                        <version>4.0.0</version>
                    </dependency>


            2.在bootstrap中添加：
                        
                    spring:
                        application:
                            name: 注册中心中的服务名
                        cloud:
                            nacos:
                                config:
                                    server-addr: 192.168.74.128:8848
                                            username: nacos
                                            password: nacos
                                            namespace: 311853ea-26c0-46e5-83e9-5d5923e1a333

              注意config，不要写出discovery了
              而且特别注意的是，bootstrap中写过config了，application了就不能再写了，
              也就是discovery和config要分开
              否则@RefreshScope动态刷新不生效
              name也必须写上

            3.在bootstrap中添加：

                    a:
                        b:
                            c:
                                114514

            4.测试：

                    @RestController
                    public class Controller_ShowDataInBootstrap {
                        @Value("${a.b.c}")
                        String data;
                        @RequestMapping("/showData")
                        public R showDataInBootstrap(){
                            return R.ok(data);
                        }
                    }

              结果为：

                    {"msg":"114514","code":0}

结果证明是可以正常读取的
但是这种情况下，每一次更高配置文件都要求重新上线服务，很不方便，因此使用nacos的配置中心
springboot启动类运行后，会给出对应的服务在nacos配置中心上对应的配置文件的名称
在nacos配置中心创建这个名称的配置文件，就可以使用nacos管理配置文件了

注意nacos中配置文件的名称是该服务在nacos中的名称，后面加上yml或properties的后缀
例如TEST模块在nacos中的服务名为mall-TEST，那么配置文件的名称就应该是mall-TEST.properties

除此之外，要实现实时刷新，还需要在调用nacos配置文件的类上方打上注解@RefreshScope：

整个过程可以分为以下几点：

            1.加依赖

            2.加bootstrap，里面配置config
              application中的config删除

            3.调用nacos配置文件的类打上注解@RefreshScope
    
            4.nacos中心加配置文件，名字要和服务名一致




## nacos 配置中心细节 

I.命名空间
用来配置隔离的，例如创建开发空间、生产空间等，对应开发环境、生产环境等


II.手动加载nacos中的配置文件到微服务
   在bootstrap加上：

            spring:
                cloud:
                    nacos:
                        config:
                            extension-configs[0]:
                                data-id: 配置文件的名称
                                group: 配置文件的group名
                                refresh: true 是否开启实时刷新

值得一提的是，extension-config本质上是一个list，因此中括号里面的数代表该加载的配置文件在list中的位置
这意味着，可以加载多个配置文件到微服务

不过一般情况下，还是把配置文件加载过去吧，免得出现一些bug，保险一点















#  网关 

使用spring cloud提供的Gateway
基于nacos注册中心和配置中心，因此所有服务都要加入nacos

路由：网关将请求发往服务的过程

断言：网关判断请求应发往哪一个服务的行为

过滤器：网关对请求进行筛选，滤除不合法的请求



            1.创建网关服务mall-gateway
              创建时导入gateway依赖
              pom中依赖mall-common

            2.开启注册发现和nacos配置
              application上加：

                    server:
                        port: 10100
                    spring:
                        cloud:
                            nacos:
                                discovery:
                                    server-addr: 192.168.74.128:8848
                                        username: nacos
                                        password: nacos
                                        namespace: 311853ea-26c0-46e5-83e9-5d5923e1a333
                        main:
                            web-application-type: reactive
                        application:
                            name: mall-Gateway

              其中，spring.main.web-application-type: reactive是定常

              其次注意Gateway不需要数据库，因此在@SpringBootApplication加上：
              
                    (exclude= {DataSourceAutoConfiguration.class})
                    jdk1.8使用：(exclude= {DataSourceAutoConfiguration.class, DruidDataSourceAutoConfigure.class})
              
              即排除MybatisPlus自动配置连接池，强制要求连接数据源的傻逼规定

            3.我们要实现一个功能
              输入路径/bilibili时，页面跳转到bilibili官网
              在application中添加：

                    spring:
                     cloud:
                      gateway:
                       routes:
                        - id: "gateway-test-bilibili"
                          uri: "https://www.bilibili.com"
                          predicates:
                           - Path=/bilibili

              
            4.测试
              运行后，可以发现，实际上我们跳转到的地址是：

                    https://www.bilibili.com/bilibili

              也就是说实际上跳转到的是导向的uri，和填写的路径的拼接值

            5.不要断言：

                              predicates:
                                - Path=/*

              但不是说不要，断言是必须要有的，只不过我们可以在逻辑层面规定实际上是否需要断言
              那就直接跳转到了首页
              不过也有缺点，那就是首页没有图片等资源













# 商品服务I：分类


## 方法实现 

首先导入数据到pms_category，表示商品的分类
商品种类分为3级，要求查出所有的分类，并根据父子关系进行组装

product服务的CategoryController中，没有对应的方法，因此自己定义一个：

            @RequestMapping("/list/tree")
            public R listTree(){}

我们已经知道了如何查出所有的分类，也就是对应的service中的list方法，但是我们希望的是得到以父子关系组织好了的结果
所以我们应该在service里定义一个方法listAsTree()，这样一来我们只需要在listTree中返回R的ok()方法得到的数据就行了：

            @RequestMapping("/list/tree")
            public R listTree(){
                List<CategoryEntity> categoryEntitiescategoryService.listAsTree();
                return R.ok().put("success", categoryEntities);
            }

所以我们应该在service的接口内添加方法：

            List<CategoryEntity> listAsTree();

随后在实现类中重写该方法：

            @Override
            public List<CategoryEntity> listAsTree() {
                return null;
            }

该方法功能是：     1.查出所有分类
                  2.组装

首先是查出所有分类，我们已经有自动生成的service提供的方法了
而且我们在重写方法时，不需要再手动注入mapper，因为实现类CategoryServiceImpl继承了ServiceImpl时，对左边的泛型传入了类型CategoryDao
且ServiceImpl声明了该泛型为baseMapper，其类型也就是传入的CategoryDao，也就是我们需要的mapper
所以我们直接使用继承下来的baseMapper就可以，不需要再@Autowired一个
故查出所有分类的方法就是：

            //查出所有分类
            List<CategoryEntity> entities=baseMapper.selectList(null);

接下来进行父子分类
首先应当说明的是，pms_category中的所有类别，第一项数字是其父，第二项数字是层级
重点就是父的数字，它严格对应表上的id，数字为0代表是最高层级

获取一级分类：

            //获取一级子类
            Stream<CategoryEntity> one=entities.stream();
            List<CategoryEntity> oneCategory=one.filter(categoryEntity -> categoryEntity.getCatLevel()==1).collect(Collectors.toList());

注意stream是线程安全的，他只能被用一次，不管是进行过滤还是转化成集合
我们得到了第一层级的分类，如何存储第二层级的分类？
最简单的方法是直接在bean类加上成员变量，一个集合，用来存储其所有子类:

            //所有子类
	        @TableField(exist = false)
	        private List<CategoryEntity> children=new ArrayList<>();

最终方法为：

            @Override
            public List<CategoryEntity> listAsTree() {
                //查出所有分类
                List<CategoryEntity> entities=baseMapper.selectList(null);
                //组装父子
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
                }
                //从二级子类开始遍历，将二级子类组装到一级子类
                for(CategoryEntity category2:twoCategory){
                    for(CategoryEntity category1:oneCategory){
                        if(category2.getParentCid()==category1.getCatId()){
                            category1.getChildren().add(category2);
                        }
                    }
                }
                return oneCategory;
            }

最终传输到前端的数据格式为：

            {
                "msg": "success",
                "code": 0,
                "success": [
                    {
                        "catId": 1,
                        "name": "图书、音像、电子书刊",
                        "parentCid": 0,
                        "catLevel": 1,
                        "showStatus": 1,
                        "sort": 0,
                        "icon": null,
                        "productUnit": null,
                        "productCount": 0,
                        "children": [
                            {
                                "catId": 22,
                                "name": "电子书刊",

                        ······
                            }
                        ]
                    }
                ]
            }

格外注意，msg、code只是通信状况的反馈，success内才是正确的数据








##  网关配置 

首先登录renren fast vue的管理界面
注意要在vscode中打开，不要用cmd打开
否则登不上

创建一个一级目录：商品系统
会发现数据库表：sys_menu多出一项：

            31,0,商品系统,"","",0,editor,1

再在此目录创建一个菜单：商品分类
我们想要在此处实现的功能是：展示所有的商品分类，一级、二级、三级
url设置为：/product/category
该url的完整路径应该为：

            localhost:8800/product/category

请求路径上，系统会自动将其修改为product-category
所以我们要在前端工程src/views/modules下面创建：/product/category.vue
使用以下模板：

            {
                "Print to console": {
                    "prefix": "vue",
                    "body": [
                        "<!-- $1 -->",
                        "<template>",
                        "<div class='$2'>$5</div>",
                        "</template>",
                        "",
                        "<script>",
                        "//这里可以导入其他文件（比如：组件，工具js，第三方插件js，json文件，图片文件等等）",
                        "//例如：import 《组件名称》 from '《组件路径》';",
                        "",
                        "export default {",
                        "//import引入的组件需要注入到对象中才能使用",
                        "components: {},",
                        "data() {",
                        "//这里存放数据",
                        "return {",
                        "",
                        "};",
                        "},",
                        "//监听属性 类似于data概念",
                        "computed: {},",
                        "//监控data中的数据变化",
                        "watch: {},",
                        "//方法集合",
                        "methods: {",
                        "",
                        "},",
                        "//生命周期 - 创建完成（可以访问当前this实例）",
                        "created() {",
                        "",
                        "},",
                        "//生命周期 - 挂载完成（可以访问DOM元素）",
                        "mounted() {",
                        "",
                        "},",
                        "beforeCreate() {}, //生命周期 - 创建之前",
                        "beforeMount() {}, //生命周期 - 挂载之前",
                        "beforeUpdate() {}, //生命周期 - 更新之前",
                        "updated() {}, //生命周期 - 更新之后",
                        "beforeDestroy() {}, //生命周期 - 销毁之前",
                        "destroyed() {}, //生命周期 - 销毁完成",
                        "activated() {}, //如果页面有keep-alive缓存功能，这个函数会触发",
                        "}",
                        "</script>",
                        "<style scoped>",
                        "//@import url($3); 引入公共css类",
                        "$4",
                        "</style>"
                    ],
                    "description": "生成vue模板"
                },
                "http-get请求": {
            	"prefix": "httpget",
            	"body": [
            		"this.\\$http({",
            		"url: this.\\$http.adornUrl(''),",
            		"method: 'get',",
            		"params: this.\\$http.adornParams({})",
            		"}).then(({ data }) => {",
            		"})"
            	],
            	"description": "httpGET请求"
                },
                "http-post请求": {
            	"prefix": "httppost",
            	"body": [
            		"this.\\$http({",
            		"url: this.\\$http.adornUrl(''),",
            		"method: 'post',",
            		"data: this.\\$http.adornData(data, false)",
            		"}).then(({ data }) => { });" 
            	],
            	"description": "httpPOST请求"
                }
            }

在vscode中，设置成vue.js的模板
随后根据模板生成category.vue
便可以操作前端页面

为了显示树形结构，在element:https://element.eleme.cn 中查找树形结构，
否则el-tree替换div
复制粘贴，替换data和methods
运行后发现结构正确

删除data的数据，置为空，接下来要导入真正的数据


在method定义方法：

            getMenus(){
              this.$http({
                  url: this.$http.adorn('product/category/list/tree'),
                  method: get
              }).then(data=>{
                  console.log("成功获取到数据...",data)
              })
            }

并且在周期方法created中，直接this调用该方法



但是此时该方法有缺陷，那就是他访问的地址默认为：8080/renren-fast
这显然不是我们想要的地址，我们要的地址是localhost:8800/product/category
因此我们应该修改
并且为了满足以后访问更多端口的服务时，我们直接让其地址指向我们设定的网关mall-Gateway：localhost:10100/api
在前端中：static/config/index.html中修改地址：

              window.SITE_CONFIG['baseUrl'] = 'http://localhost:10100/api';

其中api表示这是前端发送来的请求，用于在请求层面区分是前端请求和后端请求，后期会将其抹除
这样运行后，请求直接发给了网关，网关直接跳转到其他页面，而没有经过renren-fast，故刷新页面后会被弹出登录，甚至都没有验证码，导致永远无法登录
对此我们的策略是：请求经过网关时，我们默认让请求转给renren-fast，故我们需要让renren-fast被nacos配置中心发现

配置renren-fast注册：

            1.引入依赖：
    
                    <!-- 引入 Spring Cloud Alibaba Nacos Discovery 相关依赖，将 Nacos 作为注册中心 -->
		            <dependency>
		            	<groupId>com.alibaba.cloud</groupId>
		            	<artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
                        <version></version>
		            </dependency>
            
		            <!-- spring cloud 2020版本以后，负载均衡就成了这个包 -->
		            <!-- 要实现springboot整合nacos、open feign，这个包是必须的 -->
		            <dependency>
		            	<groupId>org.springframework.cloud</groupId>
		            	<artifactId>spring-cloud-starter-loadbalancer</artifactId>
		            	<version>3.1.3</version>
		            </dependency>

              一定要考虑负载均衡
              除此之外，还要考虑每个依赖的版本兼容问题
              SpringBoot: 2.6.6
              Alibaba Cloud: 2021.0.1.0
              

            2.再在application.yml中添加：

                    spring
                        cloud:
                            nacos:
                                config:
                                    server-addr: 192.168.74.128:8848
                                    username: nacos
                                    password: nacos
                                    namespace: 311853ea-26c0-46e5-83e9-5d5923e1a333
                        application:
                            name: mall-Admin

            2.随后在启动类加上：

                    @EnableDiscoveryClient

            3.重启后就可以被发现了

配置网关，前端请求api默认路由到renren-fast:

                    - id: admin-route
                      uri: http://localhost:8080
                      predicates:
                        - Path= /api/**
                          filters:
                            - RewritePath=/api(?<segment>/?.*),/renren-fast/$\{segment}

注意要用http，不能用https
此时便可以查找到验证码了

首先必须明白，我们的页面是前后联调的，前端会指定一条url指向对应的后端，以此实现前后联调，而且那个url也就是index.js里面的那个

所以整体的运作过程是：我们输入的localhost:8001是前端页面，原本会通过index.js里的url发送一条请求，该请求直接指向后端localhost:8080/renren-fast
但是我们对其进行更改，发送的请求指向了网关localhost:10100/api
网关的逻辑是：将/api/xxxx转化为/renren-fast/xxxx，拼接到指定的uri也就是localhost:8080
因此会跳转到localhost:8080/renren-fast/xxxx
而我们设置的请求，api后面没有，因此实际上最终跳转的请求为：

            localhost:8080/renren-fast

也就是最开始，前端默认指向的后端地址

不过这样一来会发现，依然无法登录，这是跨域问题未解决













## 跨域

就是不能使用另一个网站的，非简单方法
因为前端登录界面，它发出的任何请求都是我们设定的：http://localhost:10100/api
但是我们不管是要读取数据还是直接访问的后端的域名，实际上为：http://localhost:8080
二者域名不同，因此会出现无法登录的情况

不过也好解决，过程就是：

            1.浏览器发出一条Option的请求到目标服务器

            2.服务器反馈浏览器是否允许访问、或使用方法

            3.浏览器获得许可后，发出真实请求

            4.服务器根据请求返回数据

可以使用nginx解决跨域，但是有点麻烦
实际上我们只需要认为使服务器允许option就可以了
首先应该想到的是网关，在网关里直接处理所有请求，直接放行就可以了:
在网关创建配置类：

            @Configuration
            public class Mall_CorsConfiguration {
                @Bean
                public CorsWebFilter corsWebFilter(){
                    UrlBasedCorsConfigurationSource source=new UrlBasedCorsConfigurationSource();
                    CorsConfiguration corsConfiguration=new CorsConfiguration();
                    //配置跨域
                    corsConfiguration.addAllowedHeader("*");            //允许跨域的请求头
                    corsConfiguration.addAllowedMethod("*");            //允许跨域的请求方式
                    corsConfiguration.addAllowedOriginPattern("*");            //允许跨域的
                    corsConfiguration.setAllowCredentials(true);        //允许携带cookie跨域
                    source.registerCorsConfiguration("/**",corsConfiguration);
                    //表示在上述配置下，允许任意请求跨域
                    return new CorsWebFilter(source);
                }
            }

然后把renren-fast配置的跨域：src.main.java.io.renren.config.CorsConfig.java，注释掉接下来
然后重启管理系统和网关

这样就可以登陆进去了




同时，以后前端发送的所有请求：http://localhost:10100/api/
都相当于是：http://localhost:8080/renren-fast/
二者为同等效力
















## 树形分类的前端展示 

在网关加入：

                    - id: product-route
                    uri: http://localhost:8800
                    predicates:
                      - Path=/api/product/**
                    filters:
                      - RewritePath=/api/(?<segment>/?.*),/$\{segment}

并在mall-Product的bootstrap中配置跨域，使用网关中的配置文件
此时重启gateway，访问：

            http://localhost:10100/api/product/category/list/tree

会返回:

            {"msg":"invalid token","code":401}

表示没有令牌访问，即没有生效
原因是上面的一个/api/** 也满足条件，而且处于第一顺位，因此实际上我们的请求被它拦截了
调整一下顺序就行了，而且把它放在最后面:

                    - id: product-route
                      uri: http://localhost:8800
                      predicates:
                        - Path=/api/product/**
                      filters:
                        - RewritePath=/api/(?<segment>/?.*),/$\{segment}
                
                    - 更多id ······
            
                    - id: admin-route
                      uri: http://localhost:8080
                      predicates:
                        - Path= /api/**
                      filters:
                        - RewritePath=/api/(?<segment>/?.*),/renren-fast/$\{segment}

如此一来便可以访问到：


            {"msg":"success","code":0,"success":[{"catId":1,"name":"图书、音像、电子书刊","parentCid":0,"catLevel":1,"showStatus":1,"sort":0,"icon":null,"productUnit":null,"productCount":0,"children":[{"catId":22,"name":"电子书刊","parentCid":1,"catLevel":2,"showStatus":1,"sort":0,"icon":null,"productUnit":null,"productCount":0,"children":[{"catId":165,"name":"电子书",  ······

能够服务到数据，接下来进行数据的树形分类：

            1.在template添加：

                    <el-tree:data="menus":props="defaultProps">

            2.在export default中添加data：

                    data() {
                        return {
                          menus: [],
                          defaultProps: {
                            children: "children",
                            label: "name"
                          }
                        };
                    },

            3.methods中添加：

                    getMenus() {
                      this.$http({
                        url: this.$http.adornUrl("/product/category/list/tree"),
                        method: "get"
                      }).then(({ data }) => {
                        console.log("成功获取到菜单数据...", data.success);
                        this.menus = data.success;
                      });
                    },

              注意data是通过get方法获取的原始json数据，而data.success则是筛选其中success的部分
              因为我们传输过去的数据，格式并发msg、code、data
              而是msg、code、success
              
            4.created周期方法中调用getMenus方法：

                    this.getMenus();

            












## 树形分类的删除 

路径为：http://localhost:10100/api/product/category/delete
请求方式为post，前端传输json到后端，后端将json的数据打包为对象，再进行逻辑处理

CategoryController中已有了一个delete方法：

            @RequestMapping("/delete")
            public R delete(@RequestBody Long[] catIds){
	        	categoryService.removeByIds(Arrays.asList(catIds));
                return R.ok();
            }

它请求的是一个数组，存放需要删
除的分类的catId号
因此前端要发送的数据应该为：

            [1432,1433]

而后端反馈的数据为：

            {
                "msg": "success",
                "code": 0
            }

代表删除成功，而数据库中这两条数据确实的没有了

但是有缺陷，那就是我们不知道要删除的数据是否被引用，那么擅自删除的话可能会引发严重的错误
因此我们需要在CategoryServiceImp中自定义一个方法，这个方法应当实现：判断数据是否被引用、根据判断结果决定是否删除、反馈情况
同时，为了保留数据，我们不可能真的去对数据进行物理删除，这样一来数据就真没了
所以我们一般只会对一条数据的某个用来控制是否显示的字段进行判断，从而决定该数据是否返回到前端
而我们的数据中，show_status就表示该数据是否被显示
如下：

            @Override
            public void hideByIds(List<Long> list) {
                //TODO 1.判断数据是否被引用
                //2.隐藏数据
                //  直接删除就可以了
                baseMapper.deleteBatchIds(list);
            }

上面的//TODO表示等待解决的功能，因为我们还不知道说明功能引用了数据，所以这些功能放到以后写

第一想法是什么？肯定是根据show_status来判断是否组装进返回数据啊？那这样一来，实际上我们的操作并非delete，而是变相的update
这个想法是好的，但是手动实现也比较麻烦了

所以mybatis-plus也想到了，我们可以直接通过delete来进行变相的update，
只需要在存储在表内的数据对应的Bean的判断项，也即是CategoryEntity的showStatus打上注解：

            @TableLogic
	          private Integer showStatus;

随后在product模块的application中添加：

            mybatis-plus:
                global-config:
                    db-config:
                        logic-delete-value: 0
                        logic-not-delete-value: 1

配置全局配置，用show_status表示是否删除
0表示删除，1表示不删除

而且listTree方法中保持原样就可以了，不需要再通过show_Status判断是否装配进返回数据
因为我们在获取一二三级菜单时，使用的是baseMapper调用的select，mybatis-plus根据配置自动过滤了不显示的数据


在application加入：

            logging:
                level:
                    com.katzenyasax.mall: debug

可以在控制台输出对应的sql语句
进行测试：

            RequestBody为：[1431]

控制台输出：

            ==>  Preparing: SELECT cat_id,name,parent_cid,cat_level,show_status,sort,icon,product_unit,product_count FROM pms_category WHERE show_status=1
            ==> Parameters:
            <==      Total: 1425
            ==>  Preparing: UPDATE pms_category SET show_status=0 WHERE cat_id IN ( ? ) AND show_status=1
            ==> Parameters: 1431(Long)
            <==    Updates: 1
            ==>  Preparing: SELECT cat_id,name,parent_cid,cat_level,show_status,sort,icon,product_unit,product_count FROM pms_category WHERE show_status=1
            ==> Parameters:
            <==      Total: 1424

第一个select是listTree方法
第二个update是更新我们要删除的数据的show_status字段
第三个select为刷新页面调用的listTree方法














## 树形分类的新增 


前端请求路径：

            http://localhost:10100/api/product/category/save

方式为post
请求体为：

            {name: "test3", parentCid: 1, catLevel: 2, showStatus······

自动生成的代码就可以使用








## 树形分类的拖拽排序 


前端请求路径：

            http://localhost:10100/api/product/category/update/sort

请求方式为post
请求体为：

            [{catId: 1, sort: 0}, {catId: 2, sort: 1}, {catId: 3, sort: 2}, {catId: 4, sort: 3},…]
                    0: {catId: 1, sort: 0}
                    1: {catId: 2, sort: 1}
                    2: {catId: 3, sort: 2}
                    3: {catId: 4, sort: 3}
                    4: {catId: 5, sort: 4}
                    5: {catId: 6, sort: 5}
                    6: {catId: 1434, sort: 6, parentCid: 0, catLevel: 1}
                    7: {catId: 7, sort: 7}
                    8: {catId: 8, sort: 8}
                    9: {catId: 9, sort: 9}
                    10: {catId: 10, sort: 10}
                    11: {catId: 11, sort: 11}
                    12: {catId: 12, sort: 12}
                    13: {catId: 13, sort: 13}
                    14: {catId: 14, sort: 14}
                    15: {catId: 15, sort: 15}
                    16: {catId: 16, sort: 16}
                    17: {catId: 17, sort: 17}
                    18: {catId: 18, sort: 18}
                    19: {catId: 19, sort: 19}
                    20: {catId: 20, sort: 20}
                    21: {catId: 21, sort: 21}
                    22: {catId: 1432, sort: 22}

在controller中定义一个方法：

            //拖拽功能排序
            @RequestMapping("/update/sort")
            public R updateSort(@RequestBody CategoryEntity[] category){
                categoryService.updateBatchById(Arrays.stream(category).toList());
                return R.ok();
            }

调用的是自动生成的方法updateBatchById，批量排序










#  商品服务II.品牌


## 品牌


BrandController：关于品牌的所有handler

BrandDao：所有映射文件

BrandEntity：品牌的实体类

BrandService，BrandServiceImpl：关于品牌的所有方法



插入所有数据：pms_brand

管理系统中在商品服务下添加品牌管理

路径为：/product/brand

使用自动生成的vue文件，注意把getDataList()加入到created()生命周期方法中




设置品牌显示
请求路径：

            http://localhost:10100/api/product/brand/update/status

数据格式：

            {brandId: 14, showStatus: 0}
            brandId: 14
            showStatus: 0

定义一个controller：

            //修改显示状态
            @RequestMapping("/update/status")
            @RequiresPermissions("product:brand:update")
            public R updateStatus(@RequestBody BrandEntity brand){
                brandService.updateById(brand);
                return R.ok();
            }

公用update就行了，因为这是只对show_status起作用的特殊update










## 云存储服务（待实现）



让用户上传的文件存储在同一个服务中
同时还要让一些图片资源等存储进去，让前端系统自动获取文件并展示，后端则需要实现上传文件至云端的功能

配置阿里云，oss对象存储服务，创建bucket：

            katzenyasax-mall
            有地域，华北
            低频访问
            本地冗余存储
            公共读

            其他默认
            
此时该阿里云的地址为：https://kaztenyasax-mall.oss-cn-beijing.aliyuncs.com

用户登录名称 mall-K@1223293873697731.onaliyun.com
登录密码 wgv&?@OFhid8P3Ri?6WBnJYslaBXjLK9
access id：LTAI5tSMQjRn2aWaXWYYezqU
access key：4RTcGMYo6UGNGAlvoicr4bVgw3ysWH



创建一个微服务mall-third-party，用于实现第三方功能
加上spring web、open feign






整个文件上传的流程：

            1.用户在前端上传文件

            2.前端向服务器申请oss云端的密钥

            3.前端拿取密钥后，直接将文件存储在指定位置

有效防止了上传文件的操作占用服务器原本业务的资源




这种是先发到服务器，服务器再上传：

            1.引入依赖：

                    <!-- 阿里云oss依赖 -->
                    <dependency>
                        <groupId>com.aliyun.oss</groupId>
                        <artifactId>aliyun-sdk-oss</artifactId>
                        <version>3.15.1</version>
                    </dependency>
                    <!-- oss需要的依赖(jdk1.8以后才用加) -->
                    <dependency>
                        <groupId>javax.xml.bind</groupId>
                        <artifactId>jaxb-api</artifactId>
                        <version>2.3.1</version>
                    </dependency>
                    <dependency>
                        <groupId>javax.activation</groupId>
                        <artifactId>activation</artifactId>
                        <version>1.1.1</version>
                    </dependency>
                    <!-- no more than 2.3.3-->
                    <dependency>
                        <groupId>org.glassfish.jaxb</groupId>
                        <artifactId>jaxb-runtime</artifactId>
                        <version>2.3.3</version>
                    </dependency>

            2.方法：

                    @Test
                    public void upload(){
                        String endpoint = "oss-cn-beijing.aliyuncs.com";
                        // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录RAM控制台创建RAM账号。
                        String accessKeyId = "LTAI5tSMQjRn2aWaXWYYezqU";
                        String accessKeySecret = "4RTcGMYo6UGNGAlvoicr4bVgw3ysWH";
                        String bucketName = "kaztenyasax-mall";
                        // 创建OSSClient实例。
                        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
                        // 上传文件流。
                        InputStream inputStream = null;
                        try {
                            inputStream = new FileInputStream("C:\\Users\\ASUS\\Desktop\\东方project\\A38FC81C7BFD7B637F9FD6A7B9F2EDF8.jpg");
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        ossClient.putObject(bucketName, "test5.jpg", inputStream);
                        // 关闭OSSClient。
                        ossClient.shutdown();
                    }

成功上传





但是签后直传：

前端从服务器要密钥，前端直接上传

加入nacos注册中心，端口号：10200
命名空间：third

启动类加上:

            @EnableDiscoveryClient
            @SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
            jdk1.8加：@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class, DruidDataSourceAutoConfigure.class})

依赖mall-common，依赖oss相关依赖，且注意将mall-common的该依赖删除

在nacos中为ThirdParty添加配置文件oss.yaml：

            spring:
                cloud:
                    alicloud:
                        access-key: LTAI5tSMQjRn2aWaXWYYezqU
                        secret-key: 4RTcGMYo6UGNGAlvoicr4bVgw3ysWH
                        oss:
                            endpoint: oss-cn-beijing.aliyuncs.com

随后在bootstrap添加：

            spring:
                cloud:
                    nacos:
                        config:
                            extension-config[0]:
                                data-id=oss.yml
                                group=DEFAULT_GROUP
                                refresh=true                                

此时测试类中：

            @Test
            public void upload(){
                String endpoint = "oss-cn-beijing.aliyuncs.com";
                // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录RAM控制台创建RAM账号。
                String accessKeyId = "LTAI5tSMQjRn2aWaXWYYezqU";
                String accessKeySecret = "4RTcGMYo6UGNGAlvoicr4bVgw3ysWH";
                String bucketName = "kaztenyasax-mall";
                // 创建OSSClient实例。
                OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
                // 上传文件流。
                InputStream inputStream = null;
                try {
                    inputStream = new FileInputStream("C:\\Users\\ASUS\\Desktop\\东方project\\A38FC81C7BFD7B637F9FD6A7B9F2EDF8.jpg");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                ossClient.putObject(bucketName, "test100.jpg", inputStream);
                // 关闭OSSClient。
                ossClient.shutdown();
            }

依然可用，文件上传成功








但是我们要用前端传啊，后端只是给前端传密钥的，怎么传？


            




















## 品牌图片上传与显示 


将其加入网关，要达成的目的应该是：
访问：http://localhost:10100/api/thirdparty/oss/policy 时，相当于访问 http://localhost:10200/thirdoarty/oss/policy

                - id: oss-policy
                  uri: http://localhost:10200
                  predicates:
                    - Path= /api/oss/policy
                  filters:
                    - RewritePath= /api/(?<segment>/?.*),/$\{segment}

再次访问 http://localhost:10100/api/thirdparty/oss/policy  时也成功






但是在封装好的前端系统（谷粒商城文档）测试时，会显示跨域错误：

            Access to XMLHttpRequest at 'http://localhost:10100/api/thirdparty/oss/policy?t=1694670945521' from origin 'http://localhost:8001' has been blocked by CORS policy: The 'Access-Control-Allow-Origin' header contains multiple values 'http://localhost:8001, *', but only one is allowed.

因此在oss管理平台中设置跨域：

            *
            POST








失败，报跨域问题，有多个请求头
未知原因
手动控制数据库的图片url地址










## 后端数据校验 


数据提交后端后，封装为BrandEntity，其中的数据虚经过校验

在BrandEntity中的字段添加注解，@NotBlank、@NotEmpty等
复杂场景下，使用@Pattern(regexp=" ", message="")，表示不符合正则表达式时，返回报错信息：

            @TableId
	        private Long brandId;

	        @NotBlank
	        private String name;

	        @URL
	        private String logo;

	        @NotBlank
	        private String descript;

	        @NotNull
	        private Integer showStatus;

	        @NotBlank
	        private String firstLetter;

	        @NotNull
	        private Integer sort;

对于String类型来说，一般使用@NotBlank
Integer一般用@NotNull



除此之外，还要在controller参数表加上@Valid，否则单独的字段注解无效
在接收数据的controller，即save方法的参数前加上：@Valid

            @RequestMapping("/save")
            @RequiresPermissions("product:brand:save")
            public R save(@RequestBody @Valid BrandEntity brand){
	        	brandService.save(brand);
                return R.ok();
            }

当保存时，会进行校验，若数据不符合校验规则，则不起效
除此之外，可以在错误时，手动报错：

            @RequestMapping("/save")
            @RequiresPermissions("product:brand:save")
            public R save(@RequestBody @Valid BrandEntity brand, BindingResult result){
                if(result.hasErrors()){
                    Map<String,String> res=new HashMap<>();
                    result.getFieldErrors().forEach((item)->{
                       String msg=item.getDefaultMessage();
                       String fld=item.getField();
                       res.put(fld,msg);
                    });
                    return R.error(400,"数据不合法").put("data",res);
                }
	        	brandService.save(brand);
                return R.ok();
            }

数据不合法时，使用一个Map存储所有报错信息，并返回前端











## 统一异常处理 


用于集中处理数据异常
使用SpringMVC提供的@ControllerAdvice注解

            1.创建ControllerAdviceExcetion，添加注解：

                    @RestControllerAdvice(basePackages = "com.katzenyasax.mall.product.controller")

              表示扫描controller包中所有的类，对其所有异常进行处理

            2.将BrandController.save方法的所有异常处理的业务去除，只保留正常业务代码：

                    @RequestMapping("/save")
                    @RequiresPermissions("product:brand:save")
                    public R save(@RequestBody @Valid BrandEntity brand){
	                	brandService.save(brand);
                        return R.ok();
                    }

            3.将处理异常的业务放于ControllerAdviceException中：

                    @Slf4j                                                                                      //日志输出  
                    //@ControllerAdvice(basePackages = "com.katzenyasax.mall.product.controller")
                    @RestControllerAdvice(basePackages = "com.katzenyasax.mall.product.controller")
                    //包含了：ControllerAdvice和ResponseBody
                    public class ControllerAdviceException {
                        //@ResponseBody                                                                         //要以json格式返回数据
                        @ExceptionHandler(value = MethodArgumentNotValidException.class)                        //表示可处理的异常
                        public R handlerValidException(MethodArgumentNotValidException e){
                            log.error("数据不合法",e.getMessage(),e.getClass());                                 //日志输出异常

                            BindingResult result=e.getBindingResult();
                            Map<String,String> map=new HashMap<>();
                            result.getFieldErrors().forEach((item)->{
                                String msg=item.getDefaultMessage();
                                String fld=item.getField();
                                map.put(fld,msg);
                            });

                            return R.error(400,"数据不合法").put("data",map);
                        }
                    }

              处理的是MethodArgumentNotValidException，即数据不合法
              注意，原本应该加上注解ControllerAdvice和ResponseBody，分别表示：处理异常，和返回数据为json格式
              但是由于有整合的注解RestControllerAdvice，因此直接选用整合版
              注解BindingResult开始的就是原本BrandController中处理异常的业务

            


此时访问： 

            localhost:10100/api/product/brand/save

post一个：

            {
                 "name": "华为", 
                 "logo": "https://kaztenyasax-mall.oss-cn-beijing.aliyuncs.com/huawei.png"
            }

返回的异常：

            {
                "msg": "数据不合法",
                "code": 400,
                "data": {
                    "name": "需要是一个合法的URL",
                    "showStatus": "不能为null",
                    "sort": "不能为null",
                    "descript": "不能为空",
                    "firstLetter": "不能为空"
                }
            }

同时服务器也记录了错误日志：

            2023-09-14T17:16:29.868+08:00 ERROR 16524 --- [nio-8800-exec-1] c.k.m.p.e.ControllerAdviceException      : 数据不合法











为了保证错误的可溯源，我们可以在ControllerAdviceException中定义一个处理最大异常Throwable的handler：

            @ExceptionHandler(value = Throwable.class)
            public R handlerThrowable(Throwable e){
                return R.error();
            }

以后在controller中，可以放心大胆地抛出异常，异常都会被这个handler处理



为了可读性，推荐根据模块的不同、方法的不同自定义一些错误码，并放在common中，整个项目都能够使用这一标准错误码

在mall-common/src/main/java com.katzenyasax.common.exception.BizCodeEnume创建该表单，用以存放所有的错误码
错误码的规则：

            1. 错误码定义规则为5为数字
            2. 前两位表示业务场景，最后三位表示错误码。例如：100001。10:通用 001:系统未知异常
            3. 维护错误码后需要维护错误描述，将他们定义为枚举形式
                错误码列表：
                10: 通用
                001：参数格式校验
                11: 商品
                12: 订单
                13: 购物车
                14: 物流

故handlerValidException可将return改写为：

            return R.error(BizCodeEnume.VAILD_EXCEPTION.getCode(),BizCodeEnume.VAILD_EXCEPTION.getMsg()).put("data",map);







## 分组异常处理 



不同的业务，异常要求也不同

可以在校验注解括号加上group={}，例如：

            @TableId
	        @NotNull(message = "更新数据时，必须指定id",groups = {UpdateGroup.class})
	        @Null(message = "插入时，禁止指定id",groups = {InsertGroup.class})
	        private Long brandId;

所有组都是标记型接口，没有实际内容
接下来在不同的方法上加注解，例如BrandController的save：

            public R save(@RequestBody @Validated({InsertGroup.class}) BrandEntity brand)

表示该方法的分类为Insert
测试一下，此时为添加，如果说id为空，那么应该不会报错的：

            localhost:10100/api/product/brand/save

            {
                 "name": "华", 
                 "logo": "https://kaztenyasax-mall.oss-cn-beijing.aliyuncs.com/huawei.png",
                 "showStatus":1,
                 "sort":1,
                 "descript":"❀",
                 "firstLetter": "F"
            }

            {
                "msg": "success",
                "code": 0
            }

如果指定了id，那应该会报错：

            localhost:10100/api/product/brand/save

            {
                 "brandId":55,
                 "name": "华", 
                 "logo": "https://kaztenyasax-mall.oss-cn-beijing.aliyuncs.com/huawei.png",
                 "showStatus":1,
                 "sort":1,
                 "descript":"❀",
                 "firstLetter": "F"
            }
            
            {
                "msg": "参数格式校验失败",
                "code": 10001,
                "data": {
                    "brandId": "插入时，禁止指定id"
                }
            }

正确的



又例如图片地址，在指定的时候必须是一个URL，但是允许不指定，那么就：

            @URL(message = "品牌logo必须是合法的URL",groups = {InsertGroup.class})
	        private String logo;

其余情况不指定就行了
所以歧视BrandEntity的最终形态应该是：

            @TableId
	        @NotNull(message = "更新数据时，必须指定id",groups = {UpdateGroup.class})
	        @Null(message = "插入时，禁止指定id",groups = {InsertGroup.class})
	        private Long brandId;
	        /**
	         * 品牌名
	         */
	        @NotBlank(message = "插入时，必须指定name",groups = {InsertGroup.class})
	        private String name;
	        /**
	         * 品牌logo地址
	         */
	        @URL(message = "品牌logo必须是合法的URL",groups = {InsertGroup.class})
	        private String logo;
	        /**
	         * 介绍
	         */
	        @NotBlank(message = "插入时，禁止指定descript",groups = {InsertGroup.class})
	        private String descript;
	        /**
	         * 显示状态[0-不显示；1-显示]
	         */
	        @NotNull(message = "插入时，禁止指定showStatus",groups = {InsertGroup.class})
	        private Integer showStatus;
	        /**
	         * 检索首字母
	         */
	        @Pattern(regexp="[a-zA-Z]",message = "插入时，禁止指定firstLetter",groups = {InsertGroup.class})
	        private String firstLetter;
	        /**
	         * 排序
	         */
	        @NotNull(message = "插入时，禁止指定sort",groups = {InsertGroup.class})
	        @Min(value = 0,message = "排序必须大于等于0")
	        private Integer sort;

没有指定分组的字段，在@Validated生效时看作没有校验注解，即此时可指定可不指定







## 自定义数据校验 



自定义校验，满足发展情况下的校验，例如我要校验showStatus，只能是0或1
而我要自定义一个注解，可以校验数据是否为我指定的一个数组内的值



            

那么流程为：
    
            1.编写自定义注解

                @Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
                @Retention(RetentionPolicy.RUNTIME)
                @Documented
                @Constraint(validatedBy = {})
                public @interface NumbersIWant {
                    String message() default "{}";
                    Class<?>[] groups() default {};
                    Class<? extends Payload>[] payload() default {};
                    int[] value();
                }

              这是雏形

            2.编写校验器

                public class NumbersIWantConstraint implements ConstraintValidator<NumbersIWant,Integer> {
                    private Set<Integer> set=new HashSet<>();
                    @Override
                    public void initialize(NumbersIWant constraintAnnotation) {
                        int[] values=constraintAnnotation.value();
                        for(int n:values){
                            set.add(n);
                        }
                    }
                    @Override
                    public boolean isValid(Integer integer/** 这里的integer是提交的值 **/, ConstraintValidatorContext constraintValidatorContext) {
                        return set.contains(integer);
                    }
                }

              他必须实现一个接口ConstraintValidator<E,F>，E是标准，F是提交上来的数
              通过initialize方法初始化数据，isValid则正式判断提交的数据是否满足注解
              isValid由系统调用，会自动传入提交的数

            3.将二者联调：
              在NumbersIWant上指定校验器

                    @Constraint(validatedBy = {NumbersIWantConstraint.class})
    
              表示使用NumbersIWantConstraint这个校验器

测试一下，给showStatus加上：

	        @NumbersIWant(value = {0,1})
	        private Integer showStatus;

访问地址：

            localhost:10100/api/product/brand/save

请求体json为：

            {
                 "name": "华",
                 "showStatus":2,
                 "sort":1,
                 "descript":"❀",
                 "firstLetter": "F"
            }

反馈：

            {
                "msg": "参数格式校验失败",
                "code": 10001,
                "data": {
                    "showStatus": ???????
                }
            }





##  品牌的查询 


模糊查询，更改BrandServiceImpl下面的方法为：

            @Override
            public PageUtils queryPage(Map<String, Object> params) {
                //获取关键字key
                String key=(String) params.get("key");
                log.info(String.valueOf((key==null)));
                //若关键字key不为空：
                if(key!=null){
        
                    //进行对name和descript的模糊匹配
                    QueryWrapper<BrandEntity> wrapper=new QueryWrapper<BrandEntity>().like("name",key).or().like("descript",key);
                    return new PageUtils(this.page(new Query<BrandEntity>().getPage(params),wrapper));
        
        
                }
                //若关键字不为空
                IPage<BrandEntity> page = this.page(
                        new Query<BrandEntity>().getPage(params),
                        new QueryWrapper<BrandEntity>()
                );
                return new PageUtils(page);
            }

结果是查到了
但其实不用判断key是否为空，即使为空的话也只是为数据添加了一个开放的判断标准罢了









#  商品服务III.属性


## 查询属性

商品系统/平台管理/属性分组

请求路径：
    
            /product/attrgroup/list/{catelogId}

方式为get
catelogId为0时默认查找所有
请求参数为

            {
                page: 1,//当前页码
                limit: 10,//每页记录数
                sidx: 'id',//排序字段
                order: 'asc/desc',//排序方式
                key: '华为'//检索关键字
            }

返回结果：

            {
            	"msg": "success",
            	"code": 0,
            	"page": {
            		"totalCount": 0,
            		"pageSize": 10,
            		"totalPage": 0,
            		"currPage": 1,
            		"list": [{
            			"attrGroupId": 0, //分组id
            			"attrGroupName": "string", //分组名
            			"catelogId": 0, //所属分类
            			"descript": "string", //描述
            			"icon": "string", //图标
            			"sort": 0 //排序
            			"catelogPath": [2,45,225] //分类完整路径
            		}]
            	}
            }



流程：

            1.在AttrGroupController中定义：

                     //根据id查找属性分组
                    @RequestMapping("list/{attrgroup}")
                    public R listAttrGroup(@RequestParam Map<String, Object> params,@PathVariable Integer attrgroup){
                        PageUtils page=attrGroupService.queryPage(params,attrgroup);
                        return R.ok().put("page",page);
                    }

            2.AttrGroupServiceImpl定义方法：

                    @Override
                    public PageUtils queryPage(Map<String, Object> params, Integer catelogId) {
                        if(catelogId==0){
                            String key=(String) params.get("key");
                            QueryWrapper<AttrGroupEntity> wrapper = new QueryWrapper<AttrGroupEntity>().like("attr_group_id", key).or().like("attr_group_name", key).or().like("descript", key);
                            //wrapper：sql评判标准，查找时会自动根据评判标准筛选不满足标准的对象
                            IPage<AttrGroupEntity> pageFinale = this.page(new Query<AttrGroupEntity>().getPage(params), wrapper);
                            return new PageUtils(pageFinale);
                        }
                        else {
                            QueryWrapper<AttrGroupEntity> wrapper =new QueryWrapper<AttrGroupEntity>().eq("catelog_id",catelogId);
                            //wrapper：sql评判标准，查找时会自动根据评判标准筛选不满足标准的对象
                            IPage<AttrGroupEntity> pageFinale=this.page(new Query<AttrGroupEntity>().getPage(params),wrapper);
                            return new PageUtils(pageFinale);
                            //catelogId不为0，但是key也不为空
                        }
                    }


              IPage集合，其构造为：

                     IPage<AttrGroupEntity> page = this.page(new Query<AttrGroupEntity>().getPage(params),new QueryWrapper<AttrGroupEntity>());

              有两个构造条件，Query就是前端传来的固定的param，决定了limit、page等分页标准
              第二个QueryWrapper则是匹配sql的校验器，查询结果必须满足该校验器，才能通过查询，而new一个空参的QueryWrapper，意思就是没有校验，任何结果都可通过查询

              对于wrapper，eq方法为判断是否相等，like则是模糊判断，条件之间可通过or()和and()方法进行连接，但是注意and()方法默认加括号



前端做好对接后（主要是categories需要把data.data改成data.success）

接下来在第三级菜单的children上加注解：

            @JsonInclude(JsonInclude.Include.NON_EMPTY)
	        @TableField(exist = false)
	        private List<CategoryEntity> children=new ArrayList<>();
        
表示当children不为空集合时，返回的json数据才包含该字段，内容为空时，json不返回该字段
这是为了防止在第三级菜单时，由于检测到集合children[]的存在，而自动生成下一级菜单（此时用的不再是el-tree，故规则不同，el-tree我们可以写死只有三级）
而导致无法选择groupId
















## 属性修改页面的数据回显 




接下来会发现，修改时，无法回显groupId的路径，因此写一个方法回显groupId的路径
路径就加在AttrGroupEntity的成员变量中：

            @TableField(exist = false)			//不存在于数据库
	        private Long[] Path;

其中依次存放父——>子的categoryId
接下来在AttrGroupController中的返回属性分组信息的方法，即：

            @RequestMapping("/info/{attrGroupId}")
            @RequiresPermissions("product:attrgroup:info")
            public R info(@PathVariable("attrGroupId") Long attrGroupId)
    
因为回显进入修改界面时，默认发出请求：

            http://localhost:10100/api/product/attrgroup/info/

因此在info上添加：
因为是有关category的查询，因此使用category的service查询

            @RequestMapping("/info/{attrGroupId}")
            @RequiresPermissions("product:attrgroup:info")
            public R info(@PathVariable("attrGroupId") Long attrGroupId){
	        	AttrGroupEntity attrGroup = attrGroupService.getById(attrGroupId);
                Long attrGroupCatelogId=attrGroup.getCatelogId();
                //先获取分组id
                Long[] path=categoryService.getCategoryPath(attrGroupCatelogId);
                //再获取完整路径
                attrGroup.setPath(path);
                //设置回显路径
                return R.ok().put("attrGroup", attrGroup);
            }

随后定义方法：
        
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
            
对于前端，还要将存储该数组的

            this.catelogPath=data.attrGroup.catelogPath
            
改为：

            this.catelogPath =  data.attrGroup.path;






至此功能完成
测试一下：

            http://localhost:10100/api/product/attrgroup/info/12?t=1694864825179

返回：

            {
                "msg": "success",
                "attrGroup": {
                    "attrGroupId": 12,
                    "attrGroupName": "1",
                    "sort": 1,
                    "descript": "1",
                    "icon": "1",
                    "catelogId": 269,
                    "path": [
                        3,
                        39,
                        269
                    ]
                },
                "code": 0
            }

至此功能完成








##  有关分页页面下的页数


发现问题：
分类属性的下方，不会显示共几条数据，只能显示有多少页和每页多少条数据
故使用mybatis plus分页插件

使用mybatis plus的分页插件
官方文档：   https://baomidou.com/pages/2976a3/#spring-boot


在product模块内加上配置文件：config.MybatisPlusConfiguration

            @Configuration
            @EnableTransactionManagement
            @MapperScan(value = "com.katzenyasax.mall.product.dao")
            public class MybatisPlusConfiguration {
                @Bean
                    public MybatisPlusInterceptor mybatisPlusInterceptor() {
                        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
                        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.H2));
                        return interceptor;
                    }
            }

此时分类属性下面就有页数了























#  商品服务IV.品牌和分类的关联


## 品牌和分类关联的查询


商品分类和品牌呈多对多的关系
一个品牌的所有商品可以有多种属性分类，一个商品分下类也可以有多个品牌的商品
对于这种情况，使用一张中间表单进行连接



请求路径：

            http://localhost:10100/api/product/categorybrandrelation/catelog/list

controller中定义：

            //获取商品关联
            @GetMapping(value = "/catelog/list")
            public R listCatelog(@RequestParam("brandId")int brandId){
                log.info("brandId: "+String.valueOf(brandId));
                List<CategoryBrandRelationEntity> data=categoryBrandRelationService.list(
                        new QueryWrapper<CategoryBrandRelationEntity>().eq("brand_id",brandId));
                return R.ok().put("data",data);
            }
        
        
        
            //获取品牌关联
            @GetMapping(value = "/brand/list")
            public R lisBrand(@RequestParam("catelogId")int catelogId){
                List<CategoryBrandRelationEntity> data=categoryBrandRelationService.list(
                        new QueryWrapper<CategoryBrandRelationEntity>().eq("catelog_id",catelogId));
                return R.ok().put("data",data);
            }

使用了校验器，校验catelog_id或brand_id是否等于参数
根据参数返回值
总之这样就返回了关联关系，例如：

            http://localhost:10100/api/product/categorybrandrelation/catelog/list?t=1694917073908&brandId=9

此处brandId=9，页面返回：

            {
                "msg": "success",
                "code": 0,
                "data": [
                    {
                        "id": 13,
                        "brandId": 9,
                        "catelogId": 225,
                        "brandName": "华为",
                        "catelogName": "手机"
                    },
                    {
                        "id": 15,
                        "brandId": 9,
                        "catelogId": 250,
                        "brandName": "华为",
                        "catelogName": "平板电视"
                    },
                    {
                        "id": 16,
                        "brandId": 9,
                        "catelogId": 449,
                        "brandName": "华为",
                        "catelogName": "笔记本"
                    }
                ]
            }












## 新增、品牌和分类关联的新增、保存 


请求路径：

            product/categorybrandrelation/save

请求参数：
    
            {"brandId":1,"catelogId":2}

响应：

            {
            	"msg": "success",
            	"code": 0
            }

表示，brandId为1的品牌，和catelogId为2的属性分类关联起来
同时要可以存入双方的name属性:

在controller中修改save方法中，调用的service的方法：

            categoryBrandRelationService.saveName(categoryBrandRelation);

save改为自定义方法saveName
随后在service接口和实现类中实现：

            //先要获取brandId和catelogId对应的name：
            //所以需要调用BrandService和CategoryService
            @Autowired
            BrandDao brandDao;
            @Autowired
            CategoryDao categoryDao;
            @Override
            public void saveName(CategoryBrandRelationEntity categoryBrandRelation) {
                //获取了name
                String brandName=brandDao.selectById(categoryBrandRelation.getBrandId()).getName();
                String catelogName=categoryDao.selectById(categoryBrandRelation.getCatelogId()).getName();
                //设置name
                categoryBrandRelation.setBrandName(brandName);
                categoryBrandRelation.setCatelogName(catelogName);
                //返回设置过name的关系对象
                this.save(categoryBrandRelation);
            }

直接使用了自动注入的dao层接口BrandDao和CategoryDao，来select双方的name
用service层的getById貌似有问题













## 品牌和商品分类关联的一致性问题 


由于我们的relation表存储的是商品分类和品牌的关系，那么其数据应该和上述两张表的数据一致
也就是说，如果商品分类或者品牌中数据发生变动，relation表的数据也要发生变动（如果此时引用了上述两张表的数据的话）


这样一来就必须在Brand和Category的模块中去实现
若名字有所变动，如修改、删除，对应的关系表也要被修改、删除

所以我们在CategoryBrandRelationServiceImpl中定义实现类特有方法：

            public void updateCategory(Long catelogId,String  catelogName){
                //获取所有关系对象
                List<CategoryBrandRelationEntity> entitiesAll=baseMapper.selectList(null);
                //使用stream过滤器过滤
                List<CategoryBrandRelationEntity> entities = entitiesAll.stream().filter(
                        categoryBrandRelationEntity -> categoryBrandRelationEntity.getCatelogId().equals(catelogId)).filter(                            //id必须相同
                        categoryBrandRelationEntity -> categoryBrandRelationEntity.getCatelogName()!=catelogName).collect(                              //name必须不同，否则视为未变更
                                Collectors.toList());         
                //遍历，修改所有
                for(CategoryBrandRelationEntity entity:entities){
                    entity.setCatelogName(catelogName);
                    this.updateById(entity);
                }
        
            }
            public void updateBrand(Long brandId,String brandName){
                //获取所有关系对象
                List<CategoryBrandRelationEntity> entitiesAll=baseMapper.selectList(null);
                //使用stream过滤器过滤
                List<CategoryBrandRelationEntity> entities = entitiesAll.stream().filter(
                        categoryBrandRelationEntity -> categoryBrandRelationEntity.getBrandId().equals(brandId)).filter(                                //id必须相同
                        categoryBrandRelationEntity -> categoryBrandRelationEntity.getBrandName()!=brandName).collect(                          //name必须不同，否则视为未更改
                        Collectors.toList());
                log.info("entities: "+entities.toString());
                //遍历，修改所有
                for(CategoryBrandRelationEntity entity:entities){
                    entity.setBrandName(brandName);
                    log.info(entities.toString());
                    this.updateById(entity);
                }
            }
    
因此分别在CategoryController和BrandController中的update方法加上：

            @RequestMapping("/update")
            public R update(@RequestBody CategoryEntity category){
	        	categoryService.updateById(category);
                categoryBrandRelationService.updateCategory(category.getCatId(),category.getName());
            //加上调用relationService，通过category的id和name更新relation中的数据
                return R.ok();
            }

            @RequestMapping("/update")
            public R update(@RequestBody BrandEntity brand){
	        	brandService.updateById(brand);
                categoryBrandRelationService.updateBrand(brand.getBrandId(),brand.getName());
            //加上调用relationService，通过brand的id和name更新relation中的数据
                return R.ok();
            }

其中categoryBrandRelationService是通过实现类自动注入的实现类的对象
可以调用其内部特有方法


整个原理就是：
当category或brand修改名字时，调用relation内部的方法，根据category或brand的id和name获取应该修改的值
判断是否应该修改，是根据stream的过滤器判断的，首先id相同，其次name必须不同，因为name不同就代表着本次并未修改name，故不需要再进行下面的操作
如果得到了应该修改的数据，会存到一个list中
遍历该list，每次修改name，然后使用updateById存入数据库
于是这样就完成了数据的同步








当然还有删除的一致性，删除某个category或brand后，对应id的relation的数据直接进行删除，不需要再经过name:

            public void deleteCategory(Long catelogId){
                //获取所有关系对象
                List<CategoryBrandRelationEntity> entitiesAll=baseMapper.selectList(null);
                //使用stream过滤器过滤
                List<CategoryBrandRelationEntity> entities = entitiesAll.stream().filter(
                        categoryBrandRelationEntity -> categoryBrandRelationEntity.getCatelogId().equals(catelogId)).collect(Collectors.toList());
                //遍历，修改所有
                for(CategoryBrandRelationEntity entity:entities){
                    entity.setCatelogName(catelogName);
                    baseMapper.deleteById(entity);
                }
            }
            public void deleteBrand(Long brandId){
                //获取所有关系对象
                List<CategoryBrandRelationEntity> entitiesAll=baseMapper.selectList(null);
                //使用stream过滤器过滤
                List<CategoryBrandRelationEntity> entities = entitiesAll.stream().filter(
                        categoryBrandRelationEntity -> categoryBrandRelationEntity.getBrandId().equals(brandId)).collect(Collectors.toList());
                //遍历，修改所有
                for(CategoryBrandRelationEntity entity:entities){
                    entity.setBrandName(brandName);
                    baseMapper.deleteById(entity);
                }
            }

随后在category和brand的controller的delete中调用方法就行了

            //逻辑删除
            @RequestMapping("/delete")
            public R deleteSafe(@RequestBody Long[] catIds){
                categoryService.hideByIds(Arrays.asList(catIds));
                for(Long id:catIds){
                    categoryBrandRelationService.deleteCategory(id);
                }
                return R.ok();
            }

            @RequestMapping("/delete")
            @RequiresPermissions("product:brand:delete")
            public R delete(@RequestBody Long[] brandIds){
	        	brandService.removeByIds(Arrays.asList(brandIds));
                for(Long id:brandIds){
                    categoryBrandRelationService.deleteBrand(id);
                }
                return R.ok();
            }

只不过注意category是要逻辑删除里调用就是了









#  商品服务V：参数


## 参数的查询

二者共用同一个表，区别在于attr_type
为0：普通规格参数
为1：销售属性
为2：二者皆是


查询
参数规格：

            /product/attr/base/list/{catelogId}

销售属性：

            /product/attr/sale/list/{catelogId}
         

请求参数：

            {
                page: 1,//当前页码
                limit: 10,//每页记录数
                sidx: 'id',//排序字段
                order: 'asc/desc',//排序方式
                key: '华为'//检索关键字
            }

响应结果：

            {
            	"msg": "success",
            	"code": 0,
            	"page": {
            		"totalCount": 0,
            		"pageSize": 10,
            		"totalPage": 0,
            		"currPage": 1,
            		"list": [{
            			"attrId": 0, //属性id
            			"attrName": "string", //属性名
            			"attrType": 0, //属性类型，0-销售属性，1-基本属性
            			"catelogName": "手机/数码/手机", //所属分类名字
            			"groupName": "主体", //所属分组名字
            			"enable": 0, //是否启用
            			"icon": "string", //图标
            			"searchType": 0,//是否需要检索[0-不需要，1-需要]
            			"showDesc": 0,//是否展示在介绍上；0-否 1-是
            			"valueSelect": "string",//可选值列表[用逗号分隔]
            			"valueType": 0//值类型[0-为单个值，1-可以选择多个值]
            		}]
            	}
            }



但是有一个问题，就是查询参数的时候，由于参数实体AttrEntity里面并没有代表所属分类和所属分组的字段，所以后端如果以AttrEntity传输前端的话，其实无法展现这两个字段
这将会导致前端无法展示所属分类和所属分组

因此使用vo
即view object
用于封装一些自定义的数据

可以接收前端一次发来的一些单独的字段，将其封装为一个对象放入后端进行处理
也可以将后端要返回给前端的一些单独字段封装为一个对象

我们定义VO：

            @Data
            public class AttrVO_WithGroupNameAndCatelogName {
            @TableId
                private Long attrId;
                private String attrName;
                private Integer searchType;
                private Integer valueType;
                private String icon;
                private String valueSelect;
                private Integer attrType;
                private Long enable;
                private Long catelogId;
                private Integer showDesc;
                private String catelogName;         //分类名
                private String groupName;           //分组名
            }

我们本来的AttrEntity本来就有catelogId，因此catelogName可以直接获取
但是AttrEntity里面没有groupId？那就只能通过属性和参数的关系表来获取了
先用attrId从关系表中得到groupId，再从group表内获取groupName
完美



AttrController中，定义接口：

            /**
            * 
            * 获取普通的规格参数，和共用者
            * 
            * 
            * */
            @RequestMapping("/base/list/{catelogId}")
            public R baseList(@RequestParam Map<String, Object> params,@PathVariable Integer catelogId){
                PageUtils page = attrService.queryPageBase(params,catelogId);
                return R.ok().put("page", page);
            }
            /**
             *
             * 获取普通的销售属性，和公用者
             *
             *
             * */
            @RequestMapping("/sale/list/{catelogId}")
            public R saleList(@RequestParam Map<String, Object> params,@PathVariable Integer catelogId){
                PageUtils page = attrService.queryPageSale(params,catelogId);
                return R.ok().put("page", page);
            }

service中定义方法：

            /**
             *
             *
             * @param params
             * @param catelogId
             * @return
             *
             * 查询所有的普通参数
             * 查询所有attr_type为0或2的参数
             *
             *
             */
            @Override
            public PageUtils queryPageBase(Map<String, Object> params, Integer catelogId) {
                QueryWrapper<AttrEntity> wrapper=new QueryWrapper<AttrEntity>().and(obj0->obj0.eq(
                        "attr_type",ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()).or().eq(
                        "attr_type",ProductConstant.AttrEnum.ATTR_TYPE_BOTH.getCode()));
        
                if(!StringUtils.isEmpty((String)params.get("key"))){
                    wrapper.and(obj->obj.like(
                            "attr_name",params.get("key")).or().like(
                            "value_select",params.get("key")));
                }
                if(catelogId!=0) {
                    wrapper.and(obj->obj.eq("catelog_id",catelogId));
                }
                IPage<AttrEntity> page=this.page(new Query<AttrEntity>().getPage(params),wrapper);
        
        
                //接下来要添加上分组名和分类名
                //将查询好的AttrEntity们放入list
                List<AttrEntity> list=page.getRecords();
                List<AttrVO_WithGroupNameAndCatelogName> finale = list.stream().map(attrEntity -> {
                    AttrVO_WithGroupNameAndCatelogName vo = new AttrVO_WithGroupNameAndCatelogName();
                    //创建vo对象
                    BeanUtil.copyProperties(attrEntity, vo);
                    //将attrEntity的所有基本属性复制到vo
        
                    //随后要为vo添加分组名和分类名
                    CategoryEntity categoryEntity = categoryDao.selectById(attrEntity.getCatelogId());
                    //通过已知的catelogId获取对应的category对象
                    String catelogName = categoryEntity.getName();
                    //通过category对象，直接获取catelogName
                    vo.setCatelogName(catelogName);
        
                    AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = attrAttrgroupRelationDao.selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrEntity.getAttrId()));
                    //通过已知的attrId，获取属性和参数的关系对象
                    if(attrAttrgroupRelationEntity!=null) {
                    //一定要判断，否则如果查到attr_id在关系表内不存在的话就会报错而无法允许
                        AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrAttrgroupRelationEntity.getAttrGroupId());
                        //通过关系对象，获取对应的attrGroupId，并以此获取对应的attrGroup对象
                        String groupName = attrGroupEntity.getAttrGroupName();
                        //通过获取的attrGroup对象直接获取groupName
                        vo.setGroupName(groupName);
                    }
                    return vo;
                }).collect(Collectors.toList());
        
        
                PageUtils pageUtils=new PageUtils(page);
                pageUtils.setList(finale);
                return pageUtils;
            }
        
        
            /**
             *
        
             * @param params
             * @param catelogId
             * @return
             *
             * 查询所有的销售属性
             * 查询所有attr_type为1或2的参数
             *
             */
        
            @Override
            public PageUtils queryPageSale(Map<String, Object> params, Integer catelogId) {
                QueryWrapper<AttrEntity> wrapper=new QueryWrapper<AttrEntity>().and(obj0->obj0.eq(
                        "attr_type",ProductConstant.AttrEnum.ATTR_TYPE_SALE.getCode()).or().eq(
                        "attr_type",ProductConstant.AttrEnum.ATTR_TYPE_BOTH.getCode()));
                if(!StringUtils.isEmpty((String)params.get("key"))){
                    wrapper.and(obj->obj.like(
                            "attr_name",params.get("key")).or().like(
                            "value_select",params.get("key")));
                }
                if(catelogId!= ProductConstant.AttrEnum.ATTR_TYPE_SALE.getCode()) {
                    wrapper.and(obj->obj.eq("catelog_id",catelogId));
                }
                IPage<AttrEntity> page=this.page(new Query<AttrEntity>().getPage(params),wrapper);
        
                //接下来要添加上分组名和分类名
                //将查询好的AttrEntity们放入list
                List<AttrEntity> list=page.getRecords();
                List<AttrVO_WithGroupNameAndCatelogName> finale = list.stream().map(attrEntity -> {
                    AttrVO_WithGroupNameAndCatelogName vo = new AttrVO_WithGroupNameAndCatelogName();
                    //创建vo对象
                    BeanUtil.copyProperties(attrEntity, vo);
                    //将attrEntity的所有基本属性复制到vo
        
                    //随后要为vo添加分组名和分类名
                    CategoryEntity categoryEntity = categoryDao.selectById(attrEntity.getCatelogId());
                    //通过已知的catelogId获取对应的category对象
                    String catelogName = categoryEntity.getName();
                    //通过category对象，直接获取catelogName
                    vo.setCatelogName(catelogName);
                    return vo;
                }).collect(Collectors.toList());
                PageUtils pageUtils=new PageUtils(page);
                pageUtils.setList(finale);
                return pageUtils;
            }

匹配顺序是：
    
            1.先匹配类型，即attr_type，匹配是普通规格参数还是销售属性，抑或是共用者
            2.随后匹配key，且必须匹配key，但是只需要名字和可选值能匹配就行
            3.根据是否传入了特定的catelogId，如果有则匹配
            4.根据最终匹配规则和param参数体得到符合要求的attrEntity

            5.将attrEntity复制到vo中
            6.根据是销售属性还是普通参数来获取catelogName和groupName，销售属性不需要catelogName
            7.将catelogName和groupName放入vo中，将vo封装为集合，并封装为pageUtil
            8.返回pageUtil




结果可以查询到
































## 参数的新增 

请求参数：

            http://localhost:10100/api/product/attr/save

请求体：

            


新增参数时，需要选择其所属属性，因此需要两个参数，属性id和自增得到的参数id，存储到attr attrGroup relation的表内
这个过程应该在新增参数时进行

返回的数据，刚好比AttrEntity多了一个attrGroupId，但是我们不想也最好不要直接在AttrEntity里新增一个成员变量
那我们直接定义vo：

            @Data
            public class AttrVO_WithAttrGroupId {
                @TableId
                private Long attrId;
                private String attrName;
                private Integer searchType;
                private Integer valueType;
                private String icon;
                private String valueSelect;
                private Integer attrType;
                private Long enable;
                private Long catelogId;
                private Integer showDesc;
                //和属性对应的attrGroupId
                private Long attrGroupId;
            }

我们要接收attrGroupId，就不能接收AttrEntity，而要接收AttrVO
因此在controller中更改：

            @RequestMapping("/save")
            @RequiresPermissions("product:attr:save")
            public R save(@RequestBody AttrVO_WithAttrGroupId attr){
                attrService.saveByAttrVO(attr);
                return R.ok();
            }

自定义的一个方法saveByAttrVO，该方法不仅要实现新增参数attr的存储，还要实现参数attr和属性attrGroup双方关系的存储
方法：

            @Override
            public void saveByAttrVO(AttrVO_WithAttrGroupId attrVO) {
                //通过AttrVO对象来存储新增参数
                //通过根据attrVO的attrGroupId来存储属性和参数的关系
                //1.存储attr
                AttrEntity attrEntity=new AttrEntity();
                BeanUtil.copyProperties(attrVO,attrEntity);     //将attrVO的同名数据复制到attrEntity，也即是除了attrGroupId
                this.save(attrEntity);                          //存储
                //2.存储关系
                Long attrGroupId=attrVO.getAttrGroupId();
                Long attrId=attrEntity.getAttrId();
                AttrAttrgroupRelationEntity attrAttrgroupRelationEntity=new AttrAttrgroupRelationEntity();
                attrAttrgroupRelationEntity.setAttrGroupId(attrGroupId);
                attrAttrgroupRelationEntity.setAttrId(attrId);
                attrAttrgroupRelationDao.insert(attrAttrgroupRelationEntity);
            }

这样也可以存储成功
注意前端在选择分组时，必须先选择分类，才能通过分类的categoryId检索有哪些关联了该categoryId的attrGroupId







说一下整个商品服务的架构：

        1.首先，商品分为四个部分：分类、品牌、属性、参数

        2.分类就是category，代表商品的类别，存储在category

        3.品牌就是brand，代表商品的品牌，存储在brand

        4.分类和品牌为多对多的关系，中间以一张category_brand_relation进行关系的存储

        5.属性就是attrGroup，代表商品的固有属性，存储在attr_group

        6.参数就是attr(attribute)，代表商品固有属性的参数，存储在attr

        7.属性和参数为多对多的关系，中间以一张attr_attrGroup_relation存储












##  参数和属性的关联删除 



要实现的功能是，任意删除一个属性或参数时，其在关系表内的相关数据也要删除

AttrGroupController中：

      /**
       * 要求删除group的同时，删除该group和其他attr的关联
       */
      @RequestMapping("/delete")
      @RequiresPermissions("product:attrgroup:delete")
      public R delete(@RequestBody Long[] attrGroupIds){
          attrGroupService.removeGroupAndRelation(attrGroupIds);

          return R.ok();
      }

自定义方法removeGroupAndRelation：

      /**
       * 要求删除group的同时，删除该group和其他attr的关联
       */
      @Override
      public void removeGroupAndRelation(Long[] attrGroupIds) {
          for(Long groupId:attrGroupIds){
              attrAttrgroupRelationDao.delete(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_group_id",groupId));
          }
      }

























##  魔法值 


消除魔法值
魔法值就是未经定义的常数，比如：

             if(catelogId!=0) 

中的0就是魔法值
我们把他改成一个定义的常量就不是魔法值了：

             if(catelogId!=ATTR_TYPE_BASE)

就不是魔法值了



在common包下创建一个表单，专门用于存放魔法值:

            public class ProductConstant {
                public enum AttrEnum {
                    ATTR_TYPE_BASE(1,"普通参数"),
                    ATTR_TYPE_SALE(0,"销售属性"),
                    ATTR_TYPE_BOTH(2,"二者皆是");
                    Integer code;
                    String msg;
                    AttrEnum(Integer code,String msg){
                        this.code=code;
                        this.msg=msg;
                    }
                    public Integer getCode() {
                        return code;
                    }
                    public void setCode(Integer code) {
                        this.code = code;
                    }
                    public String getMsg() {
                        return msg;
                    }
                    public void setMsg(String msg) {
                        this.msg = msg;
                    }
                }
            }

随后可以将参数里面的魔法值替换为表单中的常量，例如：

            QueryWrapper<AttrEntity> wrapper=new QueryWrapper<AttrEntity>().and(obj0->obj0.eq(
                "attr_type",ProductConstant.AttrEnum.ATTR_TYPE_SALE.getCode()).or().eq(
                "attr_type",ProductConstant.AttrEnum.ATTR_TYPE_BOTH.getCode())).and(obj->obj.like(
                                "attr_name",params.get("key")).or().like(
                                "value_select",params.get("key"))
                );

将原本的0、2等变成了常量，这就消除了魔法值




















##  参数修改页面的数据回显 

请求路径：

            http://localhost:10100/api/product/attr/info/{attrId}



修改时，分类和属性的路径不会回显

属性分组，向前端返回一个attrGroupId
分类，向前端返回一个Long数组，依次存储祖父、父、子的catelogId

定义一个AttrVO_WithPaths:

            @Data
            public class AttrVO_WithGroupIdAndPaths {
                @TableId
                private Long attrId;
                private String attrName;
                private Integer searchType;
                private Integer valueType;
                private String icon;
                private String valueSelect;
                private Integer attrType;
                private Long enable;
                private Long catelogId;
                private Integer showDesc;
                private Long attrGroupId;
                private Long[] catelogPath;
            }

注意传递到前端时，变量名要和之前的前端名字一致
在AttrController中：

            /**
             * 专供参数修改的页面
             * 查询参数
             * 传出的对象比AttrEntity多了Long类型的attrGroupId，和一个Long类型数组
             * 分别表示所属属性、所属类型的完整路径
             *
             * 因此要使用AttrVO_WithGroupIdAndPaths
             *
             *
             */
            @RequestMapping("/info/{attrId}")
            @RequiresPermissions("product:attr:info")
            public R info(@PathVariable("attrId") Long attrId){
                AttrVO_WithGroupIdAndPaths attr = attrService.getAttrWithGroupIdAndPath(attrId);
                return R.ok().put("attr", attr);
            }

定义方法：

            /**
             * @param attrId
             * @return
             *
             * 通过attrId查询信息（专供参数修改页面）
             * 查询的信息使用AttrVO_WithGroupIdAndPaths返回前端
             * 原型为AttrEntity，但是多了attrGroupId和path
             * 分别代表所属属性，和所属分类的完整路径
             *
             */
            @Override
            public AttrVO_WithGroupIdAndPaths getAttrWithGroupIdAndPath(Long attrId) {
                //思路是，先获取AttrEntity，将其复制到一个AttrVO_WithGroupIdAndPaths
                //然后通过attrId，直接查询catelogId，因为attrEntity里面自带了catelogId
                //之后直接调用方法获取完整路径
                //然后，如果不单独为销售属性，则从关系表中根据attr_id获取groupId
                AttrEntity attrEntity=this.getById(attrId);
                AttrVO_WithGroupIdAndPaths vo=new AttrVO_WithGroupIdAndPaths();
                BeanUtil.copyProperties(attrEntity,vo);
                //完成了数据的复制

                Long catelogId = vo.getCatelogId();
                Long[] path = getCategoryPath(catelogId);
                vo.setCatelogPath(path);
                //获取了catelogId的完整路径

                if(vo.getAttrType()!=ProductConstant.AttrEnum.ATTR_TYPE_SALE.getCode()) {
                    Long attrGroupId = attrAttrgroupRelationDao.selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrId)).getAttrGroupId();
                    vo.setAttrGroupId(attrGroupId);
                    //获取了attrGroupId
                }
                //如果不是销售属性，添加分组
                return vo;
            }

其中获取三级分类路径的方法：

            /**
             * @param id
             * @return
             *
             * 根据一个第三级的商品分类，获取其完整路径
             * 调用方法的时CategoryService
             *
             */
            
            public Long[] getCategoryPath(Long id) {
                Long[] pathF=new Long[3];
                //最多三级，因此数组长度为3
                pathF[2]=id;
                //最后一个数就是该元素（孙子）的id
                CategoryEntity son = categoryService.getById(id);
                //儿子对象
                pathF[1]=son.getParentCid();
                //数组第二个元素为儿子的id
                CategoryEntity father=categoryService.getById(son.getParentCid());
                //父亲对象
                pathF[0]=father.getParentCid();
                //数组第一个元素为父亲的id
                return pathF;
                //直接返回
            }

完成功能

















## 参数的修改 



修改时，会发现无法修改参数的分组
请求路径：

            http://localhost:10100/api/product/attr/update

请求体是一个完整的AttrEntity对象外加一个attrGroupId
因此使用AttrVO_WithAttrGroupId进行接收



在AttrController中：

            /**
             * @param attr
             * @return
             *
             * 更新参数
             * 同时要保存参数属性分组，即groupId
             * 但是AttrEntity内没有attrGroupId
             * 所以要接收AttrVO_WithAttrGroupId
             */
            @RequestMapping("/update")
            @RequiresPermissions("product:attr:update")
            public R update(@RequestBody AttrVO_WithAttrGroupId vo){
                attrService.updateAttrWithGroupId(vo);
        
                return R.ok();
            }

在AttrServiceImpl中定义方法：

            /**
             *
             * @param vo
             *
             * 修改参数信息，同时修改groupId
             * 也即是不仅更新一个AttrEntity，还要更新attr对应的groupId
             * 所以也需要用到attrAttrGroupRelationDao，调用其update方法
             *
             * 此外整个过程需要一同进行一同失败
             * 因此要加上事务
             * @Transictional
             *
             *
             */
            @Transactional
            @Override
            public void updateAttrWithGroupId(AttrVO_WithAttrGroupId vo) {
                //获取AttrEntity，用于保存
                AttrEntity attrEntity=new AttrEntity();
                BeanUtil.copyProperties(vo,attrEntity);
                this.updateById(attrEntity);
        
                //获取关系对象，并存入数据
                Long attrGroupId=vo.getAttrGroupId();
                Long attrId=vo.getAttrId();
                AttrAttrgroupRelationEntity attrAttrgroupRelationEntity=new AttrAttrgroupRelationEntity();
                attrAttrgroupRelationEntity.setAttrId(attrId);
                attrAttrgroupRelationEntity.setAttrGroupId(attrGroupId);
        
                //判断，如果欲修改的id在关系表中不存在，则说明参数原本并未和任何属性进行关联，故说明该操作其实是新增关系操作
                //若存在则为单词的修改关系操作
                //判断关系是否存在，使用selectCount方法
                Long count=attrAttrgroupRelationDao.selectCount(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id",vo.getAttrId()));
                if(count!=0) {
                    //如果已存在该关系的话，修改
                    attrAttrgroupRelationDao.update(attrAttrgroupRelationEntity, new UpdateWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrId));
                }
                else {
                    //若不存在该关系，新增
                    attrAttrgroupRelationDao.insert(attrAttrgroupRelationEntity);
                }
            }

测试结果也无误









#  商品服务VII：属性与参数间的关联 



## 属性和参数关联的查询

属性与参数之间的关联
查询与一个属性进行关联的所有属性
请求路径

            http://localhost:10100/api/product/attrgroup/{attrGroupId}/noattr/relation

返回值：

            {
                "msg": "success",
                "code": 0,
                "data": [
                    {
                        "attrId": 4,
                        "attrName": "aad",
                        "searchType": 1,
                        "valueType": 1,
                        "icon": "qq",
                        "valueSelect": "v;q;w",
                        "attrType": 1,
                        "enable": 1,
                        "catelogId": 225,
                        "showDesc": 1
                    }
                ]
            }

注意数据的标识为data
以属性为视角出发
在AttrGroupController中：

            /**查找和属性发生关联的所有参数
             * 根据属性id，从关系表内获取所有与之关联的参数
             *
             * @param attrGroupId
             * @return
             *
             *
             *
             */
            @RequestMapping("/{attrGroupId}/attr/relation")
            public R listAttrRelation(@PathVariable Integer attrGroupId){
                List<AttrEntity> page=attrGroupService.getAttrRelatedWithGroup(attrGroupId);
        
                return R.ok().put("data",page);
            }

自定义方法getAttrRelatedWithGroup，标识查询与group关联的attr：

            /**
             *
             * @param attrGroupId
             * @return
             *
             * 查询所有和属性已关联的参数
             * 通过attrGroupId
             *
             * 返回值为AttrEntity的List集合
             *
             *
             */
            @Override
            public List<AttrEntity> getAttrRelatedWithGroup(Integer attrGroupId) {
                //思路是，先在关系表中查询所有相关对象relations，封装
                // 再遍历relations遍历获取attrId，不需要封装，直接在遍历中就获取attr集合封装
                
                List<AttrAttrgroupRelationEntity> relations=attrAttrgroupRelationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_group_id",attrGroupId));
                //获取了所有的关系对象
                List<AttrEntity> attrS=new ArrayList<>();
                for(AttrAttrgroupRelationEntity entity:relations){
                    attrS.add(attrDao.selectById(entity.getAttrId()));
                }
                //获取了所有的attrIds
                
                return attrS;
            }

完成功能





## 属性和参数关联的删除

请求路径：

      /product/attrgroup/attr/relation/delete

请求参数：

      [{"attrId":1,"attrGroupId":2}]

是一个对象数组，定义一个vo：

      @Data
      public class AttrAttrGroupVO_AttrIdWithAttrGroupId {
          private Long attrId;
          private Long attrGroupId；
      }

AttrGroupController中：

      /**
       *
       * @return
       *
       * 删除attr和attrId的联系
       */
      @RequestMapping("attr/relation/delete")
      public R deleteRelation(@RequestBody AttrAttrGroupVO_AttrIdWithAttrGroupId[] vos){

          attrGroupService.deleteRelation(vos);
          return R.ok();
      }

自定义方法deleteRelation：

      /**
       *
       * @return
       *
       * 删除attr和attrId的联系
       */
      @Override
      public void deleteRelation(AttrAttrGroupVO_AttrIdWithAttrGroupId[] vos) {
          for(AttrAttrGroupVO_AttrIdWithAttrGroupId vo:vos){
              Long attrId=vo.getAttrId();
              Long attrGroupId=vo.getAttrGroupId();
              attrAttrgroupRelationDao.delete(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id",attrId).and(
                      w->w.eq("attr_group_id",attrGroupId)));
          }
      }

测试样例就免了，结果是成功的











## 查询与属性未关联的参数 


请求路径：

            http://localhost:10100/api/product/attrgroup/{attrGroupId}/noattr/relation

发出请求后，返回与该attrGroupId未关联的所有参数

AttrGroupController：

            /**查找和属性未发生关联的所有参数
             * 根据属性id，从关系表内获取所有未与之关联的参数
             *
             * @param attrGroupId
             * @return R
             *
             */
            @RequestMapping("{attrGroupId}/noattr/relation")
            public R listAttrNOTRelation(@RequestParam Map<String, Object> params,@PathVariable Integer attrGroupId){
                PageUtils page=attrGroupService.getAttrRelatedNOTWithGroup(params,attrGroupId);
                return R.ok().put("page",page);
            }

自定义方法getAttrRelatedNOTWithGroup：

            /**
             *
             * @param attrGroupId
             * @return
             *
             * 查询所有和属性未发生关联关联的参数
             *
             * 专供属性的新增关联界面
             *
             * 返回值为AttrEntity的Page集合
             *
             *
             */
            @Override
            public PageUtils getAttrRelatedNOTWithGroup(@RequestParam Map<String, Object> params,Integer attrGroupId) {
                //首先得到groupEntity，别问先做
                //思路是，先用关系的dao，获取所有与groupId关联的关系对象，封装为relations
                //遍历关系对象的集合，attrId，封装成集合List
                //然后直接使用attrDao，根据param直接获取所有参数，这些参数的catelogId要和groupEntity的catelogId一致
                //在所有参数中剔除attrId在集合List内的参数，就得到了所有未关联的参数
                //0.获取该groupId对应的group实体
                AttrGroupEntity groupEntity=this.getById(attrGroupId);
                //1.获取所有与groupId关联的关系对象
                List<AttrAttrgroupRelationEntity> relations=attrAttrgroupRelationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_group_id",attrGroupId));
                //2.封装attrId
                List<Long> attrIds=new ArrayList<>();
                for(AttrAttrgroupRelationEntity entity:relations){
                    attrIds.add(entity.getAttrId());
                }
                //3.获取全部参数，前提是要处于同一分类
                //4.剔除处于attrIds的所有attrId，
                // 但是要获取的是一个wrapper，也即是一个方案，后面直接根据该方案查询参数
                QueryWrapper<AttrEntity> wrapper=new QueryWrapper<AttrEntity>().eq("catelog_id",groupEntity.getCatelogId()).notIn("attr_id",attrIds);
                //4.1如果有模糊关键字key，还有匹配模糊搜索
                String key=(String) params.get("key");
                if(!StringUtils.isEmpty(key)){
                    wrapper.and((obj)->{obj.like("attr_id",key).or().like("attr_name",key);});
                }
                //5.根据params和wrapper，获取page对象
                IPage page=attrDao.selectPage(new Query<AttrEntity>().getPage(params),wrapper);
                PageUtils finale=new PageUtils(page);
                return finale;
            }

结果是可以查询得到










## 属性和参数关联的新增 


查询是可以查询了，但是没法新增

请求路径：

            http://localhost:10100/api/product/attrgroup/attr/relation

请求参数：

            [{attrId: 9, attrGroupId: 1}, {attrId: 10, attrGroupId: 1}]

是一个实体的集合
可以直接使用AttrAttrGroupRelation对象接收数据
但是最好定义一个完全相同的vo进行接收，AttrAttrGroupVO_JustReceiveData：

            @Data
            public class AttrAttrGroupVO_JustReceiveData {
                private static final long serialVersionUID = 1L;
                @TableId
                private Long id;
                private Long attrId;
                private Long attrGroupId;
                private Integer attrSort;
            }
    
AttrGroupController:

            /**
             *
             * @param vos
             * @return
             *
             *
             * 接收AttrAttrGroupVO_JustReceiveData
             * 根据其中的数据增加属性和参数的关系
             *
             *
             */
        
            @RequestMapping("attr/relation")
            public R addRelation(@RequestBody List<AttrAttrGroupVO_JustReceiveData> vos){
                attrGroupService.addRelation(vos);
                return R.ok();
            }

自定义方法addRelation：

            /**
             *
             * @param vos
             *
             * 接收的是vo的集合
             * 根据vo的集合内数据新增属性和参数关系
             *
             * 因此注入了attrAttrGroupRelationDao
             *
             */
        
            @Override
            public void addRelation(List<AttrAttrGroupVO_JustReceiveData> vos) {
                //思路是，遍历vos，创建关系对象传入每个vo的数据
                //将每个数据根据关系的dao，进行存入
                for(AttrAttrGroupVO_JustReceiveData vo:vos){
                    AttrAttrgroupRelationEntity relation=new AttrAttrgroupRelationEntity();
                    BeanUtils.copyProperties(vo,relation);
                    attrAttrgroupRelationDao.insert(relation);
                }
            }

            
可以了









#  商品维护I：用户等级信息

商品维护/发布商品，进入该页面时发出请求：

            /member/memberlevel/list

用于获取所有用户的等级信息

该方法定义于member模块
其中mybatis plus已生成了简单方法，可以直接进行简单的增删改查














## 发布商品/基本信息，查询与分类绑定的所有品牌 


发布商品的第一个页面，选择商品分类后，会发出请求：

            /product/categorybrandrelation/brands/list

属于商品模块的分类和品牌的关系
请求参数：

            {
                "catId":
            }

响应：

            {
            	"msg": "success",
            	"code": 0,
            	"data": [{
            		"brandId": 0,
            		"brandName": "string",
            	}]
            }

返回的是品牌的id和名称，相比起BrandEntity来说，只需要id和name
因此定义一个vo，专门用于返回数据：

            @Data
            public class BrandVO_OnlyIdAndName {
                private Long brandId;
                private String name;
            }

在product模块的CategoryBrandRelationController：

            /**
             *
             * @param catelogId
             * @return
             *
             * 通过catelogId，获取所有与之相关的分类与品牌的关系对象
             * 并封装为List返回前端
             *
             *
             */
            @GetMapping(value = "/brands/list")
            public R listBrand(@RequestParam("catId")Long catelogId){
                List<BrandVO_OnlyIdAndName> data=categoryBrandRelationService.selectBrandsThatRelatedWithCatelogId(catelogId);
                return R.ok().put("data",data);
            }

自定义方法selectBrandsThatRelatedWithCatelog，搜索与catelog关联的所有Brand：

            /**
             *
             * @param catelogId
             * @return
             *
             * 通过一个catelogId，查询所有与之关联的brand
             * 需要用到分类和品牌的dao，也就是本家的dao
             *
             *
             */
            @Override
            public List<BrandVO_OnlyIdAndName> selectBrandsThatRelatedWithCatelogId(Long catelogId) {
                //思路是，先从关系表中查询所有与catelogId关联的关系对象，封装为关系对象的集合
                //随后遍历集合，每次遍历都拿取id和name封装至vo，加入返回值的集合finale
                List<CategoryBrandRelationEntity> relations=categoryBrandRelationDao.selectList(new QueryWrapper<CategoryBrandRelationEntity>().eq("catelog_id",catelogId));
                List<BrandVO_OnlyIdAndName> finale=new ArrayList<>();
                for(CategoryBrandRelationEntity entity:relations){
                    Long id=entity.getBrandId();
                    String name=entity.getBrandName();
                    BrandVO_OnlyIdAndName vo=new BrandVO_OnlyIdAndName();
                    vo.setBrandId(id);
                    vo.setBrandName(name);
                    finale.add(vo);
                }
                return finale;
            }

结果是可以成功查询








##  发布商品/基本信息，查询与分类绑定的所有品牌 


请求路径：

            /product/categorybrandrelation/catelog/list

是在分类和品牌的关系dao下进行的
请求参数：一个brandId，
响应：

            {
            	"msg": "success",
            	"code": 0,
            	"data": [{
            		"catelogId": 0,
            		"catelogName": "string",
            	}]
            }

一个Category实体的id和name
定义vo：

            @Data
            public class CategoryVO_OnlyIdAndName {
                Long catelogId;
                String catelogName;
            }

CatelgoryBrandRelationController中：

             /**
             *
             * @param brandId
             * @return
             *
             * 通过catelogId，获取所有与之相关的分类与品牌的关系对象
             * 并封装为List返回前端
             *
             *
             */
            @GetMapping(value = "/brands/list")
            public R listCatelog(@RequestParam("brandId")Long brandId){
                List<BrandVO_OnlyIdAndName> data=categoryBrandRelationService.selectCategoriesThatRelatedWithBrand(brandId);
                return R.ok().put("data",data);
            }

自定义方法：selectCategoriesThatRelatedWithBrand:

            /**
             *
             * @param brandId
             * @return
             *
             * 通过一个brandId，查询所有与之关联的category
             * 需要用到分类和品牌的dao，也就是本家的dao
             *
             *
             *
             *
             *
             *
             */
            @Override
            public List<CategoryVO_OnlyIdAndName> selectCategoriesThatRelatedWithBrand(Long brandId) {
                //思路查询brand的一模一样
                List<CategoryBrandRelationEntity> relations=categoryBrandRelationDao.selectList(new QueryWrapper<CategoryBrandRelationEntity>().eq("brand0_id",brandId));
                List<CategoryVO_OnlyIdAndName> finale=new ArrayList<>();
                for(CategoryBrandRelationEntity entity:relations){
                    Long id=entity.getBrandId();
                    String name=entity.getBrandName();
                    CategoryVO_OnlyIdAndName vo=new CategoryVO_OnlyIdAndName();
                    vo.setCatelogId(id);
                    vo.setCatelogName(name);
                    finale.add(vo);
                }
                return finale;
            }








#  商品维护II：发布商品/基本信息，查询与分类绑定的所有品牌和属性 


在发布商品的第二个页面，选择好商品基本信息后，会发出请求：

            /product/attrgroup/{catelogId}/withattr

属于attrGroup下面
根据一个catelogId获取与之关联的所有属性和参数

响应：

            {
            	"msg": "success",
            	"code": 0,
            	"data": [{
            		"attrGroupId": 1,
            		"attrGroupName": "主体",
            		"sort": 0,
            		"descript": "主体",
            		"icon": "dd",
            		"catelogId": 225,
            		"attrs": [{
            			"attrId": 7,
            			"attrName": "入网型号",
            			"searchType": 1,
            			"valueType": 0,
            			"icon": "xxx",
            			"valueSelect": "aaa;bb",
            			"attrType": 1,
            			"enable": 1,
            			"catelogId": 225,
            			"showDesc": 1,
            			"attrGroupId": null
            			}, {
            			"attrId": 8,
            			"attrName": "上市年份",
            			"searchType": 0,
            			"valueType": 0,
            			"icon": "xxx",
            			"valueSelect": "2018;2019",
            			"attrType": 1,
            			"enable": 1,
            			"catelogId": 225,
            			"showDesc": 0,
            			"attrGroupId": null
            			}]
            		},
            		{
            		"attrGroupId": 2,
            		"attrGroupName": "基本信息",
            		"sort": 0,
            		"descript": "基本信息",
            		"icon": "xx",
            		"catelogId": 225,
            		"attrs": [{
            			"attrId": 11,
            			"attrName": "机身颜色",
            			"searchType": 0,
            			"valueType": 0,
            			"icon": "xxx",
            			"valueSelect": "黑色;白色",
            			"attrType": 1,
            			"enable": 1,
            			"catelogId": 225,
            			"showDesc": 1,
            			"attrGroupId": null
            		}]
            	}]
            }


返回的是groupEntity的集合，但是其中包含了参数的集合
因此定义VO：

            @Data
            public class AttrGroupVO_WithAttrs {
            private static final long serialVersionUID = 1L;
                @TableId
                private Long attrGroupId;
                private String attrGroupName;
                private Integer sort;
                private String descript;
                private String icon;
                private Long catelogId;
            
                private List<AttrEntity> attrs;
            }

多了一个AttrEntity的集合
返回值就是这个VO的集合



在AttrGroupController中：

            /**
             * 
             * @param catelogId
             * @return
             * 
             * 根据catelogId，获取对应的属性，和属性对应的参数
             * 
             * 返回值为一个vo
             * 
             */
        
            @RequestMapping("{catelogId}/withattr")
            public R listAttrGroupsAndAttrs(@PathVariable Long catelogId){
                List<AttrGroupVO_WithAttrs> finale=attrGroupService.getAttrGroupWithsAttrs(catelogId);
                return R.ok().put("data",finale);
            }

自定义方法getAttrGroupWithAttrs：

            /**
             *
             * @param catelogId
             * @return
             *
             * 根据catelogId，查询所有与之关联的属性和参数
             *
             * 返回值格式上，属性属于一级
             * 参数属于二级，且该参数必须属于该属性
             *
             * 需要本家的dao
             * 和attr的dao
             *
             */
            @Override
            public List<AttrGroupVO_WithAttrs> getAttrGroupWithsAttrs(Long catelogId) {
                //先获取对应的groupEntity
                List<AttrGroupEntity> attrGroupEntities=attrGroupDao.selectList(new QueryWrapper<AttrGroupEntity>().eq("catelog_id",catelogId));
                //最终返回值
                List<AttrGroupVO_WithAttrs> finale=new ArrayList<>();
                //遍历属性集合
                for(AttrGroupEntity groupEntity:attrGroupEntities){
                    AttrGroupVO_WithAttrs vo=new AttrGroupVO_WithAttrs();
                    BeanUtils.copyProperties(groupEntity,vo);
                    //复制基础参数
                    Long groupId=vo.getAttrGroupId();
                    List<AttrAttrgroupRelationEntity> relationEntities=attrAttrgroupRelationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_group_id",groupId));
                    //获取了与groupId对应的关系对象集合
                    List<AttrEntity> attrEntities=new ArrayList<>();
                    for(AttrAttrgroupRelationEntity entity:relationEntities){
                        Long attrId=entity.getAttrId();
                        attrEntities.add(attrDao.selectById(attrId));
                    }
                    vo.setAttrs(attrEntities);
                    //设置参数
                    finale.add(vo);
                }
                return finale;
            }

结果正确








#  商品维护

## 商品新增 

逆向生成的所有vo，删除getter/setter，添加@Data
有关价格的全用BigDecimal
整数一律使用Long


于是在SkuInfoController中：

            /**
             *
             * @param vo
             *
             *
             * 保存有关SpuSaveVO对象
             * 该对象为生成的，接收商品数据的对象
             *
             * 专供商品维护/发布商品
             *
             *
             */
            @RequestMapping("/save")
            @RequiresPermissions("product:skuinfo:save")
            public R save(@RequestBody SpuSaveVO vo){
                skuInfoService.saveSpuVo(vo);
                return R.ok();
            }

自定义方法saveSpuVo，接收SpuSaveVO对象：

            /**
             *
             * @param vo
             *
             *
             * 保存有关SpuSaveVO对象
             * 该对象为生成的，接收商品数据的对象
             *
             * 专供商品维护/发布商品
             *
             * 该方法过长，使用@Transactional注解保证同步进行
             *
             */
            @Transactional
            @Override
            public void saveSpuVo(SpuSaveVO vo) {
                /** 分为几个部分
                 // 1.spu基本信息，存在spu_info中 */
        
                SpuInfoEntity spuInfoEntity=new SpuInfoEntity();
                BeanUtils.copyProperties(vo,spuInfoEntity);
                spuInfoEntity.setCreateTime(new DateTime());
                spuInfoEntity.setUpdateTime(new DateTime());
                spuInfoEntity.setPublishStatus(0);
                spuInfoDao.insert(spuInfoEntity);
        
                /** 2.spu描述图片，存在spu_info_desc */
        
                SpuInfoDescEntity spuInfoDescEntity=new SpuInfoDescEntity();
                spuInfoDescEntity.setSpuId(spuInfoEntity.getId());
                //直接get spuInfoEntity的id
                //因为上面的方法先行执行，该spu已经得到了自增的id
                spuInfoDescEntity.setDecript(String.join(",",vo.getDecript()));
                spuInfoDescDao.insert(spuInfoDescEntity);
        
                /** 3.spu图片集，存在spu_images */
        
                //注意images是集合，需要遍历添加
                List<String> images=vo.getImages();
                for(String image:images) {
                    SpuImagesEntity spuImagesEntity = new SpuImagesEntity();
                    spuImagesEntity.setSpuId(spuInfoEntity.getId());
                    spuImagesEntity.setImgUrl(image);
                    spuImagesDao.insert(spuImagesEntity);
                }
        
                /** 4.spu的参数，存在product_attr_value */
        
                List<BaseAttrs> baseAttrs=vo.getBaseAttrs();
                for(BaseAttrs attr:baseAttrs){
                    ProductAttrValueEntity entity=new ProductAttrValueEntity();
        
                    entity.setSpuId(spuInfoEntity.getId());
                    entity.setAttrId(attr.getAttrId());
                    entity.setAttrName(attrDao.selectOne(new QueryWrapper<AttrEntity>().eq("attr_id",attr.getAttrId())).getAttrName());
                    entity.setAttrValue(attr.getAttrValues());
                    entity.setQuickShow(attr.getShowDesc());
        
                    productAttrValueDao.insert(entity);
                }
        
        
                /** 5.spu的所有sku信息，包括：
                 // sku全部保存在vo的sku集合内：*/
        
                List<Skus> skus=vo.getSkus();
        
                for(Skus sku:skus) {
                    /** 5.1.sku基本信息，存在sku_info */
        
                    SkuInfoEntity skuInfoEntity=new SkuInfoEntity();
                    BeanUtils.copyProperties(sku,skuInfoEntity);
                    //只复制了skuName/skuTitle/skuSubtitle/price过去
                    skuInfoEntity.setSpuId(spuInfoEntity.getId());
                    //spu id
                    skuInfoEntity.setSkuDesc(String.join(",",sku.getDescar()));
                    //描述
                    skuInfoEntity.setBrandId(spuInfoEntity.getBrandId());
                    //brand id
                    skuInfoEntity.setCatalogId(spuInfoEntity.getCatalogId());
                    //catelog id
                    List<Images> sku_images=new ArrayList<>();
                    //这个sku_images是下面5.2的内容，现在趁着找default_image的功夫一起弄了
                    Images default_sku_image=new Images();
                    for(Images image: sku.getImages()){
                        if(image.getDefaultImg()==1){
                            default_sku_image=image;
                        }
                        else{
                            sku_images.add(image);
                        }
                    }
                    skuInfoEntity.setSkuDefaultImg(default_sku_image.getImgUrl());
                    //default image
                    skuInfoEntity.setSaleCount(0L);
                    //手动设置折扣为0
                    skuInfoDao.insert(skuInfoEntity);
        
                    /** 5.2.sku的图片信息，存在sku_images */
        
                    for(Images image:sku_images) {
                        SkuImagesEntity skuImagesEntity = new SkuImagesEntity();
                        skuImagesEntity.setSkuId(skuInfoEntity.getSkuId());
                        skuImagesEntity.setImgUrl(image.getImgUrl());
                        skuImagesEntity.setDefaultImg(image.getDefaultImg());
                        skuImagesDao.insert(skuImagesEntity);
                    }
                    SkuImagesEntity defaultSkuImagesEntity=new SkuImagesEntity();
                    defaultSkuImagesEntity.setSkuId(skuInfoEntity.getSkuId());
                    defaultSkuImagesEntity.setImgUrl(default_sku_image.getImgUrl());
                    defaultSkuImagesEntity.setDefaultImg(default_sku_image.getDefaultImg());
                    skuImagesDao.insert(defaultSkuImagesEntity);
        
                    /** 5.3.sku的销售属性，存在sku_sale_attr_value */
                    for(Attr attr: sku.getAttr()){
                        SkuSaleAttrValueEntity entity=new SkuSaleAttrValueEntity();
                        entity.setSkuId(skuInfoEntity.getSkuId());
                        entity.setAttrId(attr.getAttrId());
                        entity.setAttrName(attr.getAttrName());
                        entity.setAttrValue(attr.getAttrValue());
                        skuSaleAttrValueDao.insert(entity);
                    }
        
        
        
                    /** 5.4.sku的优惠、折扣信息，存在sms中
                     具体而言，sku_ladder，存储折扣与购买商品件数的关系
                     sku_full_reduction，存储满减的相关信息
                     sku_bounds，存储积分
                     需要用到远程调用
                     定义一个CouponFeign作为和coupon服务远程调用的接口
                     */
        
        
                    // 5.4.1，先试一下spu bounds
                    Bounds bound=vo.getBounds();
                    BigDecimal buyBound=bound.getBuyBounds();
                    BigDecimal growBound=bound.getGrowBounds();
                    //spu id:spuInfoEntity.getSpuId();
                    //现在已经有了bounds需要的参数，接下来要往coupon服务里传
                    //所以product和coupon都需要一个相同的对象，分别进行数据的传输和接收
                    //将该对象定义在common里面：
                    SpuBoundsTO spuBoundsTO=new SpuBoundsTO();
                    spuBoundsTO.setSpuId(spuInfoEntity.getId());
                    spuBoundsTO.setBuyBounds(buyBound);
                    spuBoundsTO.setGrowBounds(growBound);
                    couponFeign.saveBounds(spuBoundsTO);
        
                    //此后couponFeign将调用coupon/spubounds/save这一路径的方法，发送的参数为一个to
                    //故coupon模块中也需要将接收参数改成to
                    //同时还要自定义方法，用于处理to的数据并存储
        
        
        
                    //5.4.2，满减信息sku_full_reduction
                    //定义SkuFullReductionTO，包含的是所有优惠满减信息，也包括ladder、member price
                    //把MemberPrice类拷贝也到to包里面，加入到满减to的成员变量内
                    //随后调用feign，feign中应当有一个方法用于让coupon模块保存满减to
                    //注意需要存到full reduction、ladder和member price表内
                    SkuFullReductionTO skuFullReductionTO=new SkuFullReductionTO();
                    BeanUtils.copyProperties(sku,skuFullReductionTO);
                    skuFullReductionTO.setSkuId(skuInfoEntity.getSkuId());
                    skuFullReductionTO.setFullCount(sku.getFullCount());
                    skuFullReductionTO.setCountStatus(sku.getCountStatus());
                    System.out.println(1);
                    couponFeign.saveFullReduction(skuFullReductionTO);
                }
            }

CouponFeign:

            @FeignClient("mall-coupon")
            public interface CouponFeign {
                /**
                 * @param to
                 * @return
                 *
                 * 远程调用coupon模块
                 * 传入一个to，要求coupon模块对其进行处理并存入sms_spu_bounds表内
                 */
                @PostMapping("/coupon/spubounds/save")
                R saveBounds(@RequestBody SpuBoundsTO to);
                /**
                 * @param to
                 * @return
                 *
                 * 远程调用coupon模块
                 * 传入一个to，要求coupon模块对其进行处理并存入sms_sku_full_reduction表内
                 */
                @RequestMapping("/coupon/skufullreduction/save")
                R saveFullReduction(@RequestBody SkuFullReductionTO to);
            }

对于coupon模块内的saveBounds：
SpuBoundsController：

            /**
             * 保存
             *
             * product远程调用coupon时调用的方法
             * 接收的是一个和SpuBoundsEntity一致的to
             * 需要将其数据进行处理，并保存到spu bounds
             *
             */
            @PostMapping("/save")
            public R saveBounds(@RequestBody SpuBoundsTO to){
                spuBoundsService.saveBoundsTO(to);
                return R.ok();
            }
            
自定义方法saveBoundsTO：

             /**
             * @param to
             *
             * 和product远程通信时，product调用的方法
             * 接收的是一个to，和spu的Bounds参数一致
             * 需要保存到spu_bounds表内
             */
            @Override
            public void saveBoundsTO(SpuBoundsTO to) {
                SpuBoundsEntity entity=new SpuBoundsEntity();
                BeanUtils.copyProperties(to,entity);
                spuBoundsDao.insert(entity);
            }


而对于saveFullReduction：
SkuFullReductionController：

             /**
             * product调用的方法
             *
             * 传入一个满减的to
             * 要求对其进行保存
             * 保存到full reduction、ladder和member price
             */
            @PostMapping("/save")
            public R saveFullReduction(@RequestBody SkuFullReductionTO to){
                skuFullReductionService.saveFullReduction(to);
                return R.ok();
            }
    
自定义方法saveFullReduction：

            /**
             *
             * @param to
             *
             * product调用的方法
             * 传入一个满减的to
             * 要求进行处理，并保存到reduction、ladder和member price
             *
             */
            @Override
            public void saveFullReduction(SkuFullReductionTO to) {
                SkuFullReductionEntity skuFullReduction=new SkuFullReductionEntity();
                BeanUtils.copyProperties(to,skuFullReduction);
                skuFullReductionDao.insert(skuFullReduction);
        
                SkuLadderEntity skuLadder=new SkuLadderEntity();
                BeanUtils.copyProperties(to,skuLadder);
                skuLadder.setAddOther(to.getCountStatus());
                skuLadderDao.insert(skuLadder);
        
                for(MemberPrice price:to.getMemberPrice()){
                    MemberPriceEntity memberPriceEntity=new MemberPriceEntity();
                    memberPriceEntity.setSkuId(to.getSkuId());
                    memberPriceEntity.setMemberLevelId(price.getId());
                    memberPriceEntity.setMemberLevelName(price.getName());
                    memberPriceEntity.setMemberPrice(price.getPrice());
                    memberPriceDao.insert(memberPriceEntity);
                }
            }



这个方法的结果是：成功保存



注意：远程调用服务中传输对象数据时必须要求所有字段都不为空











## 商品新增的优化





1.发现在添加images的时候，总会有空的，所以需要进行判断url是否非空
将5.1遍历images的代码块：

            Images default_sku_image=new Images();
            for(Images image: sku.getImages()){
                if(image.getDefaultImg()==1){
                    default_sku_image=image;
                }
                else{
                    sku_images.add(image);
                }
            }
            skuInfoEntity.setSkuDefaultImg(default_sku_image.getImgUrl());

改为：

            Images default_sku_image=new Images();
            for(Images image: sku.getImages()){
                if(image.getDefaultImg()==1){
                    default_sku_image=image;
                }
                else if(!StringUtils.isEmpty(image.getImgUrl())){
                    sku_images.add(image);
                }
            }
            skuInfoEntity.setSkuDefaultImg(default_sku_image.getImgUrl());


可以看到，在add之前还加上了一层判断，可以过滤url为空的String，只将非空的字符串加入到images集合中





2.随后注意到，在sku_ladder表内，满0减0的数据也是没有意义的，因此给他做一个剔除
将：

            couponFeign.saveFullReduction(skuFullReductionTO);

改为：

            //如果sku内满减fullCount和discount都为0，则不发起远程请求，也即是不需要添加至表内
            //只有二者有一个有意义，那就说明该优惠价有意义，加入表内
            if(skuFullReductionTO.getFullCount().compareTo(BigDecimal.ZERO)==1 || skuFullReductionTO.getDiscount().compareTo(BigDecimal.ZERO)==1 ) {
                couponFeign.saveFullReduction(skuFullReductionTO);
            }

(注意BigDecimal的比较规则)
表示如果该优惠券有意义，才加入表内，否则不加入
而这样一来，fulCount大于0，说明是满减；如果是discount大于0，则说明是打折。
故在coupon模块的saveFullReduction内的方法做判断：

            SkuFullReductionEntity skuFullReduction=new SkuFullReductionEntity();
            BeanUtils.copyProperties(to,skuFullReduction);
            //满减
            if(to.getFullCount().compareTo(BigDecimal.ZERO)==1){
                skuFullReductionDao.insert(skuFullReduction);
            }

            SkuLadderEntity skuLadder=new SkuLadderEntity();
            BeanUtils.copyProperties(to,skuLadder);
            //打折
            if(to.getDiscount().compareTo(BigDecimal.ZERO)==1) {
                skuLadder.setAddOther(to.getCountStatus());
                skuLadderDao.insert(skuLadder);
            }

如此便做出了判断




3.同样可以发现，memberPrice里面有的为0，因此在SpuInfoService里面进行过滤：

            for(MemberPrice price:to.getMemberPrice()){
                if(price.getPrice().compareTo(BigDecimal.ZERO)==1) {
                    MemberPriceEntity memberPriceEntity = new MemberPriceEntity();
                    memberPriceEntity.setSkuId(to.getSkuId());
                    memberPriceEntity.setMemberLevelId(price.getId());
                    memberPriceEntity.setMemberLevelName(price.getName());
                    memberPriceEntity.setMemberPrice(price.getPrice());
                    memberPriceDao.insert(memberPriceEntity);
                }
            }

只有当价格大于0时，才会通过该price实例化MemberPriceEntity对象并加入表




4.该方法还有一些其他未知但可能发生的错误，例如事务回滚问题，系统不稳定问题，都放到以后再来看吧







## 检索SPU

请求路径：

            /product/spuinfo/list

请求参数：

            {
               page: 1,//当前页码
               limit: 10,//每页记录数
               sidx: 'id',//排序字段
               order: 'asc/desc',//排序方式
               key: '华为',//检索关键字
               catelogId: 6,//三级分类id
               brandId: 1,//品牌id 
               status: 0,//商品状态
            }

响应格式：

            {
            	"msg": "success",
            	"code": 0,
            	"page": {
            		"totalCount": 0,
            		"pageSize": 10,
            		"totalPage": 0,
            		"currPage": 1,
            		"list": [{
                    
            			"brandId": 0, //品牌id
            			"brandName": "品牌名字",
            			"catalogId": 0, //分类id
            			"catalogName": "分类名字",
            			"createTime": "2019-11-13T16:07:32.877Z", //创建时间
            			"id": 0, //商品id
            			"publishStatus": 0, //发布状态
            			"spuDescription": "string", //商品描述
            			"spuName": "string", //商品名字
            			"updateTime": "2019-11-13T16:07:32.877Z", //更新时间
            			"weight": 0 //重量

            		}]
            	}
            }


SpuInfoController中：

            /**
             * 列表
             *
             * 根据param，返回spuInfoEntity的集合
             */
            @RequestMapping("/list")
            public R list(@RequestParam Map<String, Object> params){
                PageUtils page = spuInfoService.getSpuInfo(params);
                return R.ok().put("page", page);
            }

自定义方法getSpuInfo：

            /**
             *
             * @param params
             * @return
             *
             * 查询符合条件的SpuInfoEntity并封装为page返回
             *
             * 包括模糊查询
             */
            @Override
            public PageUtils getSpuInfo(@NotNull Map<String, Object> params) {
                //params里面有key、catelogId、brandId、status

                QueryWrapper<SpuInfoEntity> wrapper=new QueryWrapper<SpuInfoEntity>();
                String key=(String) params.get("key");
                if(!StringUtils.isEmpty(key)){
                    log.info("key not null: "+key);
                    wrapper.and(w->{
                       w.like("spu_name",key).or().like("spu_description",key).or().like("id",key);
                    });
                }
                String  catelogId=(String) params.get("catelogId");
                if(catelogId!=null && !"0".equalsIgnoreCase(catelogId)){
                    log.info("catelogId not null: "+catelogId);
                    wrapper.eq("catalog_id",catelogId);
                }
                String  brandId=(String) params.get("brandId");
                if(brandId!=null && !"0".equalsIgnoreCase(brandId)){
                    log.info("brandId not null: "+brandId);
                    wrapper.eq("brand_id",brandId);
                }

                String  status=(String) params.get("status");
                if(!StringUtils.isEmpty(status)) {
                    log.info("status not null: "+status);
                    wrapper.eq("publish_status", status);
                }

                IPage<SpuInfoEntity> finale = this.page(
                        new Query<SpuInfoEntity>().getPage(params),
                        wrapper
                );

                return new PageUtils(finale);

            }

匹配的顺序为：

            1.令name、description、id匹配key（用一个and方法封装）
            2.匹配catelogId，注意一个惊天大坑，数据库中该字段的名字为catalogId，为0或为空时匹配全部
            3.匹配brandId，为0或为空时匹配全部
            4.匹配status，为空的话则匹配全部




2023/9/21  19:32
修复了增加spu时publish status为空的bug



## SPU商品上架下架管理

请求路径：

            /product/spuinfo/{spuId}/up

SpuInfoController中：

             /**
             *
             * @param spuId
             * @return
             *
             * 根据spuId上架
             * 就是把publish status改为1
             *
             */
            @RequestMapping("/{spuId}/up")
            public R upSpu(@PathVariable Long spuId){
                spuInfoService.upSpu(spuId);
                return R.ok();
            }
            /**
             *
             * @param spuId
             * @return
             *
             * 顺便把下架也整一下，虽然用不到
             * 根据spuId下架
             * 就是把publish status改为2
             *
             */
            @RequestMapping("/{spuId}/down")
            public R downSpu(@PathVariable Long spuId){
                spuInfoService.downSpu(spuId);
                return R.ok();
            }

自定义方法upSpu和downSpu：

            /**
             *
             * @param spuId
             *
             * 根据spuId上架商品
             *
             *
             */
            @Override
            public void upSpu(Long spuId) {
                SpuInfoEntity entity=baseMapper.selectById(spuId);
                entity.setPublishStatus(1);
                baseMapper.updateById(entity);
            }
            /**
             *
             * @param spuId
             *
             * 根据spuId下架商品
             *
             *
             */
            @Override
            public void downSpu(Long spuId) {
                SpuInfoEntity entity=baseMapper.selectById(spuId);
                entity.setPublishStatus(2);
                baseMapper.updateById(entity);
            }

成功




            




## 检索SKU

请求路径：

            /product/skuinfo/list

请求参数：

            {
                page: 1,//当前页码
                limit: 10,//每页记录数
                sidx: 'id',//排序字段
                order: 'asc/desc',//排序方式
                key: '华为',//检索关键字
                catelogId: 0,
                brandId: 0,
                min: 0,
                max: 0
            }

响应格式：

            {
            	"msg": "success",
            	"code": 0,
            	"page": {
            		"totalCount": 26,
            		"pageSize": 10,
            		"totalPage": 3,
            		"currPage": 1,
            		"list": [{
            			"skuId": 1,
            			"spuId": 11,
            			"skuName": "华为 HUAWEI Mate 30 Pro 星河银 8GB+256GB",
            			"skuDesc": null,
            			"catalogId": 225,
            			"brandId": 9,
            			"skuDefaultImg": "https://gulimall-hello.oss-cn-beijing.aliyuncs.com/2019-11-26/60e65a44-f943-4ed5-87c8-8cf90f403018_d511faab82abb34b.jpg",
            			"skuTitle": "华为 HUAWEI Mate 30 Pro 星河银 8GB+256GB麒麟990旗舰芯片OLED环幕屏双4000万徕卡电影四摄4G全网通手机",
            			"skuSubtitle": "【现货抢购！享白条12期免息！】麒麟990，OLED环幕屏双4000万徕卡电影四摄；Mate30系列享12期免息》",
            			"price": 6299.0000,
            			"saleCount": 0
            		}]
            	}
            }


和上一个查询SpuInfo几乎一模一样
SkuInfoController中：

            /**
             * 列表
             *
             * 根据param列出sku信息
             * param中的参数有：key、catelogId、brandId、min、max
             * 注意表内的字段名还是catalogId
             *
             */
        
            @RequestMapping("/list")
            public R list(@RequestParam Map<String, Object> params){
                PageUtils page = skuInfoService.getSkuInfo(params);
                return R.ok().put("page", page);
            }

自定义方法getSkuInfo：

             /**
             * 列表
             *
             * 根据param列出sku信息
             * param中的参数有：key、catelogId、brandId、min、max
             * 注意表内的字段名还是catalogId
             *
             */
            @Override
            public PageUtils getSkuInfo(Map<String, Object> params) {
                QueryWrapper<SkuInfoEntity> wrapper=new QueryWrapper<>();
                String key=(String) params.get("key");
                if(!StringUtils.isEmpty(key)){
                    log.info("key not null: "+key);
                    wrapper.and(w->{
                        w.like("sku_name",key).or().like("sku_title",key).or().like("sku_id",key).or().like("sku_subtitle",key);
                    });
                }
                String  catelogId=(String) params.get("catelogId");
                if(catelogId!=null && !"0".equalsIgnoreCase(catelogId)){
                    log.info("catelogId not null: "+catelogId);
                    wrapper.eq("catalog_id",catelogId);
                }
                String  brandId=(String) params.get("brandId");
                if(brandId!=null && !"0".equalsIgnoreCase(brandId)){
                    log.info("brandId not null: "+brandId);
                    wrapper.eq("brand_id",brandId);s
                }
                String max=(String)params.get("max");
                String min=(String)params.get("min");
                if(!StringUtils.isEmpty(max)){
                    wrapper.le("price",max);
                }
                if(!StringUtils.isEmpty(min)){
                    wrapper.ge("price",min);
                }
                IPage<SkuInfoEntity> finale = this.page(
                        new Query<SkuInfoEntity>().getPage(params),
                        wrapper
                );
                return new  PageUtils(finale);
            }

匹配的顺序为：

            1.令name、title、subtitle、id匹配key（用一个and方法封装）
            2.匹配catalogId，为0或为空时匹配全部
            3.匹配brandId，为0或为空时匹配全部
            4.令price匹配min和max，若min、max有一个为空或min和max数值相同时，匹配全部，否则price需要在min和max之间





# 仓库管理

## 仓库列表


首先ware服务加入网关

请求路径：

            /ware/wareinfo/list

请求参数：

            {
               page: 1,//当前页码
               limit: 10,//每页记录数
               sidx: 'id',//排序字段
               order: 'asc/desc',//排序方式
               key: '华为'//检索关键字
            }

包含一个关键字key用于模糊查询

响应格式：

            {
            	"msg": "success",
            	"code": 0,
            	"page": {
            		"totalCount": 0,
            		"pageSize": 10,
            		"totalPage": 0,
            		"currPage": 1,
            		"list": [{
            			"id": 2,
            			"name": "aa",
            			"address": "bbb",
            			"areacode": "124"
            		}]
            	}
            }

ware服务中，WareInfoController：

            /**
             * 列表
             * 要求可以根据key模糊查询
             *
             */
            @RequestMapping("/list")
            @RequiresPermissions("ware:wareinfo:list")
            public R list(@RequestParam Map<String, Object> params){
                PageUtils page = wareInfoService.getWareInfo(params);
                return R.ok().put("page", page);
            }

自定义方法getWareInfo：

            /**
             *
             * @param params
             * @return
             *
             * 根据param获取合法的wareInfo
             * params包含一个key，用于模糊查询
             *
             */
            @Override
            public PageUtils getWareInfo(Map<String, Object> params) {
                QueryWrapper<WareInfoEntity> wrapper=new QueryWrapper<>();
                String key=(String) params.get("key");
                if(!StringUtils.isNullOrEmpty(key)){
                    wrapper.like("name",key).or().like("address",key).or().like("areacode",key);
                }
                IPage<WareInfoEntity> finale = this.page(
                        new Query<WareInfoEntity>().getPage(params),
                        wrapper
                );
                return new PageUtils(finale);
            }

结果是可以模糊查询







## 查询库存

请求路径：

            /ware/waresku/list

请求参数：

            {
               page: 1,//当前页码
               limit: 10,//每页记录数
               sidx: 'id',//排序字段
               order: 'asc/desc',//排序方式
               wareId: 123,//仓库id
               skuId: 123//商品id
            }

要求根据wareId和skuId查询合法的商品
WareSkuController中：

            /**
             * 列表
             * params中包含wareId和skuId
             * 要求根据这俩查询sku
             */
            @RequestMapping("/list")
            @RequiresPermissions("ware:waresku:list")
            public R list(@RequestParam Map<String, Object> params){
                PageUtils page = wareSkuService.getSkuInfo(params);

                return R.ok().put("page", page);
            }

自定义方法getSkuInfo：

             /**
             * 列表
             * params中包含wareId和skuId
             * 要求根据这俩查询sku
             */
            @Override
            public PageUtils getSkuInfo(Map<String, Object> params) {
                String wareId=(String) params.get("wareId");
                String skuId=(String) params.get("skuId");
                QueryWrapper<WareSkuEntity> wrapper=new QueryWrapper<>();
                if(!StringUtils.isNullOrEmpty(wareId)){
                    wrapper.eq("ware_id",wareId);
                }
                if(!StringUtils.isNullOrEmpty(skuId)){
                    wrapper.eq("sku_id",skuId);
                }
                IPage<WareSkuEntity> finale = this.page(
                        new Query<WareSkuEntity>().getPage(params),
                        wrapper
                );
                return new PageUtils(finale);
            }

结果是可以查询




## 采购需求

请求路径：

            /ware/purchasedetail/list

请求参数：

            {
               page: 1,//当前页码
               limit: 10,//每页记录数
               sidx: 'id',//排序字段
               order: 'asc/desc',//排序方式
               key: '华为',//检索关键字
               status: 0,//状态    
               wareId: 1,//仓库id
            }

其中包含key、status和wareId

PurchaseDetailController中：

            /**
             * 列表
             *
             * 根据status和wareId查询采购需求
             */
            @RequestMapping("/list")
            @RequiresPermissions("ware:purchasedetail:list")
            public R list(@RequestParam Map<String, Object> params){
                PageUtils page = purchaseDetailService.getPurchaseDetails(params);

                return R.ok().put("page", page);
            }

自定义方法getPurchaseDetails：

             /**
             * 列表
             *
             * 根据status和wareId查询采购需求
             */
            @Override
            public PageUtils getPurchaseDetails(Map<String, Object> params) {
                QueryWrapper<PurchaseDetailEntity> wrapper=new QueryWrapper<>();
                String key=(String)params.get("key");
                String status=(String) params.get("status");
                String wareId=(String) params.get("wareId");

                if(!StringUtils.isNullOrEmpty(key)){
                    wrapper.and(obj->{
                       obj.like("purchase_id",key).or().eq("sku_id",key);
                    });
                }
                if(!StringUtils.isNullOrEmpty(status)){
                    wrapper.eq("status",status);
                }
                if(!StringUtils.isNullOrEmpty(wareId)){
                    wrapper.eq("ware_id",wareId);
                }

                IPage<PurchaseDetailEntity> finale = this.page(
                        new Query<PurchaseDetailEntity>().getPage(params),
                        wrapper
                );
                return new PageUtils(finale);
            }

可以查询





##  许可管理IV：查询未领取的采购单

请求路径：

            /ware/purchase/unreceive/list

没有请求参数，就是查询状态status为0的采购单
PurchaseController：

            /**
             * 查询未领取的采购单
             * 也即是状态为0还未分配的采购单，或是状态为1刚分配给人还未处理的采购单
             */
            @RequestMapping("/unreceive/list")
            public R listUnreceive(@RequestParam Map<String, Object> params){
                PageUtils page = purchaseService.getUnreceive(params);

                return R.ok().put("page", page);
            }

自定义方法getUnreveive：

             /**
             *
             * @param params
             * @return
             *
             * 获取未领取的采购单
             * 也即是状态为0还未分配的采购单，或是状态为1刚分配给人还未处理的采购单
             */
            @Override
            public PageUtils getUnreceive(Map<String, Object> params) {
                IPage<PurchaseEntity> page = this.page(
                        new Query<PurchaseEntity>().getPage(params),
                        new QueryWrapper<PurchaseEntity>().eq("status",0).or().eq("status",1)
                );
                return new PageUtils(page);
            }

成功







## 合并采购单

采购需求需要合并为采购单
请求路径：

            /ware/purchase/merge
        
请求参数：

            {
              purchaseId: 1, //整单id
              items:[1,2,3,4] //合并项集合
            }

传一个数组，表示要整合为单的所有需求
传一个purchaseId，表示整合成采购单后单的id
且如果purchaseId为空，则说明我们需要自己新建一个purchase整合需求


定义vo接收数据：

            @Data
            public class PurchaseVO_Merge {
            
                private Long purchaseId;
                private Long[] items;

            }

PurchaseController中：

            /**
             *
             * @param vo
             * @return
             *
             * 合并从采购需求为采购单
             * params包含purchaseId和数组
             * 代表要合并需求到哪一个采购单
             * 和所有需要合并到采购单的需求
             *
             * purchaseId为空时，需要自己创建一个新的采购单
             *
             */
            @RequestMapping("/merge")
            public R merge(@RequestBody PurchaseVO_Merge vo){
                purchaseService.mergePurchse(vo);
                return R.ok();
            }       

为了在自定义方法中消除魔法值，在common中再定义一个表单：

            public class WareConstant {
                public enum PurchaseStatusEnum {
                    CREATED(0,"新建"),
                    ASSIGNED(1,"已分配"),
                    RECEIVED(2,"已领取"),
                    FINISHED(3,"已完成"),
                    HASERROR(4,"异常");
                    Integer code;
                    String msg;
                    PurchaseStatusEnum(Integer code,String msg){
                        this.code=code;
                        this.msg=msg;
                    }
                    public Integer getCode() {
                        return code;
                    }
                    public void setCode(Integer code) {
                        this.code = code;
                    }
                    public String getMsg() {
                        return msg;
                    }
                    public void setMsg(String msg) {
                        this.msg = msg;
                    }
                }
                public enum PurchaseDetailStatusEnum {
                    CREATED(0,"新建"),
                    ASSIGNED(1,"已分配"),
                    RECEIVED(2,"采购中"),
                    FINISHED(3,"采购完成"),
                    HASERROR(4,"异常");
                    Integer code;
                    String msg;
                    PurchaseDetailStatusEnum(Integer code,String msg){
                        this.code=code;
                        this.msg=msg;
                    }
                    public Integer getCode() {
                        return code;
                    }
                    public void setCode(Integer code) {
                        this.code = code;
                    }
                    public String getMsg() {
                        return msg;
                    }
                    public void setMsg(String msg) {
                        this.msg = msg;
                    }
                }
            }

因此自定义方法mergePurchase：

            /**
             *
             * @param vo
             *
             * 合并从采购需求为采购单
             * params包含purchaseId和purchaseDetailId数组
             * 代表要合并需求到哪一个采购单
             * 和所有需要合并到采购单的需求
             */
            @Override
            public void mergePurchse(PurchaseVO_Merge vo) {
                Long purchaseId=vo.getPurchaseId();
                Long[] items=vo.getItems();

                if(purchaseId!=null){
                    for(Long detailId:items){
                        PurchaseDetailEntity entity=purchaseDetailDao.selectById(detailId);
                        entity.setPurchaseId(purchaseId);
                        entity.setStatus(WareConstant.PurchaseDetailStatusEnum.ASSIGNED.getCode());
                        purchaseDetailDao.updateById(entity);
                    }
                    //遍历item，修改item对应的数据
                    PurchaseEntity entity=baseMapper.selectById(purchaseId);
                    entity.setUpdateTime(new DateTime());
                    baseMapper.updateById(entity);
                    //要修改采购单的更新日期
                }
                else{
                    PurchaseEntity purchase=new PurchaseEntity();
                    purchase.setPriority(1);
                    purchase.setStatus(WareConstant.PurchaseStatusEnum.CREATED.getCode());
                    purchase.setCreateTime(new DateTime());
                    purchase.setUpdateTime(new DateTime());
                    baseMapper.insert(purchase);
                    //新建一个purchaseEntity，并存储
                    Long newPurchaseId=purchase.getId();
                    //此时才purchaseEntity已经被存入，被分配了id
                    for(Long detailId:items){
                        PurchaseDetailEntity entity=purchaseDetailDao.selectById(detailId);
                        entity.setPurchaseId(newPurchaseId);
                        entity.setStatus(WareConstant.PurchaseDetailStatusEnum.ASSIGNED.getCode());
                        purchaseDetailDao.updateById(entity);
                    }
                }
            }



结果是成功运行





## 领取采购单

请求路径：

            /ware/purchase/received

请求参数：

            [1,2,3,4]//采购单id

要求根据请求参数中采购单的id进行领取操作

PurchaseController：

            /**
             *
             * @param ids
             * @return
             *
             * 领取采购单
             * 根据前端传回的ids，改变采购单的状态status
             *
             */
            @RequestMapping("/received")
            public R receivePurchase(@RequestBody List<Long> ids){
                purchaseService.receivePurchase(ids);
                return R.ok();
            }

自定义方法receivePurchase：

            /**
             *
             * @param ids
             * @return
             *
             * 领取采购单
             * 根据前端传回的采购单的id封装为的ids，改变采购单的状态status
             *
             */
            @Override
            public void receivePurchase(List<Long> ids) {
                //分为三步：
                //1.判断id对应的purchase是否为已分配状态
                //2.改变purchase的状态为已领取
                //3.改变每个purchase对应的purchaseDetails的状态为采购中

                for(Long id:ids){
                    PurchaseEntity purchase=baseMapper.selectById(id);
                    if(purchase.getStatus()==WareConstant.PurchaseStatusEnum.ASSIGNED.getCode()){
                        //只有已分配、未领取的采购单才会进入更改流程
                        purchase.setStatus(WareConstant.PurchaseStatusEnum.RECEIVED.getCode());
                        //更改采购单的状态为已领取
                        List<PurchaseDetailEntity> purchaseDetailEntities=purchaseDetailDao.selectList(new QueryWrapper<PurchaseDetailEntity>().eq("purchase_id",id));
                        //获取当前循环中采购单对应的所有采购需求
                        for(PurchaseDetailEntity entity:purchaseDetailEntities){
                            entity.setStatus(WareConstant.PurchaseDetailStatusEnum.RECEIVED.getCode());
                            purchaseDetailDao.updateById(entity);
                        }
                        //将当前循环对应的采购需求的状态改为采购中
                    }
                }
            }

用apifox测试一下：

请求路径：

            localhost:10100/api/ware/purchase/received

请求参数：

            [5]

数据库原本：

            purchase:
            5,1,admin,13612345678,1,1,,,2023-09-22 13:55:18,2023-09-22 15:34:21
            pruchase_detail:
            1,5,5,80,,1,1
            2,5,6,100,,2,1

变为：

            purchase:
            5,1,admin,13612345678,1,2,,,2023-09-22 13:55:18,2023-09-22 15:36:15
            purchase_detail:
            1,5,5,80,,1,2
            2,5,6,100,,2,2

可以看到成功了






## 完成采购

请求路径：

            /ware/purchase/done

请求参数：

            {
               id: 123,//采购单id
               items: [{itemId:1,status:4,reason:""}]//完成/失败的需求详情
            }

要求根据采购单id进行采购完成，将采购单状态改为已完成
并将对应的所有采购需求改为采购完成
此外若items不为空，需要更改items中对应的采购需求的状态为采购失败

定义VO：

            @Data
            public class PurchaseVO_IdAndErrorPurchaseDetail {
                private Long id;
                private List<ErrorItem> items;
            }

            @Data
            public class ErrorItem {
                private Long itemId;
                private Long status;
                private String reason;
            }

用来接收参数

PurchaseController：

            /**
             *
             * @param vo
             * @return
             *
             * 完成采购
             * 并将对应的所有采购需求改为采购完成
             * 此外若items不为空，需要更改items中对应的采购需求的状态为采购失败，采购单也改成异常
             * 而采购成功的需求则更改状态为完成，并且入库
             *
             */
            @RequestMapping("/received")
            public R receiveDone(@RequestBody PurchaseVO_IdAndErrorPurchaseDetail vo){
                purchaseService.receiveDone(vo);
                return R.ok();
            }

自定义方法receiveDone：






需要远程调用product模块，获取skuName
定义productFeign:

            @FeignClient("mall-product")
            public interface ProductFeign {
                /**
                 * 
                 * @param skuId
                 * @return
                 * 
                 * 远程调用product的skuinfo模块，获取sku的name
                 */
                @RequestMapping("/product/skuinfo/skuName")
                String getSkuName(@RequestParam Long skuId);
            }

SkuInfoController：

            /**
             * 
             * @param skuId
             * @return
             * 
             * ware模块调用的方法
             * 根据传来的skuId
             * 查询sku的name
             */
            @RequestMapping("product/skuinfo/skuName")
            public String getSkuName(@RequestParam Long skuId){
                return skuInfoService.getSkuName(skuId);
            }

自定义方法getSkuName：

            /**
             *
             * @param skuId
             * @return
             *
             * ware模块调用的方法
             * 根据传来的skuId
             * 查询sku的name
             */
            @Override
            public String getSkuName(Long skuId) {
                SkuInfoEntity entity=baseMapper.selectById(skuId);
                return entity.getSkuName();
            }

一定要记得网关配置，测试时网关、ware、product都要重启





PurchaseController：

            /**
             *
             * @param vo
             * @return
             *
             * 完成采购
             * 并将对应的所有采购需求改为采购完成
             * 此外若items不为空，需要更改items中对应的采购需求的状态为采购失败，采购单也改成异常
             * 而采购成功的需求则更改状态为完成，并且入库
             *
             */
            @RequestMapping("/done")
            public R receiveDone(@RequestBody PurchaseVO_IdAndErrorPurchaseDetail vo){
                purchaseService.donePurchase(vo);
                return R.ok();
            }

自定义方法donePurchase:

            /**
             *
             * @param vo
             * @return
             *
             * 完成采购
             * 并将对应的所有采购需求改为采购完成
             * 此外若items不为空，需要更改items中对应的采购需求的状态为采购失败，采购单也改成异常
             * 而采购成功的需求则更改状态为完成，并且入库
             * 入库则需要WareSkuDao
             *
             */
            @Autowired
            ProductFeign productFeign;
            @Override
            public void donePurchase(PurchaseVO_IdAndErrorPurchaseDetail vo) {
                Long purchaseId=vo.getId();
                List<ErrorItem> errors=vo.getItems();
                List<Long> errorIds=new ArrayList<>();
                if(errors!=null) {
                    for (ErrorItem error : errors) {
                        errorIds.add(error.getItemId());
                    }
                }
                //获取所有采购失败的需求对象的id

                PurchaseEntity purchaseEntity=baseMapper.selectById(purchaseId);
                //获取采购单对象
                List<PurchaseDetailEntity> purchaseDetailEntities=purchaseDetailDao.selectList(new QueryWrapper<PurchaseDetailEntity>().eq("purchase_id",purchaseId));
                //获取对应的全部需求对象

                Boolean flag=true;
                //判断该采购单是否全部完成采购

                for(PurchaseDetailEntity entity:purchaseDetailEntities){
                    Long itemId=entity.getId();
                    if(errorIds.contains(itemId)&&!errorIds.isEmpty()){
                        //如果该需求属于异常需求的集合，修改状态为异常，不入库
                        entity.setStatus(WareConstant.PurchaseDetailStatusEnum.HASERROR.getCode());
                        purchaseDetailDao.updateById(entity);
                        flag=false;
                    }
                    else{
                        //若该需求正常完成采购，入库
                        entity.setStatus(WareConstant.PurchaseDetailStatusEnum.FINISHED.getCode());
                        //入库的思路是：
                        //查询该需求的wareId和skuId
                        //先去到wareId对应的仓库，在从仓库中查询skuId
                        //若skuId存在，即该仓库存在同样商品的库存，则直接对该库存数量stock进行增加
                        //若不存在，则新建一个
                        WareSkuEntity wareSkuEntity=wareSkuDao.selectOne(new QueryWrapper<WareSkuEntity>().eq("ware_id",entity.getWareId()).eq("sku_id",entity.getSkuId     ()));
                        if(wareSkuEntity==null){
                            //若库存中不存在同一仓库的统一商品，则新创建一个商品入库
                            //而wareId是添加需求时必须指定的，因此默认一件需求必有wareId
                            WareSkuEntity newEntity=new WareSkuEntity();
                            newEntity.setSkuId(entity.getSkuId());
                            newEntity.setWareId(entity.getWareId());
                            newEntity.setStock(entity.getSkuNum());
                            //查询sku的name需要远程调用product的skuInfo，利用skuID查询
                            log.info("Sku Id:"+entity.getSkuId());
                            newEntity.setSkuName(productFeign.getSkuName(entity.getSkuId()));
                            newEntity.setStockLocked(0);
                            //完成了sku的定义
                            wareSkuDao.insert(newEntity);
                            //完成入库
                        }
                        else{
                            //若库存中存在同一商品，则加到商品数量上
                            Integer stock=wareSkuEntity.getStock();
                            wareSkuEntity.setStock(stock+entity.getSkuNum());
                            wareSkuDao.updateById(wareSkuEntity);
                        }
                        purchaseDetailDao.updateById(entity);
                        //更新需求状态

                    }
                    //若该需求正常采购完成，入库
                }

                if(flag){
                    purchaseEntity.setStatus(WareConstant.PurchaseStatusEnum.FINISHED.getCode());
                    //若flag为true，即没有采购失败的需求，则purchase的状态为完成
                }
                else{
                    purchaseEntity.setStatus(WareConstant.PurchaseStatusEnum.HASERROR.getCode());
                    //若flag为false，即有采购失败的需求，则purchase的状态为有异常
                }

                purchaseEntity.setUpdateTime(new DateTime());
                //统一更新时间

                baseMapper.updateById(purchaseEntity);
                //更新数据

            }







测试一下：

请求参数：

            {
                "id":5
            }

数据库中，原本的数据：

            purchase：
            5,1,admin,13612345678,1,2,,,2023-09-22 13:55:18,2023-09-22 15:36:15

            purchase_detail：
            1,5,5,80,,1,2
            2,5,6,100,,2,2

变成：

            purchase：
            5,1,admin,13612345678,1,3,,,2023-09-22 13:55:18,2023-09-22 18:07:53

            purchase_detail：
            1,5,5,80,,1,3
            2,5,6,100,,2,3

            ware_sku多出：
            4,6,2,100,华为 HUAWEI Mate 30 Pro 翡冷翠 8GB+128GB,0
            5,5,1,80,华为 HUAWEI Mate 30 Pro 翡冷翠 8GB+256GB,0


运行成功











## 获取Spu规格

请求路径：

            /product/attr/base/listforspu/{spuId}

响应格式：

            {
            	"msg": "success",
            	"code": 0,
            	"data": [{
            		"id": 43,
            		"spuId": 11,
            		"attrId": 7,
            		"attrName": "入网型号",
            		"attrValue": "LIO-AL00",
            		"attrSort": null,
            		"quickShow": 1
            	}]
            }



AttrController：

            /**
             *
             * @param spuId
             * @return
             *
             * 根据spuId查询与之关联的所有参数
             *
             */
            @RequestMapping("/base/listforspu/{spuId}")
            public R getSpuList(@PathVariable Long spuId){
                List<ProductAttrValueEntity> page= attrService.getSpuById(spuId);
                return R.ok().put("data",page);
            }

自定义方法getSpuById：

            /**
             *
             * @param spuId
             * @return
             *
             * 根据spuId查询与之关联的所有参数
             * 需要ProductAttrValueDao
             *
             */
            @Override
            public List<ProductAttrValueEntity> getSpuById(Long spuId) {
            
                List<ProductAttrValueEntity> finale=productAttrValueDao.selectList(new QueryWrapper<ProductAttrValueEntity>().eq("spu_id",spuId));
                return finale;

            }

点进spu规格时，可以看到回显















## 修改Spu规格

请求路径：

            /product/attr/update/{spuId}

请求参数：

            [{
            	"attrId": 7,
            	"attrName": "入网型号",
            	"attrValue": "LIO-AL00",
            	"quickShow": 1
            }, {
            	"attrId": 14,
            	"attrName": "机身材质工艺",
            	"attrValue": "玻璃",
            	"quickShow": 0
            }, {
            	"attrId": 16,
            	"attrName": "CPU型号",
            	"attrValue": "HUAWEI Kirin 980",
            	"quickShow": 1
            }]

参数直接使用ProductAttrValueEntity的集合接收

AttrController：

            /**
             * @param list,spuId
             * @return
             *
             * 更新参数
             *
             * 要根据传回的spuId和productAttrValueEntity集合
             * 更新这些productAttrValueEntity
             *
             */
            @RequestMapping("/update/{spuId}")
            @RequiresPermissions("product:attr:update")
            public R updateBySpuId(@RequestBody List<ProductAttrValueEntity> list,@PathVariable Long spuId){
                attrService.updateSpuAttr(list,spuId);

                return R.ok();
            }


自定义方法updateBySpuAttr：

            /**
             * @param list,spuId
             * @return
             *
             * 更新参数
             *
             * 要根据传回的spuId和productAttrValueEntity集合
             * 更新这些productAttrValueEntity
             *
             */
            @Override
            public void updateSpuAttr(List<ProductAttrValueEntity> list, Long spuId) {
                //更新时，需要将原先的数据删除
                //删除也是根据attrId和spuId进行的
                //然后再保存新的


                for(ProductAttrValueEntity entity:list){
                    Long attrId=entity.getAttrId();
                    productAttrValueDao.delete(new QueryWrapper<ProductAttrValueEntity>().eq("attr_id",attrId).eq("spu_id",spuId));
                    entity.setSpuId(spuId);
                    productAttrValueDao.insert(entity);
                }

            }


结果可以修改























# elastic search
p103、p104

## 使用docker安装
可视化界面使用kibana

            docker pull elasticsearch:7.4.2
            docker pull kibana:7.4.2

## 创建实例化es容器：

            mkdir -p /mydata/elasticsearch/config
            mkdir -p /mydata/elasticsearch/data
            echo "http.host: 0.0.0.0" >> /mydata/elasticsearch/config/elasticsearch.yml
            chmod -R 777 /mydata/elasticsearch/ 
                (保证权限)

            docker run --name ES -p 9200:9200 -p 9300:9300 -e "discovery.type=single-node" -e ES_JAVA_OPTS="-Xms512m -Xmx512m" -v /mydata/elasticsearch/config/elasticsearch.yml:/usr/share/elasticsearch/config/elasticsearch.yml -v /mydata/elasticsearch/data:/usr/share/elasticsearch/data -v /mydata/elasticsearch/plugins:/usr/share/elasticsearch/plugins -d elasticsearch:7.4.2

            
此时es的ip和端口为192.168.74.130:9200，9300为集群环境下的和其他节点的通信端口
将/usr/share/elasticsearch/config/elasticsearch.yml挂载到/mydata/elasticsearch/config/elasticsearch.yml
将/usr/share/elasticsearch/data挂载到/mydata/elasticsearch/data
将/usr/share/elasticsearch/plugins挂载到/mydata/elasticsearch/plugins

访问：192.168.74.130:9200
查询到：

        {
          "name" : "c7dd6e29859c",
          "cluster_name" : "elasticsearch",
          "cluster_uuid" : "R-ATqFpiR4WEOLpB8avx0g",
          "version" : {
            "number" : "7.4.2",
            "build_flavor" : "default",
            "build_type" : "docker",
            "build_hash" : "2f90bbf7b93631e52bafb59b3b049cb44ec25e96",
            "build_date" : "2019-10-28T20:40:44.881551Z",
            "build_snapshot" : false,
            "lucene_version" : "8.2.0",
            "minimum_wire_compatibility_version" : "6.8.0",
            "minimum_index_compatibility_version" : "6.0.0-beta1"
          },
          "tagline" : "You Know, for Search"
        }

表示安装成功了
注意卸载重写创建es容器时，要删除mydata/elasticsearch里的文件

## 创建实例化kibana容器：

            docker run --name kibana -e ELASTICSEARCH_HOSTS=http://192.168.74.130:9200 -p 5601:5601 -d kibana:7.4.2

此时kibana的ip和端口为：192.168.74.130:5601
访问该地址，出现kibana的图形化界面表示成功了










# es入门
 p105


## _cat命令

      node/health/master/indices


## PUT（保存）

类似于在关系型数据库中：创建数据库、创建表、存入数据
PUT命令是在es中：进入索引、进入文档、存入数据

例如：

      192.168.74.130:9200/customer/external/1

请求方式为PUT或POST，参数为：

      {
          "String":"Hello Elastic Search!"
      }

保存成功后，响应：

      {
          "_index": "customer",
          "_type": "external",
          "_id": "1",
          "_version": 1,
          "result": "created",
          "_shards": {
              "total": 2,
              "successful": 1,
              "failed": 0
          },
          "_seq_no": 0,
          "_primary_term": 1
      }



## GET

类似于关系型数据库中查询数据，
请求方式为GET

            192.168.74.130:9200/customer/external/1

响应为：

      {
          "_index": "customer",
          "_type": "external",
          "_id": "1",
          "_version": 1,
          "_seq_no": 0,
          "_primary_term": 1,
          "found": true,
          "_source": {
              "String": "Hello Elastic Search!"
          }
      }




## PUT（更新）


PUT也可以更新，例如已经有了一个 192.168.74.130:9200/customer/external/1 ，再次发送：



            {
                "String":"Hello Elastic Search!",
                "description":"updated"
            }

再次GET查询：

            {
                "_index": "customer",
                "_type": "external",
                "_id": "1",
                "_version": 2,
                "_seq_no": 1,
                "_primary_term": 1,
                "found": true,
                "_source": {
                    "String": "Hello Elastic Search!",
                    "description": "updated"
                }
            }

注意到_sourse中的数据变成了更新的值，
_version也变成了2，表示该地址的数据第二个版本，
_version不能用于控制并发

_seq_no和_primary_term属于整个索引共用，是用来做乐观锁的，用来控制并发的，
例如三个线程都想修改这条数据的情况下，所有线程都必须在_seq_no为1的情况下才能修改数据，因为这个时候_seq_no还是为1
此时若线程2进入数据并修改后，version和_seq_no都+1，此时_seq_no就变成2了
后续赶到的线程1和线程3，看到_seq_no不是1，已经变成2了，不满足修改条件了，那就直接放弃修改操作了


值得注意的是，POST也可以进行修改数据，后面需要加上/_update，即：

            192.168.74.130:9200/customer/external/1/_update

它会先判断数据是否被修改：
若确实被修改才会进行修改操作，更新结果result为updated，_version和_seq_no自增
若未被修改则放弃操作，更新结果result为noop，表示未操作，且_version和_seq_no也不会自增





## DELETE删除

可以直接删除索引和数据，但是不能直接删除类型
但是情况类型里的所有数据，就相当于删除了类型





## _bulk批量操作

在所有命令后加上/_bulk就是批量操作
在kibana控制台操作：

            POST customer/external/_bulk
            {
              "index":{
                "_id":"2"
              }
            } 
            {
              "sentence":"Hello there"
            }
            {
              "index":{"_id":"3"}
            }
            {
              "sentence":"this is id 3"
            }

响应结果：

            #! Deprecation: [types removal] Specifying types in bulk requests is deprecated.
            {
              "took" : 30,
              "errors" : false,
              "items" : [
                {
                  "index" : {
                    "_index" : "customer",
                    "_type" : "external",
                    "_id" : "2",
                    "_version" : 1,
                    "result" : "created",
                    "_shards" : {
                      "total" : 2,
                      "successful" : 1,
                      "failed" : 0
                    },
                    "_seq_no" : 2,
                    "_primary_term" : 1,
                    "status" : 201
                  }
                },
                {
                  "index" : {
                    "_index" : "customer",
                    "_type" : "external",
                    "_id" : "3",
                    "_version" : 1,
                    "result" : "created",
                    "_shards" : {
                      "total" : 2,
                      "successful" : 1,
                      "failed" : 0
                    },
                    "_seq_no" : 3,
                    "_primary_term" : 1,
                    "status" : 201
                  }
                }
              ]
            }

表示操作成功了

查询

            GET customer/external/2

响应：

            #! Deprecation: [types removal] Specifying types in document get requests is deprecated, use the /{index}/_doc/{id} endpoint instead.
            {
              "_index" : "customer",
              "_type" : "external",
              "_id" : "2",
              "_version" : 1,
              "_seq_no" : 2,
              "_primary_term" : 1,
              "found" : true,
              "_source" : {
              "sentence" : "Hello there"
              }
            }

查询：

            #! Deprecation: [types removal] Specifying types in document get requests is deprecated, use the /{index}/_doc/{id} endpoint instead.
            {
              "_index" : "customer",
              "_type" : "external",
              "_id" : "3",
              "_version" : 1,
              "_seq_no" : 3,
              "_primary_term" : 1,
              "found" : true,
              "_source" : {
                "sentence" : "this is id 3"
              }
            }


查询成功



随后在 

            PUT /bank/account/_bulk

插入数据，数据文件已给出
插入后，返回响应：

            {
                "took" : 909,
                "errors" : false,
                "items" : [
                  {
                    "index" : {
                      "_index" : "bank",
                      "_type" : "account",
                      "_id" : "1",
                      "_version" : 1,
                      "result" : "created",
                      "_shards" : {
                        "total" : 2,
                        "successful" : 1,
                        "failed" : 0
                      },
                      "_seq_no" : 0,
                      "_primary_term" : 1

                      ······

可以看到900ms，还是比较快的操作









# es进阶
p110

先开个自启动

            docker update [container id] --restart=always


## SearchAPI

### 后面加_search?条件，例如：

            GET bank/_search?q=*&sort=account_number:asc

请求方式GET，_bank表查询；
查询范围未索引bank；
q=*表示查询全部；
&是多条件
sort=account_number:asc表示按照account_number升序排列

响应体：

            {
                "took" : 43,
                "timed_out" : false,
                "_shards" : {
                  "total" : 1,
                  "successful" : 1,
                  "skipped" : 0,
                  "failed" : 0
                },
                "hits" : {
                  "total" : {
                    "value" : 1000,
                    "relation" : "eq"
                  },
                  "max_score" : null,
                  "hits" : [
                    {
                      "_index" : "bank",
                      "_type" : "account",
                      "_id" : "0",
                      "_score" : null,
                      "_source" : {
                        "account_number" : 0,
                        "balance" : 16623,
                        "firstname" : "Bradshaw",
                        "lastname" : "Mckenzie",
                        "age" : 29,
                        "gender" : "F",
                        "address" : "244 Columbus Place",
                        "employer" : "Euron",
                        "email" : "bradshawmckenzie@euron.com",
                        "city" : "Hobucken",
                        "state" : "CO"
                      },
                      "sort" : [
                        0
                      ]
                    },
                    {
                      "_index" : "bank",
                      "_type" : "account",
                      "_id" : "1",
                      "_score" : null,
                      "_source" : {
                        "account_number" : 1,
                        "balance" : 39225,
                        "firstname" : "Amber",
                    ······

会按照account_number升序排列

### Query DSL

例如不加?条件，直接传递参数，例如：

            GET bank/_search
            { 
              "query": { 
                "match": {
                    "gender": "F"
                }
              },
              "sort": [
                { 
                  "account_number": "asc"
                }
              ]，
              "from": 0, 
              "size":15 ,
              "_source": ["firstname","lastname","gender","address"]
            }


1.此处match中表示匹配gender中包含F的数据，但是注意match不可以有多个，match并不是复杂匹配

2.from表示id开始，size表示从id开始后查询几条数据

3._source表示只展示特定字段




## match查询
 p112

query里面的匹配条件，
例如：

            "match":{
                "address": "mill"
            }

表示匹配address字段中包含mill的数据

而：

            "match":{
                "address": "mill road"
            }

则表示匹配address字段中包含mill或包含road的数据





## match_phrase短语查询
 p113

也是query里面的匹配条件，
例如：

            "match_phrase":{
                "address": "mill road"
            }

表示匹配address字段包含mill road整个短语的数据



## multi_match多字段查询
 p114

也是query里面的匹配条件，
例如：

            "multi_match": 
            { 
                "query": "mill", 
                "fields": ["state","address"]
            }

表示匹配state或address中包含mill的数据




## bool符合查询
p115

也是query里的匹配条件，
但是它可以包含其他任何查询语句
例如：

            "bool": { 
                "must": [
                    { 
                        "match": { 
                            "address": "mill" 
                        } 
                    },
                    { 
                        "match": { 
                            "gender": "M" 
                        } 
                    },
                    {
                        "multi_match":{
                            "query":"B b",
                            "fields": ["lastname","firstname"]
                        }
                    }
                ]
            }

表示同时查询address包含mill、gender包含M、lastname或firstname包含有B或b的所有数据

这是must字段下
除此之外还有和must一级的should、must_not、filter字段
在查询和加分上有自己的功能
区别在于：

            1.must      查询        加分
            2.must_not  不查询      不加分
            3.should    不查询      加分
            4.filter    查询        不加分







## term匹配非文本
 p117

和match差不多的，只不过不要用match匹配非文本，也不要使用term匹配文本
而且term检索精确的非文本

            "match": { 
                "balance": "999" 
            } 

匹配balance字段为999的数据








## match keyword和match_phrase的区别
p117

例如：

            "match":{
                "address.keyword":"mill road"
            }

和：

            "match_phrase":{
                "address":"mill road"
            }

前者表示，address必须是"mill road"这个精确的字符串
但后者仅需要address中包含有"mill road"的完整字段就行









## agg聚合 
p118

可以针对query查询到的数据进行分析

例如，查询所有人的年龄分布（term），并计算出平均年龄（avg）：

            GET bank/_search
            {
              "query": {
                "match_all": {}
              }
              , 
              "from": 0,
              "size": 15,
              "aggs": {
                "Age_Term": {
                  "terms": {
                    "field": "age",
                    "size": 1000
                  }
                },
                "Age_AVG":{
                  "avg": {
                    "field": "age"
                  }
                }
              }
            }

查询结果上，除了有常规的查询数据外，还有：

          "aggregations" : {
            "Age_AVG" : {
              "value" : 30.171
            },
            "Age_Term" : {
              "doc_count_error_upper_bound" : 0,
              "sum_other_doc_count" : 0,
              "buckets" : [
                {
                  "key" : 31,
                  "doc_count" : 61
                },
                {
                  "key" : 39,
                  "doc_count" : 60
                },
                {
                  "key" : 26,
                  "doc_count" : 59
                },
                {
                  "key" : 32,
                  "doc_count" : 52
                },
                {
                  "key" : 35,
                  "doc_count" : 52
                },
                {
                  "key" : 36,
                  "doc_count" : 52
                },
                {
                  "key" : 22,
                  "doc_count" : 51
                },
                {
                  "key" : 28,
                  "doc_count" : 51
                },
                {
                  "key" : 33,
                  "doc_count" : 50
                },
                {
                  "key" : 34,
                  "doc_count" : 49
                },
                {
                  "key" : 30,
                  "doc_count" : 47
                },
                {
                  "key" : 21,
                  "doc_count" : 46
                },
                {
                  "key" : 40,
                  "doc_count" : 45
                },
                {
                  "key" : 20,
                  "doc_count" : 44
                },
                {
                  "key" : 23,
                  "doc_count" : 42
                },
                {
                  "key" : 24,
                  "doc_count" : 42
                },
                {
                  "key" : 25,
                  "doc_count" : 42
                },
                {
                  "key" : 37,
                  "doc_count" : 42
                },
                {
                  "key" : 27,
                  "doc_count" : 39
                },
                {
                  "key" : 38,
                  "doc_count" : 39
                },
                {
                  "key" : 29,
                  "doc_count" : 35
                }
              ]
            }
          }

其中可以看到Age_AVG为30.171，
后面的Age_Term都是age的分布


聚合可以嵌套，例如在上面的基础上，加上功能：计算每个年龄段的平均薪资balance
则：

            GET bank/_search
            {
              "query": {
                "match_all": {}
              },
              "aggs": {
                "Age_Term": {
                  "terms": {
                    "field": "age",
                    "size": 1000
                  },
                  "aggs": {
                    "Balance_AVG": {
                      "avg": {
                        "field": "balance"
                      }
                    }
                  }
                },
                "Age_AVG":{
                  "avg": {
                    "field": "age"
                  }
                }
              },
              "size": 0
            }

在每一个term中再次进行了一项聚合，因为该term会查询出的该年龄段的所有人，这项聚合则会根据该年龄段内所有人的balance求得平均balance
查询结果：

            "aggregations" : {
              "Age_AVG" : {
                "value" : 30.171
              },
              "Age_Term" : {
                "doc_count_error_upper_bound" : 0,
                "sum_other_doc_count" : 0,
                "buckets" : [
                  {
                    "key" : 31,
                    "doc_count" : 61,
                    "Balance_AVG" : {
                      "value" : 28312.918032786885
                    }
                  },
                  {
                    "key" : 39,
                    "doc_count" : 60,
                    "Balance_AVG" : {
                      "value" : 25269.583333333332
                    }
                  },
                  {
                    "key" : 26,
                    "doc_count" : 59,
                    "Balance_AVG" : {
                      "value" : 23194.813559322032
                    }
                  },
                  {
                    "key" : 32,
                    "doc_count" : 52,
                    "Balance_AVG" : {
                      "value" : 23951.346153846152
                    }
                  },
                  {
                    "key" : 35,
                    "doc_count" : 52,
                    "Balance_AVG" : {
                      "value" : 22136.69230769231
                    }
                  },
                  {
                    "key" : 36,
                    "doc_count" : 52,
                    "Balance_AVG" : {
                      "value" : 22174.71153846154
                    }
                  },
                  {
                    "key" : 22,
                    "doc_count" : 51,
                    "Balance_AVG" : {
                      "value" : 24731.07843137255
                    }
                  },
                  {
                    "key" : 28,
                    "doc_count" : 51,
                    "Balance_AVG" : {
                      "value" : 28273.882352941175
                    }
                  },
                  {
                    "key" : 33,
                    "doc_count" : 50,
                    "Balance_AVG" : {
                      "value" : 25093.94
                    }
                  },
                  {
                    "key" : 34,
                    "doc_count" : 49,
                    "Balance_AVG" : {
                      "value" : 26809.95918367347
                    }
                  },
                  {
                    "key" : 30,
                    "doc_count" : 47,
                    "Balance_AVG" : {
                      "value" : 22841.106382978724
                    }
                  },
                  {
                    "key" : 21,
                    "doc_count" : 46,
                    "Balance_AVG" : {
                      "value" : 26981.434782608696
                    }
                  },
                  {
                    "key" : 40,
                    "doc_count" : 45,
                    "Balance_AVG" : {
                      "value" : 27183.17777777778
                    }
                  },
                  {
                    "key" : 20,
                    "doc_count" : 44,
                    "Balance_AVG" : {
                      "value" : 27741.227272727272
                    }
                  },
                  {
                    "key" : 23,
                    "doc_count" : 42,
                    "Balance_AVG" : {
                      "value" : 27314.214285714286
                    }
                  },
                  {
                    "key" : 24,
                    "doc_count" : 42,
                    "Balance_AVG" : {
                      "value" : 28519.04761904762
                    }
                  },
                  {
                    "key" : 25,
                    "doc_count" : 42,
                    "Balance_AVG" : {
                      "value" : 27445.214285714286
                    }
                  },
                  {
                    "key" : 37,
                    "doc_count" : 42,
                    "Balance_AVG" : {
                      "value" : 27022.261904761905
                    }
                  },
                  {
                    "key" : 27,
                    "doc_count" : 39,
                    "Balance_AVG" : {
                      "value" : 21471.871794871793
                    }
                  },
                  {
                    "key" : 38,
                    "doc_count" : 39,
                    "Balance_AVG" : {
                      "value" : 26187.17948717949
                    }
                  },
                  {
                    "key" : 29,
                    "doc_count" : 35,
                    "Balance_AVG" : {
                      "value" : 29483.14285714286
                    }
                  }
                ]
              }
            }



需求升级，不仅要查出所有年龄段内人的平均薪资，还有查出不同性别的平均薪资，
则：

            GET bank/_search
            {
              "query": {
                "match_all": {}
              },
              "aggs": {
                "Age_Term": {
                  "terms": {
                    "field": "age",
                    "size": 1000
                  },
                  "aggs": {
                    "gender_agg": {
                      "terms": {
                        "field": "gender.keyword",
                        "size": 2
                      },
                      "aggs": {
                        "balance_avg": {
                          "avg": {
                            "field": "balance"
                          }
                        }
                      }
                    }
                  }
                },
                "Age_AVG":{
                  "avg": {
                    "field": "age"
                  }
                }
              }
            }

主要是在第一个年龄段的term下添加agg，对该年龄段下所有人进行聚合操作
响应：

          "aggregations" : {
            "Age_AVG" : {
              "value" : 30.171
            },
            "Age_Term" : {
              "doc_count_error_upper_bound" : 0,
              "sum_other_doc_count" : 0,
              "buckets" : [
                {
                  "key" : 31,
                  "doc_count" : 61,
                  "gender_agg" : {
                    "doc_count_error_upper_bound" : 0,
                    "sum_other_doc_count" : 0,
                    "buckets" : [
                      {
                        "key" : "M",
                        "doc_count" : 35,
                        "balance_avg" : {
                          "value" : 29565.628571428573
                        }
                      },
                      {
                        "key" : "F",
                        "doc_count" : 26,
                        "balance_avg" : {
                          "value" : 26626.576923076922
                        }
                      }
                    ]
                  }
                },
                {
                  "key" : 39,
                  "doc_count" : 60,
                  "gender_agg" : {
                    "doc_count_error_upper_bound" : 0,
                    "sum_other_doc_count" : 0,
                    "buckets" : [
                    ······

可以看到，实现了需求
在每个年龄段下，不仅有该年龄段的平均薪资
还嵌套了一层聚合，将该年龄段所有人再次分成了不同性别
而对应该年龄段下该性别的人，再次嵌套聚合，查出了该群体的平均薪资









## mapping映射 
p119

在创建索引时就对其文档映射规则进行指定，或者在索引创建新文档时也可以指定规则
但是已经存在的文档不能指定规则

例如：

            PUT /my-index{ 
                "mappings": { 
                    "properties": {
                        "age": { "type": "integer" }, 
                        "email": { "type": "keyword" }, 
                        "name": { "type": "text" }
                    }
                }
            }

创建了一个my-index索引，创建文档并指定了类型
例如age类型为integer，那么只能使用term进行匹配
email类型为keyword，那么只能使用精确匹配
name类型为text文本类型，那么就可以使用模糊匹配，同时存储时其文本会被分词器自动分词存储


在索引新创建一个指定映射关系的文档：

            PUT /my-index/_mapping{ 
                "properties": { 
                    "employee-id": { 
                        "type": "keyword", 
                        "index": false
                    }
                }
            }

其中employee-id就是新建的文档
type:keyword表示只能使用精确匹配
index:false表示该字段并不会被检索（不会被查询到）。如果不写明这一条的话，默认为true，即会被检索




## 数据迁移 
p121

将一个索引的数据迁移到另一个索引
例如：

            POST _reindex
            { 
                "source": { 
                    "index": "twitter"
                },
                "dest": { 
                    "index": "new_twitter"
                }
            }

source表示要迁移的索引
dest表示迁移至的索引
该方法就表示将twitter的数据迁移到new_twitter


但是数据迁移可以达到更新文档映射规则的作用，创建新索引时，创建旧索引的同名文档但是类型指定为新的
例如创建my-index时：

        PUT /new-my-index{ 
                "mappings": { 
                    "properties": {
                        "age": { "type": "long" }, 
                        "email": { "type": "text" }, 
                    }
                }
            }

这样旧创建好了一个新的索引new-my-index，age改为long类型，email改为text
之后进行数据迁移：

            { 
                "source": {
                    "index": "my-index", 
                    "type": "integer"
                },
                "dest": { 
                    "index": "new-my-index"
                }
            }

表示将原索引my-index下的integer类型的文档迁移到new-my-index
而integer类型的文档中，只有age一个而且是新索引的同名文档，且新索引中age的类型为long，那么age迁移到新索引时类型也变成long
如果integer类型的文档有新索引没有的，那么就单纯的迁移




## 分词 
p122


安装ik分词器
地址

            https://github.com/medcl/elasticsearch-analysis-ik

下载文件后，在windows中解压到一个lk目录
将其复制到ubuntu的 

            /mydata/elasticsearch/plugins

使用命令：

            elasticsearch-plugin list

查看是否安装了lk
安装成功后，重启es容器
在kibana中输入：

            POST _analyze
            { 
              "analyzer": "ik_smart", 
              "text": "我是中国人"
            }

让es分析每个词语
正常来说，没有安装汉语分词时，也就是不是有analyzer:ik_smart时，分出来的应该是每个汉字一个词
但是按照lk后：

            {
              "tokens" : [
                {
                  "token" : "我",
                  "start_offset" : 0,
                  "end_offset" : 1,
                  "type" : "CN_CHAR",
                  "position" : 0
                },
                {
                  "token" : "是",
                  "start_offset" : 1,
                  "end_offset" : 2,
                  "type" : "CN_CHAR",
                  "position" : 1
                },
                {
                  "token" : "中国人",
                  "start_offset" : 2,
                  "end_offset" : 5,
                  "type" : "CN_WORD",
                  "position" : 2
                }
              ]
            }

可以看到，确实分出来了


ik_smart是一种ik分词模式，表示智能分词，根据语义分词
但还有另一种模式：ik_max_word，会将所有可能的词语都分出来
例如：

            POST _analyze
            { 
              "analyzer": "ik_max_word", 
              "text": "我是中国人"
            }

结果：

            {
              "tokens" : [
                {
                  "token" : "我",
                  "start_offset" : 0,
                  "end_offset" : 1,
                  "type" : "CN_CHAR",
                  "position" : 0
                },
                {
                  "token" : "是",
                  "start_offset" : 1,
                  "end_offset" : 2,
                  "type" : "CN_CHAR",
                  "position" : 1
                },
                {
                  "token" : "中国人",
                  "start_offset" : 2,
                  "end_offset" : 5,
                  "type" : "CN_WORD",
                  "position" : 2
                },
                {
                  "token" : "中国",
                  "start_offset" : 2,
                  "end_offset" : 4,
                  "type" : "CN_WORD",
                  "position" : 3
                },
                {
                  "token" : "国人",
                  "start_offset" : 3,
                  "end_offset" : 5,
                  "type" : "CN_WORD",
                  "position" : 4
                }
              ]
            }

可以看到基本上把所有的词都分析出来了







## 自定义ik词库 
p124


### 首先下载nginx

在mydata/下创建一个nginx文件夹，进入该文件夹，用docker随意地创建一个实例：

            docker run -p 80:80 --name nginx -d nginx:1.10

这一步是为了将nginx的配置文件复制出来
复制：

            docker container cp nginx:/etc/nginx .

注意别忘了后面的空格和点
此时我们所处的文件夹是在mydata，后面的.就是表明将文件复制到当前文件夹下
之后会恰好地复制到mydata中同样名为nginx的文件夹

随后便可以关闭并移除nginx容器了

将nginx文件夹改为conf：

            mv nginx conf

然后再创建一个nginx文件夹麻将conf移动端nginx文件夹内

            mv conf nginx/

将conf整个文件夹移动到nginx下

随后再次运行nginx容器：

            docker run -p 80:80 --name nginx -v /mydata/nginx/html:/usr/share/nginx/html -v /mydata/nginx/logs:/var/log/nginx -v /mydata/nginx/conf:/etc/nginx -d nginx:1.10

访问80端口就可以看到页面了，虽然是403，但是还是会显示是nginx欢迎页面
将静态页面html挂载到 /mydata/nginx/html
将nginx所在文件夹挂载到/mydata/nginx
将配置文件conf挂载到/mydata/nginx/conf


在html文件夹下面的就是页面了，创建index.html就是默认页面
在下面创建es文件夹，表示所有和es相关的文件都存在这里面
之后使用 /es/ 就可以访问有关文件了

例如在 /html下创建 /es/fenci.txt：

            奇幻全能
            邪王之龙
            远古之龙
            全知全能之书
            刃王剑
            十圣刃

此时其路径就是：

            http://192.168.74.130/es/fenci.txt




### 自定义词库

进入/mydata/elasticsearch/plugins/lk/config里面，修改IKAnalyzer.cfg.xml：

            vim IKAnalyzer.cfg.xml

将远程词库一栏取消注释，修改为：

            <entry key="remote_ext_dict">http://192.168.74.130/es/fenci.txt</entry>

表示可以找该路径获取分词

修改完成后docker重启es和nginx

貌似不行，原因未知




# SpringBoot整合es



## 安装 
p125

使用 Elasticsearch-Rest-Client


在总服务中创建mall-search，选择java web就可以
老演员了
导入依赖：

            <!-- https://mvnrepository.com/artifact/org.elasticsearch.client/elasticsearch-rest-high-level-client -->
            <dependency>
                <groupId>org.elasticsearch.client</groupId>
                <artifactId>elasticsearch-rest-high-level-client</artifactId>
                <version>7.4.2</version>
            </dependency>

这是高阶的api
在该pom的properties中加上：

            <elasticsearch.version>7.4.2</elasticsearch.version>

表示手动规定版本为7.4.2

导入common包，要用到nacos




## 配置 
p125



application.yml添加：

            spring:
              cloud:
                nacos:
                  discovery:
                    server-addr: 192.168.74.130:8848
                    password: nacos
                    username: nacos
                    namespace: 311853ea-26c0-46e5-83e9-5d5923e1a333
              application:
                name: mall-search
            server:
                port: 10300

bootstrap添加：

            spring:
              cloud:
                nacos:
                  config:
                    server-addr: 192.168.74.130:8848
                    username: nacos
                    password: nacos
                    namespace: 311853ea-26c0-46e5-83e9-5d5923e1a333
            
随后在启动类添加@EnableDiscoveryClient
总之一定要弄好nacos注册中心和配置中心的配置


其次还要排除数据源@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)


在search模块中加上config配置类，config.ESConfig：

            @Configuration
            public class ESConfig {
                @Bean
                public RestHighLevelClient EsClient(){
                    RestHighLevelClient highLevelClient=new RestHighLevelClient(
                            RestClient.builder(
                                    new HttpHost("192.168.74.130",9200,"http")
                            )
                    );
                    return  highLevelClient;
                }
            }

工厂模式创建一个high level的client
该工厂会使用RestClient的builder方法，根据ip地址、端口号、协议建造一个client并返还给工厂调用者


定义一个test测试一下：

            @SpringBootTest
            @RunWith(SpringRunner.class)
            class MallSearchTests {
                @Autowired
                private ESClientConfiguration client;
                @Test
                void contextLoads() {
                    System.out.println(client);
                }
            }

注意加上@Runwith(SpringRunner.class)注解

测试结果：

            com.katzenyasax.mall.search.config.ESClientConfiguration$$SpringCGLIB$$0@19a544cd




## 使用 
p126


在配置类加一个通用配置：

            /**
             *  es官方建议的es通用设置
             */
            public static final RequestOptions COMMON_OPTIONS;
            static {
                RequestOptions.Builder builder = RequestOptions.DEFAULT.toBuilder();
                COMMON_OPTIONS=builder.build();
            }

然后做一个存储数据的测试：

            @Test
            void IndexData() throws IOException {
                /**
                 * 任何请求首先要定义一个IndexRequest类型的对象才能实现   
                 */
                IndexRequest indexRequest=new IndexRequest("data_test");
                indexRequest.id("1");
                //任何情况下都要使用字符串形式
                /**
                 * 将要保存的数据准备好
                 * 将其转化为json格式
                 */
                Data_Test dataTest=new Data_Test();
                String json= JSON.toJSONString(dataTest);
                indexRequest.source(json, XContentType.JSON);
                /**
                 * 进行存储
                 */
                IndexResponse indexResponse=client.EsClient().index(indexRequest,ESClientConfiguration.COMMON_OPTIONS);
                    //此时的indexResponse用于执行indexRequest
                /**
                 * 打印结果测试
                 */
                System.out.println("indexResponse:"+indexResponse.toString());

            }

Date_Test是一个实体类
运行结果：

            indexResponse:
                IndexResponse[
                    index=data_test,
                    type=_doc,
                    id=1,
                    version=1,
                    result=created,
                    seqNo=0,
                    primaryTerm=1,
                    shards={
                        "total":2,
                        "successful":1,
                        "failed":0
                    }
                ]


在kibana查询一下，在此之前是没有data_test这个索引的数据的：

            GET data_test/_search

结果：

            {
              "took" : 12,
              "timed_out" : false,
              "_shards" : {
                "total" : 1,
                "successful" : 1,
                "skipped" : 0,
                "failed" : 0
              },
              "hits" : {
                "total" : {
                  "value" : 1,
                  "relation" : "eq"
                },
                "max_score" : 1.0,
                "hits" : [
                  {
                    "_index" : "data_test",
                    "_type" : "_doc",
                    "_id" : "1",
                    "_score" : 1.0,
                    "_source" : { }
                  }
                ]
              }
            }

看到确实是成功了
_source为空，是因为保存对象之前没有设置任何值
设置一下，再保存一下：

            /**
             * 将要保存的数据准备好
             * 将其转化为json格式
             */
            Data_Test dataTest=new Data_Test();
            dataTest.setName("NAME");
            dataTest.setGender("M");
            dataTest.setAge(20);
            String json= JSON.toJSONString(dataTest);
            indexRequest.source(json, XContentType.JSON);

再次查询kibana：

            {
              "took" : 0,
              "timed_out" : false,
              "_shards" : {
                "total" : 1,
                "successful" : 1,
                "skipped" : 0,
                "failed" : 0
              },
              "hits" : {
                "total" : {
                  "value" : 1,
                  "relation" : "eq"
                },
                "max_score" : 1.0,
                "hits" : [
                  {
                    "_index" : "data_test",
                    "_type" : "_doc",
                    "_id" : "1",
                    "_score" : 1.0,
                    "_source" : {
                      "age" : 20,
                      "gender" : "M",
                      "name" : "NAME"
                    }
                  }
                ]
              }
            }

_source不为空了，同时其version也自增1了，说明这是一个更新操作











## 复杂检索 
p127


复杂查询数据，索引就用bank吧

测试：

      /**
       * 测试一下，查询数据操作
       */
      @Test
      public void SearchData() throws IOException {
          /**1.
           * 查询首先要定义一个SearchRequest对象
           * 指定查询的索引
           * 这个对象只发挥存储命令的作用
           * 不发挥构建命令、执行命令的作用
           */
          SearchRequest searchRequest=new SearchRequest();
          searchRequest.indices("bank");

          /**2.
           * 随后创建一个SearchSourceBuilder对象
           * 发挥构建命令的作用
           * 需要将其交给searchRequest，表示器存储的一切命令都来源与这个searchBuilder
           */
          SearchSourceBuilder searchSourceBuilder=new SearchSourceBuilder();
          {
              /**
               * 开始构造
               * 例如实现一个功能：
               * 查出每个年龄段人数及平均年龄，计算平均薪资balance
               * 在kibana中命令为：
               *
               *          # 查出每个年龄段人数及平均年龄，计算平均薪资balance
               *          GET bank/_search
               *          {
               *            "query": {
               *              "match_all": {}
               *            },
               *
               *            "aggs": {
               *              "Age_Term": {
               *                "terms": {
               *                  "field": "age",
               *                  "size": 1000
               *                }
               *              },
               *              "Balance_AVG":{
               *                "avg": {
               *                  "field": "balance"
               *                }
               *              }
               *            }
               *          }
               *
               * 那么构造应该是：
               */
              //这个是最外最大的query，即查询
              //此处matchAllQuery表示查询所有
              searchSourceBuilder.query(QueryBuilders.matchAllQuery());
              /**
               * 之后聚合函数
               */
              //根据年龄聚合
              TermsAggregationBuilder termsAge = AggregationBuilders.terms("Age_Term").field("age").size(1000);
              searchSourceBuilder.aggregation(termsAge);
              //求出平均薪资
              AvgAggregationBuilder avgBalance=AggregationBuilders.avg("Balance_Avg").field("balance");
              searchSourceBuilder.aggregation(avgBalance);
          }
          System.out.println("检索条件："+ searchSourceBuilder);
          searchRequest.source(searchSourceBuilder);

          /**3.
           * 执行searchRequest，使用client执行
           */
          SearchResponse searchResponse=client.EsClient().search(searchRequest,ESClientConfiguration.COMMON_OPTIONS);

          /**4.
           * 分析结果
           */
          //整个结果
          System.out.println("查询结果："+searchResponse);
          //命中结果
          SearchHits hits=searchResponse.getHits();
          SearchHit[] searchHits = hits.getHits();
          for(SearchHit hit:searchHits){
              String object= hit.getSourceAsString();
              BankAccount account=JSON.parseObject(object, BankAccount.class);
              System.out.println("account: "+account);
          }

          //运行时间
          TimeValue took = searchResponse.getTook();
          System.out.println("took: "+took.toString());

          //聚合函数分析结果
          Aggregations aggregations=searchResponse.getAggregations();
          {
              //所有年龄段
              Terms AgeAvg = aggregations.get("Age_Term");
              for (Terms.Bucket bucket : AgeAvg.getBuckets()) {
                  String object = bucket.getKeyAsString();
                  System.out.println("bucket: " + object);
              }

              //平均薪资
              Avg balanceAvg=aggregations.get("Balance_Avg");
              System.out.println("balance avg: "+balanceAvg.getValue());
          }
      }

可以看到，整个过程无非四部：

      1.创建请求对象Request，该请求对象用于存储完整的请求命令

      2.创建构造器对象RequestBuilder构造命令，并将完整的命令交予请求对象

      3.使用客户端对象Client执行命令，将执行结果封装为一个Response

      4.分析Response，获取需要的信息

以后照着这个格式抄就完事了
















# 商城业务：商品上架

## sku保存至es的文档设计 
p128


spu信息上传es的格式应该为：

      { 
        "mappings": { 
          "properties": { 
            "skuId": { 
              "type": "long"
            },
            "spuId": { 
              "type": "keyword"
            ,"skuTitle": { 
              "type": "text", 
              "analyzer": "ik_smart"
            },
            "skuPrice": { 
              "type": "keyword"
            },
            "skuImg": { 
              "type": "keyword",
              "index": false, 
              "doc_values": false
            },
            "saleCount": { 
              "type": "long"
            },
            "hasStock": { 
              "type": "boolean"
            },
            "hotScore": { 
              "type": "long"
            },
            "brandId": { 
              "type": "long"
            },
            "catalogId": { 
              "type": "long"
            },
            "brandName": { 
              "type": "keyword", 
              "index": false, 
              "doc_values": false
            },
            "brandImg": { 
              "type": "keyword", 
              "index": false, 
              "doc_values": false
            },
            "catalogName": { 
              "type": "keyword", 
              "index": false, 
              "doc_values": false
            },
            "attrs": { 
              "type": "nested", 
              "properties": { 
                "attrId": { 
                  "type": "long"
                },
                "attrName": { "
                type": "keyword", 
                "index": false, 
                "doc_values": false
                },
                "attrValue": { 
                  "type": "keyword"
                }
              }
            }
          }
        }
      }

直接将其PUT，存入到product索引：

      PUT product

响应：

      {
        "acknowledged" : true,
        "shards_acknowledged" : true,
        "index" : "product"
      }

表示成功了

再来看建的索引
其中index代表是否能被用于检索，例如spuImg，就是一串url，不应该作为检索的标识。默认为true
doc_values代表是否能用于数据分析，例如聚合、排序等，如果不能则仅仅作为展示。默认为true
hasStock表示该spu的商品有库存
hotScore表示商品热度
很重要的是，nested表示嵌入式对象

以空间换时间，并发情况下空间（硬盘）反而不那么重要了，性能应当是考虑的第一要素
这种设计下，当我们全文检索“手机”时，es会根据spuName进行匹配，从而直接将所有查到的spu数据列出
也就是说，只需要查询一次，不需要再所谓地根据spuId查询对应的attr，不需要

那么为什么不能将attrs根据spuId存入单独的索引？
因为这样一来虽然优化了空间，但是每次查询时，首先要获取spu的信息，再通过spuId查询sku信息
也就是说要经过两次检索
当数据量足够大时，将会对性能带来非常大的压力
因此并不适用于数据量特别大的应用场景







## nested嵌入式对象 
p129

为什么这么重要？
例如我需要存入一个对象数组，里面包含两个对象：

      obj1: {
        firstname:one,
        lastname:777
      }
      obj2:{
        firstname:two,
        lastname:888
      }

当没有nested标签时，es会将其处理为：

      obj:{
        firstname:[one,two],
        lastname:[777,888]
      }

将所有同名字段弄成了一个数组，而我们实际上存的“对象数组”，成了一个“字段全是数组的单个对象”
此时我们若想查询：

      match:{
        firstname:one,
        lastname:888
      }

按照我们的设想，我们查询的是一个完整的对象，而这个对象实际上并不存在
但是这样一来，es会显示可以查到，这就不符合我们的要求了
因为它每个字段都会往字段的数组里找，找不到才怪了

而使用nested标签可以禁止es将对象数组处理成字段全是数组的对象







## 上架 
p130

请求路径：

      /product/spuinfo/{spuId}/up

不仅要在mysql中上架，还要存入es


1.在common模块的to中加上SkuESModel：

      @Data
      public class SkuEsModel {
          private Long skuId;
          private Long spuId;
          private String skuTitle;
          private BigDecimal skuPrice;
          private String skuImg;
          private Long saleCount;
          private Boolean hasStock;
          private Long hotScore;
          private Long brandId;
          private Long catalogId;
          private String brandName;
          private String brandImg;
          private String catalogName;
          private List<Attrs> attrs;
          @Data
          public static class Attrs {
              private Long attrId;
              private String attrName;
              private String attrValue;
          }
      }

用于封装要存至es的数据

2.在SpuInfoService的upSpu方法中添加存入es的方法：

       /**
        *
        * @param spuId
        *
        * 根据spuId上架商品
        *
        * 功能拓展：要求上架时，将该spu的所有信息、和spuId对应的所有sku存储至es
        *
        */
        @Autowired
        WareFeign wareFeign;
        @Override
        public void upSpu(Long spuId) {
            SpuInfoEntity entity=baseMapper.selectById(spuId);
            entity.setPublishStatus(1);
            baseMapper.updateById(entity);
            /**
            * 接下来进行存储至es
            */
            //1.查出所有sku
            List<SkuInfoEntity> skus=skuInfoDao.selectList(new QueryWrapper<SkuInfoEntity>().eq("spu_id",spuId));
            /*2.处理attrs
             * 注意在这个spu下，每个sku对应的attrs都是一样的
             * 所以只需要查一遍，获取attrs后直接赋给所有sku就行
             * 并且还要注意，查的应该是search_type=1，即可以用于查询的attr
             * 在product_attr_value表中查，里面有attr和spu_id的对应关系，还有AttrEsModel要用的所有东西
             */
            List<ProductAttrValueEntity> attrEntities=this.getAttrThatCanBeSearchedBySpuId(spuId);
            List<SkuEsModel.Attrs> attrs=new ArrayList<>();
            //复制
            if((attrEntities!=null&&!attrEntities.isEmpty())) {
                //当spuId没有关联的sku时，attr直接为空集合
                for (ProductAttrValueEntity attrEntity : attrEntities) {
                    SkuEsModel.Attrs attr = new SkuEsModel.Attrs();
                    BeanUtils.copyProperties(attrEntity, attr);
                    attrs.add(attr);
                }
            }
            System.out.println(JSON.toJSONString(attrs));
            //3.封装信息
            List<SkuEsModel> skuEsModels=new ArrayList<>();
            for (SkuInfoEntity info : skus) {
                SkuEsModel model=new SkuEsModel();
                /*
                * 拷贝数据
                * 不同的字段为：
                * skuPrice     price
                * skuImg       skuDefaultImg
                *
                * 缺失的字段：
                * hasStock，关联spu的stock
                * hotScore，根据点击率获取，后端不管
                * brandName，根据brandId查
                * brandImg，根据brandId查
                * catalogName，根据catalogId查
                *
                */
                //3.1复制对象
                BeanUtils.copyProperties(info,model);
                //3.2处理不同名称字段
                model.setSkuPrice(info.getPrice());
                model.setSkuImg(info.getSkuDefaultImg());
                //3.3处理缺失字段
                model.setBrandName(brandDao.selectById(model.getBrandId()).getName());
                model.setCatalogName(categoryDao.selectById(model.getCatalogId()).getName());
                model.setBrandImg(brandDao.selectById(model.getBrandId()).getLogo());
                    //TODO 热度评分需要更复杂的操作
                    //  目前就给设置个0得了
                model.setHotScore(0L);
                //需要远程调用ware了：通过sku_id在ware的数据库的ware_sku中查询stock；若stock不为空，且有一个键值对的value不为0，则hasStock设置为true
                {
                    model.setHasStock(false);
                    Map<Long, Integer> stock = wareFeign.getStockBySkuId(model.getSkuId());
                    if (!stock.isEmpty() && stock != null) {
                        for (Map.Entry<Long, Integer> entry : stock.entrySet()) {
                            if (entry.getValue() != 0) {
                                model.setHasStock(true);
                                break;
                            }
                        }
                    }
                }
                //3.4处理attrs
                //注意每一个sku的attrs都是在一个
                model.setAttrs(attrs);
                //3.5将该循环内的model加入skuEsMedels中
                skuEsModels.add(model);
            }
            System.out.println(JSON.toJSONString(skuEsModels));
            //4.远程添加至es
            R r=searchFeign.SkuUp(skuEsModels);
            System.out.println(r.toString());
        }
      
主要干了四件事：

      1.查出spuId对应的所有sku
      2.查出spuId对应的所有可检索的attr，获取并封装成attrs
      3.遍历sku，将sku的信息封装到model内
        3.1.直接使用BeanUtils的复制功能，先复制同名的一部分数据
        3.2.处理不同名，但代表相同含义的数据
        3.3.获取缺失的字段（其中包含了跨服务操作）
        3.4.设置attrs，每个sku的attrs都相同
        3.5.将处理好数据的model加入到skuEsModel
      4.连接es，存储skuEsModel序列化好的数据
        

3.接下来进行远程传输至es
应该在mall-search中定义，ESController，在其中定义接口：

      /**
       *
       * @param skuEsModels
       * @return
       *
       * 上传skuEsModels到es
       *
       */
      @RequestMapping("/up")
      public R SpuUp(@RequestBody List<SkuEsModel> skuEsModels){
          try {
              eSService.SpuUp(skuEsModels);
              return R.ok();
          }catch (Exception e){
              log.info("SpuUP(/search/es/up) 发生错误");
              return R.error(BizCodeEnume.PRODUCT_ES_SAVE_EXCEPTION.getCode(),BizCodeEnume.PRODUCT_ES_SAVE_EXCEPTION.getMsg());
          }

          //TODO 更完善的异常反馈机制

      }

自定义方法SkuUp：

      /**
       *
       * @param skuEsModels
       * @return
       *
       * 上传skuEsModels到es
       *
       */
      @Autowired
      RestHighLevelClient highLevelClient;
      @Override
      public void SkuUp(List<SkuEsModel> skuEsModels) {
          //创建批量操作指令
          BulkRequest bulkRequest=new BulkRequest();
          for(SkuEsModel model:skuEsModels){
              //创建索引请求，表示在哪一个索引下进行操作
              IndexRequest indexRequest=new IndexRequest(ESConstant.PRODUCT_INDEX);
              //表示在该索引的哪一个id进行操作，由于此处的skuId是唯一的，因此id直接使用skuId
              indexRequest.id(model.getSkuId().toString());
              //获取存储数据的json格式
              String data= JSON.toJSONString(model);
              //指定索引请求的json请求来源
              indexRequest.source(data, XContentType.JSON);
              //将该次遍历得到的json指令拼接到总的批量操作
              bulkRequest.add(indexRequest);
          }
          //执行批量操作指令，记得抛异常
          try {
              highLevelClient.bulk(bulkRequest,ESClientConfiguration.COMMON_OPTIONS);
          } catch (IOException e) {
              log.info("！！保存失败！！");
          }
      }

接下来在product建立SearchFeign，远程调用search：

      @FeignClient("mall-search")
      public interface SearchFeign {
          /**
           *
           * @param skuEsModels
           * @return
           *
           * 上传skuEsModels到es
           *
           */
          @RequestMapping("/search/es/up")
          R SkuUp(@RequestBody List<SkuEsModel> skuEsModels);
      }

在方法中调用：

      R r=searchFeign.SkuUp(skuEsModels);

最后在整个SpuUp方法加上注解：

      @Transactional

保证整个方法执行结果一致


4.查询结果
模拟执行一个上架操作，成功之后
在kibana中查询：

      GET product/_search
      {
        "query": {
          "match_all": {}
        },
        "size": 1000
      }

应该会出现：

      {
      "took" : 0,
      "timed_out" : false,
      "_shards" : {
        "total" : 1,
        "successful" : 1,
        "skipped" : 0,
        "failed" : 0
      },
      "hits" : 
      {
        "total" : {
          "value" : 26,
          "relation" : "eq"
        },
        "max_score" : 1.0,
        "hits" : [
          {
            "_index" : "product",
            "_type" : "_doc",
            "_id" : "1",
            "_score" : 1.0,
            "_source" : 
            {
              "attrs" : [
                {
                  "attrId" : 15,
                  "attrName" : "CPU品牌",
                  "attrValue" : "以官网信息为准"
                },
                {
                  "attrId" : 16,
                  "attrName" : "CPU型号",
                  "attrValue" : "HUAWEI Kirin 980"
                }
              ],
              "brandId" : 9,
              "brandImg" : "https://kaztenyasax-mall.oss-cn-beijing.aliyuncs.com/huawei.png",
              "brandName" : "华为",
              "catalogId" : 225,
              "catalogName" : "手机",
              "hasStock" : false,
              "hotScore" : 0,
              "saleCount" : 0,
              "skuId" : 1,
              "skuImg" : "https://gulimall-hello.oss-cn-beijing.aliyuncs.com/2019-11-26/    60e65a44-f943-4ed5-87c8-8cf90f403018_d511faab82abb34b.jpg",
              "skuPrice" : 6299.0,
              "skuTitle" : "华为 HUAWEI Mate 30 Pro 星河银 8GB+256GB麒麟990旗舰芯片OLED环幕屏双4000万徕卡电影四摄4G全网通手机",
              "spuId" : 11
            }
          },
      ······

添加成功了

















# 商城业务：首页



## thymeleaf渲染首页 
p136


在product模块引入依赖：

      <!-- thymeleaf 渲染首页的依赖 -->
      <!-- https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-thymeleaf -->
      <dependency>
          <groupId>org.springframework.boot</groupId>
          <artifactId>spring-boot-starter-thymeleaf</artifactId>
      </dependency>

将静态页面放入static，动态页面放入templates
这俩都是默认的资源存放处，无需配置便可生效

在application内关闭缓存：

      spring:
        thymeleaf:
          cache: false

此时启动服务，访问对应的路径便可以看到首页







## dev-tools渲染一级菜单 
p137


创建web模块，创建IndexController：

      @Controller
      public class IndexController {
      
          @Autowired
          CategoryService categoryService;



          @GetMapping({"/","/index"})
          public String getIndex(Model model){
              /*
              查出所有的一级分类
              对于分类，我们有categoryService进行查询，但是默认查的是三级分类
              我们再在里面创建一个方法查一级分类就好了
               */
              List<CategoryEntity> ones=categoryService.listOne();
              /*
              model是springmvc提供的一个接口，用于传递数据的
               */
              model.addAttribute("categories",ones);
              return "index";
          }

      }

自定义方法listOne：

      /**
       * @return
       *
       * 查出所有的一级分类
       * 返回一级分类实体的集合
       *
       */
      @Override
      public List<CategoryEntity> listOne() {
          //查出所有分类
          List<CategoryEntity> entities=baseMapper.selectList(null);
          //获取一级子类
          List<CategoryEntity> oneCategory=entities.stream().filter(categoryEntity -> categoryEntity.getCatLevel()==1).toList();
          return oneCategory;
      }



前端项目每次改完非常麻烦，因此这里使用devtools实现热更新

引入依赖：

      <!-- devtool的依赖 -->
      <!-- https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-devtools -->
      <dependency>
          <groupId>org.springframework.boot</groupId>
          <artifactId>spring-boot-devtools</artifactId>
          <optional>true</optional>
      </dependency>

一定要写<optional>true</optional>，这才算是真正导入了
这个依赖可以实现热更新，前端项目的更改可以不用重启微服务了
只需要ctrl f9重新编译html就可以了



在此之前，向前端传递了一个模型model，其中数据表示为传递的参数categoriesOne
故需要在前端工程index.html中指定数据的标识
和谷粒商城给的源码里的html一样，改个名就行了




## dev-tools渲染二三级菜单 
p138


原本的二三级菜单是写在json里的，前端直接到static里面找
但是要实现前端往服务器找二三级菜单才行，因为菜单是存在数据库中的，是变化的

照着json文件里的格式，发现，一级菜单的格式为：

      "1": [
        Object{...},
        Object{...},
        Object{...},
        Object{...},
        Object{...},
        Object{...},
        Object{...},
        Object{...},
        Object{...},
        Object{...},
        Object{...},
        Object{...}
    ],

算是一个String和Object数组的map，object也就是二级菜单的对象
二级菜单的格式为：

      {
        "catalog1Id":"1",
        "catalog3List":Array[4],
        "id":"1",
        "name":"电子书刊"
      }

其catalog3List的数组包含的就是三级菜单
但是三级菜单的格式则为：

      {
        "catalog2Id":"1",
        "id":"1",
        "name":"电子书"
      }

三级菜单下面就没有四级菜单了，故不需要再来一个四级菜单数组的成员变量

而且注意，三级菜单的名字需要是：catalog3List

定义vo：

      @Data
      @AllArgsConstructor
      @NoArgsConstructor
      public class Catalog2VO {
          private String catalog1Id;
          private List<Catalog3VO> catalog3List;
          private String id;
          private String name;
          @Data
          @NoArgsConstructor
          @AllArgsConstructor
          public static class Catalog3VO{
              private String catalog2Id;
              private String id;
              private String name;
          }
      }



定义好之后，要让前端向后端发送请求，获取所有三级菜单处理好的json数据
那就不能向静态文件夹发请求了，要自己定义一个接口，该接口的请求为为：

      /index/catalog.json

那么在catalogLoader.js中：

      $.getJSON("index/catalog.json",function (data) {    （2）

在IndexController中定义：

      /**
       *
       * @return
       *
       * 查询一级分类下所有的二三级菜单，返回一级分类的Map
       *
       */
      @ResponseBody
      @GetMapping("/index/catalog.json")
      public Map<String, List<Catalog2VO>> getCatalogJson(){
          Map<String, List<Catalog2VO>> map=categoryService.getCatalogJson();
          return map;
      }

自定义方法getCatalogJson：

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
      @Override
      public Map<String, List<Catalog2VO>> getCatalogJson() {
          //查出所有一级菜单：
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
          return finale;
      }


完成













# 商城业务：域名

p139


## switchhosts反向代理


先考虑本机访问域名的情况，修改本机域名和ip的映射规则
使用switchhosts

管理员启动

不过貌似域名不能加点，加了没法访问


不过：

      192.168.74.130 katzenyasax-mall

访问katzenyasax-mall可以访问到虚拟机




## 配置nginx

目的是要，把路由里的那个ip：localhost改成katzenyasax-mall

在mydata/nginx/conf/conf.d/里面添加mall.conf（复制旁边那个default.conf）
在里面改：

      server_name  katzenyasax-mall;

删掉location里的东西，改成：

      location / {
        proxy_pass http://10.60.50.85:8800;
      }

改成windows本机地址，端口表示商品服务，因为商城首页是通过8800的product服务开启的
保存后退出，重启nginx

此时直接在本机访问katzenyasax-mall，就可以访问到商城首页了




原理就是：

      1.在windows上使用switchhosts，配置katzenyasax-mall为ip：192.168.74.130
        这样一来，原本我们直接访问192.168.74.130可以访问到虚拟机，katzenyasax-mall则不行。
        但是现在配置之后，katzenyasax-mall和192.168.74.130拥有同等效力了

      2.在nginx上配置，监控80端口（即http协议）时，如果监控到访问到该虚拟机的域名为katzenyasax-mall，直接放行到10.60.50.85:8800
        而10.60.50.85:8800就是本机的商城首页
        故此时规则再次改变：katzenyasax-mall和192.168.74.130不再拥有同等效力
        192.168.74.130直接访问虚拟机
        但是katzenyasax-mall直接被nginx代理回本机8800端口




## 负载均衡到网关
        

但是我们的目的是，单纯地把localhost:10100改成katzenyasax-mall
这样一来如果我要单独访问库存模块，也就是访问：

      katzenyasax-mall:8001

理论上会等于：

      localhost:8001

但实际上，根本不行
这样配置下的katzenyasax-mall只能接收单独的katzenyasax-mall


怎么搞？
去到nginx总配置：mydata/nginx/conf/nginx.conf，在http块中添加：

      upstream mall{
        server 10.60.50.85:10100;
      }

mall表示上游服务器的名字，也就是windows本机开启的服务器
server后表示windows本机上，网关的ip和地址
注意每次连接不同网时主机的ip都会变，每次都要改nginx的配置
目前只部署了一台网关，因此只加一个
保存退出


随后到config.d/mall.conf内，修改location内容，不再直接代理到特定的ip和端口，而是代理给整个上游服务器，mall：

      location / {
        proxy_set_header Host $host;
        proxy_pass http://mall;
      }

另外proxy_set_header Host $host项是为了保留请求头，如果不设置会导致单独请求katzenyasax-mall时无效
保存退出后，重启nginx


随后配置gateway模块的application：
添加：

          - id: host-route
            uri: lb://mall-product
            predicates:
              - Host=**.katzenyasax-mall

使用Host断言，表示当域名为**.katzenyasax-mall时，路由到mall-product模块，而lb就表示使用动态服务名而非ip端口
mall-product是nacos中的服务名，这些都是基于nacos实现的


这样一来，访问：

      http://katzenyasax-mall/api/product/category/list/tree

和访问：

      http://localhost:10100/api/product/category/list/tree

的结果应该是一样的
事实也是如此









## 复盘

1.首先在windows上配置域名katzenyasax-mall，其映射的ip为192.168.74.130也即是虚拟机的ip
  此时二者享有同等效力，且katzenyasax-mall将永远享有代替ip访问虚拟机的效力

2.配置好nginx后，虚拟机的80也就是http协议的端口
  若发现域名katzenyasax-mall，有两种结果：

3.一种是单独的katzenysax-mall作为请求，则直接路由给主机的网关，
  主机网关收到这个特殊请求后，会经由特殊的方法，将其路由到mall-product模块的主页

4.另一种是katzenyasax/api/xxx，这种请求会直接路由/api即以后的请求给主机单独网关（localhost:10100）
  网关会根据路由来的/api/xxx来判定该请求路由到哪一个服务（mall-xxx）



最终达成了：
首先katzenyasax-mall可以代替虚拟机ip，直接访问虚拟机部署的服务，
其次katzenyasax-mall单独作为请求时，直接访问商城首页
最后katzenyasax-mall后面接上/api/xx后，可以直接替代网关的ip和端口




















# 性能
p141



## JMeter压测
p142

压测商城主页
50个线程，每个循环20次
总1000次请求，
路径：

      katzenyasax-mall

结果：

          样本      平均    中位   90     95     99    最小  最大   异常   吞吐量     接收       发送
TOTAL	    1000	    1113	  23	  3046	 7124	  15446	 13	  31643	 0.0	  21.67%  	562.33	   2.50

吞吐量仅21.67/sec
可以看到性能并不好，很差，接下来进行优化




## JVM模型
p144

其实整个jvm，堆区是最好优化的




## 垃圾回收机制
p144

首先堆区分为两个大区：新生代和老年代
新生代又分为：伊甸园区Eden和幸存者区Survivor


  1 新创对象在堆区开辟空间时，先来到新生代区的伊甸园区，并判断是否有足够空间：

      1.1 若足够，则直接存入伊甸园区

      1.2 若不够，则进行MinorGC（伊甸园区清理垃圾），清理伊甸园区内存
          清除未在使用的内存占用者（无用对象），将还在用的内存占用者（旧对象）移入幸存者区，或者引入老生代区：

          1.2.1 若幸存者区空间足够，将旧对象存入幸存者区，同时该旧对象年龄加一
                此时要进行年龄判断：

                1.2.1.1 若年龄大于15，则旧对象准备存入老生代区，进入 1.3.2 进行判断

          1.2.2 若不够，则直接存入老生代区

      1.3 随后判断伊甸园区是否有足够空间：

          1.3.1 若现在已经足够，则存入伊甸园区

          1.3.2 若还是不够，则准备存入老生代区前判断老生代区空间是否足够：

              1.3.2.1 若足够，直接存入老生代区

              1.3.2.2 若不够，进行FullGC（整个堆空间清理垃圾），再次判断老生代空间是否足够：

                  1.3.2.2.1 若足够，存入老生代区

                  1.3.2.2.2 若不够，则报异常，提示内存不足




## 监控堆区内存情况
p145

使用jvisualvm
下载jvisualvm，jdk8以后不再集成到jdk了，需要自行下载

要监控垃圾回收的情况，安装插件：Visual GC





## 中间件对性能的影响
p146

### 测试nginx性能

直接web服务器ip地址：

      192.168.74.130:80

协议为http
会直接访问到nginx
在jmeter中创建该ip的压测，准备进行

同时开启docker检测nginx：

      docker stats

开启压测，发现nginx主要吃cpu，基本上90%网上
反而是内存基本没怎么变过，一直是3.6MB左右
吞吐量4918.3/sec


### 测试网关性能

路径：

      localhost:10100

协议为http

开启压测发现吃cpu，开启后直接50%上下
内存还好，不能说特别爆，没吃满内存上限
MinorCG次数比较多，404次，耗时672.44ms
吞吐量1439/sec


### 测试首页
p147

如果不经过任何中间件，直接访问服务，压测其性能

路径：

      http://localhost:8800

50个线程每个10次

开启压测

发现cpu25%到35%直接，不是太吃cpu
内存比较稳定，没吃满上限
吞吐量218/sec

所以实际上回传三级分类业务本身是比较慢的，需要优化
不过可以知道主要的原因还是查数据库耗了太多时间（db）



### 测试商城首页全量数据

路径为

      localhost:8800

50个线程每个10次
协议http
高级里勾选获取http传输的所有资源

开启压测

cnm，非常爆
cpu直接干到50%上下，峰值60%
吞吐量仅仅14.9/sec

应该是加载静态资源的缘故




### 结论

1.中间件越多性能越差，特别是与网络直接相关的中间件，性能损失很大

2.除了中间件，还有数据库（DB）、模板渲染（themeleaf）、静态资源等
  不过还是DB的影响最大







## 初步优化

### 数据库

根据某个字段查询时，可以对基于字段建立索引，可以大幅提升查询的效率
但是相应的，由于建立索引后数据库存储结构变化了，增删改的效率会大幅降低
但是在用户层面上，大部分情况请求数据库的操作都是查，增删改的操作频率还是太少了
所以增加索引就看取舍了

例如我们的首页展示业务，也需要查出三级分类，即从category表中根据parent_cid查，所以可以基于parent_cid建立索引：

      create index pms_category_parent_cid_index on pms_category (parent_cid);

在IndexController对应方法上加：

      /**
      *
      * @return
      *
      * 查询一级分类下所有的二三级菜单，返回一级分类的Map
      *
      */
      @ResponseBody
      @GetMapping("/index/catalog.json")
      public Map<String, List<Catalog2VO>> getCatalogJson(){
          Long time=System.currentTimeMillis();
          Map<String, List<Catalog2VO>> map=categoryService.getCatalogJson();
          System.out.println(System.currentTimeMillis()-time);
          return map;
      }

每次访问，结果基本如下：

      建立索引前：644
      建立索引后：198

快了一大半吧




### themeleaf

其实开启缓存是可以提升性能的，不过提升可能不大
但是还是将application.yml的

      spring:
          thymeleaf:
            cache: true

开启缓存





### 日志

其实关闭数据库输出日志也是可以的，配置：

      logging:
        level:
          com.katzenyasax.mall: error

把debug缓存error，报错时才输出日志






### 初优化后压测


已经完成了初步的优化，再次进行商城首页压测，看看吞吐量会不会提升：
请求路径：

      localhost:8800

50个线程每个10次

吞吐量：334.4/sec

可以看到确实是比218/sec快了不少





再来看看全链路
路径：

      katzenyasax-mall

50个线程每个100次

结果：

          样本      平均    中位   90     95     99    最小  最大   异常   吞吐量     接收       发送
TOTAL	    5000	    398	    115	  248	   1042	  7206	 13	  31648	  0.0	  80/sec	  2074.08	   9.22

吞吐量：80/sec


快了不少








### nginx动静分离
p148

1.将product模块下static里的index文件夹复制到 /mydata/nginx/html/static

2.将index.html的所有 href=" 替换为 href="/static/ ，使用ctrl+r
  所有的 script src=" 替换为 script src="/static/ 
  所有的 img src=" 替换为 img src="/static/
  所有的 src=" 替换为 src="/static/

3.配置nginx：/mydata/nginx/conf， conf.d/mall.conf
  添加：

    location /static/ {
      root /usr/share/nginx/html;
    }

表示mall的静态资源要去到/usr/share/nginx/html/static里面找
也就是挂载到/mydata/nginx/html/static

4.保存，重启nginx，重新访问
  会发现katzenyasax-mall可以访问了
  而如果删除了product下面的static，那么localhost:8800就已经访问不到静态资源了





### 三级分类优化
p150

优化CategoryService和IndexService中的方法：，
CategoryService中：

      @Override
      public List<CategoryEntity> listAsTree() {
        /**
         * 只需要连接一次数据库
         * 性能好
         */
        //查出所有菜单
        List<CategoryEntity> listAll=baseMapper.selectList(null);
        //查出所有一级菜单：
        List<CategoryEntity> listI=listAll.stream().filter(c->c.getParentCid()==0).collect(Collectors.toList());
        return listI.stream().map(
                I->{
                    List<CategoryEntity> listII=listAll.stream().filter(c->c.getParentCid()==I.getCatId()).collect(Collectors.toList());
                    List<CategoryEntity> ii = listII.stream().map(
                            II->{
                                List<CategoryEntity> listIII=listAll.stream().filter(c->c.getParentCid()==II.getCatId()).collect(Collectors.toList());
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
        ).collect(Collectors.toList());
      }

IndexService中：

      @Override
      public Map<String, List<Catalog2VO>> getCatalogJson() {
        /**
         * 一次性查出所有的数据，在处理过程中不再连接数据库
         */
        //查出所有数据
        List<CategoryEntity> listAll=baseMapper.selectList(new QueryWrapper<>());
        //查出所有一级分类
        List<CategoryEntity> listI=listAll.stream().filter(c->c.getParentCid()==0).collect(Collectors.toList());
        Map<String, List<Catalog2VO>> finale = listI.stream().collect(Collectors.toMap(
                k -> k.getCatId().toString(),
                //遍历到单个一级菜单
                I -> {
                    //查出该一级菜单下所有二级菜单：
                    List<CategoryEntity> listII = listAll.stream().filter(c->c.getParentCid()==I.getCatId()).collect(Collectors.toList());
                    List<Catalog2VO> catalogII = listII.stream().map(
                            //遍历到单个二级菜单
                            II -> {
                                //查出该二级菜单下所有三级菜单：
                                List<CategoryEntity> listIII = listAll.stream().filter(c->c.getParentCid()==II.getCatId()).collect(Collectors.toList());
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
        return finale；
      }


再来看看加载三级分类的耗时：

      耗时：59
      耗时：25
      耗时：20
      耗时：22

只有第一次耗时比较多







# 缓存
P151


将更新频率少、访问次数多的数据放入redis缓存，实现一次加载以后直接拿来用的目的


## 本地缓存的问题

无法适配分布式架构，本地缓存的作用对象始终是单一的一台服务器，无法有效作用于其他同服务的服务器

并且容易出现，同服务但不同服务器做出了不同的修改，因此相互本地缓存不一致，导致在不同服务器上相同标识的数据不一致



# SpringBoot整合redis


## 安装
p151

product模块引入依赖：

      <!-- https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-data-redis -->
      <dependency>
          <groupId>org.springframework.boot</groupId>
          <artifactId>spring-boot-starter-data-redis</artifactId>
          <version>3.1.3</version>
      </dependency>

随后在application中配置：

      spring:
        data:
          redis:
            host: 192.168.74.130
            port: 6379



## 使用
p152

定义测试方法：

      /**
       * redis连接器
       */
      @Autowired
      StringRedisTemplate stringRedisTemplate;
      @Test
      public void RedisTest01(){
          //让连接器创建一个操作杠杆，用于直接操作redis
          ValueOperations<String,String> ops=stringRedisTemplate.opsForValue();
          //存入一个数据
          ops.set("Hello","Redis!"+ UUID.randomUUID());
          //查询，并输出
          System.out.println(ops.get("Hello"));
      }

运行：

      Redis!4a2fbeee-99a3-4bce-bfad-aa6fe4180cd0

可以查到
再到redisinsight里面查一查，确实存入了



## 改造三级分类
p153


第一次访问到三级分类后，将三级分类存入redis缓存，下一次再查询时便不需要再经过mysql查询，直接从redis中获取
因此对两个三级分类的方法进行改写，
为了方便，直接创建一个新的方法，原先方法进行改名备份：

商城首页：

      /**
       *
       * @return
       *
       * 使用redis改写的新·三级分类方法
       */
      @Override
      public Map<String, List<Catalog2VO>> getCatalogJson() {
          /**
           * 1.从redis中拿取数据
           * 2.判断数据是否为空
           * 3.1.若为空，则从数据库中调取，并存入redis
           * 3.2.若不为空，则无需查库，直接返回
           */
          //让连接器创建一个操作杠杆，用于直接操作redis
          ValueOperations<String,String> ops=stringRedisTemplate.opsForValue();
          //获取json字符串，redis中名为：CatalogJson
          String catalogJson=ops.get("CatalogJson");
          //判断catalogJson是否为空
          if(StringUtils.isEmpty(catalogJson)){
              //若为空，代表redis中还未有该json数据
              //则通过数据库获取数据，并存入json字符串数据
              Map<String, List<Catalog2VO>> finale=this.getCatalogJson_DB();
              ops.set("CatalogJson",JSON.toJSONString(finale));
              return finale;
          }
          else{
              //若不为空，则直接将redis中获取的json字符串反序列化为对象，返回对象
              Map<String, List<Catalog2VO>> finale=JSON.parseObject(catalogJson, new TypeReference<Map<String, List<Catalog2VO>>>() {});
              return finale;
          }
      }

树形：

       /**
       *
       * @return
       *
       * 经过redis缓存判断的三级菜单
       *
       */
      @Override
      public List<CategoryEntity> listAsTree(){
          //让连接器创建一个操作杠杆，用于直接操作redis
          ValueOperations<String,String> ops=stringRedisTemplate.opsForValue();
          //获取json字符串，redis中名为：CatalogListAsTree
          String catalogListAsTree=ops.get("CatalogListAsTree");
          //判断catalogListAsTree是否为空
          if(StringUtils.isEmpty(catalogListAsTree)){
              //若为空，代表redis中还未有该json数据
              //则通过数据库获取数据，并存入json字符串数据
              List<CategoryEntity> finale=this.listAsTree_DB();
              ops.set("CatalogListAsTree",JSON.toJSONString(finale));
              return finale;
          }
          else{
              //若不为空，则直接将redis中获取的json字符串反序列化为对象，返回对象
              List<CategoryEntity> finale=JSON.parseObject(catalogListAsTree,new TypeReference<List<CategoryEntity>>(){});
              return finale;
          }
      }

记得import的TypeReference是这一个：

      import com.alibaba.fastjson.TypeReference;




测试：
首页三级分类，redis中无该缓存，耗时：

      耗时：28
      耗时：3
      耗时：4
      耗时：4
      耗时：5
      耗时：5

树形三级分类，redis中无该缓存。耗时：

      list/tree：token：43
      list/tree：token：13
      list/tree：token：9
      list/tree：token：9
      list/tree：token：7
      list/tree：token：3

可以看到，这个效率基本上在300%以上，非常给力





## 缓存的一系列问题
p155



### 缓存穿透

若一个方法内，本该从redis读取一个数据data，但该数据在redis中不存在kv对，那么我们读取的data就为空，经过判断此时应该访问数据库获取data，
但问题是，如果访问数据库时，恰好数据库中也不存在data对应的数据，那么我们获取的data永远内容为空，并且还会将data存入redis，
所以实际上缓存根本没有作用，还会拖累占用服务器部分性能，连同全盘扫描数据库，效率可以说非常低下

理论上不应该出现这种情况，
但在某些不可预测的情况下，该情况是可能出现的，并且有可能形成循环，对redis和数据库都是不小的压力，从而大幅降低服务器的性能
例如有人利用该漏洞故意请求查询不存在的数据，那么很可能造成服务器崩溃



解决？

将这个不存在数据按照kv存入redis，只不过v为null
同时设定过期时间，三五分钟的都行，在这个时间段内访问它的结果都是null
设置null时，存入一个内容为空的实例对象就行了



### 缓存雪崩

指redis中存在一个kv键值对，对所有可以读取它的服务器的过期时间相同，过期时可能造成大量服务器无法同时读取，从而将同时读取数据库造成数据库崩溃的现象



解决？

在固定的过期时间上加一个Random值，尽量不造成其对大量服务器同时失效的情况









### 缓存击穿

一个被经常高并发请求访问的数据，在失效的时候，原本的高并发请求不再发往redis而是发往数据库，造成数据库失效的现象



解决？

加锁，先放一个请求进锁进来查库，后面的请求都等待
等先进来的请求查到库后把结果存入redis，后面的请求直接从redis查，不用经过数据库了











## 解决缓存穿透和缓存雪崩
p155

      Map<String, List<Catalog2VO>> finale=this.getCatalogJson_DB();
      if(finale==null){
        //若从数据库中获取来的也为空
        finale=new HashMap<>();
        //直接将finale赋为空内容对象
      }
      ops.set("CatalogJson",JSON.toJSONString(finale),300+(new Random().nextInt(150)), TimeUnit.SECONDS);
      //设置过期时间，标准过期时间300s，在此基础上加上0-149秒的随机时间

在第一次从redis中读取数据为空时，访问数据库后再次添加判断，若finale为空（空指针），则将其赋值为空内容对象
除此之外设置标准300s、随机为0到149s的随机随机





## 解决缓存击穿
p156

### 本地锁

加在查数据库的两个DB内
以this为锁，表示单个进程上锁，解锁后其余进程进来后还要从redis查是不是有数据，如果有就不用再查数据库了
而redis中的数据自然也是第一个单例进程查库得到数据并存入数据库的结果
以首页分类的哪个为例；

      public Map<String, List<Catalog2VO>> getCatalogJson_DB() {
        /**
         * 上锁
         * 先从redis读取，没有才进行查库
         * 查到库后，不管是哪一个进程都要存进redis
         * 后续进来的就不用再查库了
         */
        synchronized (this) {
            //让连接器创建一个操作杠杆，用于直接操作redis
            ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
            //获取json字符串，redis中名为：CatalogJson
            String catalogJson = ops.get("CatalogJson");
            if (StringUtils.isEmpty(catalogJson)) {
                /**
                 * ============================================ 查库 ===========================================================
                 * 一次性查出所有的数据，在处理过程中不再连接数据库
                 *
                 * redis中查不到数据时，才进行查库
                 *
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
                  /**
                   * ============================================================================================================
                   */
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
                  //若不为空，则直接将redis中获取的json字符串反序列化为对象，返回对象
                  Map<String, List<Catalog2VO>> finale=JSON.parseObject(catalogJson, new TypeReference<Map<String, List<Catalog2VO>>> (){});
                  return finale;
              }
          }
      }


在查库的方法内，对整个查库方法进行再次封装加锁，锁头为当前的单例进程
若当前进程未结束，锁住整个synchronized代码块
若当前进程查不到redis数据，则进行查库，无论结果是否为空内容，都将其存入redis，存储完毕后释放锁
若当前进程查到了redis数据，则无需再进行查库，直接返回redis获取的数据并释放锁


但是值得注意的是，它的性能比分布式锁的性能快
在特定场景，可以替代分布式锁



但是在分布式场景下，其缺点：只能锁住当前服务的进程，锁不住其他服务
其结果为，在redis中无缓存时恰好多台同服务的服务器同时开始执行同一个指令，那么几乎在最开始每个服务的进程都会进锁并查库
总的看来，服务部署到多少台服务器上，就会最多查多少次库







### 分布式锁
p158


一个进程抢占到锁后，在redis中set一个kv:lock,1，表示此时该线程抢占到锁；
其他进程查到redis中有lock时，便不再进去，但是一直等待锁的释放，也就是redis中查不到lock

可能的问题：

  1.进程进锁后，因不可抗原因，而在删除锁前便强制停止，造成锁未释放且无法再释放的问题
    解决：在进锁时，便要设置过期时间

  2.当前进程业务超时，导致业务还在执行时锁就被删除了，此时会有新的进程得到锁，若当前进程业务完成后，删除的锁实际上是别人的锁
    解决：将锁的值设置为uuid，这样一来删除锁时就要判断是否未自己的锁，如果是则删除，如果不是则啥也不干

  3.从开始判断锁是否为自己并已经拿取锁的值、到值传输到判断语句这段时间内，若锁恰好过期，则其他进程进锁后，锁的值便不是自己的值了
    但问题是我们拿到锁的值的时候锁并未过期，值仍未自己的uuid，此后删除锁时其实删的是别人的锁
    所以结论是删除锁（包括执行判断、拿取值、删除锁）必须是一整个原子操作
    解决：使用lua脚本：

        String script="if redis.call('get',KEYS[1])==ARGV[1] then return redis.call('del',KEYS[1]) else return 0 end";
        stringRedisTemplate.execute(new DefaultRedisScript<Integer>(script,Ling.class),Arrays.asList("lock"),uuid);

    也就是将删除锁的操作全部交给redis封装的api执行，对于java来说这是一个原子操作，
    redis封装了该方法会将所有的获取、判断、传输的操作封装，
    jvm就不需要再手动判断、获取等，排除了数据传输的时间差等带来的问题

  4.锁的自动续期
    最理想的情况就是业务执行过程中锁一直有效



但是压测看来，似乎有点不太稳定







# Redisson

前面的锁综合性能都不好
redisson的性能会好一些，此处使用原生依赖可以更好理解redisson的原理和运行方式


## 安装与配置
p160

第一次时，引入：

      <!-- https://mvnrepository.com/artifact/org.redisson/redisson -->
      <dependency>
          <groupId>org.redisson</groupId>
          <artifactId>redisson</artifactId>
          <version>3.23.5</version>
      </dependency>

创建配置类config/RedissonConfiguration：

      @Configuration
      public class RedissonConfiguration {
      
          /**
           * 
           * @return
           * 
           * redisson配置类
           * 
           */
          @Bean(destroyMethod = "shutdown")//销毁方法为shutdown
          RedissonClient redissonClient(){
              //创建配置
              Config config=new Config();
              config.useSingleServer().setAddress("redis://192.168.74.130:6379");
              //根据配置实例化redisson的客户端
              RedissonClient redissonClient= Redisson.create(config);
              return redissonClient;
          }
      }

测试一下：

      @Autowired
      RedissonConfiguration redissonConfiguration;
      @Test
      void printRedisson(){
          System.out.println(redissonConfiguration);
      }

结果：

      com.katzenyasax.mall.product.config.RedissonConfiguration$$SpringCGLIB$$0@6a4ba6f4

打印出了实例化配置类的地址，说明可以加载

注意，redis的地址必须以redis://或rediss://开头，像这种单独以ip开头的是不行的，例如：

      setAddress("redis://192.168.74.130:6379")：可以
      setAddress("192.168.74.130:6379")：不可以





## 可重入锁Lock
p160
p161

解决了两个问题：


      1.死锁问题，也是看门狗机制实现，即是没有在业务中手动解锁，当没有进程占用该锁时，当锁的ttl结束时该锁自动释放

      2.锁的自动续期问题，保证进程从运行开始到结束始终占用同一把锁
        即看门狗机制，若有进程占用该锁仍在运行，但ttl快结束了，那么锁的时长会自动蓄满；
        默认ttl为30秒，每隔10秒或有进程占用该锁时，自动蓄满至30秒，
        也可以获取锁时自定义，但是不会自动续期，且必须要大于业务的执行时间，
        否则该进程业务结束释放锁时，会被系统判断当前已经是其他进程的锁而报错

虽然当时，还是推荐使用自定义ttl的方式，因为自动续期浪费的资源太高了，
而满足一个进程占用同一把锁也只需要把ttl设置大一点就行了，
比如就设置成30秒，有整整30秒的时间给进程发挥，
执行完了直接手动解锁不谈，要是30秒进程业务都还执行不完，说明进程已经死了，这已经不是业务本身的问题了，而是数据库连接不上这种外部问题
但是不管怎样，30秒不管你进程是死是活，redis都会自动强制解锁

实现：

      RLock lock = redissonClient.getLock("LOCK");
      lock.lock(30,TimeUnit.SECONDS);

其中getLock的里面参数随便写，只要是同一个名字就代表是同一把锁


除此之外还可以让锁在一段时间内尝试上锁，该时间段内上不了锁就不上了，
业务lock方法默认是阻塞式上锁，进程永远会自旋上锁，上不了锁就没完的那种
实现：

      RLock lock = redissonClient.getLock("LOCK");
      boolean ifLocked = lock.tryLock(30,100,TimeUtil.SECONDS);

自旋100秒，上不了锁就不上了
而且有个boolean返回值，表示是否上锁，可以根据这个来判断要不要执行业务




## 公平锁FairLock
p162

所有进程会排队一个一个拿锁，一个进程解锁后，下一个进程拿锁，不会像非公平锁一样一个进程解锁后所有的锁竞争拿锁

实现：

      RLock fairLock = redissonClient.getFairLock("FAIRLOCK");

其他使用和其他一样



## 读写锁ReadWirteLock
p162

可以实现进程修改数据和读取数据的严谨性，保证读取到的永远是最新数据，
例如一个进程正在修改数据，另一个进程想读取该数据，那就必须等待上一个进程修改数据完成后才能读取到。

      1.读可以所有进程并发读，
      2.但是写必须成为一个单独进程，不能和读并发，也不能和其他写并发，
      3.读的过程中有进程写时，写必须等待读完成

实现(写);

      RLock wrLock = redissonClient.getWriteReadLock("WRLOCK");
      try{
        wrLock.writeLock().lock();
        *写数据的业务*
      } catch(Exception e){
        
      } finally{
        wrLock.wirteLock().unlock();
      }

实现(读);

      RLock wrLock = redissonClient.getWriteReadLock("WRLOCK");
      wrLock.readLock().lock();
      *读数据的业务*
      wrLock.readLock().unlock();



## 信号量Semaphore
p165

一个限制进程并发数的量，
一个进程执行时，信号量减一(release())，一直减到0时不再可减
进程结束时，信号量加一(aquire())，一直加到设定量时不再可加

除此之外tryAcquire和tryRelease表示如果可以的话就加和减，如果不可以那就算了，就瞅一眼
一样有一个boolean的返回值

实现：

      RSemaphore semaphore = redissonClient.getSemaphore("SEMAPHORE");




## 闭锁CountDownLatch
p164

一个闭锁关联若干个进程，关联应当是一个进程结束后闭锁计数减一，当所有进程都完成，即闭锁计数为0时，闭锁才关闭
该关联是逻辑关联，没有任何物理关联手段，编写和维护全为手动

实现：

      RCountDownLatch latch = redissonClient.getCountDownLatch("LATCH");
      latch.trySetCount(10);
      latch.await();

设置10个关联进程
关联进程实现：

      RCountDownLatch latch = redissonClient.getCountDownLatch("LATCH");
      *业务*
      latch.countDown();

完成时，latch的计数减一









## 缓存一致性
p166

改造三级分类，使用redisson分布式锁
首先三级分类的锁是用来放入仅仅一个进程查数据库的，而redisson可以保证只有一个进程进锁
因此锁用于放入进程，一个普通的可重入锁便可以
其次读写顺序要规范，若redis中没有数据时则写入，此时使用写锁；而这样一来其他读取的情况则要加上读锁。

但是问题来了，每次只存一次，过期时间后重新存，万一这个期间数据修改了呢？
这就牵扯到缓存一致性问题了
两种解决方法
1.双写模式，修改数据的时候修改缓存
2.失效模式，修改数据后让缓存失效，并让下一个查询缓存的进程进锁查询

但是缺点是：
1.双写模式会造成脏读，造成暂时脏数据的存在，但是由于数据以数据库为准，因此等缓存失效后查库存入的缓存一定是最新的数据，满足最终一致性
2.失效模式会造成读和写的无序混乱，有可能读和写不会按照顺序来

解决方案：
1.加过期时间，可以满足大部分场景
2.再加个读写锁，保证读写分离

当然也有完美方案，即使用中间件canal订阅binlog，不需要我们手动更新缓存了，他会自动监听数据库更新状况，并映射到缓存


但是针对特别热点的数据来说，一般不存在缓存中，直接采用查库的方式

最终方案，首页三级分类：

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
              if (finale == null) {           //若从数据库中获取来的也为空
                  finale = new HashMap<>();       //直接将finale赋为空内容对象
              }

              RReadWriteLock readWriteLock = redissonClient.getReadWriteLock("CatalogJson_ReadWriteLock");  //读写锁
              readWriteLock.writeLock().lock(30, TimeUnit.SECONDS);    //写锁上锁
              ops.set("CatalogJson", JSON.toJSONString(finale), 300 + (new Random().nextInt(150)), TimeUnit.SECONDS); //数据存入  redis
              readWriteLock.writeLock().unlock();         //写锁解锁
          } finally {
              lock.unlock();      //解锁进程
          }
          System.out.println("获取到数据库中数据，是否为空？"+!finale.isEmpty());
          return finale;
      }
      /**
       *
       * 使用redis改写的新·首页三级分类方法
       */
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

              if(finale==null){   //若从数据库中获取来的也为空
                  finale=new HashMap<>(); //直接将finale赋为空内容对象
              }

              readWriteLock.writeLock().lock(30,TimeUnit.SECONDS);        //写锁上锁
              ops.set("CatalogJson",JSON.toJSONString(finale),300+(new Random().nextInt(150)), TimeUnit.SECONDS);     //设置过期时  间，标准过期时间300s，在此基础上加上0-149秒的随机时间
              readWriteLock.writeLock().unlock();     //写锁解锁

              return finale;
          }
          else{
              System.out.println("redis中有数据，直接返回");
              Map<String, List<Catalog2VO>> finale=JSON.parseObject(catalogJson, new TypeReference<Map<String, List<Catalog2VO>>>() {});               //若不为空，则直接将redis中获取的json字符串反序列化为对象，返回对象
              return finale;
          }
      }


普通三级分类：

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
              if (finale == null) {       //若从数据库中获取来的也为空
                  finale = new ArrayList<>();     //直接将finale赋为空内容对象
              }

              RReadWriteLock readWriteLock = redissonClient.getReadWriteLock("CatalogListAsTree_ReadWriteLock");  //读写锁
              readWriteLock.writeLock().lock(30, TimeUnit.SECONDS);    //写锁上锁
              ops.set("CatalogListAsTree", JSON.toJSONString(finale), 300 + (new Random().nextInt(150)), TimeUnit.SECONDS);     //数  据存入redis
              readWriteLock.writeLock().unlock();  //写锁解锁
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

              if(finale==null){   //若从数据库中获取来的也为空
                  finale=new ArrayList<>();   //直接将finale赋为空内容对象
              }

              readWriteLock.readLock().lock(30,TimeUnit.SECONDS); //写锁上锁
              ops.set("CatalogListAsTree",JSON.toJSONString(finale),300+(new Random().nextInt(150)), TimeUnit.SECONDS);   //设置过期  时间，标准过期时间300s，在此基础上加上0-149秒的随机时间
              readWriteLock.readLock().unlock();  //写锁过期

              return finale;
          }
          else{
              System.out.println("redis中有数据，直接返回");
              List<CategoryEntity> finale=JSON.parseObject(catalogListAsTree,new TypeReference<List<CategoryEntity>>()  {});                //若不为空，则直接将redis中获取的json字符串反序列化为对象，返回对象
              return finale;
          }
      }















# SpringCache
p167

之前说了，数据库一更新，就要将缓存也更新，而且是手动更新，太麻烦了
因此使用SpringCache，实现对写入缓存的简化


## 基本整合
p168

1.引入依赖：

      <!-- https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-cache -->
      <dependency>
          <groupId>org.springframework.boot</groupId>
          <artifactId>spring-boot-starter-cache</artifactId>
      </dependency>

不用写版本号
除此之外，cache是基于redis的，因此需要redis的依赖，已经引入了

2.application.yml中添加：

      spring:
          cache:
            type: redis

表示使用redis作为缓存


3.在启动类加上注解：

      @EnableCaching

注意导入的是org.springframework.cache这个包下的注解：

      import org.springframework.cache.annotation.Cacheable;

否则没用




## 测试

以mall首页一级分类为例，因为它和三级分类是独立的方法
我们想将其存入redis，也即是将其方法返回值存入redis，那么直接在方法上加一个注解：

      @Cacheable({"product-category"})
      @Override
      public List<CategoryEntity> listOne() {
          System.out.println("缓存：获取到了一级分类...");
          List<CategoryEntity> listAll=baseMapper.selectList(null);
          List<CategoryEntity> finale=listAll.stream().filter(c->c.getCatLevel()==1).collect(Collectors.toList());
          return finale;
      }

表示将其分类存入product-category分区，分区可以有多个
并且，如果该方法确实存入了数据到redis，那么下次就不用调用方法内的业务了，直接从redis中获取完事

运行后，查看redis，发现有kv数据：

      product-category::SimpleKey []

但是格式是java serialized格式







## 细节
p169，好几把糊
p170

1.自定义数据的名字

在@EnableCache注解中加上key，可以接收String，也可以接收SpEL表达式
接收String时，使用单引号：

      key="'listOne'"

接收SpEL表达式，例如命名为方法名：

      key="#root.method.name"

更多的表达式写法参照官方文档

配合上上面写的分组，最后数据的名字就是：

      product-category::listOne

2.自定义ttl

在application.yml中设置：

      spring:
        cache:
          redis:
            time-to-live: 3600000
    
单位为毫秒，3600000ms即1h

3.自定义数据格式为json

需要自定义Redis的缓存管理器RedisCacheConfiguration，因此需要读源码

原理是，RedisCacheConfiguration配置会被RedisCacheManager管理，若我们没有自定义配置，则RedisCacheManager使用默认的配置；
若自定义了配置，并将其设置为自动装配，那么RedisCacheManager就会监听到，并取代默认配置。
同时，RedisCacheConfiguration被CacheAutoConfiguration注入，成为SpringCache对redis缓存的总配置。

      @Configuration
      @EnableCaching
      public class SpringCacheConfiguration {
          @Bean
          RedisCacheConfiguration redisCacheConfiguration(){
              RedisCacheConfiguration conf=RedisCacheConfiguration.defaultCacheConfig();
              //先弄一个默认配置，在其基础上修改
              conf = conf
                      .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                      .serializeValuesWith(RedisSerializationContext
                            .SerializationPair
                            .fromSerializer(new GenericFastJsonRedisSerializer())
                      )
              ;
              //把格式固定成为了fastJson包下的Json格式，因为Json是标准格式，用哪个包下面的Json肯定都是标准的。
              return conf;
          }
      }

注意注解@EnableCaching表示是缓存的配置

测试一下
缓存名为：

      product-category::listOne

缓存内容：

      [
        {
            "@type": "com.katzenyasax.mall.product.entity.CategoryEntity",
            "catId": 1,
            "catLevel": 1,
            "children": [],
            "name": "图书
            ······

是json格式，和预期一致

但是ttl不是60min了，和默认的ttl一样为-1
这是因为配置类优先于配置文件，有配置类就不再读配置文件了
所以除此之外还要将其他的配置加上去：

      .entryTtl(Duration.ofMinutes(60))

除此之外，还有：

      .prefixCacheNameWith("CACHE_")         //缓存名前缀，在最终名字面前加上前缀
      .disableKeyPrefix()               //禁用前缀，包括前缀和用@Cacheable注解指定的分组名，当然::也没有了                      
      .disableCachingNullValues()       //禁用空值缓存，如果禁用有可能造成缓存穿透




把三级分类的方法改造一下，不要再手动调用redis缓存，直接让SpringCache注解来缓存，
树形三级分类：

      public List<CategoryEntity> listAsTree_RedissonLock() {
        System.out.println("redisson锁内...");
        List<CategoryEntity> finale;  //返回值实例
        ValueOperations<String, String> ops = redisTemplate.opsForValue();  //让连接器创建一个操作杠杆ops，用于直接操作redis
        RLock lock=redissonClient.getLock("CatalogListAsTree_Lock");    //获取可重入锁，锁整个进程
        lock.lock(30,TimeUnit.SECONDS);     //上锁，自定义30秒
        try {
            finale = this.getListTreeDB();  //封装的方法，从库中获取三级分类
        } finally {
            lock.unlock();  //解锁进程
        }
        System.out.println("获取到数据库中数据，是否为空？"+!finale.isEmpty());
        return finale;
     }

     @Cacheable(value = "product-category",key="'CatalogListAsTree'")
     @Override
     public List<CategoryEntity> listAsTree(){

         ValueOperations<String,String> ops= redisTemplate.opsForValue();    //让连接器创建一个操作杠杆，用于直接操作redis
         RReadWriteLock readWriteLock = redissonClient.getReadWriteLock("CatalogListAsTree_ReadWriteLock");  //读写锁

         readWriteLock.readLock().lock(30,TimeUnit.SECONDS);     //读锁
         String catalogListAsTree=ops.get("CatalogListAsTree");  //从redis获取catalogListAsTree数据
         readWriteLock.readLock().unlock();//读锁解锁

         if(StringUtils.isEmpty(catalogListAsTree)){         //判断catalogJson是否为空。
             System.out.println("redis中无数据，将进锁查库...");

             List<CategoryEntity> finale=this.listAsTree_RedissonLock();     //使用redisson分布式锁

             return finale;
         }
         else{
             System.out.println("redis中有数据，直接返回");
             List<CategoryEntity> finale=JSON.parseObject(catalogListAsTree,new TypeReference<List<CategoryEntity>>(){});                //若不为空，则直接将redis中获取的json字符串反序列化为对象，返回对象
             return finale;
         }
     }

首页三级分类：

      public Map<String, List<Catalog2VO>> getCatalogs_RedissonLock() {
        System.out.println("redisson锁内...");
        Map<String, List<Catalog2VO>> finale;       //返回值实例化对象
        ValueOperations<String, String> ops = redisTemplate.opsForValue();      //让连接器创建一个操作杠杆ops，用于直接操作redis
        RLock lock=redissonClient.getLock("CatalogJson_Lock");    //获取可重入锁，锁整个进程
        lock.lock(30,TimeUnit.SECONDS);     //上锁，自定义30秒
        try {
            finale = this.getCatalogsDB();      //封装的方法，从库中获取三级分类
        } finally {
            lock.unlock();      //解锁进程
        }
        System.out.println("获取到数据库中数据，是否为空？"+!finale.isEmpty());
        return finale;
      }

      @Cacheable(value = "product-category",key="'CatalogJson'")
      @Override
      public Map<String, List<Catalog2VO>> getCatalogJson() {
      
          RReadWriteLock readWriteLock=redissonClient.getReadWriteLock("CatalogJson_ReadWriteLock");      //读写锁
          ValueOperations<String,String> ops= redisTemplate.opsForValue();        //让连接器创建一个操作杠杆，用于直接操作redis

          readWriteLock.readLock().lock(30,TimeUnit.SECONDS);     //读锁上锁
          String catalogJson=ops.get("CatalogJson");  //获取json字符串，redis中名为：CatalogJson
          readWriteLock.readLock().unlock();          //读锁解锁

          if(StringUtils.isEmpty(catalogJson)){               //判断catalogJson是否为空。
              System.out.println("redis中无数据，将进锁查库...");

              Map<String, List<Catalog2VO>> finale=this.getCatalogs_RedissonLock();   //使用redisson分布式锁

              return finale;
          }
          else{
              System.out.println("redis中有数据，直接返回");
              Map<String, List<Catalog2VO>> finale=JSON.parseObject(catalogJson, new TypeReference<Map<String, List<Catalog2VO>>>() {});               //若不为空，则直接将redis中获取的json字符串反序列化为对象，返回对象
              return finale;
          }
      }

删除了所有手动操作redis存缓存的操作



在双写模式下，还可以使用@CachePut，表示将该结果存到缓存，如果缓存中已有则覆盖








## 删除缓存
p171

使用@CacheEvict注解，实现失效模式
比如更新数据库的时候，会删除缓存中的旧数据，下次缓存查库时再存入新的缓存
比如让一级分类的缓存失效：

      @CacheEvict(value = {"product-category"},key="'listOne'") //删除一级分类

加在CategoryController上最简单
让三级分类失效：

      @CacheEvict(value = {"product-category"},key="'CatalogListAsTree'")
      @CacheEvict(value = {"product-category"},key="'CatalogJson'")



现在问题是，更新商品分类数据后，一级和三级的都要失效，但是@CacheEvict注解不能同时标两遍
解决方法：

      1.@Caching注解聚合缓存操作

      2.@Cacheable(value = {"product-category"}, allEntries=true)，删除分区内所有缓存






## 不足
p172


读多写少的直接闭眼用SpringCache，非热点数据或其他只需要保证最终一致性的数据，也直接用SpringCache

但是一些特殊数据，SpringCache并不能完全解决问题，需要自己设计业务逻辑，例如读写顺序啥的

或者引入canal，直接从数据库层面更改缓存，不需要再在业务层进行缓存写入，完美解决一切问题






# 商城首页：全局检索


## 搭建环境
p173

检索服务用search模块
1.search模块引入thymeleaf和devtools依赖
  包括关闭thymeleaf缓存，开启devtools热更新等

2.将搜索页的index.html复制到search服务templates目录
  使用ctrl+r，
  将index.html的所有 href=" 替换为 href="/static/search/ 
  所有的 src=" 替换为 src="/static/search/

3.将静态资源全部复制到 /mydata/nginx/html/static/search

4.搜索路径：

      mall-search/

  在/mydata/nginx/conf/conf.d中创建search.conf文件，和mall.conf一致，只不过监听的路径变了：

      listen       80;
      server_name  mall-search

5.配置SwicthHosts方案：

      192.168.74.130 mall-search

6.配置网关：

      - id: search-route
        uri: lb://mall-search
        predicates:
          - Host=**.mall-search


此时搜索http://mall-search就可以跳转到渲染好的搜索页面了，
前提是index.html在的情况下，如果没有index.html那么访问的就是nginx的主页






值得一提的是，资料给的文件，左侧分类栏点进去永远是search.gmall.com，不是自定义的mall-search
所以直接在mall.conf中统一配置网关：

      server_name  katzenyasax-mall mall-search search.gmall.com;

表示如果监听到这三个请求，一律交给网关处理
之后该配置会更多



## 页面跳转
p174

跳转首页，index.html的20行、402行更改url为http://katzenyasax-mall


通过左侧三级分类搜索时，路径为list.html，将search的index.html改为list.html
随后定义controller：

      @Controller
      public class SearchController {

          @GetMapping("list.html")
          public String listHtml(){
              return "list";
          }
      }

方法不重要，重要的是list.html这个请求会直接导向资源库
另外需要注意的是@Controller不能替换为@RestController，否则会强制按照方法而不会经过资源库




通过顶部栏关键字查询时
product模块下index.html第118行删除超链接，并将search()方法装入<a>，即：

      <a href="javascript:search();"><img src="/static/index/img/img_09.png" /></a>

这是用search的方法得到的结果替换了写死的超链接
第611改路径为：

      window.location.href="http://mall-search/list.html?keyword="+keyword;

search方法，跳转到一个连接





## 搜索页查询参数
p175

请求路径为：

      http://search.gmall.com/list.html?

或

      http://mall-search/list.html?

?后面跟的是检索条件，将其封装为一个vo，SearchParam：

      @Data
      public class SearchParam {
      
          private String keyword;
          private Long brandId;
          private Long catalogId;
          private String sort;
          private Integer hasStock;
          private String skuPrice;
          private List<String> attrs;
          private Integer pageNum = 1;
          private String _queryString;
      }


将结果也封装成一个vo，即SearchResult：

      @Data
      public class SearchResult {
          private List<SkuEsModel> product;
          private Integer pageNum;
          private Long total;
          private Integer totalPages;
          private List<Integer> pageNavs;
          private List<BrandVo> brands;
          private List<AttrVo> attrs;
          private List<CatalogVo> catalogs;
          private List<NavVo> navs;
          @Data
          public static class NavVo {
              private String navName;
              private String navValue;
              private String link;
          }
          @Data
          public static class BrandVo {
              private Long brandId;
              private String brandName;
              private String brandImg;
          }
          @Data
          public static class AttrVo {
              private Long attrId;
              private String attrName;
              private List<String> attrValue;
          }
          @Data
          public static class CatalogVo {
              private Long catalogId;
              private String catalogName;
          }
      }




## 测试一下dsl语句
p177

例如精确查询一个spu：

      GET product/_search
      {
        "query": {
          "bool": {
            "must": [
              {
                "match": {
                  "skuTitle": "HUAWEI"
                }
              },
              {
                "nested": {
                  "path": "attrs",
                  "query": {
                    "bool": {
                      "must": [
                        {
                          "match": {
                            "attrs.attrValue": "HUAWEI Kirin 980"
                          }
                        }
                      ]
                    }
                  }
                }
              },
              {
                "term": {
                  "brandId": 9
                }
              }
            ],
            "filter": [
              {
                "term": {
                  "catalogId": 225
                }
              },
              {
                "term":{
                  "skuId": 1
                }
              },{
                "range": {
                  "skuPrice": {
                    "gte": 6000,
                    "lte": 7000
                  }
                }
              }
            ]
          }
        },
        "from": 0, 
        "size": 1000,
        "highlight": {
          "fields": { "skuTitle":{} },
          "pre_tags": "<b style='color:red'>",
          "post_tags": "</b>"
        }
      }

其中must和filter都是bool的下一级，
must中匹配商品名skuId、属性的值attrs.attrValue、品牌brandId，和品牌热度相关联，因此加热度分
filter中则匹配商品分类catalogId、一件商品的不同版本的参数skuId、价格skuPrice这些常规参数，和品牌热度无很大关联，因此不加分
除此之外还有must not，只有匹配不是条件的才能通过，而且不会加分，适合于排除某些字段
should，不用匹配也不用匹配非，只需要字段被查询过就加分，适合不参与查询但是与品牌热度有关的字段

highlight表示高亮








## 复杂聚合分析
p178

先将product的数据迁移到mall-product中，把所有不参与聚合的映射改为参与聚合
先创建一个新的索引：

      PUT mall-product
      {
        "mappings": {
          "properties": {
            "skuId": {
              "type": "long"
            },
            "spuId": {
              "type": "keyword"
            },
            "skuTitle": {
              "type": "text",
              "analyzer": "ik_smart"
            },
            "skuPrice": {
              "type": "keyword"
            },
            "skuImg": {
              "type": "keyword"
            },
            "saleCount": {
              "type": "long"
            },
            "hasStock": {
              "type": "boolean"
            },
            "hotScore": {
              "type": "long"
            },
            "brandId": {
              "type": "long"
            },
            "catalogId": {
              "type": "long"
            },
            "brandName": {
              "type": "keyword"
            },
            "brandImg": {
              "type": "keyword"
            },
            "catalogName": {
              "type": "keyword"
            },
            "attrs": {
              "type": "nested",
              "properties": {
                "attrId": {
                  "type": "long"
                },
                "attrName": {
                  "type": "keyword"
                },
                "attrValue": {
                  "type": "keyword"
                }
              }
            }
          }
        }
      }

迁移数据：

      POST _reindex
      {
        "source": {
          "index": "product"
        },
        "dest": {
          "index": "mall-product"
        }
      }

迁移后所有字段都可参与查询和聚合

其实如果不嫌麻烦的话可以把product删了，再把mall-product迁移到product
就不弄了吧，不过要把common里的ESConstant的PRODUCT_INDEX改成"mall-product"：

      public class ESConstant {}
        public static final String PRODUCT_INDEX="mall-product";
      }


先简单查一下，分别聚合catalogId和brandId，然后分别再字聚合出catalogName和brandName：

      GET mall-product/_search
      {
        "query": {
          "match_all": {}
        },
        "aggs": {
          "catalogsAgg":{
            "terms": {
              "field": "catalogId",
              "size": 20
            },
            "aggs": {
              "catalogNamesAgg": {
                "terms": {
                  "field": "catalogName",
                  "size": 20
                }
              }
            }
          },
          "brandsAgg": {
            "terms": {
              "field": "brandId",
              "size": 20
            },
            "aggs": {
              "brandNamesAgg": {
                "terms": {
                  "field": "brandName",
                  "size": 20
                }
              }
            }
          }
        }
      }

响应的聚合结果：

      "aggregations" : {
        "catalogsAgg" : {
          "doc_count_error_upper_bound" : 0,
          "sum_other_doc_count" : 0,
          "buckets" : [
            {
              "key" : 225,
              "doc_count" : 35,
              "catalogNamesAgg" : {
                "doc_count_error_upper_bound" : 0,
                "sum_other_doc_count" : 0,
                "buckets" : [
                  {
                    "key" : "手机",
                    "doc_count" : 35
                  }
                ]
              }
            },
            {
              "key" : 1434,
              ······
        },
        "brandsAgg" : {
          "doc_count_error_upper_bound" : 0,
          "sum_other_doc_count" : 0,
          "buckets" : [
            {
              "key" : 12,
              "doc_count" : 24,
              "brandNamesAgg" : {
                "doc_count_error_upper_bound" : 0,
                "sum_other_doc_count" : 0,
                "buckets" : [
                  {
                    "key" : "Apple",
                    "doc_count" : 24
                  }
                ]
              }
            },
            {
              "key" : 9,
              ······


再加一个嵌入式nested聚合，注意path需要完整路径：

      "attrsAgg": {
        "nested": {
          "path": "attrs"
        },
        "aggs": {
          "attrIdAgg": {
            "terms": {
              "field": "attrs.attrId",
              "size": 100
            },
            "aggs": {
              "attrNameAgg": {
                "terms": {
                  "field": "attrs.attrName",
                  "size": 100
                }
              },
              "attrValueAgg": {
                "terms": {
                  "field": "attrs.attrValue",
                  "size": 100
                }
              }
            }
          }
        }
      }

结果除了上述的brandsAgg和catalogsAgg，还有：

      "attrsAgg" : {
        "doc_count" : 68,
        "attrIdAgg" : {
          "doc_count_error_upper_bound" : 0,
          "sum_other_doc_count" : 0,
          "buckets" : [
            {
              "key" : 15,
              "doc_count" : 35,
              "attrNameAgg" : {
                "doc_count_error_upper_bound" : 0,
                "sum_other_doc_count" : 0,
                "buckets" : [
                  {
                    "key" : "CPU品牌",
                    "doc_count" : 35
                  }
                ]
              },
              "attrValueAgg" : {
                "doc_count_error_upper_bound" : 0,
                "sum_other_doc_count" : 0,
                "buckets" : [
                  {
                    "key" : "以官网信息为准",
                    "doc_count" : 34
                  },
                  {
                    "key" : "海思（Hisilicon）",
                    "doc_count" : 1
                  }
                ]
              }
            },
            {
              "key" : 16,
              ······
            }
          ]
        }
      }












## 构建bool查询
p179


      //开始聚合
      //先来一个bool查询，创建的对象bool就是整个bool查询语句
      BoolQueryBuilder bool = QueryBuilders.boolQuery();
      //匹配关键字，如果关键字不为空的话
      if(StringUtils.isNotEmpty(params.getKeyword())){
          //相当于将一个match拼接到bool语句的后面了
          bool.must(QueryBuilders.matchQuery("skuTitle",params.getKeyword()));
      }
      //匹配分类
      if(params.getCatalog3Id()!=null) {
          bool.filter(QueryBuilders.termQuery("catalogId", params.getCatalog3Id()));
      }
      //匹配品牌，品牌可多选
      if(params.getBrandId()!=null) {
          bool.filter(QueryBuilders.termQuery("brandId", params.getBrandId()));
      }
      //匹配有无库存
      if(params.getHasStock()!=null) {
          bool.filter(QueryBuilders.termQuery("hasStock", params.getHasStock()));
      }
      //匹配价格，格式为a_b，即再a和b之间，a和b可为空
      if(StringUtils.isNotEmpty(params.getSkuPrice())){
          String[] prices=params.getSkuPrice().split("_");
          //这个是两个价格，为空则表示不需要匹配
          RangeQueryBuilder range=QueryBuilders.rangeQuery("skuPrice");
          //价格不为空，则进行匹配
          if(StringUtils.isNotEmpty(prices[0])){
              range.lte(Integer.parseInt(prices[0]));
          }
          if(StringUtils.isNotEmpty(prices[1])){
              range.gte(Integer.parseInt(prices[1]));
          }
          //匹配价格
          bool.filter(range);
      }
      //匹配属性
      if(params.getAttrs()!=null&&params.getAttrs().size()>0){
          //attr的格式为attrId_attrName:attrSplit，表示
          for(String attr:params.getAttrs()) {
              
              Long attrId=Long.parseLong(attr.split("_")[0]);
              String[] attrValue=attr.split("_")[1].split(":");

              bool.filter(QueryBuilders.nestedQuery(
                        "attrs",
                        QueryBuilders.boolQuery().
                                must(QueryBuilders.
                                        termQuery("attr.attrId",attrId)).
                                must(QueryBuilders.
                                        termQuery("attr.attrValue",attrValue)),
                        ScoreMode.None)
                );
          }
      }
      //完成了bool查询
      searchSourceBuilder.query(bool);

其实也是严格按照json来的，等级分明











## 构建页面设置
p180

      //2.1、排序
      //排序条件：sort=price/salecount/hotscore_desc/asc
      if(StringUtils.isNotEmpty(params.getSort())){
          //分割数据
          String word= params.getSort().split("_")[0];
          String sort= params.getSort().split("_")[1];
          if(sort.equals("desc")){
              searchSourceBuilder.sort(word, SortOrder.DESC);
          }
          else if(sort.equals("asc")){
              searchSourceBuilder.sort(word, SortOrder.ASC);
          }
      }
      //2.2、分页
      //在ESConstant中定义一个PRODUCT_PAGESIZE
      //起始个数为：(页数-1)*每页的长度
      searchSourceBuilder.from(ESConstant.PRODUCT_PAGESIZE*(params.getPageNum()-1) );
      searchSourceBuilder.size(ESConstant.PRODUCT_PAGESIZE);
      //2.3、高亮
      //只高亮关键字
      if(StringUtils.isNotEmpty(params.getKeyword())){
          searchSourceBuilder.highlighter(
                    new HighlightBuilder().
                            field("skuTitle").
                            preTags("<b style='color:red'>").
                            postTags("</b>")
            );
      }

都是在最外层的searchSourceBuilder直接进行




此时可以测试一下，输出searchSourceBuilder：

      System.out.println(searchSourceBuilder.toString());
  
假设param为空，searchSourceBuilder应为：

      {"from":0,"size":5,"query":{"bool":{"adjust_pure_negative":true,"boost":1.0}}}

在kibana测试一下：

      GET mall-product/_search
      {
        "from": 0,
        "size": 5,
        "query": {
          "bool": {
            "adjust_pure_negative": true,
            "boost": 1
          }
        }
      }

结果就是输出了5条数据
这也代表是成功的

假如在apifox中，给他加一条catalogId=225，searchSourceBuilder为：

      {
        "from": 0,
        "size": 5,
        "query": {
          "bool": {
            "filter": [
              {
                "term": {
                  "catalogId": {
                    "value": 225,
                    "boost": 1
                  }
                }
              }
            ],
            "adjust_pure_negative": true,
            "boost": 1
          }
        }
      }

在kibana的测试，结果也是对的，查到了所有catalogId为225的数据




## 构建聚合分析
p181

      //聚合品牌
      TermsAggregationBuilder brandAgg= AggregationBuilders.
              terms("brandAgg").
              field("brandId").size(100).
              subAggregation(AggregationBuilders.
                      terms("brandNameAgg").
                      field("brandName.keyword").
                      size(1)
              ).
              subAggregation(
                      AggregationBuilders.
                      terms("brandImgAgg").
                      field("brandImg.keyword").
                      size(1)
              );
      searchSourceBuilder.aggregation(brandAgg);
      //聚合分类：
      TermsAggregationBuilder catalogAgg=AggregationBuilders.
              terms("catalogAgg").
              field("catalogId").
              size(100).
              subAggregation(AggregationBuilders.
                      terms("catalogNameAgg").
                      field("catalogName.keyword").
                      size(1)
              );
      searchSourceBuilder.aggregation(catalogAgg);
      //聚合属性
      NestedAggregationBuilder attrAgg=AggregationBuilders.
              nested("attrAgg","attrs").
              subAggregation(AggregationBuilders.
                      terms("attrIdAgg").
                      field("attrs.attrId").
                      size(100)).
                      subAggregation(AggregationBuilders.
                              terms("attrNameAgg").
                              field("attrs.attrName").
                              size(1)
                      ).
                      subAggregation(AggregationBuilders.
                              terms("attrValueAgg").
                              field("attr.attrValue").
                              size(1)
                      );
      searchSourceBuilder.aggregation(attrAgg);


再次启动测试，这次直接访问不加param，看看会出现什么：

      {
        "from": 0,
        "size": 5,
        "query": {
          "bool": {
            "adjust_pure_negative": true,
            "boost": 1
          }
        },
        "aggregations": {
          "brandAgg": {
            "terms": {
              "field": "brandId",
              "size": 100,
              "min_doc_count": 1,
              "shard_min_doc_count": 0,
              "show_term_doc_count_error": false,
              "order": [
                {
                  "_count": "desc"
                },
                {
                  "_key": "asc"
                }
              ]
            },
            "aggregations": {
              "brandNameAgg": {
                "terms": {
                  "field": "brandName",
                  "size": 1,
                  "min_doc_count": 1,
                  "shard_min_doc_count": 0,
                  "show_term_doc_count_error": false,
                  "order": [
                    {
                      "_count": "desc"
                    },
                    {
                      "_key": "asc"
                    }
                  ]
                }
              },
              "brandImgAgg": {
                "terms": {
                  "field": "brandImg",
                  "size": 1,
                  "min_doc_count": 1,
                  "shard_min_doc_count": 0,
                  "show_term_doc_count_error": false,
                  "order": [
                    {
                      "_count": "desc"
                    },
                    {
                      "_key": "asc"
                    }
                  ]
                }
              }
            }
          },
          "catalogAgg": {
            "terms": {
              "field": "catalogId",
              "size": 100,
              "min_doc_count": 1,
              "shard_min_doc_count": 0,
              "show_term_doc_count_error": false,
              "order": [
                {
                  "_count": "desc"
                },
                {
                  "_key": "asc"
                }
              ]
            },
            "aggregations": {
              "catalogNameAgg": {
                "terms": {
                  "field": "catalogName",
                  "size": 1,
                  "min_doc_count": 1,
                  "shard_min_doc_count": 0,
                  "show_term_doc_count_error": false,
                  "order": [
                    {
                      "_count": "desc"
                    },
                    {
                      "_key": "asc"
                    }
                  ]
                }
              }
            }
          },
          "attrAgg": {
            "nested": {
              "path": "attrs"
            },
            "aggregations": {
              "attrIdAgg": {
                "terms": {
                  "field": "attrs.attrId",
                  "size": 100,
                  "min_doc_count": 1,
                  "shard_min_doc_count": 0,
                  "show_term_doc_count_error": false,
                  "order": [
                    {
                      "_count": "desc"
                    },
                    {
                      "_key": "asc"
                    }
                  ]
                },
                "aggregations": {
                  "attrNameAgg": {
                    "terms": {
                      "field": "attrs.attrName",
                      "size": 100,
                      "min_doc_count": 1,
                      "shard_min_doc_count": 0,
                      "show_term_doc_count_error": false,
                      "order": [
                        {
                          "_count": "desc"
                        },
                        {
                          "_key": "asc"
                        }
                      ]
                    }
                  },
                  "attrValueAgg": {
                    "terms": {
                      "field": "attrs.attrValue",
                      "size": 100,
                      "min_doc_count": 1,
                      "shard_min_doc_count": 0,
                      "show_term_doc_count_error": false,
                      "order": [
                        {
                          "_count": "desc"
                        },
                        {
                          "_key": "asc"
                        }
                      ]
                    }
                  }
                }
              }
            }
          }
        }
      }

爆了，可以查出来，而且是对的


那么完成sdl的构建后，直接返回：

      return new SearchRequest(new String[] {ESConstant.PRODUCT_INDEX},searchSourceBuilder);

就完事了







完成了sdl的构建后，应该可以直接获取到一个searchResponse，测试一下，将其输出：

      System.out.println("响应："+searchResponse.toString());

运行结果：

      响应：{"took":20,"timed_out":false,"_shards":{"total":1,"successful":1,"skipped":0,"failed":0},"hits":{"total":{"value":36,"relation":"eq"},"max_score":1.0,"hits":[{"_index":"mall-product","_type":"_doc","_id":"1","_score":1.0,"_source":{"attrs":[{"attrId":15,"attrName":"CPU品牌","attrValue":"以官网信息为准"},{"attrId":16,"attrName":"CPU型号","attrValue":"HUAWEI Kirin 980"}],"brandId":9,"brandImg":"https://kaztenyasax-mall.oss-cn-beijing.aliyuncs.com/huawei.png","brandName":"华为","catalogId":225,"catalogName":"手机","hasStock":false,"hotScore":0,"saleCount":0,"skuId":1,"skuImg":"https://gulimall-hello.oss-cn-beijing.aliyuncs.com/2019-11-26/60e65a44-f943-4ed5-87c8-8cf90f403018_d511faab82abb34b.jpg","skuPrice":6299.0,"skuTitle":"华为 HUAWEI Mate 30 Pro 星河银 8GB+256GB麒麟990旗舰芯片OLED环幕屏双4000万徕卡电影四摄4G全网通手机","spuId":11}},{"_index":"mall-product","_type":"_doc","_id":"2","_score":1.0,"_source":{"attrs":[{"attrId":15,"attrName":"CPU品牌","attrValue":"以官网信息为准"},{"attrId":16,"attrName":"CPU型号","attrValue":"HUAWEI Kirin 980"}],"brandId":9,"brandImg":"https://kaztenyasax-mall.oss-cn-beijing.aliyuncs.com/huawei.png","brandName":"华为","catalogId":225,"catalogName":"手机","hasStock":true,"hotScore":0,"saleCount":0,"skuId":2,"skuImg":"https://gulimall-hello.oss-cn-beijing.aliyuncs.com/2019-11-26/ef2691e5-de1a-4ca3-827d-a60f39fbda93_0d40c24b264aa511.jpg","skuPrice":5799.0,"skuTitle":"华为 HUAWEI Mate 30 Pro 星河银 8GB+128GB麒麟990旗舰芯片OLED环幕屏双4000万徕卡电影四摄4G全网通手机","spuId":11}},{"_index":"mall-product","_type":"_doc","_id":"3","_score":1.0,"_source":{"attrs":[{"attrId":15,"attrName":"CPU品牌","attrValue":"以官网信息为准"},{"attrId":16,"attrName":"CPU型号","attrValue":"HUAWEI Kirin 980"}],"brandId":9,"brandImg":"https://kaztenyasax-mall.oss-cn-beijing.aliyuncs.com/huawei.png","brandName":"华为","catalogId":225,"catalogName":"手机","hasStock":false,"hotScore":0,"saleCount":0,"skuId":3,"skuImg":"https://gulimall-hello.oss-cn-beijing.aliyuncs.com/2019-11-26/ef2691e5-de1a-4ca3-827d-a60f39fbda93_0d40c24b264aa511.jpg","skuPrice":6299.0,"skuTitle":"华为 HUAWEI Mate 30 Pro 亮黑色 8GB+256GB麒麟990旗舰芯片OLED环幕屏双4000万徕卡电影四摄4G全网通手机","spuId":11}},{"_index":"mall-product","_type":"_doc","_id":"4","_score":1.0,"_source":{"attrs":[{"attrId":15,"attrName":"CPU品牌","attrValue":"以官网信息为准"},{"attrId":16,"attrName":"CPU型号","attrValue":"HUAWEI Kirin 980"}],"brandId":9,"brandImg":"https://kaztenyasax-mall.oss-cn-beijing.aliyuncs.com/huawei.png","brandName":"华为","catalogId":225,"catalogName":"手机","hasStock":false,"hotScore":0,"saleCount":0,"skuId":4,"skuImg":"https://gulimall-hello.oss-cn-beijing.aliyuncs.com/2019-11-26/ef2691e5-de1a-4ca3-827d-a60f39fbda93_0d40c24b264aa511.jpg","skuPrice":5799.0,"skuTitle":"华为 HUAWEI Mate 30 Pro 亮黑色 8GB+128GB麒麟990旗舰芯片OLED环幕屏双4000万徕卡电影四摄4G全网通手机","spuId":11}},{"_index":"mall-product","_type":"_doc","_id":"5","_score":1.0,"_source":{"attrs":[{"attrId":15,"attrName":"CPU品牌","attrValue":"以官网信息为准"},{"attrId":16,"attrName":"CPU型号","attrValue":"HUAWEI Kirin 980"}],"brandId":9,"brandImg":"https://kaztenyasax-mall.oss-cn-beijing.aliyuncs.com/huawei.png","brandName":"华为","catalogId":225,"catalogName":"手机","hasStock":true,"hotScore":0,"saleCount":0,"skuId":5,"skuImg":"https://gulimall-hello.oss-cn-beijing.aliyuncs.com/2019-11-26/ef2691e5-de1a-4ca3-827d-a60f39fbda93_0d40c24b264aa511.jpg","skuPrice":6299.0,"skuTitle":"华为 HUAWEI Mate 30 Pro 翡冷翠 8GB+256GB麒麟990旗舰芯片OLED环幕屏双4000万徕卡电影四摄4G全网通手机","spuId":11}}]},"aggregations":{"nested#attrAgg":{"doc_count":68,"lterms#attrIdAgg":{"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"key":15,"doc_count":35,"sterms#attrNameAgg":{"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"key":"CPU品牌","doc_count":35}]},"sterms#attrValueAgg":{"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"key":"以官网信息为准","doc_count":34},{"key":"海思（Hisilicon）","doc_count":1}]}},{"key":16,"doc_count":33,"sterms#attrNameAgg":{"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"key":"CPU型号","doc_count":33}]},"sterms#attrValueAgg":{"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"key":"A13仿生","doc_count":18},{"key":"HUAWEI Kirin 980","doc_count":15}]}}]}},"lterms#brandAgg":{"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"key":12,"doc_count":24,"sterms#brandImgAgg":{"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"key":"https://kaztenyasax-mall.oss-cn-beijing.aliyuncs.com/apple.png","doc_count":24}]},"sterms#brandNameAgg":{"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"key":"Apple","doc_count":24}]}},{"key":9,"doc_count":8,"sterms#brandImgAgg":{"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"key":"https://kaztenyasax-mall.oss-cn-beijing.aliyuncs.com/huawei.png","doc_count":8}]},"sterms#brandNameAgg":{"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"key":"华为","doc_count":8}]}},{"key":13,"doc_count":3,"sterms#brandImgAgg":{"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"key":"https://kaztenyasax-mall.oss-cn-beijing.aliyuncs.com/bbb3-iqyrykv5144586.jpg","doc_count":3}]},"sterms#brandNameAgg":{"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"key":"一加","doc_count":3}]}},{"key":14,"doc_count":1,"sterms#brandImgAgg":{"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[]},"sterms#brandNameAgg":{"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"key":"万代","doc_count":1}]}}]},"lterms#catalogAgg":{"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"key":225,"doc_count":35,"sterms#catalogNameAgg":{"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"key":"手机","doc_count":35}]}},{"key":1434,"doc_count":1,"sterms#catalogNameAgg":{"doc_count_error_upper_bound":0,"sum_other_doc_count":0,"buckets":[{"key":"Rider Driver","doc_count":1}]}}]}}}

反正序列化后是对的。
之后需要将这些结果解析封装到一个SearchResult对象








## 分析结果
p182

主要是两部分：hits、aggregations
其中hits中，hits才是所有的SkuEsModel对象，其余都是附加信息
先获取hits：

      //1.分析hits
      SearchHits hits = searchResponse.getHits();
      //1.1、总记录数
      result.setTotal(hits.getTotalHits().value);
      //1.2、总页码，计算
      result.setTotalPages(
              (int) ((hits.getTotalHits().value%ESConstant.PRODUCT_PAGESIZE)==0
              ?hits.getTotalHits().value/ESConstant.PRODUCT_PAGESIZE
              :hits.getTotalHits().value/ESConstant.PRODUCT_PAGESIZE+1)
      );
      //1.3、当前页码
      result.setPageNum(param.getPageNum());
      //1.4、所有商品sku
      //要查的是_source，将每个hit转成Json格式字符串，再用FastJSON转成skuEsModel对象，再将其存入result的product
      if(searchResponse.getHits().getHits()!=null&&searchResponse.getHits().getHits().length>0){
          for(SearchHit hit:searchResponse.getHits().getHits()){
              result.getProduct().add(JSON.parseObject(
                      hit.getSourceAsString(),
                      SkuEsModel.class
                      )
              );
          }
      }

分析了hits，接下来分析aggregations：

      //返回值的几个集合
      List<SearchResult.BrandVo> brands=new ArrayList<>();
      /**
       * 当前查询到的结果，所有涉及到的所有属性
       */
      List<SearchResult.AttrVo> attrs=new ArrayList<>();
      /**
       * 当前查询到的结果，所有涉及到的所有分类
       */
      List<SearchResult.CatalogVo> catalogs=new ArrayList<>();
      //3.1、获取brand聚合
      ParsedLongTerms brandAgg = searchResponse.getAggregations().get("brandAgg");
      for (Terms.Bucket bucket : brandAgg.getBuckets()) {
          //当前bucket的vo对象
          SearchResult.BrandVo brandVo=new SearchResult.BrandVo();
          //brandId
          brandVo.setBrandId(Long.parseLong(bucket.getKeyAsString()));
          //获取name子聚合
          ParsedStringTerms brandNameAgg = bucket.getAggregations().get("brandNameAgg");
          //获取brandName
          if((brandNameAgg.getBuckets())!=null&&brandNameAgg.getBuckets().size()>0) {
              String brandName = brandNameAgg
                      .getBuckets()
                      .get(0)
                      .getKeyAsString();
              brandVo.setBrandName(brandName);
          }else {
              brandVo.setBrandName(null);
          }
          //获取Img子聚合
          ParsedStringTerms brandImgAgg = bucket.getAggregations().get("brandImgAgg");
          System.out.println(brandImgAgg);
          //获取brandImg
          if((brandImgAgg.getBuckets())!=null&&brandImgAgg.getBuckets().size()>0) {
              String brandImg = brandImgAgg
                      .getBuckets()
                      .get(0)
                      .getKeyAsString();
              brandVo.setBrandImg(brandImg);
          }else{
              brandVo.setBrandImg(null);
          }
          brands.add(brandVo);
      }
      //3.2、获取catalog聚合
      ParsedLongTerms catalogAgg = searchResponse.getAggregations().get("catalogAgg");
      for (Terms.Bucket bucket : catalogAgg.getBuckets()) {
          //获取vo对象
          SearchResult.CatalogVo catalogVo=new SearchResult.CatalogVo();
          //catalogId
          catalogVo.setCatalogId(Long.parseLong(bucket.getKeyAsString()));
          //获取Name子聚合
          ParsedStringTerms catalogNameAgg=bucket.getAggregations().get("catalogNameAgg");
          if((catalogNameAgg.getBuckets())!=null&&catalogNameAgg.getBuckets().size()>0) {
              //获取catalogName
              String catalogName = catalogNameAgg
                      .getBuckets()
                      .get(0)
                      .getKeyAsString();
              catalogVo.setCatalogName(catalogName);
          }else {
              catalogVo.setCatalogName(null);
          }
          catalogs.add(catalogVo);
      }
      //3.3、获取attr聚合
      ParsedNested attrAgg = searchResponse.getAggregations().get("attrAgg");
      ParsedLongTerms attrIdAgg = attrAgg.getAggregations().get("attrIdAgg");
      for (Terms.Bucket bucket : attrIdAgg.getBuckets()) {
          //当前bucket的vo对象
          SearchResult.AttrVo attrVo=new SearchResult.AttrVo();
          //获取id
          attrVo.setAttrId(Long.parseLong(bucket.getKeyAsString()));
          //获取attrName子聚合
          ParsedStringTerms attrNameAgg=bucket.getAggregations().get("attrNameAgg");
          if((attrNameAgg.getBuckets())!=null&&attrNameAgg.getBuckets().size()>0) {
              //获取attrName
              String attrName = attrNameAgg
                      .getBuckets()
                      .get(0)
                      .getKeyAsString();
              attrVo.setAttrName(attrName);
          }else{
              attrVo.setAttrName(null);
          }
          //获取attrValue子聚合，结果是一个list，需要遍历
          ParsedStringTerms attrValueAgg=bucket.getAggregations().get("attrValueAgg");
          if(!(attrValueAgg.getBuckets()==null)&&attrValueAgg.getBuckets().size()>0){
              List<String> attrValue=new ArrayList<>();
              for (Terms.Bucket thisBucket : attrValueAgg.getBuckets()) {
                     attrValue.add(thisBucket.getKeyAsString());
              }
              attrVo.setAttrValue(attrValue);
          }else{
              attrVo.setAttrValue(null);
          }
          attrs.add(attrVo);
      }
      result.setBrands(brands);
      result.setCatalogs(catalogs);
      result.setAttrs(attrs);

测试一下结果，令catalogId=25，理论上es中没有catalogId为25的数据，那么查询时应该无数据，结果：

      {
          "attrs":[
          
          ],
          "brands":[
          
          ],
          "catalogs":[
          
          ],
          "pageNum":1,
          "product":[
          
          ],
          "total":0,
          "totalPages":0
      }

还真是








## 渲染结果到页面
p184

抄前端，注意改几个参数就行




## 关于匹配字段为数组、集合时查不出的问题

例如

      "term": {
        "attrs.attrValue": {
          "value": [
            "A13仿生"
          ],
          "boost": 1
        }
      
这种，匹配的是一个[]时无查出数据

总之先把全部这些改成单个字段吧，瑕不掩瑜，不影响业务












      








# 异步


## 线程池
p193

所有线程交给线程池执行，不要再new了，内存会爆
核心参数：

      ThreadPoolExecutor tpe=new ThreadPoolExecutor(
        3,                                          //核心线程数
        6,                                          //最大线程数
        60,                                         //空闲线程最大存活时间
        TimeUnit.SECONDS,                           //时间单位（秒）
        new ArrayBlockingQueue<>(3),                //建立阻塞队列，允许同时运行线程的数量
        Executors.defaultThreadFactory(),           //建立线程工厂
        new ThreadPoolExecutor.AbortPolicy()        //拒绝服务策略，队列满时处理剩余线程的策略
      );



## CompletableFuture异步编排
p195

异步编排，将有关系的异步线程进行串联
在java原生中有一个类CompletableFuture用于异步编排

### 编排的定义：

      CompletableFuture<Object> future01=CompletableFuture.runAsync(()->{
          <方法体>
      },executorService);
      System.out.println(future01.get().toString());

future01.xxx()就是future01下一步干什么，.get()就是获取返回值


### 返回值回调与面向异常处理
p197

可以将异步编排的返回值回调给异步编排
有一个方法whenComplete：

      .whenComplete((rst,ecp)-{
         rst是结果，
         ecp是异常
      })

例如：

      CompletableFuture<Long> future01=CompletableFuture.supplyAsync(()->{
            Long id;
            System.out.println("当前进程"+(id=Thread.currentThread().getId()));
            return (id=10l/0l);
        },executorService)
                .whenComplete((result,exception)->{
                    System.out.println("结果："+result+" ;异常："+exception);
                });

输出：

      当前进程16
      结果：null ;异常：java.util.concurrent.CompletionException: java.lang.ArithmeticException: / by zero



而可以针对异常作出行动，其中有一个方法exceptionally:

      CompletableFuture<Long> future01=CompletableFuture.supplyAsync(()->{
           Long id;
           System.out.println("当前进程"+(id=Thread.currentThread().getId()));
           return (id=10l/0l);
       },executorService)
               .whenComplete((result,exception)->{
                   System.out.println("结果："+result+" ;异常："+exception);
               })
               .exceptionally(throwable -> {
                   System.out.println("发生异常");
                   return -1l;
               });

此时输出：

      当前进程16
      结果：null ;异常：java.util.concurrent.CompletionException: java.lang.ArithmeticException: / by zero
      发生异常
      -1




### handle最终处理
p198

方法handle可以在第一步执行完后就进行结果处理：

      CompletableFuture<Object> future01=CompletableFuture.supplyAsync(()->{
          Long id;
          System.out.println("当前进程"+(id=Thread.currentThread().getId()));
          return (id=10l/0l);
      },executorService)
             /* .whenComplete((result,exception)->{
                  System.out.println("结果："+result+" ;异常："+exception);
              })
              .exceptionally(throwable -> {
                  System.out.println("发生异常");
                  return -1l;
              })*/
              .handle((res,exc)->{
                  if(res!=null){
                      return "结果是："+res;
                  }
                  if(exc!=null){
                      return "发生异常："+exc;
                  }
                  return "未发生异常，但是结果为空";
              });

结果：

      当前进程16
      发生异常：java.util.concurrent.CompletionException: java.lang.ArithmeticException: / by zero




### 异步线程串行化方法
p199

即异步线程按照逻辑顺序依次执行



注意，以下方法有不带Async的版本
带Async表示在线程池（原生的或自定义的都行）新开一个线程执行下一个异步线程；不带Async则表示就在该线程执行下一个异步线程




1.使用thenRunAsync方法

      CompletableFuture<Void> future = CompletableFuture.supplyAsync(()->{
          Long id;
          System.out.println("当前进程"+(id=Thread.currentThread().getId()));
          return (id=10l/2l);
      },executorService)
              .thenRunAsync(()->{
                  System.out.println("thenAsync，启动！");
              },executorService)
      ;

结果：

      当前进程16
      thenAsync，启动！

但是这个方法无法感知到上一个异步线程的返回值，且其本身无返回值



2.使用thenAcceptAsync方法：

      CompletableFuture<Void> future = CompletableFuture.supplyAsync(()->{
          Long id;
          System.out.println("当前进程"+(id=Thread.currentThread().getId()));
          return (id=10l/2l);
      },executorService)
         .thenAcceptAsync((res)->{
                 System.out.println("thenAcceptAsync，启动！"+"，上一个线程的返回值："+res);
             },executorService)
      ;

结果：

      当前进程16
      thenAcceptAsync，启动！，上一个线程的返回值：5

但是这个方法其本身无返回值



3.使用thenApplyAsync方法

      CompletableFuture<Object> future = CompletableFuture.supplyAsync(()->{
          Long id;
          System.out.println("当前进程"+(id=Thread.currentThread().getId()));
          return (id=10l/2l);
      },executorService)  
          .thenApplyAsync(res->{
              System.out.println("thenApplyAsync，启动！"+"，上一个线程的返回值："+res);
              return (Object)999l;
          },executorService)
          .whenComplete((res,exc)->{
              System.out.println("thenApplyAsync的返回值："+res);
          })
      }
      System.out.println("get: "+future.get().toString());


结果：

      当前进程16
      thenApplyAsync，启动！，上一个线程的返回值：5
      thenApplyAsync的返回值：999
      get：999


可以感知到结果，且方法本身有返回值




可以明显看到，前面两种方法无返回值时，也即整个函数式编程块无返回值，所以等号前面的东西必须用泛型<Void>表示为空，而且也不能用get方法；
而后面的有返回值，因此前面可以加东西，类型不做限定，可以使用get方法





### 异步线程组合：都要完成
p200

即要求二者同时执行

注意以下所有方法均是需要被其中一个线程调用，不是静态方法

1.thenCombineAsync方法：

      CompletableFuture<Object> future01=CompletableFuture.supplyAsync(()->{
          Long id;
          System.out.println("第一个线程："+(id=Thread.currentThread().getId()));
          return (id);
      },executorService)
                .thenCombineAsync(
                    CompletableFuture.supplyAsync(()->{
                        Long id;
                        System.out.println("第二个线程："+(id=Thread.currentThread().getId()));
                        return (id);
                    })
                    , ((res1,res2)->{
                        System.out.println("第一个异步线程的结果："+res1);
                        System.out.println("第二个异步线程的结果："+res2);
                        return res1+res2;
                    }
                )
            );    
      System.out.println("get："+future01.get().toString());

结果：

      第一个线程：16
      第二个线程：17
      第一个异步线程的结果：16
      第二个异步线程的结果：17
      get：33

该方法允许二者执行完后处理二者的返回值，且其本身也有返回值




2.thenAcceptBothAsync方法：

      CompletableFuture<Void> future01=CompletableFuture.supplyAsync(()->{
          Long id;
          System.out.println("第一个线程："+(id=Thread.currentThread().getId()));
          return (id);
      },executorService)
                .thenCombine(
                    CompletableFuture.supplyAsync(()->{
                        Long id;
                        System.out.println("第二个线程："+(id=Thread.currentThread().getId()));
                        return (id);
                    })
                    , ((res1,res2)->{
                        System.out.println("第一个异步线程的结果："+res1);
                        System.out.println("第二个异步线程的结果："+res2);
                    }
                )
            );    

结果：

      第一个线程：16
      第二个线程：17
      第一个异步线程的结果：16
      第二个异步线程的结果：17

该方法可以处理二者返回值，但本身无返回值
因此整个函数式编程块的结果类型应当是<Void>，且无法调用get方法




3.thenAfterBoth方法：

      CompletableFuture<Void> future01=CompletableFuture.supplyAsync(()->{
          Long id;
          System.out.println("第一个线程："+(id=Thread.currentThread().getId()));
          return (id);
      },executorService)
            .runAfterBothAsync(
                        CompletableFuture.supplyAsync(()->{
                            Long id;
                            System.out.println("第二个线程："+(id=Thread.currentThread().getId()));
                            return (Object) id;
                        })
                        ,()->{
                            Long id;
                            System.out.println("第三个线程："+(id=Thread.currentThread().getId()));
                        }
                        ,executorService
                );

结果：

      第一个线程：16
      第二个线程：17
      第三个线程：18

该方法返回值为空，即<Void>
并且两个线程完成后下一个线程，无结果感知、无返回值








### 异步线程组合：只需要一个完成
p201

1.applyToEither方法：

      CompletableFuture<Object> future01=CompletableFuture.supplyAsync(()->{
          Long id;
          System.out.println("第一个线程："+(id=Thread.currentThread().getId()));
          return (Object) id;
      },executorService)
            .applyToEither(
                        CompletableFuture.supplyAsync(()->{
                            Long id=0l;
                            while((5l-++id)>=0l) {
                                System.out.println(10l-id);
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                            System.out.println("第二个线程："+(id=Thread.currentThread().getId()));
                            return (Object) id;
                        })
                        ,(res->{
                            System.out.println("返回值为："+res);
                            return (Object) res;
                        })
                )
      ;
      System.out.println("get："+future01.get().toString());

结果；

      第一个线程：16
      5
      返回值为：16
      get：16
      4
      3
      2
      1
      0
      第二个线程：17


第二个线程为5到0的倒计时
可以看到，第一个线程完成后就开启下一个线程，同时下一个线程可以感知结果、本身有返回值；
另外未完成的那一个线程依然会执行




2.acceptEither方法：

      CompletableFuture<Void> future01=CompletableFuture.supplyAsync(()->{
          Long id;
          System.out.println("第一个线程："+(id=Thread.currentThread().getId()));
          return (Object) id;
      },executorService)
            .applyToEither(
                        CompletableFuture.supplyAsync(()->{
                            Long id=0l;
                            while((5l-++id)>=0l) {
                                System.out.println(10l-id);
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                            System.out.println("第二个线程："+(id=Thread.currentThread().getId()));
                            return (Object) id;
                        })
                        ,(res->{
                            System.out.println("返回值为："+res);
                        })
                )
      ;
      System.out.println("get："+future01.get().toString());

结果：

      5
      第一个线程：16
      返回值为：16
      Exception in thread "main" java.lang.NullPointerException: Cannot invoke "Object.toString()" because the return value of "java.     util.concurrent.CompletableFuture.get()" is null
      	at com.katzenyasax.mall.search.thread.CompletableFutureTest.main(CompletableFutureTest.java:177)
      4
      3
      2
      1
      0
      第二个线程：17

更直观地看到，get方法在第三个线程结束后就执行了，只不过此时函数式编程块的返回值为Void，所以get方法报错







3.runAfterEither方法：

      CompletableFuture<Void> future01=CompletableFuture.supplyAsync(()->{
          Long id;
          System.out.println("第一个线程："+(id=Thread.currentThread().getId()));
          return (Object) id;
      },executorService)
            .runAfterEither(
                        CompletableFuture.supplyAsync(()->{
                            Long id=0l;
                            while((5l-++id)>=0l) {
                                System.out.println(10l-id);
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                            System.out.println("第二个线程："+(id=Thread.currentThread().getId()));
                            return (Object) id;
                        })
                        ,(()->{
                            System.out.println("第三个线程");
                        })
                )
      ;
      System.out.println("get："+future01.get().toString());

结果：

      5
      第一个线程：16
      第三个线程
      Exception in thread "main" java.lang.NullPointerException: Cannot invoke "Object.toString()" because the return value of "java.     util.concurrent.CompletableFuture.get()" is null
      	at com.katzenyasax.mall.search.thread.CompletableFutureTest.main(CompletableFutureTest.java:203)
      4
      3
      2
      1
      0
      第二个线程：17


该方法无结果感知、无返回值








### 多任务组合
p202

下列皆为静态方法。


allOf方法，接收多个Completable<Void>线程，全部执行完后开启下一个线程。
注意要求接收的线程全部无返回值，方法本身无结果感知、无返回值


anyOf方法，接收多个Completable<Object>线程，有一个完成就开启下一个线程。
有结果感知，有返回值，其返回值是类似于一个报告的Object类对象，例如：

      CompletableFuture<Object> res = CompletableFuture.anyOf(future01);
      System.out.println(res);

其结果为：

      第一个线程：16
      java.util.concurrent.CompletableFuture@5d3411d[Completed normally]

要获取线程的返回值，使用get方法：

      CompletableFuture<Object> res = CompletableFuture.anyOf(future01);
      System.out.println(res.get());

结果：

      第一个线程：16
      16









# 商城业务：商品详情


## 页面搭建
p202

switchhosts写入：

      192.168.74.130 mall-item 


nginx中，mall.conf监听列表加入：

      mall-item

重启nginx


配置网关：

        - id: item-route
          uri: lb://mall-product
          predicates:
            - Host=**.mall-item


引入item.html（谷粒商城完全版源代码那个，那个是直接调试好前端的），修改必要的一些字段


将必要的js、css等加入到nginx，路径为 /mydata/nginx/html/static/item
先将其放到一个item文件夹下，执行：

      mv item /mydata/nginx/html/static/


随后在product/web中写一个ItemController：

      @Controller
      public class ItemController {
          @GetMapping("{skuId}.html")
          public String showItem(@PathVariable String skuId){
              return "item.html";
          }
      }

访问既可以看到基础的页面
之后要根据sku返回封装数据






## 数据封装
p204

要返回契合页面要求的数据
创建vo：

      @Data
      public class SkuItemVo {
          //1、sku基本信息的获取  pms_sku_info
          private SkuInfoEntity info;

          private boolean hasStock = true;

          //2、sku的图片信息    pms_sku_images
          private List<SkuImagesEntity> images;

          //3、获取spu的销售属性组合
          private List<SkuItemSaleAttrVo> saleAttr;

          //4、获取spu的介绍
          private SpuInfoDescEntity desc;

          //5、获取spu的规格参数信息
          private List<SpuItemAttrGroupVo> groupAttrs;

          //6、秒杀商品的优惠信息
          private SeckillSkuVo seckillSkuVo;
      }

其中SkuItemSaleAttrVo、SpuItemAttrGroupVo、SeckillSkuVo为自定义vo：

      @Data
      public static class SkuItemSaleAttrVo {
          private Long attrId;
          private String attrName;
          private List<AttrValueWithSkuIdVo> attrValues;
      }

      @Data
      public static class SpuItemAttrGroupVo {
          private String groupName;
          private List<Attr> attrs;
      }

    @Data
    public static class SeckillSkuVo {
        private Long promotionId;
        private Long promotionSessionId;
        private Long skuId;
        private BigDecimal seckillPrice;
        private Integer seckillCount;
        private Integer seckillLimit;
        private Integer seckillSort;
        private Long startTime;
        private Long endTime;
        private String randomCode;
    }

而对于SkuItemSaleAttrVo，抢占的AttrValueWithSkuVo也是自定义的：

      @Data
      public static class SpuItemAttrGroupVo {
          private String groupName;
          private List<Attr> attrs;
      }

其中Attr：

      @Data
      public class Attr {
      
          private Long attrId;
          private String attrName;
          private String attrValue;
      }



## 基本实现


ItemController:

      @GetMapping("/{skuId}.html")
      public String showItem(@PathVariable("skuId") String skuId, Model model){
          SkuItemVo item=spuInfoService.getSkuItem(skuId);
          model.addAttribute("item",item);
          return "item";
      }

其中自定义方法getSkuItem：

     /**
      *
      * @param skuId
      * @return finale
      *
      *
      * 根据skuId，获取符合详情页的所有内容
      * 返回值为一个SkuItemVo对象
      *
      *
      */
      @Override
      public SkuItemVo getSkuItem(String skuId) {
          //结果封装
          SkuItemVo finale=new SkuItemVo();
          //sku基本信息
          SkuInfoEntity skuInfo=skuInfoDao.selectById(skuId);

          //1.sku基本信息，直接通过mapper和skuId获取
          finale.setInfo(skuInfo);

          //是否有货
          //默认有货
          //finale.setHasStock(true);

          //2.图片信息
          finale.setImages(skuImagesDao
                  .selectList(new QueryWrapper<SkuImagesEntity>()
                          .eq("sku_id", skuId)
                  )
          );

          //3.spu销售信息组合
          List<SkuItemVo.SkuItemSaleAttrVo> saleAttr=this.getSkuItemSaleAttrVo(skuInfo.getSpuId());
          finale.setSaleAttr(saleAttr);
          
          //4.spu介绍
          finale.setDesc(spuInfoDescDao
                  .selectById(skuInfo.getSpuId())
          );
          
          //5.spu规格参数
          List<SkuItemVo.SpuItemAttrGroupVo> groupAttrs=this.getSpuItemAttrGroupVo(skuInfo.getSpuId());
          finale.setGroupAttrs(groupAttrs);

          //6.商品的优惠信息
          // TODO 远程调用coupon模块，获取优惠信息
          
          System.out.println(JSON.toJSONString(finale));
          return finale;
      }

其中方法getSkuItemSaleAttrVo：

      /**
      *
      *
      * @param spuId
      * @return
      *
      * 根据spuId，返回所有与之相关的Sku、销售属性的vo
      *     1.0版本：属性之间自由组合，但是不能映射到确定的一个sku
      *
      */
      private List<SkuItemVo.SkuItemSaleAttrVo> getSkuItemSaleAttrVo(Long spuId) {

          /**
          * 根据spuId来查，spuId对应多个skuId
          * 有且只有一个spu
          *
          * 大致思路是，
          *      1.通过skuId获取spuId（skuInfo）
          *          1.1.再从spuId获取对应的所有skuId_RelatingSpu（pms_sku_info）
          *          1.2.通过spuId获取所有spu对应的attr_RelatingSpu（pms_product_attr_value）
          *              1.2.1.通过遍历attr_RelatingSpu，获取所有关联spu的saleAttr_RelatingSpu（pms_sku_sale_attr_value）
          *
          */

          //所有的销售属性和sku的关联表
          List<SkuSaleAttrValueEntity> relation_skuSale_attrValue=skuSaleAttrValueDao.selectList(null);

          //1.1.spu关联的所有的skuId_RelatingSpu
          List<Long> skuIds_RelatingSpu=skuInfoDao.selectList(
                          new QueryWrapper<SkuInfoEntity>().eq(
                                  "spu_id",spuId)
                  ).stream()
                  .map(e-> e.getSkuId())
                  .collect(Collectors.toList());


          //所有属性AllAttrs
          //因为该spu对应的attrId并非只有一个，为了避免循环查库，这里直接查询所有attr
          List<AttrEntity> AllAttrs=attrDao.selectList(null);

          //与spu对应的所有属性attr_RelatingSpu
          //在pms_product_attr_value中查询，spuId和attrId为一对多的关系，因此这里直接查询了关联spuId的所有attr
          List<Long> attr_RelatingSpu=productAttrValueDao.selectList(
                          new QueryWrapper<ProductAttrValueEntity>()
                                  .eq("spu_id",spuId)
                  )
                  .stream().map(e->{
                              return e.getAttrId();
                          }
                  )
                  .collect(Collectors.toList());


          //1.2.1与spu对应的所有销售属性saleAttr_RelatingSpu
          //从所有属性AllAttr中查询，主要条件为attrId必须与spu关联（即包含于attr_RelatingSpu）、且必须是销售属性（即attrType不为0）
          List<AttrEntity> saleAttr_RelatingSpu=new ArrayList<>();
          AllAttrs.forEach(entity->{
              if(attr_RelatingSpu.contains(entity.getAttrId()) && entity.getAttrType()!=1){
                  saleAttr_RelatingSpu.add(entity);
              }
          });

          //遍历与spu关联的销售属性saleAttr_RelatingSpu
          List<SkuItemVo.SkuItemSaleAttrVo> saleAttr = saleAttr_RelatingSpu.stream().map(sale -> {
              //该遍历下，每个当前元素都是大集合的单个元素

              //当前元素封装对象vo
              SkuItemVo.SkuItemSaleAttrVo vo = new SkuItemVo.SkuItemSaleAttrVo();

              //vo的两个单字成员变量
              vo.setAttrId(sale.getAttrId());
              vo.setAttrName(sale.getAttrName());

              //vo的一个集合成员变量初始化
              vo.setAttrValues(new ArrayList<>());


              //存储attrValue的集合，用于判断去重
              List<String> templeValue=new ArrayList<>();
              //遍历所有sku和销售属性的关系
              relation_skuSale_attrValue.forEach(relation -> {
                  //确保attrValue不重复
                  if(!templeValue.contains(relation.getAttrValue())) {
                      //skuId包含于指定的集合内、且attrId为当前遍历销售属性时，才进行参数设置
                      if (skuIds_RelatingSpu.contains(relation.getSkuId()) && relation.getAttrId() == sale.getAttrId()) {
                          //小集合单个元素
                          SkuItemVo.AttrValueWithSkuIdVo skuValue = new SkuItemVo.AttrValueWithSkuIdVo();
                          //小元素设置成员变量
                          skuValue.setSkuIds(relation.getSkuId().toString());
                          skuValue.setAttrValue(relation.getAttrValue());
                          //小元素添加到当前vo的集合变量
                          vo.getAttrValues().add(skuValue);

                          //除此之外将该attrValue加入临时templeValue表，确保下一次进入时不会有重复的attrValue
                          templeValue.add(relation.getAttrValue());
                      }
                  }
              });

              return vo;
          }).collect(Collectors.toList());

          return saleAttr;
      }

getSpuItemAttrGroupVo：

       /**
        *
        * @param spuId
        * @return
        *
        * 根据spuId，返回所有与之相关的attr和attrGroup的vo
        *
        */
        private List<SkuItemVo.SpuItemAttrGroupVo> getSpuItemAttrGroupVo(Long spuId) {
           /**
            * 1.pms_product_attr_value表，通过spuId获取每个attrId
            * 2.pms_attr_attrgroup_relation，通过attrId获取attrGroupId
            * 3.pms_attrgroup，通过attrGroupId获取attrGroupName
            */
            //用这个map，代替SpuItemAttrGroupVo的List，因为这个满足KV对条件
            Map<String,List<Attr>> groupAndAttr=new HashedMap();
            productAttrValueDao.selectList(
                    new QueryWrapper<ProductAttrValueEntity>()
                            .eq("spu_id",spuId)
            ).forEach(
                    pavEntity->{
                        /**
                        * 我只要attrId
                        */
                        //获取attrId
                        Long attrId=pavEntity.getAttrId();
                        //获取该attr对应的attrGroupId
                        Long attrGroupId=attrAttrgroupRelationDao.selectOne(
                                        new QueryWrapper<AttrAttrgroupRelationEntity>()
                                                .eq("attr_id",attrId))
                                .getAttrGroupId();
                        //获取该attr对应的groupName
                        String attrGroupName=attrGroupDao.selectById(attrGroupId).getAttrGroupName();
                        if (groupAndAttr.containsKey(attrGroupName)) {
                            //如果map中存在了以groupName为键的数据
                            //就直接加入该键值对下值的list中
                        }
                        else{
                            //若map中还不存在以groupName为键的数据
                            //则新建一个键值对存入
                            groupAndAttr.put(attrGroupName,new ArrayList<>());
                        }

                        Attr attr=new Attr();
                        attr.setAttrId(attrId);
                        attr.setAttrName(attrDao.selectById(attrId).getAttrName());
                        attr.setAttrValue(productAttrValueDao.selectOne(
                                                //同时匹配attrID和spuId
                                                new QueryWrapper<ProductAttrValueEntity>()
                                                        .eq("spu_id",spuId)
                                                        .and(w->w
                                                                .eq("attr_id",attrId))
                                        )
                                        .getAttrValue()
                        );
                        groupAndAttr.get(attrGroupName).add(attr);
                    }
            );
            List<SkuItemVo.SpuItemAttrGroupVo> groupAttrs=new ArrayList<>();
            groupAndAttr.entrySet().forEach(set->{
                String groupName=set.getKey();
                List<Attr> attrs=set.getValue();
                SkuItemVo.SpuItemAttrGroupVo vo=new SkuItemVo.SpuItemAttrGroupVo();
                vo.setGroupName(groupName);
                vo.setAttrs(attrs);
                groupAttrs.add(vo);
            });
            return groupAttrs;
        }









## 销售类型组合问题
p209


发现销售属性目前实际上是无逻辑关联的，
因为对于一个sku来说其销售属性的所有字段是固定的，而一般一个sku有多个销售属性，导致多个独立的销售属性往往无法映射一个准确的sku

因此需要组合销售属性，方案是，在每一个销售属性后再加上一个skuId的集合，表示有哪些sku有这一个销售属性

在getSkuItemSaleAttrVo上开刀：

      relation_skuSale_attrValue.forEach(relation -> {
          //确保attrValue不重复
          if(!templeValue.contains(relation.getAttrValue())) {
              //skuId包含于指定的集合内、且attrId为当前遍历销售属性时，新添加参数设置
              if (skuIds_RelatingSpu.contains(relation.getSkuId()) && relation.getAttrId() == sale.getAttrId()) {
                  //小集合单个元素
                  SkuItemVo.AttrValueWithSkuIdVo skuValue = new SkuItemVo.AttrValueWithSkuIdVo();
                  //小元素设置成员变量
                  skuValue.setSkuIds(relation.getSkuId().toString());
                  skuValue.setAttrValue(relation.getAttrValue());
                  //小元素添加到当前vo的集合变量
                  vo.getAttrValues().add(skuValue);
                  //除此之外将该attrValue加入临时templeValue表，确保下一次进入时不会有重复的attrValue
                  templeValue.add(relation.getAttrValue());
              }
          }
          else{
              //否则，代表该attrId已经设置了集合，直接在其skuValue后拼接,skuId就
              //查找该skuId是在哪一个attrId下
              vo.getAttrValues().forEach(value->{
                  if(value.getAttrValue().equals(relation.getAttrValue())){
                      value.setSkuIds(value.getSkuIds()+","+relation.getSkuId());
                  }
              });
          }
      });

在判断attrValue是否已经存在的if下，加上一个else，表示将该attrValue所属的所有skuId都拼接到一个字符串上，最后交由前端统一处理











## 自定义配置
p210

自定义线程池：

      @Configuration
      public class ThisThreadPool {
          @Bean
          public ThreadPoolExecutor TPE(){
              ThreadPoolExecutor tpe;
              tpe = new ThreadPoolExecutor(
                      20,                                          //核心线程数
                      200,                                          //最大线程数
                      10,                                         //空闲线程最大存活时间
                      TimeUnit.SECONDS,                           //时间单位（秒）
                      new ArrayBlockingQueue<>(10000),                //建立阻塞队列，允许同时运行线程的数量
                      Executors.defaultThreadFactory(),           //默认的线程工厂
                      new ThreadPoolExecutor.AbortPolicy()        //拒绝服务策略(Abort)，队列满时处理剩余线程的策略
              );
              return tpe;
          }
      }

为了实现线程池参数可调节，再定义一个ConfigurationProperties：

      @ConfigurationProperties(prefix = "mall.thread")
      @Component
      @Data
      public class ThisThreadPoolConfigurationProperties {
          private Integer coreSize;   //核心线程数
          private Integer maxSize;    //最大线程数
          private Integer keepAliveTime;      //存活时间
      }

其中@ConfigurationProperties注解表示直接装配配置，
@Component是为了将其自动装配，使其在application中可被识别
prefix字段就是在yml中配置的前缀，直接在application中配置就可以配置这个Configuration配置类

要生效，添加依赖：

      <!-- ConfigurationProperties的依赖 -->
      <dependency>
          <groupId>org.springframework.boot</groupId>
          <artifactId>spring-boot-configuration-processor</artifactId>
      </dependency>

并且在启动类加上注解，扫描ConfigurationProperties：

      @ConfigurationPropertiesScan

随后在application中进行配置：

      mall:
        thread:
          core-size:  
          max-size:
          keep-alive-time:

然后自定义线程池就可以改成：

      @Autowired
      ThisThreadPoolConfigurationProperties threadConfiguration;
      @Bean
      public ThreadPoolExecutor TPE(){
          ThreadPoolExecutor tpe;
          tpe = new ThreadPoolExecutor(
                  threadConfiguration.getCoreSize(),                                          //核心线程数
                  threadConfiguration.getMaxSize(),                                          //最大线程数
                  threadConfiguration.getKeepAliveTime(),                                         //空闲线程最大存活时间
                  TimeUnit.SECONDS,                           //时间单位（秒）
                  new ArrayBlockingQueue<>(10000),                //建立阻塞队列，允许同时运行线程的数量
                  Executors.defaultThreadFactory(),           //默认的线程工厂
                  new ThreadPoolExecutor.AbortPolicy()        //拒绝服务策略(Abort)，队列满时处理剩余线程的策略
          );
          return tpe;
      }







整个流程的核心是spring的自动装配原理，先手动在application.yml中写配置，再在配置类中指定该配置，并将其映射到成员变量上
如此一来，其结果就是：在application.yml中更改配置时，实际上会直接影响配置类的成员变量，成员变量会跟随的配置更改








## 面向异步编排优化业务
p209

第一个获取skuInfo的线程，后续所有线程例如3、4、5都要使用它的结果，因此要等线程1结束后，3、4、5并行处理
而2和1没什么关系，可以新建一个异步任务独立执行
实现：

      @Override
      public SkuItemVo getSkuItem(String skuId) {
          //结果封装
          SkuItemVo finale=new SkuItemVo();

          CompletableFuture<SkuInfoEntity> thread1=CompletableFuture.supplyAsync(()->{
              //1.sku基本信息，直接通过mapper和skuId获取
              SkuInfoEntity skuInfo=skuInfoDao.selectById(skuId);
              finale.setInfo(skuInfo);
              return skuInfo;
          });

          CompletableFuture<Void> thread2=CompletableFuture.runAsync(()->{
              //2.图片信息
              finale.setImages(skuImagesDao
                      .selectList(new QueryWrapper<SkuImagesEntity>()
                              .eq("sku_id", skuId)
                      )
              );
          },threadPool.TPE());

          CompletableFuture<Void> thread3 = thread1.thenAcceptAsync(res -> {
              //3.spu销售信息组合
              List<SkuItemVo.SkuItemSaleAttrVo> saleAttr = this.getSkuItemSaleAttrVo(res.getSpuId());
              finale.setSaleAttr(saleAttr);
          }, threadPool.TPE());

          CompletableFuture<Void> thread4 = thread1.thenAcceptAsync(res -> {
              //4.spu介绍
              finale.setDesc(spuInfoDescDao
                      .selectById(res.getSpuId())
              );
          }, threadPool.TPE());

          CompletableFuture<Void> thread5 = thread1.thenAcceptAsync(res -> {
              //5.spu规格参数
              List<SkuItemVo.SpuItemAttrGroupVo> groupAttrs = this.getSpuItemAttrGroupVo(res.getSpuId());
              finale.setGroupAttrs(groupAttrs);
          }, threadPool.TPE());

          //6.商品的优惠信息
          // TODO 远程调用coupon模块，获取优惠信息

          //判断1-6的线程是否完成：
          CompletableFuture.allOf(
                  thread1
                  ,thread2
                  ,thread3
                  ,thread4
                  ,thread5
          );
          
          System.out.println(JSON.toJSONString(finale));
          return finale;
      }

核心逻辑就是线程1执行，执行完成后给出返回值也即是一个skuInfoEntity，
此时3、4、5三个线程使用supplyAsync方法接收线程1的返回值并并发执行，直到所有线程执行完成，才会执行最后的return
















# 商城业务：认证服务


## 环境搭建
p211

1.创建mall-auth模块，引入thymeleaf、open feign、spring web、lombok、spring boot devtools；

2.引入common公共模块；

3.启动类上排除mybatis plus依赖：

      @SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})

4.加入nacos，application中：

      spring:
        cloud:
          nacos:
            discovery:
              server-addr: 192.168.74.130:8848
              username: nacos
              password: nacos
              namespace: 311853ea-26c0-46e5-83e9-5d5923e1a333
        application:
          name: mall-auth
        thymeleaf:
          cache: false
      server:
        port: 10400

bootstrap中：

      spring:
        cloud:
          nacos:
            config:
              server-addr: 192.168.74.130:8848
              username: nacos
              password: nacos
              namespace: 311853ea-26c0-46e5-83e9-5d5923e1a333

5.启动类加上：

      @EnableDiscoveryClient

6.添加前端页面（源码里直接拿）

7.switchhost加上：

      192.168.74.130 mall-auth

8.静态资源引入 /mydata/nginx/html/static/
登录放到 /login
注册放到 /reg
  
9.mall.conf中加入监听：

      mall-auth

重启nginx

10.配置网关：

      - id: auth-route
        uri: lb://mall-auth
        predicates:
          - Host=**.mall-auth







## 接口
p212


auth模块编写接口跳转auth界面，AuthController：

      @Controller
      public class LoginController {
      
          @GetMapping("/login.html")
          public String logIn(){
              return "login";
          }

          @GetMapping("/reg.html")
          public String Register(){
              return "reg";
          }
      }

这下可以查询了，但是试想一种场景，发送一个请求直接跳转到页面，不需要再使用空方法作为接口了
使用SpringMVC的ViewController直接映射，在配置类中重新实现：

      @Configuration
      public class WebViewController implements WebMvcConfigurer {
          @Override
          public void addViewControllers(ViewControllerRegistry registry){
              registry.addViewController("/login.html").setViewName("login");
              registry.addViewController("/reg.html").setViewName("reg");
          }
      }

表示将这俩路径注册为对应的页面了
上面两个空方法可以删除了





## 短信验证码
p213

接口：

      /**
       * @param phone
       *
       * 发送短信验证码
       * 使用的是阿里云的服务
       *
       *
       *
       */
       @GetMapping("/sms/sendCode")
       public void sendCode(@RequestParam String phone){
           String host = "https://dfsns.market.alicloudapi.com";
           String path = "/data/send_sms";
           String method = "POST";
           String appcode = "57912ef0235f4cd184fe7b241b5ae347";
           Map<String, String> headers = new HashMap<String, String>();
           //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
           headers.put("Authorization", "APPCODE " + appcode);
           //根据API的要求，定义相对应的Content-Type
           headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
           Map<String, String> querys = new HashMap<String, String>();
           Map<String, String> bodys = new HashMap<String, String>();
           bodys.put("content", "code:9898");
           bodys.put("template_id", "CST_ptdie100");
           bodys.put("phone_number", phone);
           
           try {
               /**
               * HttpUtils请从
               * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/utilHttpUtils.java
               */
               HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
               System.out.println(response.toString());
               //获取response的body
               //System.out.println(EntityUtils.toString(response.getEntity()));
           } catch (Exception e) {
               e.printStackTrace();
           }
       }

测试结果可行

将其可配置化，写一个类再次封装，要配置的信息就是url等信息：

      @ConfigurationProperties(prefix = "send-code")
      @Component
      @Data
      public class SendCodeConfigurationProperties {
          private String host;
          private String path;
          private String appcode;
          public void send(String phone){
            String host = this.getHost();
            String path = this.getPath();
            String method = "POST";
            String appcode = this.getAppcode();
            Map<String, String> headers = new HashMap<String, String>();
            //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
            headers.put("Authorization", "APPCODE " + appcode);
            //根据API的要求，定义相对应的Content-Type
            headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            Map<String, String> querys = new HashMap<String, String>();
            Map<String, String> bodys = new HashMap<String, String>();
            bodys.put("content", "code:9898");
            bodys.put("template_id", "CST_ptdie100");
            bodys.put("phone_number", phone);
            try {
                /**
                 * HttpUtils请从
                 * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/   HttpUtils.java
                 */
                HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
                System.out.println(response.toString());
                //获取response的body
                //System.out.println(EntityUtils.toString(response.getEntity()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
      }

注意添加依赖、启动类加上扫描配置
随后在application中配置：

      mall:
        send-code:
          appcode: 57912ef0235f4cd184fe7b241b5ae347
          path: /data/send_sms
          host: https://dfsns.market.alicloudapi.com

因此在接口可以改为：

      @GetMapping("/sms/sendCode")
        public void sendCode(@RequestParam String phone){
            sendCodeConfigurationProperties.send(phone);
        }

测试之下也一样可以使用



=======================================（待考证）======================================================


再迭代一下，因为之后的业务中，总是其他的服务来调用该服务，且要获取返回值来决定下一步业务；
故此controller改为：

      @GetMapping("/sms")
      public R sendCode(@RequestParam("phone") String phone,@RequestParam("code")  String code){
          sendCodeConfigurationProperties.send(phone,code);
          return R.ok();
      }

其中的phone由前端传入，而code则由调用该接口的服务提供


===================================================================================================













## 验证码校验
p214


一是验证码需要随机生成，而是验证码的校验问题


1.code随机

      this.code=new Random().nextLong(1000,9999);



2.code防刷
刷新就可以跳过60秒倒计时，可能造成短时间内恶意访问服务器造成宕机等后果，因此将一个手机号码对应的数据加入缓存，存活时间为60秒
例如，一个号码刚开始发送验证码时，会存入：key:182****8845Phone value:5489 ttl:60s
服务器会执行发送验证码的请求，而60s倒计时会继续到技术
当60s倒计时未完时，redis中依然查得到182****8845Phone，那么此时前端申请发送验证码会被拒绝执行
只有当60s结束时，该号码才能再次申请验证码
而阿里云的短信验证码服务，本身要求同一个手机号10分钟内申请此时不得超过3次

引入redis依赖，application中配置redis

实现，拿配置类里的方法开刀：

      public R send(String phone){

        //创建操作柄
        ValueOperations<String,String> ops= redisTemplate.opsForValue();
        if(StringUtils.isNotEmpty(ops.get("Phone"+phone))){
            return R.ok("请60秒后重试！");
        }

        ······（发送验证码的业务）······

        ops.set("Phone"+phone,code.toString(),60, TimeUnit.SECONDS);

        //判断状态码
        if(httpResult==400){
            return R.error("请求参数错误");
        }
        else if(httpResult==403){
            return R.error("请求次数过多，请十分钟后再试！");
        }
        else if(httpResult==500){
            return R.error("服务器错误");
        }
        else{
            return R.ok("已发送验证码").put("code",this.code);
        }
    }



那么这样一来验证码有效时长就只有60秒？我要是想让他10分钟内有效呢？
那就把存活时间改成10分钟
解决方案就是在code后面用-拼接一个System.currentTimeMills
且要将存活时间（时长、单位）,都加入配置文件可配置化
所以最终：

      /**
       *
       * @param phone
       * @return
       *
       * 给出手机号码phone，要求向其发送随机验证码
       *
       * 并且要包含防刷、等功能
       *
       */
      public R send(String phone){
          //创建操作柄
          ValueOperations<String,String> ops= redisTemplate.opsForValue();
          //拿取数据（code和上一次发送时间），如果有的话，没有就初始化成原点
          String codeAndTime;
          if(StringUtils.isNotEmpty(ops.get(this.pre + phone)))
          //如果redis中存在数据
          {
              codeAndTime=ops.getAndDelete(this.pre+phone);
              //上一次发送的时间
              Long lastTime = Long.parseLong(codeAndTime.split("-")[1]);

              if (System.currentTimeMillis()-lastTime<this.timeTrap*1000) {   //当和上次发送验证码间隔小于60秒时，说明未到时间（注意  timeTrap是毫秒，乘1000才是秒）
                  return R.error("请60秒后重试！");
              }
          }

          //随机验证码
          Long code = new Random().nextLong(1000, 9999);
          //状态码，初始为200
          int httpResult=200;
          //========== 发送验证码api   ===========================================
          String host = this.host;
          String path = this.path;
          String method = "POST";
          String appcode = this.appcode;
          Map<String, String> headers = new HashMap<String, String>();
          //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
          headers.put("Authorization", "APPCODE " + appcode);
          //根据API的要求，定义相对应的Content-Type
          headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
          Map<String, String> querys = new HashMap<String, String>();
          Map<String, String> bodys = new HashMap<String, String>();
          bodys.put("content", "code:" + code);
          bodys.put("template_id", "CST_ptdie100");
          bodys.put("phone_number", phone);
          try {
              /**
               * HttpUtils请从
               * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/ HttpUtils.java
               */
              HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
              //httpResult改成api返回的状态码
              httpResult = response.getStatusLine().getStatusCode();
              System.out.println(response.toString());
              //获取response的body
              //System.out.println(EntityUtils.toString(response.getEntity()));
          } catch (Exception e) {
              e.printStackTrace();
          }
          //===========================================================================================
          //将结果存入redis
          ops.set(
                  this.pre + phone
                  , code.toString() + "-" + System.currentTimeMillis()
                  , this.aliveTime
                  , this.unit
          );
          //判断状态码
          if(httpResult==400){
              return R.error("请求参数错误");
          }
          else if(httpResult==403){
              return R.error("请求次数过多，请十分钟后再试！");
          }
          else if(httpResult==500){
              return R.error("服务器错误");
          }
          else {
              return R.ok("已发送验证码").put("code",code);
          }
      }

在调用api之前，就排除掉非法情况










## 注册页面
p215


注册的url地址：

      http://mall-auth/register

请求参数：

      userName: katzenyasax
      password: 13594869076aA!
      phone: 18290531268
      code: 

code为验证码，需要在后端进行验证，即查询是否和redis中对应电话号码的值一致


编写接口RegisterController接收注册，还要一个UserVo接收参数：

      @Data
      public class UserRegisterVo {
          @NotEmpty(message = "未提交用户名！")
          @Length(min = 6,max = 18,message = "用户名必须是6-18位的字符")
          private String userName;
          @NotEmpty(message = "未提交密码！")
          @Length(min = 6,max = 18,message = "密码必须是6-18位的字符")
          private String password;
          @NotEmpty(message = "未提交手机号码！")
          @Pattern(regexp = "1[0-9]{10}",message = "手机号码必须是11位数字！")
          private String phone;
          @NotEmpty(message = "未提交验证码！")
          @Pattern(regexp = "[0-9]{4}",message = "验证码必须是4位数字！")
          private String code;
      }

在里面还需要手动校验数据，防止前端漏洞，保证数据安全

接口：

      /**
      *
      * @param
      * @return
      *
      * 注册用户
      * 在此之前要先判断验证码是否相同
      * 还要判断用户名和手机号未被占用（远程调用member服务）
      *
      */
      //todo 分布式session问题
      //  
      @RequestMapping("/register")
      public String register(@Valid UserRegisterVo vo, BindingResult result, /*Model model*/ RedirectAttributes redirectAttributes){

          //1.若数据有异常，跳转回注册页面
          if(result.hasErrors()){
              //并且要返回错误字段
              Map<String, String> errors = result.getFieldErrors().stream().collect(Collectors.toMap(
                      error -> "msg"
                      ,error -> error.getDefaultMessage()
              ));
              //  model.addAttribute(errors);
              //不用model了，这里使用redirectAttributes，防止重复提交表单

              System.out.println("数据异常");

              redirectAttributes.addFlashAttribute("errors",errors);
              //转到reg.html页面，带上所有的error这个map的数据
              return "redirect:http://mall-auth/reg.html";
          }

          //2.判断code是否正确
          boolean isRightCode = registerService.isRightCode(vo);
          if(isRightCode){
              //验证码正确
              //            registerService.Register(vo);
              System.out.println("验证码正确");
              //并且还要删除code
              registerService.deleteCode(vo.getPhone());
              return "redirect:http://mall-auth/login.html";
          }
          else {
              //验证码错误
              Map<String,String> errors=new HashMap<>();
              errors.put("msg",res.get("msg").toString());
              redirectAttributes.addFlashAttribute("errors",errors);
              System.out.println("验证码错误");
              return "redirect:http://mall-auth/reg.html";
          }
      }

自定义方法就不放了，随便写写就完了

1.先校验数据，判断code是否正确
全部正确后，重定向（redirect）到login页面，否则重定向reg页面重新注册

2.之所以用redirectAttribute替代model，是为了防止重复提交表单

3.令牌机制，比如code完成使命后就要删除





未完成的：

1.注册方法暂时未完成，需要远程调用其他服务

2.关于session分布式问题，以后再讲







## 注册会员
p216

需要远程调用member模块，流程：

1.定义远程调用公共UserRegisterTo：

      @Data
      public class UserRegisterTo {
          @NotEmpty(message = "未提交用户名！")
          @Length(min = 6,max = 18,message = "用户名必须是6-18位的字符")
          private String userName;
          @NotEmpty(message = "未提交密码！")
          @Length(min = 6,max = 18,message = "密码必须是6-18位的字符")
          private String password;
          @NotEmpty(message = "未提交手机号码！")
          @Pattern(regexp = "1[0-9]{10}",message = "手机号码必须是11位数字！")
          private String phone;
      }

相比UserRegisterVo，至少了一个code，对于合法的表单这个code也不再需要了

2.编写feign接口：

      @FeignClient("mall-member")
      public interface MemberFeign {
      
          /**
           *
           * @param vo
           * @return
           *
           * 注册会员
           */
          @PostMapping("/member/member/register")
          R register(@RequestBody UserRegisterTo to);
      }

注意路径完整

3.auth模块调用member模块：

      /**
       *
       * @param vo
       *
       * 根据vo注册用户
       * 需要调用远程模块mall-member
       *
       */
      @Override
      public R Register(UserRegisterVo vo) {
          UserRegisterTo to=new UserRegisterTo();
          //复制to
          BeanUtils.copyProperties(vo,to);
          R r = memberFeign.register(to);
          return r;
      }

3.auth启动类上加：

      @EnableFeignClients(value = "com.katzenyasax.mall.auth.feign")

表示开启远程调用

4.memeber模块内，加上接口：

      /**
       *
       * @param to
       * @return
       *
       * 注册会员
       */
      @PostMapping("/register")
      R register(@RequestBody UserRegisterTo to){
          R r=memberService.register(to);
          return r;
      }

5.register方法：

      /**
      *
      * @param to
      * @return
      *
      * 注册用户
      *
      */
      @Override
      public R register(UserRegisterTo to) {

          //在此之前判断数据库中是否已有重复的手机号、用户名
          //手机号和用户名作为唯一标识（后续可更新）
          if (baseMapper.selectList(new QueryWrapper<MemberEntity>().eq("username", to.getUserName())).size() > 0) {
              System.out.println("MemberService：已存在用户名为" + to.getUserName() + "的用户！");
              return R.error(USERNAME_ALREADY_EXIST.getMsg());
          }
          if (baseMapper.selectList(new QueryWrapper<MemberEntity>().eq("mobile", to.getPhone())).size() > 0) {
              System.out.println("emberService：已存在手机号为" + to.getUserName() + "的用户！");
              return R.error(PHONE_ALREADY_EXIST.getMsg());
          }

          //封装对象，新用户
          MemberEntity finale=new MemberEntity();
          //三个to参数
          finale.setUsername(to.getUserName());
          finale.setPassword(to.getPassword());
          finale.setMobile(to.getPhone());

          //初始化参数
          finale.setLevelId(1L);
          finale.setNickname(to.getUserName());
          finale.setIntegration(0);
          finale.setGender(0);
          finale.setStatus(1);
          finale.setCreateTime(new DateTime());

          //存入数据库
          baseMapper.insert(finale);
          
          //反馈
          return R.ok(REGISTER_SUCCESS.getMsg());
      }













## 密码加密
p217


密码不可明文存于数据库，存入时分别要进行加密操作
且应当进行的是不可逆加密，也即不能推算出原本密码，这样一来进行任何类似登录、安全确认等需要密码的操作直接将输入的密码再次加密对比数据库就可以了

解决方案：使用MD5加密算法+盐值

BCryptPasswordEncoder可以用于加密和匹配，加密，使用encode方法：

      new BCryptPasswordEncoder().encode("xxx");

匹配，即是使用明文密码，和已经撒过盐的密文密码进行匹配，encode可以自动匹配：

      new BCryptPasswordEncoder().matches(password,passwordEncoded)

返回值为boolean




改变密码：

      //加盐加密，BCryptPasswordEncoder自带加盐和读取盐值功能
      finale.setPassword(
              new BCryptPasswordEncoder().encode(to.getPassword())
      );






## 登录
p218


用户输入用户名和密码进行登录
请求地址：

      http://mall-auth/login

参数：

      loginacct: katzenyasax
      password: 123456

弄一个to：

      @Data
      public class UserLoginTo {
          @NotEmpty(message = "未提交用户名或手机号！")
          @Length(min = 6,max = 18,message = "必须是6-18位的字符或11位数字")
          private String loginacct;
          @NotEmpty(message = "未提交密码！")
          @Length(min = 6,max = 18,message = "密码必须是6-18位的字符")
          private String password;
      }

auth模块下接口：

      /**
       *
       * 用户登录
       */
      @RequestMapping("/login")
      public String login(@Valid UserLoginTo to, BindingResult result, RedirectAttributes redirectAttributes){
          if(result.hasErrors()){
              //并且要返回错误字段
              Map<String, String> errors = result.getFieldErrors().stream().collect(Collectors.toMap(
                      error -> "msg"
                      ,error -> error.getDefaultMessage()
              ));

              redirectAttributes.addFlashAttribute("errors",errors);
              return "redirect:http://mall-auth/login.html";
          }
          System.out.println(to.toString());
          R r=loginService.login(to);
          System.out.println("LoginController Login: "+r);
          if(r.get("code").equals(0)){
              //登陆成功
              return "redirect:http://katzenyasax-mall";
          }
          else {
              Map<String,String> errors=new HashMap<>();
              errors.put("msg",r.get("msg").toString());
              redirectAttributes.addFlashAttribute("errors",errors);
              return "redirect:http://mall-auth/login.html";
          }
      }

可以在前端返回errors的msg

方法login：

      /**
       *
       * @return
       *
       * 登录
       * 需要远程调用member模块
       *
       */
      @Override
      public R login(UserLoginTo to) {
          R r=memberFeign.login(to);
          System.out.println("LoginService Login: "+r);
          return r;
      }

远程调用：

      @RequestMapping("/member/member/login")
      R login(UserLoginTo to);

member的接口：

      /**
       * 用户登录
       * 被auth远程调用
       */
      @RequestMapping("/login")
      R login(@RequestBody UserLoginTo to){
          R r=memberService.login(to);
          System.out.println("MemberController Login:"+r);
          return r;
      }

方法login：

      /**
      * 用户登录
      */
      @Override
      public R login(UserLoginTo to) {
          //输入的账号有可能是用户名，也有可能是手机号，所以要同时查
          List<MemberEntity> members=baseMapper.selectList(
                  new QueryWrapper<MemberEntity>()
                          .eq("username",to.getLoginacct())
                          .or().eq("mobile",to.getLoginacct())
          );

          //1.账号不存在
          if(members.size()==0){
              System.out.println("账户不存在");
              return R.error("账户不存在");
          }

          //2.密码错误
          //如果查得到，只可能查到一个，直接查索引为0的元素
          MemberEntity thisAccount=members.get(0);
          //加密器
          BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
          //对密码进行加密
          String passwordEncoded=encoder.encode(thisAccount.getPassword());
          //匹配
          if(!encoder.matches(to.getPassword(),passwordEncoded)){
              return R.error("密码错误");
          }
          
          //完成正确反馈
          return R.ok("登陆成功");
      }


测试一下，url：

      http://mall-auth/login

参数：

      loginacct: katzenyasax
      password: 123456    

结果是登陆成功

但是发现登录成功后，页面也没有任何变化，登录的账户信息并没有映射到页面







## 扫码登陆（待实现）
p221

尝试用OAuth2.0用qq扫描登录

APP ID：102068535
APP Key：eDcqsskPr563rS0K





## 登出

      auth.katzenyasax-mall.com/logout.html

接口：

      /**
       *
       * @param session
       * @return
       *
       * 用户登出，将session置为空
       */
      @RequestMapping("/logout.html")
      public String logOut(HttpSession session){
          session.setAttribute(AuthConstant.USER_LOGIN,null);
          return "redirect:http://katzenyasax-mall.com";
      }








# 分布式session问题
p225

所谓session，就是前端（也即是浏览器）用以存储部分后端传来的数据的一块空间，
可以看作是一整个会话，只要页面不关闭，会话就不会结束，session就能一直保存数据
但是当页面关闭后，会话结束，session无法再存储数据，下一次访问页面时就没有保存的用户登录信息等
所以使用cookie，浏览器会从session抽取数据保存为cookie，即使会话关闭，cookie也能一直保存，下一次访问相同的网址时就能带上对应的cookie了

cookie的存储结构是：

      jsessionid:
      value:

和map类似


在登录界面加上cookie，首先让登录成功时返回用户信息：
member模块下，MemberServiceImpl的login方法：

      //完成正确反馈
      return R.ok("登陆成功").put("LoginUser",thisAccount);
    
让r返回了有关LoginUser，存放的是该用户的信息
随后在auth模块下的LoginController，登陆成功后返回session：

       //返回session
       session.setAttribute("loginUser",r.get("LoginUser"));







现在的问题是，登陆成功后，用户信息无法同步到页面，
因为我们是在mall-auth域名下登录的，所有cookie都存在了mall-auth域名下，
而我们其他的域名例如katzenyasax-mall主页面都无法共享这个cookie，也就不能记忆用户登录的信息
同域名、不同服务也不能共享
这就是session在分布式下不同服务之间无法共享的问题





## 解决
p226

### session复制

配置tomcat或其他，使所有cookie都存到所有服务

缺点是不安全，而且占用空间大

淘汰


### hash一致性

配置nginx，为ip加上一个hash算法，使其每次访问都被负载均衡到同一台服务器，从而每次都带有cookie

缺点是服务器数量增加或减少时，ip的hash运算结果可能会变化（因为hash和服务器数量有关），导致部分用户的ip会负载均衡到其他服务，导致无cokkie

但是session本身就是有时效的，所以其实问题不大

由于本身服务器少，选用了




### 统一存储

最好想到的一集

把所有服务得到的cookie统一存到redis或其他nosql的中间件，

缺点是访问redis可能带来性能问题，网络通信比读取内存慢太多了

但是使用SpringSession可以解决问题








# 有关switchHosts失效的问题

关梯子，否则加上父域名或者子域名会失效，导致只会使单独的域名生效

随后为了实现session，一律将域名改为：**.katzenyasax-mall.com





# nginx配置明确

conf.d中mall.conf，监听的端口为80，监听的url为：

      katzenyasax-mall.com
      search.katzenyasax-mall.com
      auth.katzenyasax-mall.com
      item.katzenyasax-mall.com

html/static/index/js/catalogLoader.js中，导向search的url：

      search.katzenyasax-mall.com





# 有关netty报错的问题

即reactor.netty.http.client.PrematureCloseException: Connection prematurely closed DURING response

将gateway配置文件中，域名大的一个放到最后，就解决了







# SpringSession

## 配置
p227

auth包中加入依赖：

      <!-- https://mvnrepository.com/artifact/org.springframework.session/spring-session-data-redis -->
      <dependency>
          <groupId>org.springframework.session</groupId>
          <artifactId>spring-session-data-redis</artifactId>
          <version>3.1.3</version>
      </dependency>

因为使用redis存session，而且很多服务都要调
例如product（主页）、search（检索）、auth（注册登录等），
而且还需要引入redis的依赖

启动类加上：

      @EnableRedisHttpSession

这样一来，当auth模块登陆成功后，redis中应当会存入一个session

尝试登录，成功后redis存入了一个Hash，名为：

      spring:session:sessions:aea3a7b7-9f92-4a81-987f-9b5f9b039c41

而由于之前手动地存入了一个loginUser，再来看这Hash，里面确实有一个名为sessionAttr:loginUser的key
除此之外，还有三个字段：

      lastAccessedTime
      creationTime
      maxInactiveInterval

不管怎样，session都存到了redis中了






## 实现
p228

接下来的问题是，session的作用域只在auth.katzenyasax-mall.com，不会在父域（katzenyasax-mall.com）生效
而我们可以在浏览器中手动修改作用域到父域，刷新之后可以发现，其余的服务也可以读取的session的数据，即我们存到session的loginUser

所以要做的是，将session的作用域从单auth放大的整个父域

1.首先在product模块中引入依赖，启动类上加注解

2.然后在auth模块写一个配置类配置组件，因为以后也许会有更多的服务会读取到该session

      @Configuration
      public class SessionConfiguration {
           /**
            * 
            * @return
            * 
            * 自定义session
            * 
            */
          @Bean
          public CookieSerializer cookieSerializer(){
              DefaultCookieSerializer cookieSerializer=new DefaultCookieSerializer();
              cookieSerializer.setDomainName("katzenyasax-mall.com");     //设置作用域为父域
              //cookieSerializer.setCookieName("katzenyasax-mall::session");    //session的名字
              return cookieSerializer;
          }
      }

3.还可以自定义session的redis序列化机制，可以将redis中存的东西改成json格式：

      /**
       *
       * @return
       *
       * 将redis的序列化机制改成json（用到Jackson的序列化器）
       *
       */
      @Bean
      public RedisSerializer<Object> springSessionDefaultRedisSerializer(){
          return new GenericJackson2JsonRedisSerializer();
      }

相当于直接将redis的序列化机制覆盖成json的序列化机制
而且注意方法名必须是springSessionDefaultRedisSerializer，否则不会生效

4.之后将这个配置类复制一份放到auth包，
注意尽量不要放到common包，因为有些服务不用redis，可能会人为挖坑





之后重启测试

登录之后，不仅可以使整个父域下的所有页面都有登录信息，redis中存储的数据也变成了json格式：

      sessionAttr:loginUser：
      { "@class": "java.util.LinkedHashMap", "id": 1, "levelId": 99, "username": "Katzenyasax", "password": "$2a$10$YQT/      msPf0FLIrwHJmB5wn.Omsh4Uk9cNWdJye1iN7c8HtANm4pAha", "nickname": "KatzenyaSax", "mobile": "18290531268", "email": null, "header":      null, "gender": null, "birth": null, "city": null, "job": null, "sign": null, "sourceType": null, "integration": null, "growth":      null, "status": null, "createTime": "2023-10-17T11:15:39.000+00:00" }

      lastAccessedTime：
      1697708810236

      creationTime：
      1697708808976

      maxInactiveInterval：
      1800


这样就完成了


## SpringSession的原理
p229

1.注解













## 点击登录自动跳转
p230


每次重新启动浏览器，都要重新进行登录，非常麻烦，能不能从缓存中拿用户的session呢？
答案是可以，redis中设置session数据时长长一点，在这段时间内从redis拿取数据后再次重置时长，实际上就可以自动登录了


并且从首页登录的逻辑是，点击登录按钮时先访问的：

      auth.katzenyasax-mall.com/login.html

我们没有定义对应的controller，所以这里访问的应该就是页面
用户输入账户后，连带用户数据访问的是

      auth.katzenyasax-mall.com/login

所以如果我们在login.html界面就直接从redis中拿取数据，
然后直接将其setAttribute给.katzenyasax-mall.com，
随后重定向到首页就完成了自动登录的效果

实现
定义一个接口：

      /**
       * 利用redis中保存的数据自动登录
       */
      @GetMapping("/login.html")
      public String autoLogin(HttpSession session){
          Object cookie = session.getAttribute(AuthConstant.USER_LOGIN);
          if(cookie!=null){
              //如果浏览器存入cookie，则直接从cookie拿取
              System.out.println("cookie exists");
              return "redirect:http://katzenyasax-mall.com";
          }
          else {
                  return "login";
          }
      }

注意要把SpringMVC的WebViewConfiguration的页面注释：

      /**
       * 淘汰了，目前login.html被作为接口了
       */
      //registry.addViewController("/login.html").setViewName("login");




还有记得在所有使用到springsession的服务添加配置，以后所有使用到spring session的操作都如下：

      1.redis的依赖并配置redis，和spring session
      2.启动类加上允许httpSession，
      3.application中配置redis的地址和端口
      3.导入session的配置类



测试之下，只要用户信息还在redis中，点击登录就能够自动登录






# 单点登录

## 演示
p231

不同域名下用户登录信息的共享

尝试使用xxl-oss框架

将端口改成：服务器端8880、8881、8882，
客户端配置的redis地址改成192.168.74.130

switchhosts准备三个域名，存放这几个服务：

      127.0.0.1 sso-server.com            认证中心
      127.0.0.1 sso-client1.com           客户端1
      127.0.0.1 sso-client2.com           客户端2

全部往部署服务的本机上转，而且域名要不一样，人为排除spring session的作用

随后整个xxl-oss打包：

      mvn clean package -Dmaven.skip.test=true

每个服务中都得到一个jar包，运行：

      java -jar [jar包名]

登录认证中心：

      http://oss-server.com:8880/xxl-sso-server/login

客户端：

      http://sso-client1.com:8881/xxl-sso-web-sample-springboot/

      http://sso-client2.com:8882/xxl-sso-token-sample-springboot/





## 实现（待实现）


接下来要实现的是：
1.中央服务，即认证中心登陆后，其余客户端也自动登陆
2.客户端登录时，跳转认证中心登录，随后跳转回客户端，此时客户端和其他的客户端也自动登录
3.所有服务共享一个session









# 商城业务：购物车


## 环境搭建
p236

1.创建mall-cart模块，导入spring web、lombok、devtools、thymeleaf、open feign
引入mall-common

2.引入页面（谷粒商城的源码里面偷）
静态资源放到 /mydata/nginx/html/static/cart

      mv cart/ /mydata/nginx/html/static/

3.nginx的mall.conf加上cart.katzenyasax-mall.com

4.网关配置上cart.katzenyasax-mall.com

5.加入配置中心（偷的auth模块的application和bootstrap）

6.启动类加上@EnableDiscoveryClient，排除DataSourceAutoConfiguration.class
因为后面回远程调用，所以加个@EnableFeignClients

写一个viewController测试一下：

      @Configuration
      public class WebViewController implements WebMvcConfigurer {
          @Override
          public void addViewControllers(ViewControllerRegistry registry) {
              registry.addViewController("/success.html").setViewName("success");
              registry.addViewController("/cartList.html").setViewName("cartList");
          }
      }

测试路径：

      http://cart.katzenyasax-mall.com/success.html

结果是可以查询到





## 数据封装
p237

购物车分为两个：
一个是未登录状态下的购物车，所有添加的商品应临时存入本地，如果session关闭，数据不会清空
一个是登录状态下的购物车，此时应当将数据存入服务器中，在登录的时候若此session里面有临时商品，会进行合并，重新添加到登录购物车

除此之外还应该有以下功能：

      1.购物车商品的增删改查
      2.购物车中多选商品合并结账
      3.商品优惠、价格的变化等

所以数据封装：

      @Data
      public class CartItemVo {
          private String skuId;
          private Boolean check;              //是否选中
          private String title;
          private List<String> skuAttrValues;
          private BigDecimal price;
          private String image;
          private Long count;
          private BigDecimal totalPrice;
          private BigDecimal reduce;          //减免
      }

该类用来表示购物车内单个种类的商品
另外使用一个购物车类：

      @Data
      public class Cart {
          List<CartItemVo> items;
          private Long countNum;          //商品总数
          private Long countType;         //商品种类的数量
          private BigDecimal totalAmount;         //总价格
          private BigDecimal reduce;
          /**
          * 另外禁止在vo内写其他方法，vo应当只是一个纯粹的变量容器
          */
      }





## 判断登录状态
p239

逻辑是，登录状态下查看到的是用户的购物车，可以用session读取用户是否登录
用户未登录的情况下，查看到的应该是临时购物车，此时应当创建一个临时用户，存到cookie中去

所以cart服务也要引入springsession

1.引入依赖
2.启动类加上@EnableRedisHttpSession
3.偷一个SessionConfiguration配置类

配置好后，请求路径：

      cart.katzenyasax-mall.com/cart.html

相应的前端页面也要改成这个路径。

1.随后在common中创建一个to，用于封装用户信息：

      @ToString
      @Data
      public class UserInfoTo {
          private Long id;
          private String userKey;
      }

若已登录，则userId非空，且为当前用户的id
若未登录，则userKey为临时用户

2.而在请求发到controller之前，对其进行一个登录的校验，使用拦截器进行校验
  在cart模块定义interceptor.CartInterceptor

      @Component
      public class CartInterceptor implements HandlerInterceptor {
          @Override
          public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
              //用户信息封装，之后要判断浏览器中是否有用户信息并封装
              UserInfoTo userInfoTo=new UserInfoTo();

              //先判断session，即是否已经登陆
              Object thisSession= request.getSession().getAttribute(AuthConstant.USER_LOGIN);
              if(thisSession!= null){
                  //若session中有名为loginUser的cookie，表示用户已登录
                  MemberTo to= JSON.parseObject(JSON.toJSONString(thisSession),MemberTo.class);
                  userInfoTo.setUserId(to.getId());
                  System.out.println("session中："+userInfoTo);
              }else {
                  //未登录，则判断cookie中是否有名为user-key的cookie，表示是否已存在临时用户，没有则应当创建
                  if(request.getCookies()!=null && request.getCookies().length>0) {
                      for (Cookie cookie : request.getCookies()) {
                          if (cookie.getName().equals(CartConstant.TEMPLE_USER)) {
                              //有名为user-key的cookie
                              userInfoTo.setUserKey(cookie.getValue());
                              System.out.println("cookie中"+userInfoTo);
                              break;
                          }
                      }
                  }
                  else {
                      //cookie里面都没有user-key，就创建一个
                  }
              }
              return true;
          }
      }

但是这个userInfoTo同时也是我们想要的，把他也一并注入到controller里，此时用到ThreadLocal技术，允许同一个线程内数据的共享
核心思想就是，将拦截器标识为一个线程，拦截器通过到controller时，controller指定该线程获取userInfoTo这一数据

3.在拦截器内标识线程：

      /**
       * 将该拦截器表示为cartThreadLocal
       */
      public static ThreadLocal<UserInfoTo> cartThreadLocal=new ThreadLocal<>();

4.同时还要在重写的preHandler方法内加上userInfoTo：

      cartThreadLocal.set(userInfoTo);

5.接口内，获取数据：

      UserInfoTo userInfoTo= CartInterceptor.cartThreadLocal.get();

6.接下来让拦截器工作，光给个@Component是不够的，还要添加到mvc：

      @Configuration
      public class CartWebConfiguration implements WebMvcConfigurer {
          @Override
          public void addInterceptors(InterceptorRegistry registry) {
              registry
                .addInterceptor(new CartInterceptor())      //添加cart的拦截器
                .addPathPatterns("/**")                     //拦截url为/**，即所有url
                ;
          }
      }



7.整个接口：

      /**
       * @return
       *
       * 根据session判断是否登录，
       * 未登录则显示临时cart
       * 登录则显示用户cart
       * 若登陆时临时cart非空还要将临时cart合并到用户cart
       */
      @GetMapping("/cart.html")
      public String getCart(){
          UserInfoTo userInfoTo= CartInterceptor.cartThreadLocal.get();
          System.out.println("controller中："+userInfoTo);
          //返回的cartList页面
          //return "success";
          return "cartList";
      }

随后重启，测试：

      http://cart.katzenyasax-mall.com/cart.html

结果是，可以打印出userInfoTo，不为空







改造一下，若session和cookie中都无数据时，新建一个临时用户
则将session和cookie改成并列关系：

      if(thisSession!= null){
          //若session中有名为loginUser的cookie，表示用户已登录
          MemberTo to= JSON.parseObject(JSON.toJSONString(thisSession),MemberTo.class);
          userInfoTo.setUserId(to.getId());
          System.out.println("session中："+userInfoTo);
      }
      //未登录，则判断cookie中是否有名为user-key的cookie，表示是否已存在临时用户，没有则应当创建
      if(request.getCookies()!=null && request.getCookies().length>0) {
          for (Cookie cookie : request.getCookies()) {
              if (cookie.getName().equals(CartConstant.TEMPLE_USER)) {
                  //有名为user-key的cookie
                  userInfoTo.setUserKey(cookie.getValue());
                  System.out.println("cookie中"+userInfoTo);
                  break;
              }
          }
      }

并且在无cookie时，新增一个临时用户：

      if(userInfoTo.getUserKey()==null){
          userInfoTo.setUserKey(UUID.randomUUID().toString());
      }



随后要让controller执行完后，将该cookie存入浏览器，时间为一个月
定义一个postHandle过滤器，在controller之后执行：

      @Override
      public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)   throws Exception {
          UserInfoTo userInfoTo = cartThreadLocal.get();
          Cookie cookie = new Cookie(CartConstant.TEMPLE_USER, userInfoTo.getUserKey());
          cookie.setDomain("katzenyasax-mall.com");   //作用域
          cookie.setMaxAge(60*60*24*30);              //过期时间一个月
          response.addCookie(cookie);
      }

此时再次测试，里面就有user-key的cookie了






再测试一下是否为临时用户，to里加一个成员变量：

      @ToString
      @Data
      public class UserInfoTo {
          private Long userId;
          private String userKey;
          private Boolean tempUser = false;
      }

默认为false，在过滤器进行判定到为临时用户时，更改为成员变量：

      userInfoTo.setTempUser(true);

随后修改postHandle方法，只有templeUser为false时，也即是此时本地还未有临时用户时，添加一个cookie：

      @Override
      public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
          UserInfoTo userInfoTo = cartThreadLocal.get();
          if(userInfoTo.getTempUser()!=true){
              //即此时无临时用户
              Cookie cookie = new Cookie(CartConstant.TEMPLE_USER, userInfoTo.getUserKey());
              cookie.setDomain("katzenyasax-mall.com");   //作用域
              cookie.setMaxAge(60 * 60 * 24 * 30);              //过期时间一个月
              response.addCookie(cookie);
          }
          //清空threadLocal，防止内存泄露
          cartThreadLocal.remove();
      }

此时就不会造成每次访问临时cart都会给临时用户续期了








## 添加商品到购物车
p241

查看购物车暂时还不可用，先来整添加商品
url：

      http://cart.katzenyasax-mall.com/addCartItem?skuId=71&num=1

1.首先明确，用户登录与未登录的标识符，登录的用户在Katzenyasax-mall::cart::后面就接上其id，例如：

      Katzenyasax-mall::cart::1

未登录的用户就在后面接上userKey，例如：

      Katzenyasax-mall::cart::

这俩就作为redis-key吧

2.方法中需要用到product的表，所以远程调用product模块，定义一个feign：

      @FeignClient("mall-product")
      public interface ProductFeign {
          @RequestMapping("product/skuinfo/info/{skuId}")
          public R info(@PathVariable("skuId") Long skuId);
      }

cart的启动类加上：

      @EnableFeignClients(basepackges = "com.katzenyasax.mall.cart.feign")

因为product模块的controller里有现成的方法，所以直接用就行了
但是获取sku的attr，也需要一个feign内方法：

      @GetMapping("product/skusaleattrvalue/skuAttrs/{skuId}")
      List<String> getSkuAttrs(@PathVariable("skuId") Long skuId);

3.其中product内接口：

      /**
       * 
       * 
       * @param skuId
       * @return
       * 
       * 被cart模块远程调用的方法
       * 根据skuId获取所有的attrs
       * 
       */
      @GetMapping("/skuAttrs")
      public List<String> getSkuAttrs(@RequestParam long skuId){
          return skuSaleAttrValueService.getSkuAttrs(skuId);
      }

4.方法getSkuAttrs：

      /**
      *
      * @param skuId
      * @return
      *
      * 根据skuId获取sku的销售属性attrs
      */
      @Override
      public List<String> getSkuAttrs(long skuId) {
          return baseMapper.selectList(
                  new QueryWrapper<SkuSaleAttrValueEntity>()
                          .eq("sku_id",skuId))
                  .stream()
                  .map(thisEntity-> 
                          thisEntity.getAttrName()+":"+thisEntity.getAttrValue()
                  ).collect(Collectors.toList()
                  );
      }

将skuId对应的attrName和attrValue进行拼接返回




5.除了远程调用product外，还需要根据redis-key为key获取数据，并且使用hash存储



6.使用异步的方式执行，所以将product里面ThisThreadPool和ThisThreadPoolConfiguration复制到cart模块中
并且在application中配置：

      mall:
        thread:
          core-size:  20
          max-size: 200
          keep-alive-time:  10

并且将ThisThreadPool配置到方法中：

      @Autowired
      ThisThreadPool threadPool;

7.接口：

      /**
       *
       * @return
       *
       * 根据vo提供的信息添加商品
       */
      @GetMapping("/addCartItem")
      public String addCart(@RequestParam Long skuId,@RequestParam Long num, HttpSession session, HttpServletRequest request, Model model){
          Object thisSession = session.getAttribute(AuthConstant.USER_LOGIN);
          System.out.println(thisSession);
          String thisKey;
          if (thisSession!=null){
              thisKey=JSON.parseObject(JSON.toJSONString(thisSession), MemberTO.class).getId().toString();
          }else {
              thisKey = Arrays.stream(request.getCookies()).filter(
                              thisCookie -> thisCookie.getName().equals(CartConstant.TEMPLE_USER)
                      ).collect(Collectors.toList()).get(0)
                      .getValue();
          }
          CartItemVO thisItem=cartService.addCartItem(skuId,num,thisKey);
          model.addAttribute("cartItem",thisItem);
          return "success";
      }

方法addCartItem：

      /**
       *
       * @param thisKey
       * @return
       *
       * 添加商品到cart
       * cart在redis的名称为：
       *
       *          katzenyasax-mall::cart::<thisKey>
       *
       *  需要用到redis
       *
       * 不过存到CartItemVO中，需要用到sku表中的数据，所以还需要远程调用product模块
       * 不仅要获取sku的实体类，还要根据skuId获取attrs
       * 之后还需要
       *
       */
      @Override
      public CartItemVO addCartItem(Long skuId,Long num, String thisKey) {
          //1.获取商品信息
          R info = productFeign.info(skuId);
          SkuInfoTO skuInfo = JSON.parseObject(
                  JSON.toJSONString(
                          info.get("skuInfo")
                  ), SkuInfoTO.class
          );

          //2.封装CartItemVo
          CartItemVO item=new CartItemVO();
          item.setSkuId(skuId.toString());
          item.setCheck(false);   //默认false，未选中
          item.setTitle(skuInfo.getSkuTitle());
          item.setImage(skuInfo.getSkuDefaultImg());
          item.setSkuAttr(productFeign.getSkuAttrs(skuId));

          item.setPrice(skuInfo.getPrice());
          item.setCount(num);
          item.setTotalPrice(
                  skuInfo.getPrice()
                          .multiply(BigDecimal
                                  .valueOf(num)
                          )
          );
          //todo 远程调用coupon模块获取减免
          item.setReduce(BigDecimal.ZERO);
          System.out.println(JSON.toJSONString(item));

          //3.从redis中获取数据
          String redis_key=CartConstant.CART_USER_PREFIX+thisKey;
          BoundHashOperations<String,Object,Object> ops = redisTemplate.boundHashOps(redis_key);

          //4.增加商品数量存入，或直接存入
          if(ops.get(skuId.toString())!=null) {
              CartItemVO oldItem = JSON.parseObject(
                      ops.get(skuId.toString()).toString()        //redis中存的就是json了，不需要再json化，只需要toString就行了
                      ,CartItemVO.class
              );
              //新的item
              CartItemVO newItem=oldItem;
              newItem.setCount(oldItem.getCount()+num);
              newItem.setTotalPrice(
                      newItem.getPrice().multiply(
                              BigDecimal.valueOf(newItem.getCount())
                      )
              );
              ops.put(skuId.toString(),JSON.toJSONString(newItem));
              return newItem;
          }else {
              //直接存item
              ops.put(skuId.toString(), JSON.toJSONString(item));
              return item;
          }
      }


测试一下，不管是登录状态还是非登录状态，都可以正常添加。
并且在添加成功页面也可以看到目前购物车中该商品的数量



## 添加购物车后重定向问题
p243

现在的问题是，在添加成功页面刷新时，相当于再次向服务器发送了addCartItem请求
解决方案是，请求结束后重定向到另一个展示页面，该展示页面对应的接口会接收重定向时发送的skuId，再在redis中查询一下，从而获取item，进行展示

接口后部分改造为：

      redirectAttributes.addAttribute("skuId",skuId);
      return "redirect:http://cart.katzenyasax-mall.com/addCartItemSuccess";

注意把Model改成RedirectAttributes
重定向的接口：

      /**
       *
       *
       * @param skuId
       * @param model
       * @return
       *
       * 重定向的方法，重定向到专门的展示页面
       *
       */
      @RequestMapping("/addCartItemSuccess")
      public String addCartItemSuccess(@RequestParam("skuId") Long skuId,Model model){
          UserInfoTO userInfoTo= CartInterceptor.cartThreadLocal.get();
          CartItemVO item=new CartItemVO();
          if(userInfoTo.getUserId()!=null) {
              item=cartService.getCartItem(skuId,userInfoTo.getUserId().toString());
          } else {
              item=cartService.getCartItem(skuId,userInfoTo.getUserKey());
          }

          model.addAttribute("cartItem",item);
          return "success";
      }

方法getCartItem：

      /**
       *
       * @param skuId
       * @param thisKey
       * @return
       *
       * 重定向的页面，从redis中查询该商品
       */
      @Override
      public CartItemVO getCartItem(Long skuId,String thisKey) {
          //从redis中获取数据
          String redis_key=CartConstant.CART_USER_PREFIX+thisKey;
          BoundHashOperations<String,Object,Object> ops = redisTemplate.boundHashOps(redis_key);
          if(ops.get(skuId.toString())!=null){
              CartItemVO item = JSON.parseObject(
                      ops.get(skuId.toString()).toString()        //redis中存的就是json了，不需要再json化，只需要toString就行了
                      ,CartItemVO.class
              );
              return item;
          }
          return null;
      }







## 显示购物车
p244

url：

      http://cart.katzenyasax-mall.com/cart.html

其中用户信息已经通过过滤器自动获取
接口：

      /**
      * @return
      *
      * 根据session判断是否登录，
      * 未登录则显示临时cart
      * 登录则显示用户cart
      * 若登陆时临时cart非空还要将临时cart合并到用户cart
      */
      @GetMapping("/cart.html")
      public String getCart(Model model){
          UserInfoTO userInfoTo= CartInterceptor.cartThreadLocal.get();
          System.out.println("controller中："+userInfoTo);
          //返回的cartList页面
          //return "success";
          Cart thisCart=new Cart();
          if(userInfoTo.getUserId()!=null) {
              thisCart = cartService.getCart(userInfoTo.getUserId().toString());
          } else {
              thisCart = cartService.getCart(userInfoTo.getUserKey());
          }

          model.addAttribute("cart",thisCart);
          return "cartList";
      }

方法：

      /**
       *
       * @return
       *
       * 获取temple cart
       * 同样从redis获取
       *
       */
      @Override
      public Cart getCart(String thisKey) {
          Cart cart=new Cart();
          cart.setItems(new ArrayList<>());
          cart.setCountType(0l);
          cart.setCountNum(0l);
          cart.setTotalAmount(BigDecimal.ZERO);
          cart.setReduce(BigDecimal.ZERO);

          //获取数据key的集合
          String redis_key=CartConstant.CART_USER_PREFIX+thisKey;
          BoundHashOperations<String,Object,Object> ops = redisTemplate.boundHashOps(redis_key);

          //遍历集合
          Set<Object> keys = ops.keys();
          keys.forEach(key->{
              CartItemVO item = JSON.parseObject(
                      ops.get(key).toString()
                      , CartItemVO.class
              );
              cart.getItems().add(item);
              cart.setCountType(cart.getCountType()+1);
              cart.setCountNum(cart.getCountNum()+ item.getCount());
              cart.setTotalAmount(cart.getTotalAmount()
                      .add(item.getTotalPrice())
              );
          });

          System.out.println("getTempleCart: "+cart);
          return cart;
      }

结果是可以查询到





## 购物车合并问题
p244

登录后，若检测到临时购物车有商品，应当自动合并到当前登录用户的购物车内，就拿getCart方法开刀
若判定到用户登录，不管用户cart有无存入redis，都要读临时cart并存入redis

      String thisKey= userInfoTO.getUserKey();;
      if(userInfoTO.getUserId()!=null){
          //用户已登录
          //就行了在另一个方法中合并临时cart到用户cart
          this.writeInUserCart(userInfoTO);
          thisKey=userInfoTO.getUserId().toString();
      }

在获取cart之前，判断是否登录，若登录则执行方法writeInUserCart：

      /**
       *
       * @param userInfoTO
       *
       * 将临时cart的数据写入用户cart中
       */
      private void writeInUserCart(UserInfoTO userInfoTO) {
          //分别获取临时和用户cart的操作柄
          String keyUser=CartConstant.CART_USER_PREFIX+userInfoTO.getUserId();
          BoundHashOperations<String,Object,Object> opsUser = redisTemplate.boundHashOps(keyUser);
          String keyTemple=CartConstant.CART_USER_PREFIX+userInfoTO.getUserKey();
          BoundHashOperations<String,Object,Object> opsTemple = redisTemplate.boundHashOps(keyTemple);
          //遍历临时cart中的数据，写入用户cart并删除临时cart的数据
          Set<Object> templeKeys = opsTemple.keys();
          if(templeKeys.size()>0){
              templeKeys.forEach(key->{
                  CartItemVO item = JSON.parseObject(
                          opsTemple.get(key).toString()
                          , CartItemVO.class
                  );
                  this.addCartItem(Long.parseLong(
                          item.getSkuId())
                          ,item.getCount()
                          ,userInfoTO.getUserId().toString()
                  );
                  //删除临时cart中的数据
                  opsTemple.delete(key);
              });
          }
      }

测试，删除redis中所有cart
先未登录状态下添加商品，登录后再查看购物车：
结果是可以




## 选中商品
p245

也即是改变CartItemVO的check
请求路径：

      http://cart.katzenyasax-mall.com/checkItem?skuId=2&checked=1

checked=1表示选中，checked=0表示不选
接口：

      /**
      * 
      * @param skuId
      * @param checked
      * @return
      * 
      * 选中商品/不选商品
      */
      @RequestMapping("/checkItem")
      public String checkItem(@RequestParam("skuId") Long skuId,@RequestParam("checked") Long checked){
          UserInfoTO userInfoTo= CartInterceptor.cartThreadLocal.get();
          String thisKey;
          if(userInfoTo.getUserId()!=null){
              thisKey=userInfoTo.getUserId().toString();
          } else{
              thisKey=userInfoTo.getUserKey();
          }
          cartService.check(skuId,thisKey,checked>0);
          //重定向到cart界面
          return "redirect:http://cart.katzenyasax-mall.com/cart.html";
      }

最后重定向回cart界面
方法：

      /**
       *
       * @param skuId
       * @param thisKey
       * @param check
       *
       * 选中商品
       * 修改cart中对应skuId的cartItemVO的check属性
       *
       * 需要从redis中查询数据
       */
      @Override
      public void check(Long skuId, String thisKey, boolean check) {
          CartItemVO thisItem=this.getCartItem(skuId,thisKey);
          thisItem.setCheck(check);
          BoundHashOperations ops = redisTemplate.boundHashOps(CartConstant.CART_USER_PREFIX + thisKey);
          ops.delete(skuId.toString());
          ops.put(skuId.toString(),JSON.toJSONString(thisItem));
      }

结果是可以选中与不选








## 修改商品数量
p246

请求路径：

      http://cart.katzenyasax-mall.com/countItem?skuId=6&num=7

接口：

      /**
       *
       * @param skuId
       * @param num
       * @return
       *
       * 选中商品/不选商品
       */
      @RequestMapping("/countItem")
      public String countItem(@RequestParam("skuId") Long skuId,@RequestParam("num") Long num){
          UserInfoTO userInfoTo= CartInterceptor.cartThreadLocal.get();
          String thisKey;
          if(userInfoTo.getUserId()!=null){
              thisKey=userInfoTo.getUserId().toString();
          } else{
              thisKey=userInfoTo.getUserKey();
          }
          cartService.count(skuId,thisKey,num);
          //重定向到cart界面
          return "redirect:http://cart.katzenyasax-mall.com/cart.html";
      }

方法count：

      /**
       *
       * @param skuId
       * @param thisKey
       * @param num
       *
       * 修改商品数量
       */
      @Override
      public void count(Long skuId, String thisKey, Long num) {
          CartItemVO thisItem=this.getCartItem(skuId,thisKey);
          thisItem.setCount(num);
          thisItem.setTotalPrice(thisItem.getPrice().multiply(BigDecimal.valueOf(num)));
          BoundHashOperations ops = redisTemplate.boundHashOps(CartConstant.CART_USER_PREFIX + thisKey);
          ops.delete(skuId.toString());
          ops.put(skuId.toString(),JSON.toJSONString(thisItem));
      }









## 删除商品
p247

请求路径：

      http://cart.katzenyasax-mall.com/deleteItem?skuId=6

接口：

      /**
       *
       * @param skuId
       * @return
       *
       * 选中商品/不选商品
       */
      @RequestMapping("/deleteItem")
      public String deleteItem(@RequestParam("skuId") Long skuId){
          UserInfoTO userInfoTo= CartInterceptor.cartThreadLocal.get();
          String thisKey;
          if(userInfoTo.getUserId()!=null){
              thisKey=userInfoTo.getUserId().toString();
          } else{
              thisKey=userInfoTo.getUserKey();
          }
          cartService.deleteCartItem(skuId,thisKey);
          //重定向到cart界面
          return "redirect:http://cart.katzenyasax-mall.com/cart.html";
      }

方法deleteCartItem：

      /**
       *
       * @param skuId
       * @param thisKey
       *
       * 从cart中删除对应skuId的商品
       */
      public void deleteCartItem(Long skuId,String thisKey){
          BoundHashOperations ops = redisTemplate.boundHashOps(CartConstant.CART_USER_PREFIX + thisKey);
          ops.delete(skuId.toString);
      }









# 商城业务：消息队列


## RabbitMQ
p248

可以在保证最终一致性的前提下，处理高并发请求，也就是将高并发的请求处理成一条有序队列，服务接口回按照顺序依次执行

工作流程：

      1.生产者（Publisher）生产消息，通过一条长连接将消息送到消息代理（Broker）

      2.消息由消息头和消息体组成，其中消息体中包含了一个路由键（route-key），决定着该消息会进入哪一个队列

      3.消息代理先接收消息，再将将消息送到交换机

      4.交换机通过消息的消息头包含的key-route，将消息送入对应的队列

      5.消费者（Consumer）通过一条长连接与消息代理连接

      6.消费者中的不同服务通过不同的信道，监听并接收不同队列的消息




<!--                                    ==============Broker================
*     =========        message          |                                  |
*     Productor   =======================> Reciver                         |
*     =========     long connection     |     |                            |
*                                       |     |==>  ExchangerI             |              
*                                       |     |         |                  |            ====Comsumer====
*                                       |     |         |                  | channel    |              |
*                                       |     |         |==>  queueI   ==================> serviceI    |              
*                                       |     |         |                  |            |              |
*                                       |     |         |==>  queueII  ==================> serviceII   |                
*                                       |     |         |                  |            |              |
*                                       |     |         |==>  queueIII ==================> serviceIII  |                
*                                       |     |         |                  |            |              |
*                                       |     |         ······                          ================
*                                       |     |                            |
*                                       |     |                            |
*                                       |     |==>  ExchangerII            |
*                                       |               |                  |
*                                       |               |=>   queueI    ······
*                                       |               |                  |
*                                       |             ······               |
*                                       |                                  |              
*                                       ====================================
*
-->






## docker安装
p251

启动rabbitmq:management

      docker run -d --name rabbitmq -p 5671:5671 -p 5672:5672 -p 4369:4369 -p 25672:25672 -p 15671:15671 -p 15672:15672 rabbitmq:management

会自动联网安装
记得开启自启动

      docker update $(docker ps -a -q) --restart=always

访问：

      http://192.168.74.130:15672/

可进入到rabbitmq的管理页面，默认用户名和密码都是guest





## 交换机Exchanger
p251

四种交换机：

      1.director：点对点模式，使用route-key绑定队列，并且route-key精确匹配

      2.headers：点对点模式，属于JMS的一种，已淘汰

      3.fanout：广播模式，交换机中所有队列都会收到

      4.topic：发布订阅模式，同样是通过route-key匹配，但是允许使用通配符部分匹配，匹配到的队列即是订阅了生产者的队列


director和fanout没什么好说的，topic需要注意，该种交换机绑定队列时同样需要声明route-key，例如：

      #.aaa       （队列1）
      bbb.#       （队列2）
      #.ccc.bbb   （队列3）

其中通配符#表示该处可以是任何字符，但是必须要有，而且只能有一个（有几个取决于有几个.）








## SpringBoot整合RabbitMQ
p255


拿order模块下刀，

1.引入依赖：

      <!-- rabbitMQ整合依赖 -->
      <!-- https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-amqp -->
      <dependency>
          <groupId>org.springframework.boot</groupId>
          <artifactId>spring-boot-starter-amqp</artifactId>
       </dependency>

只要引入依赖后，RabbitAutoConfiguration会自动生效

2.application中配置ip、端口和虚拟主机：

      spring:
          rabbitmq:
            addresses: 192.168.74.130
            port: 5672
            virtual-host: /

3.启动类中加注解，开启rabbit功能：

      @EnableRabbit

整合完毕




## 声明mq组件
p256

基于AmqpAdmin进行：

      @Autowired
      AmqpAdmin amqpAdmin;

测试方法：

      @Test
      void rabbitTest01(){
          DirectExchange directExchange=new DirectExchange(
                  "spring.test01.directExchange"      //名称
                  ,true                       //是否持久化
                  ,false          //是否自动删除
          );
          amqpAdmin.declareExchange(directExchange);
      }

先创建一个交换机，名称为spring.test01.directExchange，持久化且不自动删除
amqpAdmin的declareXXX方法，用于声明、也即是创建

测试一下，确实出现了名为spring.test01.directExchange的交换机




声明一个队列：

      @Test
      void createQueue(){
          Queue queue=new Queue(
                  "spring.test01.queue01"
                  ,true                       //持久化
                  ,false              //排他性
                  ,false          //自动删除
          );
          //声明一个队列
          amqpAdmin.declareQueue(queue);
      }


确实有了一个




声明一个绑定关系：

      @Test
      void createBinding(){
          Binding binding=new Binding(
                  /**
                   * String destination,          目的地
                   * DestinationType destinationType,     目的地类型
                   * String exchange,         交换机
                   * String routingKey,       路由键
                   * @Nullable Map<String, Object> arguments          自定义参数
                   */

                  "spring.test01.queue01"
                  , Binding.DestinationType.QUEUE
                  ,"spring.test01.directExchange"
                  ,"testRK"
                  ,null
          );
          //声明一个绑定关系
          amqpAdmin.declareBinding(binding);
      }

确实有了一个绑定关系

注意目的地和目的地类型就是非交换机那一方，或是其中一个交换机，因为第三个参数表示绑定的另一方是交换机，而一个绑定必须要有一个交换机





## 发送消息
p256

发送一条消息：

      @Test
      void rabbitMessageSending(){
          //创建并发送消息
          rabbitTemplate.convertAndSend(
                  "spring.test01.directExchange"
                  ,"testRK"
                  ,"ttttest");
      }   

参数先后是：交换机、路由键、消息

运行之后确实有了一个
注意消息是object类，用其他bean都可以，但是对象想要发送，必须实现一个序列化接口
例如要发送一个OrderEntity类对象，该类就必须实现：

      public class OrderEntity implements Serializable {}

实现一个Serializable接口，才能通过默认序列化输出

比如直接发送一个OrderEntity对象：

      @Test
      void rabbitMessageSending(){
          OrderEntity message=new OrderEntity();
          message.setMemberId(1l);
          message.setMemberUsername("hdow");

          //发送消息
          rabbitTemplate.convertAndSend(
                  "spring.test01.directExchange"
                  ,"testRK"
                  ,message);
      }

发送成功后，可以看到队列种有一条消息：

      rO0ABXNyAC1jb20ua2F0emVueWFzYXgubWFsbC5vcmRlci5lbnRpdHkuT3JkZXJFbnRpdHkAAAAAAAAAAQIAKkwADmF1dG9Db25maXJtRGF5dAATTGphdmEv
      bGFuZy9JbnRlZ2VyO0wAC2JpbGxDb250ZW50dAASTGphdmEvbGFuZy9TdHJpbmc7TAAKYmlsbEhlYWRlcnEAfgACTAARYmlsbFJlY2VpdmVyRW1haWxxAH4A
      AkwAEWJpbGxSZWNlaXZlclBob25lcQB+AAJMAAhiaWxsVHlwZXEAfgABTAALY29tbWVudFRpbWV0ABBMamF2YS91dGlsL0RhdGU7TAANY29uZmlybVN0YXR1
      c3EAfgABTAAMY291cG9uQW1vdW50dAAWTGphdmEvbWF0aC9CaWdEZWNpbWFsO0wACGNvdXBvbklkdAAQTGphdmEvbGFuZy9Mb25nO0wACmNyZWF0ZVRpbWVx
      AH4AA0wADGRlbGV0ZVN0YXR1c3EAfgABTAAPZGVsaXZlcnlDb21wYW55cQB+AAJMAApkZWxpdmVyeVNucQB+AAJMAAxkZWxpdmVyeVRpbWVxAH4AA0wADmRp
      c2NvdW50QW1vdW50cQB+AARMAA1mcmVpZ2h0QW1vdW50cQB+AARMAAZncm93dGhxAH4AAUwAAmlkcQB+AAVMAAtpbnRlZ3JhdGlvbnEAfgABTAARaW50ZWdy
      YXRpb25BbW91bnRxAH4ABEwACG1lbWJlcklkcQB+AAVMAA5tZW1iZXJVc2VybmFtZXEAfgACTAAKbW9kaWZ5VGltZXEAfgADTAAEbm90ZXEAfgACTAAHb3Jk
      ZXJTbnEAfgACTAAJcGF5QW1vdW50cQB+AARMAAdwYXlUeXBlcQB+AAFMAAtwYXltZW50VGltZXEAfgADTAAPcHJvbW90aW9uQW1vdW50cQB+AARMAAtyZWNl
      aXZlVGltZXEAfgADTAAMcmVjZWl2ZXJDaXR5cQB+AAJMABVyZWNlaXZlckRldGFpbEFkZHJlc3NxAH4AAkwADHJlY2VpdmVyTmFtZXEAfgACTAANcmVjZWl2
      ZXJQaG9uZXEAfgACTAAQcmVjZWl2ZXJQb3N0Q29kZXEAfgACTAAQcmVjZWl2ZXJQcm92aW5jZXEAfgACTAAOcmVjZWl2ZXJSZWdpb25xAH4AAkwACnNvdXJj
      ZVR5cGVxAH4AAUwABnN0YXR1c3EAfgABTAALdG90YWxBbW91bnRxAH4ABEwADnVzZUludGVncmF0aW9ucQB+AAF4cHBwcHBwcHBwcHBwcHBwcHBwcHBwcHNy
      AA5qYXZhLmxhbmcuTG9uZzuL5JDMjyPfAgABSgAFdmFsdWV4cgAQamF2YS5sYW5nLk51bWJlcoaslR0LlOCLAgAAeHAAAAAAAAAAAXQABGhkb3dwcHBwcHBw
      cHBwcHBwcHBwcHBw

序列化方式是：

      content_type:	application/x-java-serialized-object



但是如果我想用json输出消息呢？那就把消息转换器改成json的
写一个配置类RabbitSerializeConfiguration：

      @Configuration
      public class RabbitSerializeConfiguration {
          @Bean
          public MessageConverter messageConverter(){
              return new Jackson2JsonMessageConverter();
          }
      }

相当于重写了rabbit默认的消息转换器，
默认的消息转换器的逻辑是，如果是String类型消息就直接输出原本的消息；如果不是就参照java默认的序列化机制
而我们在配置类中加入了同名的组件MessgaeConverter，系统会优先使用该组件，这一举动会使rabbit在处理非String类型消息时参照json序列化

注意两个MessageConverter都是在amqp包下的才有效果

再次测试，消息是：

      {"id":null,"memberId":1,"orderSn":null,"couponId":null,"createTime":null,"memberUsername":"hdow","totalAmount":null,"payAmount":null,"freightAmount":null,"promotionAmount":null,"integrationAmount":null,"couponAmount":null,"discountAmount":null,"payType":null,"sourceType":null,"status":null,"deliveryCompany":null,"deliverySn":null,"autoConfirmDay":null,"integration":null,"growth":null,"billType":null,"billHeader":null,"billContent":null,"billReceiverPhone":null,"billReceiverEmail":null,"receiverName":null,"receiverPhone":null,"receiverPostCode":null,"receiverProvince":null,"receiverCity":null,"receiverRegion":null,"receiverDetailAddress":null,"note":null,"confirmStatus":null,"deleteStatus":null,"useIntegration":null,"paymentTime":null,"deliveryTime":null,"receiveTime":null,"commentTime":null,"modifyTime":null}

编码方式变成了：

      content_type:	application/json

变成了json输出结果了





## 接收消息
p258

在OrderService里写一个监听方法，打上@RabbitLIstener注解：

      /**
       * 监听消息队列，获取消息
       */
      @RabbitListener(queues = {"spring.test01.queue01"})
      public void listenOrderMessage(Message message){
          System.out.println("监听到消息："+message.toString()+"，类型为："+message.getClass());
      }

注意一下，该方法注解只有在容器内才能生效，也就是在各种组件里面@Service、@Controller、@Component等才行
@RabbitListener可以监听多个队列，放大括号里面就可以了，这里只监听一个

测试一下，开启Order服务，如果队列里出现了一个消息，使用上面的test发送一个OrderEntity，那么会打印：

      监听到消息：(Body:'[B@1f06c07c(byte[820])' MessageProperties [headers={__TypeId__=com.katzenyasax.mall.order.entity.OrderEntity}, contentType=application/json, contentEncoding=UTF-8, contentLength=0, receivedDeliveryMode=PERSISTENT, priority=0, redelivered=false, receivedExchange=spring.test01.directExchange, receivedRoutingKey=testRK, deliveryTag=1, consumerTag=amq.ctag-zOPjexiGSPI21zDUGAr-mA, consumerQueue=spring.test01.queue01])，类型为：class org.springframework.amqp.core.Message


接收消息时也可以直接把对象类型放上去：

      public void listenOrderMessage(Message message,OrderEntity body){
        System.out.println("监听到消息："+body+"，类型为："+message.getClass());
      }

结果：

      监听到消息：OrderEntity(id=null, memberId=1, orderSn=null, couponId=null, createTime=null, memberUsername=hdow, totalAmount=null, payAmount=null, freightAmount=null, promotionAmount=null, integrationAmount=null, couponAmount=null, discountAmount=null, payType=null, sourceType=null, status=null, deliveryCompany=null, deliverySn=null, autoConfirmDay=null, integration=null, growth=null, billType=null, billHeader=null, billContent=null, billReceiverPhone=null, billReceiverEmail=null, receiverName=null, receiverPhone=null, receiverPostCode=null, receiverProvince=null, receiverCity=null, receiverRegion=null, receiverDetailAddress=null, note=null, confirmStatus=null, deleteStatus=null, useIntegration=null, paymentTime=null, deliveryTime=null, receiveTime=null, commentTime=null, modifyTime=null)，类型为：class org.springframework.amqp.core.Message

可以看到确实接收到了


甚至可以获取信道：

      public void listenOrderMessage(Message message, OrderEntity body, Channel channel)

有什么用我就不知道了



但是需要注意的是@RabbitListener只能支持同一个消息被仅仅一个服务监听到，
但是碰到很多条消息时，消息可以被负载均衡到不同的服务

要是消息队列里消息太多，比监听并接收该队列的服务器数量还多怎么办呢？那就只有在一台服务器处理完一个消息时，才能接收下一个消息，未被接收的消息就会阻塞在消息代理中准备被处理






除此之外，还有一个注解是@RabbitHandler，它可以和@RabbitListener注解搭配使用，就可以区分队列消息的类型
将@RabbitListener放在类上，@RabbitHandler放在方法上，例如拿OrderService开刀：

      @RabbitListener(queues = {"spring.test01.queue01"})
      @Service("orderService")
      public class OrderServiceImpl extends ServiceImpl<OrderDao, OrderEntity> implements OrderService {
          /**
          * 监听OrderEntity对象消息
          */
          @RabbitHandler
          //@RabbitListener(queues = {"spring.test01.queue01"})
          public void listenOrderMessage(Message message, OrderEntity body, Channel channel){
              System.out.println("接收到OrderEntity对象消息："+body);
          }

          /**
          * 接收String对象消息
          */
          @RabbitHandler
          public void listenOrderMessage(String message){
              System.out.println("接受到String对象消息："+message);
          }
      }

我们先后在test里发送两个消息，测试方法如下：

      @Test
      void rabbitMessageSending(){
          OrderEntity message=new OrderEntity();
          message.setMemberId(1l);
          message.setMemberUsername("hdow");

          //发送消息
          rabbitTemplate.convertAndSend(
                  "spring.test01.directExchange"
                  ,"testRK"
                  ,message);
      }
      @Test
      void StringMessageSending(){
          //发送String消息
          rabbitTemplate.convertAndSend(
                  "spring.test01.directExchange"
                  ,"testRK"
                  ,"ttttest");
      }

结果为：

      接受到String对象消息：ttttest
      接收到OrderEntity对象消息：OrderEntity(id=null, memberId=······

可以看到，可以通过配合使用@RabbitListener和@RabbitHandler，进行方法重载监听不同类型的消息






## 可靠投递


防止消息在发送、接收过程中因为一系列原因导致的的消息丢失
一种解决方法是使用事务，但是据rabbitMQ官方说法，这可能会使性能下降250倍，因此不推荐使用

另一种解决方案是使用确认机制，具体工作流程如下：

      1.Publisher发送消息到Broker，若Broker收到了消息则调用一个方法confirmCallback进行确认

      2.Broker通过Exchange将消息发往Queue，若Queue未收到消息，则调用一个方法returnCallback表示未收到消息

      3.Consumer开始接收Queue的消息时，采用的是ack机制，若Consumer正确地消费到了消息则将消息从Queue中删除，否则退回到Queue或重新投递等

前两步都是发送端，后一步是接收端的


### 生产者到消息代理确认
p259

1.配置application以开启功能
注意视频里说的publisher-confirm已经弃用，现版本使用的是：

      spring:
        rabbitmq:
          publisher-confirm-type: correlated

2.随后要将redisTemplate的ComfirmCallback自定义，在重新写一个配置类，执行的方法：

      @Configuration
      public class RabbitConfirmConfiguration {
          @Autowired
          RabbitTemplate rabbitTemplate;
          @PostConstruct
          public void setRabbitTemplate(){
              rabbitTemplate.setConfirmCallback(
                      (correlationData, b, s) -> System.out.println("Data: "+correlationData+" , 是否接收到: "+b+" 原因: "+s)
              );
          }
      }

该方法的作用是将自动配置的rabbitTemplate中的setConfirmCallback方法重写，以便执行我们自定义的操作
而@PostConstruct就是字面意思，在该配置类实例化之后执行该方法


注意不要在RabbitSerializeConfiguration中写这个方法，会循环依赖
原因就是RabbitTemplate依赖于该配置中的自定义序列化方法，而该配置类有引入了RabbitTemplate的单例实例化对象，二者就造成了循环依赖


3.测试一下，还是使用测试类中发送消息的方法
测试之后发现是执行发送命令的客户端打印了语句，也就是@Test自动生成的一个客户端：

      Data: null , 是否接收到: true 原因: null

注意这里的ack参数，只要消息从Publisher传到Broker，都是true，他不会参考后续进展。事实上，ack的作用就是用来表示消息是否被删除，在这里就可以看作是，Publisher将消息复制了一份交给Broker，Broker收到后就给一个反馈ConfirmCallback告诉Publisher有没有收到消息，如果收到了，ack就为true，消息就会从Publisher删除；没有收到ack就为false，该消息可能会重发，也可能会直接丢弃，依照具体配置执行。

注意，该过程可以看作是整个rabbitMQ中，两点间消息传递确认机制的模板，区别在于任何处理发送失败的消息。





### 消息代理到队列确认
p259


1.配置application：

      spring:
        rabbitmq:
          publisher-returns: true
          template:
            mandatory: true

publisher-returns表示开启消息代理到队列的确认
template.mandatory表示已异步（新开一个线程执行）方式优先回调return

2.在setRabbitConfirm方法中加上一段：

        /**
         * 2.broker到queue的确认
         */
        rabbitTemplate.setReturnsCallback(returnedMessage ->
            System.out.println(
                    "发送失败的消息内容: "+returnedMessage.getMessage()
                    +" 回复的状态码: "+ returnedMessage.getReplyCode()
                    +" 回复的文本内容: "+ returnedMessage.getReplyText()
                    +" 发送该消息的交换机: "+returnedMessage.getExchange()
                    +" 路由键: "+returnedMessage.getRoutingKey()
            )
        );

在旧版本中，这些参数都是单一的，新版本中封装了


3.测试，为了认为造成发送失败，将测试方法的路由键暂时修改，随后测试
同样是在发送消息的客户端中，但是没有在test中特别展示，要在完整终端查看：

      发送失败的消息内容: (Body:'"ttttest"' MessageProperties [headers={__TypeId__=java.lang.String}, contentType=application/json, contentEncoding=UTF-8, contentLength=0, receivedDeliveryMode=PERSISTENT, priority=0, deliveryTag=0]) 回复的状态码: 312 回复的文本内容: NO_ROUTE 发送该消息的交换机: spring.test01.directExchange 路由键: ttttestRK

确实是有错误提示，而且会给出错误的原因：

    回复的状态码: 312 
    回复的文本内容: NO_ROUTE
    发送该消息的交换机: spring.test01.directExchange 
    路由键: ttttestRK

也就是没有正确的路由








### 队列到消费者确认
p260

自带一个自动ack的机制，一旦接收到数据就会回复ack给队列，队列接到反馈后就会删除消息
但是一个很严重的缺陷是，该机制允许服务器一接收到消息就回复ack，也就是我只管接收到，我一接收到，队列就删除该消息
但是从逻辑上讲，服务器接收到消息后还应该进行处理，若是在这段处理的时间内服务器宕机，那么此时服务器丢失了消息，而队列那边早就把消息删了，这就导致消息的丢失
更严重的是，服务器一宕机，队列那边剩余阻塞的消息也会清空

解决方案是手动确认

1.配置application：

      spring:
        rabbitmq:
          istener:
            simple:
              acknowledge-mode: manual

此时，除非我们手动确认消息，否则消息在队列中是不会删除的，且会一直处于ready状态

2.在接收消息的方法后面手动确认ack，加上channel.basicAck()方法

      /**
       * 监听OrderEntity对象消息
       *
       * 加上了手动确认ack的
       */
      @RabbitHandler
      //@RabbitListener(queues = {"spring.test01.queue01"})
      public void listenOrderMessage(Message message, OrderEntity body, Channel channel) {
          System.out.println("接收到OrderEntity对象消息："+body);
          try {
              channel.basicAck(
                      message.getMessageProperties().getDeliveryTag()     //消息的tag
                      , false                                         //是否批量确认
              );
          }catch (Exception e){
              System.out.println(message.getMessageProperties().getDeliveryTag()+"号消息异常");
          }
      }

其中第一个参数是消息的tag，根据消息进入channel的顺序，从1开始的自增id。另外要Message对象的消息才能有tag
第二个表示是否连带确认后面进入该channel的所有消息


同样，basickNack()方法就是不确认ack，其中会多出一个参数requeue，表示是否将消息退回队列重新ready
例如：

      
    channel.basicNack(
            message.getMessageProperties().getDeliveryTag()     //消息的tag
            , false                                         //是否批量确认
            , true                                          //退回队列，或是删除消息
    );


      



