package com.example.mynote;

import android.provider.BaseColumns;

public final class FeedReaderContract {
    private FeedReaderContract(){}
    public static class Notepad implements BaseColumns {
        public static final String  TABLE_NAME="notes";
        public static final String  COLUMN_NAME_ID="_id";
        public static final String COLUMN_NAME_TITLE="title";
        public static final String COLUMN_NAME_DETAIL="detail";
        public static final String COLUMN_NAME_DATE="lastdate";
        public static final String COMMA_SEP=",";
        public  static final String SQL_CREATETABLE_NOTES=
                "CREATE TABLE "+TABLE_NAME+"("+COLUMN_NAME_ID+" INTEGER PRIMARY KEY,"
                +COLUMN_NAME_TITLE+" TEXT"+ COMMA_SEP
                +COLUMN_NAME_DETAIL + " TEXT"+ COMMA_SEP+COLUMN_NAME_DATE+" TEXT"+")";
      //  +COLUMN_NAME_DETAIL +")";
        public  static final String SQL_DELETETABLE_NOTES=
                "DROP TABLE IF EXISTS"+TABLE_NAME;


    }
}
