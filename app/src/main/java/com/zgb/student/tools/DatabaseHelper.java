package com.zgb.student.tools;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import java.io.File;
import java.io.IOException;

/**
 * 单例模式
 * Created by zgb on 2018.03.31
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static DatabaseHelper instance;
    public static final String CREATE_ADMIN = "create table admin(id integer primary key autoincrement, name text,password text)";//创建管理员表
   public static final String CREATE_HEALTH = "create table health(id text, measureDate text,info text)";//创建健康表
    public static final String CREATE_STUDENT = "create table student(id text primary key,name text,password text,sex text,number text,mathScore integer,chineseScore integer,englishScore integer, measureDate text,info text)";//创建学生表
    public static final String DB_NAME = "student.db";

    private DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_ADMIN);
        db.execSQL(CREATE_STUDENT);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            if(newVersion==3){
                db.execSQL(CREATE_HEALTH);
            }

    }

    public static DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context, getMyDatabaseName(context), null, 3);

        }
        return instance;

    }

    private static String getMyDatabaseName(Context context){
        String dbName = "";
        boolean isSdcardEnable =false;
        String state =Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(state)){//SDCard是否插入
            isSdcardEnable = true;
        }
        String dbPath = null;
        if(isSdcardEnable){
            dbPath =Environment.getExternalStorageDirectory().getPath() +"/database/";
        }else{//未插入SDCard，建在内存中
            dbPath =context.getFilesDir().getPath() + "/database/";
        }
        File dbp = new File(dbPath);
        if(!dbp.exists()){
            dbp.mkdirs();
        }
        dbName = dbPath +DB_NAME;
        return dbName;
    }
}
