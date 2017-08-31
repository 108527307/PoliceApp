package com.example.administrator.policeapp.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/8/29.
 */
public class NewsListBean implements Parcelable {
   private Results result;
    private String reason;
  private String error_code;

    public Results getResult() {
        return result;
    }

    public void setResult(Results result) {
        this.result = result;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    protected NewsListBean(Parcel in) {
        result = in.readParcelable(Results.class.getClassLoader());
        reason = in.readString();
        error_code = in.readString();
    }

    public static final Creator<NewsListBean> CREATOR = new Creator<NewsListBean>() {
        @Override
        public NewsListBean createFromParcel(Parcel in) {
            return new NewsListBean(in);
        }

        @Override
        public NewsListBean[] newArray(int size) {
            return new NewsListBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(result, flags);
        dest.writeString(reason);
        dest.writeString(error_code);
    }
}
