package com.soaic.zero.designpatterns.observer;

import java.util.ArrayList;
import java.util.List;

public class Subject {

    List<Observer> observerList = new ArrayList<>();
    private int state;

    public void setState(int state){
        this.state = state;
        notifyAllChange();
    }

    public int getStage() {
        return state;
    }

    public void attach(Observer observer) {
        observerList.add(observer);
    }

    public void notifyAllChange() {
        for (Observer observer : observerList) {
            observer.update(this);
        }
    }

    public void detach(Observer observer) {
        observerList.remove(observer);
    }
}
