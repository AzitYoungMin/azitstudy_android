package com.trams.azit.model;

/**
 * Created by sonnv on 1/15/2016.
 */
public class SectionItemExam implements Item{
    private String title;

    public SectionItemExam(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean isSection() {
        return true;
    }
}
