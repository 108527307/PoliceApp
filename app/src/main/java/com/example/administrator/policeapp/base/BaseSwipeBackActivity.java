package com.example.administrator.policeapp.base;

import android.os.Bundle;

import com.example.administrator.policeapp.widget.swipebacklayout.SwipeBackActivity;
import com.example.administrator.policeapp.widget.swipebacklayout.SwipeBackLayout;

import butterknife.ButterKnife;

/**
 * Created by 11 on 2016/4/1.
 * <p/>
 * 侧滑返回Activity基类
 */
public abstract class BaseSwipeBackActivity extends SwipeBackActivity
{

    public SwipeBackLayout mSwipeBackLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //设置布局内容
        setContentView(getLayoutId());
        //初始化黄油刀控件绑定框架
        ButterKnife.bind(this);
        //初始化侧滑返回layout
        mSwipeBackLayout = getSwipeBackLayout();//获取滑动布局是咧
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);//设置方向
        //初始化控件
        initViews(savedInstanceState);


    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    public abstract int getLayoutId();

    public abstract void initViews(Bundle savedInstanceState);
}
