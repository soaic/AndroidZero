package com.soaic.zero.rxjava;

abstract class BasicObserver<T, R> implements Observer<T>{

    protected Observer<R> downStream;

    BasicObserver(Observer<R> stream) {
        this.downStream = stream;
    }


}
