package com.katzenyasax.mall.search.thread;


import ch.qos.logback.core.joran.conditional.ThenAction;
import io.swagger.models.auth.In;
import org.elasticsearch.common.recycler.Recycler;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 测试异步编排
 */
public class CompletableFutureTest {

    /**
     * 自定义一个核心线程数为10的线程池
     */
    public static ExecutorService executorService= Executors.newFixedThreadPool(10);

    public static void main(String[] args) throws ExecutionException, InterruptedException {


        CompletableFuture<Object> future01=CompletableFuture.supplyAsync(()->{
            Long id;
            System.out.println("第一个线程："+(id=Thread.currentThread().getId()));
            return (Object) id;
        },executorService)
        /**
         * 结果和异常感知
         */
               /* .whenComplete((result,exception)->{
                    System.out.println("结果："+result+" ;异常："+exception);
                })
                .exceptionally(throwable -> {
                    System.out.println("发生异常");
                    return -1l;
                })*/

        /**
         * 最终处理
         */
                /*.handle((res,exc)->{
                    if(res!=null){
                        return "结果是："+res;
                    }
                    if(exc!=null){
                        return "发生异常："+exc;
                    }
                    return "未发生异常，但是结果为空";
                })*/

        /**
         * 无结果感知、也无返回值的串行化
         */
                /*.thenRunAsync(()->{
                    System.out.println("thenRunAsync，启动！");
                },executorService)*/

        /**
         * 可感知结果、但是无返回值的串行化
         */
                /*.thenAcceptAsync((res)->{
                    System.out.println("thenAcceptAsync，启动！"+"，上一个线程的返回值："+res);
                },executorService)*/

        /**
         * 可感知结果、有返回值的串行化
         */
               /* .thenApplyAsync(res->{
                    System.out.println("thenApplyAsync，启动！"+"，上一个线程的返回值："+res);
                    return (Object) 999l;
                },executorService)
                .whenComplete((res,exc)->{
                    System.out.println("thenApplyAsync的返回值："+res);
                })*/

        /**
         * 可感知两个线程的返回值、本身有返回值的同时处理：
         */
                /*.thenCombineAsync(
                        CompletableFuture.supplyAsync(()->{
                            Long id;
                            System.out.println("第二个线程："+(id=Thread.currentThread().getId()));
                            return (id);
                        })
                        , ((res1,res2)->{
                            System.out.println("第一个异步线程的结果："+res1);
                            System.out.println("第二个异步线程的结果："+res2);
                            return res1+res2;
                        })
                );*/

        /**
         * 可感知两个线程的返回值、本身无返回值的同时处理
         */
                /*.thenAcceptBothAsync(
                        CompletableFuture.supplyAsync(()->{
                            Long id;
                            System.out.println("第二个线程："+(id=Thread.currentThread().getId()));
                            return (id);
                        })
                        , ((res1,res2)->{
                            System.out.println("第一个异步线程的结果："+res1);
                            System.out.println("第二个异步线程的结果："+res2);
                        })
                );*/

        /**
         * 两个线程执行完后，才开始执行第三个线程
         */

                /*.runAfterBothAsync(
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
                );*/


                /**
                 * 两个线程有一个执行完后，就开始执行下一个线程，未完成的那个线程依然执行
                 * 结果感知、本身有返回值
                 */

                /*.applyToEither(
                        CompletableFuture.supplyAsync(()->{
                            Long id=-1l;
                            while((10l-++id)>=0l) {
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
                            return res;
                        })
                )*/

                /**
                 * 两个线程有一个执行完后，就开始执行下一个线程，未完成的那个线程依然执行
                 * 仅结果感知
                 */
                /*.acceptEither(
                        CompletableFuture.supplyAsync(()->{
                            Long id=-1l;
                            while((10l-++id)>=0l) {
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
                        }))*/


                /**
                 * 无结果感知、无返回值
                 */

                /*.runAfterEither(
                        CompletableFuture.supplyAsync(()->{
                            Long id=-1l;
                            while((10l-++id)>=0l) {
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
                        }))*/







        ;

        Thread.sleep(2000);




        CompletableFuture<Object> res = CompletableFuture.anyOf(future01);
        System.out.println(res.get());

        //System.out.println("get："+future01.get().toString());









    }


}
