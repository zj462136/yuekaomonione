package com.example.lenovo.yuekaomonione.ui.cart;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.yuekaomonione.R;
import com.example.lenovo.yuekaomonione.bean.CartsBean;
import com.example.lenovo.yuekaomonione.bean.MessageEvent;
import com.example.lenovo.yuekaomonione.bean.PriceAndCountEvent;
import com.example.lenovo.yuekaomonione.bean.SearchBean;
import com.example.lenovo.yuekaomonione.component.DaggerHttpComponent;
import com.example.lenovo.yuekaomonione.ui.base.BaseActivity;
import com.example.lenovo.yuekaomonione.ui.search.contract.SearchContract;
import com.example.lenovo.yuekaomonione.ui.search.presenter.SearchPresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartActivity extends BaseActivity<SearchPresenter> implements SearchContract.View, View.OnClickListener {
    private int count = 0;
    private List<CartsBean.DataBean> list = new ArrayList<>();
    private ShopCartExListAdapter adapter;
    private boolean flag = false;
    private int totalCount = 0;
    private double totalPrice = 0.00;
    private String uid;
    private ImageView back;
    /**
     * 购物车
     */
    private TextView title;
    /**
     * 编辑
     */
    private TextView subtitle;
    private LinearLayout topBar;
    private ExpandableListView exListView;
    /**
     * 全选
     */
    private CheckBox allChekbox;
    /**
     * ￥0.00
     */
    private TextView tvTotalPrice;
    /**
     * 结算(0)
     */
    private TextView tvGoToPay;
    private LinearLayout llInfo;
    /**
     * 分享宝贝
     */
    private TextView tvShare;
    /**
     * 移到收藏夹
     */
    private TextView tvSave;
    /**
     * 删除
     */
    private TextView tvDelete;
    private LinearLayout llShar;
    private LinearLayout llCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        EventBus.getDefault().register(this);
        uid = getIntent().getStringExtra("uid");
        initView();
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_cart;
    }

    @Override
    public void inject() {
        DaggerHttpComponent.builder()
                .build()
                .inject(this);
    }

    @Override
    public void onSuccess(List<SearchBean.DataBean> list) {

    }

    @Override
    public void onSuccessss(String str) {

    }

    @Override
    public void onCartSuccess(List<CartsBean.DataBean> dataBean) {
        list.addAll(dataBean);
        adapter.notifyDataSetChanged();
        for (int i = 0; i < adapter.getGroupCount(); i++) {
            exListView.expandGroup(i);
        }
    }

    private void initView() {
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);
        title = (TextView) findViewById(R.id.title);
        subtitle = (TextView) findViewById(R.id.subtitle);
        subtitle.setOnClickListener(this);
        topBar = (LinearLayout) findViewById(R.id.top_bar);
        exListView = (ExpandableListView) findViewById(R.id.exListView);
        allChekbox = (CheckBox) findViewById(R.id.all_chekbox);
        allChekbox.setOnClickListener(this);
        tvTotalPrice = (TextView) findViewById(R.id.tv_total_price);
        tvTotalPrice.setOnClickListener(this);
        tvGoToPay = (TextView) findViewById(R.id.tv_go_to_pay);
        tvGoToPay.setOnClickListener(this);
        llInfo = (LinearLayout) findViewById(R.id.ll_info);
        tvShare = (TextView) findViewById(R.id.tv_share);
        tvShare.setOnClickListener(this);
        tvSave = (TextView) findViewById(R.id.tv_save);
        tvSave.setOnClickListener(this);
        tvDelete = (TextView) findViewById(R.id.tv_delete);
        tvDelete.setOnClickListener(this);
        llShar = (LinearLayout) findViewById(R.id.ll_shar);
        llCart = (LinearLayout) findViewById(R.id.ll_cart);

        Map<String, String> map = new HashMap<>();
        map.put("uid", uid);
        map.put("source", "android");
        mPresenter.getCartsData(map);
        adapter = new ShopCartExListAdapter(this, list);
        exListView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.all_chekbox:
                adapter.changeAllListCbState(allChekbox.isChecked());
                break;
            case R.id.tv_share:
                Toast.makeText(this, "分享成功", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_save:
                Toast.makeText(this, "加入成功", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_delete:
                AlertDialog dialog;
                if (totalCount == 0) {
                    Toast.makeText(CartActivity.this, "请选择要删除的商品", Toast.LENGTH_SHORT).show();
                    return;
                }
                dialog = new AlertDialog.Builder(CartActivity.this).create();
                dialog.setTitle("操作提示");
                dialog.setMessage("您确定要将这些商品从购物车中移除吗？");
                dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        return;
                    }
                });
                dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        delete();
                        Toast.makeText(CartActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.show();
                break;
            case R.id.subtitle:
                flag = !flag;
                if (flag) {
                    subtitle.setText("完成");
                    llShar.setVisibility(View.VISIBLE);
                    llInfo.setVisibility(View.GONE);
                } else {
                    subtitle.setText("编辑");
                    llShar.setVisibility(View.GONE);
                    llInfo.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.tv_go_to_pay:
                AlertDialog alert;
                if (totalCount == 0) {
                    Toast.makeText(this, "请选择要支付的商品", Toast.LENGTH_LONG).show();
                    return;
                }
                alert = new AlertDialog.Builder(this).create();
                alert.setTitle("操作提示");
                alert.setMessage("总计:\n" + totalCount + "种商品\n" + totalPrice + "元");
                alert.setButton(DialogInterface.BUTTON_NEGATIVE, "取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        });
                alert.setButton(DialogInterface.BUTTON_POSITIVE, "确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(CartActivity.this,"订单",Toast.LENGTH_SHORT).show();
                            }
                        });
                alert.show();
                break;
            case R.id.back:
                finish();
                break;
        }
    }
    public void delete() {

        for (int i = 0; i < list.size(); i++) {
            List<CartsBean.DataBean.ListBean> listbean = list.get(i).getList();
            for (int j = 0; j < listbean.size(); j++) {
                CartsBean.DataBean.ListBean listBean = listbean.get(j);
                if (listBean.isChecked()) {
                    listbean.remove(j);
                    j--;
                    if (listbean.size() == 0) {
                        list.remove(i);
                        i--;
                    }
                }
            }
        }
        adapter.notifyDataSetChanged();
    }
    @Subscribe
    public void onMessageEvent(MessageEvent event) {
        allChekbox.setChecked(event.isChecked());
    }
    @Subscribe
    public void onMessageEvent(PriceAndCountEvent event) {
        tvGoToPay.setText("结算(" + event.getCount() + ")");
        tvTotalPrice.setText("￥" + event.getPrice());
        totalCount = event.getCount();
        totalPrice = event.getPrice();
    }
}
