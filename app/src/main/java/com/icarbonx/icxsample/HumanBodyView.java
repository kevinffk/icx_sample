package com.icarbonx.icxsample;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Author:  Kevin Feng
 * Email:   fengfenkai@icarbonx.com
 * Date:    2018/1/31
 * Description:
 */
public class HumanBodyView extends View {

    private Bitmap mHumanBg;

    private Bitmap mHumanBgOn;

    public HumanBodyView(Context context) {
        super(context);
    }

    public HumanBodyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public HumanBodyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);




    }


}
