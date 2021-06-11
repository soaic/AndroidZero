package com.soaic.zero.designpatterns.chain;

public class CacheIntercept implements Intercept {

    @Override
    public Response intercept(Chain chain) {
        Request request = chain.getRequest();
        System.out.println("Cache after proceed");
        Response response = chain.proceed(request);
        System.out.println("Cache before proceed");
        return response;
    }
}
