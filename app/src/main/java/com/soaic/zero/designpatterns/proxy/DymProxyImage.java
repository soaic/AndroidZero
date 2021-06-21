package com.soaic.zero.designpatterns.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class DymProxyImage implements Image{
    private final Image image;
    public DymProxyImage(Image image) {
        this.image = image;
    }

    @Override
    public void display() {
        Image proxyImage = (Image) Proxy.newProxyInstance(DymProxyImage.class.getClassLoader(), new Class[]{Image.class}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                System.out.println("代理之前捕捉");
                Object invokeObject = method.invoke(image, args);
                System.out.println("代理之后捕捉");
                return invokeObject;
            }
        });
        proxyImage.display();
    }
}
