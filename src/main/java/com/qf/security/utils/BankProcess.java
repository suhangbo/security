package com.qf.security.utils;

public class BankProcess extends Thread{

    private Account account;

    public BankProcess(Account account) {
        this.account = account;
    }

    @Override
    public void run() {
        int num = account.withDraw(600);
        System.out.println("取出来的金额：" + num);
    }

    public static void main(String[] args) {
        Account account = new Account();
//        Account account2 = new Account();

        Thread t1 = new BankProcess(account);
        Thread t2 = new BankProcess(account);

        t1.start();
        t2.start();
    }
}
