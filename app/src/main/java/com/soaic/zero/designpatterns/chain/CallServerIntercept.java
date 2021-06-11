package com.soaic.zero.designpatterns.chain;

public class CallServerIntercept implements Intercept {

    @Override
    public Response intercept(Chain chain) {
        Request request = chain.getRequest();
        System.out.println("CallServer after proceed");
        Response response = new Response();
        response.body = "success";
        System.out.println("CallServer before proceed");
        return response;
    }
}
