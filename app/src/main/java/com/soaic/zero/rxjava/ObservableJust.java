package com.soaic.zero.rxjava;

public class ObservableJust<T> extends Observable<T> {

    private final T mItem;

    public ObservableJust(T item) {
        this.mItem = item;
    }


    @Override
    protected void subscribeActual(Observer<T> observer) {
        ScalarRunnable<T> scalarRunnable = new ScalarRunnable<T>(observer, mItem);
        scalarRunnable.run();
    }

    private static class ScalarRunnable<T> implements Runnable {

        private final Observer<T> observer;
        private final T mItem;

        public ScalarRunnable(Observer<T> observer, T mItem) {
            this.observer = observer;
            this.mItem = mItem;
        }

        @Override
        public void run() {
            observer.onNext(mItem);
            observer.onComplete();
        }
    }

}
