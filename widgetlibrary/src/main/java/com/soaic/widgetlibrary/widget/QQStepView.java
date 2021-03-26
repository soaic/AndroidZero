package com.soaic.widgetlibrary.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.Nullable;

import com.soaic.widgetlibrary.R;

public class QQStepView extends View {

    private int mOuterColor = Color.BLUE;
    private int mInnerColor = Color.RED;
    private int mBorderWidth = 20;
    private int mStepColor = Color.RED;
    private int mStepTextSize = dp2px(20);
    private Paint mOuterPaint;
    private Paint mInnerPaint;
    private Paint mTextPaint;
    private int mCurrentStep = 500;
    private int mMaxStep = 5000;
    private String TAG = "QQStepView";

    public QQStepView(Context context) {
        this(context , null);
    }

    public QQStepView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QQStepView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);


        init(context, attrs);
    }

    private void init(Context context, AttributeSet attr) {
        TypedArray array = context.obtainStyledAttributes(attr, R.styleable.QQStepView);
        mOuterColor = array.getColor(R.styleable.QQStepView_outerColor, mOuterColor);
        mInnerColor = array.getColor(R.styleable.QQStepView_innerColor, mInnerColor);
        mBorderWidth = (int) array.getDimension(R.styleable.QQStepView_borderWidth, mBorderWidth);
        mStepColor = array.getColor(R.styleable.QQStepView_stepTextColor, mStepColor);
        mStepTextSize = array.getDimensionPixelSize(R.styleable.QQStepView_stepTextSize, mStepTextSize);
        //mStepTextSize = dp2px(mStepTextSize);
        array.recycle();

        mOuterPaint = new Paint();
        mOuterPaint.setAntiAlias(true);
        mOuterPaint.setColor(mOuterColor);
        mOuterPaint.setStrokeWidth(mBorderWidth);
        mOuterPaint.setStyle(Paint.Style.STROKE); // 画笔空心
        mOuterPaint.setStrokeCap(Paint.Cap.ROUND); // 圆弧

        mInnerPaint = new Paint();
        mInnerPaint.setAntiAlias(true);
        mInnerPaint.setColor(mInnerColor);
        mInnerPaint.setStrokeWidth(mBorderWidth);
        mInnerPaint.setStyle(Paint.Style.STROKE); // 画笔实心
        mInnerPaint.setStrokeCap(Paint.Cap.ROUND);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(mStepColor);
        mTextPaint.setTextSize(mStepTextSize);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        setMeasuredDimension(Math.min(width, height), Math.min(width, height));
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int center = getWidth() / 2;
        int radius = getWidth() / 2 - mBorderWidth/2;

        // 1. 画外圆弧
        RectF rectF = new RectF(center-radius,center-radius,center+radius, center+radius);
        canvas.drawArc(rectF, 135, 270, false, mOuterPaint);
        // 2. 内圆弧
        RectF innerRectF = new RectF(center-radius,center-radius,center+radius, center+radius);
        float sweepAngle = mCurrentStep*1.0f/mMaxStep;
        canvas.drawArc(innerRectF, 135, 270 * sweepAngle, false, mInnerPaint);
        // 3. 画文字
        String stepText = mCurrentStep+"";
        Rect rect = new Rect();
        mTextPaint.getTextBounds(stepText, 0, stepText.length(), rect);
        int dx = getWidth()/2 - rect.width()/2;
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        float dy = (fontMetrics.bottom - fontMetrics.top)/2 - fontMetrics.bottom;
        int baseLine = (int) (getHeight()/2 + dy);
        canvas.drawText(stepText, dx, baseLine, mTextPaint);

    }

    public synchronized void setMaxStep(int maxStep) {
        this.mMaxStep = maxStep;
    }

    public synchronized void setCurrentStep(int currentStep) {
        this.mCurrentStep = currentStep;
        invalidate();
    }

    private int dp2px(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }
}
