package com.example.administrator.policeapp.cache;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.example.administrator.policeapp.utils.DES;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;


public class Cache {
private List<String> list;

private SharedPreferences user;
 public  void Writecache(Context context, String ObjectId, String username, String nickname,String province,String pas,String email,String headportrait)
 {
	 String password=null;
	 try {
		  password= DES.getDES(pas,"publicfunctionary");//
	 } catch (Exception e) {
		 e.printStackTrace();
	 }
	 user=context.getSharedPreferences("user", Context.MODE_PRIVATE);
	 Editor editor=user.edit();
	 editor.putString("password",password);
	 editor.putString("username", username);
	 if(!nickname.equals("null"))
	 editor.putString("nickname",nickname);
	 if(!province.equals("null"))
	 editor.putString("province",province);
	 editor.putString("ObjectId", ObjectId);
	 editor.putString("current", "yes");
	 editor.putString("isfirst","no");
	 editor.putString("email",email);
	 //editor.putString("headPortraitsSdcard","null");
	 if(!headportrait.equals("null"))
	 editor.putString("headPortraitsHttp",headportrait);
	 editor.commit();
	 Log.i("fsdfsf", user.getString("ObjectId", "-1"));
 }
	public  String getPar(Context context,String par) throws Exception {
		user=context.getSharedPreferences("user", Context.MODE_PRIVATE);
		if("password".equals(par))
			return DES.getDESOri(user.getString(par,"nothing"),"publicfunctionary");
		return user.getString(par,"nothing");
	}
	public void destroyCache(Context context){
		BmobUser.logOut();
		user=context.getSharedPreferences("user", Context.MODE_PRIVATE);
		Editor editor=user.edit();
		editor.clear();
		editor.commit();
		user=context.getSharedPreferences("config", Context.MODE_PRIVATE);
		Editor editor1=user.edit();
		editor1.clear();
		editor1.commit();
	}

	public List<String> GetAll(Context context) throws Exception {
		List<String> list=new ArrayList<>();
		user=context.getSharedPreferences("user", Context.MODE_PRIVATE);
		 list.add(user.getString("username","nothing"));
		list.add(DES.getDESOri(user.getString("password","nothing"),"publicfunctionary"));
		list.add(user.getString("nickname","nothing"));
		list.add(user.getString("province","nothing"));
	list.add(user.getString("ObjectId","nothing"));
		list.add(user.getString("headPortraitsSdcard","nothing"));
		list.add(user.getString("headPortraitsHttp","nothing"));
		list.add(user.getString("email","nothing"));
	return list;
	}
public String GetObjectId(Context context)
	{
		user=context.getSharedPreferences("user", Context.MODE_PRIVATE);
		return user.getString("ObjectId", "-1");
	}
	public void SetPar(Context context,String par,String value)
	{
		user=context.getSharedPreferences("user", Context.MODE_PRIVATE);
		Editor editor=user.edit();
		editor.putString(par,value);
		editor.commit();
	}

}
