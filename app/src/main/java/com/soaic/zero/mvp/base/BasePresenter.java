package com.soaic.zero.mvp.base;

public class BasePresenter<V extends BaseView> {

    private V mView;

    public void attach(V view) {
        this.mView = view;
    }

    public void detach() {
        this.mView = null;
    }
}
