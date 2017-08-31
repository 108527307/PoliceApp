package com.example.administrator.policeapp.sqlite;

/**
 * Created by Administrator on 2016/8/29.
 */
public class NewsInfo {
    public static final String DB_NAME="news_db";
    public static final String TABLE_READ="read";
    public static final int version=3;
    public static final String COLUMN_ID="newid";
    public static final String CREATE_TABLE_READ="create table "+TABLE_READ+"("+COLUMN_ID+"text)";
}
