package com.soaic.zero.rxjava;

public abstract class Observable<T> implements ObservableSource<T>{

    protected abstract void subscribeActual(Observer<T> observer);

    public static <T> Observable<T> just(T item) {
        return new ObservableJust<>(item);
    }

    public <R> Observable<R> map(Function<T, R> mapper) {
        return new ObservableMap<>(this, mapper);
    }

    @Override
    public void subscript(Observer<T> observer) {
        subscribeActual(observer);
    }

    public Observable<T> observableOn(Scheduler scheduler) {
        return new ObservableObserverOn<>(this, scheduler);
    }

    public Observable<T> subscriptOn(Scheduler scheduler) {
        return new ObservableSubscribeOn<>(this, scheduler);

    }
}
