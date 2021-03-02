package com.soaic.widgetlibrary.indicator;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

/**
 * 指示器ViewGroup
 */
public class IndicatorGroup extends FrameLayout {
    private final Context mContext;
    private LinearLayout mItemViewGroup;
    private View mBottomView;
    private int mItemWidth;
    private LayoutParams mBottomParams;
    private int mInitLeftMargin;

    public IndicatorGroup(Context context) {
        this(context, null);
    }

    public IndicatorGroup(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IndicatorGroup(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
    }


    /**
     * 添加每栏目的View
     */
    public void addItemView(View view) {
        if (mItemViewGroup == null) {
            mItemViewGroup = new LinearLayout(mContext);
            mItemViewGroup.setOrientation(LinearLayout.HORIZONTAL);
            mItemViewGroup.setPadding(0, dp2px(5), 0, dp2px(6));
            LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            super.addView(mItemViewGroup, params);
        }
        mItemViewGroup.addView(view);
    }

    /**
     * 获取某个位置的itemView
     */
    public View getItemChildAt(int i) {
        return mItemViewGroup.getChildAt(i);
    }

    /**
     * 设置底部View的高度
     */
    public void addBottomView(View view, int itemWidth) {
        if (view == null) {
            return;
        }
        this.mItemWidth = itemWidth;
        this.mBottomView = view;
        super.addView(mBottomView);
        mBottomParams = (LayoutParams) mBottomView.getLayoutParams();
        int width = mBottomParams.width;
        if (width == LayoutParams.MATCH_PARENT) {
            width = mItemWidth;
        }
        if (width < mItemWidth) {
            mInitLeftMargin = (mItemWidth - width) / 2;
        }
        mBottomParams.gravity = Gravity.BOTTOM;
        mBottomParams.leftMargin = mInitLeftMargin;
        mBottomParams.width = width;
    }

    /**
     * 设置底部view的left margin
     */
    public void setBottomMargin(int position, float positionOffset) {
        if (mBottomView == null) {
            return;
        }

        int leftMargin = (int) ((position + positionOffset) * mItemWidth) + mInitLeftMargin;
        final LayoutParams params = (LayoutParams) mBottomView.getLayoutParams();
        params.leftMargin = leftMargin ;
        mBottomView.setLayoutParams(params);
    }

    /**
     * 滚动底部指示器 点击移动带动画
     */
    public void scrollBottomTrack(int position) {
        if (mBottomView == null) {
            return;
        }
        int finalLeftMargin = mItemWidth * position + mInitLeftMargin;
        int currentLeftMargin = mBottomParams.leftMargin;
        int distance = finalLeftMargin - currentLeftMargin;

        // 带动画
        ValueAnimator animator = ObjectAnimator.ofFloat(currentLeftMargin, finalLeftMargin).setDuration((long) Math.abs(distance * 0.4));
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float currentLeftMargin = (float) animation.getAnimatedValue();
                mBottomParams.leftMargin = (int) currentLeftMargin;
                mBottomView.setLayoutParams(mBottomParams);
            }
        });
        animator.setInterpolator(new DecelerateInterpolator());
        animator.start();
    }

    private int dp2px(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }
}
