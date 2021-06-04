package com.soaic.zero.designpatterns.observer;

public class SecondObserver extends Observer {

    @Override
    public void update(Subject subject) {
        System.out.println("SecondObserver fetch update stage: " + subject.getStage());
    }
}
