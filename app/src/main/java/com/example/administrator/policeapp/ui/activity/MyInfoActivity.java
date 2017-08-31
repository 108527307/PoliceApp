package com.example.administrator.policeapp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.policeapp.R;
import com.example.administrator.policeapp.base.RxBaseActivity;
import com.example.administrator.policeapp.cache.Cache;
import com.example.administrator.policeapp.widget.CircleImageView;

import java.util.List;

import butterknife.Bind;

/**
 * Created by Administrator on 2016/9/29.
 */

public class MyInfoActivity extends RxBaseActivity {
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.collasping_toolbar)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @Bind(R.id.user_image)
    CircleImageView mCircleImageView;
    @Bind(R.id.nickname1)
    TextView name;
    @Bind(R.id.province1)
    TextView province;
    @Bind(R.id.userid)
    TextView userid;
    @Bind(R.id.email)
    TextView email;
    @Bind(R.id.signout)
    FloatingActionButton signout;
    @Override
    public int getLayoutId() {
        return R.layout.myinfo_activity;
    }
    @Override
    public void initViews(Bundle savedInstanceState) {
     setInfo();
    }

    private void setInfo() {
        List<String> temp= null;
        try {
            temp = new Cache().GetAll(MyInfoActivity.this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Glide.with(MyInfoActivity.this)
                .load(temp.get(5))
                .asBitmap()
                .placeholder(R.drawable.ic_slide_menu_avatar_no_login)
                .into(mCircleImageView);
        name.setText(temp.get(2));
        userid.setText(temp.get(0));
        email.setText(temp.get(7));
        province.setText(temp.get(3));
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Cache().destroyCache(MyInfoActivity.this);
//               MySqliteHelper mySqliteHelper =new MySqliteHelper(MyInfoActivity.this);
//                mySqliteHelper.deleteAll(MyInfoActivity.this);
//                mySqliteHelper.close();
                Intent intent=new Intent(MyInfoActivity.this,LoginActivity.class);
                startActivity(intent);
                Intent intent1 = new Intent();
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent1.setAction("com.scott.sayhi");
                sendBroadcast(intent1);
                finish();

            }
        });
    }

    @Override
    public void initToolBar() {
        setSupportActionBar(mToolbar);
        ActionBar supportActionBar=getSupportActionBar();
        if(supportActionBar!=null){
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            mCollapsingToolbarLayout.setTitle("我的信息");
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_user_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home: onBackPressed();
                break;
            case R.id.action_edit:
                Intent intent=new Intent(MyInfoActivity.this,FogotPasActivity.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
