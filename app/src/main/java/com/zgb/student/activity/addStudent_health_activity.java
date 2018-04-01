package com.zgb.student.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.ListView;
import com.zgb.student.R;
import com.zgb.student.model.Student;
import com.zgb.student.tools.DatabaseHelper;
import com.zgb.student.tools.QueryAdapter;
import com.zgb.student.tools.StudentAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 增加学生健康信息
 */
public class addStudent_health_activity extends Activity {

    private EditText name;
    private EditText sex;
    private EditText id;
    private EditText measureDate;
    private EditText password;
    private EditText info;
    private EditText mEditText;
    private ListView mListView;

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

        dbHelper = DatabaseHelper.getInstance(this);

    //    initView();
        name = (EditText) findViewById(R.id.add_student_health_name);
        sex = (EditText) findViewById(R.id.add_student_health_sex);
        id = (EditText) findViewById(R.id.add_student_health_id);
    //    number = (EditText) findViewById(R.id.add_student_layout_number);
   //     password = (EditText) findViewById(R.id.add_student_layout_password);
  //      math = (EditText) findViewById(R.id.add_student_layout_math);
    //    chinese = (EditText) findViewById(R.id.add_student_layout_chinese);
    //    english = (EditText) findViewById(R.id.add_student_layout_english);


        oldData = getIntent();
        if (oldData.getStringExtra("haveData").equals("true")) {
            initInfo();//恢复旧数据
        }


        sure = (Button) findViewById(R.id.add_student_health_sure);
        //将数据插入数据库
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sex不能为空否则程序崩溃，因为在StudentAdapter中有一个ImageView要设置图片
                //我这里要求id,name,sex都不能为空
                String id_ = id.getText().toString();
                String name_ = name.getText().toString();
                String sex_ = sex.getText().toString();
//                String password_ = password.getText().toString();
//                String number_ = number.getText().toString();
//                String mathScore = math.getText().toString();
//                String chineseScore = chinese.getText().toString();
//                String englishScore = english.getText().toString();

                if (!TextUtils.isEmpty(id_) && !TextUtils.isEmpty(name_) && !TextUtils.isEmpty(sex_)) {

                    if (sex_.matches("[女|男]")) {
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        db.beginTransaction();//开启事务
                        db.execSQL("delete from student where id=?", new String[]{oldID});//删除旧数据

                        //判断学号是否重复
                        Cursor cursor = db.rawQuery("select * from student where id=?", new String[]{id_});
                        if (cursor.moveToNext()) {
                            Toast.makeText(addStudent_health_activity.this, "已有学生使用该学号,请重新输入", Toast.LENGTH_SHORT).show();
                        } else {
                            db.execSQL("insert into student(id,name,sex) values(?,?,?)", new String[]{id_, name_, sex_,});
                            db.setTransactionSuccessful();//事务执行成功
                            db.endTransaction();//结束事务
                            Intent intent = new Intent(addStudent_health_activity.this, admin_activity.class);
                            startActivity(intent);
                        }
                    } else {
                        Toast.makeText(addStudent_health_activity.this, "请输入正确的性别信息", Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Toast.makeText(addStudent_health_activity.this, "姓名，学号，性别均不能为空", Toast.LENGTH_SHORT).show();
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
        String oldId = oldData.getStringExtra("id");
        oldID = oldId;
        id.setText(oldId);
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

    private void initView() {

//        mEditText = (EditText) findViewById(R.id.edittext);
//        mListView = (ListView) findViewById(R.id.listview);
//
//        findViewById(R.id.listview);


        //EditText添加监听
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}//文本改变之前执行

            @Override
            //文本改变的时候执行
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //显示ListView
                showListView();
            }
            @Override
            public void afterTextChanged(Editable s) { }//文本改变之后执行
        });


    }

    private void showListView() {
        mListView.setVisibility(View.VISIBLE);
        //获得输入的内容
        String name = mEditText.getText().toString().trim();
        //获取数据库对象
        SQLiteDatabase db = dbHelper.getReadableDatabase();


        List<Map<String,Object>> studentList =query(name);


        SimpleAdapter adapter = new SimpleAdapter(addStudent_health_activity.this, studentList,R.layout.add_student_health, new String[] { "id",
                "name" }, new int[] { R.id.add_student_health_id, R.id.add_student_health_name,
                });

        mListView.setAdapter(adapter);

  //      cursor.close();
    }

    //初始化学生信息
    private List<Map<String,Object>> query(String name) {
        List<Map<String,Object>> studentList = new ArrayList<Map<String,Object>>();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String[] selectioinArgs = {"%"+name+"%"};
        Cursor cursor = db.rawQuery("select * from student s where name like ? order by s.id", selectioinArgs);
        Map<String, Object> map;
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex("id"));
//            String name = cursor.getString(cursor.getColumnIndex("name"));
//            String password = cursor.getString(cursor.getColumnIndex("password"));
            String sex = cursor.getString(cursor.getColumnIndex("sex"));
//            String info = cursor.getString(cursor.getColumnIndex("info"));
//            String measureDate = cursor.getString(cursor.getColumnIndex("measureDate"));
//            String number = cursor.getString(cursor.getColumnIndex("number"));
//            int mathScore = cursor.getInt(cursor.getColumnIndex("mathScore"));
//            int chineseScore = cursor.getInt(cursor.getColumnIndex("chineseScore"));
//            int englishScore = cursor.getInt(cursor.getColumnIndex("englishScore"));
//            int order=cursor.getInt(cursor.getColumnIndex("ranking"));
            map=new HashMap<String, Object>();
            map.put(id,name);
            studentList.add(map);
        }
        cursor.close();
        return studentList;

    }
}
