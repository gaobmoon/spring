package com.zgb.student.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.zgb.student.R;
import com.zgb.student.model.HealthInfo;
import com.zgb.student.tools.StudentAdapter;
import com.zgb.student.tools.DatabaseHelper;
import com.zgb.student.model.Student;
import com.zgb.student.util.DateUtils;
import com.zgb.student.util.SearchAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 展示学生信息的activity
 * Created by zgb on 2018.03.31
 */
public class studentInfo_activity extends Activity  implements View.OnClickListener {
    private List<Student> studentList = new ArrayList<Student>();
    private DatabaseHelper dbHelper;
    private ListView listView;
    private StudentAdapter adapter;
    private LinearLayout empty;
    private AutoCompleteTextView search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.studentinfo_activity_layout);
        listView = (ListView) findViewById(R.id.list_view);

        dbHelper = DatabaseHelper.getInstance(this);
        empty = (LinearLayout) findViewById(R.id.emptyStudent);
        empty.setOnClickListener(this);
        search = (AutoCompleteTextView) findViewById(R.id.searchStudent);
        // 支持拼音检索
        SearchAdapter<String> searchAdapter = new SearchAdapter<String>(studentInfo_activity.this,
                android.R.layout.simple_list_item_1, getAllStudent(), SearchAdapter.ALL);
        search.setAdapter(searchAdapter);
        search.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Object obj = parent.getItemAtPosition(position);
                String value=obj.toString();
    //            Toast.makeText(studentInfo_activity.this, value, Toast.LENGTH_SHORT).show();
                queryStudent(value);//从数据库中检索学生信息
                if(studentList.size()>0){
                    adapter = new StudentAdapter(studentInfo_activity.this, R.layout.student_item, studentList);

                    listView.setAdapter(adapter);
                }
            }

        });
        initStudent();//从数据库中检索所有学生信息
        if(studentList.size()>0){
            adapter = new StudentAdapter(studentInfo_activity.this, R.layout.student_item, studentList);
            listView.setAdapter(adapter);
        }





