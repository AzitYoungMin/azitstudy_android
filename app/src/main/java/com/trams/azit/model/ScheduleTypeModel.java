package com.trams.azit.model;

/**
 * Created by Administrator on 15/01/2016.
 */
public class ScheduleTypeModel {

    private int id;
    private String name;
    private boolean isAddIcon;
    private int activityType;
    private boolean idDelete;
    private int icon_id;

    public ScheduleTypeModel() {

    }

    public ScheduleTypeModel(int id, String name, boolean isAddIcon, int activityType, boolean idDelete, int icon_id) {
        this.id = id;
        this.name = name;
        this.isAddIcon = isAddIcon;
        this.activityType = activityType;
        this.idDelete = idDelete;
        this.icon_id = icon_id;
    }

    public int getIcon_id() {
        return icon_id;
    }

    public void setIcon_id(int icon_id) {
        this.icon_id = icon_id;
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

    public boolean isAddIcon() {
        return isAddIcon;
    }

    public void setIsAddIcon(boolean isAddIcon) {
        this.isAddIcon = isAddIcon;
    }

    public int getActivityType() {
        return activityType;
    }

    public void setActivityType(int activityType) {
        this.activityType = activityType;
    }

    public boolean isIdDelete() {
        return idDelete;
    }

    public void setIdDelete(boolean idDelete) {
        this.idDelete = idDelete;
    }

    @Override
    public String toString() {
        return "ScheduleTypeModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", isAddIcon=" + isAddIcon +
                ", activityType=" + activityType +
                ", idDelete=" + idDelete +
                ", icon_id=" + icon_id +
                '}';
    }

}
