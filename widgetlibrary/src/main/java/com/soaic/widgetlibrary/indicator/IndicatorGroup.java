package com.soaic.widgetlibrary.indicator;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

/**
 * 指示器ViewGroup
 */
public class IndicatorGroup extends FrameLayout {
    private LinearLayout mItemViewGroup;
    private View mBottomView;

    public IndicatorGroup(Context context) {
        this(context, null);
    }

    public IndicatorGroup(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IndicatorGroup(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mItemViewGroup = new LinearLayout(context);
        mItemViewGroup.setOrientation(LinearLayout.HORIZONTAL);
        mItemViewGroup.setPadding(0, 6, 0,8);
        FrameLayout.LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        addView(mItemViewGroup, params);

        mBottomView = new View(context);
        mBottomView.setBackgroundColor(Color.RED);
        FrameLayout.LayoutParams bottomParams = new LayoutParams(LayoutParams.MATCH_PARENT, 6);

        bottomParams.gravity = Gravity.BOTTOM;
        addView(mBottomView, bottomParams);
    }


    /**
     * 添加每栏目的View
     */
    public void addItemView(View view) {
        mItemViewGroup.addView(view);
    }

    /**
     * 获取某个位置的itemView
     */
    public View getItemChildAt(int i) {

        return mItemViewGroup.getChildAt(i);
    }

    public void smoothScroll() {
        
    }

    /**
     * 设置底部View的高度
     */
    public void setBottomViewWidth(int width) {
        LayoutParams params = (LayoutParams) mBottomView.getLayoutParams();
        params.width = width;
    }

    /**
     * 设置底部view的left margin
     */
    public void setBottomMargin(int leftMargin) {
        final LayoutParams params = (LayoutParams) mBottomView.getLayoutParams();
        params.leftMargin = leftMargin;
        mBottomView.setLayoutParams(params);
    }
}
