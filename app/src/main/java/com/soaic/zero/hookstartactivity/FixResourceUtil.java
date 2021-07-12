package com.soaic.zero.hookstartactivity;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Build;
import android.os.Environment;
import android.util.ArrayMap;

import java.io.File;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * 热修复-资源修复
 * 测试把 Apk 放到某个目录
 */
public class FixResourceUtil {

    public static void fixResource(Context context, String resourcePath, List<Activity> activityList) {
        try {

            File dexFile = context.getExternalCacheDir();
            if (!dexFile.exists()) {
                dexFile.mkdirs();
            }
            // 下行代码测试用
            // resourcePath = dexFile.getAbsolutePath() + File.separator + "plugin.rs";
            if (!new File(resourcePath).exists()){
                return;
            }
            // 1. 创建一个 AssetManager 通过 addAssetPath 方法添加 Patch
            // 创建AssetManager，但是不能直接new所以只能通过反射
            AssetManager assetManager = AssetManager.class.newInstance();
            // 反射获取addAssetPath方法
            Method addAssetPathMethod = AssetManager.class.getDeclaredMethod("addAssetPath", String.class);
            // 3. 找到所有之前引用到的 AssetManager的地方，通过反射把引用替换为新的AssetManager
            replaceAssetsManager(activityList, assetManager);
            // 4. 加载修复包的路径:  本地sdcard/plugin.skin
            addAssetPathMethod.invoke(assetManager, resourcePath);
        } catch (Exception ignore) {

        }
    }

    private static void replaceAssetsManager(List<Activity> activityList, AssetManager assetManager) throws Exception {
        if (activityList != null) {
            for (Activity activity : activityList) {
                Resources resources = activity.getResources();
                try {
                    Field mAssets = Resources.class.getDeclaredField("mAssets");
                    mAssets.setAccessible(true);
                    mAssets.set(resources, assetManager);
                } catch (Throwable ignore) {
                    // 如果从 Resources 中拿不到 mAssets 属性，则从 mResourcesImpl 中拿
                    Field mResourcesImpl = Resources.class.getDeclaredField("mResourcesImpl");
                    mResourcesImpl.setAccessible(true);
                    Object resourcesImpl = mResourcesImpl.get(resources);
                    Field mAssets = resourcesImpl.getClass().getDeclaredField("mAssets");
                    mAssets.setAccessible(true);
                    mAssets.set(resourcesImpl, assetManager);

                }
            }
        }
    }

}
