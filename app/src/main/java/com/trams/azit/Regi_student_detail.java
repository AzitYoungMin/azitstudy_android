package com.trams.azit;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.trams.azit.LoginActivity_;
import com.trams.azit.util.ConnActivity;
import com.trams.azit.util.Url_define;
import com.tsengvn.typekit.TypekitContextWrapper;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2015-09-02.
 */
@EActivity(R.layout.activity_regi_student_detail)
public class Regi_student_detail extends ConnActivity {

    @ViewById
    ImageView back_btn;
    @ViewById
    TextView liberal, natural, koreanA, koreanB, mathA, mathB;
    @ViewById
    LinearLayout area_liberal, area_natural;
    @ViewById
    TextView economy, law, culture, history_korea, history_world, history_east, geography_korea, geography_world, life_ethics, ethics_idea;
    @ViewById
    CheckBox inmungwahakDaehak, bupgwaDaehak, sahwaeDaehak, sanggyungDaehak, sabumDaehak, bogunDaehak, misulDaehak, bubDaehak, yeaDaehak;
    @ViewById
    TextView physics1, physics2, chemistry1, chemistry2, bioscience1, bioscience2, earthscience1, earthscience2;

    @ViewById
    TextView inmungwahakDaehak_text, sahwaeDaehak_text, sanggyungDaehak_text;

    @ViewById
    RelativeLayout layout_sanggyungDaehak, layout_bupgwa;
    Boolean economy_check = true, law_check = true, culture_check = true, history_korea_check = true, history_world_check = true, history_east_check = true, geography_korea_check = true, geography_world_check = true, ethics_life_check = true, ethics_idea_check = true;
    Boolean physics1_check = true, physics2_check = true, chemistry1_check = true, chemistry2_check = true, bio_science1_check = true, bio_science2_check = true, earth_science1_check = true, earth_science2_check = true;

