package com.soaic.zero.designpatterns.template;

public class MainTemplate implements Template {

    @Override
    public void onCreate() {
        System.out.println("MainTemplate onCreate");
    }

    @Override
    public void onResume() {
        System.out.println("MainTemplate onResume");
    }

    @Override
    public void onPause() {
        System.out.println("MainTemplate onPause");
    }

    @Override
    public void onDestroy() {
        System.out.println("MainTemplate onDestroy");
    }

}
