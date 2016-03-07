package com.trams.azit.model;

import java.util.ArrayList;

/**
 * Created by Administrator on 11/01/2016.
 */
public class Student {

    private int id;
    private String name;
    private String school;
    private String email;
    private boolean isSelected;
    private String mockTest;
    private String testDescription;
    private String photo;
    public int mon;
    public int tue;
    public int wed;
    public int thu;
    public int fri;
    public int sat;
    public int sun;
    private ArrayList<ExamModel> examModels;

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", school='" + school + '\'' +
                ", email='" + email + '\'' +
                ", isSelected=" + isSelected +
                ", mockTest='" + mockTest + '\'' +
                ", testDescription='" + testDescription + '\'' +
                ", photo='" + photo + '\'' +
                ", mon=" + mon +
                ", tue=" + tue +
                ", wed=" + wed +
                ", thu=" + thu +
                ", fri=" + fri +
                ", sat=" + sat +
                ", sun=" + sun +
                '}';
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getMon() {
        return mon;
    }

    public void setMon(int mon) {
        this.mon = mon;
    }

    public int getTue() {
        return tue;
    }

    public void setTue(int tue) {
        this.tue = tue;
    }

    public int getWed() {
        return wed;
    }

    public void setWed(int wed) {
        this.wed = wed;
    }

    public int getThu() {
        return thu;
    }

    public void setThu(int thu) {
        this.thu = thu;
    }

    public int getFri() {
        return fri;
    }

    public void setFri(int fri) {
        this.fri = fri;
    }

    public int getSat() {
        return sat;
    }

    public void setSat(int sat) {
        this.sat = sat;
    }

    public int getSun() {
        return sun;
    }

    public void setSun(int sun) {
        this.sun = sun;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTestDescription() {
        return testDescription;
    }

    public void setTestDescription(String testDescription) {
        this.testDescription = testDescription;
    }

    public String getMockTest() {
        return mockTest;
    }

    public void setMockTest(String mockTest) {
        this.mockTest = mockTest;
    }

    public ArrayList<ExamModel> getExamModels() {
        return examModels;
    }

    public void setExamModels(ArrayList<ExamModel> examModels) {
        this.examModels = examModels;
    }
}
