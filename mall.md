

springboot版本：3.1.3























==========关于数据库==============

使用linux环境部署

=================================

==========docker========================================================================================================

I.安装

            1.网址：https://zhuanlan.zhihu.com/p/143156163
    
            2.运行docker：

                    systemctl start docker

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
                    sudo systemctl restart docker

========================================================================================================================













==========docker安装mysql================================================================================================
            

I.安装
            docker pull mysql

默认安装最新版mysql

            





II.创建mysql实例并运行

            docker run -itd --name mysql-test -p 3306:3306 -v /mydata/mysql/log:/var/log/mysql -v /mydata/mysql/data:/var/data/mysql -v /mydata/mysql/conf.d:/etc/mysql.d -e MYSQL_ROOT_PASSWORD=123456 mysql

此时，外部服务器可以通过3306端口访问mysql，其密码为123456
将mysql的日志log挂载到/mydata/mysql/log/mysql
将mysql的数据data挂载到/mydata/mysql/data/mysql
将mysql的配置文件conf.d挂载到/mydata/mysql/conf.d



III.连接远程数据库
由于本虚拟机ip地址为192.168.74.128，故远程连接时的ip地址要选用此ip
    
            192.168.74.128:3306
            root
            123456

IV.在linux用docker命令进入mysql

            docker exec -it [ID] /bin/bash

其中-it表示交互方法
[ID]为该容器id开头三个字母
/bin/bash表示进入mysql的内部

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

主要是把编码格式改为utf8
配置过后要重启容器

进入mysql内部后，cd /etc/mysql.d
随后cat my.cnf就可以查看配置文件了


========================================================================================================================










==========docker安装redis================================================================================================

I.直接pull redis，创建实例并运行：

            docker run -itd --name redis-test -p 6379:6379 -v /mydata/redis/data:/data -v /mydata/redis/conf/redis.conf:/etc/redis/redis.conf -d redis redis-server /etc/redis/redis.conf

此时，redis的端口6379映射到docker的6379，/data和redis.conf映射到指定位置
-d代表后台运行，以redis镜像文件启动redis服务，并加载后面的配置文件




II.进入redis内部，然后可以使用redis的指令

            docker exec -it redis redis-指令



III.持久化
在redis.conf里加上一行：

            appendonly yes

随后要重启redis
此时redis设置的数据，可以直接持久化






IV.连接
使用的ip是虚拟机的ip地址



========================================================================================================================




==========配置git=========================================================

安装git

在git bash中配置：

            git config --global user.name "InnerSekiro"
            git config --global user.email "a18290531268@163.com"
            
生成免密连接密钥：

            ssh-keygen -t rsa -C "a18290531268@163.com"

密钥位置会给出提示

=========================================================================






==========创建微服务模块===============================================================

最先要导入的是：Web/SpringWeb、SpringCloudRoutine/OpenFeign
所有模块的父包名（组织名）都应该是：com.katzenyasax.mall

要创建的模块有：

            product：商品
            order：订单
            member：人员
            coupon：优惠券
            ware：

注意外面应当有一个大型mall模块聚合上述所有模块
可以在创建之时创建一个空项目，但是选上maven，创建之后就有pom.xml了，把packaging改为pom
然后用<module>所有模块
注意之后要在maven里添加mall作为总服务，此时mall会包含所有微服务




在总服务的.gitignore添加：

            **/.mvn
            **/mvnw
            **/mvnw.cmd
            **/target/
            .idea
            **/.gitignore

表示上传git时忽略这些无用文件





上传到gitee：
在settings/plugins安装gitee插件
mall总服务git->commit->push







======================================================================================




==========数据库初始化=======================================

连接docker部署的mysql，都创建数据库：

            mall_oms：order
            mall_pms：product
            mall_sms：coupon
            mall_ums：member
            mall_wms：ware

ms为manage system的缩写




========================================================



==========使用开源项目模板创建后台管理系统==================================================================================================

人人开源：renren-fast、renren-fats-vue、renren-generator

renren-fast删掉.git直接加入mall的包下，并在mall的pom.xml里加入module
根据fast目录下db里的sql创建数据库：

            mall_admin

在application.yml中设置默认环境为test，并将application-test.yml的连接设置做好
比如url、username、password啥的：

            driver-class-name: com.mysql.cj.jdbc.Driver
            url: jdbc:mysql://192.168.74.128:3306/mall_admin?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai
            username: root
            password: 123456



运行人人fast的启动类，在浏览器输入localhost:8080/renren-fast，应该会返回：

            {"msg":"invalid token","code":401}

表示运行成功了








renren-fast-vue，前端工程用vscode打开

安装node.js
配置npm淘宝镜像：

            npm config set registry http://r.cnpmjs.org/

