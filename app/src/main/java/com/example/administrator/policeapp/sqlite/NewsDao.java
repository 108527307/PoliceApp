package com.example.administrator.policeapp.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/30.
 * 数据库操作封装类
 */
public class NewsDao {
    private DbOpenHelper dbOpenHelper;
    public NewsDao(Context context){
        dbOpenHelper=DbOpenHelper.getInstance(context);

    }
    public void intsertRead(String id){
        SQLiteDatabase db=dbOpenHelper.getWritableDatabase();//获取可写数据库实例
        ContentValues cv=new ContentValues();//用于存储基本数据类型的机制
        cv.put(NewsInfo.COLUMN_ID,id);
        db.insert(NewsInfo.TABLE_READ,null,cv);//成功返回id，否则返回-1
    }
    public List<String> getAllReadNew(){
        List<String> list=new ArrayList<>();
        SQLiteDatabase db=dbOpenHelper.getReadableDatabase();
        String sqlQuery="select * from "+NewsInfo.TABLE_READ;
        Cursor cursor=db.rawQuery(sqlQuery,null);
        if(cursor.moveToNext()){
            do {
                String id=cursor.getString(0);//获取所有已读的id
                list.add(id);
            }while(cursor.moveToNext());
        }
        return list;
    }
}
