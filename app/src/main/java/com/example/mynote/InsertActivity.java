package com.example.mynote;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.mynote.FeedReaderContract.Notepad.*;

//插入activity

public class InsertActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(initData.getBgstyle());
        setContentView(R.layout.activity_insert);


    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


    //插入按钮点击事件
    public void Insert(View view){
        EditText TITLE =(EditText) findViewById (R.id.insert_title);
        EditText DETAIL =(EditText) findViewById (R.id.insert_detail);

        String title=TITLE.getText().toString();
        String detail=DETAIL.getText().toString();

        //获取系统时间
        SimpleDateFormat formatter   =   new   SimpleDateFormat   ("yyyy.MM.dd.HH:mm:ss");//获取时间
        Date curDate =  new Date(System.currentTimeMillis());
        String date=formatter.format(curDate);


        //打包数据
        ContentValues values=new ContentValues();
        values.put(COLUMN_NAME_TITLE,title);
        values.put(COLUMN_NAME_DETAIL,detail);
        values.put(COLUMN_NAME_DATE,date);


        DBHelper dbh=new DBHelper(getApplicationContext());
        SQLiteDatabase db=dbh.getWritableDatabase();

        //把数据放入数据库
        db.insert(TABLE_NAME,null,values);

        //页面跳转
        Intent intent= new Intent(InsertActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }


}
