package com.soaic.zero.designpatterns.single;

// 枚举
public enum Single5 {

    INSTANCE("Name1"), INSTANCE2("Name2");

    String name;
    Single5(String name) {
        this.name = name;
    }
}
