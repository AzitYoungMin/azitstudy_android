package com.trams.azit.model;

/**
 * Created by Administrator on 15/01/2016.
 */
public class ScheduleTextBookModel {

    private int id;
    private String name;
    private int typeId;
    private String typeName;
    private String des;
    private boolean isImgPlus;
    private boolean isCustom = false;

    public boolean getIsCustom() {
        return isCustom;
    }
    public void setIsCustom(boolean custom) {
        isCustom = custom;
    }
    @Override
    public String toString() {
        //        return "ScheduleTextBookModel{" +
//                "id=" + id +
//                ", name='" + name + '\'' +
//                ", typeId=" + typeId +
//                ", typeName='" + typeName + '\'' +
//                ", des='" + des + '\'' +
//                ", isImgPlus=" + isImgPlus +
//                '}';
        return name;
    }

    public ScheduleTextBookModel() {

    }

    public ScheduleTextBookModel(int id, String name, int typeId, String typeName, String des, boolean isImgPlus) {
        this.id = id;
        this.name = name;
        this.typeId = typeId;
        this.typeName = typeName;
        this.des = des;
        this.isImgPlus = isImgPlus;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public boolean isImgPlus() {
        return isImgPlus;
    }

    public void setIsImgPlus(boolean isImgPlus) {
        this.isImgPlus = isImgPlus;
    }


}
