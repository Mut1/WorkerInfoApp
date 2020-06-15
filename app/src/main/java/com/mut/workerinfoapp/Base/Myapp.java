package com.mut.workerinfoapp.Base;

import android.app.Application;
import android.content.Context;

import com.zhangke.websocket.WebSocketHandler;
import com.zhangke.websocket.WebSocketManager;
import com.zhangke.websocket.WebSocketSetting;

import interfaces.heweather.com.interfacesmodule.view.HeConfig;

public class Myapp extends Application {
    private static Context mContext;
   public static  WebSocketSetting setting;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        HeConfig.init("HE2006100921421095", "7c53ac50a09b4464bdd575ad9cd603e0");
        HeConfig.switchToFreeServerNode();


        setting = new WebSocketSetting();
        setting.setConnectUrl("ws://192.168.1.102:8080/websocket");
        //设置连接超时时间
        setting.setConnectTimeout(10 * 1000);

//设置心跳间隔时间
        setting.setConnectionLostTimeout(60);

//设置断开后的重连次数，可以设置的很大，不会有什么性能上的影响
        setting.setReconnectFrequency(40);

//设置 Headers

//网络状态发生变化后是否重连，
        WebSocketHandler.registerNetworkChangedReceiver(getApplicationContext());
        setting.setReconnectWithNetworkChanged(true);
        //通过 init 方法初始化默认的 WebSocketManager 对象
        WebSocketManager manager = WebSocketHandler.init(Myapp.setting);
//启动连接
        manager.start();
    }

    public static Context getInstance() {
        return mContext;
    }

}
