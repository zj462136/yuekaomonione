package com.example.lenovo.yuekaomonione.bean;

import java.util.List;

public class MessageEvent {
    private List<SearchBean.DataBean> dataBeans;

    public MessageEvent() {
    }

    public List<SearchBean.DataBean> getDataBeans() {
        return dataBeans;
    }

    public void setDataBeans(List<SearchBean.DataBean> dataBeans) {
        this.dataBeans = dataBeans;
    }

    public MessageEvent(List<SearchBean.DataBean> dataBeans) {

        this.dataBeans = dataBeans;
    }
    private boolean checked;

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
