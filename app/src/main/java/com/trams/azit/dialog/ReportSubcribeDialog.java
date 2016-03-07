package com.trams.azit.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.trams.azit.R;
import com.trams.azit.activity.ReportPaymentActivity;

/**
 * Created by Administrator on 05/01/2016.
 */
public class ReportSubcribeDialog extends Dialog implements View.OnClickListener {

    private Button btnConfirm, btnCancel;
    private RadioButton rb1, rb2, rb3;
    public static final String EXTRA_PRICE = "price";

    public ReportSubcribeDialog(Context context) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.setCancelable(true);
        this.setCanceledOnTouchOutside(false);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.report_subscribe_dialog);

        btnConfirm = (Button) findViewById(R.id.btn_confirm);
        btnConfirm.setOnClickListener(this);

        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(this);

        rb1 = (RadioButton) findViewById(R.id.rb_1);
        rb2 = (RadioButton) findViewById(R.id.rb_2);
        rb3 = (RadioButton) findViewById(R.id.rb_3);

        rb1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    rb2.setChecked(false);
                    rb3.setChecked(false);
                }
            }
        });

        rb2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    rb1.setChecked(false);
                    rb3.setChecked(false);
                }
            }
        });

        rb3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    rb1.setChecked(false);
                    rb2.setChecked(false);
                }
            }
        });

    }

    /**
     * Show view
     */
    public void showView() {
        if (!this.isShowing()) {
            this.show();
        }
    }

    /**
     * hide view
     */
    public void hideView() {
        if (this.isShowing()) {
            this.dismiss();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_confirm:
                doConfirm();
                break;

            case R.id.btn_cancel:
                dismiss();
                break;
        }
    }

    private void doConfirm() {

        hideView();

        int priceId = 1;

        if (rb1.isChecked()) {
            priceId = 1;
        } else if (rb2.isChecked()) {
            priceId = 2;
        } else if (rb3.isChecked()) {
            priceId = 3;
        }

        Intent intent = new Intent(getContext(), ReportPaymentActivity.class);
        intent.putExtra(EXTRA_PRICE, priceId);
        getContext().startActivity(intent);

    }
}
