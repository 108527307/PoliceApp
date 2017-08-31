package com.example.administrator.policeapp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.policeapp.R;
import com.example.administrator.policeapp.base.RxBaseActivity;
import com.example.administrator.policeapp.model.User;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;


/**
 * Created by Administrator on 2016/6/9 0009.
 */
public class FogotPasActivity extends RxBaseActivity implements View.OnClickListener {
    private Button user_fogot,getCode;
    private EditText phone,code;
    private int index=60;
    public static final int TAG_END=12,TAG_SUCCESSFUCL=13;
    Timer tim;
     User u=null;
    private Handler Uihandler=new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.arg1){
                case 0001:
                    Toast.makeText(FogotPasActivity.this, "验证错误:" + msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    code.setText("");
                    getCode.setText("获取验证码");
                    index=60;
                    break;
                case 0002:
                    Toast.makeText(FogotPasActivity.this, "验证成功",  Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(FogotPasActivity.this,ResetPasActivity.class);
                    intent.putExtra("Code",code.getText().toString().trim());
                    Toast.makeText(FogotPasActivity.this,code.getText().toString().trim(),Toast.LENGTH_LONG).show();
                    startActivityForResult(intent,TAG_END);
                    //finish();
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    break;
                case 0103:
                    Toast.makeText(FogotPasActivity.this, "账户异常："+msg.obj.toString(),  Toast.LENGTH_SHORT).show();
                    index=60;
                    getCode.setText("获取验证码");
                    break;
                case 0004:
                    getCode.setText("("+index+")后可再次获取");
                    if(index==0)
                    {

                        tim.cancel();
                        Message m=new Message();
                        m.arg1=0006;
                        getCode.setClickable(true);
                        Uihandler.sendMessage(m);

                    }
                    break;
                case 0005:
                    Toast.makeText(FogotPasActivity.this, msg.obj.toString(),  Toast.LENGTH_SHORT).show();break;
                case 0006:
                    code.setText("");
                    getCode.setText("获取验证码");
                    index=60;
                    break;
                case 0102:
                    versms(phone.getText().toString().trim(),code.getText().toString().trim());
                default:break;
            }
            super.handleMessage(msg);
        }
    };
    @Override
    public int getLayoutId() {
        return R.layout.activity_fogot_pwd;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        user_fogot= (Button) findViewById(R.id.user_fogot);
        getCode= (Button) findViewById(R.id.getCode);
        phone= (EditText) findViewById(R.id.phone);
        code= (EditText) findViewById(R.id.code);
        getCode.setOnClickListener(this);
        user_fogot.setOnClickListener(this);
    }

    @Override
    public void initToolBar() {

    }


    /**
     * 发送短信
     */
    private void getsms(String phone)
    {
        tim=new Timer();
        tim.schedule(new TimerTask() {
            @Override
            public void run() {
                Message m = new Message();
                m.arg1 = 0004;
                getCode.setClickable(false);
                index--;
                Uihandler.sendMessage(m);
            }
        },0,1000);
        BmobSMS.requestSMSCode(phone, "叼炸天模板", new QueryListener<Integer>() {
            @Override
            public void done(Integer integer, cn.bmob.v3.exception.BmobException e) {
                Message msg = new Message();
                msg.arg1 = 0005;
                //Log.i("sgvs", arg1.toString());
                String a = e!= null ? (e.toString()) : ("发送成功");
                msg.obj = a;
                Uihandler.sendMessage(msg);
            }
        });




    }
    /**
     * 验证短信验证码
     * @param phone
     * @param smscode
     */
    private void versms(String phone, String smscode)
    {

        BmobSMS.verifySmsCode(phone, smscode, new UpdateListener() {
            @Override
            public void done(cn.bmob.v3.exception.BmobException e) {
                if (e == null) {
                    Message msg = new Message();
                    msg.arg1 = 0002;
                    Uihandler.sendMessage(msg);
                } else {
                    Message msg = new Message();
                    msg.arg1 = 0001;
                    msg.obj=e;
                    Uihandler.sendMessage(msg);
                }
            }
        });
    }
    /**
     * 判断是否存在该用户

    * */
    private void Judge(  String phone){
        BmobQuery<User> query = new BmobQuery<User>();
        query.addWhereEqualTo("username", phone);
        query.findObjects(new FindListener<User>() {

            @Override
            public void done(List<User> list, BmobException e) {
                if(e==null){
                    if (list.size() == 1) {
                        Message msg = new Message();
                        msg.arg1 = 0102;
                        // msg.obj = NewPas;
                        Uihandler.sendMessage(msg);
                    }
                }else{
                    Message msg = new Message();
                    msg.arg1 = 0103;
                    msg.obj = e.toString();
                    Uihandler.sendMessage(msg);
                }
            }
        });
    }
    @Override
    public void onClick(View v) {
        String Phone = phone.getText().toString().trim();
        switch (v.getId()) {

            case R.id.getCode:

                if (Phone.equals(""))
                    Toast.makeText(this, "请输入手机号", Toast.LENGTH_LONG).show();
                else {
                    getsms(Phone);
                }
                break;
            case R.id.user_fogot:
                String Code = code.getText().toString().trim();
                if (Code.equals(""))
                    Toast.makeText(this, "请输入验证码", Toast.LENGTH_LONG).show();
                else if (Phone.equals(""))
                    Toast.makeText(this, "请输入手机号", Toast.LENGTH_LONG).show();
                else {
                    Judge(Phone);
                }
                break;
            default:break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case TAG_END:
                if(resultCode==TAG_SUCCESSFUCL)
                    finish();
                break;
        }
    }
}
