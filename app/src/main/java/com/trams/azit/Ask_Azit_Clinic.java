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
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.trams.azit.util.MyProgressDialog;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import com.trams.azit.dialog.TextbookPopDialog;
import com.trams.azit.model.ScheduleTextBookModel;
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
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Administrator on 2015-09-23.
 */
public class Ask_Azit_Clinic extends ConnActivity {

    ImageView cancel;
    TextView korean, social, math, science, english, art, album, photo;
    Button regi;
    Boolean status = false;
    EditText page, question_num, body;
    //    EditText title;
    TextView textbook;
    public static String subject_id;
    Bitmap photo_bit;
    boolean hasphoto = false, hasimage1 = false, hasimage2 = false, hasimage3 = false;
    private Uri mImageCaptureUri;
    ImageView image1, image2, image3, main_image;
    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    SharedPreferences myPrefs;
    ArrayList<String> deleted_images = new ArrayList<>();
    ArrayList<String> images_id = new ArrayList<>();
    String secret, user_id, mImgPath, textbookId, customTextbookId;
    MyProgressDialog pd;

    public static String socialName;
    public static String scienceName;

    private ScheduleTextBookModel scheduleTextBookModel;
    private static final String TAG = Ask_Azit_Clinic.class.getName();

    private ImageView imgDelete1, imgDelete2, imgDelete3;
    int posting_id;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_azit_clinic);

        myPrefs = getSharedPreferences("Azit", MODE_PRIVATE);
        secret = myPrefs.getString("secret", "");
        user_id = myPrefs.getString("user_id", "");
        posting_id = getIntent().getIntExtra("posting_id", 0);

        socialName = "사탐";
        scienceName = "과탐";
        textbookId = "0";
        customTextbookId = "0";
        subject_id = "10000";
//        subject_id = getIntent().getStringExtra("subject_id");

        cancel = (ImageView) findViewById(R.id.cancel);
        korean = (TextView) findViewById(R.id.korean);
        social = (TextView) findViewById(R.id.social);
        math = (TextView) findViewById(R.id.math);
        science = (TextView) findViewById(R.id.science);
        english = (TextView) findViewById(R.id.english);
        art = (TextView) findViewById(R.id.art);
        album = (TextView) findViewById(R.id.album);
        photo = (TextView) findViewById(R.id.photo);
        regi = (Button) findViewById(R.id.regi_btn);
        textbook = (TextView) findViewById(R.id.textbook);
        page = (EditText) findViewById(R.id.page);
        question_num = (EditText) findViewById(R.id.question_num);
