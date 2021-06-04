package com.soaic.zero.designpatterns.single;

// 懒汉式-加锁、双重校验锁、DCL
public class Single3 {

    //volatile(防止重排序-没有关联顺序的语句会被jvm排序进行性能优化、线程可见性，所有的write操作都在read操作之前)
    private volatile static Single3 INSTANCE;

    public String test;

    private Single3(){}

    // 消耗性能
    public synchronized static Single3 getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Single3();
        }
        return INSTANCE;
    }

    // 双重校验锁
    public static Single3 getInstanceDCL() {
        if (INSTANCE == null) {
            synchronized (Single3.class) {
                if (INSTANCE == null) {
                    INSTANCE = new Single3();
                }
            }
        }
        return INSTANCE;
    }

}
