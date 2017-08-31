package com.example.administrator.policeapp.ui.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.policeapp.R;
import com.example.administrator.policeapp.base.BaseSwipeBackActivity;
import com.example.administrator.policeapp.model.NewsBean;
import com.example.administrator.policeapp.widget.CircleProgressView;

import butterknife.Bind;

/**
 * Created by Administrator on 2016/9/5.
 */
public class DetailActivity extends BaseSwipeBackActivity {
    @Bind(R.id.coll_toolbar_layout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.detail_image)
    ImageView detailImage;
    @Bind(R.id.detail_title)
    TextView title;
    @Bind(R.id.detail_web_view)
    WebView webview;
    @Bind(R.id.circle_progress)
    CircleProgressView circleProgressView;
    private NewsBean newsBean;
    private ActionBar actionBar;
    private MenuItem share;
    private static final String EXTRA_DETAIL="extra_detail";

    @Override
    public int getLayoutId() {
        return R.layout.detaillayout;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
       Intent intent=getIntent();
        if(intent!=null){
             newsBean=intent.getParcelableExtra(EXTRA_DETAIL);
        }
        mSwipeBackLayout.setEdgeDp(120);//设置侧滑返回
        setSupportActionBar(toolbar);
        actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        collapsingToolbarLayout.setTitleEnabled(true);
        actionBar.setTitle("");
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initInfo();
    }

    private void initInfo() {
        Glide.with(DetailActivity.this).load(newsBean.getThumbnail_pic_s()).placeholder(R.drawable.image_default_avatar).into(detailImage);
        title.setText(newsBean.getTitle().toString());
        webview.loadUrl(newsBean.getUrl());
    }

    public static void launch(Context context, NewsBean newsBean){
        Intent intent=new Intent(context,DetailActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(EXTRA_DETAIL,newsBean);
        context.startActivity(intent);
    }

    public void showProgress()
    {

        circleProgressView.setVisibility(View.VISIBLE);
        circleProgressView.spin();
    }

    public void hideProgress()
    {

        circleProgressView.setVisibility(View.GONE);
        circleProgressView.stopSpinning();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {

        getMenuInflater().inflate(R.menu.menu_detail, menu);//实例化菜单
       share=menu.findItem(R.id.menu_action_share);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        switch (item.getItemId())
        {
            case R.id.menu_action_share:
                //分享新闻
                share();
                return true;


            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    private void share()
    {
//分享代码
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TITLE,newsBean.getTitle());
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share));
        intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_from) + newsBean.getTitle() + "," + newsBean.getUrl());
        startActivity(Intent.createChooser(intent, newsBean.getTitle()));
    }
}
