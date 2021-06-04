package com.soaic.zero.designpatterns.observer;

public class FirstObserver extends Observer {

    @Override
    public void update(Subject subject) {
        System.out.println("FirstObserver fetch update stage: " + subject.getStage());
    }
}