    @ViewById
    CheckBox langcheck;
    String koreanFlag, mathFlag;
    String optional_subjects = "";
    JSONObject jsonObject;
    private String studentData;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @AfterViews
    protected void init() {
        Intent intent = getIntent();
        Log.d("getget", intent.getStringExtra("studentData"));
        studentData = intent.getStringExtra("studentData");
        try {
            jsonObject = new JSONObject(studentData);
            jsonObject.put("department", "B");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Click(R.id.back_btn)
    protected void setBack_btn() {
        finish();
    }

    /**
     * 회원가입 문과 선택시 색상변경
     */
    @Click(R.id.liberal)
    protected void setLiberal() {

        try {
            jsonObject = new JSONObject(studentData);
            jsonObject.put("department", "A");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        layout_bupgwa.setVisibility(View.VISIBLE);
        sanggyungDaehak_text.setText("상경대학");
        sahwaeDaehak_text.setText("사회과학대학");

        natural.setBackgroundColor(Color.parseColor("#E9E9E9"));
        natural.setTextColor(Color.parseColor("#A7ABAE"));

        liberal.setTextColor(Color.parseColor("#1FB4FF"));
        liberal.setBackgroundColor(Color.parseColor("#FFFFFF"));
        area_liberal.setVisibility(View.VISIBLE);
        area_natural.setVisibility(View.GONE);
    }

    @Click(R.id.langcheck)
    protected void setlangCheck() {
        try {
            jsonObject.put("foreign_language", langcheck.isChecked());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 회원가입 이과 선택시 색상 변경
     */
    @Click(R.id.natural)
    protected void setNatural() {
        try {
            jsonObject = new JSONObject(studentData);
            jsonObject.put("department", "B");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        layout_bupgwa.setVisibility(View.GONE);
        sanggyungDaehak_text.setText("생활과학대학");
        sahwaeDaehak_text.setText("공과대학");

        liberal.setTextColor(Color.parseColor("#A7ABAE"));
        liberal.setBackgroundColor(Color.parseColor("#E9E9E9"));

        natural.setTextColor(Color.parseColor("#1FB4FF"));
        natural.setBackgroundColor(Color.parseColor("#FFFFFF"));
        area_liberal.setVisibility(View.GONE);
        area_natural.setVisibility(View.VISIBLE);
    }

    @Click(R.id.koreanA)
    protected void setKoreanA() {
        koreanA.setBackgroundResource(R.drawable.a_push);
        koreanB.setBackgroundResource(R.drawable.b);
        koreanFlag = "A";
        try {
            jsonObject.put("korean_type", koreanFlag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Click(R.id.koreanB)
    protected void setKoreanB() {
        koreanA.setBackgroundResource(R.drawable.a);
        koreanB.setBackgroundResource(R.drawable.b_push);
        koreanFlag = "B";
        try {
            jsonObject.put("korean_type", koreanFlag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Click(R.id.mathA)
    protected void setMathA() {
        mathA.setBackgroundResource(R.drawable.a_push);
        mathB.setBackgroundResource(R.drawable.b);
        mathFlag = "A";
        try {
            jsonObject.put("math_type", mathFlag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Click(R.id.mathB)
    protected void setMathB() {
        mathA.setBackgroundResource(R.drawable.a);
        mathB.setBackgroundResource(R.drawable.b_push);
        mathFlag = "B";
        try {
            jsonObject.put("math_type", mathFlag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Click(R.id.physics1)
    protected void setPhysics1() {
        if (physics1_check) {
            physics1_check = false;
            physics1.setBackgroundColor(Color.parseColor("#bbedff"));
            optional_subjects += ",501";
        } else {
            physics1_check = true;
            physics1.setBackgroundColor(Color.parseColor("#ffffff"));
        }
    }

    @Click(R.id.physics2)
    protected void setPhysics2() {
        if (physics2_check) {
            physics2_check = false;
            physics2.setBackgroundColor(Color.parseColor("#bbedff"));
            optional_subjects += ",502";
        } else {
            physics2_check = true;
            physics2.setBackgroundColor(Color.parseColor("#ffffff"));
        }
    }

    @Click(R.id.chemistry1)
    protected void setChemistry1() {
        if (chemistry1_check) {
            chemistry1_check = false;
            chemistry1.setBackgroundColor(Color.parseColor("#bbedff"));
            optional_subjects += ",503";

        } else {
            chemistry1_check = true;
            chemistry1.setBackgroundColor(Color.parseColor("#ffffff"));
        }
    }

    @Click(R.id.chemistry2)
    protected void setChemistry2() {
        if (chemistry2_check) {
            chemistry2_check = false;
            chemistry2.setBackgroundColor(Color.parseColor("#bbedff"));
            optional_subjects = ",504";

        } else {
            chemistry2_check = true;
            chemistry2.setBackgroundColor(Color.parseColor("#ffffff"));
        }
    }

    @Click(R.id.bioscience1)
    protected void setBioscience1() {
        if (bio_science1_check) {
            bio_science1_check = false;
            bioscience1.setBackgroundColor(Color.parseColor("#bbedff"));
            optional_subjects += ",505";

        } else {
            bio_science1_check = true;
            bioscience1.setBackgroundColor(Color.parseColor("#ffffff"));
        }
    }

    @Click(R.id.bioscience2)
    protected void setBioscience2() {
        if (bio_science2_check) {
            bio_science2_check = false;
            bioscience2.setBackgroundColor(Color.parseColor("#bbedff"));
            optional_subjects += ",506";
        } else {
            bio_science2_check = true;
            bioscience2.setBackgroundColor(Color.parseColor("#ffffff"));
        }
    }

    @Click(R.id.earthscience1)
    protected void setEarth_science1() {
        if (earth_science1_check) {
            earth_science1_check = false;
            earthscience1.setBackgroundColor(Color.parseColor("#bbedff"));
            optional_subjects += ",507";

        } else {
            earth_science1_check = true;
            earthscience1.setBackgroundColor(Color.parseColor("#ffffff"));
        }
    }

    @Click(R.id.earthscience2)
    protected void setEarth_science2() {
        if (earth_science2_check) {
            earth_science2_check = false;
            earthscience2.setBackgroundColor(Color.parseColor("#bbedff"));
            optional_subjects += ",508";
        } else {
            earth_science2_check = true;
            earthscience2.setBackgroundColor(Color.parseColor("#ffffff"));
        }
    }

    @Click(R.id.economy)
    protected void setEconomy() {
        if (economy_check) {
            economy_check = false;
            economy.setBackgroundColor(Color.parseColor("#bbedff"));
            optional_subjects += ",400";

        } else {
            economy_check = true;
            economy.setBackgroundColor(Color.parseColor("#ffffff"));
        }
    }

    @Click(R.id.law)
    protected void setLaw() {
        if (law_check) {
            law_check = false;
            law.setBackgroundColor(Color.parseColor("#bbedff"));
            optional_subjects += ",402";

        } else {
            law_check = true;
            law.setBackgroundColor(Color.parseColor("#ffffff"));
        }
    }

    @Click(R.id.culture)
    protected void setCulture() {
        if (culture_check) {
            culture_check = false;
            culture.setBackgroundColor(Color.parseColor("#bbedff"));
            optional_subjects += ",403";

        } else {
            culture_check = true;
            culture.setBackgroundColor(Color.parseColor("#ffffff"));
        }
    }

    @Click(R.id.history_korea)
    protected void setHistory_korea() {
        if (history_korea_check) {
            history_korea_check = false;
            history_korea.setBackgroundColor(Color.parseColor("#bbedff"));
            optional_subjects += ",404";

        } else {
            history_korea_check = true;
            history_korea.setBackgroundColor(Color.parseColor("#ffffff"));
        }
    }

    @Click(R.id.history_world)
    protected void setHistory_world() {
        if (history_world_check) {
            history_world_check = false;
            history_world.setBackgroundColor(Color.parseColor("#bbedff"));
            optional_subjects += ",405";

        } else {
            history_world_check = true;
            history_world.setBackgroundColor(Color.parseColor("#ffffff"));
        }
    }

    @Click(R.id.history_east)
    protected void setHistory_east() {
        if (history_east_check) {
            history_east_check = false;
            history_east.setBackgroundColor(Color.parseColor("#bbedff"));
            optional_subjects += ",406";
        } else {
            history_east_check = true;
            history_east.setBackgroundColor(Color.parseColor("#ffffff"));
        }
    }

    @Click(R.id.geography_korea)
    protected void setGeography_korea() {
        if (geography_korea_check) {
            geography_korea_check = false;
            geography_korea.setBackgroundColor(Color.parseColor("#bbedff"));
            optional_subjects += ",407";

        } else {
            geography_korea_check = true;
            geography_korea.setBackgroundColor(Color.parseColor("#ffffff"));
        }
    }

    @Click(R.id.geography_world)
    protected void setGeography_world() {
        if (geography_world_check) {
            geography_world_check = false;
            geography_world.setBackgroundColor(Color.parseColor("#bbedff"));
            optional_subjects += ",408";
        } else {
            geography_world_check = true;
            geography_world.setBackgroundColor(Color.parseColor("#ffffff"));
        }
    }

    @Click(R.id.life_ethics)
    protected void setLife_ethics() {
        if (ethics_life_check) {
            ethics_life_check = false;
            life_ethics.setBackgroundColor(Color.parseColor("#bbedff"));
            optional_subjects += ",409";

        } else {
            ethics_life_check = true;
            life_ethics.setBackgroundColor(Color.parseColor("#ffffff"));
        }
    }

    @Click(R.id.ethics_idea)
    protected void setEthics_idea() {
        if (ethics_idea_check) {
            ethics_idea_check = false;
            ethics_idea.setBackgroundColor(Color.parseColor("#bbedff"));
            optional_subjects += ",410";
        } else {
            ethics_idea_check = true;
            ethics_idea.setBackgroundColor(Color.parseColor("#ffffff"));
        }

    }

    @Click(R.id.btnComplete)
    protected void setComplete() {
        Log.d("optional_subjects(0)", optional_subjects);
        // TODO: 2015-11-02

        if (optional_subjects.substring(0, 1).equals(",")) {
            optional_subjects = optional_subjects.replaceFirst(",", "");
        }
        Log.d("optional_subjects(0)", optional_subjects);

        String target_departments = "";
        if (inmungwahakDaehak.isChecked()) {
            target_departments += "";
        }
        if (bupgwaDaehak.isChecked()) {

        }
        if (sahwaeDaehak.isChecked()) {

        }
        if (sanggyungDaehak.isChecked()) {

        }
        if (sabumDaehak.isChecked()) {

        }
        if (bogunDaehak.isChecked()) {

        }
        if (misulDaehak.isChecked()) {

        }
        if (bubDaehak.isChecked()) {

        }
        if (yeaDaehak.isChecked()) {

        }
        try {
            jsonObject.put("optional_subjects", optional_subjects);

            Log.d("jsonObject", jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        requestJson(Url_define.BASE + "/api/student/signup" + Url_define.KEY, jsonObject, new ConnHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject res) {
                Log.d("statusCode", statusCode + "");
                Log.d("onSuccess res", res.toString());
                try {
                    String str = (String) res.get("message");
                    if (str.equals("duplicated email")) {
                        Toast.makeText(getApplicationContext(), "중복된 이메일이 존재합니다.", Toast.LENGTH_SHORT).show();
                    } else if (!res.get("result").toString().equals("fail")) {
                        Intent i = new Intent(Regi_student_detail.this, LoginActivity_.class);
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
            }

        });

    }

}
