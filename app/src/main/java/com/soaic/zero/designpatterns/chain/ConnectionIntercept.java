package com.soaic.zero.designpatterns.chain;

public class ConnectionIntercept implements Intercept {

    @Override
    public Response intercept(Chain chain) {
        Request request = chain.getRequest();
        System.out.println("Connection after proceed");
        Response response = chain.proceed(request);
        System.out.println("Connection before proceed");
        return response;
    }
}
