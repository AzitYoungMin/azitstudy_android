package com.trams.azit;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.trams.azit.LoginActivity_;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;
import com.trams.azit.activity.MentorJoinStep1Activity;
import com.trams.azit.util.ConnActivity;
import com.trams.azit.util.MyProgressDialog;
import com.trams.azit.util.Url_define;
import com.tsengvn.typekit.TypekitContextWrapper;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;

/**
 * Created by Administrator on 2015-09-02.
 */
@EActivity(R.layout.activity_regi_basic)
public class Regi_mento extends ConnActivity {

    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;

    private Uri mImageCaptureUri;

    Bitmap photo_bit;
    boolean photochange = false;
    ImageView main_image;

    MyProgressDialog pd;
    @ViewById
    TextView man, woman, my_grade;
    @ViewById
    AutoCompleteTextView et_school;
    @ViewById
    EditText et_name, et_nick, et_email, et_pwd, et_pwd_check, et_phone;

    String genderFlag, edu_inst_idValue;
    private JSONObject jsonObject;
    Hashtable<String, String> hashTable;

    String emailStr, passStr;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @AfterViews
    protected void init() {

        final String[] arrayOfSchool = getResources().getStringArray(R.array.school_name);
        final String[] arrayOfSchoolId = getResources().getStringArray(R.array.school_id);

        hashTable = new Hashtable<String, String>();
        for (int i = 0; i < arrayOfSchool.length; i++) {
            hashTable.put(arrayOfSchool[i], arrayOfSchoolId[i]);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, arrayOfSchool);
        et_school.setAdapter(adapter);

        et_school.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < arrayOfSchool.length; i++) {
                    if (arrayOfSchool[i].equals(et_school.getText().toString())) {
                        edu_inst_idValue = arrayOfSchoolId[i];

                    }
                }

