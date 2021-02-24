package com.soaic.zero;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.soaic.widget.bannerview.BannerAdapter;
import com.soaic.widget.bannerview.BannerView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private BannerView bannerView;

    private String[] list = {
            "https://www.baidu.com/img/dong_8f1d47bcb77d74a1e029d8cbb3b33854.gif",
            "https://www.hualigs.cn/image/603600e6b6209.jpg"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        bannerView = findViewById(R.id.banner_view);
        bannerView.setAdapter(new BannerAdapter() {
            @Override
            public View getView(int position) {
                ImageView imageView = new ImageView(MainActivity.this);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                //imageView.setImageResource(R.drawable.banner1);
                Glide.with(MainActivity.this).load(list[position]).into(imageView);
                return imageView;
            }

            @Override
            public int getCount() {
                return list.length;
            }
        });

    }
}