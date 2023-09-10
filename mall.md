
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





==========maven配置阿里云======================




==============================================


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
然后从renren fast复制需要的工具包，需要的是Query、R和PageUtils
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
                    <!-- 并且出现找不到xml中方法的情况时，第一时间也应想到是不是只依赖了该包 -->
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





==========Spring Cloud==========================================================================

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





