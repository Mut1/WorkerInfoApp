package com.mut.workerinfoapp.Utils;

import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

public class PollingManger  {
    private HandlerThread handlerThread = new HandlerThread("PollingThread");
    private Handler handler = new Handler(handlerThread.getLooper());

    private static class INS {
        static PollingManger ins = new PollingManger();
    }

    public PollingManger() {
        handlerThread.start();
    }

    public static PollingManger getInstance() {
        return INS.ins;
    }

    public void start() {
        // 先清除原来的，防止出现多次开始
        handler.removeCallbacksAndMessages(null);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // 在这里做轮询的事情
                // doSomeThing();
                Log.d("111","lunxun");
                // 60秒后下一次轮询开始
                handler.postDelayed(this, 10 * 1000);
            }
        };
        handler.postDelayed(runnable, 60 * 1000);
    }

    public void stop() {
        handler.removeCallbacksAndMessages(null);
    }
}
