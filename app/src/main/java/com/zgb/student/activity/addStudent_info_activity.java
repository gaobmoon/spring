package com.zgb.student.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zgb.student.R;
import com.zgb.student.tools.DatabaseHelper;

/**
 * 添加学生信息的界面,修改学生信息的界面
 * Created by com.zgb on 2016/10/1.
 */
public class addStudent_info_activity extends Activity {

    private EditText name;
    private EditText sex;
    private EditText id;
    private EditText measureDate;
    private EditText password;
    private EditText info;
//    private String oldID;//用于防治修改信息时将ID也修改了，而原始的有该ID的学生信息还保存在数据库中
    private Button sureButton;//确定按钮
    private DatabaseHelper dbHelper;
//    Intent oldData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.add_student_info_layout);

        addSureButtonEvent();
    }
    /**
     * 新增学生基本信息
     */
    private void addSureButtonEvent(){

        name = (EditText) findViewById(R.id.add_student_layout_name);
        sex = (EditText) findViewById(R.id.add_student_layout_sex);
        id = (EditText) findViewById(R.id.add_student_layout_id);

        dbHelper = DatabaseHelper.getInstance(this);

//        oldData = getIntent();
//        if (oldData.getStringExtra("haveData").equals("true")) {
//            initInfo();//恢复旧数据
//        }

        sureButton = (Button) findViewById(R.id.add_student_layout_sure);
        //将数据插入数据库
        sureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //id,name,sex都不能为空
                final String id_ = id.getText().toString();
                final String name_ = name.getText().toString();
                final String sex_ = sex.getText().toString();

                if (TextUtils.isEmpty(name_)) {
                    Toast.makeText(addStudent_info_activity.this, "请填写姓名!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(sex_)) {
                    Toast.makeText(addStudent_info_activity.this, "请填写性别!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!sex_.matches("[女|男]")) {
                    Toast.makeText(addStudent_info_activity.this, "请填写正确性别!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(id_)) {
                    Toast.makeText(addStudent_info_activity.this, "请填写学号!", Toast.LENGTH_SHORT).show();
                    return;
                }
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                //判断学号是否重复
                Cursor cursor = db.rawQuery("select id,sex,name from student where id=?", new String[]{id_});
                if (cursor.moveToNext()) {
                    String sId=cursor.getString(0);
                    String sSex=cursor.getString(1);
                    String sName=cursor.getString(2);
                    new AlertDialog.Builder(addStudent_info_activity.this).setTitle("确定更新吗?")//设置对话框标题
                            .setMessage("学号"+sId+"已经被"+sName+"同学使用!")//设置显示的内容
                            .setPositiveButton("确定",new DialogInterface.OnClickListener() {//添加确定按钮

                                @Override
                                public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                                    ContentValues values = new ContentValues();
                                    values.put("name",name_);
                                    values.put("sex",sex_);
                                    int num = dbHelper.getWritableDatabase().update("student",values,"id=?", new String[]{id_});
                                    if(num>0){
                                        Toast.makeText(addStudent_info_activity.this, "更新学号"+id_+"同学的基本信息", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }).setNegativeButton("取消",new DialogInterface.OnClickListener() {//添加返回按钮
                                @Override
                                public void onClick(DialogInterface dialog, int which) {//响应事件
                                    Toast.makeText(addStudent_info_activity.this, "请输入正确学号!", Toast.LENGTH_SHORT).show();
                                }
                    }).show();//在按键响应事件中显示此对话框

                } else {
                    db.execSQL("insert into student(id,name,sex) values(?,?,?)", new String[]{id_, name_, sex_,});
                    Toast.makeText(addStudent_info_activity.this, "新增"+name_+"同学的基本信息!", Toast.LENGTH_SHORT).show();
                }
                cursor.close();
            }
        });

    }
}
