package com.example.administrator.policeapp.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/8/29.
 */
public class NewsBean implements Parcelable {
    private String title;
    private String date;
    private String author_name  ;
    private String thumbnail_pic_s;
    private String url;
    private String thumbnail_pis_s03;
    private String category;
    private boolean isread;

    public boolean isread() {
        return isread;
    }

    public void setIsread(boolean isread) {
        this.isread = isread;
    }

    protected NewsBean(Parcel in) {

        title = in.readString();
        date = in.readString();
        author_name = in.readString();
        thumbnail_pic_s = in.readString();
        url = in.readString();
        thumbnail_pis_s03 = in.readString();
        category = in.readString();
        isread=in.readByte()!=0;
    }

    public static final Creator<NewsBean> CREATOR = new Creator<NewsBean>() {
        @Override
        public NewsBean createFromParcel(Parcel in) {
            return new NewsBean(in);
        }

        @Override
        public NewsBean[] newArray(int size) {
            return new NewsBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.date);
        dest.writeString(this.author_name);
        dest.writeString(this.thumbnail_pic_s);
        dest.writeString(this.url);
        dest.writeString(this.thumbnail_pis_s03);
        dest.writeString(this.category);
        dest.writeByte(isread?(byte)1:(byte)0);
    }
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public String getThumbnail_pic_s() {
        return thumbnail_pic_s;
    }

    public void setThumbnail_pic_s(String thumbnail_pic_s) {
        this.thumbnail_pic_s = thumbnail_pic_s;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getThumbnail_pis_s03() {
        return thumbnail_pis_s03;
    }

    public void setThumbnail_pis_s03(String thumbnail_pis_s03) {
        this.thumbnail_pis_s03 = thumbnail_pis_s03;
    }



}
