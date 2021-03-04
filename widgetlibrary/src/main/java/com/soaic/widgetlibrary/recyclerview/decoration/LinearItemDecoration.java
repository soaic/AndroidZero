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
import androidx.recyclerview.widget.RecyclerView;

import com.soaic.widgetlibrary.AppEnv;

/**
 * 绘制 RecycleView Line
 * Simple: DividerItemDecoration.newBuilder().build()
 * created by soaic on 2019.11.29
 */
public class LinearItemDecoration extends RecyclerView.ItemDecoration {

    private Builder mBuilder;
    private final Rect mBounds = new Rect();

    private LinearItemDecoration(Builder builder) {
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
        int position = parent.getChildAdapterPosition(view);

        if (position >= mBuilder.startPosition) {
            outRect.bottom = mBuilder.dividerHeight;
        }
    }


    @Override
    public void onDraw(@NonNull Canvas canvas, RecyclerView parent, @NonNull RecyclerView.State state) {
        canvas.save();

        mBounds.left = parent.getPaddingLeft() + mBuilder.marginLeft;
        mBounds.right = parent.getWidth() - parent.getPaddingRight() - mBuilder.marginRight;

        final int childCount = parent.getChildCount() - (mBuilder.isDrawLastLine ? 0 : 1);
        for (int i = mBuilder.startPosition; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            mBounds.top = child.getBottom();
            mBounds.bottom = child.getBottom() + mBuilder.dividerHeight;
            mBuilder.dividerDrawable.setBounds(mBounds);
            mBuilder.dividerDrawable.draw(canvas);
        }
        canvas.restore();
    }

    public static class Builder {
        /** 分割线Drawable */
        private Drawable dividerDrawable = new ColorDrawable(Color.parseColor("#d8d8d8"));

        /** 分割线高度 */
        private int dividerHeight = dpToPx(0.5f);

        /** 距离左边 */
        private int marginLeft = 0;

        /** 距离右边 */
        private int marginRight = 0;

        /** 是否绘制最后一行 */
        private boolean isDrawLastLine = false;

        /** 开始绘制行 */
        private int startPosition = 0;


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

        public Builder setMarginLeft(int marginLeft) {
            this.marginLeft = dpToPx(marginLeft);
            return this;
        }

        public Builder setMarginRight(int marginRight) {
            this.marginRight = dpToPx(marginRight);
            return this;
        }

        public Builder setDrawLastLine(boolean drawLastLine) {
            isDrawLastLine = drawLastLine;
            return this;
        }

        public Builder setStartPosition(int startPosition) {
            this.startPosition = startPosition;
            return this;
        }

        public LinearItemDecoration build() {
            return new LinearItemDecoration(this);
        }
    }
}
