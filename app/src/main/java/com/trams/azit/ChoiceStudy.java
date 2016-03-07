package com.trams.azit;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tsengvn.typekit.TypekitContextWrapper;

/**
 * Created by Administrator on 2015-11-04.
 */
public class ChoiceStudy extends Activity {

    String title;
    TextView tx_title;
    ImageView cancel;
    LinearLayout liberal_ll, natural_ll;
    TextView economy, law, culture, history_korea, history_world, history_east, geography_korea, geography_world, life_ethics, ethics_idea;
    TextView physics1, physics2, chemistry1, chemistry2, bioscience1, bioscience2, earthscience1, earthscience2;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_choice_study);

        title = getIntent().getStringExtra("title");

        setButton();
        choice();

        if (title.equals("사탐")) {
            liberal_ll.setVisibility(View.VISIBLE);
            natural_ll.setVisibility(View.GONE);
        } else {
            liberal_ll.setVisibility(View.GONE);
            natural_ll.setVisibility(View.VISIBLE);
        }
    }

    private void choice() {
        economy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(ChoiceStudy.this, Ask_Azit_Clinic.class);
//                i.putExtra("subject_id", "401");
//                startActivity(i);
                Ask_Azit_Clinic.socialName = "경제";
                Ask_Azit_Clinic.subject_id = "60100";
                finish();
            }
        });
        law.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(ChoiceStudy.this, Ask_Azit_Clinic.class);
//                i.putExtra("subject_id", "402");
//                startActivity(i);
                Ask_Azit_Clinic.socialName = "법과 정치";
                Ask_Azit_Clinic.subject_id = "60200";
                finish();
            }
        });
        culture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(ChoiceStudy.this, Ask_Azit_Clinic.class);
//                i.putExtra("subject_id", "403");
//                startActivity(i);
                Ask_Azit_Clinic.socialName = "사회문화";
                Ask_Azit_Clinic.subject_id = "60300";
                finish();
            }
        });
        history_korea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(ChoiceStudy.this, Ask_Azit_Clinic.class);
//                i.putExtra("subject_id", "404");
//                startActivity(i);
                Ask_Azit_Clinic.socialName = "한국사";
                Ask_Azit_Clinic.subject_id = "60400";
                finish();
            }
        });
        history_world.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(ChoiceStudy.this, Ask_Azit_Clinic.class);
//                i.putExtra("subject_id", "405");
//                startActivity(i);
                Ask_Azit_Clinic.socialName = "세계사";
                Ask_Azit_Clinic.subject_id = "60500";
                finish();
            }
        });
        history_east.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(ChoiceStudy.this, Ask_Azit_Clinic.class);
//                i.putExtra("subject_id", "406");
//                startActivity(i);
                Ask_Azit_Clinic.socialName = "동아시아사";
                Ask_Azit_Clinic.subject_id = "60600";
                finish();
            }
        });
        geography_korea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(ChoiceStudy.this, Ask_Azit_Clinic.class);
//                i.putExtra("subject_id", "407");
//                startActivity(i);
                Ask_Azit_Clinic.socialName = "한국지리";
                Ask_Azit_Clinic.subject_id = "60700";
                finish();
            }
        });
        geography_world.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(ChoiceStudy.this, Ask_Azit_Clinic.class);
//                i.putExtra("subject_id", "408");
//                startActivity(i);
                Ask_Azit_Clinic.socialName = "세계지리";
                Ask_Azit_Clinic.subject_id = "60800";
                finish();
            }
        });
        life_ethics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(ChoiceStudy.this, Ask_Azit_Clinic.class);
//                i.putExtra("subject_id", "409");
//                startActivity(i);
                Ask_Azit_Clinic.socialName = "생활과 윤리";
                Ask_Azit_Clinic.subject_id = "60900";
                finish();
            }
        });
        ethics_idea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(ChoiceStudy.this, Ask_Azit_Clinic.class);
//                i.putExtra("subject_id", "410");
//                startActivity(i);
                Ask_Azit_Clinic.socialName = "윤리와 사상";
                Ask_Azit_Clinic.subject_id = "61000";
                finish();

            }
        });
        physics1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(ChoiceStudy.this, Ask_Azit_Clinic.class);
