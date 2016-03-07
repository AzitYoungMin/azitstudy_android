package com.trams.azit;

/**
 * Created by Administrator on 05/01/2016.
 */
public class BelongModel {

    private String name;
    private String id;
    private boolean isSelected = false;

    @Override
    public String toString() {
        return "BelongModel{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", isSelected=" + isSelected +
                '}';
    }

    public BelongModel(boolean isSelected, String id, String name) {
        this.isSelected = isSelected;
        this.id = id;
        this.name = name;
    }

    public BelongModel() {

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
