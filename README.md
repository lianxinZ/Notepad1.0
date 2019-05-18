# Notepad1.0
## 一.实现功能：

### 基础功能：
1.备忘录基本功能，备忘录的添加和删除。

2.备忘录的时间戳功能

3.备忘录的查询功能，实现选择字段查询。

### 拓展功能
3.备忘录重新编辑功能。

4.备忘录更换主题功能。

5.界面美化。

## 二 主要目录说明： 注：因为太晚开始编写这个项目，没有想好就开始写了，所以写的有点混乱。敬请谅解。
### FeedReaderContract
定义架构和契约： 定义表的创建和删除语句。定义表的字段。
### DBHelper
数据库表的创建和删除。
### MainActivity 
主界面：使用listview 显示 备忘录内容 和其他界面的入口
### InsertActivity
插入界面：实现添加备忘录
### SearchActivity
搜索界面：实现搜索功能
### updateActivity
编辑界面：实现备忘录的重新编辑
### preferenceActivity
设置界面：使用preferenceFragment实现偏好设置
### Setting
设置类，加载preferences.xml配置文件
### initData
数据类，用来存放偏好设置的数据

## 三. 实现功能的详细说明

### 数据库的创建
  
FeedReaderContract.java
~~~
public final class FeedReaderContract {
    private FeedReaderContract(){}
    public static class Notepad implements BaseColumns {

        //notes表的字段
        public static final String  TABLE_NAME="notes";
        public static final String  COLUMN_NAME_ID="_id";
        public static final String COLUMN_NAME_TITLE="title";
        public static final String COLUMN_NAME_DETAIL="detail";
        public static final String COLUMN_NAME_DATE="lastdate";
        public static final String COMMA_SEP=",";

        //创建notes表的sql语句
        public  static final String SQL_CREATETABLE_NOTES=
                "CREATE TABLE "+TABLE_NAME+"("+COLUMN_NAME_ID+" INTEGER PRIMARY KEY,"
                +COLUMN_NAME_TITLE+" TEXT"+ COMMA_SEP
                +COLUMN_NAME_DETAIL + " TEXT"+ COMMA_SEP+COLUMN_NAME_DATE+" TEXT"+")";

        //删除表的sql语句
        public  static final String SQL_DELETETABLE_NOTES=
                "DROP TABLE IF EXISTS"+TABLE_NAME;


    }
}


~~~

DBHelper.java
~~~
public class DBHelper extends SQLiteOpenHelper {
    public static final int VERSION=1;
    public static final String DATABASE_NAME="MyNoteDatabase";
    public DBHelper(Context context){
        super(context,DATABASE_NAME,null,VERSION);
    }
    public void onCreate(SQLiteDatabase db){
        db.execSQL(SQL_CREATETABLE_NOTES);
        //执行创建表的sql语句
    }
    public void onUpgrade(SQLiteDatabase db,int oldVersion, int newVersion){
        db.execSQL(SQL_DELETETABLE_NOTES);
        onCreate(db);
    }

}
~~~

### 实现备忘录的插入功能
InsertActivity
~~~
    public void Insert(View view){
        //从EditText获取数据
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
~~~

### 查看数据
Mainactivity
1.先查询数据库所有的数据返回游标
~~~
 //查询notes表所有数据，返回游标
    public Cursor query(){
        DBHelper dbh=new DBHelper(getApplicationContext());
        SQLiteDatabase db=dbh.getWritableDatabase();

        Cursor cursor=db.query(TABLE_NAME,null,null,null,null,null,null);
        cursor.moveToFirst();//游标移动到第一位
        return cursor;
    }
~~~
2.创建适配器，使用listview显示备忘录数据
~~~
        //创建Cursor适配器
        
        final Cursor cursor=query();
        String[] from={COLUMN_NAME_ID,COLUMN_NAME_TITLE,COLUMN_NAME_DETAIL,COLUMN_NAME_DATE};
        int[] to={R.id.List_id,R.id.List_title,R.id.List_detail,R.id.List_date};
        apt=new SimpleCursorAdapter(this,R.layout.listview_note,cursor,from,to, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

        //创建listView
        listView=findViewById(R.id.lv1);
        listView.setAdapter(apt);

~~~
### 备忘录点击重新编辑功能
1.设置listview监听事件，获取当前item数据，带数据跳转到编辑界面，
~~~
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

~~~
2.获取点击的item的数据填充到编辑页面的Edtext里，在重新编辑之后更新数据库。
~~~

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
~~~
###
