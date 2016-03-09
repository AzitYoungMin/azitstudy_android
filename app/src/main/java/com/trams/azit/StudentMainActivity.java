package com.trams.azit;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Color;
import com.trams.azit.Edit_Student_Profile_;
import com.google.android.gms.common.api.GoogleApiClient;
import com.kakao.kakaolink.KakaoLink;
import com.kakao.kakaolink.KakaoTalkLinkMessageBuilder;
import com.kakao.util.KakaoParameterException;
import com.orhanobut.logger.Logger;
import com.squareup.picasso.Picasso;
import com.trams.azit.adapter.ExamAdapter;
import com.trams.azit.adapter.UniversityAdapter;
import com.trams.azit.fragment.TutFragment;
import com.trams.azit.menu.MenuAdapter;
import com.trams.azit.menu.StudentMenuLayout;
import com.trams.azit.model.Exam;
import com.trams.azit.model.Item;
import com.trams.azit.model.SectionItemExam;
import com.trams.azit.model.Subject;
import com.trams.azit.model.University;
import com.trams.azit.util.BackPressCloseHandler;
import com.trams.azit.util.ConnActivity;
import com.trams.azit.util.ConnFragment;
import com.trams.azit.util.Url_define;
import com.tsengvn.typekit.TypekitContextWrapper;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Locale;


public class StudentMainActivity extends ConnActivity {

