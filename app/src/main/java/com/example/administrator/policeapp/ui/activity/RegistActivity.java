package com.example.administrator.policeapp.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.policeapp.R;
import com.example.administrator.policeapp.base.RxBaseActivity;
import com.example.administrator.policeapp.model.User;

import java.util.Timer;
import java.util.TimerTask;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;


/**
 * @author c_875
 *
 */

public class RegistActivity extends RxBaseActivity implements OnClickListener {
	private EditText name;
	private EditText pas1;
	private EditText pas2;
	private EditText yanzhenid;
	private Button submit;
	private Button qinqiu;
	private EditText email;
	private int index=60;
	private String phone=null;
	private String password1=null;
	private String password2=null;
	private String em=null;
	private String yanzhen=null;
	Timer tim;
	User u=null;
	private Handler uihandler=new Handler()
	{
		public void handleMessage(android.os.Message msg) {
			switch(msg.arg1)
			{
			case 0001:
				Toast.makeText(RegistActivity.this, "验证错误:"+msg.obj.toString(), Toast.LENGTH_SHORT).show();
				yanzhenid.setText("");
				qinqiu.setText("获取验证码");
				index=60;
				break;
			case 0002:
				Toast.makeText(RegistActivity.this, "验证成功",  Toast.LENGTH_SHORT).show();
				break;
			case 0003:	
				Toast.makeText(RegistActivity.this, "登陆异常"+msg.obj.toString(),  Toast.LENGTH_SHORT).show();
				index=60;
				qinqiu.setText("获取验证码");
				break;
			case 0004:
				qinqiu.setText("("+index+")后可再次获取");
				if(index==0)
				{

					tim.cancel();
					Message m=new Message();
					m.arg1=0006;
					qinqiu.setClickable(true);
					uihandler.sendMessage(m);

				}
				break;
			case 0005:
				Toast.makeText(RegistActivity.this, msg.obj.toString(),  Toast.LENGTH_SHORT).show();break;

			default:break;
			}

		};
	};

	@Override
	public int getLayoutId() {
		return R.layout.activity_register;
	}

	@Override
	public void initViews(Bundle savedInstanceState) {
		name=(EditText) findViewById(R.id.username);
		pas1=(EditText) findViewById(R.id.password);
		pas2=(EditText) findViewById(R.id.password2);
		yanzhenid=(EditText) findViewById(R.id.yanzhenid);
		submit=(Button) findViewById(R.id.submit);
		qinqiu=(Button) findViewById(R.id.qinqiuyanzhen);
		email=(EditText) findViewById(R.id.email);
		qinqiu.setText("获取验证");
		qinqiu.setOnClickListener(this);
		submit.setOnClickListener(this);
	}

	@Override
	public void initToolBar() {

	}

	/**
	 * 发送短信
	 */
	private void getsms()
	{
		tim=new Timer();
		tim.schedule(new TimerTask() {
			@Override
			public void run() {
				Message m = new Message();
				m.arg1 = 0004;

				qinqiu.setClickable(false);
				index--;
				uihandler.sendMessage(m);
			}
		}, 0, 1000);


                BmobSMS.requestSMSCode(phone, "叼炸天模板", new QueryListener<Integer>() {
					@Override
					public void done(Integer integer, cn.bmob.v3.exception.BmobException e) {
						if(e==null){
							Message msg = new Message();
							msg.arg1 = 0005;
							//Log.i("sgvs", arg1.toString());
							String a = "发送成功";
							msg.obj = a;
							uihandler.sendMessage(msg);
						}
						else{
							Message msg = new Message();
							msg.arg1 = 0001;
							//Log.i("sgvs", arg1.toString());
							String a = e.toString();
							msg.obj = a;
							uihandler.sendMessage(msg);
						}
					}
				});
				



	}
	/**
	 *
	 * 绑定手机号验证信息

	 * */
	private void UpPhone(String phone){
		User user =new User();
		user.setMobilePhoneNumber(phone);
		user.setMobilePhoneNumberVerified(true);
		User cur = User.getCurrentUser(User.class);
		user.update( cur.getObjectId(),new UpdateListener() {
			@Override
			public void done(cn.bmob.v3.exception.BmobException e) {
				if(e==null)
					Log.i("smile","手机号码绑定成功");
				else
					Log.i("smile","手机号码绑定失败："+e.toString());
			}

		});
	}
	/**
	 * 注册
	 */
	private void regist()

	{ 
		u=new  User();
		u.setUsername(phone.trim());
		u.setMobilePhoneNumber(phone);
		u.setMobilePhoneNumberVerified(true);

		u.setPassword(password1);
		u.setEmail(em);
        u.setEmailVerified(true);
		u.signUp(new SaveListener<User>() {
			@Override
			public void done(User user, BmobException e) {
				if (e == null||(e!=null&&e.getErrorCode()==9015)) {
					Message m = new Message();
					m.arg1 = 0002;
					m.what= Integer.parseInt(user.getUsername());
					uihandler.sendMessage(m);
					UpPhone(phone);
					finish();
				} else {
					Message m = new Message();
					m.arg1 = 0003;
					m.obj = e.toString();
					uihandler.sendMessage(m);
				}
			}
		});
//		u.signUp( new SaveListener<User>() {
//			@Override
//			public void done(User user, BmobException e) {
//				BmobUser.logOut();//删除本地用户缓存
//				if (e == null) {
//					Message m = new Message();
//					m.arg1 = 0002;
//                    m.what= Integer.parseInt(user.getUsername());
//					uihandler.sendMessage(m);
//					UpPhone(phone);
//					finish();
//				} else {
//					Message m = new Message();
//					m.arg1 = 0003;
//					m.obj = e.toString();
//					uihandler.sendMessage(m);
//				}
//			}
//		});

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

				if(e==null)
				{
					regist();
				}
				else {
					Message msg=new Message();
					msg.arg1=0001;
					msg.obj=e;
					uihandler.sendMessage(msg);
				}
			}
		});
	}
	/**
	 *
处理点击事件
	 * */
	@Override
	public void onClick(View v) {
		phone=name.getText().toString().trim();
		password1=pas1.getText().toString().trim();
		password2=pas2.getText().toString().trim();
		yanzhen=yanzhenid.getText().toString().trim();
		em=email.getText().toString().trim();		
		switch(v.getId())
		{
		case R.id.submit:
			Toast.makeText(RegistActivity.this,yanzhen,Toast.LENGTH_LONG).show();
			if(yanzhen.equals(""))
				Toast.makeText(RegistActivity.this, "请输入验证码", Toast.LENGTH_SHORT).show();
					else if(phone.equals("")||password1.equals("")||password2.equals("")||em.equals(""))
				Toast.makeText(RegistActivity.this, "请填完整信息", Toast.LENGTH_SHORT).show();
			else if(!password1.equals(password2))
			{
				Toast.makeText(RegistActivity.this, "密码输入不一致", Toast.LENGTH_SHORT).show();
				pas1.setText("");
				pas2.setText("");
				pas1.requestFocus();
			}else
				versms(phone, yanzhen);
			break;
		case R.id.qinqiuyanzhen:

			getsms();
			break;
		default:break;

		}
	};
}
