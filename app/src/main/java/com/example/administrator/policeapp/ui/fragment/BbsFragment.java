package com.example.administrator.policeapp.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.administrator.policeapp.R;
import com.example.administrator.policeapp.base.RxBaseFragment;
import com.example.administrator.policeapp.utils.JsHandler;
import com.example.administrator.policeapp.widget.CircleProgressView;
import com.example.administrator.policeapp.widget.web.CommonWebChromeClient;
import com.example.administrator.policeapp.widget.web.CommonWebView;
import com.example.administrator.policeapp.widget.web.CommonWebViewClient;

import butterknife.Bind;

import static android.view.View.GONE;


public class BbsFragment extends RxBaseFragment
{
    @Bind(R.id.back)
    FloatingActionButton back;
    @Bind(R.id.circle_progress)
    CircleProgressView mCircleProgressView;

    @Bind(R.id.progress_bar)
    ProgressBar mBar;

    @Bind(R.id.web_view)
    CommonWebView mCommonWebView;
    static  Activity mactivity;


    private String url="http://bbs.qzzn.com";
private void initToolbar(){
    //toolbar.setTitle();

    //toolbar.setBackgroundResource(R.drawable.back);
//    android.app.ActionBar actionBar=getActivity().getActionBar();
//    if (actionBar != null)
//    {
//        actionBar.setDisplayHomeAsUpEnabled(true);
//    }
}

    @Override
    public int getLayoutId()
    {

        return R.layout.activity_web;
    }
    public static BbsFragment newInstance(String url,Activity mactivity1,Boolean isZoom)
    {
             mactivity=mactivity1;
        BbsFragment bbsFragment=new BbsFragment();
        Bundle bundle=new Bundle();
        bundle.putString("url",url);
        bundle.putBoolean("isZoom",isZoom);
        bbsFragment.setArguments(bundle);
        return bbsFragment;
    }
//    public static BbsFragment newInstance(Activity activity)
//    {
//
//        return newInstance("http://bbs.qzzn.com",activity,false);
//    }
    @Override
    public void initViews() {

        initToolbar();
        url=  getArguments().getString("url");
        Log.i("url",url);
        Boolean isZoom=getArguments().getBoolean("isZoom");
        if(isZoom)
            back.setVisibility(View.VISIBLE);
        else
        back.setVisibility(GONE);
        initWebSetting();
         back.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 mCommonWebView.goBack();
             }
         });
        hideProgress();
         // mCommonWebView.setWebChromeClient(new WebChromeClient());
        mCommonWebView.getSettings().setBuiltInZoomControls(isZoom);
         mCommonWebView.setWebChromeClient(new CommonWebChromeClient(mBar, mCircleProgressView));
        //mCommonWebView.setWebViewClient(new WebViewClient());
        mCommonWebView.setWebViewClient(new CommonWebViewClient(mCommonWebView));
        mCommonWebView.loadUrl(url);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId)
        {
            case android.R.id.home:
                mCommonWebView.goBack();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private void initWebSetting()
    {

        JsHandler jsHandler = new JsHandler(mactivity, mCommonWebView);
        mCommonWebView.addJavascriptInterface(jsHandler, "JsHandler");
        // mCommonWebView.set
    }



    public void hideProgress()
    {

        mCircleProgressView.setVisibility(GONE);
        mCircleProgressView.stopSpinning();
    }



}