package com.example.common;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.hardware.biometrics.BiometricManager;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.annotation.Nullable;

public class RoundImageView extends ImageView {

    private Bitmap bitmap;
    private Paint paint;

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public RoundImageView(Context context) {
        super(context);
    }

    public RoundImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);
        if (bitmap == null || bitmap.isRecycled()) {
            xfermodRound(canvas);
        }
    }

    //离屏混合模式实现
    @TargetApi(21)
    private void xfermodRound(Canvas canvas) {
        //图层：离屏绘制，restore后才将内容显示到屏幕上
        canvas.saveLayer(0, 0, getWidth(), getHeight(), paint);
        //狂傲自己制定
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, getHeight() / 2, paint);
        //设置混合模式为SRC_IN
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, null, new Rect(0, 0, getWidth(), getHeight()), paint);
        canvas.restore();
    }
}
