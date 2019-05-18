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



