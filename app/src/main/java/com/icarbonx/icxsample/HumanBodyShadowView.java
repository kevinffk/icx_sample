package com.icarbonx.icxsample;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.icarbonx.icxsample.utils.MySystemParams;

/**
 * Author:  Kevin Feng
 * Email:   fengfenkai@icarbonx.com
 * Date:    2018/2/5
 * Description:
 */
public class HumanBodyShadowView extends View {

    private static final float HUMAN_SCALE = 0.62f;

    private Bitmap mHumanFlashBmp;

    private int viewHeight;

    private int viewWidth;

    private boolean isMeasure = false;

    public HumanBodyShadowView(Context context) {
        super(context);
    }

    public HumanBodyShadowView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public HumanBodyShadowView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mHumanFlashBmp, 0, 0, null);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mHumanFlashBmp = zoomImg(R.mipmap.human_body_flash);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (!isMeasure) {
            initHumanSize();
            isMeasure = true;
        }
        setMeasuredDimension(viewWidth, viewHeight);
    }

    private void initHumanSize() {
        mHumanFlashBmp = BitmapFactory.decodeResource(getResources(), R.mipmap.human_body);
        viewHeight = (int) (MySystemParams.getInstance(getContext()).screenHeight * HUMAN_SCALE);

        viewWidth = (int) ((float) (mHumanFlashBmp.getWidth()) / mHumanFlashBmp.getHeight() * viewHeight);
    }

    private Bitmap zoomImg(int bmpRes) {
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), bmpRes);

        // 获得图片的宽高
        int bmpWidth = bmp.getWidth();
        int bmpHeight = bmp.getHeight();

        float bmpDiv = ((float) bmpWidth) / bmpHeight;
        float viewDiv = ((float) viewWidth) / viewHeight;

        float scaleSize;
        if (viewDiv > bmpDiv) {
            scaleSize = ((float) viewHeight) / bmpHeight;
        } else {
            scaleSize = ((float) viewWidth) / bmpWidth;
        }

        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleSize, scaleSize);

        // 得到新的图片
        Bitmap newbm = Bitmap.createBitmap(bmp, 0, 0, bmpWidth, bmpHeight, matrix, true);
        bmp.recycle();
        return newbm;
    }
}
