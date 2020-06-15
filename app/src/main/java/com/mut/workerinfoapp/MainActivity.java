package com.mut.workerinfoapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.NetworkUtils;
import com.mut.workerinfoapp.Base.BaseActivity;
import com.mut.workerinfoapp.Base.Myapp;
import com.mut.workerinfoapp.Utils.PollingManger;
import com.mut.workerinfoapp.Utils.RetrofitManager;
import com.mut.workerinfoapp.adpater.ClassCountAdapter;
import com.mut.workerinfoapp.adpater.Rightadpter;
import com.mut.workerinfoapp.domain.ClassCount;
import com.mut.workerinfoapp.domain.CountBean;
import com.mut.workerinfoapp.domain.Workerbean;
import com.zhangke.websocket.SimpleListener;
import com.zhangke.websocket.SocketListener;
import com.zhangke.websocket.WebSocketHandler;
import com.zhangke.websocket.WebSocketManager;
import com.zhangke.websocket.response.ErrorResponse;

import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.Enumeration;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import interfaces.heweather.com.interfacesmodule.bean.Code;
import interfaces.heweather.com.interfacesmodule.bean.Lang;
import interfaces.heweather.com.interfacesmodule.bean.Unit;
import interfaces.heweather.com.interfacesmodule.bean.basic.Basic;
import interfaces.heweather.com.interfacesmodule.bean.weather.now.Now;
import interfaces.heweather.com.interfacesmodule.bean.weather.now.NowBase;
import interfaces.heweather.com.interfacesmodule.view.HeWeather;
import lombok.NonNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends BaseActivity {

    private static final String TAG1 ="web socket";
    private static final String TAG_WEATHER = "weather" ;
    private static final String TAG_WORKINFO ="workinfo" ;
    private RecyclerView rv_right;
    Rightadpter mRight_Adpter;
    private ImageView mImgCond;
    private TextView mTvCond;
    private TextView mTvTmp;
    private TextView mTvWindDir;
    private TextView mWindSc;
    private TextView mTvTotal;
    private TextView mTvIn;
    private TextView mTvOut;
    private RecyclerView mRvClasscount;
    ClassCountAdapter mClassCountAdapter;
    private Handler mHandler_workinfo = new Handler();
    private Handler mHandler_weather = new Handler();
    private SocketListener socketListener = new SimpleListener() {
        @Override
        public void onConnected() {
            Log.d(TAG1,"web socket onConnected");
        }

        @Override
        public void onConnectFailed(Throwable e) {
            if (e != null) {
                Log.d(TAG1,"web socket onConnectFailed:" + e.toString());
            } else {
                Log.d(TAG1,"web socket onConnectFailed:null");
            }
        }

        @Override
        public void onDisconnect() {
            Log.d(TAG1,"web socket  onDisconnect");
        }

        @Override
        public void onSendDataError(ErrorResponse errorResponse) {
            Log.d(TAG1,"web socket onSendDataError:" + errorResponse.toString());
            errorResponse.release();
        }

        @Override
        public <T> void onMessage(String message, T data) {
            Log.d(TAG1 ,  "web socket onMessage(String, T):" + message);
            getworkerinfo();
        }

        @Override
        public <T> void onMessage(ByteBuffer bytes, T data) {
            Log.d(TAG1,"web socket onMessage(ByteBuffer, T):" + bytes);
        }
    };
    private WebSocketManager mWebSocketManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mian);
        initView_class_count();
        initView_count();
        initView_weather();
        initView_workerInOut_rv();
        getWeather();
        getworkerinfo();
        Start_Lopper_weather();
        Log.e(TAG, NetworkUtils.getIPAddress(true));
        Log.e(TAG, NetworkUtils.getGatewayByWifi());
        Log.e(TAG, NetworkUtils.getGatewayByWifi());
        Log.e(TAG, NetworkUtils.getServerAddressByWifi());
        Log.e(TAG,NetworkUtils.getBroadcastIpAddress());
        Log.e(TAG,NetworkUtils.getWifiEnabled()+"");
        Log.e(TAG,NetworkUtils.getIpAddressByWifi());


        Start_Lopper_workinfo();

       // start();
        mWebSocketManager = WebSocketHandler.getDefault().addListener(socketListener);

    }

    public void initView_workerInOut_rv() {
        rv_right = (RecyclerView) findViewById(R.id.rv_right);
        rv_right.setLayoutManager(new LinearLayoutManager(this));
        mRight_Adpter = new Rightadpter();
        rv_right.setAdapter(mRight_Adpter);
    }

    public void getworkrinout() {
        Retrofit retrofit = RetrofitManager.getRetrofit();
        API api = retrofit.create(API.class);
        Call<Workerbean> task = api.getworkrinout();

        task.enqueue(new Callback<Workerbean>() {
            @Override
            public void onResponse(Call<Workerbean> call, @NonNull Response<Workerbean> response) {
                if (response.body().getCode()==HttpURLConnection.HTTP_OK) {
                    updateWorkerInOut(response.body());
                    Log.d(TAG_WORKINFO,"最后一名人员--->"+response.body().getData().get( response.body().getData().size()-1).getPersonname());


                }


            }

            @Override
            public void onFailure(Call<Workerbean> call, Throwable t) {
             Log.d(TAG_WORKINFO,"workinfo  workin&out error ---->"+t.toString());
            }
        });
    }

    public void updateWorkerInOut(Workerbean workerbean) {
        mRight_Adpter.setdata(workerbean);
    }

    private void initView_weather() {
        mImgCond = (ImageView) findViewById(R.id.img_cond);
        mTvCond = (TextView) findViewById(R.id.tv_cond);
        mTvTmp = (TextView) findViewById(R.id.tv_tmp);
        mTvWindDir = (TextView) findViewById(R.id.tv_wind_dir);
        mWindSc = (TextView) findViewById(R.id.wind_sc);

    }

    public void getWeather() {
        HeWeather.getWeatherNow(MainActivity.this, "zhenjiang", Lang.CHINESE_SIMPLIFIED, Unit.METRIC, new HeWeather.OnResultWeatherNowBeanListener() {
            @Override
            public void onError(Throwable throwable) {
                Log.d(TAG_WEATHER, "weather  error: " + throwable.toString());

            }

            @Override
            public void onSuccess(Now now) {
                if (Code.OK.getCode().equalsIgnoreCase(now.getStatus())) {
                    //此时返回数据
                    Log.d(TAG_WEATHER, "天气请求成功");
                    updateWeatherView(now);

                } else {
                    //在此查看返回数据失败的原因
                    String status = now.getStatus();
                    Code code = Code.toEnum(status);
                    Log.d(TAG_WEATHER, "failed code: " + code);
                }
            }
        });
    }

    private void updateWeatherView(Now now) {
        NowBase now1 = now.getNow();
        Basic basic = now.getBasic();
        String s = "a" + now1.getCond_code();
        int drawable = MainActivity.this.getApplicationContext().getResources().getIdentifier(s, "drawable", MainActivity.this.getApplicationContext().getPackageName());

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mImgCond.setImageResource(drawable);
                       // Log.d(TAG, "111" + now1.getCond_txt());
                        mTvCond.setText(now1.getCond_txt());
                        mTvTmp.setText(now1.getTmp());
                      //  Log.d(TAG, "111" + now1.getTmp());
                        mTvWindDir.setText(now1.getWind_dir());
                        mWindSc.setText(now1.getWind_sc());

                    }
                });

            }
        });
        thread.start();


    }


    //count view
    private void initView_count() {
        mTvTotal = (TextView) findViewById(R.id.tv_total);
        mTvIn = (TextView) findViewById(R.id.tv_in);
        mTvOut = (TextView) findViewById(R.id.tv_out);
    }

    private void getcount() {
        Retrofit retrofit = RetrofitManager.getRetrofit();
        API api = retrofit.create(API.class);
        Call<CountBean> task = api.getCount();
        task.enqueue(new Callback<CountBean>() {
            @Override
            public void onResponse(Call<CountBean> call, Response<CountBean> response) {
                updateCountView(response.body());
            }

            @Override
            public void onFailure(Call<CountBean> call, Throwable t) {
                Log.d(TAG_WORKINFO, "get count ERROR CODE -----> " + t.toString());
            }
        });

    }

    private void updateCountView(CountBean body) {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (body.getCode() == HttpURLConnection.HTTP_OK) {
                            mTvTotal.setText(body.getData().getTotal());
                            mTvIn.setText(body.getData().getIn());
                            mTvOut.setText(body.getData().getOut());
                        }

                    }
                });

            }
        });
        thread.start();

    }

