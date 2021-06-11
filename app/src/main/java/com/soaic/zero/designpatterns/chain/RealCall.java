package com.soaic.zero.designpatterns.chain;

import java.util.ArrayList;
import java.util.List;

public class RealCall {
    private final Request request;

    public RealCall(Request request) {
        this.request = request;
    }

    public Response execute() {
        return getResponseWithInterceptChain();
    }

    public void enqueue(final Callback callback) {
        if (callback == null) {
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = getResponseWithInterceptChain();
                    callback.onSuccess(response);
                } catch (Exception e) {
                    callback.onFail(e);
                }
            }
        }).start();
    }

    private Response getResponseWithInterceptChain() {
        List<Intercept> intercepts = new ArrayList<>();
        intercepts.add(new CacheIntercept());
        intercepts.add(new ConnectionIntercept());
        intercepts.add(new CallServerIntercept());
        Intercept.Chain chain = new RealInterceptChain(intercepts, request, 0);
        return chain.proceed(request);
    }

    public interface Callback {
        void onSuccess(Response response);
        void onFail(Throwable throwable);

    }
}
