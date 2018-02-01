package com.icarbonx.icxsample;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Author:  Kevin Feng
 * Email:   fengfenkai@icarbonx.com
 * Date:    2018/2/1
 * Description:
 */
public class TwoAct extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.two);
        GuaGuaView guaGuaView = (GuaGuaView) findViewById(R.id.tv_Scratch);
        guaGuaView.initScratchCard(this, 0xFFCECED1, 20, 1f);
        guaGuaView.setText("一等奖");
    }
}
