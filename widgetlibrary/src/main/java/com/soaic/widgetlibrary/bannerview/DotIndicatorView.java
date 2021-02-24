package com.soaic.widgetlibrary.bannerview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * 圆点View
 */
public class DotIndicatorView extends View {

    private Drawable drawable;

    public DotIndicatorView(Context context) {
        super(context);
    }

    public DotIndicatorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        if (drawable != null) {
            //把drawable转换成bitmap
            Bitmap bitmap = drawableToBitmap(drawable);

            //把bitmap变圆
            Bitmap cycleBitmap = getCycleBitmap(bitmap);

            //把圆形bitmap绘制到画布上
            canvas.drawBitmap(cycleBitmap, 0, 0, null);
        }
    }


    /**
     * 获取圆形bitmap
     */
    private Bitmap getCycleBitmap(Bitmap bitmap) {

        Bitmap cycleBitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas =  new Canvas(cycleBitmap);

        Paint paint = new Paint();
        //设置抗锯齿
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        //设置防抖动
        paint.setDither(true);

        //在画布上绘制圆
        canvas.drawCircle(getMeasuredWidth()/2.0f, getMeasuredHeight()/2.0f, getMeasuredWidth()/2.0f, paint);

        //取圆和bitmap的交集
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        //把bitmap画到canvas中
        canvas.drawBitmap(bitmap, 0,0, paint);

        bitmap.recycle();
        bitmap = null;

        return cycleBitmap;
    }


    /**
     * 把drawable转换成Bitmap
     */
    private Bitmap drawableToBitmap(Drawable drawable) {

        // 如果是BitmapDrawable类型
        if(drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        // 否则为其它类型 ColorDrawable
        Bitmap outBitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(outBitmap);
        // 要设置Bounds
        drawable.setBounds(0,0,getMeasuredWidth(), getMeasuredHeight());
        drawable.draw(canvas);

        return outBitmap;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
        invalidate();
    }
}
