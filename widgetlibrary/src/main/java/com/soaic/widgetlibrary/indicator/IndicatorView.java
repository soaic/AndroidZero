package com.soaic.widgetlibrary.indicator;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;

import androidx.viewpager.widget.ViewPager;

import com.soaic.widgetlibrary.R;

/**
 * 带滚动的通用指示器
 */
public class IndicatorView extends HorizontalScrollView implements ViewPager.OnPageChangeListener {

    private IndicatorAdapter mAdapter;
    private final IndicatorGroup mIndicatorGroup;

    private int mVisibleNumbers = 0;
    private ViewPager mViewPager;
    private int mItemWidth;
    private int mCurrentItem = 0;
    // 解决点击抖动问题
    private boolean mIsExecuteScroll = false;
    // ViewPage切换页面是否滚动
    private boolean mSmoothScroll;

    public IndicatorView(Context context) {
        this(context, null);
    }

    public IndicatorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttribute(context, attrs);

        mIndicatorGroup = new IndicatorGroup(context);
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
            mIndicatorGroup.addItemView(view);
        }

        // 默认点亮第一个
        mAdapter.highIndicator(mIndicatorGroup.getItemChildAt(0));

    }

    public void setAdapter(IndicatorAdapter adapter, ViewPager viewPager) {
        if (viewPager == null){
            throw new NullPointerException("viewPager is null");
        }
        this.mViewPager = viewPager;
        setAdapter(adapter);
        viewPager.addOnPageChangeListener(this);
    }

    public void setAdapter(IndicatorAdapter adapter, ViewPager viewPager, boolean smoothScroll) {
        this.mSmoothScroll = smoothScroll;
        setAdapter(adapter, viewPager);
    }

    private void switchClick(View view, final int position) {
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mViewPager !=null) {
                    mViewPager.setCurrentItem(position, mSmoothScroll);
                }
                // IndicatorItem对应滚动到最中心
                indicatorSmoothScrollTo(position);
                // 移动下标
                mIndicatorGroup.scrollBottomTrack(position);
            }
        });
    }

    public View getItem(int position) {
        return mIndicatorGroup.getItemChildAt(position);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        if (changed) {
            adapterItemWidth();
            // 添加指示器
            mIndicatorGroup.addBottomView(mAdapter.getBottomView(), mItemWidth);
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
                int childWidth = mIndicatorGroup.getItemChildAt(i).getMeasuredWidth();
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
            ViewGroup.LayoutParams params = mIndicatorGroup.getItemChildAt(i).getLayoutParams();
            params.width = mItemWidth;
            mIndicatorGroup.getItemChildAt(i).setLayoutParams(params);
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

    /**
     * 点击移动 带动画
     */
    private void indicatorSmoothScrollTo(int position) {
        // 当前的偏移量
        int currentOffset =  ((position) * mItemWidth);
        // 原始的左边的偏移量
        int originLeftOffset = (getWidth()-mItemWidth)/2;
        // 当前应该滚动的位置
        int scrollToOffset = currentOffset - originLeftOffset;
        // smoothScrollTo
        smoothScrollTo(scrollToOffset,0);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (mIsExecuteScroll) {
            indicatorScrollTo(position, positionOffset);
            mAdapter.onPageScrolled(position, positionOffset);
            mIndicatorGroup.setBottomMargin(position, positionOffset);
        }
    }

    @Override
    public void onPageSelected(int position) {
        mAdapter.restoreIndicator(mIndicatorGroup.getItemChildAt(mCurrentItem));
        mCurrentItem = position;
        mAdapter.highIndicator(mIndicatorGroup.getItemChildAt(mCurrentItem));
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if(state == 1) {
            mIsExecuteScroll = true;
        }
        if (state == 0) {
            mIsExecuteScroll = false;
        }
    }
}