private void getClassCount()
{
    Retrofit retrofit = RetrofitManager.getRetrofit();
    API api = retrofit.create(API.class);
    Call<ClassCount> task = api.getClassCount();
    task.enqueue(new Callback<ClassCount>() {
        @Override
        public void onResponse(Call<ClassCount> call, Response<ClassCount> response) {
            updateClassCountView(response.body());
        }

        @Override
        public void onFailure(Call<ClassCount> call, Throwable t) {
            Log.d(TAG_WORKINFO, "get class count ERROR CODE -----> " + t.toString());
        }
    });
}

    private void updateClassCountView(@NonNull  ClassCount body) {
        if (body.getCode() ==HttpURLConnection.HTTP_OK) {
            mClassCountAdapter.setData(body);

        }
    }

    private void initView_class_count() {
        mRvClasscount = (RecyclerView) findViewById(R.id.rv_classcount);
        mRvClasscount.setLayoutManager(new LinearLayoutManager(this));
        mClassCountAdapter=new ClassCountAdapter();
        mRvClasscount.setAdapter(mClassCountAdapter);
    }



    public void Start_Lopper_workinfo() {
        // 先清除原来的，防止出现多次开始
        mHandler_workinfo.removeCallbacksAndMessages(null);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // 在这里做轮询的事情
                // doSomeThing();
              //  Log.d("111","lunxun");
             //  getworkerinfo();
                if (mWebSocketManager.isConnect()) {

                }
                else {
                    mWebSocketManager.reconnect();

                }

                // 60秒后下一次轮询开始
                mHandler_workinfo.postDelayed(this, 1 * 1000);
            }
        };
        mHandler_workinfo.postDelayed(runnable, 1 * 1000);
    }

    public void Stop__Lopper_workinfo() {
        mHandler_workinfo.removeCallbacksAndMessages(null);
    }


    public void Start_Lopper_weather() {
        // 先清除原来的，防止出现多次开始
        mHandler_weather.removeCallbacksAndMessages(null);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // 在这里做轮询的事情
                // doSomeThing();
                Log.d(TAG_WEATHER,"weather 轮询");
                // 60秒后下一次轮询开始
                getWeather();
                mHandler_weather.postDelayed(this, 60*60 * 1000);
            }
        };
        mHandler_weather.postDelayed(runnable, 1 * 1000);
    }

    public void Stop__Lopper_weather() {
        mHandler_weather.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onDestroy() {
     Stop__Lopper_weather();
     Stop__Lopper_workinfo();
     mWebSocketManager.removeListener(socketListener);
     super.onDestroy();
    }

    public void getworkerinfo()
    {

        getworkrinout();

        getcount();
        getClassCount();
    }
}
