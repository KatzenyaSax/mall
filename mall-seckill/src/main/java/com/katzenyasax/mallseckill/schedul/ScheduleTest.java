package com.katzenyasax.mallseckill.schedul;


import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

//@Component
@EnableAsync
@EnableScheduling
public class ScheduleTest {
    @Scheduled(cron = "1-10 * * * * ? ")
    @Async
    public void test01() throws InterruptedException {
        System.out.println("TEST");
        Thread.sleep(3000);
    }
}
