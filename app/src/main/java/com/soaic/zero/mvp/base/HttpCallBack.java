package com.soaic.zero.mvp.base;

public interface HttpCallBack<T> {
    void onSuccess(T t);
    void onFailed();

}
