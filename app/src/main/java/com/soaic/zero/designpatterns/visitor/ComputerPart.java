package com.soaic.zero.designpatterns.visitor;

public interface ComputerPart {
    void accept(ComputerPartVisitor part);
}
