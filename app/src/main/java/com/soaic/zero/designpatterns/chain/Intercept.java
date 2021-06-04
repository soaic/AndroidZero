package com.soaic.zero.designpatterns.chain;

public interface Intercept {

    Response intercept(Chain chain);

    interface Chain {
        Request getRequest();
        Response proceed(Request request);
    }

}
