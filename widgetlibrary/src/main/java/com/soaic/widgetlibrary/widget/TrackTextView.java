package com.soaic.widgetlibrary.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.soaic.widgetlibrary.R;

/**
 * 带进度颜色变化的TextView
 */
public class TrackTextView extends AppCompatTextView {

    private static final String TAG = "TrackTextView";
    private Paint mChangePaint;
    private Paint mOriginPaint;
    private int mChangeColor;
    private final Context mContext;
    // 当前进度
    private float mCurrentProgress = 0.3f;
    // 朝向
    public Direction mDirection = Direction.LEFT_TO_RIGHT;
    // 朝向枚举
    public enum Direction{
        LEFT_TO_RIGHT, // 从左到右
        RIGHT_TO_LEFT // 从右到左
    }

    public TrackTextView(Context context) {
        this(context, null);
    }

    public TrackTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TrackTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initView(attrs);
        initPaint();
    }

    private void initView(AttributeSet attrs) {
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs,R.styleable.TrackTextView);
        mChangeColor = typedArray.getColor(R.styleable.TrackTextView_changeColor, getTextColors().getDefaultColor());
        typedArray.recycle();
    }

    private void initPaint() {
        mChangePaint = getPaintByColor(mChangeColor);
        mOriginPaint = getPaintByColor(getTextColors().getDefaultColor());
    }

    private Paint getPaintByColor(int color) {
        Paint paint = new Paint();
        //设置抗锯齿
        paint.setAntiAlias(true);
        //设置防抖动
        paint.setDither(true);
        paint.setColor(color);
        paint.setTextSize(getTextSize());
        return paint;
    }

    public void setChangeColor(@ColorInt int color) {
        this.mChangeColor = color;
    }

    /**
     * 设置进度
     */
    public void setProgress(float progress) {
        this.mCurrentProgress = progress;
        invalidate();
    }

    /**
     * 设置朝向
     */
    public void setDirection(Direction direction) {
        this.mDirection = direction;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        String text = getText().toString();
        if (!TextUtils.isEmpty(text)) {
            int width = getWidth();
            float middle = width * mCurrentProgress;
            if (mDirection == Direction.LEFT_TO_RIGHT) {
                // 绘制变色部分的文本
                drawText(text, 0, middle, mChangePaint, canvas);
                // 绘制不变色部分文本
                drawText(text, middle, width, mOriginPaint, canvas);
            } else if (mDirection == Direction.RIGHT_TO_LEFT){
                drawText(text, width - middle, width, mChangePaint, canvas);
                drawText(text, 0, width - middle, mOriginPaint, canvas);
            }
        }
    }

    private void drawText(String text, float startX, float endX, Paint paint, Canvas canvas) {
        canvas.save();
        // 设置画布绘制区域
        canvas.clipRect(startX, 0, endX, getHeight());
        // 获取FontMetrics用来获取基线
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        // fontMetrics.bottom 为基线到底部的距离 为正数
        // fontMetrics.top 为基线到顶部的距离 为负数
        // 中心线到基线的距离
        float dy = (fontMetrics.bottom-fontMetrics.top)/2 - fontMetrics.bottom;
        // 基线 = 中心线 + 中心线到基线的距离
        float baseLine = dy + getMeasuredHeight()/2.0f;
        // 获取文字矩形区域大小
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        // 开始绘制文字位置
        float textStartX = getMeasuredWidth()/2.0f - bounds.width()/2.0f;
        canvas.drawText(text, textStartX, baseLine, paint);
        canvas.restore();
    }


}
