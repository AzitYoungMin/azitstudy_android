package com.trams.azit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.trams.azit.activity.MentoMainActivity;
import com.trams.azit.util.ConnActivity;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class BoardView extends ConnActivity  {

    private EditText commentEdit;
    private TextView mento_content, write_time, writer, QnAtitle, mento_answer_regist;
    private ImageView back, mento_content_image1, mento_content_image2, mento_content_image3, comment_image, mento_answer_picture, report;
    LinearLayout rLayout, replyLayout;

    SharedPreferences myPrefs;
    JSONObject jo = null;
    private String posting_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_view);

        Init();
        Init2();







    }


    public void Init() {
        rLayout = (LinearLayout) findViewById(R.id.mento_answer_comment_add_ll);
        replyLayout = (LinearLayout) findViewById(R.id.mento_reply_comment_add_ll);
        back = (ImageView) findViewById(R.id.button_back);
        commentEdit = (EditText) findViewById(R.id.mento_answer_comment_editText);
        mento_answer_regist = (TextView) findViewById(R.id.mento_answer_regist);
        QnAtitle = (TextView) findViewById(R.id.QnAtitle);
        writer = (TextView) findViewById(R.id.writer);
        write_time = (TextView) findViewById(R.id.write_time);
        mento_content = (TextView) findViewById(R.id.mento_content);
        mento_content_image1 = (ImageView) findViewById(R.id.mento_content_image1);
        mento_content_image2 = (ImageView) findViewById(R.id.mento_content_image2);
        mento_content_image3 = (ImageView) findViewById(R.id.mento_content_image3);
        mento_answer_picture = (ImageView) findViewById(R.id.mento_answer_picture);
        comment_image = (ImageView) findViewById(R.id.comment_image);
        report = (ImageView) findViewById(R.id.report);
    }

    public void Init2() {

        Intent i = getIntent();
        posting_id = i.getStringExtra("posting_id");

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
                        QnAtitle.setText(jo.getString("Title"));
                        mento_content.setText(jo.getString("Writer"));
                        writer.setText(jo.getString("Name"));
                        write_time.setText(jo.getString("Date"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