//                i.putExtra("subject_id", "501");
//                startActivity(i);
                Ask_Azit_Clinic.scienceName = "물리1";
                Ask_Azit_Clinic.subject_id = "70100";
                finish();
            }
        });
        physics2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(ChoiceStudy.this, Ask_Azit_Clinic.class);
//                i.putExtra("subject_id", "502");
//                startActivity(i);
                Ask_Azit_Clinic.scienceName = "물리2";
                Ask_Azit_Clinic.subject_id = "70200";
                finish();
            }
        });
        chemistry1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(ChoiceStudy.this, Ask_Azit_Clinic.class);
//                i.putExtra("subject_id", "503");
//                startActivity(i);
                Ask_Azit_Clinic.scienceName = "화학1";
                Ask_Azit_Clinic.subject_id = "70300";
                finish();
            }
        });
        chemistry2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(ChoiceStudy.this, Ask_Azit_Clinic.class);
//                i.putExtra("subject_id", "504");
//                startActivity(i);
                Ask_Azit_Clinic.scienceName = "화학2";
                Ask_Azit_Clinic.subject_id = "70400";
                finish();
            }
        });
        bioscience1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(ChoiceStudy.this, Ask_Azit_Clinic.class);
//                i.putExtra("subject_id", "505");
//                startActivity(i);
                Ask_Azit_Clinic.scienceName = "생명과학1";
                Ask_Azit_Clinic.subject_id = "70500";
                finish();
            }
        });
        bioscience2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(ChoiceStudy.this, Ask_Azit_Clinic.class);
//                i.putExtra("subject_id", "506");
//                startActivity(i);
                Ask_Azit_Clinic.scienceName = "생명과학2";
                Ask_Azit_Clinic.subject_id = "70600";
                finish();
            }
        });
        earthscience1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(ChoiceStudy.this, Ask_Azit_Clinic.class);
//                i.putExtra("subject_id", "507");
//                startActivity(i);
                Ask_Azit_Clinic.scienceName = "지구과학1";
                Ask_Azit_Clinic.subject_id = "70700";
                finish();
            }
        });
        earthscience2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(ChoiceStudy.this, Ask_Azit_Clinic.class);
//                i.putExtra("subject_id", "508");
//                startActivity(i);
                Ask_Azit_Clinic.scienceName = "지구과학2";
                Ask_Azit_Clinic.subject_id = "70800";
                finish();
            }
        });
    }

    private void setButton() {
        tx_title = (TextView) findViewById(R.id.title);
        tx_title.setText(title + "선택");
        cancel = (ImageView) findViewById(R.id.back_btn);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        liberal_ll = (LinearLayout) findViewById(R.id.area_liberal);
        natural_ll = (LinearLayout) findViewById(R.id.area_natural);

        economy = (TextView) findViewById(R.id.economy);
        law = (TextView) findViewById(R.id.law);
        culture = (TextView) findViewById(R.id.culture);
        history_korea = (TextView) findViewById(R.id.history_korea);
        history_world = (TextView) findViewById(R.id.history_world);
        history_east = (TextView) findViewById(R.id.history_east);
        geography_korea = (TextView) findViewById(R.id.geography_korea);
        geography_world = (TextView) findViewById(R.id.geography_world);
        life_ethics = (TextView) findViewById(R.id.life_ethics);
        ethics_idea = (TextView) findViewById(R.id.ethics_idea);
        physics1 = (TextView) findViewById(R.id.physics1);
        physics2 = (TextView) findViewById(R.id.physics2);
        chemistry1 = (TextView) findViewById(R.id.chemistry1);
        chemistry2 = (TextView) findViewById(R.id.chemistry2);
        bioscience1 = (TextView) findViewById(R.id.bioscience1);
        bioscience2 = (TextView) findViewById(R.id.bioscience2);
        earthscience1 = (TextView) findViewById(R.id.earthscience1);
        earthscience2 = (TextView) findViewById(R.id.earthscience2);
    }
}
