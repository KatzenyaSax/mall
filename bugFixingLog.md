<!--
 * @Author: InnerSekiro a18290531268@163.com
 * @Date: 2023-09-25 21:43:28
 * @LastEditors: InnerSekiro a18290531268@163.com
 * @LastEditTime: 2023-10-07 15:39:53
 * @FilePath: \undefinedc:\Users\ASUS\Desktop\IDEA\mall\bugFixingLog.md
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
-->

## 2023/9/25

补充了下列功能：
    1.删除属性(AttrGroup)时，连带删除属性(AttrGroup)与参数(Attr)的关系

修复了以下功能：
    1.无法删除属性(AttrGroup)和参数(Attr)的关联

新增了下列功能：
    1.商品上架时，所有sku可被存至es







## 2023/9/26

新增了以下内容：
    1.商城首页的渲染
    2.商城左侧商品分类栏
    3.基本配置好了域名，可用于：访问虚拟机、网关路由、访问商城首页

更新了以下内容：
    1.将CategoryService内的获取三级分类的方法用流式编程重构
    2.将网关路由的目的地全部改为动态服务名



## 2023/9/27

更新了以下内容：
    1.更新了三级分类的两个接口，优化了接口方法的处理时间




## 2023/10/1

更新了以下内容：
    1.使用redis缓存方法改写了两个三级分类业务




## 2023/10/2

更新了以下内容：
    1.为三级分类加上了本地锁




## 2023/10/7

更新了以下内容：
    1.为三级分类加上了原生分布式锁
    2.将三级分类改造为使用redisson分布式锁进行缓存
    




## 2023/10/9

更新了以下内容：
    1.三级分类的写入缓存操作由SpringCache注解实现，同时实现了数据库更新时缓存的失效模式


