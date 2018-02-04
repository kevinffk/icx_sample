package com.icarbonx.icxsample;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.icarbonx.icxsample.utils.DisplayUtil;
import com.icarbonx.icxsample.utils.MySystemParams;

/**
 * Author:  Kevin Feng
 * Email:   fengfenkai@icarbonx.com
 * Date:    2018/1/31
 * Description:
 */
public class HumanBodyView extends View {

    /**
     * Const.
     */
    private int CIRCLE_SIZE = 0;

    private int MIN_DISTANCE = 0;

    private static final float HUMAN_SCALE = 0.62f;

    private static final int RESET_CANVAS = 1;


    private Bitmap mHumanBmp;

    private Bitmap mHumanOnBmp;

    private int viewHeight;

    private int viewWidth;

    private int touchX;

    private int touchY;

    private Paint mPaint;

    private Canvas mCanvas;

    private boolean isTouch = false;

    private boolean isMeasure = false;

    //椭圆参数
    private int arcWidth = 0;
    private int arcHeigh = 0;


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case RESET_CANVAS:
                    resetCanvas();
                    break;
            }
        }
    };

    public HumanBodyView(Context context) {
        super(context);
        init(context);
    }

    public HumanBodyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public HumanBodyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private void init(Context context) {
        CIRCLE_SIZE = DisplayUtil.dip2px(80, context);
        MIN_DISTANCE = DisplayUtil.dip2px(10, context);
        mHumanOnBmp = BitmapFactory.decodeResource(getResources(), R.mipmap.human_body);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画椭圆形

        canvas.drawBitmap(mHumanOnBmp, 0, 0, null); //蓝色
        if (isTouch) {
            mCanvas.drawCircle(touchX, touchY, CIRCLE_SIZE, mPaint);
        }
        canvas.drawBitmap(mHumanBmp, 0, 0, null); //白色
    }


    private void initPaint() {
        mPaint = new Paint();//创建画笔
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));//
        mPaint.setAntiAlias(true);// 抗锯齿
        mPaint.setDither(true);// 防抖动
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);// 画笔类型： STROKE空心 FILL实心 FILL_AND_STROKE用契形填充
        mPaint.setStrokeJoin(Paint.Join.ROUND);// 画笔接洽点类型
        mPaint.setStrokeCap(Paint.Cap.ROUND);// 画笔笔刷类型
        mPaint.setStrokeWidth(1);// 画笔笔刷宽度

        mCanvas = new Canvas(mHumanBmp);//通过bitmap生成一个画布
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mHumanBmp = zoomImg(R.mipmap.human_body);
        mHumanOnBmp = zoomImg(R.mipmap.human_body_on);
        initPaint();
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
        mHumanOnBmp = BitmapFactory.decodeResource(getResources(), R.mipmap.human_body);
        viewHeight = (int) (MySystemParams.getInstance(getContext()).screenHeight * HUMAN_SCALE);

        viewWidth = (int) ((float) (mHumanOnBmp.getWidth()) / mHumanOnBmp.getHeight() * viewHeight);


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


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mHandler.removeMessages(RESET_CANVAS);
                isTouch = true;
                touchX = (int) event.getX();
                touchY = (int) event.getY();
                toViewer(mPaint, touchX, touchY);
                invalidate();
            case MotionEvent.ACTION_MOVE:
                if (checkDistance((int) event.getX(), (int) event.getY())) {
                    mHandler.removeMessages(RESET_CANVAS);
                    isTouch = true;
                    touchX = (int) event.getX();
                    touchY = (int) event.getY();
                    toViewer(mPaint, touchX, touchY);
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                mHandler.sendEmptyMessageDelayed(RESET_CANVAS, 2000);
                break;
        }

        return super.onTouchEvent(event);
    }

    private boolean checkDistance(int x, int y) {
        return Math.sqrt((touchX - x) * (touchX - x) + (touchY - y) * (touchY - y)) >= MIN_DISTANCE;
    }

    /**
     * 还原图层.
     */
    private void resetCanvas() {
        mHumanBmp.recycle();
        mHumanBmp = zoomImg(R.mipmap.human_body);
        mCanvas = new Canvas(mHumanBmp);//通过bitmap生成一个画布
        isTouch = false;
        invalidate();
    }


    public void toViewer(Paint paint, int pointX, int pointY) {
        RadialGradient gradient = new RadialGradient(pointX, pointY, CIRCLE_SIZE, new int[]{Color.parseColor("#ffB70505"), Color.parseColor("#ffB70505"), Color.parseColor("#ffB70505"), Color.parseColor("#a0B70505"), Color.parseColor("#00B70505")}, null, Shader.TileMode.CLAMP);
        paint.setShader(gradient);
    }

    public void recycle() {
        mHandler.removeMessages(RESET_CANVAS);
        mHumanBmp.recycle();
        mHumanOnBmp.recycle();
    }

    /*    private int measureWidth(int pWidthMeasureSpec) {
        int result = 0;

        int widthMode = MeasureSpec.getMode(pWidthMeasureSpec);
        int widthSize = MeasureSpec.getSize(pWidthMeasureSpec);

        switch (widthMode) {
            case MeasureSpec.AT_MOST:
                //处理wrap content
                break;
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

                //处理wrap content
            case MeasureSpec.EXACTLY:
                result = heightSize;
                break;
        }

        return result;
    }*/
}
