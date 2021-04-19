package com.soaic.zero.bannerview;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.soaic.widgetlibrary.bannerview.BannerAdapter;
import com.soaic.widgetlibrary.bannerview.BannerView;
import com.soaic.widgetlibrary.skin.BaseSkinActivity;
import com.soaic.zero.R;

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
        bannerViewTest();
    }

    private void bannerViewTest() {
        bannerView = findViewById(R.id.banner_view);
        bannerView.setVisibility(View.VISIBLE);
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

    }
}