package com.soaic.widgetlibrary.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class LetterSideBar extends View {

    private Paint mPaint;
    private int mTextSize = sp2px(12);
    private int mColor = Color.BLACK;
    private String[] mLetters = new String[] {
      "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
      "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
      "U", "V", "W", "X", "Y", "Z", "#"
    };
    private int curTouchIndex = 0;

    public LetterSideBar(Context context) {
        this(context, null);
    }

    public LetterSideBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LetterSideBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(mTextSize);
        mPaint.setColor(mColor);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 计算指定宽度 左右padding宽度 + 字母宽度
        int textWidth = (int) mPaint.measureText("A");
        int width = getPaddingLeft() + getPaddingRight() + textWidth;
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    private int sp2px(int sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp,
                getResources().getDisplayMetrics());
    }


    @Override
    protected void onDraw(Canvas canvas) {
        // 画26个字母
        int itemHeight = (getHeight() - getPaddingTop() - getPaddingBottom()) / mLetters.length;
        for (int i = 0; i < mLetters.length; i++) {
            int width = (int) mPaint.measureText(mLetters[i]);
            int dx = getWidth() / 2 - width / 2;
            Paint.FontMetricsInt fontMetrics = mPaint.getFontMetricsInt();
            int dy = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom;
            Rect rect = new Rect();
            mPaint.getTextBounds(mLetters[i], 0, mLetters[i].length(), rect);
            int baseLine = (itemHeight * i + getPaddingTop()) + rect.height() / 2 + dy;
            if (curTouchIndex == i) {
                mPaint.setColor(Color.RED);
            } else {
                mPaint.setColor(Color.BLACK);
            }
            canvas.drawText(mLetters[i], dx, baseLine, mPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                float curY = event.getY();
                int itemHeight = (getHeight() - getPaddingTop() - getPaddingBottom()) / mLetters.length;
                curTouchIndex = (int) (curY / itemHeight);
                if (curTouchIndex >= 0 && curTouchIndex < mLetters.length) {
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:

                break;
        }
        return true;
    }
}
