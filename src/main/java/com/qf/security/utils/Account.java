package com.qf.security.utils;

public class Account {

    private int amount = 1000;

    // 取款， 参数是取款金额
    public synchronized int withDraw(int num) {
        if(num <= 0) {
            return -2;
        }

        if(num > amount) {
            return -1;
        }

        try{
            Thread.sleep(200);
            amount = amount - num;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("取款：" + num + ", 账户剩余：" + amount);

        return num;
    }
}
