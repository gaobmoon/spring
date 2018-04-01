package com.zgb.student.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.zgb.student.R;

/**
 * 管理员的管理界面
 * Created by zgb on 2018.03.31
 */
public class admin_activity extends Activity {

    private Button select;//查询学生信息按钮
    private Button add;//添加学生信息按钮
    private Button addHealth;//增加学生健康信息
//    private TextView forceOffline;//强制下线

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.admin_layout);

        select = (Button) findViewById(R.id.admin_activity_select);
        add = (Button) findViewById(R.id.admin_activity_add);
        addHealth = (Button) findViewById(R.id.admin_activity_health);
  //      forceOffline = (TextView) findViewById(R.id.admin_activity_forceOffline);

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(admin_activity.this, studentInfo_activity.class);
                startActivity(intent);

            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(admin_activity.this, addStudent_info_activity.class);
                intent.putExtra("haveData","false");
                startActivity(intent);
            }
        });


        addHealth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(admin_activity.this,addStudent_health_activity.class);
                intent.putExtra("haveData","false");
                startActivity(intent);
            }
        });


//        forceOffline.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setAction("com.zgb.com.OfflineBradcast");
//                sendBroadcast(intent);
//            }
//        });


    }
}