//        title = (EditText) findViewById(R.id.title);
        body = (EditText) findViewById(R.id.body);
        image1 = (ImageView) findViewById(R.id.image1);
        image2 = (ImageView) findViewById(R.id.image2);
        image3 = (ImageView) findViewById(R.id.image3);



        imgDelete1 = (ImageView) findViewById(R.id.img_delete1);
        imgDelete1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgDelete1.setVisibility(View.INVISIBLE);
                image1.setImageResource(R.drawable.add_pic);
                hasimage1 = false;
                if (posting_id != 0) {
                    deleted_images.add(images_id.get(0));
                }
            }
        });

        imgDelete2 = (ImageView) findViewById(R.id.img_delete2);
        imgDelete2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgDelete2.setVisibility(View.INVISIBLE);
                image2.setImageResource(R.drawable.add_pic);
                hasimage2 = false;
                if (posting_id != 0) {
                    deleted_images.add(images_id.get(1));
                }
            }
        });

        imgDelete3 = (ImageView) findViewById(R.id.img_delete3);
        imgDelete3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgDelete3.setVisibility(View.INVISIBLE);
                image3.setImageResource(R.drawable.add_pic);
                hasimage3 = false;
                if (posting_id != 0) {
                    deleted_images.add(images_id.get(2));
                }
            }
        });

        textbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextbookPopDialog textbookPopDialog = new TextbookPopDialog(Ask_Azit_Clinic.this, Integer.valueOf(subject_id));
                textbookPopDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

                textbookPopDialog.setTextbookPopDialogListener(new TextbookPopDialog.TextbookPopDialogListener() {
                    @Override
                    public void onComplete(ScheduleTextBookModel textBookModel) {
                        scheduleTextBookModel = textBookModel;
                        textbook.setText(textBookModel.getName());
                        if (textBookModel.getIsCustom()) {
                            customTextbookId = String.valueOf(scheduleTextBookModel.getId());
                        } else {
                            textbookId = String.valueOf(scheduleTextBookModel.getId());
                        }
                    }
                });

                textbookPopDialog.showView();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener confirmListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(Ask_Azit_Clinic.this, StudentMainActivity.class);
                        i.putExtra("position", 5);
                        startActivity(i);
                        finish();
                    }
                };

                DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                };

                new AlertDialog.Builder(Ask_Azit_Clinic.this)
                        .setTitle("해당 질문을 삭제하시겠어요?")
                        .setPositiveButton("확인", confirmListener)
                        .setNegativeButton("취소", cancelListener)
                        .show();
            }
        });

        korean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                korean.setTextColor(Color.parseColor("#ffffff"));
                korean.setBackgroundResource(R.drawable.ask_clinic_bg_green);

                social.setTextColor(Color.parseColor("#000000"));
                social.setBackgroundResource(R.drawable.ask_clinic_bg);

                math.setTextColor(Color.parseColor("#000000"));
                math.setBackgroundResource(R.drawable.ask_clinic_bg);

                science.setTextColor(Color.parseColor("#000000"));
                science.setBackgroundResource(R.drawable.ask_clinic_bg);

                english.setTextColor(Color.parseColor("#000000"));
                english.setBackgroundResource(R.drawable.ask_clinic_bg);

                art.setTextColor(Color.parseColor("#000000"));
                art.setBackgroundResource(R.drawable.ask_clinic_bg);

                subject_id = "10000";
            }
        });

        social.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                korean.setTextColor(Color.parseColor("#000000"));
                korean.setBackgroundResource(R.drawable.ask_clinic_bg);

                social.setTextColor(Color.parseColor("#ffffff"));
                social.setBackgroundResource(R.drawable.ask_clinic_bg_green);

                math.setTextColor(Color.parseColor("#000000"));
                math.setBackgroundResource(R.drawable.ask_clinic_bg);

                science.setTextColor(Color.parseColor("#000000"));
                science.setBackgroundResource(R.drawable.ask_clinic_bg);

                english.setTextColor(Color.parseColor("#000000"));
                english.setBackgroundResource(R.drawable.ask_clinic_bg);

                art.setTextColor(Color.parseColor("#000000"));
                art.setBackgroundResource(R.drawable.ask_clinic_bg);

                subject_id = "60000";

                Intent i = new Intent(Ask_Azit_Clinic.this, ChoiceStudy.class);
                i.putExtra("title", "사탐");
                startActivity(i);

            }
        });

        math.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                korean.setTextColor(Color.parseColor("#000000"));
                korean.setBackgroundResource(R.drawable.ask_clinic_bg);

                social.setTextColor(Color.parseColor("#000000"));
                social.setBackgroundResource(R.drawable.ask_clinic_bg);

                math.setTextColor(Color.parseColor("#ffffff"));
                math.setBackgroundResource(R.drawable.ask_clinic_bg_green);

                science.setTextColor(Color.parseColor("#000000"));
                science.setBackgroundResource(R.drawable.ask_clinic_bg);

                english.setTextColor(Color.parseColor("#000000"));
                english.setBackgroundResource(R.drawable.ask_clinic_bg);

                art.setTextColor(Color.parseColor("#000000"));
                art.setBackgroundResource(R.drawable.ask_clinic_bg);

                subject_id = "30000";
            }
        });

        science.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                korean.setTextColor(Color.parseColor("#000000"));
                korean.setBackgroundResource(R.drawable.ask_clinic_bg);

                social.setTextColor(Color.parseColor("#000000"));
                social.setBackgroundResource(R.drawable.ask_clinic_bg);

                math.setTextColor(Color.parseColor("#000000"));
                math.setBackgroundResource(R.drawable.ask_clinic_bg);

                science.setTextColor(Color.parseColor("#ffffff"));
                science.setBackgroundResource(R.drawable.ask_clinic_bg_green);

                english.setTextColor(Color.parseColor("#000000"));
                english.setBackgroundResource(R.drawable.ask_clinic_bg);

                art.setTextColor(Color.parseColor("#000000"));
                art.setBackgroundResource(R.drawable.ask_clinic_bg);

                subject_id = "70000";

                Intent i = new Intent(Ask_Azit_Clinic.this, ChoiceStudy.class);
                i.putExtra("title", "과탐");
                startActivity(i);

            }
        });

        english.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                korean.setTextColor(Color.parseColor("#000000"));
                korean.setBackgroundResource(R.drawable.ask_clinic_bg);

                social.setTextColor(Color.parseColor("#000000"));
                social.setBackgroundResource(R.drawable.ask_clinic_bg);

                math.setTextColor(Color.parseColor("#000000"));
                math.setBackgroundResource(R.drawable.ask_clinic_bg);

                science.setTextColor(Color.parseColor("#000000"));
                science.setBackgroundResource(R.drawable.ask_clinic_bg);

                english.setTextColor(Color.parseColor("#ffffff"));
                english.setBackgroundResource(R.drawable.ask_clinic_bg_green);

                art.setTextColor(Color.parseColor("#000000"));
                art.setBackgroundResource(R.drawable.ask_clinic_bg);
                subject_id = "50000";
            }
        });

        art.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                korean.setTextColor(Color.parseColor("#000000"));
                korean.setBackgroundResource(R.drawable.ask_clinic_bg);

                social.setTextColor(Color.parseColor("#000000"));
                social.setBackgroundResource(R.drawable.ask_clinic_bg);

                math.setTextColor(Color.parseColor("#000000"));
                math.setBackgroundResource(R.drawable.ask_clinic_bg);

                science.setTextColor(Color.parseColor("#000000"));
                science.setBackgroundResource(R.drawable.ask_clinic_bg);

                english.setTextColor(Color.parseColor("#000000"));
                english.setBackgroundResource(R.drawable.ask_clinic_bg);

                art.setTextColor(Color.parseColor("#ffffff"));
                art.setBackgroundResource(R.drawable.ask_clinic_bg_green);

                subject_id = "130000";
            }
        });

        album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                album.setTextColor(Color.parseColor("#e9e9e9"));
                album.setBackgroundResource(R.drawable.album_bdg_push);
                photo.setTextColor(Color.parseColor("#444444"));
                photo.setBackgroundResource(R.drawable.photo_bdg);
                status = false;
            }
        });

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                album.setTextColor(Color.parseColor("#444444"));
                album.setBackgroundResource(R.drawable.album_bdg);
                photo.setTextColor(Color.parseColor("#e9e9e9"));
                photo.setBackgroundResource(R.drawable.photo_bdg_push);
                status = true;
            }
        });
        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main_image = image1;
                hasimage1 = true;
                if (status) {
                    doTakePhotoAction();
                } else {
                    doTakeAlbumAction();
                }
            }
        });
        image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main_image = image2;
                hasimage2 = true;
                if (status) {
                    doTakePhotoAction();
                } else {
                    doTakeAlbumAction();
                }
            }
        });
        image3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main_image = image3;
                hasimage3 = true;
                if (status) {
                    doTakePhotoAction();
                } else {
                    doTakeAlbumAction();
                }
            }
        });
        regi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pd = MyProgressDialog.show(Ask_Azit_Clinic.this, "", "", true, false, null);

                try {
                    String url = Url_define.Student_Save_Ask_Clinic + Url_define.KEY;
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("secret", secret);
                    jsonObject.put("user_id", user_id);
                    jsonObject.put("title", "");
                    jsonObject.put("article", body.getText().toString());
                    jsonObject.put("subject_id", subject_id);
                    jsonObject.put("textbook_id", textbookId);
                    jsonObject.put("custom_textbook_id", customTextbookId);
                    jsonObject.put("textbook_page", page.getText().toString());
                    jsonObject.put("question_number", question_num.getText().toString());

                    if (posting_id != 0) {
                        jsonObject.put("posting_id", posting_id);
                        String deletedImages = "";
                        for (String temp : deleted_images) {
                            deletedImages += temp + ",";
                        }
                        if (!deletedImages.equals("")) {

                            deletedImages = deletedImages.substring(0, deletedImages.length() - 1);
                        }
                        jsonObject.put("deleted_images", deletedImages);
                        url = Url_define.Student_Update_Ask_Clinic + Url_define.KEY;

                    }

                    RequestParams params = new RequestParams();
                    if (hasphoto) {
                        if (hasimage1) {
                            BitmapFactory.Options op = new BitmapFactory.Options();
                            op.inSampleSize = 4;

                            ByteArrayOutputStream bos1 = new ByteArrayOutputStream();
//                            BitmapDrawable d1 = (BitmapDrawable) image1.getDrawable();
//                            Bitmap b1 = d1.getBitmap();
                            Log.e("image1", image1.getTag().toString());
                            Bitmap b1 = BitmapFactory.decodeFile(image1.getTag().toString(), op);

                            Matrix matrix = new Matrix();
                            matrix.postRotate(getExifOrientation(image1.getTag().toString()));

                            b1 = Bitmap.createBitmap(b1,0,0,b1.getWidth(), b1.getHeight(), matrix, true);
                            b1.compress(Bitmap.CompressFormat.JPEG, 100, bos1);
                            params.put("profile_image1", new ByteArrayInputStream(bos1.toByteArray()), "image1", "image/jpeg");
                        }
                        if (hasimage2) {
                            BitmapFactory.Options op = new BitmapFactory.Options();
                            op.inSampleSize = 4;

                            ByteArrayOutputStream bos2 = new ByteArrayOutputStream();
//                            BitmapDrawable d2 = (BitmapDrawable) image2.getDrawable();
                            Bitmap b2 = BitmapFactory.decodeFile(image2.getTag().toString(), op);

                            Matrix matrix = new Matrix();
                            matrix.postRotate(getExifOrientation(image2.getTag().toString()));

                            b2 = Bitmap.createBitmap(b2,0,0,b2.getWidth(), b2.getHeight(), matrix, true);

                            b2.compress(Bitmap.CompressFormat.JPEG, 100, bos2);
                            params.put("profile_image2", new ByteArrayInputStream(bos2.toByteArray()), "image2", "image/jpeg");
                        }
                        if (hasimage3) {
                            BitmapFactory.Options op = new BitmapFactory.Options();
                            op.inSampleSize = 4;

                            ByteArrayOutputStream bos3 = new ByteArrayOutputStream();
//                            BitmapDrawable d3 = (BitmapDrawable) image3.getDrawable();
                            Bitmap b3 = BitmapFactory.decodeFile(image3.getTag().toString(), op);

                            Matrix matrix = new Matrix();
                            matrix.postRotate(getExifOrientation(image3.getTag().toString()));

                            b3 = Bitmap.createBitmap(b3,0,0,b3.getWidth(), b3.getHeight(), matrix, true);

                            b3.compress(Bitmap.CompressFormat.JPEG, 100, bos3);
                            params.put("profile_image3", new ByteArrayInputStream(bos3.toByteArray()), "image3", "image/jpeg");
                        }
                    }

                    requestMultipart(url, jsonObject, params, new ConnHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.d("statusCode : ", statusCode + "");
                            Log.d("response : ", response.toString());
                            super.onSuccess(statusCode, headers, response);

                            if (posting_id != 0) {
                                Toast.makeText(Ask_Azit_Clinic.this, "수정 완료!", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(Ask_Azit_Clinic.this, "등록 완료!", Toast.LENGTH_LONG).show();
                            }

                            if (pd != null) {
                                if (pd.isShowing()) {
                                    pd.dismiss();
                                }
                            }
                            Intent i = new Intent(Ask_Azit_Clinic.this, StudentMainActivity.class);
                            i.putExtra("position", 5);
                            startActivity(i);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                            Log.d("statusCode : ", statusCode + "");
                            super.onFailure(statusCode, headers, res, t);
                            if (pd != null) {
                                if (pd.isShowing()) {
                                    pd.dismiss();
                                }
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.getStackTrace();
                }

            }
        });

        if (posting_id != 0) {
            getEditData();
        }

    }

    private void getEditData() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("secret", secret);
            jsonObject.put("user_id", user_id);
            jsonObject.put("posting_id", posting_id);

            requestJson(Url_define.Student_Get_Ask_Clinic + Url_define.KEY, jsonObject, new ConnHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    try {
                        if (response.getString("result").equals("success")) {
                            page.setText(response.getString("textbook_page"));
                            body.setText(response.getString("article"));
                            question_num.setText(response.getString("question_number"));
                            textbook.setText(response.getString("textbook"));
                            textbookId = response.getString("textbook_id");
                            customTextbookId = response.getString("custom_textbook_id");
                            subject_id = response.getString("sub_subject");
                            int subjectInt = Integer.parseInt(subject_id) / 10000;
                            if (subjectInt == 1) {
                                korean.setTextColor(Color.parseColor("#ffffff"));
                                korean.setBackgroundResource(R.drawable.ask_clinic_bg_green);

                                social.setTextColor(Color.parseColor("#000000"));
                                social.setBackgroundResource(R.drawable.ask_clinic_bg);

                                math.setTextColor(Color.parseColor("#000000"));
                                math.setBackgroundResource(R.drawable.ask_clinic_bg);

                                science.setTextColor(Color.parseColor("#000000"));
                                science.setBackgroundResource(R.drawable.ask_clinic_bg);

                                english.setTextColor(Color.parseColor("#000000"));
                                english.setBackgroundResource(R.drawable.ask_clinic_bg);

                                art.setTextColor(Color.parseColor("#000000"));
                                art.setBackgroundResource(R.drawable.ask_clinic_bg);
                            } else if (subjectInt == 3) {
                                korean.setTextColor(Color.parseColor("#000000"));
                                korean.setBackgroundResource(R.drawable.ask_clinic_bg);

                                social.setTextColor(Color.parseColor("#000000"));
                                social.setBackgroundResource(R.drawable.ask_clinic_bg);

                                math.setTextColor(Color.parseColor("#ffffff"));
                                math.setBackgroundResource(R.drawable.ask_clinic_bg_green);

                                science.setTextColor(Color.parseColor("#000000"));
                                science.setBackgroundResource(R.drawable.ask_clinic_bg);

                                english.setTextColor(Color.parseColor("#000000"));
                                english.setBackgroundResource(R.drawable.ask_clinic_bg);

                                art.setTextColor(Color.parseColor("#000000"));
                                art.setBackgroundResource(R.drawable.ask_clinic_bg);
                            } else if (subjectInt == 5) {
                                korean.setTextColor(Color.parseColor("#000000"));
                                korean.setBackgroundResource(R.drawable.ask_clinic_bg);

                                social.setTextColor(Color.parseColor("#000000"));
                                social.setBackgroundResource(R.drawable.ask_clinic_bg);

                                math.setTextColor(Color.parseColor("#000000"));
                                math.setBackgroundResource(R.drawable.ask_clinic_bg);

                                science.setTextColor(Color.parseColor("#000000"));
                                science.setBackgroundResource(R.drawable.ask_clinic_bg);

                                english.setTextColor(Color.parseColor("#ffffff"));
                                english.setBackgroundResource(R.drawable.ask_clinic_bg_green);

                                art.setTextColor(Color.parseColor("#000000"));
                                art.setBackgroundResource(R.drawable.ask_clinic_bg);
                            } else if (subjectInt == 6) {
                                korean.setTextColor(Color.parseColor("#000000"));
                                korean.setBackgroundResource(R.drawable.ask_clinic_bg);

                                social.setTextColor(Color.parseColor("#ffffff"));
                                social.setBackgroundResource(R.drawable.ask_clinic_bg_green);

                                math.setTextColor(Color.parseColor("#000000"));
                                math.setBackgroundResource(R.drawable.ask_clinic_bg);

                                science.setTextColor(Color.parseColor("#000000"));
                                science.setBackgroundResource(R.drawable.ask_clinic_bg);

                                english.setTextColor(Color.parseColor("#000000"));
                                english.setBackgroundResource(R.drawable.ask_clinic_bg);

                                art.setTextColor(Color.parseColor("#000000"));
                                art.setBackgroundResource(R.drawable.ask_clinic_bg);

                                switch (subject_id) {
                                    case "60100":
                                        social.setText("경제");
                                        break;
                                    case "60200":
                                        social.setText("법과 정치");
                                        break;
                                    case "60300":
                                        social.setText("사회문화");
                                        break;
                                    case "60400":
                                        social.setText("한국사");
                                        break;
                                    case "60500":
                                        social.setText("세계사");
                                        break;
                                    case "60600":
                                        social.setText("동아시아사");
                                        break;
                                    case "60700":
                                        social.setText("한국지리");
                                        break;
                                    case "60800":
                                        social.setText("세계지리");
                                        break;
                                    case "60900":
                                        social.setText("생활과 윤리");
                                        break;
                                    case "61000":
                                        social.setText("윤리와 사상");
                                        break;
                                    default:
                                        social.setText("사탐");
                                        break;
                                }

                            } else if (subjectInt == 7) {
                                korean.setTextColor(Color.parseColor("#000000"));
                                korean.setBackgroundResource(R.drawable.ask_clinic_bg);

                                social.setTextColor(Color.parseColor("#000000"));
                                social.setBackgroundResource(R.drawable.ask_clinic_bg);

                                math.setTextColor(Color.parseColor("#000000"));
                                math.setBackgroundResource(R.drawable.ask_clinic_bg);

                                science.setTextColor(Color.parseColor("#ffffff"));
                                science.setBackgroundResource(R.drawable.ask_clinic_bg_green);

                                english.setTextColor(Color.parseColor("#000000"));
                                english.setBackgroundResource(R.drawable.ask_clinic_bg);

                                art.setTextColor(Color.parseColor("#000000"));
                                art.setBackgroundResource(R.drawable.ask_clinic_bg);

                                switch (subject_id) {
                                    case "70100":
                                        science.setText("물리1");
                                        break;
                                    case "70200":
                                        science.setText("물리2");
                                        break;
                                    case "70300":
                                        science.setText("화학1");
                                        break;
                                    case "70400":
                                        science.setText("화학2");
                                        break;
                                    case "70500":
                                        science.setText("생명과학1");
                                        break;
                                    case "70600":
                                        science.setText("생명과학2");
                                        break;
                                    case "70700":
                                        science.setText("지구과학1");
                                        break;
                                    case "70800":
                                        science.setText("지구과학2");
                                        break;
                                    default:
                                        science.setText("과탐");
                                        break;
                                }

                            } else if (subjectInt == 13) {
                                korean.setTextColor(Color.parseColor("#000000"));
                                korean.setBackgroundResource(R.drawable.ask_clinic_bg);

                                social.setTextColor(Color.parseColor("#000000"));
                                social.setBackgroundResource(R.drawable.ask_clinic_bg);

                                math.setTextColor(Color.parseColor("#000000"));
                                math.setBackgroundResource(R.drawable.ask_clinic_bg);

                                science.setTextColor(Color.parseColor("#000000"));
                                science.setBackgroundResource(R.drawable.ask_clinic_bg);

                                english.setTextColor(Color.parseColor("#000000"));
                                english.setBackgroundResource(R.drawable.ask_clinic_bg);

                                art.setTextColor(Color.parseColor("#ffffff"));
                                art.setBackgroundResource(R.drawable.ask_clinic_bg_green);
                            }


                            JSONArray jsonArray = new JSONArray(response.getString("images"));
                            if (jsonArray.length() > 0) {
                                hasphoto = true;
                            }


                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject obj = jsonArray.getJSONObject(i);

                                if (i == 0) {
                                    imgDelete1.setVisibility(View.VISIBLE);
                                    images_id.add(obj.getString("id"));
                                    Picasso.with(Ask_Azit_Clinic.this).load(Url_define.BASE_Image + obj.getString("image_url")).into(image1);
                                } else if (i == 1) {
                                    imgDelete2.setVisibility(View.VISIBLE);
                                    images_id.add(obj.getString("id"));
                                    Picasso.with(Ask_Azit_Clinic.this).load(Url_define.BASE_Image + obj.getString("image_url")).into(image2);
                                } else if (i == 2) {
                                    imgDelete3.setVisibility(View.VISIBLE);
                                    images_id.add(obj.getString("id"));
                                    Picasso.with(Ask_Azit_Clinic.this).load(Url_define.BASE_Image + obj.getString("image_url")).into(image3);
                                }

                            }
                            Log.e("deleted_images", images_id.toString());

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

    @Override
    protected void onResume() {
        super.onResume();
        LogUtils.d(TAG, "onStart socialName , : " + socialName);
        LogUtils.d(TAG, "onStart scienceName , : " + scienceName);

        social.setText(socialName);
        science.setText(scienceName);

    }


    private void doTakePhotoAction() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        String url = "tmp_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
        mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/azit/", url));

        pd = MyProgressDialog.show(Ask_Azit_Clinic.this, "", "", true, false, null);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
        startActivityForResult(intent, PICK_FROM_CAMERA);

    }

    /**
     * 앨범에서 이미지 가져오기
     */
    private void doTakeAlbumAction() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");

        pd = MyProgressDialog.show(Ask_Azit_Clinic.this, "", "", true, false, null);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(intent, PICK_FROM_ALBUM);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }

        hasphoto = true;
        switch (requestCode) {
            case PICK_FROM_ALBUM: {
                mImageCaptureUri = data.getData();
                Log.e("data", data.toString());

                try {
                    if (photo_bit != null) {
                        photo_bit.recycle();
                    }

                    String filePath = getPath(mImageCaptureUri);
                    Log.e("filePath", filePath);

//                    InputStream stream = getContentResolver().openInputStream(mImageCaptureUri);
//                    photo_bit = BitmapFactory.decodeStream(stream);
//                    photo_bit = Bitmap.createScaledBitmap(photo_bit, 100, 100, false);
//                    photo_bit

                    Picasso.with(Ask_Azit_Clinic.this).load(mImageCaptureUri).rotate(getExifOrientation(filePath)).into(main_image, new Callback() {
                        @Override
                        public void onSuccess() {
                            pd.dismiss();
                        }

                        @Override
                        public void onError() {

                        }
                    });

                    if (main_image == image1) {
                        imgDelete1.setVisibility(View.VISIBLE);
                        image1.setTag(filePath);
                    } else if (main_image == image2) {
                        imgDelete2.setVisibility(View.VISIBLE);
                        image2.setTag(filePath);
                    }
                    if (main_image == image3) {
                        imgDelete3.setVisibility(View.VISIBLE);
                        image3.setTag(filePath);
                    }

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
                    temp = temp.replace("file://", "");
                    Log.e("temp", temp);

                    if (main_image == image1) {
                        imgDelete1.setVisibility(View.VISIBLE);
                        image1.setTag(temp);
                    } else if (main_image == image2) {
                        imgDelete2.setVisibility(View.VISIBLE);
                        image2.setTag(temp);
                    }
                    if (main_image == image3) {
                        imgDelete3.setVisibility(View.VISIBLE);
                        image3.setTag(temp);
                    }




//                    ByteArrayOutputStream bos1 = new ByteArrayOutputStream();
//                    BitmapDrawable d1 = (BitmapDrawable) ((ImageView) findViewById(R.id.image1)).getDrawable();
//                    Bitmap b1 = d1.getBitmap();
//                    b1.compress(Bitmap.CompressFormat.JPEG, 100, bos1);

//                    InputStream stream = getContentResolver().openInputStream(mImageCaptureUri);
//                    photo_bit = BitmapFactory.decodeStream(stream);
//                    photo_bit = Bitmap.createScaledBitmap(photo_bit, 100, 100, false);
//                    main_image.setImageBitmap(photo_bit);
                    Picasso.with(Ask_Azit_Clinic.this).load(mImageCaptureUri).placeholder(R.drawable.add_pic).into(main_image, new Callback() {
                        @Override
                        public void onSuccess() {
                            pd.dismiss();
                        }

                        @Override
                        public void onError() {

                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            }
        }
    }

    Transformation transformation = new Transformation() {

        @Override
        public Bitmap transform(Bitmap source) {
            int targetWidth = main_image.getWidth();

            int targetHeight = main_image.getHeight();
            Bitmap result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false);
            if (result != source) {
                // Same bitmap is returned if sizes are the same
                source.recycle();
            }
            return result;
        }

        @Override
        public String key() {
            return "transformation" + " desiredWidth";
        }
    };

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