/*        //listView点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final Student student = studentList.get(position);//捕获学生实例
                AlertDialog.Builder builder = new AlertDialog.Builder(studentInfo_activity.this);
                LayoutInflater factory = LayoutInflater.from(studentInfo_activity.this);
                final View textEntryView = factory.inflate(R.layout.stundent_info_layout, null);//加载AlertDialog自定义布局
                builder.setView(textEntryView);
                builder.setTitle("请选择相关操作");

                Button selectInfo = (Button) textEntryView.findViewById(R.id.student_info_select);//查看学生详细信息按钮
                selectInfo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //再次弹出一个alertDialog，用于显示详细学生信息
                        AlertDialog.Builder select_builder = new AlertDialog.Builder(studentInfo_activity.this);
                        select_builder.setTitle("学生详细信息");
                        StringBuilder sb = new StringBuilder();
                        sb.append("姓名：" + student.getName() + "\n");
                        sb.append("学号：" + student.getId() + "\n");
                        sb.append("性别：" + student.getSex() + "\n");
                        sb.append("测量日期：" + student.getMeasureDate() + "\n");
                        sb.append("健康状况：" + student.getInfo() + "\n");
//                        sb.append("手机号：" + student.getNumber() + "\n");
//                        int math = student.getMathScore();//数学成绩
//                        sb.append("数学成绩：" + math + "\n");
//                        int chinese = student.getChineseScore();
//                        sb.append("语文成绩：" + chinese + "\n");
//                        int english = student.getEnglishScore();
//                        sb.append("英语成绩：" + english + "\n");
//                        int sum = math + chinese + english;//总成绩
//                        sb.append("总成绩：" + sum + "\n");
//                        sb.append("排名："+student.getOrder()+"\n");
                        select_builder.setMessage(sb.toString());
                        select_builder.create().show();

                    }
                });


                //删除学生信息
                Button delete_info = (Button) textEntryView.findViewById(R.id.student_info_delete);
                delete_info.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder delete_builder = new AlertDialog.Builder(studentInfo_activity.this);
                        delete_builder.setTitle("警告！！！！");
                        delete_builder.setMessage("您将删除该学生信息，此操作不可逆，请谨慎操作！");

                        delete_builder.setNegativeButton("取消", null);
                        delete_builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SQLiteDatabase db = dbHelper.getWritableDatabase();
                                db.execSQL("delete from student where id=?", new String[]{student.getId()});
                                studentList.remove(position);//移除
                                adapter.notifyDataSetChanged();//刷新列表

                            }
                        });
                        delete_builder.create().show();

                    }
                });

                //修改学生信息,通过intent传递旧学生信息
                Button update_info = (Button) textEntryView.findViewById(R.id.student_info_update);
                update_info.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //跳转到添加学生信息的界面,通过intent传递数据
                        Intent intent = new Intent(studentInfo_activity.this, addStudent_info_activity.class);
                        intent.putExtra("haveData", "true");
                        intent.putExtra("name", student.getName());
                        intent.putExtra("sex", student.getSex());
                        intent.putExtra("id", student.getId());
                        intent.putExtra("info", student.getInfo());
                        intent.putExtra("measureDate", student.getMeasureDate());
//                        intent.putExtra("number", student.getNumber());
                        intent.putExtra("password", student.getPassword());
//                        intent.putExtra("mathScore", student.getMathScore());
//                        intent.putExtra("chineseScore", student.getChineseScore());
//                        intent.putExtra("englishScore", student.getEnglishScore());
                        startActivity(intent);
                    }
                });

                builder.create().show();
            }
        });*/

    }
    //初始化学生信息
    private void queryStudent(String name) {
        studentList.clear();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select s.id,s.name,s.sex,h.measuredate,h.info from (select id,name,sex from student where name=?) as s LEFT JOIN health as h ON s.id=h.id order by s.id,h.measuredate desc", new String[]{name});
        while (cursor.moveToNext()) {
            String id = cursor.getString(0);
    //        String name = cursor.getString(1);
//            String password = cursor.getString(cursor.getColumnIndex("password"));
            String sex = cursor.getString(2);
            String measureDate = cursor.getString(3);
            String info = cursor.getString(4);

            studentList.add(new Student(name,sex,id,null,info,measureDate));
        }
        cursor.close();
    }
    //初始化学生信息
    private void initStudent() {
        studentList.clear();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select s.id,s.name,s.sex,h.measuredate,h.info from student as s LEFT JOIN health as h ON s.id=h.id order by s.id,h.measuredate desc", null);
        while (cursor.moveToNext()) {
            String id = cursor.getString(0);
            String name = cursor.getString(1);
//            String password = cursor.getString(cursor.getColumnIndex("password"));
            String sex = cursor.getString(2);
            String measureDate = cursor.getString(3);
            String info = cursor.getString(4);

            studentList.add(new Student(name,sex,id,null,info,measureDate));
        }
        cursor.close();
    }
    @Override
    /**
     * 关闭搜索条件时，显示全部数据
     */
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.emptyStudent:
                search.setText("");
                search.requestFocus();
                initStudent();//从数据库中检索所有学生信息
                if(studentList.size()>0){
                    adapter = new StudentAdapter(studentInfo_activity.this, R.layout.student_item, studentList);
                    listView.setAdapter(adapter);
                }
                break;
        }
    }
    //提取所有学生信息
    private List<String> getAllStudent() {
        List<String> studentList = new ArrayList<String>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select name from student s  order by s.id", null);
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex("name"));
//            String password = cursor.getString(cursor.getColumnIndex("password"));
//            String sex = cursor.getString(cursor.getColumnIndex("sex"));
            studentList.add(name);
        }
        cursor.close();
        return studentList;

    }
}
