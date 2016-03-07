package com.trams.azit.model;

/**
 * Created by Administrator on 15/01/2016.
 */
public class ScheduleSubjectModel {

    private int id;
    private String name;
    private int idType;
    private String typeName;
    private String description;

    public ScheduleSubjectModel() {

    }

    public ScheduleSubjectModel(int id, String name, int idType, String typeName, String description) {
        this.id = id;
        this.name = name;
        this.idType = idType;
        this.typeName = typeName;
        this.description = description;
    }

    @Override
    public String toString() {
        return "ScheduleSubjectModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", idType=" + idType +
                ", typeName='" + typeName + '\'' +
                ", description='" + description + '\'' +
                '}';
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

    public int getIdType() {
        return idType;
    }

    public void setIdType(int idType) {
        this.idType = idType;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
