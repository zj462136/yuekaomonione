package com.example.lenovo.yuekaomonione.ui.search.contract;

import com.example.lenovo.yuekaomonione.bean.CartsBean;
import com.example.lenovo.yuekaomonione.bean.SearchBean;
import com.example.lenovo.yuekaomonione.ui.base.BaseContract;

import java.util.List;
import java.util.Map;

public interface SearchContract {
    interface View extends BaseContract.BaseView {
        void onSuccess(List<SearchBean.DataBean> list);
        void onSuccessss(String str);
        void onCartSuccess(List<CartsBean.DataBean> dataBean);
    }

    interface Presenter extends BaseContract.BasePresenter<View> {
        void searchProducts(String keywords,int page);
        void addCart(String uid, String pid, String token);
        void getCartsData(Map<String, String> map);
    }
}
