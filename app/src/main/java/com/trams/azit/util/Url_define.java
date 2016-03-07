package com.trams.azit.util;

/**
 * Created by Administrator on 2015-09-04.
 */
public class Url_define {

    public static String BASE = "http://52.192.0.99";
    //        public static String BASE = "http://192.168.0.20:9000";
    public static String BASE_Image = "http://52.192.0.99:5000";
    public static String KEY = "?key=sQzVKrZqrVYOt/KaAlsw5Adu2es";
    public static String Login = BASE + "/api/login";
    public static String Teacher_Main = BASE + "/api/teacher/student/list";
    public static String Teacher_Search_student = BASE + "/api/teacher/student/search";
    public static String Teacher_add_student = BASE + "/api/teacher/student/add";

    public static String Student_get_profile = BASE + "/api/student/profile";
    public static String Student_Push_Setting = BASE + "/api/push/setting";
    public static String Student_Edit_profile = BASE + "/api/student/profile/edit";
    public static String Student_Get_My_act = BASE + "/api/student/activity/list";
    public static String Student_Plus_My_act = BASE + "/api/student/activity/add";
    public static String Student_Delete_My_act = BASE + "/api/student/activity/delete";
    public static String Student_Send_My_Time = BASE + "/api/student/activity/time/save";
    public static String Student_UPDATE_My_Time = BASE + "/api/student/activity/time/update";
    public static String Student_Get_DDayList = BASE + "/api/student/d-day/list";
    public static String Student_Get_Main_DDay = BASE + "/api/student/d-day";
    public static String Student_Set_DDay = BASE + "/api/student/d-day/choice";
    public static String Student_Edit_DDay = BASE + "/api/student/d-day/edit";
    public static String Student_Delete_DDay = BASE + "/api/student/d-day/delete";
    public static String Student_Plus_DDay = BASE + "/api/student/d-day/save";
    public static String Student_Get_List = BASE + "/api/posting/list";
    public static String Student_Save_Ask_Clinic = BASE + "/api/posting/clinic/save";
    public static String Student_Save_Ask_mento = BASE + "/api/posting/mentoring/save";
    public static String Student_Main_Grade = BASE + "/api/analysis/grade";

    public static String Student_Get_Ask_mento = BASE + "/api/posting/mentoring/get";
    public static String Student_Update_Ask_mento = BASE + "/api/posting/mentoring/update";
    public static String Student_Get_Ask_Clinic = BASE + "/api/posting/clinic/get";
    public static String Student_Update_Ask_Clinic = BASE + "/api/posting/clinic/update";

    public static String Common_Send_message = BASE + "/api/message/send";
    public static String Common_Delete_message = BASE + "/api/message/delete";
    public static String Common_Get_message = BASE + "/api/message/get";
    public static String Mento_Get_Profile = BASE + "/api/mentor/profile";
    public static String Mento_Edit_Profile = BASE + "/api/mentor/profile/edit";
    public static String Find_Pass = BASE + "/api/password/find";
    public static String Common_Reply_like = BASE + "/api/posting/reply/like";
    public static String Common_Reply_delete = BASE + "/api/posting/reply/delete";
    public static String Student_Clinic_Evaluation = BASE + "/api/posting/clinic/evaluation";
    public static String Student_Clinic_Choice = BASE + "/api/posting/clinic/choice";
    public static String Student_Clinic_ReChoice = BASE + "/api/posting/answer/choice/change";
    public static String Teacher_signup = BASE + "/api/teacher/signup";

    public static String STUDENT_SCHOOL_SAVE_API = BASE + "/api/student/school-record/save";
    public static String UNIVERSITY_RECOMMEND_API = BASE + "/api/student/university/recommend";
    public static String LIST_EXAM_API = BASE + "/api/student/exam-record/list";
    public static String SAVE_EXAM_API = BASE + "/api/student/exam-record/save";
    public static String SAVE_UNIVERSITY_API = BASE + "/api/student/university/save";

    public static final String TEACHER_PROFILE = BASE + "/api/teacher/profile";
    public static final String TEACHER_PROFILE_EDIT = BASE + "/api/teacher/profile/edit";
    public static final String CHANGE_PASS = BASE + "/api/password/change";
    public static final String CHANGE_PHONE = BASE + "/api/phone/change";

    public static final String GET_TEXTBOOK = BASE + "/api/student/activity/textbook/list";
    public static final String GET_TEXTBOOK_LIST = BASE + "/api/student/posting/textbook/list";

    public static final String STUDENT_GOAL_SAVE = BASE + "/api/student/study/goal/save";
    public static final String STUDENT_GOAL = BASE + "/api/student/study/goal";

    public static String mentor_posting = BASE + "/api/mentor/posting/answer";
    public static String report_posting = BASE + "/api/posting/reply/report";
    public static String answer_report = BASE + "/api/posting/report";
    public static String posting_delete = BASE + "/api/posting/delete";

    public static String mentor_savingpoint = BASE + "/api/mentor/save/history";
    public static String mentor_refundpoint = BASE + "/api/mentor/refund/history";
    public static String mentor_refund = BASE + "/api/mentor/refund";

    public static final String SCHEDULE_ADD_CUSTOM_TYPE = BASE + "/api/student/activity/custom-type/add";
    public static final String SCHEDULE_GET_ALL_CUSTOM_TYPE = BASE + "/api/student/activity/custom-type/list";
    public static final String SCHEDULE_DELETE_CUSTOM_TYPE = BASE + "/api/student/activity/custom-type/delete";

    public static final String SCHEDULE_TEXT_BOOK_LIST = BASE + "/api/student/activity/selected-textbook/list";
    public static final String SCHEDULE_TEXT_BOOK_ADD = BASE + "/api/student/activity/selected-textbook/add";
    public static final String SCHEDULE_TEXT_BOOK_DELETE = BASE + "/api/student/activity/selected-textbook/delete";

    public static final String STUDENT_ANALYTIC_ALL = BASE + "/api/analysis/study/all";
    public static final String STUDENT_DAILY_DATA = BASE + "/api/analysis/study/daily";
    public static final String STUDENT_SUBJECT_DATA = BASE + "/api/analysis/study/subject";

    public static final String STUDENT_SEND_MESSAGE = BASE + "/api/message/send";
    public static final String PUSH_SETTING = BASE + "/api/push/setting";
    public static final String UPDATE_SCHOOL_TEACHER = BASE + "/api/teacher/profile/last-school/update";
    public static final String UPDATE_EDU_TEACHER = BASE + "/api/teacher/profile/edu-inst/update";
    public static final String TEACHER_AUTHENTICATION = BASE + "/api/teacher/authentication";

    public static final String WITHDRAWAL = BASE + "/api/withdrawal";
    public static final String Notice = BASE + "/api/notice/list";
    public static final String SET_TOKEN = BASE + "/api/token/set";

    public static final String STUDENT_ADD_CUSTOM_TEXTBOOK = BASE + "/api/student/custom/textbook/add";
}
