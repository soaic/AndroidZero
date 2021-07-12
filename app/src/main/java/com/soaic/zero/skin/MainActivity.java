package com.soaic.zero.skin;

import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;

import com.soaic.widgetlibrary.skin.BaseSkinActivity;
import com.soaic.zero.R;

import java.io.File;
import java.lang.reflect.Method;


public class MainActivity extends BaseSkinActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bannerViewTest();
    }

    private void bannerViewTest() {
        ImageView imageView = findViewById(R.id.testImage);
        imageView.setVisibility(View.VISIBLE);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skin();
            }
        });
    }

    private void skin() {
        try {
            Resources superRes = getResources();
            // 创建AssetManager，但是不能直接new所以只能通过反射
            AssetManager assetManager = AssetManager.class.newInstance();
            // 反射获取addAssetPath方法
            Method addAssetPathMethod = AssetManager.class.getDeclaredMethod("addAssetPath",String.class);
            // 皮肤包的路径:  本地sdcard/plugin.skin
            String skinPath = Environment.getExternalStorageDirectory().getAbsoluteFile()+ File.separator+"plugin.skin";
            // 反射调用addAssetPath方法
            addAssetPathMethod.invoke(assetManager, skinPath);
            // 创建皮肤的Resources对象
            Resources skinResources = new Resources(assetManager,superRes.getDisplayMetrics(),superRes.getConfiguration());
            // 通过资源名称,类型，包名获取Id
            int bgId = skinResources.getIdentifier("test","drawable","com.soaic.zero");
            Drawable bgDrawable = skinResources.getDrawable(bgId);
            // 设置背景
            findViewById(R.id.testImage).setBackground(bgDrawable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}