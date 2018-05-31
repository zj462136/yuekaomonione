package com.example.lenovo.yuekaomonione.ui.search;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.example.lenovo.yuekaomonione.R;
import com.example.lenovo.yuekaomonione.bean.CartsBean;
import com.example.lenovo.yuekaomonione.bean.MessageEvent;
import com.example.lenovo.yuekaomonione.bean.SearchBean;
import com.example.lenovo.yuekaomonione.component.DaggerHttpComponent;
import com.example.lenovo.yuekaomonione.ui.base.BaseActivity;
import com.example.lenovo.yuekaomonione.ui.xiangqing.ShowsActivity;
import com.example.lenovo.yuekaomonione.ui.search.adapter.MySearchAdapter;
import com.example.lenovo.yuekaomonione.ui.search.contract.SearchContract;
import com.example.lenovo.yuekaomonione.ui.search.presenter.SearchPresenter;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends BaseActivity<SearchPresenter> implements SearchContract.View {

    private XRecyclerView xrv;
    private List<SearchBean.DataBean> list=new ArrayList<>();
    private boolean isRefresh = true;
    private int page = 1;
    private MySearchAdapter adapter;
    private String keywords;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        keywords = getIntent().getStringExtra("keywords");
        uid = getIntent().getStringExtra("uid");
        initView();
    }

    private void initView() {
        xrv = findViewById(R.id.xrv);
        xrv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MySearchAdapter(SearchActivity.this, list);
        xrv.setAdapter(adapter);
        mPresenter.searchProducts(keywords,page);
        //设置刷新和加载更多监听
        xrv.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                //刷新
                page = 1;
                isRefresh = true;
                mPresenter.searchProducts(keywords,page);
            }

            @Override
            public void onLoadMore() {
                //加载更多
                page++;
                isRefresh = false;
                mPresenter.searchProducts(keywords,page);
            }
        });
        adapter.setOnItemClickListener(new MySearchAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent(SearchActivity.this, ShowsActivity.class);
                intent.putExtra("uid",uid);
                startActivity(intent);
                MessageEvent messageEvent = new MessageEvent();
                messageEvent.setDataBeans(list);
                EventBus.getDefault().postSticky(messageEvent);
            }

            @Override
            public void onLongClick(int position) {

            }
        });
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_search;
    }

    @Override
    public void inject() {
        DaggerHttpComponent.builder()
                .build()
                .inject(this);
    }

    @Override
    public void onSuccess(List<SearchBean.DataBean> list) {
        if (isRefresh) {
            adapter.refresh(list);
            xrv.refreshComplete();
        } else {
            adapter.loadMore(list);
            int listNum = adapter.getListNum();
            xrv.loadMoreComplete();
            int size = list.size();
            int count = listNum + size;
            if (count >= 20) {
                xrv.setLoadingMoreEnabled(false);
            }
        }
    }

    @Override
    public void onSuccessss(String str) {

    }

    @Override
    public void onCartSuccess(List<CartsBean.DataBean> dataBean) {

    }
}
