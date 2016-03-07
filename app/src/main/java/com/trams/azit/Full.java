package com.trams.azit;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.trams.azit.util.Url_define;

import uk.co.senab.photoview.PhotoViewAttacher;

public class Full extends AppCompatActivity {
    PhotoViewAttacher mAttacher;
    Button btn;
    ImageView im;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full);
        Intent intent = getIntent();
        String get_src = intent.getExtras().getString("image_url");

        im = (ImageView)findViewById(R.id.imageView44444);


        Picasso.with(Full.this).load(get_src).into(im);
        mAttacher = new PhotoViewAttacher(im);
        mAttacher.setScaleType(ImageView.ScaleType.FIT_XY);

        btn = (Button)findViewById(R.id.but10);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }


        });
    }
}
