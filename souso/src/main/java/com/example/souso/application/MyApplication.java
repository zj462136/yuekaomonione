package com.example.souso.application;

import android.app.Application;

import com.example.souso.dao.DaoMaster;
import com.example.souso.dao.DaoSession;

public class MyApplication extends Application {

    private static DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(this, "baway1.db");
        DaoMaster daoMaster = new DaoMaster(devOpenHelper.getWritableDb());
        daoSession = daoMaster.newSession();
    }
    public static DaoSession getDaoSession(){
        return daoSession;
    }
}
