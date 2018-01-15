package com.example.jsouptest.greenDao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Yw_Ambition on 2018/1/15.
 */
@Entity
public class GreenBean {

    private String name;
    private String time;
    @Generated(hash = 1770070743)
    public GreenBean(String name, String time) {
        this.name = name;
        this.time = time;
    }
    @Generated(hash = 1002137420)
    public GreenBean() {
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getTime() {
        return this.time;
    }
    public void setTime(String time) {
        this.time = time;
    }
   
}
