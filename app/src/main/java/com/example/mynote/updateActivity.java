package com.example.mynote;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.mynote.FeedReaderContract.Notepad.COLUMN_NAME_DATE;
import static com.example.mynote.FeedReaderContract.Notepad.COLUMN_NAME_DETAIL;
import static com.example.mynote.FeedReaderContract.Notepad.COLUMN_NAME_TITLE;
import static com.example.mynote.FeedReaderContract.Notepad.TABLE_NAME;

public class updateActivity extends AppCompatActivity {
    @Override

    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setTheme(initData.getBgstyle());
        setContentView(R.layout.activity_update);


    }

    @Override
    protected void onResume() {
        super.onResume();

        //通过intent从listview获取数据
        Intent intent=this.getIntent();
        Bundle bundle=intent.getExtras();
        String title=bundle.getString("title");
        String detail=bundle.getString("detail");
        int  _id=bundle.getInt("_id");
        final String ids=Integer.toString(_id);


        final EditText TITLE =(EditText) findViewById (R.id.update_title);
        final EditText DETAIL =(EditText) findViewById (R.id.update_detail);

        //设置editText内容
        TITLE.setText(title);
        DETAIL.setText(detail);


        //按钮点击事件 修改笔记
        Button update=(Button)findViewById(R.id.update_b1);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //获取当前时间
                SimpleDateFormat formatter   =   new   SimpleDateFormat   ("yyyy.MM.dd.HH:mm:ss");//获取时间
                Date curDate =  new Date(System.currentTimeMillis());
                String lastdate=formatter.format(curDate);

                //数据库连接
                DBHelper dbh=new DBHelper(getApplicationContext());
                SQLiteDatabase db=dbh.getWritableDatabase();


                //获取Edittext内容
                String Atitle=TITLE.getText().toString();
                String Adetail=DETAIL.getText().toString();

                //存储键值对
                ContentValues values=new ContentValues();
                values.put(COLUMN_NAME_TITLE,Atitle);
                values.put(COLUMN_NAME_DETAIL,Adetail);
                values.put(COLUMN_NAME_DATE,lastdate);
                String[] id={String.valueOf(ids)};

                //updates数据库
                db.update(TABLE_NAME,values,"_id=?",id);

                //页面跳转
                Intent intent= new Intent(updateActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
