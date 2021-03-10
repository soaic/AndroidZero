package com.soaic.widgetlibrary.recyclerview;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class RefreshV2RecyclerView extends WrapRecyclerView {

    // 默认状态
    private final int REFRESH_STATUS_NORMAL = 0x001;
    // 下拉状态
    private final int REFRESH_STATUS_PULL = 0x002;
    // 准备刷新
    private final int REFRESH_STATUS_READY_REFRESH = 0x003;
    // 刷新中
    private final int REFRESH_STATUS_REFRESHING = 0x004;
    // 当前状态
    private int mCurrentStatus;
    // 当前刷新创造器
    private RefreshViewCreator mRefreshViewCreator;
    // 是否添加过刷新头部
    private boolean isAddedRefreshHeader = false;
    // 当前刷新View
    private View mRefreshView;
    // 当前刷新View Height
    private int mRefreshViewHeight;
    // 是否下拉中
    private boolean mPulling;
    // 下拉时最后的距离屏幕顶端的Y坐标
    private float mLastY;
    // 下拉阻尼系数
    private final float mPullRatio = 0.35f;

    public RefreshV2RecyclerView(@NonNull Context context) {
        super(context);
    }

    public RefreshV2RecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RefreshV2RecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    /**
     * 设置Refresh创造器
     */
    public void setRefreshViewCreator(RefreshViewCreator refreshViewCreator) {
        this.mRefreshViewCreator = refreshViewCreator;
        addRefreshHeaderView();
    }

    /**
     * 添加Refresh头部
     */
    private void addRefreshHeaderView() {
        if (!isAddedRefreshHeader && getAdapter() != null && mRefreshViewCreator != null) {
            mRefreshView = mRefreshViewCreator.getView(getContext(), this);
            getAdapter().addHeaderView(mRefreshView);
            isAddedRefreshHeader = true;
        }
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        addRefreshHeaderView();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {




        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        return super.onInterceptTouchEvent(e);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {

        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:
                // 不处于下拉刷新的动作
                if (!mPulling) {
                    // 不能向上滑动
                    if (!canScrollUp()) {
                        // 下拉时最后一次距离屏幕顶端Y坐标
                        mLastY = e.getY();
                    } else {
                        // 可以向上滑则不处理
                        break;
                    }
                }

                // 获取下拉距离
                float distance = (e.getY() - mLastY) * mPullRatio + 0.5f;
                if (distance < 0) { break; }
                mPulling = true;
                setRefreshViewHeight((int) (distance));
                updatePullStatus();
                break;
            case MotionEvent.ACTION_UP:
                restoreStatus();
                break;
        }

        return super.onTouchEvent(e);
    }

    /**
     * 重置状态
     */
    private void restoreStatus() {
        if (mRefreshView == null) {return;}

        int finalHeight = 0;
        if (mCurrentStatus == REFRESH_STATUS_READY_REFRESH) {
            finalHeight = mRefreshViewHeight;
            mCurrentStatus = REFRESH_STATUS_REFRESHING;
        } else {
            mCurrentStatus = REFRESH_STATUS_NORMAL;
        }

        int currentHeight = mRefreshView.getMeasuredHeight() - mRefreshViewHeight;
        int distance = currentHeight - finalHeight;

        if (distance > 0) {
            ValueAnimator animator = ObjectAnimator.ofFloat(currentHeight, finalHeight)
                    .setDuration(distance);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float value = (float) animation.getAnimatedValue();
                    setRefreshViewHeight((int) value);
                }
            });
            animator.start();
        }
        mPulling = false;
    }

    /**
     * 设置下拉状态
     */
    private void updatePullStatus() {
        int distance = mRefreshView.getMeasuredHeight();
        Log.d("TAG", "distance="+(distance/2));
        Log.d("TAG", "mRefreshViewHeight="+mRefreshViewHeight);

        if (distance <= mRefreshViewHeight) {
            mCurrentStatus = REFRESH_STATUS_NORMAL;
        } else if (distance <= 2 * mRefreshViewHeight) {
            mCurrentStatus = REFRESH_STATUS_PULL;
        } else {
            mCurrentStatus = REFRESH_STATUS_READY_REFRESH;
        }

        Log.d("TAG", "mCurrentStatus"+mCurrentStatus);
    }

    /**
     * 能否向上滑动
     * @return true 能
     */
    private boolean canScrollUp() {
        return this.canScrollVertically(-1);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        if (changed) {
            if (mRefreshView != null && mRefreshViewHeight <= 0) {
                mRefreshViewHeight = mRefreshView.getMeasuredHeight();
                setRefreshMarginTop(-mRefreshViewHeight);
            }
        }
    }

    /**
     * 设置RefreshView margin top
     */
    private void setRefreshMarginTop(int marginTop) {
        if (mRefreshView == null) {return;}
        MarginLayoutParams refreshViewParams = (MarginLayoutParams)mRefreshView.getLayoutParams();
        refreshViewParams.topMargin = marginTop;
        mRefreshView.setLayoutParams(refreshViewParams);
    }

    /**
     * 设置 RefreshView 高度
     */
    private void setRefreshViewHeight(int distance) {
        if (mRefreshView == null) {return;}
        LayoutParams params = (LayoutParams) mRefreshView.getLayoutParams();
        params.height = mRefreshViewHeight + distance;
        mRefreshView.setLayoutParams(params);
    }
}
