package com.icarbonx.icxsample;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Author:  Kevin Feng
 * Email:   fengfenkai@icarbonx.com
 * Date:    2018/3/9
 * Description:
 */
public class TestA extends FragmentActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_a);
    }

    public void onClick(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Pair<View, String> pair2 = new Pair<>(view, "image");

            ActivityOptionsCompat options = ActivityOptionsCompat
                    .makeSceneTransitionAnimation(TestA.this, pair2);

            Intent it = new Intent(TestA.this, TestB.class);
            it.putExtra("tag", (String)view.getTag());

            ActivityCompat.startActivity(TestA.this, it, options.toBundle());

        }
    }
}
