package com.zgb.student.model;

/**
 * 保存学生信息的实体类
 * Created by com.zgb on 2016/10/1.
 */
public class Student {
    private String name;
    private String sex;
    private String id;//学号
    private String password;//学生登录密码
    private String info;//健康状况
    private String measureDate;//测量日期

    public Student(String name, String sex, String id, String password, String info, String measureDate) {
        this.name = name;
        this.sex = sex;
        this.id = id;
        this.password = password;
        this.info = info;
        this.measureDate = measureDate;
    }
    //    private HealthInfo healthInfo;//学生健康状况
//    private String number;//手机号
//    private int MathScore;
//    private int ChineseScore;
//    private int EnglishScore;
//    private int order;//名次


    public String getInfo() {
        return info;
    }

    public String getMeasureDate() {
        return measureDate;
    }

    public String getId() {
        return id;
    }



    public String getName() {
        return name;
    }


    public String getPassword() {
        return password;
    }

    public String getSex() {
        return sex;
    }



}
