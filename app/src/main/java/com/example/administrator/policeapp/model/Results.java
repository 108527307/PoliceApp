package com.example.administrator.policeapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Administrator on 2016/8/31.
 */
public class Results implements Parcelable {
    private String stat;
    private List<NewsBean> data;
 public Results(){};
    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }

    public List<NewsBean> getList() {
        return data;
    }

    public void setList(List<NewsBean> list) {
        this.data = list;
    }
public Results(List<NewsBean> list){
    this.data=list;
};
    protected Results(Parcel in) {
        stat = in.readString();
        data = in.createTypedArrayList(NewsBean.CREATOR);
    }

    public static final Creator<Results> CREATOR = new Creator<Results>() {
        @Override
        public Results createFromParcel(Parcel in) {
            return new Results(in);
        }

        @Override
        public Results[] newArray(int size) {
            return new Results[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(stat);
        dest.writeTypedList(data);
    }
}
