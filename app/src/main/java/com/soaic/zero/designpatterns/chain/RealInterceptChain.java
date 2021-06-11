package com.soaic.zero.designpatterns.chain;

import java.util.List;

public class RealInterceptChain implements Intercept.Chain{

    private final int index;
    private final List<Intercept> intercepts;
    private final Request request;

    public RealInterceptChain(List<Intercept> intercepts, Request request, int index) {
        this.intercepts = intercepts;
        this.request = request;
        this.index = index;
    }


    @Override
    public Request getRequest() {
        return request;
    }

    @Override
    public Response proceed(Request request) {
        if (index >= intercepts.size()) {
            throw new RuntimeException("index error");
        }
        RealInterceptChain next = new RealInterceptChain(intercepts, request, index + 1);

        Intercept intercept = intercepts.get(index);
        return intercept.intercept(next);
    }


}
