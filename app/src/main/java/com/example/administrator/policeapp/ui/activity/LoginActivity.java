package com.example.administrator.policeapp.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.policeapp.R;
import com.example.administrator.policeapp.base.RxBaseActivity;
import com.example.administrator.policeapp.cache.Cache;
import com.example.administrator.policeapp.model.User;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;


public class LoginActivity extends RxBaseActivity implements OnClickListener {
    private EditText username;
    private EditText password;
    private Button submit;
    private TextView regist, user_fogetPassword;
    private String name;
    private String pas;
    ImageView eyes;
    boolean onclicked=false;
    User bu = new User();
    private List<String> list = new ArrayList<String>();
    private Handler uihandler = new Handler() {
        public void handleMessage(android.os.Message msg) {

            switch (msg.what) {
                case 0001:
                    Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_LONG).show();
                    break;
                case 0002:
                    Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_LONG).show();
                    password.setText("");


            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);



    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        user_fogetPassword = (TextView) findViewById(R.id.user_fogetPassword);
        user_fogetPassword.setOnClickListener(this);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        submit = (Button) findViewById(R.id.submit);
        regist = (TextView) findViewById(R.id.regist);
        submit.setOnClickListener(this);
        regist.setOnClickListener(this);
        eyes = (ImageView) findViewById(R.id.eyes);
        eyes.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        eyes.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (!onclicked) {
                        password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        eyes.setImageResource(R.drawable.icon_eye_open);
                        onclicked = true;
                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (onclicked) {
                        password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        eyes.setImageResource(R.drawable.icon_eye_close);
                        onclicked = false;
                    }
                }
                        return  true;
            }
        });
    }

    @Override
    public void initToolBar() {

    }


    /**
     *
显示提示信息，并处理退出程序事件
     * */
    private void showTips() {
        AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this)
                .setTitle("退出程序")
                .setMessage("是否退出程序")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        System.exit(0);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                }).create(); //创建对话框
        alertDialog.show(); // 显示对话框
    }

    /**
     *
处理点击退出事件
     * */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            this.showTips();
            return false;
        }
        return false;
    }
    /**
     *
处理点击事件
     * */
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.submit:
                name = username.getText().toString();
                pas = password.getText().toString();
                Log.i("c错误", name);
                Log.i("c错误", pas);
                bu.setUsername(name);
                bu.setPassword(pas);
                Log.i("username", bu.getUsername());
                bu.login(new SaveListener<User>() {
                    @Override
                    public void done(User user, BmobException e) {
                        if(e==null){
                            new Cache().destroyCache(LoginActivity.this);
                            String nickname="null";
                            String province="null";
                            String portrait="null";
                            if(!(user.getHeadPortrait()==null))
                                portrait=user.getHeadPortrait();
                            if(!(user.getNichen()==null))
                                nickname=user.getNichen();
                            if(!(user.getProvience()==null))
                                province=user.getProvience();
                            new Cache().Writecache(LoginActivity.this,user.getObjectId(),name,nickname,province,pas,user.getEmail(),portrait);
                            uihandler.sendEmptyMessage(0001);
                            Intent intent = new Intent();
                            //intent.putExtra("ObjectId", bu.getObjectId());
                            intent.setClass(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }else{
                            uihandler.sendEmptyMessage(0002);
                        }
                    }
                });
                break;
            case R.id.regist:
                Intent intent = new Intent(LoginActivity.this, RegistActivity.class);
                startActivity(intent);
                break;
            case R.id.user_fogetPassword:
                Intent int1 = new Intent(this, FogotPasActivity.class);
                startActivity(int1);
            default:
                break;

        }
    }


}

