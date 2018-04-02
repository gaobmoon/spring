package com.zgb.student.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ListView;
import com.zgb.student.R;
import com.zgb.student.model.Student;
import com.zgb.student.tools.DatabaseHelper;
import com.zgb.student.util.DateUtils;
import com.zgb.student.util.SearchAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;


/**
 * 增加学生健康信息
 */
public class addStudent_health_activity extends Activity  implements View.OnClickListener {

    private EditText name;
    private EditText sex;
    private EditText idText;
    private EditText measureDate;
    private EditText password;
    private EditText info;
    private EditText mEditText;
    private ListView mListView;


    private LinearLayout empty;
    private AutoCompleteTextView search;

    Cursor cursor;

    private String oldID;//用于防治修改信息时将ID也修改了，而原始的有该ID的学生信息还保存在数据库中


    private Button sure;//确定按钮
    private DatabaseHelper dbHelper;
    Intent oldData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.add_student_health);

        empty = (LinearLayout) findViewById(R.id.empty);
        empty.setOnClickListener(this);
        search = (AutoCompleteTextView) findViewById(R.id.search);

        name = (EditText) findViewById(R.id.add_student_health_name);
        sex = (EditText) findViewById(R.id.add_student_health_sex);
        idText = (EditText) findViewById(R.id.add_student_health_id);
        measureDate= (EditText) findViewById(R.id.add_student_health_date);
        info=(EditText) findViewById(R.id.add_student_health_info);
        dbHelper = DatabaseHelper.getInstance(this);
        List<String> studentList =query();
        // 自动提示适配器
        //      ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, str);
        // 支持拼音检索
        SearchAdapter<String> adapter = new SearchAdapter<String>(addStudent_health_activity.this,
                android.R.layout.simple_list_item_1, studentList, SearchAdapter.ALL);
        search.setAdapter(adapter);
        search.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Object obj = parent.getItemAtPosition(position);
                String value=obj.toString();
                Toast.makeText(addStudent_health_activity.this, value, Toast.LENGTH_SHORT).show();
                Student student =query(value);
                name.setText(student.getName());
                idText.setText(student.getId());
                sex.setText(student.getSex());
                name.setFocusable(false);
                name.setFocusableInTouchMode(false);
                idText.setFocusable(false);
                idText.setFocusableInTouchMode(false);
                sex.setFocusable(false);
                sex.setFocusableInTouchMode(false);
                measureDate.setText(DateUtils.getSystemTime());
            }

        });


    //    number = (EditText) findViewById(R.idText.add_student_layout_number);
   //     password = (EditText) findViewById(R.idText.add_student_layout_password);
  //      math = (EditText) findViewById(R.idText.add_student_layout_math);
    //    chinese = (EditText) findViewById(R.idText.add_student_layout_chinese);
    //    english = (EditText) findViewById(R.idText.add_student_layout_english);


        oldData = getIntent();
        if (oldData.getStringExtra("haveData").equals("true")) {
            initInfo();//恢复旧数据
        }


        sure = (Button) findViewById(R.id.add_student_health_sure);
        //将数据插入数据库
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //我这里要求id,measureDate,info都不能为空
                String id_ = idText.getText().toString();
                String name_ = name.getText().toString();
//                String sex_ = sex.getText().toString();
                String info_ = info.getText().toString();
                String measureDate_ = measureDate.getText().toString();


                if (!TextUtils.isEmpty(id_) && !TextUtils.isEmpty(info_) && !TextUtils.isEmpty(measureDate_)) {

                    if (DateUtils.isValidDate(measureDate_)) {
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        db.beginTransaction();//开启事务

                        //判断学号是否重复
                        ContentValues values = new ContentValues();
                        values.put("info",info_);
                        int num = db.update("health",values,"id=? and measureDate=?", new String[]{id_,measureDate_,});
                        if (num>0) {
                            Toast.makeText(addStudent_health_activity.this, "更新学生:"+name_+"的记录信息", Toast.LENGTH_SHORT).show();
                        } else {
                            db.execSQL("insert into health (id,measureDate,info) values(?,?,?)", new String[]{id_, measureDate_, info_,});
                            db.setTransactionSuccessful();//事务执行成功
                            db.endTransaction();//结束事务
//                            Intent intent = new Intent(addStudent_health_activity.this, admin_activity.class);
//                            startActivity(intent);
                            Toast.makeText(addStudent_health_activity.this, "成功新增学生:"+name_+"的记录信息", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(addStudent_health_activity.this, "请输入正确的记录日期", Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Toast.makeText(addStudent_health_activity.this, "记录日期，记录信息均不能为空", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    //恢复旧数据
    private void initInfo() {
        String oldName = oldData.getStringExtra("name");
        name.setText(oldName);
        String oldSex = oldData.getStringExtra("sex");
        sex.setText(oldSex);
        String oldId = oldData.getStringExtra("idText");
        oldID = oldId;
        idText.setText(oldId);
//        String oldNumber = oldData.getStringExtra("number");
//        number.setText(oldNumber);
//        String oldPassword = oldData.getStringExtra("password");
//        password.setText(oldPassword);
//        int mathScore = oldData.getIntExtra("mathScore", 0);
//        math.setText(String.valueOf(mathScore));
//        int chineseScore = oldData.getIntExtra("chineseScore", 0);
//        chinese.setText(String.valueOf(chineseScore));
//        int englishScore = oldData.getIntExtra("englishScore", 0);
//        english.setText(String.valueOf(englishScore));
    }



    //初始化学生信息
    private Student query(String name) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] selectioinArgs = new String[]{name};
        Cursor cursor = db.rawQuery("select id,sex from student s where name = ? ", selectioinArgs);
        Student student=null;
        cursor.moveToNext();
        String id = cursor.getString(0);
        String sex = cursor.getString(1);
        student = new Student(name,sex,id);
        cursor.close();
        return student;

    }
    //初始化学生信息
    private List<String> query() {
        List<String> studentList = new ArrayList<String>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("select * from student s  order by s.id", null);
        Map<String, Object> map;
        while (cursor.moveToNext()) {

            String name = cursor.getString(cursor.getColumnIndex("name"));
//            String password = cursor.getString(cursor.getColumnIndex("password"));
            String sex = cursor.getString(cursor.getColumnIndex("sex"));

            studentList.add(name);
        }
        cursor.close();
        return studentList;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.empty:
                search.setText("");
                name.setText("");
                idText.setText("");
                sex.setText("");
                measureDate.setText("");
                info.setText("");
                search.requestFocus();
                break;
        }
    }

}
