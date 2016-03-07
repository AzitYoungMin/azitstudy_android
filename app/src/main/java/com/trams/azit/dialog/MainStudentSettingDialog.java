package com.trams.azit.dialog;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.trams.azit.R;
import com.trams.azit.util.ConnActivity;
import com.tsengvn.typekit.TypekitContextWrapper;

/**
 * Created by zin9x on 1/14/2016.
 */
public class MainStudentSettingDialog extends ConnActivity implements View.OnClickListener{

    ImageView btn_close;
    Button refund;
    TextView can_money;
    EditText name, account, money;
    SharedPreferences myPrefs;
    String secret, user_id;
    Spinner spinner;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setFinishOnTouchOutside(false);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_main_student_setting);

        myPrefs = getSharedPreferences("Azit", MODE_PRIVATE);
        secret = myPrefs.getString("secret", "");
        user_id = myPrefs.getString("user_id", "");

        name = (EditText)findViewById(R.id.name);
        account = (EditText)findViewById(R.id.account);
        money = (EditText)findViewById(R.id.money);
        can_money = (TextView)findViewById(R.id.can_money);
        spinner = (Spinner)findViewById(R.id.spinner);
        btn_close = (ImageView) findViewById(R.id.btn_close);

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
                doRefund();
                break;
        }
    }

    private void doRefund() {
        finish();
    }
}
