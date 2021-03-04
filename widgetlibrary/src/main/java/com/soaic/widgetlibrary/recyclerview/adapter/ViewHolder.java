package com.soaic.widgetlibrary.recyclerview.adapter;

import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder extends RecyclerView.ViewHolder {

    private SparseArray<View> mViews;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        mViews = new SparseArray<>();
    }


    public <T extends View> T getView(int viewId) {
        // 从缓存中找
        View view = mViews.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            // 没有则findViewById 再放入缓存
            mViews.put(viewId, view);
        }
        return (T)view;
    }

}
