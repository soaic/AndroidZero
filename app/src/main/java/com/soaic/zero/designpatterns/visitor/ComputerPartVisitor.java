package com.soaic.zero.designpatterns.visitor;

public interface ComputerPartVisitor {
    void visitor(Mouse part);
    void visitor(Monitor part);
}
