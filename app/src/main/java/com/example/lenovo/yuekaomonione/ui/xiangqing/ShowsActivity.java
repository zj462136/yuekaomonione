package com.example.lenovo.yuekaomonione.ui.xiangqing;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.lenovo.yuekaomonione.R;
import com.example.lenovo.yuekaomonione.bean.CartsBean;
import com.example.lenovo.yuekaomonione.bean.MessageEvent;
import com.example.lenovo.yuekaomonione.bean.SearchBean;
import com.example.lenovo.yuekaomonione.component.DaggerHttpComponent;
import com.example.lenovo.yuekaomonione.net.GlideImageLoader;
import com.example.lenovo.yuekaomonione.ui.base.BaseActivity;
import com.example.lenovo.yuekaomonione.ui.cart.CartActivity;
import com.example.lenovo.yuekaomonione.ui.search.contract.SearchContract;
import com.example.lenovo.yuekaomonione.ui.search.presenter.SearchPresenter;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

public class ShowsActivity extends BaseActivity<SearchPresenter> implements SearchContract.View {

    private ImageView detail_image_back;
    private ImageView detail_share;
    private RelativeLayout detai_relative;
    private JCVideoPlayerStandard js;
    private Banner banner;
    private TextView detail_title;
    /**
     * aaaa
     */
    private TextView detail_yuan_price;
    private TextView detail_bargin_price;
    /**
     * 购物车
     */
    private TextView watch_cart;
    /**
     * 加入购物车
     */
    private TextView detai_add_cart;
    private JCVideoPlayerStandard jcVideoPlayerStandard;
    String url = "http://ips.ifeng.com/video19.ifeng.com/video09/2014/06/16/1989823-102-086-0009.mp4";
    private String images;
    private String uid;
    private String token="FAA93C3CA1A691ACB8ECC730D929D36B";
    private int pid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shows);
        uid = getIntent().getStringExtra("uid");
        initView();
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onMessageEvent(final MessageEvent event) {
        /* Do something */
        List<SearchBean.DataBean> dataBeans = event.getDataBeans();

        if (dataBeans != null) {
            //添加删除线
            for (int j = 0; j < dataBeans.size(); j++) {
                images = dataBeans.get(j).getImages();
                pid = dataBeans.get(j).getPid();
                detail_yuan_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                //设置数据显示
                detail_title.setText(dataBeans.get(j).getTitle());
                detail_bargin_price.setText("优惠价:" + dataBeans.get(j).getBargainPrice());
                detail_yuan_price.setText("原价:" + dataBeans.get(j).getPrice());
                String[] stringss = dataBeans.get(j).getImages().split("\\|");
                final ArrayList<String> imageUrls = new ArrayList<>();
                for (int i = 0; i < stringss.length; i++) {
                    imageUrls.add(stringss[i]);
                }
                banner.setImages(imageUrls);
                //bannner点击事件进行跳转
                banner.start();
            }
        }
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_shows;
    }

    @Override
    public void inject() {
        DaggerHttpComponent.builder()
                .build()
                .inject(this);
    }

    private void initView() {
        detail_image_back = (ImageView) findViewById(R.id.detail_image_back);
        detail_share = (ImageView) findViewById(R.id.detail_share);
        detai_relative = (RelativeLayout) findViewById(R.id.detai_relative);
        js = (JCVideoPlayerStandard) findViewById(R.id.js);
        banner = (Banner) findViewById(R.id.banner);
        detail_title = (TextView) findViewById(R.id.detail_title);
        detail_yuan_price = (TextView) findViewById(R.id.detail_yuan_price);
        detail_bargin_price = (TextView) findViewById(R.id.detail_bargin_price);
        watch_cart = (TextView) findViewById(R.id.watch_cart);
        detai_add_cart = (TextView) findViewById(R.id.detai_add_cart);
        initBanner();
        detail_image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        watch_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ShowsActivity.this, "跳转购物车页面", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ShowsActivity.this, CartActivity.class);
                intent.putExtra("uid",uid);
                startActivity(intent);
            }
        });
        detai_add_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.addCart(uid, pid + "", token);
                Toast.makeText(ShowsActivity.this, "加入购物车", Toast.LENGTH_SHORT).show();

            }
        });
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                Intent intent = new Intent(ShowsActivity.this, BannerDetailsActivity.class);
                intent.putExtra("imgs", images);
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });
        //找控件
        jcVideoPlayerStandard = (JCVideoPlayerStandard) findViewById(R.id.js);
        //设置视频上显示的文字
        jcVideoPlayerStandard.setUp(url, JCVideoPlayer.CURRENT_STATE_NORMAL,"舌尖上的太阳系");
        Glide.with(getApplicationContext()).load("http://p.qpic.cn/videoyun/0/2449_43b6f696980311e59ed467f22794e792_1/640") .into(jcVideoPlayerStandard.thumbImageView);
    }
    private void initBanner() {
        //设置banner样式...CIRCLE_INDICATOR_TITLE包含标题
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(2500);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.CENTER);
    }
    @Override
    public void onBackPressed() {//被按下
        if (JCVideoPlayer.backPress()){
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {//暂停
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }
    //在onDestory()方法中取消订阅：防止内存溢出
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().removeAllStickyEvents();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onSuccess(List<SearchBean.DataBean> list) {

    }

    @Override
    public void onSuccessss(String str) {
        Toast.makeText(ShowsActivity.this, str, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCartSuccess(List<CartsBean.DataBean> dataBean) {

    }
}
