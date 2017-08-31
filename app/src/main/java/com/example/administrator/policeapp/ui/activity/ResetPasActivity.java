package com.example.administrator.policeapp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.policeapp.R;
import com.example.administrator.policeapp.base.RxBaseActivity;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;


/**
 * Created by Administrator on 2016/6/9 0009.
 */
public class ResetPasActivity extends RxBaseActivity implements View.OnClickListener {
    EditText newPas,newPasOk;
    Button user_reset;
    Handler uihandler=new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.arg1){
                case 0010:
                   // List<User> u=(List<User>)msg.obj;
                    Updata();
                    break;
                case 0020:
                    Toast.makeText(ResetPasActivity.this,msg.obj.toString(), Toast.LENGTH_LONG).show();
                    break;
                case 0101:
                    Toast.makeText(ResetPasActivity.this,"重置成功", Toast.LENGTH_LONG).show();
                    Intent mIntent = new Intent();
                    ResetPasActivity.this.setResult(FogotPasActivity.TAG_SUCCESSFUCL, mIntent);
                    finish();
                    break;
                case 0102:
                    Toast.makeText(ResetPasActivity.this,msg.obj.toString(), Toast.LENGTH_LONG).show();
                    break;
                default:break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.activity_reset_pwd;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        newPas= (EditText) findViewById(R.id.newPassword);
        newPasOk= (EditText) findViewById(R.id.newPasswordOk);
        user_reset= (Button) findViewById(R.id.user_reset);
        user_reset.setOnClickListener(this);
    }

    @Override
    public void initToolBar() {

    }
    /**
     *
根据号码重置密码
     * */
private void Updata(){
   // User user=new User();
    Intent intent=getIntent();
    BmobUser.resetPasswordBySMSCode(String.valueOf(getIntent().getIntExtra("Code", -1)), newPas.getText().toString().trim(), new UpdateListener() {
        @Override
        public void done(BmobException e) {
            if (e == null) {
                Log.i("smile", "密码重置成功");
                Message msg = new Message();
                msg.arg1 = 0101;
                uihandler.sendMessage(msg);
            } else {
                Log.i("smile", "重置失败：msg = " + e.toString());
                Message msg = new Message();
                msg.arg1 = 0102;
                msg.obj = e.getLocalizedMessage();
                uihandler.sendMessage(msg);
            }
        }
    });
}
    @Override
    public void onClick(View v) {
        String newpas=newPas.getText().toString().trim();
        String newpasok=newPasOk.getText().toString().trim();
        if(newpas.equals("")||newpasok.equals(""))
            Toast.makeText(this,"请填完提交", Toast.LENGTH_LONG).show();
        else if(!newpas.equals(newpasok))
            Toast.makeText(this,"密码输入不一致", Toast.LENGTH_LONG).show();
        else{
             Updata();
        }
    }
}
