package com.soaic.zero.hookstartactivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

/**
 * 无需在 AndroidManifest 中注册， 也可以startActivity
 */
public class HookStartActivityUtils {
    private String TAG = "HookActivityUtils";

    private Context mContext;
    private Class<?> mClass;
    private String EXTRA_ORIGIN_INTENT = "EXTRA_ORIGIN_INTENT";

    /**
     * @param mClass 代理 Activity Class, 需要在AndroidManifest.xml 中注册
     */
    public HookStartActivityUtils(Context mContext, Class<?> mClass) {
        this.mContext = mContext.getApplicationContext();
        this.mClass = mClass;
    }

    public void hookLaunchActivity() throws Exception {
        // 1. 获取ActivityThread sCurrentActivityThread
        // private static volatile ActivityThread sCurrentActivityThread;
        Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
        Field catField = activityThreadClass.getDeclaredField("sCurrentActivityThread");
        catField.setAccessible(true);
        Object catObj = catField.get(null);
        // 2. 拿到 mH 属性
        // final H mH = new H();
        Field mhField = activityThreadClass.getDeclaredField("mH");
        mhField.setAccessible(true);
        Handler mh = (Handler) mhField.get(catObj);
        // 3. 设置 Handler mCallback
        // final Callback mCallback;
        Field callbackField = Handler.class.getDeclaredField("mCallback");
        callbackField.setAccessible(true);
        callbackField.set(mh, new LaunchCallBack());
    }

    private class LaunchCallBack implements Handler.Callback {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            // 6.0 是 100，// msg.obj 是 ActivityClientRecord
            // 10.0 是159, // msg.obj 是 ClientTransaction
            if (msg.what == 159) {
                handlerLaunchActivity(msg.obj);
            }
            return false;
        }

        private void handlerLaunchActivity(Object obj) {
            try {
                // 从 ClientTransaction 中拿到 mActivityCallbacks 属性
                Class<?> clientTransactionClass = Class.forName("android.app.servertransaction.ClientTransaction");
                Field mActivityCallbacksField = clientTransactionClass.getDeclaredField("mActivityCallbacks");
                mActivityCallbacksField.setAccessible(true);
                // 从 obj 中获取 mActivityCallbacks的值
                List activityCallbacksList = (List) mActivityCallbacksField.get(obj);
                Class<?> launchActivityItemClass = Class.forName("android.app.servertransaction.LaunchActivityItem");
                if (activityCallbacksList != null) {
                    for (Object item : activityCallbacksList) {
                        // 循环判断是否是 LaunchActivityItem Class
                        if (item.getClass() == launchActivityItemClass) {
                            // 从 LaunchActivityItem 中拿到Intent
                            Field intentField = launchActivityItemClass.getDeclaredField("mIntent");
                            intentField.setAccessible(true);
                            Intent proxyIntent = (Intent) intentField.get(item);
                            if (proxyIntent != null) {
                                Intent originIntent = proxyIntent.getParcelableExtra(EXTRA_ORIGIN_INTENT);
                                if (originIntent != null) {
                                    // 替换为原始Intent
                                    intentField.set(item, originIntent);
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // hook
    public void hookStartActivity() throws Exception{
        // 1. 先获取 ActivityTaskManager 中 IActivityTaskManagerSingleton
        Class<?> atmClass = Class.forName("android.app.ActivityTaskManager");
        Field iatmsField  = atmClass.getDeclaredField("IActivityTaskManagerSingleton");
        iatmsField.setAccessible(true);
        Object Singleton = iatmsField.get(null);
        // 2 在调用 IActivityTaskManagerSingleton.get() 获取 Singleton
        Class<?> singletonClass = Class.forName("android.util.Singleton");
        // 3. 从Singleton中获取 mInstance 属性 拿到 IActivityTaskManager
        Field mInstanceFiled = singletonClass.getDeclaredField("mInstance");
        mInstanceFiled.setAccessible(true);
        Object iatmInstance = mInstanceFiled.get(Singleton);
        // 获取IActivityTaskManager class
        Class<?> iatmClass = Class.forName("android.app.IActivityTaskManager");
        // 动态代理，IActivityTaskManager
        iatmInstance = Proxy.newProxyInstance(HookStartActivityUtils.class.getClassLoader(),
                new Class[]{iatmClass},
                // InvocationHandler 执行者
                new StartActivityInvocationHandler(iatmInstance));

        // 重新设置 Singleton 中的 mInstance(IActivityTaskManager)
        mInstanceFiled.set(Singleton, iatmInstance);
    }

    // 动态代理执行
    private class StartActivityInvocationHandler implements InvocationHandler {
        private Object mObject;

        public StartActivityInvocationHandler(Object obj) {
            this.mObject = obj;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (method.getName().equals("startActivity")) {
                // 1. 拿到原始的 Intent
                Intent originIntent = (Intent) args[2];
                // 2. 用 ProxyActivity 替换原始 Intent
                Intent proxyIntent = new Intent(mContext, mClass);
                // 3. 把原始Intent 绑定到 ProxyActivity中
                proxyIntent.putExtra(EXTRA_ORIGIN_INTENT, originIntent);
                // 4. 替换原始的Intent
                args[2] = proxyIntent;
            }

            return method.invoke(mObject, args);
        }
    }


    // 兼容 AppCompatActivity
    public void hookAppPackageManager() throws Exception {
        // 继承自AppCompatActivity, 分析得知下面这行报错
        // final ActivityInfo info = pm.getActivityInfo( new ComponentName(mContext, mHost.getClass()), flags);
        // 查看 ApplicationPackageManager 源码得知,最终调用的是如下，报了 NameNotFoundException Name 找不到
        // ActivityInfo ai = mPM.getActivityInfo(className, updateFlagsForComponent(flags, userId, null), userId);
        // hook mPM(IPackageManager) 拦截 getActivityInfo方法 手动替换 className

        // 1. 通过ActivityThread getPackageManager 方法，获取 IPackageManager
        Class<?> atClass = Class.forName("android.app.ActivityThread");

//        Field sCurrentActivityThreadField = atClass.getDeclaredField("sCurrentActivityThread");
//        sCurrentActivityThreadField.setAccessible(true);
//        Object atThread = sCurrentActivityThreadField.get(null);

        Method getPackageManagerMethod = atClass.getMethod("getPackageManager");
        getPackageManagerMethod.setAccessible(true);
        Object ipmObj = getPackageManagerMethod.invoke(null);
        // 2. hook  IPackageManager
        Class<?> ipmInterface = Class.forName("android.content.pm.IPackageManager");
        Object proxy  = Proxy.newProxyInstance(HookStartActivityUtils.class.getClassLoader(),
                new Class[]{ipmInterface},
                new HandlerPackManager(ipmObj));
        // 3. 重新注入 IPackageManager
        Field sPackageManagerField = atClass.getDeclaredField("sPackageManager");
        sPackageManagerField.setAccessible(true);
        sPackageManagerField.set(null, proxy);

    }

    private class HandlerPackManager implements InvocationHandler {
        private Object mIpmObj;
        public HandlerPackManager(Object ipmObj) {
            this.mIpmObj = ipmObj;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            // ActivityInfo ai = mPM.getActivityInfo(className, updateFlagsForComponent(flags, userId, null), userId);
            // 如果是getActivityInfo方法，则替换第一个参数 className 为 代理className
            if (method.getName().equals("getActivityInfo")) {
                args[0] = new ComponentName(mContext, mClass);
            }
            return method.invoke(mIpmObj, args);
        }
    }
}
