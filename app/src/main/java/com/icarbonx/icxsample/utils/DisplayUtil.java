package com.icarbonx.icxsample.utils;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;

/**
 * Android大小单位转换工具类
 * 
 * @author wader
 * 
 */
public class DisplayUtil {
	
	
	/**
	 * px
	 * @param fontSize
	 * @return
	 */
	public static int getFontHeight(float fontSize){
	    Paint paint = new Paint();
	    paint.setTextSize(fontSize);
	    FontMetrics fm = paint.getFontMetrics();
	    return (int) Math.ceil(fm.descent - fm.top);
	}
	
	
	
	/**
	 * 将px值转换为dip或dp值，保证尺寸大小不变
	 * 
	 * @param pxValue
	 * @param scale
	 *            （DisplayMetrics类中属性density）
	 * @return
	 */
	public static int px2dip(float pxValue, Context activity) {
		float scale = MySystemParams.getInstance(activity).scale;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 将dip或dp值转换为px值，保证尺寸大小不变
	 * 
	 * @param dipValue
	 * @param scale
	 *            （DisplayMetrics类中属性density）
	 * @return
	 */
	public static int dip2px(float dipValue, Context activity) {
		float scale = MySystemParams.getInstance(activity).scale;
		return (int) (dipValue * scale + 0.5f);
	}

	/**
	 * 将px值转换为sp值，保证文字大小不变
	 * 
	 * @param pxValue
	 * @param fontScale
	 *            （DisplayMetrics类中属性scaledDensity）
	 * @return
	 */
	public static int px2sp(float pxValue, Context activity) {
		float fontScale = MySystemParams.getInstance(activity).fontScale;
		return (int) (pxValue / fontScale + 0.5f);
	}

	/**
	 * 将sp值转换为px值，保证文字大小不变
	 * 
	 * @param spValue
	 * @param fontScale
	 *            （DisplayMetrics类中属性scaledDensity）
	 * @return
	 */
	public static int sp2px(float spValue, Context activity) {
		float fontScale = MySystemParams.getInstance(activity).fontScale;
		return (int) (spValue * fontScale + 0.5f);
	}
}
