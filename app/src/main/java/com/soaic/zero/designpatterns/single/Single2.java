package com.soaic.zero.designpatterns.single;

// 懒汉式
public class Single2 {

    private static Single2 INSTANCE;

    public String test;

    private Single2(){}

    public static Single2 getInstance() {
        if (INSTANCE == null)
            INSTANCE = new Single2();
        return INSTANCE;
    }

}
