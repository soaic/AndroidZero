package com.soaic.zero.designpatterns.clone;

import androidx.annotation.NonNull;

public class People{

    private int age;
    private String name;
    private Company company;

    public People(String name, int age, Company company) {
        this.age = age;
        this.name = name;
        this.company = company;
    }

    public People clone() {
        Company com = new Company(company.name, company.address);
        People object = new People(name, age, com);
        // 也可以 implements Cloneable 调用系统自带的clone方法
        //object = super.clone();
        return object;
    }
}
