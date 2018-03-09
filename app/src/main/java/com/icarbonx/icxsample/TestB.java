package com.icarbonx.icxsample;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.widget.ImageView;

import com.jaeger.library.StatusBarUtil;

/**
 * Author:  Kevin Feng
 * Email:   fengfenkai@icarbonx.com
 * Date:    2018/3/9
 * Description:
 */
public class TestB extends FragmentActivity {

    private ImageView iv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_b);

        if (Build.VERSION.SDK_INT > 19) {
            StatusBarUtil.setTranslucentForImageView(this, 0, null);
        }

        iv = (ImageView) findViewById(R.id.iv1);

        String tag = getIntent().getStringExtra("tag");

        int res = getResources().getIdentifier(tag, "mipmap", getPackageName());

        iv.setImageResource(res);
        ViewCompat.setTransitionName(iv, "image");
    }


}