    StudentMenuLayout mLayout;
    Fragment fragment = null;
    ImageView profile_change;
    private ListView lvMenu;
    private BackPressCloseHandler backPressCloseHandler;
    private String[] lvMenuItems;
    public ImageView btMenu, btBack, btPen, btPerson, btAlarm, btMessage, btPoint, profile_pic;
    public TextView tvTitle, name, target_university;
    public static String[] lvMenuTag = {"home", "my_study", "my_grde", "Free_Borad", "meet_mento", "azit_clinic", "report", "inquiry","Invitation", "setting"};
    public static int[] lvMenuImages = {R.drawable.home, R.drawable.my_study_white, R.drawable.my_grade_whiite, R.drawable.my_grade_whiite, R.drawable.meet_mento_white, R.drawable.azit_clinic_white, R.drawable.mentoring_white, R.drawable.enter_azit_white, R.drawable.papa, R.drawable.app_setting};
    SharedPreferences myPrefs;
    static String secret, user_id;
    private int idKorean, idMath, idEnglish, idSocial, idScience, idMonth;
    private ArrayList<University> arrayUniversity;
    private ArrayList<Exam> arrayExam;
    private ArrayList<String> arrayIdExam;
    private ArrayList<Item> arrayExamSubject;
    private ExamAdapter examAdapter;
    UniversityAdapter universityAdapter;
    private GoogleApiClient client;
    private ScrollView includeInputGrade1;
    private LinearLayout includeInputGrade2;
    private RelativeLayout includeInputGrade3;
    private TextView tvTitleStep1, tvTitleStep2, tvTitleStep3, tvTitleMonth;
    private Hashtable<String, String> hashTable;
    private Dialog dialog;
    private AutoCompleteTextView edUniversity;
    static Boolean canMove = true;
    private KakaoLink kakaoLink = null;;
    private KakaoTalkLinkMessageBuilder kakaoTalkLinkMessageBuilder = null;
    static Context context;
    public static Handler mTimer = new Handler() {
        public void handleMessage(Message msg) {
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss", Locale.KOREA);
            Date currentTime = new Date();
            String dTime = formatter.format(currentTime);
            Log.e("dTime from main", dTime);

            if (dTime.equals("03:00:00")) {
                Student_Home.mArrayList.get(Student_Home.selectedPosition).mStatus = Student_Home.STOP;
                Student_Home.mArrayList.get(Student_Home.selectedPosition).endTimeforserver = "03:00:00";
                this.removeMessages(0);
                Student_Home.setTick();
                Log.e("startTimeforserver", Student_Home.mArrayList.get(Student_Home.selectedPosition).startTimeforserver);
                Log.e("endTimeforserver", Student_Home.mArrayList.get(Student_Home.selectedPosition).endTimeforserver);
                Log.e("timer", Student_Home.mArrayList.get(Student_Home.selectedPosition).timer);

                ArrayList<StudentHomeListData> mListData = Student_Home.mArrayList;

                for (int i = 0; i < mListData.size(); i++) {
                    mListData.get(i).isClickable = true;
                }
                StudentMainActivity.canMove = true;

                //팝업 띄워 보내기
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (Student_Home.mArrayList.get(Student_Home.selectedPosition).activityType == 1) {
                            Intent FinishActivity = new Intent(context, com.trams.azit.dialog.FinishActivity.class);
                            FinishActivity.putExtra("activity_id", Student_Home.mArrayList.get(Student_Home.selectedPosition).id);
                            FinishActivity.putExtra("start_time", Student_Home.mArrayList.get(Student_Home.selectedPosition).startTimeforserver);
                            Log.e("start", Student_Home.mArrayList.get(Student_Home.selectedPosition).startTime);
                            Log.e("startTimeforserver", Student_Home.mArrayList.get(Student_Home.selectedPosition).startTimeforserver);
                            FinishActivity.putExtra("end_time", Student_Home.mArrayList.get(Student_Home.selectedPosition).endTimeforserver);
                            FinishActivity.putExtra("duration", Student_Home.mArrayList.get(Student_Home.selectedPosition).timer.replace("'", ":").replace("\"", ""));
                            FinishActivity.putExtra("activity_type", Student_Home.mArrayList.get(Student_Home.selectedPosition).activityType);
                            context.startActivity(FinishActivity);


                        } else {
                            try {
                                JSONObject jsonObject = new JSONObject();
                                jsonObject.put("secret", secret);
                                jsonObject.put("user_id", user_id);
                                jsonObject.put("start_time", Student_Home.mArrayList.get(Student_Home.selectedPosition).startTimeforserver);
                                jsonObject.put("end_time", Student_Home.mArrayList.get(Student_Home.selectedPosition).endTimeforserver);
                                jsonObject.put("activity_id", Student_Home.mArrayList.get(Student_Home.selectedPosition).id);
                                jsonObject.put("duration", Student_Home.mArrayList.get(Student_Home.selectedPosition).timer.replace("'", ":").replace("\"", ""));
                                jsonObject.put("activity_type", Student_Home.mArrayList.get(Student_Home.selectedPosition).activityType);

                                Log.e("jsonObject", jsonObject.toString());

                                requestTimeJson(context, Url_define.Student_Send_My_Time + Url_define.KEY, jsonObject, new ConnHttpResponseHandlers() {
                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                        Log.d("statusCode : ", statusCode + "");
                                        Log.d("response : ", response.toString());
                                        super.onSuccess(statusCode, headers, response);
                                        try {
                                            if (response.getString("result").equals("success")) {

                                                Toast.makeText(context, "시간이 저장 되었습니다.", Toast.LENGTH_SHORT).show();
                                                Student_Home.setTick();
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

                        }//닫으면서 페이지 입력 화면으로 넘어가기
                    }
                });
                alert.setCancelable(false);
                alert.setMessage("하루가 종료 되었습니다.\n다시 시작해 주세요");
                alert.show();
            } else {


                if (Student_Home.mArrayList.get(Student_Home.selectedPosition).mStatus == Student_Home.STOP) {
                    Student_Home.mArrayList.get(Student_Home.selectedPosition).timer = "00:00\'00\"";
                } else if (Student_Home.mArrayList.get(Student_Home.selectedPosition).mStatus == Student_Home.PAUSE) {
                    Student_Home.mArrayList.get(Student_Home.selectedPosition).timer = getEllapse(Student_Home.selectedPosition);
                } else if (Student_Home.mArrayList.get(Student_Home.selectedPosition).mStatus == Student_Home.RUNNING) {
                    Student_Home.mArrayList.get(Student_Home.selectedPosition).timer = getEllapse(Student_Home.selectedPosition);
                }

                Student_Home.setTick();
                mTimer.sendEmptyMessageDelayed(0, 1000);

            }
        }
    };


    static String getEllapse(int selectedPosition) {        //스톱워치의 핵심 메서드
        long now = SystemClock.elapsedRealtime();    //현재부팅 경과시간 기록
        long ell = now - Student_Home.mArrayList.get(selectedPosition).mBaseTime;    // 시작버튼 누른시간부터 ~~기록버튼 클릭시간 계산하기
        String sEll = String.format("%02d:%02d\'%02d\"", (ell / (1000 * 60 * 60)) % 24, (ell / (1000 * 60)) % 60,
                (ell / 1000) % 60);
        return sEll;
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLayout = (StudentMenuLayout) this.getLayoutInflater().inflate(
                R.layout.activity_student_main, null);
        setContentView(mLayout);
        context = StudentMainActivity.this;
        try {
            kakaoLink = KakaoLink.getKakaoLink(this);
            kakaoTalkLinkMessageBuilder = kakaoLink.createKakaoTalkLinkMessageBuilder();
        }catch (KakaoParameterException e) {
            e.printStackTrace();
        }
        initData();

        backPressCloseHandler = new BackPressCloseHandler(this);

        myPrefs = getSharedPreferences("Azit", MODE_PRIVATE);
        secret = myPrefs.getString("secret", "");
        user_id = myPrefs.getString("user_id", "");

        lvMenuItems = getResources().getStringArray(R.array.student_menu_items);
        lvMenu = (ListView) findViewById(R.id.menu_listview);

        lvMenu.setAdapter(new MenuAdapter(this, lvMenuTag, lvMenuItems, lvMenuImages));
        lvMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                onMenuItemClick(position);
            }

        });

