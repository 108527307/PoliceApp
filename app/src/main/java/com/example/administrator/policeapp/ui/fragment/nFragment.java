package com.example.administrator.policeapp.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.administrator.policeapp.R;
import com.example.administrator.policeapp.adapter.NewsAdapter;
import com.example.administrator.policeapp.base.RxBaseFragment;
import com.example.administrator.policeapp.model.NewsBean;
import com.example.administrator.policeapp.model.NewsListBean;
import com.example.administrator.policeapp.model.Results;
import com.example.administrator.policeapp.network.RetrofitHelper;
import com.example.administrator.policeapp.sqlite.NewsDao;
import com.example.administrator.policeapp.widget.AutoLoadOnScrollListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


/**
 * Created by Administrator on 2016/9/22.
 */
public class nFragment extends RxBaseFragment {
    @Bind(R.id.swipe_refresha)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.recyclea)
    RecyclerView recyclerView;
    Boolean isRefresh;
    private String[] tabsValues=new String[]{"toutiao","shehui","guonei","guoji","junshi"};
    private LinearLayoutManager mLinearLayoutManager;
    static int Index=0;
    private List<NewsBean> newsBeans=new ArrayList<>();
    private NewsAdapter newsAdapter;
    NewsListBean a;
    private String currentTime="";
    private AutoLoadOnScrollListener autoLoadOnScrollListener;
    private Handler mHandler = new Handler()
    {

        @Override
        public void handleMessage(Message msg)
        {

            super.handleMessage(msg);
            if (msg.what == 0)
            {
                Log.i("info","b");
                getLatesNews(false);
            } else if (msg.what == 1)
            {
                hideProgress();
                swipeRefreshLayout.setRefreshing(false);
                finishGetNews();
            }
        }
    };
    private void finishGetNews(){

        newsAdapter.notifyDataSetChanged();
        // recyclerView.notify();
        // start
    }
    public static nFragment newInstance(int index){
        nFragment courseFragment=new nFragment();
        Bundle bundle=new Bundle();
        Index=index;
        bundle.putInt("Index", index);
        courseFragment.setArguments(bundle);
        return courseFragment;
    }
    @Override
    public int getLayoutId() {
        return R.layout.fragment_n;
    }
    public void showProgress(){
        recyclerView.setVisibility(View.GONE);

    }
    public void hideProgress(){

        recyclerView.setVisibility(View.VISIBLE);
    }
    private void getLatesNews(final boolean isDownRefresh){
        Log.i("info",tabsValues[Index]);
        RetrofitHelper.builder()
                .getLatestNews(tabsValues[(int) getArguments().get("Index")], "52e4fc8cef5304046d03179ceee784c6")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {


                        if (!isDownRefresh) {

                            showProgress();
                        }

                    }
                }).map(new Func1<NewsListBean, NewsListBean>() {
            @Override
            public NewsListBean call(NewsListBean newsListBean) {
                newsBeans = newsListBean.getResult().getList();


                // return changReadState(newsListBean);//转化
                return newsListBean;
            }
        })
                .subscribe(new Action1<NewsListBean>() {
                    @Override
                    public void call(NewsListBean newsListBean) {
                        if (newsListBean.getResult().getList() == null) {
                            hideProgress();
                            a = newsListBean;
                            swipeRefreshLayout.setRefreshing(false);
                            Toast.makeText(getActivity(), "加载数据失败", Toast.LENGTH_LONG).show();
                        } else {
                            newsAdapter.updateData(newsListBean.getResult().getList());
                            if (newsListBean.getResult().getList().size() < 8) {
                                loadMoreNews("none");
                            }

                            mHandler.sendEmptyMessageAtTime(1, 2000);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        hideProgress();
                        Toast.makeText(getActivity(), "加载数据失败", Toast.LENGTH_LONG).show();
                    }
                });

    }
    public NewsListBean changReadState(NewsListBean newslist)
    {
        List<NewsBean> temp=new ArrayList<>();
        List<String> hasRead=new NewsDao(getActivity()).getAllReadNew();
        for(int i=0;i<newslist.getResult().getList().size();i++){
            for(int j=0;j<hasRead.size();j++){
                if(newslist.getResult().getList().get(i).equals(hasRead.get(j))){
                    NewsBean n=newslist.getResult().getList().get(i);
                    n.setIsread(true);
                    temp.add(n);
                }

            }
        }
        Results t=new Results();
        t.setList(temp);
        newslist.setResult(t);
        return newslist;
    }
    private void loadMoreNews(String currentTime) {
        RetrofitHelper
                .builder().
                getLatestNews(tabsValues[(int) getArguments().get("Index")], "52e4fc8cef5304046d03179ceee784c6").
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<NewsListBean, NewsListBean>() {
                    @Override
                    public NewsListBean call(NewsListBean newsListBean) {
                        return   changReadState(newsListBean);
                    }
                }).subscribe(new Action1<NewsListBean>() {
            @Override
            public void call(NewsListBean newsListBean) {
                autoLoadOnScrollListener.setLoading(false);//设置是否在加载
                newsAdapter.addData(newsListBean.getResult().getList());
                //NewsFragment.this.currentTime=
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                autoLoadOnScrollListener.setLoading(false);
            }
        });
    }

    @Override
    public void initViews() {
       // Index= (int) getArguments().get("Index");
        swipeRefreshLayout.setColorSchemeColors(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mHandler.sendEmptyMessageAtTime(0, 1000);
            }
        });
        newsAdapter=new NewsAdapter(getActivity(),newsBeans);
        mLinearLayoutManager=new LinearLayoutManager(getActivity());//负责item视图的布局
        recyclerView.setHasFixedSize(true);//item固定高度
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(mLinearLayoutManager);//设置
        recyclerView.setAdapter(newsAdapter);

        autoLoadOnScrollListener=new AutoLoadOnScrollListener(mLinearLayoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                loadMoreNews(currentTime);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                swipeRefreshLayout.setEnabled(mLinearLayoutManager.findFirstCompletelyVisibleItemPosition()==0);
            }
        };
        recyclerView.addOnScrollListener(autoLoadOnScrollListener);
        getLatesNews(false);

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
}
