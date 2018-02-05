package com.icarbonx.icxsample;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.View;

import com.icarbonx.icxsample.utils.AnimUtils;

/**
 * Author:  Kevin Feng
 * Email:   fengfenkai@icarbonx.com
 * Date:    2018/1/31
 * Description:
 */
public class MainActivity extends Activity {

    private HumanBodyShadowView humanBodyShadowView;
    private HumanBodyView humanBodyView;

    private boolean isFlag = false;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        humanBodyView = (HumanBodyView) findViewById(R.id.human_body_view);
        humanBodyShadowView = (HumanBodyShadowView) findViewById(R.id.human_body_shadow);

        AnimUtils.startFlick(humanBodyShadowView);

        humanBodyView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                isFlag = !isFlag;
                humanBodyView.setmHumanOnBmp(isFlag ? R.mipmap.human_body_on_changed : R.mipmap.human_body_on);
                return false;
            }
        });
    }




}
