package com.trams.azit.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.trams.azit.R;
import com.trams.azit.StudentMainActivity;
import com.trams.azit.dialog.ReportSubcribeDialog;
import com.trams.azit.util.ConnActivity;
import com.tsengvn.typekit.TypekitContextWrapper;

/**
 * Created by Administrator on 25/01/2016.
 */
public class ReportPaymentActivity extends ConnActivity implements View.OnClickListener {

    private RadioButton rb1, rb2, rb3;
    private Button btnPaymentPhone, btnPaymentCard, btnPaymentBook, btnPayment;
    private int priceId;
    private ImageView imgBack;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_payment_activity);

        rb1 = (RadioButton) findViewById(R.id.rb_1);
        rb2 = (RadioButton) findViewById(R.id.rb_2);
        rb3 = (RadioButton) findViewById(R.id.rb_3);

        btnPaymentPhone = (Button) findViewById(R.id.btn_payment_cellphone);
        btnPaymentCard = (Button) findViewById(R.id.btn_payment_credit_card);
        btnPaymentBook = (Button) findViewById(R.id.btn_payment_blank_book);
        btnPayment = (Button) findViewById(R.id.btn_payment);

        btnPaymentPhone.setOnClickListener(this);
        btnPaymentCard.setOnClickListener(this);
        btnPaymentBook.setOnClickListener(this);
        btnPayment.setOnClickListener(this);

        imgBack = (ImageView) findViewById(R.id.img_back);
        imgBack.setOnClickListener(this);

        priceId = getIntent().getIntExtra(ReportSubcribeDialog.EXTRA_PRICE, 1);

        if (priceId == 1) {
            rb1.setChecked(true);
        } else if (priceId == 2) {
            rb2.setChecked(true);
        } else if (priceId == 3) {
            rb3.setChecked(true);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_payment_cellphone:
                setPaymentType(1);
                break;

            case R.id.btn_payment_credit_card:
                setPaymentType(2);
                break;

            case R.id.btn_payment_blank_book:
                setPaymentType(3);
                break;

            case R.id.btn_payment:
                doPayment();
                break;

            case R.id.img_back:
                finish();
                break;
        }
    }

    private void doPayment() {
//        Intent intent = new Intent(this, StudentMainActivity.class);
        startActivityNewTask(StudentMainActivity.class);
    }

    private void setPaymentType(int type) {
        btnPaymentPhone.setBackgroundResource(R.drawable.btn_payment_non_active);
        btnPaymentCard.setBackgroundResource(R.drawable.btn_payment_non_active);
        btnPaymentBook.setBackgroundResource(R.drawable.btn_payment_non_active);

        switch (type) {
            case 1:
                btnPaymentPhone.setBackgroundResource(R.drawable.btn_payment_active);
                break;
            case 2:
                btnPaymentCard.setBackgroundResource(R.drawable.btn_payment_active);
                break;
            case 3:
                btnPaymentBook.setBackgroundResource(R.drawable.btn_payment_active);
                break;
        }
    }
}

