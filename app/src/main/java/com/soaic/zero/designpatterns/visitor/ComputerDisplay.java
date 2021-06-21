package com.soaic.zero.designpatterns.visitor;

public class ComputerDisplay implements ComputerPartVisitor{

    @Override
    public void visitor(Mouse part) {
        System.out.println("Mouse display");
    }

    @Override
    public void visitor(Monitor part) {
        System.out.println("Monitor display");
    }
}
