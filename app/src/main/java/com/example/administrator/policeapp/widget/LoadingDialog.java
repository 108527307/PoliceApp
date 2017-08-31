package com.example.administrator.policeapp.widget;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.policeapp.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by hcc on 16/6/16 21:05
 * 100332338@qq.com
 */
public class LoadingDialog extends DialogFragment
{


    @Bind(R.id.load_progress)
    CircleProgressView mCircleProgressView;

    @Bind(R.id.load_dialog_tv)
    TextView mLoadTv;


    public static LoadingDialog newInstance()
    {

        return new LoadingDialog();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        View view = inflater.inflate(R.layout.layout_load_dialog, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {

        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        initView();
    }

    private void initView()
    {

    }
}
