package com.soaic.zero;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.LayoutInflaterCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.soaic.widgetlibrary.bannerview.BannerAdapter;
import com.soaic.widgetlibrary.bannerview.BannerView;
import com.soaic.widgetlibrary.skin.BaseSkinActivity;
import com.soaic.widgetlibrary.widget.QQStepView;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class MainActivity extends BaseSkinActivity {

    private static final String TAG = "MainActivity";
    private BannerView bannerView;

    private String[] list = {
            "https://dss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=2636368620,2367075435&fm=26&gp=0.jpg",
            "https://dss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=3588533707,3876173106&fm=26&gp=0.jpg"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    private void stepViewTest() {
        final QQStepView stepView = findViewById(R.id.stepView);
        stepView.setMaxStep(4000);
        ValueAnimator animation = ObjectAnimator.ofFloat(0, 3000).setDuration(1000);
        animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                stepView.setCurrentStep((int) value);
            }
        });
        animation.setInterpolator(new DecelerateInterpolator());
        animation.start();
    }

    private void bannerViewTest() {
        bannerView = findViewById(R.id.banner_view);
        bannerView.setAdapter(new BannerAdapter() {
            @Override
            public View getView(int position, View convertView) {
                ImageView imageView;
                if (convertView == null) {
                    imageView = new ImageView(MainActivity.this);
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                } else {
                    imageView = (ImageView) convertView;
                }
                Glide.with(MainActivity.this).load(list[position]).into(imageView);
                return imageView;
            }

            @Override
            public int getCount() {
                return list.length;
            }
        });
        findViewById(R.id.testImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skin();
            }
        });
        getPermission();
    }

    private void getPermission() {
        int result = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            // 如果授权了

        } else {
            // 未授权 去请求授权
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0x1001);
        }

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