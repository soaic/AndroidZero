package com.soaic.widgetlibrary.recyclerview;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class RefreshRecyclerView extends WrapRecyclerView {
    // 默认状态
    private static final int REFRESH_STATUS_NORMAL = 0X0011;
    // 下拉刷新状态
    private static final int REFRESH_STATUS_PULL_DOWN_REFRESH = 0X0033;
    // 松开刷新状态
    private static final int REFRESH_STATUS_LOOSEN_REFRESHING = 0X0033;
    // 刷新中状态
    private static final int REFRESH_STATUS_REFRESHING = 0X0044;
    // 当前刷新创造器
    private RefreshViewCreator mRefreshViewCreator;
    // 当前刷新View
    private View mRefreshView;
    // 手按下时Y的位置
    private int mFingerDownY;
    // 当前刷新状态
    private int mCurrentRefreshStatus;
    // 拖拽阻力系数
    private float mDragIndex = 0.35f;
    // 刷新View的高度
    private int mRefreshViewHeight;
    // 当前是否在拖拽
    private boolean mCurrentDrag;
    // 当前刷新接口
    private OnRefreshListener mOnRefresh;

    public RefreshRecyclerView(@NonNull Context context) {
        this(context, null);
    }

    public RefreshRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);


    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        addRefreshView();
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 记录手按下的位置 之所以写在这里，是因为如果我们处理了条目点击事件
                // 那么就不会进入onTouchEvent里面，所以只能在这里获取
                mFingerDownY = (int)ev.getY();
                break;
            case MotionEvent.ACTION_UP:
                if (mCurrentDrag) {
                    restoreRefreshView();
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 重置当前刷新状态
     */
    private void restoreRefreshView() {
        if (mRefreshView == null){
            return;
        }
        int currentTopMargin = ((MarginLayoutParams)mRefreshView.getLayoutParams()).topMargin;
        int finalTopMargin = -mRefreshViewHeight + 1;
        if (mCurrentRefreshStatus == REFRESH_STATUS_LOOSEN_REFRESHING) {
            finalTopMargin = 0;
            mCurrentRefreshStatus = REFRESH_STATUS_REFRESHING;
            if (mRefreshViewCreator != null) {
                mRefreshViewCreator.onRefreshing();
            }
            if (mOnRefresh != null) {
                mOnRefresh.onRefresh();
            }
        }

        int distance = currentTopMargin - finalTopMargin;
        if (distance > 0) {
            // 回弹到指定位置
            ValueAnimator animator = ObjectAnimator.ofFloat(currentTopMargin, finalTopMargin).setDuration(distance);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float value = (float) animation.getAnimatedValue();
                    setRefreshViewMarginTop((int) value);
                }
            });
            animator.start();
            mCurrentDrag = false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:
                // 如果能向上滚动说明没有到最顶端 或者 当前状态正在刷新
                if (canScrollUp() || mCurrentRefreshStatus == REFRESH_STATUS_REFRESHING) {
                    // 可以不处理 也就是说还可以向上滚动
                    return super.onTouchEvent(e);
                }

                // 获取手指拖拽距离
                int distanceY = (int) ((e.getRawY() - mFingerDownY) * mDragIndex);
                // 当前已经到达头部，并且不断向下拉，那么不断的改变refreshView的margin的值
                if (distanceY > 0) {
                    int marginTop = distanceY - mRefreshViewHeight;
                    setRefreshViewMarginTop(marginTop);
                    updateRefreshState(marginTop);
                    mCurrentDrag = true;
                }
                break;
        }
        return super.onTouchEvent(e);
    }

    /**
     * 更新刷新状态
     */
    private void updateRefreshState(int marginTop) {
        if (marginTop <= -mRefreshViewHeight) {
            mCurrentRefreshStatus = REFRESH_STATUS_NORMAL;
        } else if (marginTop < 0) {
            mCurrentRefreshStatus = REFRESH_STATUS_PULL_DOWN_REFRESH;
        } else {
            mCurrentRefreshStatus = REFRESH_STATUS_LOOSEN_REFRESHING;
        }

        if (mRefreshViewCreator != null) {
            mRefreshViewCreator.onPull(marginTop, mRefreshViewHeight, mCurrentRefreshStatus);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed) {
            if (mRefreshView != null && mRefreshViewHeight <= 0) {
                // 获取头部刷新View的高度
                mRefreshViewHeight = mRefreshView.getMeasuredHeight();
                if (mRefreshViewHeight > 0) {
                    // 隐藏头部刷新的View marginTop 多留出1px防止无法判断是不是滚动到头部问题
                    setRefreshViewMarginTop(-mRefreshViewHeight + 1);
                }
            }
        }
    }

    /**
     * 设置marginTop
     */
    private void setRefreshViewMarginTop(int marginTop) {
        if (mRefreshView == null){
            return;
        }
        MarginLayoutParams params = (MarginLayoutParams) mRefreshView.getLayoutParams();
        if (marginTop < -mRefreshViewHeight + 1) {
            marginTop = -mRefreshViewHeight + 1;
        }
        params.topMargin = marginTop;
        mRefreshView.setLayoutParams(params);
    }

    /**
     * 判断是不是滚动到了最顶部
     */
    private boolean canScrollUp() {
        // Negative to check scrolling up, positive to check scrolling down.
        // 负为检查向上，正为检查向下
        return this.canScrollVertically(-1);
    }

    /**
     * 添加刷新头部创造器
     */
    public void addRefreshViewCreator(RefreshViewCreator refreshViewCreator) {
        this.mRefreshViewCreator = refreshViewCreator;
        addRefreshView();
    }

    /**
     * 添加头部刷新View
     */
    private void addRefreshView() {
        RecyclerView.Adapter adapter = getAdapter();
        if (adapter != null && mRefreshViewCreator != null) {
            View refreshView = mRefreshViewCreator.getView(getContext(), this);
            if (refreshView != null) {
                addHeaderView(refreshView);
                this.mRefreshView = refreshView;
            }
        }
    }

    /**
     * 停止刷新
     */
    public void onStopRefresh() {
        mCurrentRefreshStatus = REFRESH_STATUS_NORMAL;
        restoreRefreshView();
        if (mRefreshViewCreator != null) {
            mRefreshViewCreator.onStopRefresh();
        }
    }

    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        this.mOnRefresh = onRefreshListener;
    }

    public interface OnRefreshListener {
        void onRefresh();
    }

}
