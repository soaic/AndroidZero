package com.soaic.zero.rxjava;

public interface Observer<T> {

    void onSubscript();

    void onNext(T t);

    void onComplete();

    void onError(Throwable t);

}
