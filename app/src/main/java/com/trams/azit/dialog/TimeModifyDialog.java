package com.trams.azit.dialog;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.trams.azit.R;

/**
 * Created by zin9x on 1/13/2016.
 */
public class TimeModifyDialog extends BaseDialogFragment {
//    RelativeLayout header_layout;

    public static TimeModifyDialog newInstance() {
        TimeModifyDialog f = new TimeModifyDialog();
        return f;
    }
    @Override
    protected int getLayoutResource() {
        return R.layout.dialog_timemodify;
    }

    @Override
    protected void initViews(Bundle bundle, View view) {
        getDialog().setCancelable(true);
        getDialog().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
//        header_layout = (RelativeLayout) view.findViewById(R.id.header_layout);
    }

    @Override
    protected void initVariables(Bundle bundle, View view) {

    }

    @Override
    protected void initWindow(Bundle bundle, View view) {

    }

    @Override
    protected void initData(Bundle bundle) {

    }
}
