package com.soaic.zero.designpatterns.observer;

public class Client {

    public static void main(String[] args) {
        Subject subject = new Subject();

        FirstObserver firstObserver = new FirstObserver();
        SecondObserver secondObserver = new SecondObserver();

        subject.attach(firstObserver);
        subject.attach(secondObserver);

        subject.setState(2);
        subject.setState(12);
    }

}
