package com.soaic.widgetlibrary.recyclerview.decoration;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.soaic.widgetlibrary.AppEnv;
import com.soaic.widgetlibrary.recyclerview.adapter.WrapRecyclerAdapter;

/**
 * 绘制 RecycleView Line
 * Simple: GridItemDecoration.newBuilder().build()
 * created by soaic on 2021.3.3
 */
public class GridItemDecoration extends RecyclerView.ItemDecoration {

    private Builder mBuilder;
    private final Rect mBounds = new Rect();

    private GridItemDecoration(Builder builder) {
        this.mBuilder = builder;
        if (mBuilder == null) {
            mBuilder = new Builder();
        }
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        outRect.bottom = mBuilder.dividerHeight;

        if (isLastColumn(view, parent)) {
            outRect.right = 0;
        } else {
            outRect.right = mBuilder.dividerHeight;
        }
    }

    @Override
    public void onDraw(@NonNull Canvas canvas, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        canvas.save();
        drawVertical(canvas, parent);
        drawHorizontal(canvas, parent);
        canvas.restore();
    }

    /**
     * 是否是最后一列
     */
    private boolean isLastColumn(View view, RecyclerView parent) {
        int spanCount = getSpanCount(parent);
        RecyclerView.Adapter adapter = parent.getAdapter();
        int position = parent.getChildLayoutPosition(view);
        if (adapter instanceof WrapRecyclerAdapter) {
            position = ((WrapRecyclerAdapter) adapter).getRealPosition(position);
        }
        if (isSingleLine(parent, position)) {
            return true;
        }
        return (position + 1) % spanCount == 0;
    }

    private int getSpanCount(RecyclerView parent) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            return ((GridLayoutManager) layoutManager).getSpanCount();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            return ((StaggeredGridLayoutManager) layoutManager).getSpanCount();
        } else {
            throw new UnsupportedOperationException("the GridDividerItemDecoration can only be used in " +
                    "the RecyclerView which use a GridLayoutManager or StaggeredGridLayoutManager");
        }
    }

    /**
     * 是否是单独一行
     */
    private boolean isSingleLine(RecyclerView parent, int position) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            return ((GridLayoutManager) layoutManager).getSpanSizeLookup().getSpanSize(position)
                    == ((GridLayoutManager) layoutManager).getSpanCount();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams)
                    parent.getChildAt(position).getLayoutParams();
            return params.isFullSpan();
        } else {
            throw new UnsupportedOperationException("the GridDividerItemDecoration can only be used in " +
                    "the RecyclerView which use a GridLayoutManager or StaggeredGridLayoutManager");
        }
    }

    private void drawHorizontal(Canvas canvas, RecyclerView parent) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            final View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            mBounds.left = child.getLeft() - params.leftMargin;
            mBounds.right = child.getRight() + params.rightMargin + mBuilder.dividerHeight;
            mBounds.top = child.getBottom() + params.bottomMargin;
            mBounds.bottom = mBounds.top + mBuilder.dividerHeight;
            mBuilder.dividerDrawable.setBounds(mBounds);
            mBuilder.dividerDrawable.draw(canvas);
        }
    }

    private void drawVertical(Canvas canvas, RecyclerView parent) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            final View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            mBounds.left = child.getRight() + params.rightMargin;
            mBounds.right = mBounds.left + mBuilder.dividerHeight ;
            mBounds.top = child.getTop() - params.topMargin;
            mBounds.bottom = child.getBottom() + params.bottomMargin + mBuilder.dividerHeight;
            mBuilder.dividerDrawable.setBounds(mBounds);
            mBuilder.dividerDrawable.draw(canvas);
        }
    }


    public static class Builder {
        /** 分割线Drawable */
        private Drawable dividerDrawable = new ColorDrawable(Color.parseColor("#d8d8d8"));

        /** 分割线高度 */
        private int dividerHeight = dpToPx(0.5f);

        private int dpToPx(float dp) {
            return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                    AppEnv.getContext().getResources().getDisplayMetrics());
        }

        public Builder setDividerColor(@ColorRes int dividerColor) {
            this.dividerDrawable = new ColorDrawable(ContextCompat.getColor(AppEnv.getContext(), dividerColor));
            return this;
        }

        public Builder setDividerDrawable(@DrawableRes int dividerDrawable) {
            this.dividerDrawable = ContextCompat.getDrawable(AppEnv.getContext(), dividerDrawable);
            return this;
        }

        public Builder setDividerHeight(float dividerHeight) {
            this.dividerHeight = dpToPx(dividerHeight);
            return this;
        }

        public GridItemDecoration build() {
            return new GridItemDecoration(this);
        }
    }
}
