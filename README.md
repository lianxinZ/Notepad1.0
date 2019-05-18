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

### 实现备忘录的插入功能，实现时间戳
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
Mainactivity
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
updataactivity
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
### 长按选项多选删除功能
Mainactivity 

设置多选监听事件，获取所有被选中的id, 选中的项目颜色变成灰色，取消选中则恢复
~~~
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
            
 ~~~
        
长按显示菜单栏

~~~
            //长按菜单栏
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {


                MenuInflater inflater=mode.getMenuInflater();
                inflater.inflate(R.menu.action_menu,menu);
                return true;
            }
            
~~~
          
菜单栏点击事件     点击删除调用删除函数，点击取消则退出

~~~
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
  ~~~
  
  删除函数：根据获取的选中id集合来执行删除语句
  
  ~~~
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
  ~~~
  
  ### 搜索功能
  
SearchActivity
根据输入的字段和下拉框的选择来调用搜索函数执行搜索，把搜索的结果放入listview显示

  ~~~
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
  ~~~
  
  搜索函数
  
  ~~~
      //输入搜索的列和搜索内容,执行sql语句模糊搜索
    public Cursor Search(String Type, String s){
        DBHelper dbh=new DBHelper(getApplicationContext());
        SQLiteDatabase db=dbh.getWritableDatabase();

        Cursor cursor=db.query(TABLE_NAME,null,Type+"  LIKE ? ",new String[] { "%" + s + "%" },null,null,null);
        return cursor;
    }
  
  ~~~
### 使用preferencefragment实现偏好设置
1.调用addPreferencesFromResource方法绑定设置布局文件
~~~
public class Setting  extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }

~~~
2.preferenceActivity
~~~
//加载设置界面
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new Setting())
                .commit();
~~~

3.在主界面读取配置信息，根据读取数据设置initData类的Bgstyle
~~~

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

    }
~~~
4.在每个activity 开始时，加载theme
~~~
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //调用initesetting（）获取xml文件数据初始化布局样式
        initeSetting();
        setTheme(initData.getBgstyle());

        setContentView(R.layout.activity_main);
    }
~~~

styles.xml

~~~
    <!--红色主题-->
    <style name="whiteTheme" parent="AppTheme">
        <item name="colorPrimary">@color/gray</item>
        <item name="colorAccent">@color/gray</item>

        <item name="android:windowBackground">@drawable/white</item>
    </style>


    <!--蓝色主题-->
    <style name="blueTheme" parent="AppTheme">
        <item name="colorPrimary">@color/blue</item>
        <item name="colorAccent">@color/blue</item>
        <item name="android:windowBackground">@drawable/blue</item>
    </style>

    <!--粉色主题-->
    <style name="redTheme" parent="AppTheme">

        <item name="colorPrimary">@color/pink</item>
        <item name="colorAccent">@color/pink</item>
        <item name="toolbarNavigationButtonStyle">@color/pink</item>
        <item name="android:windowBackground">@drawable/pink</item>
    </style>
~~~


