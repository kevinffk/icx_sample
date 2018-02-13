package com.icarbonx.icxsample;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

/**
 * Author:  Kevin Feng
 * Email:   fengfenkai@icarbonx.com
 * Date:    2018/2/13
 * Description:
 */
public class MainAct7 extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bb);
        final HandleDrawerLayout handleDrawerLayout = (HandleDrawerLayout) findViewById(R.id.hdl_layout);
        View handleView = findViewById(R.id.hdl_handle_view);
        handleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(handleDrawerLayout.isDrawerOpen()){
                    handleDrawerLayout.closeDrawer();
                }else{
                    handleDrawerLayout.openDrawer();
                }
            }
        });
    }
}
