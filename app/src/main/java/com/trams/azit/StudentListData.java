package com.trams.azit;

/**
 * Created by Administrator on 2015-09-03.
 */
public class StudentListData {
    public int idx;
    public String photo;
    public String stu_name;
    public int mon;
    public int tue;
    public int wed;
    public int thu;
    public int fri;
    public int sat;
    public int sun;
    public String mock;

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean isChecked() {
        return checked;
    }

    public boolean checked;
}
