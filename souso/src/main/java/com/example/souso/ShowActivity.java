package com.example.souso;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.souso.application.MyApplication;
import com.example.souso.bean.DataBean;
import com.example.souso.dao.DataBeanDao;
import com.fynn.fluidlayout.FluidLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShowActivity extends AppCompatActivity {
    @BindView(R.id.main_title)
    TitleItem mainTitle;
    @BindView(R.id.fluid_layout)
    FluidLayout fluidLayout;
    @BindView(R.id.but_clear)
    Button butClear;
    private List<DataBean> list;
    private TextView tv;
    private DataBeanDao dataBeanDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        ButterKnife.bind(this);

        list = new ArrayList<>();
        //创建数据库
        dataBeanDao = MyApplication.getDaoSession().getDataBeanDao();
        //得到自定义控件上的值
        mainTitle.setOnTitleClickListener(new TitleItem.OnTitleClickListener() {
            @Override
            public void onClick(String s) {
                //当前页面中的控件清空
                fluidLayout.removeAllViews();
                //创建自定义的HistoricalBean
                DataBean dataBean = new DataBean();
                //把从自定义控件上得到的s值放到bean对象中
                dataBean.setTitle(s);
                //把对象存到数据库中,不添加重复的字段名
                ShowActivity.this.dataBeanDao.insertOrReplace(dataBean);
                //清空list中的数据
                list.clear();
                //添加完数据后，再次查询数据库，把刚才添加的搜索内容，展示出来
                list.addAll(ShowActivity.this.dataBeanDao.loadAll());
                genTag();
            }
        });
        //查询数据库
        list.addAll(this.dataBeanDao.loadAll());
        genTag();

    }

    private void genTag() {
        for (int x = 0; x < list.size(); x++) {
            tv = new TextView(ShowActivity.this);
            tv.setText(list.get(x).getTitle());
            tv.setTextSize(16);
            FluidLayout.LayoutParams params = new FluidLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(12, 12, 12, 12);
            fluidLayout.addView(tv, params);
        }
    }

    @OnClick(R.id.but_clear)
    public void onViewClicked() {
        fluidLayout.removeAllViews();
        dataBeanDao.deleteAll();
    }
}
