package com.soaic.zero.rxjava;

public class ObservableSubscribeOn<T> extends ObservableUpstream<T, T> {

    private final Scheduler scheduler;

    public ObservableSubscribeOn(ObservableSource<T> source, Scheduler scheduler) {
        super(source);
        this.scheduler = scheduler;
    }

    @Override
    protected void subscribeActual(final Observer<T> observer) {
        observer.onSubscript();

        scheduler.schedule(new Runnable() {
            @Override
            public void run() {
                source.subscript(new SubscriptOnObserver<>(observer));
            }
        });
    }

    private static class SubscriptOnObserver<T> implements Observer<T> {

        private final Observer<T> observer;

        public SubscriptOnObserver(Observer<T> observer) {
            this.observer = observer;
        }

        @Override
        public void onSubscript() {
            observer.onSubscript();
        }

        @Override
        public void onNext(T t) {
            observer.onNext(t);
        }

        @Override
        public void onComplete() {
            observer.onComplete();
        }

        @Override
        public void onError(Throwable t) {
            observer.onError(t);
        }
    }
}
