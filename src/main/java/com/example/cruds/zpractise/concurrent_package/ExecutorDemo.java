package com.example.cruds.zpractise.concurrent_package;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//Step 1: Main method starts
//System.out.println("Main thread starts...");
//Output:
//Main thread starts...


//Step 2: Create Executor
//Executor executor = new SimpleExecutor();
//An object of SimpleExecutor is created.
//executor
//SimpleExecutor Object
//Nothing is executed yet.


//Step 3: Create Task 1
//        executor.execute(new Task("Task 1"));
//First,
//        new Task("Task 1")
//is executed.
//So the constructor runs.
//public Task(String taskName) {
//    this.taskName = taskName;
//    System.out.println("##################i am here");
//}
//Output
//##################i am here
//Now an object is created.
//        Task Object
//        implements Runnable
//This object is passed to
//execute(Runnable command)
//because
//Task IS-A Runnable ******


//Step 4: execute() is called
//public void execute(Runnable command)
//receives
//        command
//Task Object
//Inside
//Thread thread = new Thread(command);
//Here the thread stores the Runnable.
//Internally think of it like this:
//Thread Object
//target -----> Task Object
//Then
//thread.start();
//creates a new thread.


// Step 5: Thread executes run()
//When you call
//thread.start();
//the JVM automatically calls
//command.run();
//which is actually
//Task.run();
//So this executes
//System.out.println(taskName + " is running...");
//Same happens for Task2 and Task3

// Main class
public class ExecutorDemo {

    public static void main(String[] args) {

        System.out.println("Main thread starts...");

        // Create an object of our custom Executor
        Executor executor = new SimpleExecutor();

        ExecutorService executor1 = Executors.newFixedThreadPool(3);



        // Submit Task 1
        //creating object of Task that implements Runnable so acts as runnable
        //now we are passing that runnable object to the execute method
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
        //
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
