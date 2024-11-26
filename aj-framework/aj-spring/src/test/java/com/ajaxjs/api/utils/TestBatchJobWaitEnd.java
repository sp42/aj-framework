package com.ajaxjs.api.utils;

import lombok.Data;
import org.junit.jupiter.api.Test;

import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestBatchJobWaitEnd {
    BatchJobWaitEnd batch = new BatchJobWaitEnd();

    @Data
    static
    class Task {
        private int task1;

        private int task2;
    }

    @Test
    public void testBatchWithTasks() {
        Task task = new Task();

        // 定义一些示例任务
        Supplier<Boolean> task1 = () -> {
            // 模拟耗时操作
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            task.setTask1(1);
            System.out.println("Finished task1");

            return true;
        };

        Supplier<Boolean> task2 = () -> {
            // 模拟耗时操作
            try {
                Thread.sleep(550);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            task.setTask2(2);
            System.out.println("Finished task2");
            return false;
        };

        // 执行 batch 方法
        batch.batch(task1, task2);
        System.out.println(task.getTask1());
        System.out.println(task.getTask2());

        assertEquals(1, task.getTask1());
        assertEquals(2, task.getTask2());
    }
}
