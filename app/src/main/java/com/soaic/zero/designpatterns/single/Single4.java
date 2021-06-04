package com.soaic.zero.designpatterns.single;

// 静态内部类, 缺点不能传构造函数的参数
public class Single4 {

    public String test;

    private Single4(){}

    // 当调用该方法的方法时，系统才会初始化SINGLE_LOADER内部类，并实例化Single4，这个过程是线程安全的
    public static Single4 getInstance() {
        return SINGLE_LOADER.INSTANCE;
    }

    private static class SINGLE_LOADER {
        private final static Single4 INSTANCE = new Single4();
    }
}
