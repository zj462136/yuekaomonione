package com.example.lenovo.yuekaomonione.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class DataBean {
    //主键自增
    @Id(autoincrement = true)
    private Long cid;
    private String title;
    @Generated(hash = 1136319213)
    public DataBean(Long cid, String title) {
        this.cid = cid;
        this.title = title;
    }
    @Generated(hash = 908697775)
    public DataBean() {
    }
    public Long getCid() {
        return this.cid;
    }
    public void setCid(Long cid) {
        this.cid = cid;
    }
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
}