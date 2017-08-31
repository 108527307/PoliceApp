package com.example.administrator.policeapp.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2016/8/29.
 */
public class DbOpenHelper extends SQLiteOpenHelper {
    private static DbOpenHelper dbOpenHelper;
    public DbOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
 public static DbOpenHelper getInstance(Context context){
     if(dbOpenHelper==null){
      synchronized (DbOpenHelper.class) {
          if (dbOpenHelper == null) {
           dbOpenHelper=new DbOpenHelper(context,NewsInfo.DB_NAME,null,NewsInfo.version);
          }
          }
      }
     return dbOpenHelper;
     }

    @Override
    public void onCreate(SQLiteDatabase db) {
           db.execSQL(NewsInfo.CREATE_TABLE_READ);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
