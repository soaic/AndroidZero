package com.soaic.zero.designpatterns.visitor;

public class Mouse implements ComputerPart{
    @Override
    public void accept(ComputerPartVisitor part) {
        part.visitor(this);
    }
}
