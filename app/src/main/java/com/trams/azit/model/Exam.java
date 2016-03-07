package com.trams.azit.model;

import java.util.ArrayList;

/**
 * Created by sonnv on 1/15/2016.
 */
public class Exam {
    private String id;
    private String title;

    private ArrayList<Subject> exam_subject_score;
    private ArrayList<Subject> exam_subject_percentile;
    private ArrayList<Subject> exam_subject_standard;

    private ArrayList<Item> examSubject;

    public Exam() {
    }

    public Exam(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public Exam(String id, String title, ArrayList<Subject> exam_subject_score, ArrayList<Subject> exam_subject_percentile, ArrayList<Subject> exam_subject_standard) {
        this.id = id;
        this.title = title;
        this.exam_subject_score = exam_subject_score;
        this.exam_subject_percentile = exam_subject_percentile;
        this.exam_subject_standard = exam_subject_standard;
    }

    public Exam(String id, String title, ArrayList<Item> examSubject) {
        this.id = id;
        this.title = title;
        this.examSubject = examSubject;
    }

    public ArrayList<Item> getExamSubject() {
        return examSubject;
    }

    public void setExamSubject(ArrayList<Item> examSubject) {
        this.examSubject = examSubject;
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

    public ArrayList<Subject> getExam_subject_score() {
        return exam_subject_score;
    }

    public void setExam_subject_score(ArrayList<Subject> exam_subject_score) {
        this.exam_subject_score = exam_subject_score;
    }

    public ArrayList<Subject> getExam_subject_percentile() {
        return exam_subject_percentile;
    }

    public void setExam_subject_percentile(ArrayList<Subject> exam_subject_percentile) {
        this.exam_subject_percentile = exam_subject_percentile;
    }

    public ArrayList<Subject> getExam_subject_standard() {
        return exam_subject_standard;
    }

    public void setExam_subject_standard(ArrayList<Subject> exam_subject_standard) {
        this.exam_subject_standard = exam_subject_standard;
    }

}
