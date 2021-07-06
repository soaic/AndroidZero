package com.soaic.zero;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.soaic.widgetlibrary.AppEnv;
import com.soaic.zero.hookstartactivity.FixDexUtils;
import com.soaic.zero.hookstartactivity.HookStartActivityUtils;
import com.soaic.zero.hookstartactivity.TestActivity;

public class ZeroApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        AppEnv.init(getApplicationContext());

        FixDexUtils.fixDex(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        MultiDex.install(this);
    }
}
