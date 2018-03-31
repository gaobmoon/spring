package com.zgb.student.model;

/**
 * Created by zgb on 2018/3/31.
 */

public class HealthInfo {
    private String id;//学号
    private String info;//健康状况
    private String measureDate;//测量日期

    public HealthInfo(String id, String info, String measureDate) {
        this.id = id;
        this.info = info;
        this.measureDate = measureDate;
    }

    public String getId() {
        return id;
    }

    public String getInfo() {
        return info;
    }

    public String getMeasureDate() {
        return measureDate;
    }
}
