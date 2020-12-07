package com.example.weather;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "database";    //数据库名
    public static final int version_code=1;
    public static final String DB_NAME = "weatherinfo";              //表名
    public static final String DB_NAME1 = "weatherinfo1";              //表名
    private static final String TAG ="DatabaseHelper" ;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null,version_code);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        //只创建一次
        Log.d(TAG,"create！");
        //创建
        String sql="create table "+DB_NAME+"(_id integer PRIMARY KEY, province_ text,city_ text,adcode_ text,weather_ text,temperature_ text,winddirection_ text,windpower_ text,humidity_ text,reporttime_ text)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG,"UPDATE！");

    }

}
