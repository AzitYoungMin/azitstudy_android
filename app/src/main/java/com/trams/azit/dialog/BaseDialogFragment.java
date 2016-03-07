package com.trams.azit.dialog;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trams.azit.R;

/**
 * Created by zin9x on 1/13/2016.
 */
public abstract class BaseDialogFragment extends DialogFragment {

    protected Context mContext;

    protected abstract int getLayoutResource();

    protected abstract void initViews(Bundle bundle, View view);

    protected abstract void initVariables(Bundle bundle, View view);

    protected abstract void initWindow(Bundle bundle, View view);

    protected abstract void initData(Bundle bundle);

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(getLayoutResource(), container, false);
        setCancelable(true);
//        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.MyDialog);
        initWindow(savedInstanceState, rootView);
        initViews(savedInstanceState, rootView);
        initVariables(savedInstanceState, rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData(savedInstanceState);
    }
}
