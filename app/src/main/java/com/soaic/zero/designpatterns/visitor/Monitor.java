package com.soaic.zero.designpatterns.visitor;

public class Monitor implements ComputerPart{
    @Override
    public void accept(ComputerPartVisitor part) {
        part.visitor(this);
    }
}
