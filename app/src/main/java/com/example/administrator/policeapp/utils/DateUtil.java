package com.example.administrator.policeapp.utils;

import java.text.SimpleDateFormat;

public class DateUtil
{

    private DateUtil()
    {

    }

    public static String formatDate(String date)
    {

        String dateFormat = null;
        try
        {
            dateFormat = date.substring(5, 7) + "月" + date.substring(8, 10) + "日";
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return dateFormat;
    }

    public static String getTime(long date)
    {
        SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm:ss");
        Long time = new Long(date);
        String d = format.format(time);

        return d;
    }
}