                Log.e(et_school.getText().toString(), hashTable.get(et_school.getText().toString()));
            }
        });

        genderFlag = "M";
        my_grade.setText("대1");

        man.setBackgroundResource(R.color.dark_grey);
        woman.setBackgroundResource(R.color.dark_grey);

        emailStr = getIntent().getStringExtra(MentorJoinStep1Activity.REGISTER_EMAIL);
        passStr = getIntent().getStringExtra(MentorJoinStep1Activity.REGISTER_PASS);

        main_image = (ImageView) findViewById(R.id.main_image);

    }

    @Click(R.id.back_btn)
    protected void setBack_btn() {
        finish();
    }

    @Click(R.id.pick_image)
    protected void pickImage() {
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

        new AlertDialog.Builder(this)
                .setTitle("업로드할 이미지 선택")
                .setPositiveButton("사진촬영", cameraListener)
                .setNeutralButton("앨범선택", albumListener)
                .setNegativeButton("취소", cancelListener)
                .show();
    }

    @Click(R.id.confirm_email)
    protected void setCnfirm_email() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("email", et_email.getText().toString());

            requestJson(Url_define.BASE + "/api/email/check" + Url_define.KEY, jsonObject, new ConnHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.d("statusCode : ", statusCode + "");
                    Log.d("response : ", response.toString());
                    super.onSuccess(statusCode, headers, response);

                    try {
                        if (Boolean.valueOf(response.getString("is_duplicated"))) {
                            Toast.makeText(Regi_mento.this, "해당 이메일이 이미 있습니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Regi_mento.this, "해당 이메일은  사용 가능 합니다.", Toast.LENGTH_SHORT).show();
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

    @Click(R.id.confirm_nick)
    protected void setConfirm_nick() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("nickname", et_nick.getText().toString());

            requestJson(Url_define.BASE + "/api/nickname/check" + Url_define.KEY, jsonObject, new ConnHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.d("statusCode : ", statusCode + "");
                    Log.d("response : ", response.toString());
                    super.onSuccess(statusCode, headers, response);

                    try {
                        if (Boolean.valueOf(response.getString("is_duplicated"))) {
                            Toast.makeText(Regi_mento.this, "해당 닉네임이 이미 있습니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Regi_mento.this, "해당 닉네임은  사용 가능 합니다.", Toast.LENGTH_SHORT).show();
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

    @Click(R.id.my_grade)
    protected void setMy_grade() {
        final CharSequence[] items = {"대1", "대2", "대3", "대4", "휴학생", "졸업생"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("학년 선택");

        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                my_grade.setText(items[item]);
            }
        });
        AlertDialog alert = builder.create();
        alert.show();

    }

    @Click(R.id.man)
    protected void setMan() {
        man.setBackgroundResource(R.color.btn_bg_blue);
        woman.setBackgroundResource(R.color.dark_grey);
        genderFlag = "M";
        Log.d("gender", genderFlag);
    }

    @Click(R.id.woman)
    protected void setWoman() {
        man.setBackgroundResource(R.color.dark_grey);
        woman.setBackgroundResource(R.color.btn_bg_blue);
        genderFlag = "F";
        Log.d("gender", genderFlag);
    }

    @Click(R.id.btnComplete)
    protected void setComplete() {
        Log.d("complete", "complete");
        if (textNullcheck(et_name) == true || /*textNullcheck(et_email) == true ||*/
               /* textNullcheck(et_pwd) == true ||*/ textNullcheck(et_nick) == true ||
                textNullcheck(et_school) == true ||/* textNullcheck(et_pwd_check) == true ||*/ textNullcheck(my_grade) == true) {
            Toast.makeText(getApplicationContext(), "입력되지 않은 데이터가 있습니다. 확인해주세요!", Toast.LENGTH_SHORT).show();
            et_name.requestFocus();
            return;
        } /*else if (!(et_pwd_check.getText().toString().equals(et_pwd.getText().toString()))) {
            Toast.makeText(getApplicationContext(), "비밀번호와 확인칸이 일치하지않습니다. 확인해주세요!", Toast.LENGTH_SHORT).show();
            et_pwd.requestFocus();
        }*/ else {
            String year = my_grade.getText().toString();
            int yearCode = 1;
            if (year.equals("대1")) yearCode = 1;
            else if (year.equals("대2")) yearCode = 2;
            else if (year.equals("대3")) yearCode = 3;
            else if (year.equals("대4")) yearCode = 4;
            else if (year.equals("휴학생")) yearCode = 5;
            Log.d("year", yearCode + "");
            try {
                jsonObject = new JSONObject();
                jsonObject.put("name", et_name.getText().toString());
                jsonObject.put("email", emailStr);
                jsonObject.put("nickname", et_nick.getText().toString());
                jsonObject.put("password", passStr);
                jsonObject.put("edu_inst_id", edu_inst_idValue);//추후 학교코드로 변경해야함!
                jsonObject.put("phone", et_phone.getText().toString());
                jsonObject.put("year", yearCode);
                jsonObject.put("gender", genderFlag);

                Log.d("beforejsonObject : ", jsonObject.toString());

                /*if (jsonObject.has("edu_inst_id")) {
                    requestJson(Url_define.BASE + "/api/mentor/signup" + Url_define.KEY, jsonObject, new ConnHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject res) {
                            Log.d("statusCode", statusCode + "");
                            Log.d("onSuccess res", res.toString());
                            try {
                                String str = (String) res.get("message");
                                if (str.equals("duplicated email")) {
                                    Toast.makeText(getApplicationContext(), "중복된 이메일이 존재합니다.", Toast.LENGTH_SHORT).show();
                                } else if (str.equals("invalid body")) {
                                    Toast.makeText(getApplicationContext(), "잘못된 입력입니다. 다시 확인해주세요.", Toast.LENGTH_SHORT).show();
                                } else if (!res.get("result").toString().equals("fail")) {
                                    Toast.makeText(getApplicationContext(), "잠시만 기다려 주세요.", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(Regi_mento.this, LoginActivity_.class);
                                    startActivity(i);
                                    finish();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable t, JSONObject res) {
                            if (res.toString() == null) Log.d("log", "으앙쥬금 ㅠㅠ");
                            Log.d("statusCode", statusCode + "");
                            Log.d("throwable", String.valueOf(t.getMessage()));
                            Log.d("onFailure res", res.toString());
                            Log.d("jsonObject : ", jsonObject.toString());
                        }

                    });
                } else {
                    Toast.makeText(getApplicationContext(), "학교가 잘못 선택 되엇습니다.", Toast.LENGTH_SHORT).show();
                }*/

                RequestParams params = new RequestParams();
                if (photochange) {

                    ByteArrayOutputStream bos1 = new ByteArrayOutputStream();
                    BitmapDrawable d1 = (BitmapDrawable) ((ImageView) findViewById(R.id.main_image)).getDrawable();
                    Bitmap b1 = d1.getBitmap();
                    b1.compress(Bitmap.CompressFormat.JPEG, 100, bos1);
                    params.put("card_image", new ByteArrayInputStream(bos1.toByteArray()), "card_image", "image/jpeg");

                    requestMultipart(Url_define.BASE + "/api/mentor/signup" + Url_define.KEY, jsonObject, params, new ConnHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject res) {
                            Log.d("statusCode", statusCode + "");
                            Log.d("onSuccess res", res.toString());
                            try {
                                String str = (String) res.get("message");
                                if (str.equals("duplicated email")) {
                                    Toast.makeText(getApplicationContext(), "중복된 이메일이 존재합니다.", Toast.LENGTH_SHORT).show();
                                } else if (str.equals("invalid body")) {
                                    Toast.makeText(getApplicationContext(), "잘못된 입력입니다. 다시 확인해주세요.", Toast.LENGTH_SHORT).show();
                                } else if (res.get("result").toString().equals("success")) {
                                    Toast.makeText(getApplicationContext(), "회원 가입에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(Regi_mento.this, LoginActivity_.class);
                                    startActivity(i);
                                    finish();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                            if (res.toString() == null) Log.d("log", "으앙쥬금 ㅠㅠ");
                            Log.d("statusCode", statusCode + "");
                            Log.d("throwable", String.valueOf(t.getMessage()));
                            Log.d("onFailure res", res.toString());
                            Log.d("jsonObject : ", jsonObject.toString());
                        }
                    });
                } else {
                    Toast.makeText(Regi_mento.this, "학생증 사진이 업습니다.", Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.getStackTrace();
            }

        }

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case PICK_FROM_ALBUM: {
                pd = MyProgressDialog.show(this, "", "", true, false, null);
                pd.show();
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

                    Picasso.with(Regi_mento.this).load(mImageCaptureUri).rotate(getExifOrientation(filePath)).resize(400, 400).into(main_image, new Callback() {
                        @Override
                        public void onSuccess() {
                            pd.dismiss();
                        }

                        @Override
                        public void onError() {

                        }
                    });
                    photochange = true;


                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }

            case PICK_FROM_CAMERA: {
                try {
                    pd = MyProgressDialog.show(this, "", "", true, false, null);
                    pd.show();
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

                    Picasso.with(Regi_mento.this).load(mImageCaptureUri).resize(400, 400).into(main_image, new Callback() {
                        @Override
                        public void onSuccess() {
                            pd.dismiss();
                        }

                        @Override
                        public void onError() {

                        }
                    });photochange = true;

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
