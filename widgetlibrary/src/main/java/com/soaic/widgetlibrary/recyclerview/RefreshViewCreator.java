package com.soaic.widgetlibrary.recyclerview;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public abstract class RefreshViewCreator {

    /**
     * 获取刷新View
     */
    public abstract View getView(Context context, RecyclerView parent);

    /**
     * 正在下拉
     * @param currentDragHeight 当前拖拽的高度
     * @param refreshViewHeight 刷新view的高度
     * @param currentRefreshStates 当前刷新的状态
     */
    public abstract void onPull(int currentDragHeight, int refreshViewHeight, int currentRefreshStates);

    /**
     * 刷新中
     */
    public abstract void onRefreshing();

    /**
     * 停止刷新
     */
    public abstract void onStopRefresh();

}
