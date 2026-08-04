package com.example.cruds.zpractise.multitreading;

class RunnableDemo implements Runnable{
    @Override
    public void run() {
        System.out.println("ram");
    }
}

public class Main{
    public static void main(String[] args) {
//        Runnable r1 =  new RunnableDemo();
        RunnableDemo r1 = new RunnableDemo();
        Thread t1 =  new Thread(r1);
        t1.start();
    }
}


