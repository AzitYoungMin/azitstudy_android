package com.trams.azit;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;
import com.trams.azit.util.ConnActivity;
import com.trams.azit.util.Url_define;
import com.tsengvn.typekit.TypekitContextWrapper;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Administrator on 2015-10-28.
 */
public class Mento_Profile extends ConnActivity {

    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;

    private Uri mImageCaptureUri;
    EditText et_nick, et_intro, et_goal_university, et_goal_major;
    ImageView main_image, back;
    TextView man, woman, photo, complete, name, email, confirm_nick;
    TextView korean, math, english, social, science, art;
    SharedPreferences myPrefs;
    Bitmap photo_bit;
    boolean photochange = false;
    String secret, user_id, gender, imageURI;
    Boolean koreanselect = false, socialselect = false, mathselect = false, scienceselect = false, englishselect = false, artselect = false;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mento_edit);

        myPrefs = getSharedPreferences("Azit", MODE_PRIVATE);
        secret = myPrefs.getString("secret", "");
        user_id = myPrefs.getString("user_id", "");

        getProfile();

        setButton();
        setBackBtn();
        setComplete();
        setPhoto();
        setMan();
        setSubject();
        setWoman();
        setConfirm_nick();
    }

    private void setConfirm_nick() {
        confirm_nick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("nickname", et_nick.getText().toString());

                    requestJson(Url_define.BASE + "/api/nickname/check" + Url_define.KEY, jsonObject, new ConnActivity.ConnHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.d("statusCode : ", statusCode + "");
                            Log.d("response : ", response.toString());
                            super.onSuccess(statusCode, headers, response);

                            try {
                                if (Boolean.valueOf(response.getString("is_duplicated"))) {
                                    Toast.makeText(Mento_Profile.this, "해당 닉네임이 이미 있습니다.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(Mento_Profile.this, "해당 닉네임은  사용 가능 합니다.", Toast.LENGTH_SHORT).show();
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
    }

    private void getProfile() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("secret", secret);
            jsonObject.put("user_id", user_id);

            Logger.e(secret);
            Logger.e(user_id);
            requestJson(Url_define.Mento_Get_Profile + Url_define.KEY, jsonObject, new ConnHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.d("statusCode : ", statusCode + "");
                    Log.d("response : ", response.toString());
                    super.onSuccess(statusCode, headers, response);

                    try {
                        if (response.getString("profile_image").equals("")) {
                            main_image.setBackgroundResource(R.drawable.profile_basic);
                            imageURI = "";
                        } else {
                            Picasso.with(Mento_Profile.this).load(Url_define.BASE_Image + response.getString("profile_image")).fit().into(main_image);
                            imageURI = response.getString("profile_image");
                        }
                        et_nick.setText(response.getString("nickname"));
                        name.setText(response.getString("name"));
                        email.setText(response.getString("email"));
                        et_intro.setText(response.getString("introduce"));
                        et_goal_university.setText(response.getString("university"));
                        et_goal_major.setText(response.getString("major"));

                        String sb = response.getString("subjects");
                        String[] arr = sb.split(",");

                        for (int i = 0; i < arr.length; i++) {
                            if (arr[i].equals("10000")) {
                                koreanselect = true;
                                korean.setTextColor(Color.parseColor("#1FB4FF"));
                            } else if (arr[i].equals("30000")) {
                                math.setTextColor(Color.parseColor("#1FB4FF"));
                                mathselect = true;
                            } else if (arr[i].equals("50000")) {
                                english.setTextColor(Color.parseColor("#1FB4FF"));
                                englishselect = true;
                            } else if (arr[i].equals("60000")) {
                                social.setTextColor(Color.parseColor("#1FB4FF"));
                                socialselect = true;
                            } else if (arr[i].equals("70000")) {
                                science.setTextColor(Color.parseColor("#1FB4FF"));
                                scienceselect = true;
                            } else if (arr[i].equals("130000")) {
                                art.setTextColor(Color.parseColor("#1FB4FF"));
                                artselect = true;
                            }

                        }

                        if (response.getString("gender").equals("F")) {
                            woman.setBackgroundResource(R.drawable.woman_push);
                            gender = "F";
                        } else {
                            man.setBackgroundResource(R.drawable.man_push);
                            gender = "M";
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

    private void setSubject() {
        korean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (koreanselect) {
                    korean.setTextColor(Color.parseColor("#000000"));
                    koreanselect = false;
                } else {
                    korean.setTextColor(Color.parseColor("#1FB4FF"));
                    koreanselect = true;
                }

            }
        });
        math.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mathselect) {
                    math.setTextColor(Color.parseColor("#000000"));
                    mathselect = false;
                } else {
                    math.setTextColor(Color.parseColor("#1FB4FF"));
                    mathselect = true;
                }

            }
        });
        english.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (englishselect) {
                    english.setTextColor(Color.parseColor("#000000"));
                    englishselect = false;
                } else {
                    english.setTextColor(Color.parseColor("#1FB4FF"));
                    englishselect = true;
                }
            }
        });
        social.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (socialselect) {
                    social.setTextColor(Color.parseColor("#000000"));
                    socialselect = false;
                } else {
                    social.setTextColor(Color.parseColor("#1FB4FF"));
                    socialselect = true;
                }

            }
        });
        science.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (scienceselect) {
                    science.setTextColor(Color.parseColor("#000000"));
                    scienceselect = false;
                } else {
                    science.setTextColor(Color.parseColor("#1FB4FF"));
                    scienceselect = true;
                }

            }
        });
        art.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (artselect) {
                    art.setTextColor(Color.parseColor("#000000"));
                    artselect = false;
                } else {
                    art.setTextColor(Color.parseColor("#1FB4FF"));
                    artselect = true;
                }
            }
        });

    }

    private void setMan() {
        man.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                man.setBackgroundResource(R.drawable.man_push);
                woman.setBackgroundResource(R.drawable.woman);
                gender = "M";
            }
        });
    }

    private void setWoman() {
        woman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                man.setBackgroundResource(R.drawable.man);
                woman.setBackgroundResource(R.drawable.woman_push);
                gender = "F";
            }
        });
    }

    private void setPhoto() {
        photo.setOnClickListener(new View.OnClickListener() {
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

                new AlertDialog.Builder(Mento_Profile.this)
                        .setTitle("업로드할 이미지 선택")
                        .setPositiveButton("사진촬영", cameraListener)
                        .setNeutralButton("앨범선택", albumListener)
                        .setNegativeButton("취소", cancelListener)
                        .show();
            }
        });
    }


    private void setComplete() {
        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("secret", secret);
                    jsonObject.put("user_id", user_id);
                    jsonObject.put("nickname", et_nick.getText().toString());
                    jsonObject.put("name", name.getText().toString());
                    jsonObject.put("gender", gender);
                    jsonObject.put("profile_image", imageURI);
                    jsonObject.put("university", et_goal_university.getText().toString());
                    jsonObject.put("major", et_goal_major.getText().toString());
                    jsonObject.put("introduce", et_intro.getText().toString());

                    String subjects = "";
                    if (koreanselect) {
                        subjects += ",10000";
                    }
                    if (mathselect) {
                        subjects += ",30000";
                    }
                    if (englishselect) {
                        subjects += ",50000";
                    }
                    if (socialselect) {
                        subjects += ",60000";
                    }
                    if (scienceselect) {
                        subjects += ",70000";
                    }
                    if (artselect) {
                        subjects += ",130000";
                    }
                    jsonObject.put("subjects", subjects.replaceFirst(",", ""));

                    RequestParams params = new RequestParams();
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    if (photochange) {
                        ByteArrayOutputStream bos1 = new ByteArrayOutputStream();
                        BitmapDrawable d1 = (BitmapDrawable) ((ImageView) findViewById(R.id.main_image)).getDrawable();
                        Bitmap b1 = d1.getBitmap();
                        b1.compress(Bitmap.CompressFormat.JPEG, 100, bos1);
                        params.put("profile_image", new ByteArrayInputStream(bos1.toByteArray()), "profile", "image/jpeg");
                    }

                    requestMultipart(Url_define.Mento_Edit_Profile + Url_define.KEY, jsonObject, params, new ConnHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.d("statusCode : ", statusCode + "");
                            Log.d("response : ", response.toString());
                            super.onSuccess(statusCode, headers, response);

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

    private void setBackBtn() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setButton() {
        main_image = (ImageView) findViewById(R.id.main_image);
        back = (ImageView) findViewById(R.id.back_btn);
        man = (TextView) findViewById(R.id.man);
        woman = (TextView) findViewById(R.id.woman);
        photo = (TextView) findViewById(R.id.photo);
        complete = (TextView) findViewById(R.id.complete);
        korean = (TextView) findViewById(R.id.korean);
        math = (TextView) findViewById(R.id.math);
        english = (TextView) findViewById(R.id.english);
        social = (TextView) findViewById(R.id.social);
        science = (TextView) findViewById(R.id.science);
        art = (TextView) findViewById(R.id.art);
        confirm_nick= (TextView) findViewById(R.id.confirm_nick);

        et_nick = (EditText) findViewById(R.id.et_nick);
        et_intro = (EditText) findViewById(R.id.et_intro);
        et_goal_university = (EditText) findViewById(R.id.et_goal_university);
        et_goal_major = (EditText) findViewById(R.id.et_goal_major);

        name = (TextView) findViewById(R.id.name);
        email = (TextView) findViewById(R.id.email);

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

                    Picasso.with(Mento_Profile.this).load(mImageCaptureUri).rotate(getExifOrientation(filePath)).fit().into(main_image);
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

                    Picasso.with(Mento_Profile.this).load(mImageCaptureUri).into(main_image);
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
