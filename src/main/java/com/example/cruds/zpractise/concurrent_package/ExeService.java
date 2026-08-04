package com.example.cruds.zpractise.concurrent_package;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
public class ExeService {

    public static void main(String[] args) throws Exception {

        System.out.println("Main Thread : " + Thread.currentThread().getName());

        ExecutorService executor = Executors.newFixedThreadPool(2);

        // ============================================================
        // 1. execute()
        // ============================================================

//        System.out.println("\n===== execute() =====");
//
//        executor.execute(() -> {
//            System.out.println("Task-1 executed by "
//                    + Thread.currentThread().getName());
//        });
//
//        executor.execute(() ->{
//            System.out.println(("ram"));
//        });
//
//        executor.execute(() -> {
//            System.out.println("Task-2 executed by "
//                    + Thread.currentThread().getName());
//        });
//
//        Thread.sleep(5000);















        // ============================================================
        // 2. submit(Runnable)
        // ============================================================
//
//        System.out.println("\n===== submit(Runnable) =====");
//
//        Future<?> future1 = executor.submit(() -> {
//            System.out.println("Runnable submitted by "
//                    + Thread.currentThread().getName());
//
//        });
//
//        System.out.println("Future returned : " + future1.get());











        // ============================================================
        // 3. submit(Callable)
        // ============================================================

//        System.out.println("\n===== submit(Callable) =====");
//
//        Future<Integer> future2 = executor.submit(() -> {
//
//            System.out.println("Callable executing on "
//                    + Thread.currentThread().getName());
//
//            Thread.sleep(2000);
//
//            return 100;
//
//        });
//
//        System.out.println("Waiting for Callable...");
//
//        System.out.println("Result = " + future2.get());










        // ============================================================
        // 4. invokeAll()
        // ============================================================

        System.out.println("\n===== invokeAll() =====");

        Callable<String> student1 = new Callable<String>() {

            @Override
            public String call() {
                return "Ram";
            }
        };

        Callable<String> student2 = new Callable<String>() {

            @Override
            public String call() {
                return "Ram";
            }
        };

        List<Callable<String>> list = new ArrayList<>();
        list.add(student1);
        list.add(student2);

//        List<Callable<String>> students = Arrays.asList(
//
//                () -> {
//                    return "Ram";
//                },
//
//                () -> {
//
//                    return "Krishna";
//                },
//
//                () -> {
//
//                    return "Ravi";
//                }
//
//        );

        List<Future<String>> results = executor.invokeAll(list);

        for (Future<String> f : results) {
            System.out.println(f.get());
        }







        // ============================================================
        // 5. invokeAny()
        // ============================================================
//
//        System.out.println("\n===== invokeAny() =====");
//
//        String first = executor.invokeAny(Arrays.asList(
//
//                () -> {
//                    Thread.sleep(3000);
//                    return "First";
//                },
//
//                () -> {
//                    Thread.sleep(1000);
//                    return "Second";
//                },
//
//                () -> {
//                    Thread.sleep(2000);
//                    return "Third";
//                }
//
//        ));
//
//        System.out.println("First Completed : " + first);

        // ============================================================
        // 6. isShutdown()
        // ============================================================

//        System.out.println("\n===== isShutdown() =====");
//
//        System.out.println(executor.isShutdown());

        // ============================================================
        // 7. shutdown()
        // ============================================================
//
//        System.out.println("\n===== shutdown() =====");
//
//        executor.shutdown();
//
//        System.out.println("shutdown() called");

        // ============================================================
        // 8. isShutdown()
        // ============================================================
//
//        System.out.println("\n===== isShutdown() After shutdown =====");
//
//        System.out.println(executor.isShutdown());

        // ============================================================
        // 9. awaitTermination()
        // ============================================================
//
//        System.out.println("\n===== awaitTermination() =====");
//
//        executor.awaitTermination(5, TimeUnit.SECONDS);

        // ============================================================
        // 10. isTerminated()
        // ============================================================
//
//        System.out.println("\n===== isTerminated() =====");
//
//        System.out.println(executor.isTerminated());

        // ============================================================
        // 11. submit after shutdown
        // ============================================================

//        System.out.println("\n===== submit after shutdown =====");
//
//        try {
//
//            executor.submit(() -> {
//
//                System.out.println("Won't Execute");
//
//            });
//
//        } catch (RejectedExecutionException e) {
//
//            System.out.println("Task Rejected : " + e);
//
//        }
//
//        System.out.println("\nProgram Finished");
    }
}


/*
execute()	Executes Runnable; no return value.
submit(Runnable)	Returns a Future; future.get() returns null because a Runnable doesn't produce a result.
submit(Callable)	Returns a Future containing the task's result.
invokeAll()	Runs all Callable tasks and returns a List<Future>.
invokeAny()	Returns the result of the first task to complete; the remaining tasks are cancelled.
shutdown()	Stops accepting new tasks but lets current tasks finish.
isShutdown()	Shows whether shutdown() has been called.
awaitTermination()	Waits for the executor to finish executing all submitted tasks.
isTerminated()	Indicates whether all tasks have completed after shutdown.
submit() after shutdown()	Throws a RejectedExecutionException because the executor no longer accepts new tasks.
 */
