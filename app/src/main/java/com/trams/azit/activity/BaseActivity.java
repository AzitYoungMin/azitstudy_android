package com.trams.azit.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.trams.azit.util.MyProgressDialog;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

import butterknife.ButterKnife;

/**
 * Created by root on 1/1/2016.
 */
public abstract class BaseActivity extends FragmentActivity {
    private static final String TAG = BaseActivity.class.getSimpleName();

    /*protected Realm realm;*/
    private MyProgressDialog pd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        ButterKnife.bind(this);
//        realm = Realm.getInstance(this);
        //pd = MyProgressDialog.show(this, "", "", true, false, null);
        initViews(savedInstanceState);
        initVariables(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        if (pd != null) {
            if (pd.isShowing()) {
                pd.dismiss();
            }
        }
//        realm.close();
    }

    protected abstract void initVariables(Bundle savedInstanceState);

    protected abstract void initViews(Bundle savedInstanceState);

    protected abstract int getLayout();

    public class ConnHttpResponseHandler extends JsonHttpResponseHandler {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            Log.d(TAG, "ConnActivity onSuccess , response : " + response);
            if (pd!=null){
                pd.dismiss();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
            Log.d(TAG, "ConnActivity onFailure1 : " + t.getMessage());
            if (pd!=null){
                pd.dismiss();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable t, JSONObject res) {
            if (pd!=null){
                pd.dismiss();
            }
            Log.d(TAG, "ConnActivity onFailure2 : " + t.getMessage());
            Log.d("test", "ConnActivity onFailure2 /code : " + statusCode + "/headers : " + headers + "//res : " + res);
            t.getStackTrace();

        }
    }

    protected void requestJson(String subUrl, JSONObject jsonObj, ConnHttpResponseHandler receiveHandler) {
        if (pd != null) {
            pd.show();
        }

        Log.d(TAG, "ConnActivity , data request : " + jsonObj.toString());

        AsyncHttpClient client = new AsyncHttpClient();

        StringEntity entity = null;
        entity = new StringEntity(jsonObj.toString(), "UTF-8");

        client.setTimeout(15000);
        client.setResponseTimeout(15000);
        client.setConnectTimeout(15000);


        client.post(this, subUrl, entity, "application/json; charset=utf-8", receiveHandler);
    }

    public void showDialog(DialogFragment fragment, String tag) {
        FragmentManager fm = getSupportFragmentManager();
        fragment.show(fm, tag);
    }


    @Override
    public void onBackPressed() {
        int count = getFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            super.onBackPressed();
        } else {
            getFragmentManager().popBackStack();
        }
    }

    public void replaceFragment(int resId, Class<? extends Fragment> fragmentClazz, boolean addBackStack) {
        replaceFragment(resId, fragmentClazz, null, addBackStack);
    }

    public void replaceFragment(int resId, Class<? extends Fragment> fragmentClazz, Bundle args, boolean addBackStack) {
        FragmentManager manager = getSupportFragmentManager();
        String tag = fragmentClazz.getName();
        try {
            boolean isExisted = manager.popBackStackImmediate(tag, 0);    // IllegalStateException
            if (!isExisted) {
                Fragment fragment;
                try {
                    fragment = fragmentClazz.newInstance();

                    if (args != null) {
                        fragment.setArguments(args);
                    }
                    FragmentTransaction transaction = manager.beginTransaction();
//                    transaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
                    transaction.replace(resId, fragment, tag);

                    if (addBackStack) {
                        transaction.addToBackStack(tag);
                    } else {
                        transaction.addToBackStack(null);
                    }

                    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    transaction.commit();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
