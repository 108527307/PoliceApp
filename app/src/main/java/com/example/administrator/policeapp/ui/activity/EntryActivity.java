package com.example.administrator.policeapp.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.administrator.policeapp.R;
import com.example.administrator.policeapp.cache.Cache;
import com.example.administrator.policeapp.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Tips:App启动页面
 * 该页面不要继承AppCompatActivity
 * 会导致界面启动卡顿
 * AppCompatActivity会默认去加载主题的原因.
 * <p/>
 * 启动界面图片资源来自专栏App,感谢分享~
 */
public class EntryActivity extends Activity
{

    @Bind(R.id.iv_entry)
    ImageView mSplashImage;

    private static final int ANIMATION_TIME = 2000;

    private static final float SCALE_END = 1.13F;

    private static final int[] IMAGES = {
            R.drawable.ic_screen_default,

            };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);
        ButterKnife.bind(this);

        Random random = new Random(SystemClock.elapsedRealtime());
        mSplashImage.setImageResource(IMAGES[random.nextInt(IMAGES.length)]);

        Observable.timer(1000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>()
                {

                    @Override
                    public void call(Long aLong)
                    {

                        startAnim();
                    }
                });
    }

    private void startAnim()
    {

        ObjectAnimator animatorX = ObjectAnimator.ofFloat(mSplashImage, "scaleX", 1f, SCALE_END);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(mSplashImage, "scaleY", 1f, SCALE_END);

        AnimatorSet set = new AnimatorSet();
        set.setDuration(ANIMATION_TIME).play(animatorX).with(animatorY);
        set.start();

        set.addListener(new AnimatorListenerAdapter()
        {

            @Override
            public void onAnimationEnd(Animator animation)
            {
                //User user= BmobUser.getCurrentUser(User.class);
                User user=new User();
                String isfirst=null;
                try {
                     isfirst=new Cache().getPar(EntryActivity.this,"isfirst");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if("no".equals(isfirst)){
                    List<String> userinfo= new ArrayList<String>();
                    try {
                         userinfo=new Cache().GetAll(EntryActivity.this);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    user.setUsername(userinfo.get(0));
                    user.setPassword(userinfo.get(1));

                    user.login(new SaveListener<User>() {
                        @Override
                        public void done(User user, BmobException e) {
                            if(e==null) {
                                Log.i("currentUserInfo",user.getObjectId());
                                Toast.makeText(EntryActivity.this, "登陆成功", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(EntryActivity.this, MainActivity.class));
                                EntryActivity.this.finish();
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            }else{
                                Toast.makeText(EntryActivity.this, "账户异常，请重新登录,"+e.getMessage(), Toast.LENGTH_LONG).show();
                                startActivity(new Intent(EntryActivity.this, LoginActivity.class));
                                EntryActivity.this.finish();
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            }
                        }
                    });

                }else{
                    startActivity(new Intent(EntryActivity.this, LoginActivity.class));
                    EntryActivity.this.finish();
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }

            }
        });
    }
}
