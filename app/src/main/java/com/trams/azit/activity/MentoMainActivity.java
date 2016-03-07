package com.trams.azit.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.trams.azit.activity.MentorSettingActivity_;
import com.trams.azit.activity.TeacherSettingAskActivity_;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;
import com.trams.azit.Invitation;
import com.trams.azit.Mento_Act;
import com.trams.azit.Mento_Azit_Clinic;
import com.trams.azit.fragment.Mento_Home;
import com.trams.azit.Mento_Profile;
import com.trams.azit.Mentoring_answer;
import com.trams.azit.R;
import com.trams.azit.menu.MenuAdapter;
import com.trams.azit.menu.StudentMenuLayout;
import com.trams.azit.util.BackPressCloseHandler;
import com.trams.azit.util.ConnActivity;
import com.trams.azit.util.Url_define;
import com.tsengvn.typekit.TypekitContextWrapper;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2015-10-27.
 */
public class MentoMainActivity extends ConnActivity {

    StudentMenuLayout mLayout;
    Fragment fragment = null;
    private ListView lvMenu;
    private String[] lvMenuItems;
    public ImageView btMenu, btBack, btPerson, profile_pic;
    public TextView tvTitle, name, introduce, point;
    SharedPreferences myPrefs;
    String secret, user_id;
    FragmentTransaction ft;
    private BackPressCloseHandler backPressCloseHandler;
    public static String[] lvMenuTag = {"home", "mentor_q_1", "mentor_q_2", "mentor_setting", "mentor_setting_ask", "ment_inp"};
    public static int[] lvMenuImages = {R.drawable.menu_1, R.drawable.menu_2, R.drawable.menu_3, R.drawable.app_setting, R.drawable.menu_5, R.drawable.papa};

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    protected void onResume() {
        LoadFromServer();
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLayout = (StudentMenuLayout) this.getLayoutInflater().inflate(R.layout.activity_mento_main, null);
        setContentView(mLayout);

        backPressCloseHandler = new BackPressCloseHandler(this);

        myPrefs = getSharedPreferences("Azit", MODE_PRIVATE);
        secret = myPrefs.getString("secret", "");
        user_id = myPrefs.getString("user_id", "");

        profile_pic = (ImageView) findViewById(R.id.profile_pic);
        name = (TextView) findViewById(R.id.name);
        introduce = (TextView) findViewById(R.id.introduce);
        point = (TextView) findViewById(R.id.point);

        lvMenuItems = getResources().getStringArray(R.array.mento_menu_items);
        lvMenu = (ListView) findViewById(R.id.menu_listview);
        lvMenu.setAdapter(new MenuAdapter(this, lvMenuTag, lvMenuItems, lvMenuImages));
        lvMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                onMenuItemClick(position);
            }

        });

        btMenu = (ImageView) findViewById(R.id.button_menu);
        btMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show/hide the menu
                toggleMenu(v);
            }
        });
        btBack = (ImageView) findViewById(R.id.button_back);
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onMenuItemClick(0);
            }
        });
        btBack.setVisibility(View.GONE);

        btPerson = (ImageView) findViewById(R.id.person);
//        btPerson.setVisibility(View.GONE);
        btPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MentoMainActivity.this, Mento_Act.class);
                if (tvTitle.getText().toString().equals("멘토링답변")) {
                    i.putExtra("position", 0);
                } else if (tvTitle.getText().toString().equals("아지트 클리닉")) {
                    i.putExtra("position", 1);
                }
                startActivity(i);
            }
        });


        tvTitle = (TextView) findViewById(R.id.activity_main_content_title);

        FragmentManager fm = MentoMainActivity.this.getSupportFragmentManager();
        ft = fm.beginTransaction();

        Intent temp = getIntent();
        int position = temp.getIntExtra("position", 0);

        if (position == 0) {
            fragment = new Mento_Home();
            btMenu.setVisibility(View.VISIBLE);
            btBack.setVisibility(View.GONE);
            btPerson.setVisibility(View.GONE);
            tvTitle.setText("AZit STUDY");
        }else if(position == 2) {
            fragment = new Mento_Azit_Clinic();
            btMenu.setVisibility(View.VISIBLE);
            btPerson.setVisibility(View.VISIBLE);
            btBack.setVisibility(View.GONE);
            tvTitle.setText("풀어주세요");
        }
        ft.add(R.id.activity_main_content_fragment, fragment);
        ft.detach(fragment);
        ft.attach(fragment);
        ft.commit();

