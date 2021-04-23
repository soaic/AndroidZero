package com.soaic.zero.mvp.callback;

public interface HttpCallBack<T> {
    void onSuccess(T t);
    void onFailed();

}
