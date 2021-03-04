package com.soaic.widgetlibrary.recyclerview.adapter;

import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

public class WrapRecyclerAdapter extends RecyclerView.Adapter<ViewHolder>{

    // 数据列表adapter, 不包含头部
    private RecyclerView.Adapter<ViewHolder> mAdapter;

    private SparseArray<View> mHeaderViews, mFooterViews;

    private int HEADER_KEY = 10000000;
    private int FOOTER_KEY = 20000000;

    public WrapRecyclerAdapter(RecyclerView.Adapter<ViewHolder> adapter) {
        this.mAdapter = adapter;
        mHeaderViews = new SparseArray<>();
        mFooterViews = new SparseArray<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(isHeaderViewType(viewType)) {
            // 头部
            View view = mHeaderViews.get(viewType);
            return createHeaderFooterViewHold(view);
        } else if (isFooterViewType(viewType)) {
            // 尾部
            return createHeaderFooterViewHold(mFooterViews.get(viewType));
        }

        return mAdapter.onCreateViewHolder(parent, viewType);
    }

    private ViewHolder createHeaderFooterViewHold(View view) {
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // 头部和尾部不用
        if (isHeaderPosition(position) || isFooterPosition(position)) {
            return;
        }
        mAdapter.onBindViewHolder(holder, position - mHeaderViews.size());
    }



    @Override
    public int getItemViewType(int position) {
        // header
        if (isHeaderPosition(position)) {
            return mHeaderViews.keyAt(position);
        }

        // footer
        if (isFooterPosition(position)) {
            int index = position - mHeaderViews.size() - mAdapter.getItemCount();
            return mFooterViews.keyAt(index);
        }

        // center
        int index = position - mHeaderViews.size();
        return mAdapter != null ? mAdapter.getItemViewType(index) : 0;
    }

    @Override
    public int getItemCount() {
        return mAdapter.getItemCount() + mHeaderViews.size() + mFooterViews.size();
    }


    /**
     * 是否是头部 position
     */
    private boolean isHeaderPosition(int position) {
        return position < mHeaderViews.size();
    }

    /**
     * 是否是尾部 position
     */
    private boolean isFooterPosition(int position) {
        return position - mHeaderViews.size() - mAdapter.getItemCount() >= 0;
    }

    /**
     * 是否是头部 type
     */
    private boolean isHeaderViewType(int viewType) {
        return mHeaderViews.indexOfKey(viewType) >= 0;
    }

    /**
     * 是否是尾部 type
     */
    private boolean isFooterViewType(int viewType) {
        return mFooterViews.indexOfKey(viewType) >= 0;
    }

    /**
     * 添加头部
     */
    public void addHeaderView(View view) {
        if (mHeaderViews.indexOfValue(view) < 0) {
            mHeaderViews.put(HEADER_KEY++, view);
            notifyDataSetChanged();
        }
    }


    /**
     * 添加尾部
     */
    public void addFooterView(View view) {
        if (mFooterViews.indexOfValue(view) < 0) {
            mFooterViews.put(FOOTER_KEY++, view);
            notifyDataSetChanged();
        }
    }

    /**
     * 移除头部
     */
    public void removeHeaderView(View view) {
        int index = mHeaderViews.indexOfValue(view);
        if (index >= 0) {
            mHeaderViews.removeAt(index);
            notifyDataSetChanged();
        }
    }

    /**
     * 移除尾部
     */
    public void removeFootedView(View view) {
        int index = mFooterViews.indexOfValue(view);
        if (index >= 0) {
            mFooterViews.removeAt(index);
            notifyDataSetChanged();
        }
    }

    /**
     * 获取真实 position 去除头部和尾部
     * @param originPosition 原始position 包含头部和尾部
     * @return 真实position
     */
    public int getRealPosition(int originPosition) {
        if (isHeaderPosition(originPosition) || isFooterPosition(originPosition)){
            return originPosition;
        }
        return originPosition - mHeaderViews.size();
    }

    /**
     * 获取原始 position 包含头部和尾部
     * @param realPosition 真实position 不包含头部和尾部
     * @return 原始position
     */
    public int getOriginPosition(int realPosition) {
        return realPosition + mHeaderViews.size();
    }

    /**
     * 获取真实 adapter 去除头部和尾部
     */
    public RecyclerView.Adapter<ViewHolder> getRealAdapter() {
        return mAdapter;
    }


    /**
     * 解决GridLayoutManager添加头部和底部不占用一行的问题
     */
    public void adjustSpanSize(RecyclerView recycler) {
        if (recycler.getLayoutManager() instanceof GridLayoutManager) {
            final GridLayoutManager layoutManager = (GridLayoutManager) recycler.getLayoutManager();
            layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    boolean isHeaderOrFooter = isHeaderPosition(position) || isFooterPosition(position);
                    return isHeaderOrFooter ? layoutManager.getSpanCount() : 1;
                }
            });
        }
    }


    @Override
    public void onViewAttachedToWindow(@NonNull ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        int index = holder.getLayoutPosition();
        // StaggeredGridLayoutManager布局时 头部和尾部都占一行
        int viewType = getItemViewType(index);
        if (isHeaderViewType(viewType) || isFooterViewType(viewType)) {
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
            if (lp instanceof StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
                p.setFullSpan(true);
            }
        }
    }
}
