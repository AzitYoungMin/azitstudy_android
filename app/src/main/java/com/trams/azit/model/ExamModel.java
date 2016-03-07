package com.trams.azit.model;

/**
 * Created by Administrator on 28/01/2016.
 */
public class ExamModel {

    private int id;
    private String title;
    private boolean hasScore;

    @Override
    public String toString() {
        return "ExamModel{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", hasScore=" + hasScore +
                '}';
    }

    public ExamModel() {

    }

    public ExamModel(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isHasScore() {
        return hasScore;
    }

    public void setHasScore(boolean hasScore) {
        this.hasScore = hasScore;
    }

}
