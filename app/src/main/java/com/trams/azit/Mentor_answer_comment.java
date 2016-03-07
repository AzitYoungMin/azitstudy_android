package com.trams.azit;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;
import com.trams.azit.util.ConnActivity;
import com.trams.azit.util.Url_define;
import com.tsengvn.typekit.TypekitContextWrapper;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Administrator on 2016-01-16.
 */
public class Mentor_answer_comment extends ConnActivity {

    String mento_answer_id;
    JSONObject jsonObject;
    private ImageView back, mento_answer_profile, mento_answer_picture, comment_image;
    private SharedPreferences mPref;
    LinearLayout replyLayout;
    String secret, user_id, writer_id;
    TextView mento_answer_regist, mento_answer_name, mento_answer_position, mento_answer_content, mento_answer_comment_createtime;
    private EditText commentEdit;
    private Uri mImageCaptureUri;
    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    boolean photochange = false;
    Bitmap photo_bit;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentor_answer_comment);

        commentEdit = (EditText) findViewById(R.id.mento_answer_comment_editText);
        back = (ImageView) findViewById(R.id.button_back);
        mento_answer_regist = (TextView) findViewById(R.id.mento_answer_regist);
        mento_answer_id = String.valueOf(getIntent().getIntExtra("mento_answer_id", 0));
        Log.e("mento_answer_id", mento_answer_id + "");
        mPref = getApplicationContext().getSharedPreferences("Azit", Context.MODE_PRIVATE);
        user_id = mPref.getString("user_id", "");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mento_answer_profile = (ImageView) findViewById(R.id.mento_answer_profile);
        mento_answer_name = (TextView) findViewById(R.id.mento_answer_name);
        mento_answer_position = (TextView) findViewById(R.id.mento_answer_position);
        mento_answer_content = (TextView) findViewById(R.id.mento_answer_content);
        mento_answer_comment_createtime = (TextView) findViewById(R.id.mento_answer_comment_createtime);
        replyLayout = (LinearLayout) findViewById(R.id.mento_reply_comment_add_ll);
        mento_answer_picture = (ImageView) findViewById(R.id.mento_answer_picture);
        comment_image = (ImageView) findViewById(R.id.comment_image);

        mento_answer_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener cameraListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        doTakePhotoAction();
                    }
                };
                DialogInterface.OnClickListener albumListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        doTakeAlbumAction();
                    }
                };
                DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                };

                new AlertDialog.Builder(Mentor_answer_comment.this)

                        .setTitle("업로드할 이미지 선택")
                        .setPositiveButton("사진촬영", cameraListener)
                        .setNeutralButton("앨범선택", albumListener)
                        .setNegativeButton("취소", cancelListener)
                        .show();
            }
        });

        sendComment();
        LoadFromServer();
    }


    private void doTakePhotoAction() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        String url = "tmp_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
        mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/azit/", url));

        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
        startActivityForResult(intent, PICK_FROM_CAMERA);

    }

    /**
     * 앨범에서 이미지 가져오기
     */
    private void doTakeAlbumAction() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(intent, PICK_FROM_ALBUM);

    }

    private void sendComment() {
        mento_answer_regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (commentEdit.getText().toString().equals("")) {
                    Toast.makeText(getApplication(), "답글이 입력되지 않았습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    jsonObject = new JSONObject();
                    try {
                        jsonObject.put("secret", mPref.getString("secret", ""));
                        jsonObject.put("posting_id", mento_answer_id);
                        jsonObject.put("user_id", mPref.getString("user_id", ""));
                        jsonObject.put("reply", commentEdit.getText().toString());
                        jsonObject.put("parent_id", "0");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    RequestParams params = new RequestParams();
                    if (photochange) {
                        ByteArrayOutputStream bos1 = new ByteArrayOutputStream();
                        BitmapDrawable d1 = (BitmapDrawable) ((ImageView) findViewById(R.id.comment_image)).getDrawable();
                        Bitmap b1 = d1.getBitmap();
                        b1.compress(Bitmap.CompressFormat.JPEG, 100, bos1);
                        params.put("profile_image", new ByteArrayInputStream(bos1.toByteArray()), "profile", "image/jpeg");

                    }

                    requestMultipart(Url_define.BASE + "/api/posting/reply/save" + Url_define.KEY, jsonObject, params, new ConnHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject res) {
                            try {
                                if (res.getString("result").equals("success")) {
                                    commentEdit.setText("");
                                    comment_image.setImageResource(0);
                                    LoadFromServer();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
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
                }


            }
        });
    }

    private void LoadFromServer() {
        jsonObject = new JSONObject();
        try {
            jsonObject.put("secret", mPref.getString("secret", ""));
            jsonObject.put("posting_id", mento_answer_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestJson(Url_define.BASE + "/api/posting/contents" + Url_define.KEY, jsonObject, new ConnHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject res) {
                Log.d("statusCode", statusCode + "");
                Log.d("onSuccess res", res.toString());
                jsonObject = res;
                Log.d("onSuccess jsonObject", jsonObject.toString());

                replyLayout.removeAllViews();

                setAddAnswerView();
                //댓글 뷰 추가하기
                setAddCommentView();
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
    }

    private void setAddAnswerView() {

        try {
            jsonObject = new JSONObject(jsonObject.getString("contents"));

            JSONArray jsonArray = new JSONArray(jsonObject.getString("answers"));

            Log.e("jsonArray", jsonArray.toString());

            TextView comment_count = (TextView) findViewById(R.id.mento_answer_comment_count);
            comment_count.setText("댓글 " + jsonObject.getString("num_of_reply") + "개");


            final int mento_answer_id = jsonObject.getInt("posting_id");
            mento_answer_name.setText(jsonObject.getString("writer"));
            mento_answer_comment_createtime.setText(jsonObject.getString("created_at"));
            mento_answer_content.setText(jsonObject.getString("article"));
            Picasso.with(getApplicationContext()).load(R.drawable.profile_basic_icon).fit().into(mento_answer_profile);

//                //피카소로 이미지 그리기
//                if (jsonObject.isNull("profile_image") || jsonObject.getString("profile_image").equals("")) {
//                    Picasso.with(getApplicationContext()).load(R.drawable.profile_basic_icon).fit().into(mento_answer_profile);
//                } else {
//                    Picasso.with(getApplicationContext())
//                            .load(Url_define.BASE_Image + resJson.getString("profile_image"))
//                            .placeholder(R.drawable.profile_basic_icon)
//                            .error(R.drawable.profile_basic_icon).fit().into(mento_answer_profile);
//                }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void doReplyReport(int reply_id) {
        jsonObject = new JSONObject();
        try {
            jsonObject.put("secret", mPref.getString("secret", ""));
            jsonObject.put("user_id", user_id);
            jsonObject.put("reply_id", String.valueOf(reply_id));
            Log.e("doReplyReport", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        requestJson(Url_define.report_posting + Url_define.KEY, jsonObject, new ConnHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    if (response.getString("result").equals("success")) {
                        if (response.getString("message").equals("reply report success")) {
                            Toast.makeText(Mentor_answer_comment.this, "신고되었습니다.", Toast.LENGTH_SHORT).cancel();
                        } else {
                            Toast.makeText(Mentor_answer_comment.this, "신고가 취소되었습니다.", Toast.LENGTH_SHORT).cancel();
                        }
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
    }

    private void setAddCommentView() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        try {
            JSONArray jsonArray = new JSONArray(jsonObject.getString("replies"));

            for (int i = 0; i < jsonArray.length(); i++) {
                View commentView = inflater.from(getApplicationContext()).inflate(R.layout.mento_answer_comment_view, null);
                JSONObject resJson = new JSONObject(jsonArray.get(i).toString());

                final int reply_id = resJson.getInt("reply_id");
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
                                doReplyReport(reply_id);
                            }
                        };

                        DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        };

                        new AlertDialog.Builder(Mentor_answer_comment.this)

                                .setTitle("해당 댓글을 신고하시겠습니까?")
                                .setPositiveButton("신고", cameraListener)
                                .setNegativeButton("취소", cancelListener)
                                .show();
                    }
                });

                if (resJson.isNull("profile_image") || resJson.getString("profile_image").equals("")) {
                    Picasso.with(getApplicationContext()).load(R.drawable.profile_basic_icon).fit().into(comment_profile);

                } else {
                    Picasso.with(getApplicationContext()).load(Url_define.BASE_Image + resJson.getString("profile_image")).placeholder(R.drawable.profile_basic_icon).error(R.drawable.profile_basic_icon).fit().into(comment_profile);
                }

                if (resJson.isNull("reply_images") || resJson.getString("reply_images").equals("")) {
                } else {
                    Picasso.with(getApplicationContext())
                            .load(Url_define.BASE_Image + resJson.getString("reply_images"))
                            .into(mento_answer_comment_image);
                }

                if (user_id.equals(resJson.getInt("user_id") + "")) {
                    mento_answer_comment_delete.setVisibility(View.VISIBLE);
                } else {
                    mento_answer_comment_delete.setVisibility(View.GONE);
                }

                mento_answer_comment_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(Mentor_answer_comment.this, R.style.myDialog));     // 여기서 this는 Activity의 this

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

                Log.e("reply_id", resJson.getInt("reply_id") + "");

                comment_name.setText(resJson.getString("writer"));
                comment_content.setText(resJson.getString("reply_article"));
                comment_time.setText(resJson.getString("created_at"));
                role.setText(resJson.getString("role_of_writer"));
                comment_like_count.setText("좋아요 " + resJson.getString("num_of_like") + "개");
                if (resJson.getBoolean("click_like")) {
                    comment_like_btn.setBackgroundResource(R.drawable.like_heart_after);
                    comment_like_count.setTextColor(Color.parseColor("#1fb4ff"));

                }

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
                                            Toast.makeText(Mentor_answer_comment.this, "좋아요!!", Toast.LENGTH_SHORT).show();
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
                });

                replyLayout.addView(commentView);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case PICK_FROM_ALBUM: {
                mImageCaptureUri = data.getData();
                Log.e("data", data.toString());

                try {
                    if (photo_bit != null) {
                        photo_bit.recycle();
                    }

                    String filePath = getPath(mImageCaptureUri);

                    InputStream stream = getContentResolver().openInputStream(mImageCaptureUri);
                    photo_bit = BitmapFactory.decodeStream(stream);
                    photo_bit = Bitmap.createScaledBitmap(photo_bit, 100, 100, false);

                    Picasso.with(Mentor_answer_comment.this).load(mImageCaptureUri).rotate(getExifOrientation(filePath)).fit().into(comment_image);
                    photochange = true;


                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }

            case PICK_FROM_CAMERA: {
                try {

                    if (photo_bit != null) {
                        photo_bit.recycle();
                    }

                    String temp = mImageCaptureUri.toString();
                    temp.replace("file://", "");
                    Uri final_uri = Uri.parse(temp);
                    Log.e("final_uri", final_uri.toString());

                    InputStream stream = getContentResolver().openInputStream(mImageCaptureUri);
                    photo_bit = BitmapFactory.decodeStream(stream);
                    photo_bit = Bitmap.createScaledBitmap(photo_bit, 100, 100, false);

                    Picasso.with(Mentor_answer_comment.this).load(mImageCaptureUri).into(comment_image);
                    photochange = true;

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            }
        }
    }

    public static int getExifOrientation(String filepath) {
        int degree = 0;
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(filepath);
        } catch (IOException ex) {
        }
        if (exif != null) {
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
            Log.e("orientation", orientation + "");
            if (orientation != -1) {
                // We only recognise a subset of orientation tag values.
                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        degree = 90;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        degree = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        degree = 270;
                        break;
                }

            }
        }
        return degree;
    }

    private String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }
}
