package com.soaic.zero.rxjava;

public class ObservableMap<T, U> extends ObservableUpstream<T, U> {

    private final Function<T, U> function;

    public ObservableMap(ObservableSource<T> source, Function<T, U> function) {
        super(source);
        this.function = function;
    }

    @Override
    protected void subscribeActual(Observer<U> observer) {
        source.subscript(new MapObserver<>(observer, function));
    }


    private static class MapObserver<T, U> extends BasicObserver<T, U> {

        private final Function<T, U> function;

        public MapObserver(Observer<U> observer, Function<T, U> function) {
            super(observer);
            this.function = function;
        }

        @Override
        public void onSubscript() {
            downStream.onSubscript();
        }

        @Override
        public void onNext(T u) {
            U result = function.apply(u);
            downStream.onNext(result);
        }

        @Override
        public void onComplete() {
            downStream.onComplete();
        }

        @Override
        public void onError(Throwable t) {
            downStream.onError(t);
        }
    }
}
