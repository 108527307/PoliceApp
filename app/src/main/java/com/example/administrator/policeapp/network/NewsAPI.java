package com.example.administrator.policeapp.network;


import com.example.administrator.policeapp.model.NewsListBean;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by 11 on 2016/8/31.
 * <p/>
 * Retrofit请求配置接
 *
 */
public interface NewsAPI//配置retrofit
{

    /**
     *获取最新分类资讯
     *
     * @return
     */
 @GET("index")
 Observable<NewsListBean> getLastNews(@Query("type") String top, @Query("key") String key);

//    @GET("nc/video/list/{videoClass}/n/20-10.html")
//    Observable<VideoList> getLastVideo(@Path("videoClass") String videlclass);


}
