package com.trams.azit.model;

/**
 * Created by sonnv on 1/15/2016.
 */
public class University {
    private String university;
    private String department;
    private int optional;

    public University() {
    }

    public University(String university, String department, int optional) {
        this.university = university;
        this.department = department;
        this.optional = optional;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getOptional() {
        return optional;
    }

    public void setOptional(int optional) {
        this.optional = optional;
    }
}
