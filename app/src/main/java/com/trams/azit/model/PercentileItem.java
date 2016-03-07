package com.trams.azit.model;

/**
 * Created by zin9x on 1/15/2016.
 */
public class PercentileItem {
    private String id;
    private String month;
    private String percentile;

    public PercentileItem() {
        super();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getPercentile() {
        return percentile;
    }

    public void setPercentile(String percentile) {
        this.percentile = percentile;
    }
}
