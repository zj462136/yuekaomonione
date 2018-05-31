package com.example.lenovo.yuekaomonione.module;

import com.example.lenovo.yuekaomonione.net.Api;
import com.example.lenovo.yuekaomonione.net.MyInterceptor;
import com.example.lenovo.yuekaomonione.net.SearchApi;
import com.example.lenovo.yuekaomonione.net.SearchApiService;

import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class HttpModule {
    @Provides
    OkHttpClient.Builder provideOkHttpClientBuilder() {
        return new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS);
    }


    @Provides
    SearchApi provideSearchApi(OkHttpClient.Builder builder) {
//        builder.addInterceptor(new MyInterceptor());
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(builder.build())
                .build();
        SearchApiService duanziApiService = retrofit.create(SearchApiService.class);
        return SearchApi.getSearchProducts(duanziApiService);
    }
}
