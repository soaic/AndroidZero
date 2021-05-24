package com.soaic.zero.rxjava;

public class ObservableObserverOn<T> extends ObservableUpstream<T, T> {

    private final Scheduler scheduler;

    public ObservableObserverOn(ObservableSource<T> source, Scheduler scheduler) {
        super(source);
        this.scheduler = scheduler;
    }

    @Override
    protected void subscribeActual(Observer<T> observer) {
        source.subscript(new ObserverOnObserver<T>(observer, scheduler));
    }


    private static class ObserverOnObserver<T> implements Observer<T> {

        private final Observer<T> observer;
        private final Scheduler scheduler;

        public ObserverOnObserver(Observer<T> observer, Scheduler scheduler) {
            this.observer = observer;
            this.scheduler = scheduler;
        }

        @Override
        public void onSubscript() {
            observer.onSubscript();
        }

        @Override
        public void onNext(final T t) {
            scheduler.schedule(new Runnable() {
                @Override
                public void run() {
                    observer.onNext(t);
                }
            });
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
