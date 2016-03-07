package com.trams.azit.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Spinner;

import com.trams.azit.R;

/**
 * Created by sonnv on 1/12/2016.
 */
public class TestActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_student_join3);

        /*Spinner spinner = (Spinner)findViewById(R.id.spStudentYear);
        spinner.setPrompt(getResources().getString(R.string.prompt_year));*/
    }
}
