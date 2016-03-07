package com.trams.azit.util;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.tsengvn.typekit.Typekit;

/**
 * Created by Administrator on 2015-09-25.
 */
public class GlobalFont extends Application {

    private static final String TAG = GlobalFont.class.getName();

    @Override
    public void onCreate() {
        super.onCreate();
        Typekit.getInstance()
               // .addNormal(Typekit.createFromAsset(this, "KoPubBatangMedium.ttf"))
                //.addBold(Typekit.createFromAsset(this, "KoPubBatangBold.ttf"));

                .addNormal(Typekit.createFromAsset(this, "NanumGothic.ttf"))
                .addBold(Typekit.createFromAsset(this, "NanumGothicBold.ttf"));
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}
