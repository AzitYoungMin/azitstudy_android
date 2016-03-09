package com.trams.azit;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.trams.azit.util.ConnActivity;
import com.trams.azit.util.Url_define;
import com.tsengvn.typekit.TypekitContextWrapper;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

public class Ask_Free_Board extends ConnActivity {

    ImageView cancel, register;
    EditText title, body;
    SharedPreferences myPrefs;
    String secret, user_id;
    int posting_id;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_meet_mentor);

        posting_id = getIntent().getIntExtra("posting_id", 0);

        myPrefs = getSharedPreferences("Azit", MODE_PRIVATE);
        secret = myPrefs.getString("secret", "");
        user_id = myPrefs.getString("user_id", "");

        cancel = (ImageView) findViewById(R.id.cancel);
        register = (ImageView) findViewById(R.id.register);
        title = (EditText) findViewById(R.id.title);
        body = (EditText) findViewById(R.id.body);

        if (posting_id != 0) {
            getEditData();
        }

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener confirmListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(Ask_Free_Board.this, StudentMainActivity.class);
                        i.putExtra("position", 4);
                        startActivity(i);
                    }
                };

                DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                };

                new AlertDialog.Builder(Ask_Free_Board.this)
                        .setTitle("해당 질문을 삭제하시겠어요?")
                        .setPositiveButton("확인", confirmListener)
                        .setNegativeButton("취소", cancelListener)
                        .show();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = Url_define.Student_Save_Ask_mento + Url_define.KEY;
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("secret", secret);
                    jsonObject.put("user_id", user_id);
                    jsonObject.put("title", title.getText().toString());
                    jsonObject.put("article", body.getText().toString());
                    if (posting_id != 0) {
                        jsonObject.put("posting_id", posting_id);
                        url = Url_define.Student_Update_Ask_mento + Url_define.KEY;

                    }
                    Logger.e(secret);
                    Logger.e(user_id);
                    requestJson(url, jsonObject, new ConnActivity.ConnHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.d("statusCode : ", statusCode + "");
                            Log.d("response : ", response.toString());
                            super.onSuccess(statusCode, headers, response);

                            if (posting_id != 0) {
                                Toast.makeText(Ask_Free_Board.this, "수정 완료!", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(Ask_Free_Board.this, "등록 완료!", Toast.LENGTH_LONG).show();
                            }

                            Intent i = new Intent(Ask_Free_Board.this, My_Act.class);
                            i.putExtra("position", 0);
                            startActivity(i);
                            finish();
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                            Log.d("statusCode : ", statusCode + "");
                            super.onFailure(statusCode, headers, res, t);
                        }
                    });
                } catch (JSONException e) {
                    e.getStackTrace();
                }
            }
        });
    }

    private void getEditData() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("secret", secret);
            jsonObject.put("user_id", user_id);
            jsonObject.put("posting_id", posting_id);

            requestJson(Url_define.Student_Get_Ask_mento + Url_define.KEY, jsonObject, new ConnHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    try {
                        if (response.getString("result").equals("success")) {
                            title.setText(response.getString("title"));
                            body.setText(response.getString("article"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
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
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
