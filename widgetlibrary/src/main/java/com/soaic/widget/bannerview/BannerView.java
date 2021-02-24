package com.soaic.widget.bannerview;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.viewpager.widget.ViewPager;

import com.soaic.widgetlibrary.R;

/**
 * 自定义头部bannerView，带底部指示器
 */
public class BannerView extends RelativeLayout {

    private static final String TAG = "BannerView";
    // BannerView内部ViewPage
    private BannerViewPage mBannerViewPage;
    // 圆点存放layout
    private LinearLayout mDotLayout;
    // BannerView的适配器
    private BannerAdapter mBannerAdapter;
    private Activity mActivity;
    // 圆点选中drawable
    private Drawable mIndicatorFocusDrawable;
    // 圆点默认drawable
    private Drawable mIndicatorNormalDrawable;
    // 当前位置
    private int mCurrentPosition = 0;
    // 圆点摆放位置
    private int mDotGravity = 1;
    // 圆点的间距
    private int mDotDistance = 8;
    // 圆点的大小
    private int mDotSize = 8;
    // 圆点指示器的背景
    private Drawable mIndicatorBg;
    // 整体宽高比
    private int mWidthProportion, mHeightProportion;

    public BannerView(Context context) {
        this(context, null);
    }

    public BannerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public BannerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        mActivity = (Activity) context;
        // 把layout绑定到view中
        inflate(context, R.layout.banner_layout, this);

        initAttribute(attrs);
        initView();
    }

    /**
     * 初始化自定义属性
     */
    private void initAttribute(AttributeSet attrs) {
        TypedArray array = mActivity.obtainStyledAttributes(attrs, R.styleable.BannerView);
        mIndicatorFocusDrawable = array.getDrawable(R.styleable.BannerView_dotFocusIndicatorView);
        if (mIndicatorFocusDrawable == null) {
            mIndicatorFocusDrawable = new ColorDrawable(Color.RED);
        }
        mIndicatorNormalDrawable = array.getDrawable(R.styleable.BannerView_dotNormalIndicatorView);
        if (mIndicatorNormalDrawable == null) {
            mIndicatorNormalDrawable = new ColorDrawable(Color.WHITE);
        }
        mDotGravity = array.getInteger(R.styleable.BannerView_dotGravity, mDotGravity);
        mDotDistance = (int) array.getDimension(R.styleable.BannerView_dotDistance, dp2px(mDotDistance));
        mDotSize = (int) array.getDimension(R.styleable.BannerView_dotSize, dp2px(mDotSize));
        mWidthProportion = array.getInteger(R.styleable.BannerView_widthProportion, mWidthProportion);
        mHeightProportion = array.getInteger(R.styleable.BannerView_heightProportion, mHeightProportion);
        mIndicatorBg = array.getDrawable(R.styleable.BannerView_dotIndicationBg);
        if (mIndicatorBg == null) {
            mIndicatorBg = new ColorDrawable(Color.WHITE);
        }

        array.recycle();
    }

    private void initView() {
        mBannerViewPage = findViewById(R.id.banner_vp);
        mDotLayout = findViewById(R.id.dot_layout);

        //监听页面改变
        mBannerViewPage.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                pageSelect(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state) {
                    case 1://开始滑动 停止滚动
                        mBannerViewPage.stopRoll();
                        break;
                    case 2://结束滑动 开始滚动
                        mBannerViewPage.startRoll();
                        break;
                }
            }
        });

        //设置点指示器的背景
        mDotLayout.setBackground(mIndicatorBg);

        //设置点的位置
        int gravity = getGravityFromDotGravity(mDotGravity);
        mDotLayout.setGravity(gravity);

        // 动态获取宽高
        post(new Runnable() {
            @Override
            public void run() {
                //设置宽高比
                if (mWidthProportion != 0 && mHeightProportion != 0) {
                    int width = getMeasuredWidth();
                    getLayoutParams().height = width * mHeightProportion / mWidthProportion;
                }
            }
        });
    }

    /**
     * 获取点的Gravity位置
     */
    private int getGravityFromDotGravity(int gravity) {
        int mGravity = 1;
        switch (gravity) {
            case 0:
                mGravity = Gravity.CENTER;
                break;
            case -1:
                mGravity = Gravity.LEFT;
                break;
            case 1:
                mGravity = Gravity.RIGHT;
                break;
        }
        return mGravity;
    }

    /**
     * 页面选中回调
     */
    private void pageSelect(int position) {
        //把选中的设置为默认
        DotIndicatorView oldIndicatorView = (DotIndicatorView) mDotLayout.getChildAt(mCurrentPosition);
        oldIndicatorView.setDrawable(mIndicatorNormalDrawable);

        //把当前位置点亮 position 为0-2的31次方
        mCurrentPosition = position % mBannerAdapter.getCount();
        DotIndicatorView currentIndicatorView = (DotIndicatorView) mDotLayout.getChildAt(mCurrentPosition);
        currentIndicatorView.setDrawable(mIndicatorFocusDrawable);
    }


    /**
     * 设置 bannerAdapter
     */
    public void setAdapter(BannerAdapter bannerAdapter) {
        this.mBannerAdapter = bannerAdapter;
        mBannerViewPage.setAdapter(bannerAdapter);

        initDotIndicator();

    }

    /**
     * 初始化指示器
     */
    private void initDotIndicator() {
        int dotCount = mBannerAdapter.getCount();

        for (int i = 0; i < dotCount; i++) {
            DotIndicatorView indicatorView = new DotIndicatorView(mActivity);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(mDotSize, mDotSize);
            layoutParams.leftMargin = mDotDistance;
            indicatorView.setLayoutParams(layoutParams);
            if (i == 0) {
                // 默认选中
                indicatorView.setDrawable(mIndicatorFocusDrawable);
            } else {
                // 未选中
                indicatorView.setDrawable(mIndicatorNormalDrawable);
            }
            mDotLayout.addView(indicatorView);
        }
    }

    private int dp2px(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }
}
