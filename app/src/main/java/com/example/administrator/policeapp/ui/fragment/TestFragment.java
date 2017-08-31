package com.example.administrator.policeapp.ui.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.policeapp.R;
import com.example.administrator.policeapp.base.RxBaseFragment;
import com.example.administrator.policeapp.sqlite.AnswerColumns;
import com.example.administrator.policeapp.sqlite.BaseColumns;
import com.example.administrator.policeapp.sqlite.CauseInfo;
import com.example.administrator.policeapp.sqlite.DBManager;
import com.example.administrator.policeapp.ui.activity.CollectActivity;
import com.example.administrator.policeapp.ui.activity.ErrorActivity;
import com.example.administrator.policeapp.ui.activity.ExamActivity;
import com.example.administrator.policeapp.ui.activity.HisResultActivity;
import com.example.administrator.policeapp.ui.activity.OrderActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import butterknife.Bind;

/**
 * Created by Administrator on 2017/2/1.
 */

public class TestFragment extends RxBaseFragment implements View.OnClickListener{
    private boolean hasPressedBack;
    //private ProgressBar progressBar;
    @Bind(R.id.order)
    TextView order;
    @Bind(R.id.simulate)
    TextView simulate;

    @Bind(R.id.favorite)
    LinearLayout favorite;
    @Bind(R.id.wrong)
    LinearLayout wrong;
    @Bind(R.id.history)
    LinearLayout history;
    @Bind(R.id.progressBar1)
    ProgressBar progressBar;
    Handler ioHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case 1:  progressBar.setVisibility(View.GONE);break;
                case 2:  Toast.makeText(getActivity(), "考试开始", Toast.LENGTH_SHORT).show();break;
                default:break;
            }
        }
    };
    @Override
    public int getLayoutId() {
        return R.layout.testfragment_layout;
    }

    @Override
    public void initViews() {
//        if (TimeUtils.isNetworkAvailable(getActivity())) {
//            DBManager.getInstance(getActivity()).removeAll(AnswerColumns.TABLE_NAME);
//        }

        try {
            asynctaskInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        order.setOnClickListener(this);
        simulate.setOnClickListener(this);

        favorite.setOnClickListener(this);
        wrong.setOnClickListener(this);
        history.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {



            case R.id.order:
                startActivity(new Intent(getActivity(), OrderActivity.class));
                break;

            case R.id.simulate:
                View layout = getActivity().getLayoutInflater().inflate(R.layout.enter_simulate, null);
                final Dialog dialog = new Dialog(getActivity());
                dialog.setTitle("温馨提示");
                dialog.show();
                dialog.getWindow().setContentView(layout);
                final EditText et_name = (EditText) layout.findViewById(R.id.et_name);
                TextView confirm = (TextView) layout.findViewById(R.id.confirm);
                TextView cancel = (TextView) layout.findViewById(R.id.cancel);
                confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (TextUtils.isEmpty(et_name.getText().toString().trim())) {
                            Toast.makeText(getActivity(), "请先输入考试姓名", Toast.LENGTH_SHORT).show();
                        } else {
                            ExamActivity.intentToExamActivity(getActivity(), et_name.getText().toString().trim());

                        }
                        dialog.dismiss();
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                break;

            case R.id.favorite:
                startActivity(new Intent(getActivity(), CollectActivity.class));
                break;

            case R.id.wrong:
                startActivity(new Intent(getActivity(), ErrorActivity.class));
                break;

            case R.id.history:
                startActivity(new Intent(getActivity(), HisResultActivity.class));
                break;

            default:
                break;
        }
    }

    private void asynctaskInstance() {
        progressBar.setVisibility(View.VISIBLE);
      new Thread(new Runnable() {
           @Override
           public void run() {
              try {
                  InputStream fi = getClass().getResourceAsStream("/assets/json.txt");
                  BufferedReader bi=new BufferedReader(new InputStreamReader(fi,"UTF-8"));
                  char str[] = new char[64];
                  String result = new String();
                  int c;
                  while ((c = bi.read(str)) != -1) {
                      result += new String(str);
                  }

                  JSONObject jsonObject = new JSONObject(result);
                  int code = jsonObject.getInt("code");
                  if (code == 1) {
                      String content = jsonObject.getString("content");
                      JSONArray array = new JSONArray(content);
                      for (int i = 0; i < 5; i++) {
                          JSONObject object = array.getJSONObject(i);
                          String timu_title = new JSONObject(object.getString("timu")).getString("title");
                          String timu_one = new JSONObject(object.getString("timu")).getString("one");
                          String timu_tow = new JSONObject(object.getString("timu")).getString("tow");
                          String timu_three = new JSONObject(object.getString("timu")).getString("three");
                          String timu_four = new JSONObject(object.getString("timu")).getString("four");
                          String daan_one = new JSONObject(object.getString("daan")).getString("daan_one");
                          String daan_tow = new JSONObject(object.getString("daan")).getString("daan_tow");
                          String daan_three = new JSONObject(object.getString("daan")).getString("daan_three");
                          String daan_four = new JSONObject(object.getString("daan")).getString("daan_four");
                          String types = new JSONObject(object.getString("types")).getString("types");
                          //String detail = new JSONObject(object.getString("detail")).getString("detail");
                          int reply = BaseColumns.NULL;
                          CauseInfo myData = new CauseInfo(timu_title, timu_one, timu_tow, timu_three, timu_four, daan_one, daan_tow,
                                  daan_three, daan_four, types, reply);
                          DBManager.getInstance(getActivity()).insert(AnswerColumns.TABLE_NAME, myData);
                      }
                  } else {
                      Message msg=new Message();
                      msg.what=2;
                      ioHandler.sendMessage(msg);
                  }
                  Message msg=new Message();
                  msg.what=1;
                  ioHandler.sendMessage(msg);
              }

              catch(Exception e){
                  e.printStackTrace();
              }
          }
      }).start();
    }
    public static TestFragment newInstance()
    {

        return new TestFragment();
    }
}
