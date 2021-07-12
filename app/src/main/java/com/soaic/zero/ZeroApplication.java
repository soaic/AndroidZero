package com.soaic.zero;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.soaic.widgetlibrary.AppEnv;
import com.soaic.widgetlibrary.bannerview.DefaultActivityLifecycleCallbacks;
import com.soaic.zero.hookstartactivity.FixDexUtils;
import com.soaic.zero.hookstartactivity.FixResourceUtil;
import com.soaic.zero.hookstartactivity.HookStartActivityUtils;
import com.soaic.zero.hookstartactivity.TestActivity;

import java.util.ArrayList;
import java.util.List;

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
