package com.trams.azit;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;
import com.trams.azit.activity.MentoMainActivity;
import com.trams.azit.util.ConnActivity;
import com.trams.azit.util.Url_define;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;


public class BoardView extends ConnActivity  {

    boolean photochange = false;
    private EditText commentEdit;
    private TextView mento_content, write_time, writer, QnAtitle, mento_answer_regist;
    private ImageView back, mento_content_image1, mento_content_image2, mento_content_image3, comment_image, mento_answer_picture, report, image_sr;
    LinearLayout rLayout, replyLayout;
    JSONObject jsonObject;
    String user_id;
    private SharedPreferences mPref;
    SharedPreferences myPrefs;
    JSONObject jo = null;
    private String posting_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_view);
        mPref = getApplicationContext().getSharedPreferences("Azit", Context.MODE_PRIVATE);
        Init();
        Init2();







    }


    public void Init() {

        Intent i = getIntent();
        posting_id = i.getStringExtra("posting_id");

        user_id = mPref.getString("user_id", "");
        rLayout = (LinearLayout) findViewById(R.id.mento_answer_comment_add_ll);
        replyLayout = (LinearLayout) findViewById(R.id.mento_reply_comment_add_ll);
        back = (ImageView) findViewById(R.id.button_back);

        LoadFromServer();
        commentEdit = (EditText) findViewById(R.id.mento_answer_comment_editText);
        mento_answer_regist = (TextView) findViewById(R.id.mento_answer_regist);

        mento_answer_regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (commentEdit.getText().toString().equals("")) {
                    Toast.makeText(getApplication(), "댓글이 입력되지 않았습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    jsonObject = new JSONObject();
                    try {
                        jsonObject.put("secret", mPref.getString("secret", ""));
                        jsonObject.put("posting_id", posting_id);
                        jsonObject.put("user_id", mPref.getString("user_id", ""));
                        jsonObject.put("reply", commentEdit.getText().toString());
                        jsonObject.put("parent_id", "0");

                        Date from = new Date();
                        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String to = transFormat.format(from);

                        RequestParams params = new RequestParams();
                        params.put("posting_id", posting_id);
                        params.put("user_id", mPref.getString("user_id", ""));
                        params.put("reply", commentEdit.getText().toString());
                        params.put("time",to);
                        if (photochange) {

                            ByteArrayOutputStream bos1 = new ByteArrayOutputStream();
                            BitmapDrawable d1 = (BitmapDrawable) ((ImageView) findViewById(R.id.comment_image)).getDrawable();
                            Bitmap b1 = d1.getBitmap();
                            b1.compress(Bitmap.CompressFormat.JPEG, 100, bos1);
                            params.put("profile_image", new ByteArrayInputStream(bos1.toByteArray()), "profile", "image/jpeg");

                        }

                        requestMultipart("http://192.168.1.21:2000/FreeBoard/Reply_Write", jsonObject, params, new ConnHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONArray res) {

                                       //res.getString("result").equals("success")
                                        commentEdit.setText("");
                                        comment_image.setImageResource(0);
                                        LoadFromServer();

                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, Throwable t, JSONObject res) {
                                t.printStackTrace();
                                if (res.toString() == null) Log.d("log", "으앙쥬금 ㅠㅠ");
                                Log.d("statusCode", statusCode + "");
                                Log.d("onFailure res", res.toString());
                                Log.d("jsonObject : ", jsonObject.toString());
                                Toast.makeText(getApplicationContext(), "데이터를 받아오지 못했습니다.", Toast.LENGTH_SHORT).show();
                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
       // QnAtitle = (TextView) findViewById(R.id.QnAtitle);
        writer = (TextView) findViewById(R.id.writer);
        write_time = (TextView) findViewById(R.id.write_time);
        mento_content = (TextView) findViewById(R.id.mento_content);
        mento_content_image1 = (ImageView) findViewById(R.id.mento_content_image1);
        mento_content_image2 = (ImageView) findViewById(R.id.mento_content_image2);
        mento_content_image3 = (ImageView) findViewById(R.id.mento_content_image3);
        mento_answer_picture = (ImageView) findViewById(R.id.mento_answer_picture);
        comment_image = (ImageView) findViewById(R.id.comment_image);
        report = (ImageView) findViewById(R.id.report);
        image_sr = (ImageView)findViewById(R.id.image_sr);
    }

    public void Init2() {



        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("posting_id", posting_id);
            Log.e("jsonObject", jsonObject.toString());
            requestJson("http://192.168.1.21:2000/FreeBoradView/", jsonObject, new ConnActivity.ConnHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                    Log.d("statusCode : ", statusCode + "");
                    super.onFailure(statusCode, headers, res, t);
                }



                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    Log.d("statusCode : ", statusCode + "");
                    Log.d("response : ", response.toString());

                    super.onSuccess(statusCode, headers, response);

                    try {
                        Log.e("jsonObject", String.valueOf(response.getJSONObject(0)));

                        jo = response.getJSONObject(0);
                        //QnAtitle.setText(jo.getString("Title"));
                        mento_content.setText(jo.getString("Content"));
                        writer.setText(jo.getString("Name"));
                        write_time.setText(jo.getString("Date"));




                        Picasso.with(BoardView.this).load("http://192.168.1.21:2000/"+ jo.getString("Image")).into(image_sr);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setAddCommentView(JSONArray response) { // 댓글 추가
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        int su = 0;
        try {
            su = jsonObject.getInt("Replies");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {

            for (int i = 0; i < su; i++) {
                jsonObject = response.getJSONObject(i);
                View commentView = inflater.from(getApplicationContext()).inflate(R.layout.mento_answer_comment_view, null);
                //JSONObject resJson = new JSONObject(jsonArray.get(i).toString());

                final int reply_id = jsonObject.getInt("Id");
                ImageView mento_answer_comment_image = (ImageView) commentView.findViewById(R.id.mento_answer_comment_image);
                ImageView comment_profile = (ImageView) commentView.findViewById(R.id.mento_answer_profile);
                TextView comment_name = (TextView) commentView.findViewById(R.id.mento_answer_name);
                TextView comment_content = (TextView) commentView.findViewById(R.id.mento_answer_comment_content);
                TextView comment_time = (TextView) commentView.findViewById(R.id.mento_answer_comment_createtime);
                LinearLayout comment_like_ll = (LinearLayout) commentView.findViewById(R.id.like_ll);
                ImageView comment_like_btn = (ImageView) commentView.findViewById(R.id.mento_answer_like_btn);
                TextView comment_like_count = (TextView) commentView.findViewById(R.id.like_text1);
                TextView role = (TextView) commentView.findViewById(R.id.role);
                ImageView mento_answer_comment_delete = (ImageView) commentView.findViewById(R.id.mento_answer_comment_delete);
                ImageView reportbtn = (ImageView) commentView.findViewById(R.id.reportbtn);

                reportbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogInterface.OnClickListener cameraListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //doReplyReport(reply_id);
                            }
                        };

                        DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        };

                        new AlertDialog.Builder(BoardView.this)

                                .setTitle("해당 댓글을 신고하시겠습니까?")
                                .setPositiveButton("신고", cameraListener)
                                .setNegativeButton("취소", cancelListener)
                                .show();
                    }
                });

                if (jsonObject.getString("Image").equals("none")) {
                    Picasso.with(getApplicationContext()).load(R.drawable.profile_basic_icon).fit().into(comment_profile);
                } else {
                    Picasso.with(getApplicationContext())
                            .load(Url_define.BASE_Image + jsonObject.getString("Image"))
                            .placeholder(R.drawable.profile_basic_icon)
                            .error(R.drawable.profile_basic_icon).fit().into(comment_profile);
                }

                /*if (resJson.isNull("reply_images") || resJson.getString("reply_images").equals("")) {
                } else {
                    Picasso.with(getApplicationContext())
                            .load(Url_define.BASE_Image + resJson.getString("reply_images"))
                            .into(mento_answer_comment_image);
                }*/


                if (user_id.equals(jsonObject.getInt("Id") + "")) {
                    mento_answer_comment_delete.setVisibility(View.VISIBLE);
                } else {
                    mento_answer_comment_delete.setVisibility(View.GONE);
                }

                /*mento_answer_comment_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(BoardView.this, R.style.myDialog));     // 여기서 this는 Activity의 this

                        builder.setTitle("댓글 삭제")
                                .setMessage("댓글을 삭제하시겟습니까?")
                                .setCancelable(false)
                                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    // 확인 버튼 클릭시 설정
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        try {
                                            JSONObject jsonObject = new JSONObject();
                                            jsonObject.put("secret", mPref.getString("secret", ""));
                                            jsonObject.put("user_id", mPref.getString("user_id", ""));
                                            jsonObject.put("reply_id", String.valueOf(reply_id));
                                            Log.e("jsonObject", jsonObject.toString());
                                            Log.e("Url_define", Url_define.Common_Reply_delete + Url_define.KEY);
                                            requestJson(Url_define.Common_Reply_delete + Url_define.KEY, jsonObject, new ConnHttpResponseHandler() {
                                                @Override
                                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                                    Log.d("statusCode : ", statusCode + "");
                                                    Log.d("response : ", response.toString());
                                                    super.onSuccess(statusCode, headers, response);

                                                    try {
                                                        if (response.getString("result").equals("success")) {
                                                            LoadFromServer();
                                                        }
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
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
                                })
                                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                    // 취소 버튼 클릭시 설정
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        dialog.cancel();
                                    }
                                });

                        AlertDialog dialog = builder.create();    // 알림창 객체 생성
                        dialog.show();
                    }
                });
*/
               // Log.e("reply_id", resJson.getInt("reply_id") + "");

                comment_name.setText("최영민");
                comment_content.setText(jsonObject.getString("Reply"));//jsonObject.getString("reply_article"));
                comment_time.setText(jsonObject.getString("Created_at"));
                comment_like_count.setText("");
                comment_like_btn.setVisibility(View.GONE);
                //role.setText("ddd");  //jsonObject.getString("role_of_writer"));
                //comment_like_count.setText("좋아요 " + "222" + "개"); //jsonObject.getString("num_of_like")
               /* if (jsonObject.getBoolean("click_like")) {

                    comment_like_count.setTextColor(Color.parseColor("#1fb4ff"));

                }*/
/*
                comment_like_ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        try {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("secret", mPref.getString("secret", ""));
                            jsonObject.put("user_id", mPref.getString("user_id", ""));
                            jsonObject.put("reply_id", String.valueOf(reply_id));

                            requestJson(Url_define.Common_Reply_like + Url_define.KEY, jsonObject, new ConnHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                    Log.d("statusCode : ", statusCode + "");
                                    Log.d("response : ", response.toString());
                                    super.onSuccess(statusCode, headers, response);

                                    try {
                                        if (response.getString("result").equals("success")) {
                                            LoadFromServer();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
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
                });*/

                replyLayout.addView(commentView);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void LoadFromServer() {
        jsonObject = new JSONObject();
        try {
            jsonObject.put("secret", mPref.getString("secret", ""));
            jsonObject.put("posting_id", posting_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        requestJson("http://192.168.1.21:2000/FreeBorad/Reply_View", jsonObject, new ConnActivity.ConnHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d("statusCode", statusCode + "");
                Log.d("onSuccess res", response.toString());
                try {
                    jsonObject = response.getJSONObject(0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("onSuccess jsonObject", jsonObject.toString());

                rLayout.removeAllViews();
                replyLayout.removeAllViews();

                //setAddAnswerView();
                //댓글 뷰 추가하기
                setAddCommentView(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable t, JSONObject res) {
                t.printStackTrace();
                Log.d("statusCode", statusCode + "");
                Log.d("jsonObject : ", jsonObject.toString());
                Toast.makeText(getApplicationContext(), "데이터를 받아오지 못했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }



}
