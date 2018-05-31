package com.example.lenovo.yuekaomonione.net;

import com.example.lenovo.yuekaomonione.bean.CartsBean;
import com.example.lenovo.yuekaomonione.bean.MsgBean;
import com.example.lenovo.yuekaomonione.bean.SearchBean;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public interface SearchApiService {

    @FormUrlEncoded
    @POST("product/searchProducts")
    Observable<SearchBean> getDuanzi(@Field("keywords") String keywords, @Field("page") int page);

    @FormUrlEncoded
    @POST("product/addCart")
    Observable<MsgBean> addCart(@Field("Uid") String uid,
                                @Field("Pid") String pid,
                                @Field("Token") String token);
    @GET("product/getCarts")
    Observable<CartsBean> getCarts(@QueryMap Map<String, String> map);
}
