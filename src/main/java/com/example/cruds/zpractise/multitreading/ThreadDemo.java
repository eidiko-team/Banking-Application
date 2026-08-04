package com.example.cruds.zpractise.multitreading;

class ThreadDemoooo extends Thread{
    @Override
    public void run() {
        System.out.println("ram"+currentThread().getName());
    }
}

public  class ThreadDemo{
    public static void main(String[] args) {
//    Thread t1 =  new Thread();

//        the default implementation of run() in the Thread class is empty.
//        Internally, it's something like:
//        public void run() {
//            // Nothing here
//        }
//        So when you call
//        t1.start();
//        a new thread is created, it executes run(), finds nothing to do, and terminates.



//        A Thread object can be started only once.
//        Think of a thread like a person running a race.

//              Person
//                |
//              Start Race
//                |
//              Finish Race
//
//        Once the race is over, you cannot start the same race again.
//        If you want to run again, you need a new Thread object.


//        You are calling start() twice on the same Thread object.
//        This is not allowed.
//        thread1.start();
//        thread1.start();


        Thread thread =  new ThreadDemoooo();
       ThreadDemoooo threadDemoooo = new ThreadDemoooo();

       //Both are valid because threadDemoooo extended the Thread so can take the reference type as the Thread
       thread.start();
       threadDemoooo.start();




    }
}

