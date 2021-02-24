package com.soaic.widgetlibrary.bannerview;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 自动循环滚动的头部横幅ViewPage
 */
public class BannerViewPage extends ViewPager {
    private static final String TAG = "BannerViewPage";

    private static final int SCROLL_MSG = 0x0011;
    private Handler mHandler;
    private long mScrollTime = 3500;
    private BannerAdapter mAdapter;
    private BannerScroller mScroller;
    private List<View> convertViews;
    // 是否在自动滚动
    private boolean isAutoRoll;


    public BannerViewPage(@NonNull Context context) {
        this(context, null);
    }

    public BannerViewPage(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        try {
            // 获取私有属性mScroller
            Field field = ViewPager.class.getDeclaredField("mScroller");
            // 设置为强制改变private
            field.setAccessible(true);
            mScroller = new BannerScroller(context);
            // 把私有属性替换为自定义的BannerScroller
            field.set(this, mScroller);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        init();
    }

    private void init() {
        convertViews = new ArrayList<>();
        mHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                setCurrentItem(getCurrentItem() + 1);
                startRoll();
                return false;
            }
        });

        startRoll();
    }

    /**
     * 是否在自动滚动
     * @return
     */
    public boolean isAutoRoll(){
        return isAutoRoll;
    }

    /**
     * 开始自动滚动
     */
    public void startRoll() {
        isAutoRoll = true;
        mHandler.removeMessages(SCROLL_MSG);
        mHandler.sendEmptyMessageDelayed(SCROLL_MSG, mScrollTime);
        Log.d(TAG, "startRoll");
    }

    /**
     * 停止自动滚动
     */
    public void stopRoll() {
        isAutoRoll = false;
        mHandler.removeMessages(SCROLL_MSG);
        Log.d(TAG, "stopRoll");
    }

    @Override
    protected void onDetachedFromWindow() {
        // 移除handler消息，防止activity销毁后还会继续循环执行
        stopRoll();
        super.onDetachedFromWindow();
    }

    public void setAdapter(@Nullable BannerAdapter adapter) {
        this.mAdapter = adapter;
        setAdapter(new BannerPagerAdapter());
    }

    /**
     * 设置页面动画持续时间
     */
    public void setScrollDuration(int duration) {
        mScroller.setScrollDuration(duration);
    }

    private class BannerPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            // 设置一个很大的值，确保可以无限轮播
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            // 固定这样写，看源码可知
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            View contentView = mAdapter.getView(position%mAdapter.getCount(), getConvertView());
            container.addView(contentView);
            return contentView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
            convertViews.add((View)object);
            object = null;
        }
    }

    /**
     * 获取转换View 用于view复用
     */
    private View getConvertView() {
        for (int i = 0; i <convertViews.size(); i++) {
            if(convertViews.get(i).getParent() == null){
                return convertViews.get(i);
            }
        }
        return null;
    }
}
