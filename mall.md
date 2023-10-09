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

                    systemctl c;

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

链接： https://developer.aliyun.com/article/1245481?spm=a2c6h.14164896.0.0.1a9a47c5JPNQiV&scm=20140722.S_community@@%E6%96%87%E7%AB%A0@@1245481._.ID_1245481-RL_docker%E9%98%BF%E9%87%8C%E4%BA%91%E9%95%9C%E5%83%8F-LOC_search~UND~community~UND~item-OR_ser-V_3-P0_1mysql

                    














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










## 云存储服务 



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
            2.匹配catelogId，注意一个惊天大坑，数据库中该字段的名字为catalogId，为0或为空时匹配全部
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
PUT命令是在es中：进入索引、进入类型、存入数据

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
            {"index":{"_id":"2"}} 
            {"sentence":"Hello there"}
            {"index":{"_id":"3"}}
            {"sentence":"this is id 3"}

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
      }private String attrValue;
                  }
  用于封装要存至es的数据
2.在productService的upSpu方法中添加存入es的方法：
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
  "hits" : {
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
        "_source" : {
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
          "skuImg" : "https://gulimall-hello.oss-cn-beijing.aliyuncs.com/2019-11-26/60e65a44-f943-4ed5-87c8-8cf90f403018_d511faab82abb34b.jpg",
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


















