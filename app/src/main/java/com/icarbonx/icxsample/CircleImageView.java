package com.icarbonx.icxsample;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Xfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

/**
 * Author:  Kevin Feng
 * Email:   fengfenkai@icarbonx.com
 * Date:    2018/2/1
 * Description:
 */
@SuppressLint("AppCompatCustomView")
public class CircleImageView extends ImageView {
    private Context context;
    private int radius = 0;
    private int defaultWidth = 0, defaultHeight = 0;

    public CircleImageView(Context context) {
        super(context);
        init(context);
    }

    public CircleImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CircleImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //获取图片
        Drawable drawable = getDrawable();
        //如果图片为空，直接放返回
        if (drawable == null)
            return;
        //如果控件的宽高其中一个为0，直接返回
        if (getWidth() == 0 || getHeight() == 0)
            return;
        this.measure(0, 0);
        //如果是.9图也无法显示，直接返回
        if (drawable.getClass() == NinePatchDrawable.class)
            return;
        Bitmap b = ((BitmapDrawable) drawable).getBitmap();
        Bitmap bitmap = b.copy(Bitmap.Config.ARGB_8888, true);
        //获取控件的宽高
        if (defaultWidth == 0) {
            defaultWidth = getWidth();
        }
        if (defaultHeight == 0) {
            defaultHeight = getHeight();
        }
        //半径的等于宽高中较小的一边的一半
        radius = (defaultHeight > defaultWidth ? defaultWidth : defaultHeight) / 2;
        Bitmap squareBitmap = Bitmap.createScaledBitmap(bitmap, radius * 2, radius * 2, false);
        Bitmap roundBitmap = toRoundBitmap(squareBitmap);
        //以控件的中心为中心画出图片
        canvas.drawBitmap(roundBitmap, defaultWidth / 2 - radius, defaultHeight / 2 - radius, null);
    }

    /**
     * 先画头像边缘的渐变
     */
    public void JianBian(Paint paint, int j, int k) {
        RadialGradient gradient = new RadialGradient(j / 2, k / 2, j / 2, new int[]{0xff5d5d5d, 0xff5d5d5d, 0x00ffffff}, new float[]{0.f, 0.8f, 1.0f}, Shader.TileMode.CLAMP);
        paint.setShader(gradient);
    }

    /**
     * 转换图片成圆形
     *
     * @param bitmap 传入Bitmap对象
     * @return
     */
    public Bitmap toRoundBitmap(Bitmap bitmap) {
        Paint paint = new Paint();
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float roundPx;
        float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
        paint.setAntiAlias(true);
        JianBian(paint, width, height);
        if (width <= height) {
            roundPx = width / 2;
            top = 0;
            bottom = width;
            left = 0;
            right = width;
            height = width;
            dst_left = 0;
            dst_top = 0;
            dst_right = width;
            dst_bottom = width;
            Log.i("111111", "111111");
        } else {
            roundPx = height / 2;
            float clip = (width - height) / 2;
            left = clip;
            right = width - clip;
            top = 0;
            bottom = height;
            width = height;
            dst_left = 0;
            dst_top = 0;
            dst_right = height;
            dst_bottom = height;
            Log.i("111111", "22222");
        }
        Bitmap output = Bitmap.createBitmap(width,
                height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Rect src = new Rect((int) left, (int) top, (int) right, (int) bottom);
        final Rect dst = new Rect((int) dst_left, (int) dst_top, (int) dst_right, (int) dst_bottom);
        final RectF rectF = new RectF(dst_left - 5//图片对应imageview的左边距
                , dst_top - 5//图片对应imageview的上边距
                , dst_right//图片对应imageview的右边距
                , dst_bottom//图片对应imageview的下边距
        );
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, src, dst, paint);
        Log.i("111111", "33333");
//BaiBian ( canvas,dst_right,dst_bottom );
        return output;
    }

    /**
     * 最后画上白边
     */
    public void BaiBian(Canvas canvas, float j, float k) {
        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#fff"));
        paint.setAntiAlias(true);
        paint.setStrokeWidth(2);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(j / 2 //左右位置
                , k / 2//上下位置
                , j / 2 - 2 //半径大小
                , paint);
    }
}
