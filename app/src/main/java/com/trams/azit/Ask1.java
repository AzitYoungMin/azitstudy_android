package com.trams.azit;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.tsengvn.typekit.TypekitContextWrapper;

/**
 * Created by Administrator on 2015-09-23.
 */
public class Ask1 extends Activity {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask1);
    }
}
