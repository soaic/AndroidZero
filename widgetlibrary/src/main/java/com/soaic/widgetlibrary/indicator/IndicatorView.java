package com.soaic.widgetlibrary.indicator;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import androidx.viewpager.widget.ViewPager;

import com.soaic.widgetlibrary.R;

/**
 * 带滚动的通用指示器
 */
public class IndicatorView extends HorizontalScrollView implements ViewPager.OnPageChangeListener {

    private IndicatorAdapter mAdapter;
    private LinearLayout mIndicatorGroup;

    private int mVisibleNumbers = 0;
    private ViewPager mViewPager;
    private int mItemWidth;
    private int mCurrentItem = 0;

    public IndicatorView(Context context) {
        this(context, null);
    }

    public IndicatorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initAttribute(context, attrs);

        mIndicatorGroup = new LinearLayout(context);
        mIndicatorGroup.setOrientation(LinearLayout.HORIZONTAL);
        addView(mIndicatorGroup);
    }

    private void initAttribute(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.IndicatorView);
        mVisibleNumbers = typedArray.getInt(R.styleable.IndicatorView_visibleNumbers, mVisibleNumbers);
        typedArray.recycle();
    }

    public void setAdapter(IndicatorAdapter adapter) {
        if(adapter == null){
            throw new NullPointerException("adapter is null");
        }
        this.mAdapter = adapter;
        int count = mAdapter.getCount();
        for (int i = 0; i < count; i++) {
            View view = mAdapter.getView(i,this);
            switchClick(view, i);
            mIndicatorGroup.addView(view);
        }

        // 默认点亮第一个
        mAdapter.highIndicator(mIndicatorGroup.getChildAt(0));
    }

    private void switchClick(View view, final int position) {
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(position);
            }
        });
    }

    public void setAdapter(IndicatorAdapter adapter, ViewPager viewPager) {
        if (viewPager == null){
            throw new NullPointerException("viewPager is null");
        }
        this.mViewPager = viewPager;
        setAdapter(adapter);
        viewPager.addOnPageChangeListener(this);
    }

    public View getItem(int position) {
        return mIndicatorGroup.getChildAt(position);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        if (changed) {
            adapterItemWidth();
        }
    }

    /**
     * 适配item宽度
     */
    private void adapterItemWidth() {
        int width = getWidth();
        if (mVisibleNumbers > 0) {
            // 如果指定了visibleNumbers
            mItemWidth = width/mVisibleNumbers;
        } else {
            // 如果没指定
            for (int i = 0; i < mAdapter.getCount(); i++) {
                int childWidth = mIndicatorGroup.getChildAt(i).getMeasuredWidth();
                mItemWidth = Math.max(mItemWidth, childWidth);
            }
        }
        // 如果不足一屏那么平均分配
        int allItemWidth = mAdapter.getCount()* mItemWidth;
        if (allItemWidth<width) {
            mItemWidth = width/mAdapter.getCount();
        }

        // 给每个item设置宽度
        for (int i = 0; i < mAdapter.getCount(); i++) {
            ViewGroup.LayoutParams params = mIndicatorGroup.getChildAt(i).getLayoutParams();
            params.width = mItemWidth;
            mIndicatorGroup.getChildAt(i).setLayoutParams(params);
        }
    }

    private void indicatorScrollTo(int position, float positionOffset) {
        // 当前滚动的距离，包含超出屏幕部分
        int curScrollOffset = (int) ((position + positionOffset) * mItemWidth);
        // 当前item 距离左边父控件位置
        int originLeftOffset = (getWidth() - mItemWidth) / 2;
        int scrollToOffset = curScrollOffset - originLeftOffset;
        scrollTo(scrollToOffset, 0);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        indicatorScrollTo(position, positionOffset);
        mAdapter.onPageScrolled(position, positionOffset, positionOffsetPixels);
    }

    @Override
    public void onPageSelected(int position) {
        mAdapter.restoreIndicator(mIndicatorGroup.getChildAt(mCurrentItem));
        mCurrentItem = position;
        mAdapter.highIndicator(mIndicatorGroup.getChildAt(mCurrentItem));
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
