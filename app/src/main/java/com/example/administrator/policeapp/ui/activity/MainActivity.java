package com.example.administrator.policeapp.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.example.administrator.policeapp.R;
import com.example.administrator.policeapp.base.RxBaseActivity;
import com.example.administrator.policeapp.cache.Cache;
import com.example.administrator.policeapp.model.HeadPortrait;
import com.example.administrator.policeapp.model.User;
import com.example.administrator.policeapp.ui.fragment.BbsFragment;
import com.example.administrator.policeapp.ui.fragment.HomeFragment;
import com.example.administrator.policeapp.ui.fragment.TestFragment;
import com.example.administrator.policeapp.utils.ImageUtils;
import com.example.administrator.policeapp.utils.SnackbarUtil;
import com.example.administrator.policeapp.widget.CircleImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;


public class MainActivity extends RxBaseActivity implements View.OnClickListener
{

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.appBar)
    AppBarLayout appBarLayout;
    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @Bind(R.id.nav_view)
    NavigationView mNavigationView;

    @Bind(R.id.coor_layout)
    CoordinatorLayout mCoordinatorLayout;

    CircleImageView mUserAvatar;

    TextView mUserName;
    ProgressBar progressBar;
    String[] provinces=null;
    String province=null;
    AlertDialog alertDialog;
    @Bind(R.id.bottom_navigation)
    AHBottomNavigation mAhBottomNavigation;//materialtabview
    public static final int ACTION_CHOOSE=11;
    private List<Fragment> fragments = new ArrayList<>();//4个页面

    private int currentTabIndex;//当前页面标识

    private long exitTime;
      private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            Log.d("scott", "on receive action="+intent.getAction());
            String action = intent.getAction();
            if (action.equals("com.scott.sayhi"))
            {
                finish();
            }
        }
    };




    @Override
    public int getLayoutId()
    {

        return R.layout.activity_main;
    }
