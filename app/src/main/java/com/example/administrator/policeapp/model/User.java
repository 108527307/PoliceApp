package com.example.administrator.policeapp.model;

import cn.bmob.v3.BmobUser;

/**
 * Created by Administrator on 2017/3/7.
 */

public class User extends BmobUser {
    private String nichen;
    String provience;

    public String getHeadPortrait() {
        return headPortrait;
    }

    public void setHeadPortrait(String headPortrait) {
        this.headPortrait = headPortrait;
    }

    String headPortrait;
    public String getNichen() {
        return nichen;
    }

    public void setNichen(String nichen) {
        this.nichen = nichen;
    }

    public String getProvience() {
        return provience;
    }

    public void setProvience(String provience) {
        this.provience = provience;
    }

    public User(){
        this.setTableName("_User");
    }
}
