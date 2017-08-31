package com.example.administrator.policeapp.network;

import com.example.administrator.policeapp.base.RxBaseApplication;
import com.example.administrator.policeapp.model.NewsListBean;
import com.example.administrator.policeapp.utils.NetWorkUtil;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

/**
 * Created by cong on 2016/3/31.
 * <p/>
 * Retrofit管理类
 */
public class RetrofitHelper
{

    public static final String ZHIHU_DAILY_URL = "http://v.juhe.cn/toutiao/";//日常url

    public static final String VIDEO_BASE_URL = "http://c.m.163.com/";//视频url

    private static OkHttpClient mOkHttpClient;

    private  NewsAPI mNewsapi;

    private  NewsAPI mNewsapi2;

    public static final int CACHE_TIME_LONG = 60 * 60 * 24 * 7;//cache保存时间7天


    public static RetrofitHelper builder()
    {

        return new RetrofitHelper();
    }

    private RetrofitHelper()
    {

        initOkHttpClient();
    mNewsapi=getLastNews();
        mNewsapi2=getLastVideo();

    }
private   NewsAPI getLastNews(){
    Retrofit mRetrofit = new Retrofit.Builder()
            .baseUrl(ZHIHU_DAILY_URL)
            .client(mOkHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .build();

    return mRetrofit.create(NewsAPI.class);
}

    public  NewsAPI getLastVideo()
    {

        Retrofit retrofit = new Retrofit.Builder()//初始化retrofit
                .baseUrl(VIDEO_BASE_URL)
                .client(new OkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        return retrofit.create(NewsAPI.class);
    }


    /**
     * 初始化OKHttpClient
     */
    private void initOkHttpClient()
    {
//拦截器，记录应用网络请求信息
        /* 可以通过 setLevel 改变日志级别
 共包含四个级别：NONE、BASIC、HEADER、BODY

NONE 不记录

BASIC 请求/响应行
--> POST /greeting HTTP/1.1 (3-byte body)
<-- HTTP/1.1 200 OK (22ms, 6-byte body)

HEADER 请求/响应行 + 头

--> Host: example.com
Content-Type: plain/text
Content-Length: 3

<-- HTTP/1.1 200 OK (22ms)
Content-Type: plain/text
Content-Length: 6

BODY 请求/响应行 + 头 + 体
*/
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        if (mOkHttpClient == null)
        {
            synchronized (RetrofitHelper.class)//代码块儿同步
            {
                if (mOkHttpClient == null)
                {
                    //设置Http缓存
                    //HttpParams params=new BasicParser();

                    Cache cache = new Cache(new File(RxBaseApplication.getContext().getCacheDir(), "HttpCache"), 1024 * 1024 * 100);//设置缓存大小为100m
                   //初始化okhttpclient
                    mOkHttpClient = new OkHttpClient.Builder()
                            .cache(cache)
                            .addInterceptor(mRewriteCacheControlInterceptor)//添加拦截器
                            .addNetworkInterceptor(mRewriteCacheControlInterceptor)
                            .addInterceptor(interceptor)
                            .retryOnConnectionFailure(true)//自动重连
                            .connectTimeout(15, TimeUnit.SECONDS)
                            .build();
                }
            }
        }
    }
    //新建应用拦截器
    private Interceptor mRewriteCacheControlInterceptor = new Interceptor()
    {

        @Override
        public Response intercept(Chain chain) throws IOException
        {

            Request request = chain.request();
            if (!NetWorkUtil.isNetworkConnected())//无网络获取缓存
            {
                request = request.newBuilder().header("User-Agent"," Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36").cacheControl(CacheControl.FORCE_CACHE).build();
            }
            Response originalResponse = chain.proceed(request);
            if (NetWorkUtil.isNetworkConnected())
            {
                String cacheControl = request.cacheControl().toString();
                return originalResponse.newBuilder().header("Cache-Control", cacheControl).removeHeader("Pragma").build();
            } else
            {
                return originalResponse.newBuilder().header("Cache-Control", "public, only-if-cached, max-stale=" + CACHE_TIME_LONG)
                        .removeHeader("Pragma").build();
            }
        }
    };

    /**
     * Api封装 方便直接调用
     **/

    public Observable<NewsListBean> getLatestNews(String type, String key)
    {
        Observable<NewsListBean> a=mNewsapi.getLastNews(type,key);
        return a;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }
}
