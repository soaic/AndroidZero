package com.soaic.zero.designpatterns.single;

// 饿汉式
public class Single1 {

    public static Single1 INSTANCE = new Single1();

    public String test;

    private Single1(){}

}
