package com.qf.security.utils;

public class Example {
    public static void main(String[] args) {
        MyExample myExample = new MyExample();
        MyExample myExample2 = new MyExample();

        MyThread t1 = new MyThread(myExample);
        MyThread2 t2 = new MyThread2(myExample2);

        t1.start();
        t2.start();
    }
}

class MyThread extends Thread {

    private MyExample myExample;

    public MyThread(MyExample myExample) {
        this.myExample = myExample;
    }

    @Override
    public void run() {
        myExample.output();
    }
}

class MyThread2 extends Thread {

    private MyExample myExample;

    public MyThread2(MyExample myExample) {
        this.myExample = myExample;
    }

    @Override
    public void run() {
        myExample.input();
    }
}



class MyExample {
    public static synchronized void output() {
        for (int i = 0; i < 10; i++) {
            System.out.println("HelloWorld: " + i);
        }
    }

    public static synchronized void input() {
        for (int i = 0; i < 10; i++) {
            System.out.println("ABCXYZ: " + i);
        }
    }
}
