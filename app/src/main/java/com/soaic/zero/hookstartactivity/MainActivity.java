package com.soaic.zero.hookstartactivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.soaic.widgetlibrary.bannerview.BannerAdapter;
import com.soaic.widgetlibrary.bannerview.BannerView;
import com.soaic.widgetlibrary.skin.BaseSkinActivity;
import com.soaic.zero.R;

public class MainActivity extends BaseSkinActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button fixText = findViewById(R.id.fixText);
        fixText.setVisibility(View.VISIBLE);
        fixText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = 6 / 1;
                Toast.makeText(MainActivity.this, "修复成功 count= " + count, Toast.LENGTH_SHORT).show();
            }
        });


    }


}