package com.soaic.zero.designpatterns.template;

public class MoreTemplate implements Template {
    @Override
    public void onCreate() {
        System.out.println("MoreTemplate onCreate");
    }

    @Override
    public void onResume() {
        System.out.println("MoreTemplate onResume");
    }

    @Override
    public void onPause() {
        System.out.println("MoreTemplate onPause");
    }

    @Override
    public void onDestroy() {
        System.out.println("MoreTemplate onDestroy");
    }
}
