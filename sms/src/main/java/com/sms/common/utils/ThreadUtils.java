package com.sms.common.utils;

import java.util.concurrent.CountDownLatch;

public class ThreadUtils {
    /**
     * 睡眠指定时间
     *
     * @param duration 睡眠时间（单位：毫秒）
     */
    public static void sleep(long duration) {
        try {
            Thread.sleep(duration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 执行指定任务，并等待所有任务执行完成
     *
     * @param tasks 任务列表
     */
    public static void runAndWait(Runnable... tasks) {
        // 创建计数器
        CountDownLatch latch = new CountDownLatch(tasks.length);

        // 启动多线程执行任务
        for (int i = 0; i < tasks.length; i++) {
            int taskIndex = i;
            String threadName = "Task-" + taskIndex;

            // 设置线程名字
            Thread thread = new Thread(() -> {
                try {
                    // 执行任务
                    tasks[taskIndex].run();
                } finally {
                    // 计数器减一
                    latch.countDown();
                }
            }, threadName);

            // 启动线程
            thread.start();
        }

        // 等待所有任务执行完成
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}