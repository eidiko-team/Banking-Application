package com.example.cruds.zpractise.concurrent_package;

import java.util.concurrent.Executor;

// Main class
public class ExecutorDemo {

    public static void main(String[] args) {

        System.out.println("Main thread starts...");

        // Create an object of our custom Executor
        Executor executor = new SimpleExecutor();

        // Submit Task 1
        executor.execute(new Task("Task 1"));

        // Submit Task 2
        executor.execute(new Task("Task 2"));

        // Submit Task 3
        executor.execute(new Task("Task 3"));

        System.out.println("Main thread continues its work...");
    }
}

// Our custom Executor implementation
class SimpleExecutor implements Executor {

    @Override
    public void execute(Runnable command) {

        System.out.println("Executor received a task.");

        // Create a new thread and execute the task
        Thread thread = new Thread(command);

        System.out.println("Starting thread: " + thread.getName());

        thread.start();
    }
}

// Task class
class Task implements Runnable {

    private String taskName;

    public Task(String taskName) {
        this.taskName = taskName;
        System.out.println("##################i am here");
    }

    @Override
    public void run() {

        System.out.println(taskName + " is running on "
                + Thread.currentThread().getName());

        // Simulate some work
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(taskName + " completed.");
    }
}
