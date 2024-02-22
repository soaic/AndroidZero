package com.soaic.zero.butterknife;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;

//import com.soaic.annotations.BindView;
import com.soaic.zero.R;


public class MainActivity extends Activity {


    //@BindView(R.id.textView1)
    TextView textView1;


    //@BindView(R.id.textView2)
    TextView textView2;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_butterknife);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
