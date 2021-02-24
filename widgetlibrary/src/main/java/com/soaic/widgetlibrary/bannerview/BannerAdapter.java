package com.soaic.widgetlibrary.bannerview;

import android.view.View;

public abstract class BannerAdapter{

    public abstract View getView(int position, View convertView);

    public abstract int getCount();
}
