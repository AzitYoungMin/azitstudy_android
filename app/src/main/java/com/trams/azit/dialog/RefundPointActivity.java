package com.trams.azit.dialog;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.trams.azit.R;
import com.trams.azit.util.ConnActivity;
import com.trams.azit.util.Url_define;
import com.tsengvn.typekit.TypekitContextWrapper;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by manh on 2016-01-21.
 */
public class RefundPointActivity extends ConnActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    ImageView btn_close;
    Button refund;
    EditText name, account;
    SharedPreferences myPrefs;
    String secret, user_id;
    int radio_position = 3000;
    Spinner spinner;
    RadioGroup radio_group;
    RadioButton radiobtn1;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setFinishOnTouchOutside(false);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refund_point);

        myPrefs = getSharedPreferences("Azit", MODE_PRIVATE);
        secret = myPrefs.getString("secret", "");
        user_id = myPrefs.getString("user_id", "");

        name = (EditText)findViewById(R.id.name);
        account = (EditText)findViewById(R.id.account);

        spinner = (Spinner)findViewById(R.id.spinner);
        radio_group = (RadioGroup)findViewById(R.id.radio_group);
        radio_group.setOnCheckedChangeListener(this);
        btn_close = (ImageView) findViewById(R.id.btn_close);
        radiobtn1 = (RadioButton)findViewById(R.id.radiobtn1);

        radiobtn1.setSelected(true);

        btn_close.setOnClickListener(this);

        refund = (Button) findViewById(R.id.refund);
        refund.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_close:
                finish();
                break;

            case R.id.refund:
                if (spinner.getSelectedItemPosition() == 0 || name.getText().toString().equals("") ||account.getText().toString().equals("")) {
                    Toast.makeText(RefundPointActivity.this, "입력하신 정보를 확인해 주세요.", Toast.LENGTH_SHORT).show();
                } else {

                    doRefund();
                }
                break;
        }
    }

    private void doRefund() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("secret", secret);
            jsonObject.put("user_id", user_id);
            jsonObject.put("name", name.getText().toString());
            jsonObject.put("bank", spinner.getSelectedItem().toString());
            jsonObject.put("account", account.getText().toString());
            jsonObject.put("amount", radio_position);
            Log.e("jsonObject", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        requestJson(Url_define.mentor_refund + Url_define.KEY, jsonObject, new ConnActivity.ConnHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.e("Refund", response.toString());
                try {
                    if (response.getString("result").equals("success")){
                        finish();
                    }else{
                        Toast.makeText(RefundPointActivity.this, response.getString("message"), Toast.LENGTH_SHORT).cancel();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }{

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                super.onFailure(statusCode, headers, res, t);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable t, JSONObject res) {
                super.onFailure(statusCode, headers, t, res);
            }
        });
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.radiobtn1:
                radio_position = 3000;
                break;
            case R.id.radiobtn2:
                radio_position = 5000;
                break;
            case R.id.radiobtn3:
                radio_position = 10000;
                break;
        }
    }
}
