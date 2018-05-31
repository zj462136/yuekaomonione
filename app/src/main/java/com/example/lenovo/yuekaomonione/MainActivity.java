package com.example.lenovo.yuekaomonione;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.yuekaomonione.application.MyApplication;
import com.example.lenovo.yuekaomonione.bean.DataBean;
import com.example.lenovo.yuekaomonione.custom.FlowLayout;
import com.example.lenovo.yuekaomonione.dao.DataBeanDao;
import com.example.lenovo.yuekaomonione.ui.adapter.SearchAdapter;
import com.example.lenovo.yuekaomonione.ui.base.BaseActivity;
import com.example.lenovo.yuekaomonione.ui.search.SearchActivity;

import java.util.Collections;
import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private EditText et_title;
    /**
     * 搜索
     */
    private Button bt_search;
    private FlowLayout fl;
    /**
     * 清除历史记录
     */
    private Button bt_clear;
    private String mNames[] = {
            "welcome", "android", "TextView",
            "apple", "jamy", "kobe bryant",
            "jordan", "layout", "viewgroup",
            "margin", "padding", "text",
            "name", "type", "search", "logcat"
    };
    int id=0;
    private DataBeanDao dataBeanDao;
    private DataBean dataBean;
    private ListView lv;
    private List<DataBean> list;
    private RelativeLayout rl;
    private SearchAdapter adapter;
    private String trim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_main;
    }

    public void initView() {
        et_title = findViewById(R.id.et_title);
        bt_search = findViewById(R.id.bt_search);
        bt_search.setOnClickListener(this);
        fl = findViewById(R.id.fl);
        bt_clear = findViewById(R.id.bt_clear);
        bt_clear.setOnClickListener(this);
        lv = findViewById(R.id.lv);
        rl = findViewById(R.id.rl);

        //流式布局
        ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = 5;
        lp.rightMargin = 5;
        lp.topMargin = 5;
        lp.bottomMargin = 5;
        for (int i = 0; i < mNames.length; i++) {
            TextView view = new TextView(this);
            view.setText(mNames[i]);
            view.setTextColor(Color.WHITE);
            view.setBackgroundDrawable(getResources().getDrawable(R.drawable.textview_bg));
            fl.addView(view, lp);
            final int finalI = i;
            fl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this, mNames[finalI], Toast.LENGTH_SHORT).show();
                }
            });
        }
        //初始化数据库
        dataBeanDao = MyApplication.getDaoSession().getDataBeanDao();

        initData();
        //这个属性为true表示listview获得当前焦点的时候，相应用户键盘输入的匹配符进行比对，筛选出匹配的
        lv.setTextFilterEnabled(true);

    }
    /**
     * 初始化adapter，更新list，重新加载列表
     */
    private void initData() {
        //查询所有
        list = dataBeanDao.queryBuilder().list();
        //这里用于判断是否有数据
        if (list.size() == 0) {
            rl.setVisibility(View.VISIBLE);
            bt_clear.setVisibility(View.GONE);
        } else {
            rl.setVisibility(View.GONE);
            bt_clear.setVisibility(View.VISIBLE);
        }
        //list倒序排列
        Collections.reverse(list);
        adapter = new SearchAdapter(MainActivity.this, list);
        lv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
//增
    private void initAdd() {
        trim = et_title.getText().toString().trim();
        try {
            if (list.size() < 8) {
                //删除已经存在重复的搜索历史
                List<DataBean> list2 = dataBeanDao.queryBuilder()
                        .where(DataBeanDao.Properties.Title.eq(trim)).build().list();
                dataBeanDao.deleteInTx(list2);
                //添加
                if (!trim.equals(""))
                    dataBeanDao.insert(new DataBean(null, trim));
                Toast.makeText(MainActivity.this, "插入数据成功:" + trim, Toast.LENGTH_SHORT).show();
            } else {
                //删除第一条数据，用于替换最后一条搜索
                dataBeanDao.delete(dataBeanDao.queryBuilder().list().get(0));
                //删除已经存在重复的搜索历史
                List<DataBean> list3 = dataBeanDao.queryBuilder()
                        .where(DataBeanDao.Properties.Title.eq(trim)).build().list();
                dataBeanDao.deleteInTx(list3);
                //添加
                if (!trim.equals(""))
                    dataBeanDao.insert(new DataBean(null, trim));
            }
            initData();
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, "插入失败", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void inject() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.bt_search:
                initAdd();
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                intent.putExtra("keywords",trim);
                intent.putExtra("uid", "2797");
                startActivity(intent);
                break;
            case R.id.bt_clear:
                delectAllDB();
                break;
        }
    }
    //清空数据库
    private void delectAllDB() {
        try {
            dataBeanDao.deleteAll();
            list.clear();
            adapter.notifyDataSetChanged();
            rl.setVisibility(View.VISIBLE);
            bt_clear.setVisibility(View.GONE);
            Toast.makeText(MainActivity.this, "清空数据库", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("exception-----delete", dataBean + "message:" + e.getMessage() + "");
        }
    }

}
