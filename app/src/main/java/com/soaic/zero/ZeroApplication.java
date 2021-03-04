package com.soaic.zero;

import android.app.Application;

import com.soaic.widgetlibrary.AppEnv;

public class ZeroApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        AppEnv.init(getApplicationContext());
    }
}
