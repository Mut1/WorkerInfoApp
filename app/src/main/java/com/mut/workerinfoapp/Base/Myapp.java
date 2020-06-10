package com.mut.workerinfoapp.Base;

import android.app.Application;
import android.content.Context;

import interfaces.heweather.com.interfacesmodule.view.HeConfig;

public class Myapp extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        HeConfig.init("HE2006100921421095", "7c53ac50a09b4464bdd575ad9cd603e0");
        HeConfig.switchToFreeServerNode();
    }

    public static Context getInstance() {
        return mContext;
    }

}