private void downloadImage(final BmobFile bmobfile){
    File saveFile = new File(Environment.getExternalStorageDirectory()+"/PublicFunctionary/", bmobfile.getFilename());
//    if(saveFile.exists())
//        saveFile.delete();
    final String fileurl=bmobfile.getFileUrl();
    bmobfile.download(saveFile, new DownloadFileListener() {
        @Override
        public void done(String s, BmobException e) {
            if(e==null){
               new Cache().SetPar(MainActivity.this,"headPortraitsSdcard",s);
                setImageToView(s);
                User user=new User();
                user.setHeadPortrait(fileurl);
                try {
                    user.update(new Cache().getPar(MainActivity.this, "ObjectId"), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if(e==null){
                                Toast.makeText(MainActivity.this, "头像同步成功", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }else{
                Toast.makeText(MainActivity.this, "头像同步出错，"+e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onProgress(Integer integer, long l) {

        }
    });
}
    @Override
    public void initViews(Bundle savedInstanceState)
    {
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.scott.sayhi");
        registerReceiver(mBroadcastReceiver, filter);
        provinces=getResources().getStringArray(R.array.proviences);
        alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        Cache cache=new Cache();
        if (mNavigationView != null)
        {
            setupDrawerContent(mNavigationView);
        }
        fragments.add(TestFragment.newInstance());
        fragments.add(BbsFragment.newInstance("http://ah.huatu.com/zt/gkcjpm/",this,true));
        fragments.add(BbsFragment.newInstance("http://bbs.qzzn.com",this,false));
        fragments.add(HomeFragment.newInstance());
        showInitFragment(fragments.get(0));
        initBottomNav();
        try {
            if(cache.getPar(MainActivity.this,"province").equals("nothing")){
                addInformation();
            }
            if(!cache.getPar(MainActivity.this,"headPortraitsSdcard").equals("nothing")){
                if(!setImageToView(cache.getPar(MainActivity.this,"headPortraitsSdcard"))){
                    HeadPortrait headPortrait=new HeadPortrait(cache.getPar(MainActivity.this,"ObjectId"),cache.getPar(MainActivity.this,"headPortraitsHttp"));
                    downloadImage(headPortrait);
                }
            }else if(!cache.getPar(MainActivity.this,"headPortraitsHttp").equals("nothing")){
                HeadPortrait headPortrait=new HeadPortrait(cache.getPar(MainActivity.this,"ObjectId"),cache.getPar(MainActivity.this,"headPortraitsHttp"));
                downloadImage(headPortrait);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void initBottomNav() {
        AHBottomNavigationItem item1 = new AHBottomNavigationItem("答题", R.drawable.ic_profile_answer, R.color.colorPrimary);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem("查询", R.drawable.ic_profile_article, R.color.colorPrimary);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem("论坛", R.drawable.ic_profile_column, R.color.colorPrimary);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem("要闻", R.drawable.ic_profile_favorite, R.color.colorPrimary);

        mAhBottomNavigation.addItem(item1);
        mAhBottomNavigation.addItem(item2);
        mAhBottomNavigation.addItem(item3);
        mAhBottomNavigation.addItem(item4);
        mAhBottomNavigation.setBehaviorTranslationEnabled(true);
        mAhBottomNavigation.setAccentColor(getResources().getColor(R.color.colorPrimary));
        mAhBottomNavigation.setInactiveColor(getResources().getColor(R.color.font_normal));
        mAhBottomNavigation.setCurrentItem(0);

        mAhBottomNavigation.setBehaviorTranslationEnabled(true);
        mAhBottomNavigation.setDefaultBackgroundColor(getResources().getColor(R.color.bg_color));


        mAhBottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener()
        {

            @Override
            public void onTabSelected(int position, boolean wasSelected)
            {

//                if(currentTabIndex==1||currentTabIndex==2){
//                    appBarLayout.setVisibility(View.GONE);
//                }else{
//                    appBarLayout.setVisibility(View.VISIBLE);
//                }
                if (currentTabIndex != position)
                {
                    FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
                    trx.hide(fragments.get(currentTabIndex));
                    if (!fragments.get(position).isAdded())
                    {
                        trx.add(R.id.content, fragments.get(position));
                    }
                    trx.show(fragments.get(position)).commit();
                }
                currentTabIndex = position;
            }
        });
    }

    private void showInitFragment(Fragment a) {
        getSupportFragmentManager().beginTransaction().replace(R.id.content,a).commit();
    }


    @Override
    public void initToolBar()
    {

        mToolbar.setTitle("公务员app");
        setSupportActionBar(mToolbar);
        ActionBar mActionBar = getSupportActionBar();
        if (mActionBar != null)
            mActionBar.setDisplayHomeAsUpEnabled(true);


        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(MainActivity.this,
                mDrawerLayout,
                mToolbar,
                R.string.app_name,
                R.string.app_name);

        mDrawerToggle.syncState();
        mDrawerLayout.addDrawerListener(mDrawerToggle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {

       // getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        switch (item.getItemId())
        {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
           // case R.id.action_search:

            //    return true;

            case R.id.action_today:
                //今日干货

                return true;

            case R.id.action_today_github:

                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }



    private void setupDrawerContent(NavigationView navigationView)
    {

        View headerView = navigationView.getHeaderView(0);
        mUserAvatar = (CircleImageView) headerView.findViewById(R.id.github_user_avatar);
        mUserName = (TextView) headerView.findViewById(R.id.github_user_name);
        mUserAvatar.setOnClickListener(this);
        try {
            mUserName.setText((new Cache().getPar(MainActivity.this,"nickname")).equals("nothing")?"默认昵称":new Cache().getPar(MainActivity.this,"nickname"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        //setUserInfo();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener()
        {

            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem)
            {

                switch (menuItem.getItemId())
                {
                    case R.id.nav_home:

                        break;


                    case R.id.nav_my_info:
                          Intent intent=new Intent(MainActivity.this,MyInfoActivity.class);
                        startActivity(intent);
                       break;



                    case R.id.nav_about_app:
                        // 关于App
                        //startActivity(new Intent(MainActivity.this, AboutActivity.class));
                        break;

                    default:
                        break;
                }
                return true;
            }
        });
    }
    @Override
    public void onClick(View v)
    {

        if (v.getId() == R.id.github_user_avatar)
        {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
            startActivityForResult(intent,ACTION_CHOOSE);
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if(keyCode== KeyEvent.KEYCODE_BACK)
                logoutApp();
        return  false;
    }

    private void logoutApp()
    {

        if (System.currentTimeMillis() - exitTime > 2500)
        {
            SnackbarUtil.showMessage(mCoordinatorLayout, "再按一次退出publicfunctionary");
            exitTime = System.currentTimeMillis();
        } else
        {
            finish();
        }
    }

    @Override
    protected void onDestroy()
    {
        BmobUser.logOut();
        this.unregisterReceiver(mBroadcastReceiver);
        super.onDestroy();
    }
    private void addInformation() {

        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);

        View view = inflater.inflate(R.layout.addinfo_dialog, null);
        alertDialog.setView(view);
        alertDialog.setCanceledOnTouchOutside(false);
        Window window = alertDialog.getWindow();
        window.setWindowAnimations(R.style.MyDialog);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar1);
        final EditText NickName = (EditText) view.findViewById(R.id.nickName);
        final Spinner province1= (Spinner) view.findViewById(R.id.pros);
        Button Submit = (Button) view.findViewById(R.id.Submit);
        Button Cancel = (Button) view.findViewById(R.id.Cancel);
        alertDialog.show();
        province1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                province = provinces[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (NickName.getText() == null)
                    NickName.setError("请输入后提交");
                else {
                    final String nickname = NickName.getText().toString();
                    progressBar.setVisibility(View.VISIBLE);

                    User user= BmobUser.getCurrentUser(User.class);
                    //User user=new User();
                    user.setNichen(nickname);
                    user.setProvience(province);
                    String objectId=null;
                    Cache cache=new Cache();
                    try {
                        objectId=cache.getPar(MainActivity.this,"ObjectId");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    user.setObjectId(objectId);
                    user.update(objectId, new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if(e==null){
                                Cache cache1=new Cache();
                                cache1.SetPar(MainActivity.this,"nickname",nickname);
                                cache1.SetPar(MainActivity.this,"province",province);
                                Toast.makeText(MainActivity.this, "添加成功", Toast.LENGTH_LONG).show();
                                alertDialog.dismiss();
                            }else{
                                Toast.makeText(MainActivity.this, e.getErrorCode()+"  "+e.getMessage(), Toast.LENGTH_LONG).show();
                                alertDialog.dismiss();
                            }
                        }
                    });
                }
            }
        });
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case ACTION_CHOOSE:
                if(data!=null){
                    String path= ImageUtils.getImageAbsolutePath(this,data.getData());
                    new Cache().SetPar(MainActivity.this,"headPortraitsSdcard",path);
                   setImageToView(path);
                    final BmobFile bmobfile=new BmobFile(new File(path));
                    bmobfile.upload(new UploadFileListener() {
                        @Override
                        public void done(BmobException e) {
                            if(e==null){
                                new Cache().SetPar(MainActivity.this,"headPortraitsHttp",bmobfile.getFileUrl());
                                User u=new User();
                                u.setHeadPortrait(bmobfile.getFileUrl());
                                try {
                                    u.update(new Cache().getPar(MainActivity.this, "ObjectId"), new UpdateListener() {
                                        @Override
                                        public void done(BmobException e) {
                                            Toast.makeText(MainActivity.this,"上传头像成功",Toast.LENGTH_LONG).show();
                                        }
                                    });
                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                }
                            }else{
                                Toast.makeText(MainActivity.this,"上传头像失败，"+e.getMessage(),Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                }
                break;
        }
    }
private boolean setImageToView(String path) {
    File file = new File(path);
    if(!file.exists())
        return false;
    Uri uri = Uri.fromFile(file);
    try {
        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
        Bitmap smallBitmap = ImageUtils.setScaleBitmap(bitmap, 2);
        mUserAvatar.setImageBitmap(smallBitmap);
    } catch (FileNotFoundException e)
    {
        e.printStackTrace();
    }finally {
        return true;
    }
}

}


