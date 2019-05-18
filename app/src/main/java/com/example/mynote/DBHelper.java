package com.example.mynote;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.mynote.FeedReaderContract.Notepad.SQL_CREATETABLE_NOTES;
import static com.example.mynote.FeedReaderContract.Notepad.SQL_DELETETABLE_NOTES;


public class DBHelper extends SQLiteOpenHelper {
    public static final int VERSION=1;
    public static final String DATABASE_NAME="MyNoteDatabase";
    public DBHelper(Context context){
        super(context,DATABASE_NAME,null,VERSION);
    }
    public void onCreate(SQLiteDatabase db){
        db.execSQL(SQL_CREATETABLE_NOTES);
    }
    public void onUpgrade(SQLiteDatabase db,int oldVersion, int newVersion){
        db.execSQL(SQL_DELETETABLE_NOTES);
        onCreate(db);
    }

}
