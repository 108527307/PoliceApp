package com.example.administrator.policeapp.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySqliteHelper extends SQLiteOpenHelper {

	private final static int VERSION = 1;
	
	public MySqliteHelper(Context context) {
		super(context, "RailwaySystem.db", null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(AnswerColumns.getCreateTableSql());
		db.execSQL(CollectColumns.getCreateTableSql());
		db.execSQL(ErrorColumns.getCreateTableSql());
		db.execSQL(ExamResultColumns.getCreateTableSql());
		db.execSQL(ExamErrorColumns.getCreateTableSql());
		db.execSQL(HistoryResultColumns.getCreateTableSql());
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}
  public void deleteAll(Context context){
	  context.deleteDatabase("RailwaySystem.db");

  }
}