//        LoadFromServer();

        findViewById(R.id.mentor_menu_point_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MentoMainActivity.this, MyPointActivity.class);
                i.putExtra("point", point.getText().toString());
                startActivity(i);
                mLayout.toggleMenu();
            }
        });

        findViewById(R.id.profile_pic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MentoMainActivity.this, Mento_Profile.class);
                startActivity(i);
                mLayout.toggleMenu();
            }
        });

        findViewById(R.id.mentor_menu_pic_profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MentoMainActivity.this, Mento_Profile.class);
                startActivity(i);
                mLayout.toggleMenu();
            }
        });

    }

    private void LoadFromServer() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("secret", secret);
            jsonObject.put("user_id", user_id);

            Logger.e(secret);
            Logger.e(user_id);
            requestJson(Url_define.BASE + "/api/mentor/info" + Url_define.KEY, jsonObject, new ConnHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.d("statusCode : ", statusCode + "");
                    Log.d("response : ", response.toString());
                    super.onSuccess(statusCode, headers, response);

                    try {
                        if (response.getString("profile_image").equals("")) {
                            profile_pic.setBackgroundResource(R.drawable.profile_basic);
                        } else {
                            Picasso.with(MentoMainActivity.this).load(Url_define.BASE_Image + response.getString("profile_image")).fit().placeholder(R.drawable.black_crop).into(profile_pic);
                            Log.e("uri", Url_define.BASE + response.getString("profile_image"));
                        }
                        name.setText(response.getString("name"));
                        introduce.setText(response.getString("introduce"));
                        point.setText(response.getString("point") + "pt");

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void toggleMenu(View v) {
        mLayout.toggleMenu();

    }

    public void onMenuItemClick(int position) {
        String selectedItem = lvMenuItems[position];
        String currentItem = tvTitle.getText().toString();
        if (selectedItem.compareTo(currentItem) == 0) {
            mLayout.toggleMenu();
            return;
        }

        FragmentManager fm = MentoMainActivity.this.getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment fragment = null;

        if (selectedItem.compareTo("홈") == 0) {
            fragment = new Mento_Home();
            btMenu.setVisibility(View.VISIBLE);
            btBack.setVisibility(View.GONE);
            btPerson.setVisibility(View.GONE);
            tvTitle.setText("AZit STUDY");
        } else if (selectedItem.compareTo("멘토링 답변") == 0) {
            fragment = new Mentoring_answer();
            btMenu.setVisibility(View.VISIBLE);
            btBack.setVisibility(View.GONE);
            btPerson.setVisibility(View.VISIBLE);
            tvTitle.setText("멘토링답변");


        } else if (selectedItem.compareTo("풀어주세요") == 0) {
            fragment = new Mento_Azit_Clinic();
            btMenu.setVisibility(View.VISIBLE);
            btPerson.setVisibility(View.VISIBLE);
            btBack.setVisibility(View.GONE);
            tvTitle.setText("풀어주세요");


        } else if (selectedItem.compareTo("설정") == 0) {

            Intent i = new Intent(this, MentorSettingActivity_.class);
            startActivity(i);

        } else if (selectedItem.compareTo("문의하기") == 0) {
            Intent i = new Intent(this, TeacherSettingAskActivity_.class);
            startActivity(i);

        }else if (selectedItem.compareTo("초대하기") == 0) {
            fragment = new Invitation();
            btMenu.setVisibility(View.VISIBLE);
            btPerson.setVisibility(View.VISIBLE);
            btBack.setVisibility(View.GONE);
            tvTitle.setText("초대하기");


        }

        if (fragment != null) {
            ft.replace(R.id.activity_main_content_fragment, fragment);
            ft.detach(fragment);
            ft.attach(fragment);
            ft.commit();

        }
        if (mLayout.isMenuShown()) {
            mLayout.toggleMenu();
        }
    }

    @Override
    public void onBackPressed() {

        if (mLayout.isMenuShown()) {
            mLayout.toggleMenu();
        } else {
            backPressCloseHandler.onBackPressed();

        }
    }
}
