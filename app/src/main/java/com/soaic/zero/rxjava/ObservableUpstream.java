package com.soaic.zero.rxjava;

abstract class ObservableUpstream<T, U> extends Observable<U>{

    protected ObservableSource<T> source;

    ObservableUpstream(ObservableSource<T> source) {
        this.source = source;
    }

}
