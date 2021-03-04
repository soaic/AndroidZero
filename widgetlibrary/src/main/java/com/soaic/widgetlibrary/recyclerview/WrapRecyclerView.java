package com.soaic.widgetlibrary.recyclerview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.soaic.widgetlibrary.recyclerview.adapter.WrapRecyclerAdapter;

public class WrapRecyclerView extends RecyclerView {
    private WrapRecyclerAdapter mWrapAdapter;
    private RecyclerView.Adapter mAdapter;

    public WrapRecyclerAdapter getAdapter() {
        return mWrapAdapter;
    }

    public WrapRecyclerView(@NonNull Context context) {
        this(context, null);
    }

    public WrapRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WrapRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        if (mAdapter != null) {
            mAdapter.unregisterAdapterDataObserver(mAdapterDataObserver);
            mAdapter = null;
        }
        this.mAdapter = adapter;
        if (adapter instanceof WrapRecyclerAdapter) {
            mWrapAdapter = (WrapRecyclerAdapter) adapter;
        } else {
            mWrapAdapter = new WrapRecyclerAdapter(adapter);
        }
        super.setAdapter(mWrapAdapter);

        // 注册一个观察者
        mAdapter.registerAdapterDataObserver(mAdapterDataObserver);

        // 解决GridLayout添加头部和底部也要占据一行
        mWrapAdapter.adjustSpanSize(this);
    }




    /**
     * 添加头部
     */
    public void addHeaderView(View view) {
        if (mWrapAdapter != null) {
            mWrapAdapter.addHeaderView(view);
        }
    }


    /**
     * 添加尾部
     */
    public void addFooterView(View view) {
        if (mWrapAdapter != null) {
            mWrapAdapter.addFooterView(view);
        }
    }

    /**
     * 移除头部
     */
    public void removeHeaderView(View view) {
        if (mWrapAdapter != null) {
            mWrapAdapter.removeHeaderView(view);
        }
    }

    /**
     * 移除尾部
     */
    public void removeFootedView(View view) {
        if (mWrapAdapter != null) {
            mWrapAdapter.removeFootedView(view);
        }
    }

    // 通过观察者模式对 WrapRecyclerAdapter 进行的数据刷新
    private final AdapterDataObserver mAdapterDataObserver = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            if (mWrapAdapter != null) {
                mWrapAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            if (mWrapAdapter != null) {
                mWrapAdapter.notifyItemRangeChanged(positionStart, itemCount);
            }
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, @Nullable Object payload) {
            if (mWrapAdapter != null) {
                mWrapAdapter.notifyItemRangeChanged(positionStart, itemCount, payload);
            }
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            if (mWrapAdapter != null) {
                mWrapAdapter.notifyItemRangeInserted(positionStart, itemCount);
            }
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            if (mWrapAdapter != null) {
                mWrapAdapter.notifyItemRangeRemoved(positionStart, itemCount);
            }
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            if (mWrapAdapter != null) {
                mWrapAdapter.notifyItemMoved(fromPosition, itemCount);
            }
        }
    };
}
