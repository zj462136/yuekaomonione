package com.example.lenovo.yuekaomonione.net;
import com.example.lenovo.yuekaomonione.bean.CartsBean;
import com.example.lenovo.yuekaomonione.bean.MsgBean;
import com.example.lenovo.yuekaomonione.bean.SearchBean;

import java.util.Map;
import io.reactivex.Observable;


public class SearchApi {
    private static SearchApi searchApi;
    private SearchApiService searchApiService;

    private SearchApi(SearchApiService searchApiService) {
        this.searchApiService = searchApiService;
    }

    public static SearchApi getSearchProducts(SearchApiService searchApiService) {
        if (searchApi == null) {
            searchApi = new SearchApi(searchApiService);
        }
        return searchApi;
    }


    public Observable<SearchBean> getSearchProducts(String keywords,int page) {
        return searchApiService.getDuanzi(keywords,page);
    }


    public Observable<MsgBean> getCatagory(String uid, String pid, String token) {
        return searchApiService.addCart(uid, pid, token);
    }

    public Observable<CartsBean> getCartsData(Map<String, String> map) {
       return searchApiService.getCarts(map);
    }
}
