package com.soaic.zero.rxjava;

public interface ObservableSource<T> {

    void subscript(Observer<T> observer);
}
