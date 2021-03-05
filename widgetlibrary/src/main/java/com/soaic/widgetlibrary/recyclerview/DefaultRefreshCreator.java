package com.soaic.widgetlibrary.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

import androidx.recyclerview.widget.RecyclerView;

import com.soaic.widgetlibrary.R;

public class DefaultRefreshCreator extends RefreshViewCreator {
    // 加载数据的ImageView
    private View mRefreshIv;

    @Override
    public View getView(Context context, RecyclerView parent) {
        View refreshView = LayoutInflater.from(context).inflate(R.layout.layout_refresh_header_view, parent, false);
        mRefreshIv = refreshView.findViewById(R.id.refresh_iv);
        return refreshView;
    }

    @Override
    public void onPull(int currentDragHeight, int refreshViewHeight, int currentRefreshStates) {
        float rotate = ((float) currentDragHeight) / refreshViewHeight;
        mRefreshIv.setRotation(rotate * 360);
    }

    @Override
    public void onRefreshing() {
        // 刷新的时候不断旋转
        RotateAnimation animation = new RotateAnimation(0, 720,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setRepeatCount(-1);
        animation.setDuration(1000);
        mRefreshIv.startAnimation(animation);
    }

    @Override
    public void onStopRefresh() {
        // 停止加载的时候清除动画
        mRefreshIv.setRotation(0);
        mRefreshIv.clearAnimation();
    }
}
