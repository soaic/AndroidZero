package com.soaic.zero.lettersidebar;

import android.os.Bundle;
import android.view.View;

import com.soaic.widgetlibrary.skin.BaseSkinActivity;
import com.soaic.zero.R;

public class MainActivity extends BaseSkinActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.letterSideBar).setVisibility(View.VISIBLE);
    }


}