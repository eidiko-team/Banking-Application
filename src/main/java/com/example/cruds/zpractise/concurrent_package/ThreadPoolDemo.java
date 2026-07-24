package com.example.cruds.zpractise.concurrent_package;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolDemo {

    public static void main(String[] args) {

        // Create a thread pool with only 3 threads
        ExecutorService executor = Executors.newFixedThreadPool(3);

        // Submit 10 tasks
        for (int i = 1; i <= 10; i++) {
            executor.execute(new Task1(i));
        }

        // Tell the executor no more tasks will be submitted
        executor.shutdown();
    }
}

// Task class
class Task1 implements Runnable {

    private int taskNumber;

    public Task1(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    @Override
    public void run() {

        System.out.println(
                "Task " + taskNumber +
                        " started by " +
                        Thread.currentThread().getName());

        try {
            // Simulate some work
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(
                "Task " + taskNumber +
                        " finished by " +
                        Thread.currentThread().getName());
    }
}
