package com.example.mynote;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

import static com.example.mynote.FeedReaderContract.Notepad.COLUMN_NAME_DATE;
import static com.example.mynote.FeedReaderContract.Notepad.COLUMN_NAME_DETAIL;
import static com.example.mynote.FeedReaderContract.Notepad.COLUMN_NAME_ID;
import static com.example.mynote.FeedReaderContract.Notepad.COLUMN_NAME_TITLE;
import static com.example.mynote.FeedReaderContract.Notepad.TABLE_NAME;

public class SearchActivity extends AppCompatActivity {
    private SimpleCursorAdapter apt;
    private ListView listView;
    private Cursor cursor;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(initData.getBgstyle());
        setContentView(R.layout.activity_search);
    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    //输入搜索的列和搜索内容,执行sql语句模糊搜索
    public Cursor Search(String Type, String s){
        DBHelper dbh=new DBHelper(getApplicationContext());
        SQLiteDatabase db=dbh.getWritableDatabase();

        Cursor cursor=db.query(TABLE_NAME,null,Type+"  LIKE ? ",new String[] { "%" + s + "%" },null,null,null);
        return cursor;
    }


    //搜索按钮点击事件
    public void  SearchByString(View view){
        EditText title =(EditText) findViewById (R.id.Search_E1);
        Spinner spinner=findViewById(R.id.spinner1);
        String t=title.getText().toString();
        String s=(String)spinner.getSelectedItem();
        String type="_id";//默认按id搜索


        switch (s){
            case "按id查询":
                type="_id";
                break;
            case "按标题查询":
                type="title";
                break;
            case "内容查询":
                type="detail";
                break;
            default:
                break;
        }

        cursor=Search(type,t);//获取搜索结果的游标

        //创建适配器放入listview
        String[] from={COLUMN_NAME_ID,COLUMN_NAME_TITLE,COLUMN_NAME_DETAIL,COLUMN_NAME_DATE};
        int[] to={R.id.List_id,R.id.List_title,R.id.List_detail,R.id.List_date};
        apt=new SimpleCursorAdapter(this,R.layout.listview_note,cursor,from,to, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        listView=findViewById(R.id.lv2);
        listView.setAdapter(apt);
    }
}
