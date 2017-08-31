package com.example.administrator.policeapp.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by Administrator on 2017/3/11.
 */

public class HeadPortrait extends BmobFile {
    @Override
    public String getFilename() {
        return filename;
    }

    @Override
    public void setFilename(String filename) {
        this.filename = filename;
    }

    private String filename;
    public HeadPortrait(String fileName,String url){
        this.filename = fileName+getTime()+".png";
        this.url = url;
    }
    private String getTime(){
        SimpleDateFormat formatter=new SimpleDateFormat("yyyyMMddHHmm:ss");
        Date curDate   =   new    Date(System.currentTimeMillis());//获取当前时间
       return formatter.format(curDate);
    }
}
