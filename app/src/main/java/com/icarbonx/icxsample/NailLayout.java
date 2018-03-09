package com.icarbonx.icxsample;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.widget.FrameLayout;

/**
 * Author:  Kevin Feng
 * Email:   fengfenkai@icarbonx.com
 * Date:    2018/2/22
 * Description:
 */
public class NailLayout extends FrameLayout {

    /**
     * Const.
     */
    private static final int NAIL_DEFAULT_STOCK_COLOR = 0xffffff;
    private static final int NAIL_DEFAULT_BACKGROUND_COLOR = 0x000000;
    private static final int NAIL_DEFAULT_SIZE = 50;

    private int mNailStockColor = 0x000000;
    private int mNailBackgroundColor = 0xffffff;
    private float mNailSize = 50;
    private boolean mNailIsLeft = true;


    private Paint mNailPaint;
    private Paint mNailBoundPaint;
    private Path mNailPath;



    public NailLayout(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public NailLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public NailLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.NailLayout);
            mNailStockColor = typedArray.getColor(R.styleable.NailLayout_nailStockColor, NAIL_DEFAULT_STOCK_COLOR);
            mNailBackgroundColor = typedArray.getColor(R.styleable.NailLayout_nailBackgroundColor, NAIL_DEFAULT_BACKGROUND_COLOR);
            mNailSize = typedArray.getDimension(R.styleable.NailLayout_nailSize, NAIL_DEFAULT_SIZE);
            mNailIsLeft = typedArray.getBoolean(R.styleable.NailLayout_nailIsLeft, true);
            typedArray.recycle();
        }

        mNailPaint = new Paint();
        mNailPaint.setAntiAlias(true); //设置抗锯齿，为了文字能清晰显示不至于很模糊
        mNailPaint.setStrokeWidth(1); //设置画笔宽度，单位px
        mNailPaint.setColor(mNailBackgroundColor); //设置外圆画笔颜色
        mNailPaint.setStyle(Paint.Style.FILL);
        mNailPaint.setStrokeCap(Paint.Cap.ROUND); //设置画笔笔刷类型 帽子 圆弧两边的小盖子 把圆弧封闭住
        mNailPaint.setStrokeWidth(1);
        mNailPaint.setDither(true);

        mNailBoundPaint = new Paint();
        mNailBoundPaint.setAntiAlias(true); //设置抗锯齿，为了文字能清晰显示不至于很模糊
        mNailPaint.setDither(true);
        mNailBoundPaint.setStrokeWidth(1); //设置画笔宽度，单位px
        mNailBoundPaint.setColor(mNailStockColor); //设置外圆画笔颜色
        mNailBoundPaint.setStyle(Paint.Style.STROKE);
        mNailBoundPaint.setStrokeCap(Paint.Cap.ROUND);

        drawNailPath();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(mNailPath, mNailPaint);
        canvas.drawPath(mNailPath, mNailBoundPaint);
    }

    private void drawNailPath() {
        mNailPath = new Path();
        if (mNailIsLeft) {
            mNailPath.moveTo(0, 0);
            mNailPath.quadTo(0, mNailSize, mNailSize, mNailSize);
            mNailPath.addArc(new RectF(0, mNailSize, 2 * mNailSize, 3 * mNailSize), -90, 180);
            mNailPath.quadTo(0, 3 * mNailSize, 0, 4 * mNailSize);
            mNailPath.lineTo(0, 0);
        } else {
            mNailPath.moveTo(2 * mNailSize, 4 * mNailSize);
            mNailPath.quadTo(2 * mNailSize, 3 * mNailSize, mNailSize, 3 * mNailSize);
            mNailPath.addArc(new RectF(0, mNailSize, 2 * mNailSize, 3 * mNailSize), 90, 180);
            mNailPath.quadTo(2 * mNailSize, mNailSize, 2 * mNailSize, 0);
            mNailPath.lineTo(2 * mNailSize, 4 * mNailSize);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension((int)(mNailSize * 2), (int) (4 * mNailSize));
    }

}
