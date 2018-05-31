package com.example.lenovo.yuekaomonione.ui.search.presenter;
import com.example.lenovo.yuekaomonione.bean.CartsBean;
import com.example.lenovo.yuekaomonione.bean.MsgBean;
import com.example.lenovo.yuekaomonione.bean.SearchBean;
import com.example.lenovo.yuekaomonione.net.SearchApi;
import com.example.lenovo.yuekaomonione.ui.base.BasePresenter;
import com.example.lenovo.yuekaomonione.ui.search.contract.SearchContract;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class SearchPresenter extends BasePresenter<SearchContract.View> implements SearchContract.Presenter{
    private SearchApi searchApi;

    @Inject
    public SearchPresenter(SearchApi searchApi) {
        this.searchApi = searchApi;
    }

    @Override
    public void searchProducts(String keywords, int page) {
        searchApi.getSearchProducts(keywords,page)
                .subscribeOn(Schedulers.io())
                .map(new Function<SearchBean, List<SearchBean.DataBean>>() {
                    @Override
                    public List<SearchBean.DataBean> apply(SearchBean duanziBean) throws Exception {
                        return duanziBean.getData();
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<SearchBean.DataBean>>() {
                    @Override
                    public void accept(List<SearchBean.DataBean> dataBeans) throws Exception {
                        mView.onSuccess(dataBeans);
                    }
                });
    }


    @Override
    public void addCart(String uid, String pid, String token) {
        searchApi.getCatagory(uid, pid, token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<MsgBean, String>() {
                    @Override
                    public String apply(MsgBean msgBean) throws Exception {
                        return msgBean.getMsg();
                    }
                }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                if (mView != null) {
                    mView.onSuccessss(s);
                }
            }
        });
    }
    @Override
    public void getCartsData(Map<String, String> map) {
        searchApi.getCartsData(map)
                .subscribeOn(Schedulers.io())
                .map(new Function<CartsBean, List<CartsBean.DataBean>>() {
                    @Override
                    public List<CartsBean.DataBean> apply(CartsBean duanziBean) throws Exception {
                        return duanziBean.getData();
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<CartsBean.DataBean>>() {
                    @Override
                    public void accept(List<CartsBean.DataBean> dataBeans) throws Exception {
                        mView.onCartSuccess(dataBeans);
                    }
                });
    }
}
