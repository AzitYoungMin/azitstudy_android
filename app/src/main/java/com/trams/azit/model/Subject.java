package com.trams.azit.model;

/**
 * Created by sonnv on 1/15/2016.
 */
public class Subject implements Item {
    private String id = "0";
    private String title;
    private int classificationSubject;
    private String unit;
    private String data;
    private int classUnit;

    public Subject(String id, String title, int classificationSubject, String unit, int classUnit) {
        this.id = id;
        this.title = title;
        this.classificationSubject = classificationSubject;
        this.unit = unit;
        this.data = "";
        this.classUnit = classUnit;
    }

    public int getClassUnit() {
        return classUnit;
    }

    public void setClassUnit(int classUnit) {
        this.classUnit = classUnit;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getClassificationSubject() {
        return classificationSubject;
    }

    public void setClassificationSubject(int classificationSubject) {
        this.classificationSubject = classificationSubject;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean isSection() {
        return false;
    }
}
