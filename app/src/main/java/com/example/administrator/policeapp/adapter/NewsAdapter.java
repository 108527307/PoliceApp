package com.example.administrator.policeapp.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.policeapp.R;
import com.example.administrator.policeapp.model.NewsBean;
import com.example.administrator.policeapp.ui.activity.DetailActivity;
import com.example.administrator.policeapp.utils.DateUtil;
import com.example.administrator.policeapp.utils.LogUtil;
import com.example.administrator.policeapp.utils.WeekUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/8/29.
 */
public class NewsAdapter  extends RecyclerView.Adapter<NewsAdapter.ItemContentViewHolder>{
    private static final int ITEM_CONTENT=0;//新闻item
    private static final int ITEM_TIME=1;//时间item

    private List<NewsBean> newsBeanList=new ArrayList<>();
    private Context context;
    public NewsAdapter(Context context, List<NewsBean> newes){
        this.context=context;
        this.newsBeanList=newes;
    }


    @Override
    public int getItemViewType(int position) {
        LogUtil.i("process","getitemtype");
        if(position==0)
            return ITEM_CONTENT;

        //如果发现此item的事件和上一个item事件不同则添加一个时间item
            String time= DateUtil.formatDate(newsBeanList.get(position).getDate());
        int index=position-1;
        boolean isDifferent=!DateUtil.formatDate(newsBeanList.get(index).getDate()).equals(time);
        int pos=isDifferent?ITEM_TIME:ITEM_CONTENT;
        return pos;
    }


    @Override
    public ItemContentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LogUtil.i("process","oncreateviewholder");
       if(viewType==ITEM_TIME){
           return new ItemTimeViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_time,parent,false));
       }else
           return new ItemContentViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news,parent,false));
    }

    @Override
    public void onBindViewHolder(ItemContentViewHolder holder, int position) {//
        LogUtil.i("process","onbindviewholder");
        NewsBean news=newsBeanList.get(position);
        if(news==null){
            return;
        }

            if(holder instanceof ItemTimeViewHolder){
                setNewsDate(holder,news);
                ItemTimeViewHolder itemTimeViewHolder=(ItemTimeViewHolder) holder;
                String timeStr="";
                if(position==0){
                    timeStr="今日新闻";
                }else{
                    timeStr= DateUtil.formatDate(news.getDate())+" "+ WeekUtil.getWeek(news.getDate());
                }
                itemTimeViewHolder.mTime.setText(timeStr);
                itemTimeViewHolder.mTime.setVisibility(View.VISIBLE);
            }else{
                setNewsDate(holder,news);
        }

    }
//附着数据
    private void setNewsDate(final ItemContentViewHolder holder,final NewsBean news){
        holder.mTitle.setText(news.getTitle());
       Glide.with(this.context).load(news.getThumbnail_pic_s()).placeholder(R.drawable.image_default_avatar).into(holder.mPic);
        holder.mMorePic.setVisibility(View.GONE);
        if(!news.isread()){
            holder.mTitle.setTextColor(ContextCompat.getColor(context, R.color.color_unread));
        }else
            holder.mTitle.setTextColor(ContextCompat.getColor(context,R.color.color_read));
        holder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!news.isread()){
                    news.setIsread(true);
                   // holder.mTitle.setSelected(true);
                    holder.mTitle.setTextColor(ContextCompat.getColor(context,R.color.color_read));
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                                newsDao.intsertRead(news.getUrl());
//                        }
//                    }).start();
                }
                //跳转
                DetailActivity.launch(context, news);

            }
        });
    }
public void updateData(List<NewsBean> list){
    this.newsBeanList=list;
    notifyDataSetChanged();
}
    public void addData(List<NewsBean> list){
        if(this.newsBeanList==null){
            updateData(list);
        }else{
            this.newsBeanList.addAll(list);
            notifyDataSetChanged();
        }
        }
    @Override
    public int getItemCount() {
       return newsBeanList.size()==0?0:newsBeanList.size();
    }
    public class ItemTimeViewHolder extends ItemContentViewHolder
    {

        @Bind(R.id.item_time)
        TextView mTime;

        public ItemTimeViewHolder(View itemView)
        {

            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
    public class ItemContentViewHolder extends RecyclerView.ViewHolder
    {


        @Bind(R.id.card_view)
        CardView mLayout;

        @Bind(R.id.item_image)
        ImageView mPic;

        @Bind(R.id.item_title)
        TextView mTitle;

        @Bind(R.id.item_more_pic)
        ImageView mMorePic;


        public ItemContentViewHolder(View itemView)
        {

            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
