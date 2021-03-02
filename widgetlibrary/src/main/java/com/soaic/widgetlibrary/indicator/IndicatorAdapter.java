package com.soaic.widgetlibrary.indicator;

import android.view.View;
import android.view.ViewGroup;

/**
 * indicator 适配器
 */
public abstract class IndicatorAdapter {

    public abstract View getView(int position, ViewGroup parent);

    public abstract int getCount();

    public void highIndicator(View view) {

    }

    public void restoreIndicator(View view) {

    }

    public View getBottomView() {
        return null;
    }

    public void onPageScrolled(int position, float positionOffset) {

    }
}