下载会快很多
然后到vscode控制台终端（因为是首次运行vue项目），安装npm：

            npm install

他是根据前端文件下package.json的目录下载的
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


==================================================================================================================================





===========逆向工程==================================================

克隆renren-fast-generator，删除.git，导入mall总服务，标记为模块
加入mall的pom.xml中
注意再generator的pom文件中，parent springboot工程下添加：

            <relativePath/> <!-- lookup parent from repository -->
            


随后根据不同的数据库生成不同的代码，例如生成mall_pms的代码
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

随后运行，比较慢

springboot遇到循环依赖问题而无法启动时：
在yml文件中添加：

            spring:
                main:
                    allow-circular-references: true



生成后下载文件，直接把文件里/main的java和resource复制到项目的product里

但是下载的包里面好多依赖都没有，因此创建一个公共的类，其作用应当是为所有微服务提供公共依赖
创建maven模块：mall-common

因此此时的mall-product需要依赖mall-common：

            <dependency>
                <groupId>com.katzenyasax</groupId>
                <artifactId>mall-commom</artifactId>
                <version>0.0.1-SNAPSHOT</version>
            </dependency>

然后到common里加上公共依赖
目前已知的公共依赖为：

            mybatis-plus
            lombok

随后在common模块加入com.katzenyasax.common.utils.R等工具包：
从renren fast复制需要的工具包，需要的是Query、R和PageUtils
复制过去后不再报错
            






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














之后对其他模块也进行逆向工程
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

然后启动类添加@MapperScan

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

如上
注意不要用7000、8000等作为端口号，浏览器会认为这是不安全的端口而自动屏蔽


========================================================================================================================





==========Spring Cloud======================================================================================

使用spring cloud alibaba

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


=============================================================================================================






========== nacos =============================================================================================================

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

                        docker network create nacos_networkne

                2.3.启动

                        docker run --name nacos -e NACOS_AUTH_ENABLE=true -d -p 9848:9848 -p 8848:8848 -p 9849:9849 --network nacos_network -e MODE=standalone nacos/nacos-server:v2.1.1

    
                  通过名为nacos_network的网络容器，在端口8848上开启了一个nacos服务器，容器模式为standable
                  注意，虚拟机的ip地址仍为：192.168.74.128

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


===================================================================================================================================================





=========== open feign 微服务间远程调用===================================================================================

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
    
            @@EnableDiscoveryClient

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


========================================================================================================================












========== nacos 作为配置中心============================================================================================

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


========================================================================================================================





========== nacos 配置中心细节 ==========================================================================

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


=======================================================================================================













========== 网关 =================================================================================================

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


======================================================================================================================










========== 商品服务I.三级分类：方法 =======================================================================================

首先导入数据到pms_category，表示商品的分类
商品种类分为3级，要求查出所有的分类，并根据父子关系进行组装

product服务的CategoryController中，没有对应的方法，因此自己定义一个：

            @RequestMapping("/list/tree")
            public R listTree(){}

我们已经知道了如何查出所有的分类，也就是对应的service中的list方法，但是我们希望的是得到以父子关系组织好了的结果
所以我们应该在service里定义一个方法listAsTree()，这样一来我们只需要在listTree中返回R的ok()方法得到的数据就行了：

            @RequestMapping("/list/tree")
            public R listTree(){
                List<CategoryEntity> categoryEntityList=categoryService.listAsTree();
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
	        private List<CategoryEntity> children;

最终方法为：

            @Override
            public List<CategoryEntity> listAsTree() {
                //查出所有分类
                List<CategoryEntity> entities=baseMapper.selectList(null);
                //组装父子
                //获取一级子类
                List<CategoryEntity> oneCategory=entities.stream().filter(categoryEntity -> categoryEntity.getCatLevel()==1)toList();
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

格外注意，msg、code只是通信状况的反馈，success内才是正确的数据


===========================================================================================================================





========== 商品服务I.三级分类：网关配置 =======================================================================================

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













跨域

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
                    corsConfiguration.addAllowedOrigin("*");            //允许跨域的
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




============================================================================================================================











========== 商品服务I.三级分类：树形分类的前端展示 =======================================================================================

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

            



================================================================================================================================================================








========== 商品服务I.三级分类：树形分类的删除 =======================================================================================

路径为：http://localhost:10100/api/product/category/delete
请求方式为post，前端传输json到后端，后端将json的数据打包为对象，再进行逻辑处理

CategoryController中已有了一个delete方法：

            @RequestMapping("/delete")
            public R delete(@RequestBody Long[] catIds){
	        	categoryService.removeByIds(Arrays.asList(catIds));
                return R.ok();
            }

它请求的是一个数组，存放需要删除的分类的catId号
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





================================================================================================================================================================








========== 商品服务I.三级分类：树形分类的新增 =======================================================================================





