        profile_pic = (ImageView) findViewById(R.id.profile_pic);
        profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(StudentMainActivity.this, Edit_Student_Profile_.class);
                startActivity(i);
            }
        });

        profile_change = (ImageView) findViewById(R.id.profile_change);
        profile_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(StudentMainActivity.this, Edit_Student_Profile_.class);
                startActivity(i);
            }
        });

        btMenu = (ImageView) findViewById(R.id.button_menu);
        btMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show/hide the menu
                mLayout.toggleMenu();
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

        btPen = (ImageView) findViewById(R.id.pen);
        btPen.setVisibility(View.GONE);
        btPen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvTitle.getText().toString().equals("멘토만나기")) {
                    Intent i = new Intent(StudentMainActivity.this, Ask_Meet_Mentor.class);
                    startActivity(i);
                } else if (tvTitle.getText().toString().equals("풀어주세요")) {
                    Intent i = new Intent(StudentMainActivity.this, Ask_Azit_Clinic.class);
                    startActivity(i);
                } else if (tvTitle.getText().toString().equals("게시판")) {
                    Intent i = new Intent(StudentMainActivity.this, Ask_Free_Board.class);
                    startActivity(i);
                }
            }
        });

        btPerson = (ImageView) findViewById(R.id.person);
        btPerson.setVisibility(View.GONE);
        btPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(StudentMainActivity.this, My_Act.class);
                if (tvTitle.getText().toString().equals("멘토만나기")) {
                    i.putExtra("position", 0);
                } else if (tvTitle.getText().toString().equals("풀어주세요")) {
                    i.putExtra("position", 1);
                }
                startActivity(i);
            }
        });

        btAlarm = (ImageView) findViewById(R.id.alarm);
        btAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(StudentMainActivity.this, My_Act.class);
                startActivity(i);
            }
        });

        btMessage = (ImageView) findViewById(R.id.message);
        btMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(StudentMainActivity.this, MyMessage.class);
                startActivity(i);
            }
        });

        btPoint = (ImageView) findViewById(R.id.point);
        btPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(StudentMainActivity.this, "준비중 입니다.", Toast.LENGTH_SHORT).show();
            }
        });
        tvTitle = (TextView) findViewById(R.id.activity_main_content_title);

        FragmentManager fm = StudentMainActivity.this.getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        Intent temp = getIntent();
        int position = temp.getIntExtra("position", 0);
        boolean isRegister = temp.getBooleanExtra("register", false);
        if (isRegister){
            showDialogWelCome();
        }

        if (position == 0) {
            fragment = new Student_Home();
            btPoint.setVisibility(View.GONE);
            btAlarm.setVisibility(View.VISIBLE);
            btMenu.setVisibility(View.VISIBLE);
            btBack.setVisibility(View.GONE);
            btPen.setVisibility(View.GONE);
            btPerson.setVisibility(View.GONE);
            btMessage.setVisibility(View.VISIBLE);
            tvTitle.setText("AZit STUDY");
        } else if (position == 4) {
            fragment = new Meet_Mentor();
            btPoint.setVisibility(View.GONE);
            btAlarm.setVisibility(View.GONE);
            btMenu.setVisibility(View.VISIBLE);
            btPen.setVisibility(View.VISIBLE);
            btBack.setVisibility(View.GONE);
            btPerson.setVisibility(View.VISIBLE);
            btMessage.setVisibility(View.GONE);
            tvTitle.setText("멘토만나기");
        } else if (position == 5) {
            fragment = new Azit_Clinic();
            btPoint.setVisibility(View.GONE);
            btAlarm.setVisibility(View.GONE);
            btMenu.setVisibility(View.VISIBLE);
            btPen.setVisibility(View.VISIBLE);
            btPerson.setVisibility(View.VISIBLE);
            btBack.setVisibility(View.GONE);
            btMessage.setVisibility(View.GONE);
            tvTitle.setText("풀어주세요");
        } else if (position == 6) {
            fragment = new Mentoring();
            btAlarm.setVisibility(View.GONE);
            btMenu.setVisibility(View.GONE);
            btPen.setVisibility(View.GONE);
            btPerson.setVisibility(View.GONE);
            btBack.setVisibility(View.VISIBLE);
            btMessage.setVisibility(View.GONE);
            tvTitle.setText("맞춤 멘토링");
        }

        ft.add(R.id.activity_main_content_fragment, fragment);
        ft.detach(fragment);
        ft.attach(fragment);
        ft.commit();


        name = (TextView) findViewById(R.id.name);
        target_university = (TextView) findViewById(R.id.target_university);

        LoadFromServer();



        getListExam();
    }

    private void initData(){
        arrayUniversity = new ArrayList<>();
        arrayExam = new ArrayList<>();
        arrayIdExam = new ArrayList<>();
        arrayExamSubject = new ArrayList<>();
    }

    private void LoadFromServer() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("secret", secret);
            jsonObject.put("user_id", user_id);

            Logger.e(secret);
            Logger.e(user_id);
            requestJson(Url_define.Student_get_profile + Url_define.KEY, jsonObject, new ConnHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.d("statusCode : ", statusCode + "");
                    Log.d("response : ", response.toString());
                    super.onSuccess(statusCode, headers, response);

                    try {
                        if (response.getString("profile_image").equals("")) {
                            profile_pic.setBackgroundResource(R.drawable.profile_basic);
                        } else {
                            Picasso.with(StudentMainActivity.this).load(Url_define.BASE_Image + response.getString("profile_image")).fit().placeholder(R.drawable.black_crop).into(profile_pic);
                            Log.e("uri", Url_define.BASE + response.getString("profile_image"));
                        }
                        name.setText(response.getString("name"));
                        target_university.setText(response.getString("target_university"));

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

    public void onMenuItemClick(int position) {
        String selectedItem = lvMenuItems[position];

        FragmentManager fm = StudentMainActivity.this.getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        if (selectedItem.compareTo("홈") == 0) {
            fragment = new Student_Home();
            btPoint.setVisibility(View.GONE);
            btAlarm.setVisibility(View.VISIBLE);
            btMenu.setVisibility(View.VISIBLE);
            btBack.setVisibility(View.GONE);
            btPen.setVisibility(View.GONE);
            btPerson.setVisibility(View.GONE);
            btMessage.setVisibility(View.VISIBLE);
            tvTitle.setText("AZit STUDY");
        } else if (selectedItem.compareTo("나의 학습 분석") == 0) {
            fragment = new MyStudy();
            btPoint.setVisibility(View.GONE);
            btAlarm.setVisibility(View.GONE);
            btMenu.setVisibility(View.VISIBLE);
            btPen.setVisibility(View.GONE);
            btPerson.setVisibility(View.GONE);
            btBack.setVisibility(View.GONE);
            btMessage.setVisibility(View.GONE);
            tvTitle.setText(selectedItem);
        } else if (selectedItem.compareTo("나의 성적 분석") == 0) {
            fragment = new MyGrade();
            btPoint.setVisibility(View.GONE);
            btAlarm.setVisibility(View.GONE);
            btMenu.setVisibility(View.VISIBLE);
            btPen.setVisibility(View.GONE);
            btBack.setVisibility(View.GONE);
            btPerson.setVisibility(View.GONE);
            btMessage.setVisibility(View.GONE);
            tvTitle.setText(selectedItem);
        } else if (selectedItem.compareTo("게시판") == 0) {
            fragment = new Free_Borad();
            btPoint.setVisibility(View.GONE);
            btAlarm.setVisibility(View.GONE);
            btMenu.setVisibility(View.VISIBLE);
            btPen.setVisibility(View.VISIBLE);
            btBack.setVisibility(View.GONE);
            //btPerson.setVisibility(View.VISIBLE);
            btMessage.setVisibility(View.GONE);
            tvTitle.setText("게시판");
        }else if (selectedItem.compareTo("멘토만나기") == 0) {
            fragment = new Meet_Mentor();
            btPoint.setVisibility(View.GONE);
            btAlarm.setVisibility(View.GONE);
            btMenu.setVisibility(View.VISIBLE);
            btPen.setVisibility(View.VISIBLE);
            btBack.setVisibility(View.GONE);
            btPerson.setVisibility(View.VISIBLE);
            btMessage.setVisibility(View.GONE);
            tvTitle.setText("멘토만나기");
        } else if (selectedItem.compareTo("풀어주세요") == 0) {
            fragment = new Azit_Clinic();
            btPoint.setVisibility(View.GONE);
            btAlarm.setVisibility(View.GONE);
            btMenu.setVisibility(View.VISIBLE);
            btPen.setVisibility(View.VISIBLE);
            btPerson.setVisibility(View.VISIBLE);
            btBack.setVisibility(View.GONE);
            btMessage.setVisibility(View.GONE);
            tvTitle.setText("풀어주세요");
        } else if (selectedItem.compareTo("맞춤리포트") == 0) {
//            fragment = new Mentoring();
           fragment = new TutFragment();
            btPoint.setVisibility(View.GONE);
            btAlarm.setVisibility(View.GONE);
            btMenu.setVisibility(View.GONE);
            btPen.setVisibility(View.GONE);
            btPerson.setVisibility(View.GONE);
            btBack.setVisibility(View.VISIBLE);
            btMessage.setVisibility(View.GONE);
            tvTitle.setText("맞춤리포트");
        } else if (selectedItem.compareTo("문의하기") == 0) {
            fragment = new Inquiry();
            btPoint.setVisibility(View.GONE);
            btAlarm.setVisibility(View.GONE);
            btMenu.setVisibility(View.VISIBLE);
            btPen.setVisibility(View.GONE);
            btPerson.setVisibility(View.GONE);
            btBack.setVisibility(View.GONE);
            btMessage.setVisibility(View.GONE);
            tvTitle.setText("문의하기");
        }else if (selectedItem.compareTo("초대하기") == 0) {
            fragment = new Invitation();
            btPoint.setVisibility(View.GONE);
            btAlarm.setVisibility(View.GONE);
            btMenu.setVisibility(View.GONE);
            btPen.setVisibility(View.GONE);
            btPerson.setVisibility(View.GONE);
            btBack.setVisibility(View.VISIBLE);
            btMessage.setVisibility(View.GONE);
            tvTitle.setText("초대하기");


        }

        else if (selectedItem.compareTo("설정") == 0) {
            fragment = new App_Setting();
            btPoint.setVisibility(View.GONE);
            btAlarm.setVisibility(View.GONE);
            btMenu.setVisibility(View.GONE);
            btPen.setVisibility(View.GONE);
            btPerson.setVisibility(View.GONE);
            btBack.setVisibility(View.VISIBLE);
            btMessage.setVisibility(View.GONE);
            tvTitle.setText("설정");
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
        if (tvTitle.getText().toString().equals("AZit STUDY")) {
            if (mLayout.isMenuShown()) {
                mLayout.toggleMenu();
            } else {
                backPressCloseHandler.onBackPressed();

            }
        } else {
            FragmentManager fm = StudentMainActivity.this.getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();

            fragment = new Student_Home();
            btPoint.setVisibility(View.GONE);
            btAlarm.setVisibility(View.VISIBLE);
            btMenu.setVisibility(View.VISIBLE);
            btBack.setVisibility(View.GONE);
            btPen.setVisibility(View.GONE);
            btPerson.setVisibility(View.GONE);
            btMessage.setVisibility(View.VISIBLE);
            tvTitle.setText("AZit STUDY");
            ft.replace(R.id.activity_main_content_fragment, fragment);
            ft.detach(fragment);
            ft.attach(fragment);
            ft.commit();
        }
    }

    private void showDialogWelCome(){
        AlertDialog.Builder builder = new AlertDialog.Builder(StudentMainActivity.this);
        builder.setMessage(getResources().getString(R.string.message_dialog_welcome_student));
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showGradeInputDialog(StudentMainActivity.this);
            }
        });

        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    public void showGradeInputDialog(final Context context){
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.activity_inputgrade_lv1);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        //lp.height = WindowManager.LayoutParams.MATCH_PARENT;

        //for header
        tvTitleStep1 = (TextView)dialog.findViewById(R.id.tvTitleStep1);
        tvTitleStep2 = (TextView)dialog.findViewById(R.id.tvTitleStep2);
        tvTitleStep3 = (TextView)dialog.findViewById(R.id.tvTitleStep3);
        tvTitleStep1.setTextColor(getResources().getColor(android.R.color.holo_red_dark));

        includeInputGrade1 = (ScrollView)dialog.findViewById(R.id.includeInputGrade1);
        includeInputGrade2 = (LinearLayout)dialog.findViewById(R.id.includeInputGrade2);
        includeInputGrade3 = (RelativeLayout)dialog.findViewById(R.id.includeInputGrade3);
        includeInputGrade1.setVisibility(View.VISIBLE);
        includeInputGrade2.setVisibility(View.GONE);
        includeInputGrade3.setVisibility(View.GONE);


        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.btNext1:
                        saveSchoolRecord();
                        break;
                    case R.id.btNext2:
                        if (!checkInputDataExam()){
                            saveExam();
                        }else {
                            Toast.makeText(StudentMainActivity.this, "입력하신 정보를 다시 확인해주세요.", Toast.LENGTH_LONG).show();
                        }
                        break;
                    case R.id.btDone:
                        if (edUniversity.getText().toString().equals("")){
                            Toast.makeText(StudentMainActivity.this, "목표하는 대학을 입력하세요.", Toast.LENGTH_LONG).show();
                        }else{
                            saveTargetUniversity();
                        }

                        break;
                    case R.id.imgCloseRecordStudent:
                        dialog.dismiss();
                        break;
                }

            }
        };
        //for header
        ImageView imgCloseRecordStudent = (ImageView)dialog.findViewById(R.id.imgCloseRecordStudent);
        imgCloseRecordStudent.setOnClickListener(listener);


        // start for step1 ===================
        Button btNext1 = (Button)dialog.findViewById(R.id.btNext1);
        btNext1.setOnClickListener(listener);

        final Spinner spin_korean = (Spinner)dialog.findViewById(R.id.spin_korean);
        final Spinner spin_math = (Spinner)dialog.findViewById(R.id.spin_math);
        final Spinner spin_english = (Spinner)dialog.findViewById(R.id.spin_english);
        final Spinner spin_social = (Spinner)dialog.findViewById(R.id.spin_social);
        final Spinner spin_science = (Spinner)dialog.findViewById(R.id.spin_science);
        final Spinner spMonth = (Spinner)dialog.findViewById(R.id.spMonth);
        final TextView tvTitleMonth = (TextView)dialog.findViewById(R.id.tvTitleMonth);

        AdapterView.OnItemSelectedListener selectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent == spin_korean){
                    idKorean = position+1;
                }
                if (parent == spin_math){
                    idMath = position+1;
                }
                if (parent == spin_english){
                    idEnglish = position+1;
                }
                if (parent == spin_social){
                    idSocial = position+1;
                }
                if (parent == spin_science){
                    idScience = position+1;
                }
                if (parent == spMonth){
                    idMonth = position;
                    tvTitleMonth.setText(arrayExam.get(idMonth).getTitle());
                    arrayExamSubject.clear();
                    arrayExamSubject.addAll(arrayExam.get(idMonth).getExamSubject());
                    examAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
        spin_korean.setOnItemSelectedListener(selectedListener);
        spin_math.setOnItemSelectedListener(selectedListener);
        spin_english.setOnItemSelectedListener(selectedListener);
        spin_social.setOnItemSelectedListener(selectedListener);
        spin_science.setOnItemSelectedListener(selectedListener);
        // end for step1 ===================

        // start for step2 ===================
        //Spinner spMonth = (Spinner)dialog.findViewById(R.id.spMonth);
        ArrayAdapter<String> adapterMonth = new ArrayAdapter<String>(StudentMainActivity.this, android.R.layout.simple_spinner_item, arrayIdExam);
        spMonth.setAdapter(adapterMonth);
        spMonth.setOnItemSelectedListener(selectedListener);

        ListView lvExam = (ListView)dialog.findViewById(R.id.lvExam);
        idMonth = arrayExam.size() -1;
        examAdapter = new ExamAdapter(StudentMainActivity.this, arrayExamSubject);
        lvExam.setAdapter(examAdapter);
        spMonth.setSelection(idMonth);

        Button btNext2 = (Button)dialog.findViewById(R.id.btNext2);
        btNext2.setOnClickListener(listener);
        // end for step2 ===================

        // start for step3 ===================
        Button btDone = (Button)dialog.findViewById(R.id.btDone);
        btDone.setOnClickListener(listener);

        ListView listViewUniversity = (ListView)dialog.findViewById(R.id.lvUniversity);
        universityAdapter = new UniversityAdapter(StudentMainActivity.this, R.layout.item_university_save, arrayUniversity);
        listViewUniversity.setAdapter(universityAdapter);

        edUniversity = (AutoCompleteTextView)dialog.findViewById(R.id.edTargetUniversity);

        String[] arrayOfSchool = getResources().getStringArray(R.array.school_name);
        final String[] arrayOfSchoolId = getResources().getStringArray(R.array.school_id);

        hashTable = new Hashtable<String, String>();
        for (int i = 0; i < arrayOfSchool.length; i++) {
            hashTable.put(arrayOfSchool[i], arrayOfSchoolId[i]);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, arrayOfSchool);
        edUniversity.setAdapter(adapter);
        // end for step3 ===================

        if (dialog != null) {
            dialog.show();
        }

        dialog.getWindow().setAttributes(lp);
    }

    private void saveSchoolRecord() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("secret", secret);
            jsonObject.put("user_id", user_id);

            jsonObject.put("korean", idKorean);
            jsonObject.put("math", idMath);
            jsonObject.put("english", idEnglish);
            jsonObject.put("social", idSocial);
            jsonObject.put("science", idScience);

            requestJson(Url_define.STUDENT_SCHOOL_SAVE_API + Url_define.KEY, jsonObject, new ConnHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);

                    try {
                        if (response.get("result").toString().equals("success")) {
                            //next step
                            includeInputGrade1.setVisibility(View.GONE);
                            includeInputGrade2.setVisibility(View.VISIBLE);
                            tvTitleStep1.setTextColor(getResources().getColor(android.R.color.black));
                            tvTitleStep2.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                    super.onFailure(statusCode, headers, res, t);
                }
            });
        } catch (JSONException e) {
            e.getStackTrace();
        }
    }

    private void getListUniversityRecommend() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("secret", secret);
            jsonObject.put("user_id", user_id);

            requestJson(Url_define.UNIVERSITY_RECOMMEND_API + Url_define.KEY, jsonObject, new ConnHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);

                    try {
                        if (response.get("result").toString().equals("success")) {

                            JSONArray universityJsonArray = new JSONArray(response.getString("university_list"));

                            int numberUniversity = universityJsonArray.length();
                            for (int i =0; i< numberUniversity; i++){
                                JSONObject jsonUniver = (JSONObject)universityJsonArray.get(i);
                                University object = new University(jsonUniver.getString("university"),jsonUniver.getString("department"), jsonUniver.getInt("optional") );
                                arrayUniversity.add(object);
                            }
                            universityAdapter.notifyDataSetChanged();

                            Log.d("onSuccess", "arrayUniversity: " + arrayUniversity.size());
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                    super.onFailure(statusCode, headers, res, t);
                }
            });
        } catch (JSONException e) {
            e.getStackTrace();
        }
    }

    private void getListExam() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("secret", secret);
            jsonObject.put("user_id", user_id);

            requestJson(Url_define.LIST_EXAM_API + Url_define.KEY, jsonObject, new ConnHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);

                    parserListExam(response);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                    super.onFailure(statusCode, headers, res, t);
                }
            });
        } catch (JSONException e) {
            e.getStackTrace();
        }
    }

    private void parserListExam(JSONObject response){
        try {
            if (response.get("result").toString().equals("success")) {
                JSONArray jsonArrayExam = new JSONArray(response.getString("exam_list"));

                int numberExam = jsonArrayExam.length();
                for (int i =0; i< numberExam; i++){
                    JSONObject jsonExam = (JSONObject)jsonArrayExam.get(i);
                    Exam exam = new Exam();
                    exam.setId(jsonExam.getString("id"));
                    exam.setTitle(jsonExam.getString("title"));
                    ArrayList<Item> arrayExamSubject = new ArrayList<>();

                    //get array score
                    JSONArray arrayScore = new JSONArray(jsonExam.getString("exam_subject_score"));
                    ArrayList<Subject> arrayListScore = new ArrayList<>();
                    arrayExamSubject.add(new SectionItemExam(jsonExam.getString("id") + " 월 모의고사 점수"));
                    for (int j =0; j< arrayScore.length(); j++){
                        JSONObject jsonSubject = (JSONObject)arrayScore.get(j);
                        Subject subject = new Subject(jsonSubject.getString("id"), jsonSubject.getString("title"), 1,"점", 1);
                        arrayListScore.add(subject);
                        arrayExamSubject.add(subject);
                    }
                    exam.setExam_subject_score(arrayListScore);

                    //get array percentile
                    JSONArray arrayPercentile = new JSONArray(jsonExam.getString("exam_subject_percentile"));
                    ArrayList<Subject> arrayListPercentile = new ArrayList<>();
                    arrayExamSubject.add(new SectionItemExam(jsonExam.getString("id") + " 월 모의고사 백분위"));
                    for (int j =0; j< arrayPercentile.length(); j++){
                        JSONObject jsonSubject = (JSONObject)arrayPercentile.get(j);
                        Subject subject = new Subject(jsonSubject.getString("id"), jsonSubject.getString("title"),2,"%",1);
                        arrayListPercentile.add(subject);
                        arrayExamSubject.add(subject);
                    }
                    exam.setExam_subject_score(arrayListPercentile);

                    //get array standard
                    JSONArray arrayStandard = new JSONArray(jsonExam.getString("exam_subject_standard"));
                    ArrayList<Subject> arrayListStandard = new ArrayList<>();
                    arrayExamSubject.add(new SectionItemExam(jsonExam.getString("id") + " 월 모의고사 표준점수"));
                    for (int j =0; j< arrayStandard.length(); j++){
                        JSONObject jsonSubject = (JSONObject)arrayStandard.get(j);
                        Subject subject = new Subject(jsonSubject.getString("id"), jsonSubject.getString("title"),3,"점",2);
                        arrayListStandard.add(subject);
                        arrayExamSubject.add(subject);
                    }
                    exam.setExam_subject_score(arrayListStandard);
                    exam.setExamSubject(arrayExamSubject);

                    arrayExam.add(exam);
                }

                getListExamId();
                Log.d("onSuccess", "arrayExam: " + arrayExam.size());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getListExamId(){
        if (arrayExam!=null && arrayExam.size()>0){
            for (int i=0; i< arrayExam.size(); i++){
                //arrayIdExam.add(arrayExam.get(i).getId());
                arrayIdExam.add(arrayExam.get(i).getTitle());
            }
        }
    }

    private JSONObject createJSONObjectSaveExam(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("secret", secret);
            jsonObject.put("user_id", user_id);
            jsonObject.put("exam_id", Integer.valueOf(arrayExam.get(idMonth).getId()));

            JSONArray jsonArrayScore = new JSONArray();
            JSONArray jsonArrayPercentile = new JSONArray();
            JSONArray jsonArrayStandard = new JSONArray();
            for (int i=0; i< arrayExamSubject.size(); i++){
                if (!arrayExamSubject.get(i).isSection()){
                    Subject subject = (Subject)arrayExamSubject.get(i);
                    if (subject.getClassificationSubject()==1){
                        JSONObject object = new JSONObject();
                        object.put("id" , Integer.valueOf(subject.getId()));
                        object.put("score" , Integer.valueOf(subject.getData()));
                        jsonArrayScore.put(object);
                    }

                    if (subject.getClassificationSubject()==2){
                        JSONObject object = new JSONObject();
                        object.put("id" , Integer.valueOf(subject.getId()));
                        object.put("score" , Integer.valueOf(subject.getData()));
                        jsonArrayPercentile.put(object);
                    }

                    if (subject.getClassificationSubject()==3){
                        JSONObject object = new JSONObject();
                        object.put("id" , Integer.valueOf(subject.getId()));
                        object.put("score" , Integer.valueOf(subject.getData()));
                        jsonArrayStandard.put(object);
                    }
                }

            }

            jsonObject.put("score",jsonArrayScore);
            jsonObject.put("percentile",jsonArrayPercentile);
            jsonObject.put("standard",jsonArrayStandard);


        }catch (JSONException e) {
            e.getStackTrace();
        }

        return jsonObject;

    }

    private void saveExam() {
        JSONObject jsonObject = createJSONObjectSaveExam();

        requestJson(Url_define.SAVE_EXAM_API + Url_define.KEY, jsonObject, new ConnHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                includeInputGrade1.setVisibility(View.GONE);
                includeInputGrade2.setVisibility(View.GONE);
                includeInputGrade3.setVisibility(View.VISIBLE);

                tvTitleStep1.setTextColor(getResources().getColor(android.R.color.black));
                tvTitleStep2.setTextColor(getResources().getColor(android.R.color.black));
                tvTitleStep3.setTextColor(getResources().getColor(android.R.color.holo_red_dark));

                getListUniversityRecommend();
                Log.d("onSuccess", "saveExam successfully: ");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                super.onFailure(statusCode, headers, res, t);
            }
        });

    }

    private void saveTargetUniversity() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("secret", secret);
            jsonObject.put("user_id", user_id);
            jsonObject.put("target_university", edUniversity.getText());


            requestJson(Url_define.SAVE_UNIVERSITY_API + Url_define.KEY, jsonObject, new ConnHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    if (dialog!= null && dialog.isShowing()){
                        dialog.dismiss();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                    super.onFailure(statusCode, headers, res, t);
                }
            });
        } catch (JSONException e) {
            e.getStackTrace();
        }
    }

    private boolean checkInputDataExam() {
        for (int i = 0; i < arrayExamSubject.size(); i++) {
            Item item = arrayExamSubject.get(i);
            if (!item.isSection()) {
                Subject subject = (Subject) item;
                if (subject.getData().equals("")) {
                    return true;
                }
            }
        }
        return false;
    }

}
