package com.trams.azit;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import com.orhanobut.logger.Logger;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by Administrator on 2015-09-24.
 */
public class ViewPagerNoScroll extends ViewPager {

    private boolean enabled;

    public ViewPagerNoScroll(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.enabled = false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        try {
            if (this.enabled) {
//				Log.i("INFO", "스크롤 중..");
                return super.onTouchEvent(event);
            }
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsStrting = sw.toString();
            Logger.e("INFO", exceptionAsStrting);
        }

        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (this.enabled) {
            return super.onInterceptTouchEvent(event);
        }

        return false;
    }

    public void setPagingEnabled() { //이 메소드를 이용해서 스크롤을 풀어주고
        this.enabled = true;
    }

    public void setPagingDisabled() { //이 메소드를 이용해서 스크롤을 막아줍니다.
        this.enabled = false;
    }

}
