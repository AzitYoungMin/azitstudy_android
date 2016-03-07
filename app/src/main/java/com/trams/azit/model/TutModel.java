package com.trams.azit.model;

/**
 * Created by Administrator on 25/01/2016.
 */
public class TutModel {

    private int idIcon, idDes;

    public TutModel() {

    }

    public TutModel(int idIcon, int idDes) {
        this.idIcon = idIcon;
        this.idDes = idDes;
    }


    public int getIdIcon() {
        return idIcon;
    }

    public void setIdIcon(int idIcon) {
        this.idIcon = idIcon;
    }

    public int getIdDes() {
        return idDes;
    }

    public void setIdDes(int idDes) {
        this.idDes = idDes;
    }

}
