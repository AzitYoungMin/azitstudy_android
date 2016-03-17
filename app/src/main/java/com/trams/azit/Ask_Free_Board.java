package com.trams.azit;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.trams.azit.util.ConnActivity;
import com.trams.azit.util.MyProgressDialog;
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
import java.text.SimpleDateFormat;
import java.util.Date;

public class Ask_Free_Board extends ConnActivity {

    ImageView cancel, register, image1111;
    EditText title, body;

    private static final int PICK_FROM_ALBUM = 1;
    SharedPreferences myPrefs;
    String secret, user_id;
    MyProgressDialog pd;
    int posting_id;
    private Uri mImageCaptureUri;

    Bitmap photo_bit;
    boolean hasphoto = false, hasimage1 = false;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_freeboard);
        hasphoto = false;
        posting_id = getIntent().getIntExtra("posting_id", 0);

        myPrefs = getSharedPreferences("Azit", MODE_PRIVATE);
        secret = myPrefs.getString("secret", "");
        user_id = myPrefs.getString("user_id", "");

        cancel = (ImageView) findViewById(R.id.cancel);
        register = (ImageView) findViewById(R.id.register);
        title = (EditText) findViewById(R.id.title);
        body = (EditText) findViewById(R.id.body);
        image1111 = (ImageView) findViewById(R.id.image1111);

        if (posting_id != 0) {
            getEditData();
        }

        image1111.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doTakeAlbumAction();
            }
        });

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
                        .setTitle("해당 질문을 취소하시겠어요?")
                        .setPositiveButton("확인", confirmListener)
                        .setNegativeButton("취소", cancelListener)
                        .show();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String images = "no";
                    RequestParams params = new RequestParams();
                    JSONObject jsonObject = new JSONObject();
                    //jsonObject.put("secret", secret);
                    jsonObject.put("user_id", user_id);
                    params.put("user_id", user_id);
                    jsonObject.put("title", title.getText().toString());
                    params.put("title", title.getText().toString());
                    jsonObject.put("article", body.getText().toString());
                    params.put("article", body.getText().toString());

                    Date from = new Date();
                    SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String to = transFormat.format(from);
                    jsonObject.put("day", to);
                    params.put("day", to);
                    Logger.e(user_id);

                    if(hasphoto) {
                        // 이미지가 안에 사진을 담고 있는지 없는지 확인해야함..
                        BitmapFactory.Options op = new BitmapFactory.Options();
                        op.inSampleSize = 2;

                        ByteArrayOutputStream bos1 = new ByteArrayOutputStream();

                        Log.e("image1", image1111.getTag().toString());
                        Bitmap b1 = BitmapFactory.decodeFile(image1111.getTag().toString(), op);

                        Matrix matrix = new Matrix();
                        matrix.postRotate(getExifOrientation(image1111.getTag().toString()));

                        b1 = Bitmap.createBitmap(b1, 0, 0, b1.getWidth(), b1.getHeight(), matrix, true);
                        b1.compress(Bitmap.CompressFormat.JPEG, 100, bos1);

                        params.put("profile_image1", new ByteArrayInputStream(bos1.toByteArray()), "image1", "image/jpeg");
                        images = "ok";
                    }

                    params.put("image", images);


                    requestMultipart2("http://192.168.1.21:2000/FreeBorad/Write", jsonObject, params, new ConnActivity.ConnHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                            Log.d("statusCode : ", statusCode + "");
                            Log.d("response : ", response.toString());
                            super.onSuccess(statusCode, headers, response);

                                    Toast.makeText(Ask_Free_Board.this, "등록 완료!", Toast.LENGTH_LONG).show();

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

    private void doTakeAlbumAction() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        pd = MyProgressDialog.show(Ask_Free_Board.this, "", "", true, false, null);



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
               // Bitmap photo = (Bitmap) data.getExtras().get("data");
               // image1111.setImageBitmap(photo);


               // Uri tempUri = getImageUri(getApplicationContext(), photo);

                // CALL THIS METHOD TO GET THE ACTUAL PATH
              //  File finalFile = new File(getRealPathFromURI(tempUri));


                mImageCaptureUri = data.getData();
                Log.e("data", data.toString());
                try {
                    if (photo_bit != null) {
                        photo_bit.recycle();

                    }
                    String filePath = getPath(mImageCaptureUri);

                    Picasso.with(Ask_Free_Board.this).load(mImageCaptureUri).rotate(0).into(image1111, new Callback() {
                        @Override
                        public void onSuccess() {
                            hasphoto = true;

                            pd.dismiss();
                        }

                        @Override
                        public void onError() {

                        }
                    });
                   image1111.setTag(filePath);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
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
        if (cursor != null) cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        if (cursor != null ) cursor.moveToFirst();

            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            cursor.close();


        return path;
    }
}
