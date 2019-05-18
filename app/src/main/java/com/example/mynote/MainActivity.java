package com.example.mynote;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.example.mynote.FeedReaderContract.Notepad.COLUMN_NAME_DATE;
import static com.example.mynote.FeedReaderContract.Notepad.COLUMN_NAME_DETAIL;
import static com.example.mynote.FeedReaderContract.Notepad.COLUMN_NAME_ID;
import static com.example.mynote.FeedReaderContract.Notepad.COLUMN_NAME_TITLE;
import static com.example.mynote.FeedReaderContract.Notepad.TABLE_NAME;

public class MainActivity extends AppCompatActivity{
    private SimpleCursorAdapter apt;
    private  ListView listView;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //调用initesetting（）获取xml文件数据初始化布局样式
        initeSetting();
        setTheme(initData.getBgstyle());

        setContentView(R.layout.activity_main);



    }

    @Override
    protected void onResume() {
        super.onResume();
        //创建Cursor适配器
        
        final Cursor cursor=query();
        String[] from={COLUMN_NAME_ID,COLUMN_NAME_TITLE,COLUMN_NAME_DETAIL,COLUMN_NAME_DATE};
        int[] to={R.id.List_id,R.id.List_title,R.id.List_detail,R.id.List_date};
        apt=new SimpleCursorAdapter(this,R.layout.listview_note,cursor,from,to, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

        //创建listView
        listView=findViewById(R.id.lv1);
        listView.setAdapter(apt);


        //listviewItem点击事件，进入编辑页面
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor c=(Cursor)listView.getItemAtPosition(position);
                String title=c.getString(c.getColumnIndex(COLUMN_NAME_TITLE));
                String detail=c.getString(c.getColumnIndex(COLUMN_NAME_DETAIL));
                int _id=c.getInt(c.getColumnIndex(COLUMN_NAME_ID));
                String lastdate=c.getString(c.getColumnIndex(COLUMN_NAME_DATE));
                Intent intent=new Intent();
                intent.putExtra(COLUMN_NAME_TITLE,title);
                intent.putExtra(COLUMN_NAME_DETAIL,detail);
                intent.putExtra(COLUMN_NAME_ID,_id);
                intent.putExtra(COLUMN_NAME_DATE,lastdate);
                intent.setClass(MainActivity.this,updateActivity.class);
                startActivity(intent);
            }
        });
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);


        //长按点击事件，长按多选删除
        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            private ListView mListView;
            private int selectnum=0;//选中项目数量

            private List<Integer> mSelectedItems = new ArrayList<>();//选中项目position

            private List<String> ids=new ArrayList<>();//选中项目id

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

                if(checked) {
                    selectnum++;
                    mSelectedItems.add((Integer) position);
                    //根据游标获取选中item 的id;
                    Cursor c1=(Cursor)listView.getItemAtPosition(position);

                    String _id=String.valueOf(c1.getInt(c1.getColumnIndex(COLUMN_NAME_ID)));
                    ids.add(_id);
                    listView.getChildAt(position).setBackgroundColor(Color.GRAY);//选中时背景色设为灰色
                }
                else {
                    selectnum--;
                    mSelectedItems.remove((Integer) position);

                    //根据游标获取选中item 的id;
                    Cursor c2=(Cursor)listView.getItemAtPosition(position);
                    String _id=String.valueOf(c2.getInt(c2.getColumnIndex(COLUMN_NAME_ID)));
                    ids.remove(_id);

                    listView.getChildAt(position).setBackgroundColor(Color.TRANSPARENT);//取消选中

                }
                mode.setTitle(selectnum + " selected");
                apt.notifyDataSetChanged();
            }
            @Override
            //长按菜单栏
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {


                MenuInflater inflater=mode.getMenuInflater();
                inflater.inflate(R.menu.action_menu,menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            //长按菜单栏点击事件
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()){
                    case R.id.item_cancel:break;
                    case R.id.item_delete:
                        deleteByID(ids,cursor);;

                        break;
                    default:
                        break;
                }
                mode.finish();
                return true;
            }

            //取消选中
            @Override
            public void onDestroyActionMode(ActionMode mode) {

                for(int i: mSelectedItems){
                    listView.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);//取消选中
                }
                mSelectedItems.clear();
                selectnum=0;

            }
        });

    }

    //顶部菜单栏
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_main,menu);
        return true;
    }

//顶部菜单栏点击事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_add:
                ToInsert();
                break;
            case R.id.item_search:
                ToSearch();
                break;
            case R.id.item_settings:
                ToSettings();
                break;
        }
        return true;
    }
    //跳转到插入界面
    public void ToInsert(){
        Intent intent= new Intent(MainActivity.this,InsertActivity.class);
        startActivity(intent);
    }
    //跳转到搜索界面
    public void ToSearch(){
        Intent intent= new Intent(MainActivity.this,SearchActivity.class);
        startActivity(intent);
    }
    //跳转到设置界面
    public void ToSettings(){
        Intent intent= new Intent(MainActivity.this,preferenceActivity.class);
        startActivity(intent);
    }

    //读取配置文件信息
    public void initeSetting(){
        SharedPreferences settings = this.getSharedPreferences("com.example.mynote_preferences", MODE_PRIVATE);
        String bgcolor=settings.getString("pref_bgcolor","初始");

        switch (bgcolor){
            case "初始":
                initData.setBgstyle(R.style.whiteTheme);
                break;
            case "蓝蓝蓝":
                initData.setBgstyle(R.style.blueTheme);
                break;
            case "粉粉粉":
                initData.setBgstyle(R.style.redTheme);
                break;
        }

        // int color=Integer.parseInt(str);
       // getWindow().setBackgroundDrawableResource(R.color.myBarColor);
    }


    //删除笔记
    public void deleteByID(List<String> seleted,Cursor cursor){
        DBHelper dbh=new DBHelper(getApplicationContext());
        SQLiteDatabase db=dbh.getWritableDatabase();
        for(String i: seleted){
            db.delete(TABLE_NAME,"_id=?",new String[]{i});
        }
        cursor.requery();
        apt.notifyDataSetChanged();

    }


    //查询notes表所有数据，返回游标
    public Cursor query(){
        DBHelper dbh=new DBHelper(getApplicationContext());
        SQLiteDatabase db=dbh.getWritableDatabase();

        Cursor cursor=db.query(TABLE_NAME,null,null,null,null,null,null);
        cursor.moveToFirst();//游标移动到第一位
        return cursor;
    }





}
