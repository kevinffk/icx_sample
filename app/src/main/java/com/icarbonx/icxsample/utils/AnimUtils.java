package com.icarbonx.icxsample.utils;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;

/**
 * Author:  Kevin Feng
 * Email:   fengfenkai@icarbonx.com
 * Date:    2018/2/5
 * Description:
 */
public class AnimUtils {


    public static void startFlick(View view) {
        if (null == view) {
            return;
        }

        Animation alphaAnimation = new AlphaAnimation(1, 0);
        alphaAnimation.setDuration(1500);
        alphaAnimation.setInterpolator(new LinearInterpolator());
        alphaAnimation.setRepeatCount(Animation.INFINITE);
        alphaAnimation.setRepeatMode(Animation.REVERSE);
        view.startAnimation(alphaAnimation);
    }

    /**
     * 取消View闪烁效果
     */
    public static void stopFlick(View view) {
        if (null == view) {
            return;
        }
        view.clearAnimation();
    }
}
