package com.icarbonx.icxsample;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.Random;

/**
 * Author:  Kevin Feng
 * Email:   fengfenkai@icarbonx.com
 * Date:    2018/2/2
 * Description:
 */
public class RandomPointView extends View {


    private Paint mPointPaint;

    private int [][] arry;

    private int viewHeight;

    private int viewWidth;

    public RandomPointView(Context context) {
        super(context);
        init();
    }

    public RandomPointView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RandomPointView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawPoints(canvas);
    }

    private void drawPoints(Canvas canvas) {
        for (int i = 0; i < 50; i++) {
            canvas.drawCircle(arry[i][0], arry[i][1], 10, mPointPaint);
            Log.e("xx", arry[i][0]+ " " + arry[i][1]);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewHeight = h;
        viewWidth = w;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measureWidth = measureWidth(widthMeasureSpec);
        int measureHeight = measureHeight(heightMeasureSpec);

        setMeasuredDimension(measureWidth, measureHeight);
    }

    private int measureWidth(int pWidthMeasureSpec) {
        int result = 0;

        int widthMode = MeasureSpec.getMode(pWidthMeasureSpec);
        int widthSize = MeasureSpec.getSize(pWidthMeasureSpec);

        switch (widthMode) {
            case MeasureSpec.AT_MOST:
            case MeasureSpec.EXACTLY:
                result = widthSize;
                break;
        }
        return result;
    }


    private int measureHeight(int pHeightMeasureSpec) {
        int result = 0;

        int heightMode = MeasureSpec.getMode(pHeightMeasureSpec);
        int heightSize = MeasureSpec.getSize(pHeightMeasureSpec);

        switch (heightMode) {
            case MeasureSpec.AT_MOST:
            case MeasureSpec.EXACTLY:
                result = heightSize;
                break;
        }

        return result;
    }


    private void init() {
        mPointPaint = new Paint();
        mPointPaint.setAntiAlias(true);// 抗锯齿
        mPointPaint.setDither(true);// 防抖动
        mPointPaint.setStyle(Paint.Style.FILL_AND_STROKE);// 画笔类型： STROKE空心 FILL实心 FILL_AND_STROKE用契形填充
        mPointPaint.setStrokeJoin(Paint.Join.ROUND);// 画笔接洽点类型
        mPointPaint.setStrokeCap(Paint.Cap.ROUND);// 画笔笔刷类型
        mPointPaint.setColor(Color.parseColor("#ffffff"));
        mPointPaint.setStrokeWidth(1);// 画笔笔刷宽度

//        arry = getArrayPoint();
    }

    private int [][] getArrayPoint() {
        Random random1 = new Random();
        Random random2 = new Random();
        int [][] arry = new int[50][2];
        for (int i = 0; i < 50; i ++) {
            arry[i][0] = (int)(random1.nextGaussian() * 500);
            arry[i][1] = (int)(random2.nextGaussian()* 500);
        }
        return arry;
    }

}
