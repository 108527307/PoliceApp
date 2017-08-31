package com.example.administrator.policeapp.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.administrator.policeapp.base.RxBaseApplication;

/**
 * 网络工具类
 */
public class NetWorkUtil
{

    private NetWorkUtil()
    {

    }
/*
* 判断移动网络通畅
* */
    public static boolean isNetworkConnected()
    {


        if (RxBaseApplication.getContext() != null)
        {
           ConnectivityManager mConnectivityManager = (ConnectivityManager) RxBaseApplication.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null)
            {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }
//判断wifi
    public static boolean isWifiConnected()
    {

        if (RxBaseApplication.getContext() != null)
        {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) RxBaseApplication.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWiFiNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mWiFiNetworkInfo != null)
            {
                return mWiFiNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    public static boolean isMobileConnected()
    {

        if (RxBaseApplication.getContext() != null)
        {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) RxBaseApplication.getContext()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mMobileNetworkInfo = mConnectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (mMobileNetworkInfo != null)
            {
                return mMobileNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    public static int getConnectedType()
    {

        if (RxBaseApplication.getContext() != null)
        {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) RxBaseApplication.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null && mNetworkInfo.isAvailable())
            {
                return mNetworkInfo.getType();
            }
        }
        return -1;
    }
}
