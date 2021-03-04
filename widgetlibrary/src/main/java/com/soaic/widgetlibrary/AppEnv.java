package com.soaic.widgetlibrary;

import android.content.Context;

public class AppEnv {

    private static Context mAppContext;

    public static void init(Context context){
        mAppContext = context;
    }

    public static Context getContext() {
        checkEnv();
        return mAppContext;
    }

    private static void checkEnv() {
        if (mAppContext == null) {
            throw new RuntimeException("need invoke AppEnv.init() in your Application.onCrate()");
        }
    }
}
