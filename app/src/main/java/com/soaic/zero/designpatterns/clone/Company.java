package com.soaic.zero.designpatterns.clone;

public class Company {

    public String name;
    public String address;

    public Company(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public Company clone()  {
        Company company = new Company(name, address);
        return company;
    }
}
