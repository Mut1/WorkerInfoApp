package com.mut.workerinfoapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.NetworkUtils;
import com.mut.workerinfoapp.Base.BaseActivity;
import com.mut.workerinfoapp.Utils.RetrofitManager;
import com.mut.workerinfoapp.adpater.ClassCountAdapter;
import com.mut.workerinfoapp.adpater.Rightadpter;
import com.mut.workerinfoapp.domain.CountBean;
import com.mut.workerinfoapp.domain.Workerbean;

import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends BaseActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mian);
        initView_class_count();
        initView_count();
        initView_weather();
        initView_workerInOut_rv();
        getworkrinout();

        Log.e(TAG, NetworkUtils.getIPAddress(true));
        Log.e(TAG, NetworkUtils.getGatewayByWifi());
        Log.e(TAG, NetworkUtils.getGatewayByWifi());
        Log.e(TAG, NetworkUtils.getServerAddressByWifi());

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
            public void onResponse(Call<Workerbean> call, Response<Workerbean> response) {
                updateWorkerInOut(response.body());
            }

            @Override
            public void onFailure(Call<Workerbean> call, Throwable t) {

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
        getWeather();

    }

    public void getWeather() {
        HeWeather.getWeatherNow(MainActivity.this, "CN101010100", Lang.CHINESE_SIMPLIFIED, Unit.METRIC, new HeWeather.OnResultWeatherNowBeanListener() {
            @Override
            public void onError(Throwable throwable) {
                Log.d(TAG, "weather  error: " + throwable.toString());

            }

            @Override
            public void onSuccess(Now now) {
                if (Code.OK.getCode().equalsIgnoreCase(now.getStatus())) {
                    //此时返回数据
                    Log.d(TAG, "天气返回chengg");
                    updateWeatherView(now);

                } else {
                    //在此查看返回数据失败的原因
                    String status = now.getStatus();
                    Code code = Code.toEnum(status);
                    Log.d(TAG, "failed code: " + code);
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
                        Log.d(TAG, "111" + now1.getCond_txt());
                        mTvCond.setText(now1.getCond_txt());
                        mTvTmp.setText(now1.getTmp());
                        Log.d(TAG, "111" + now1.getTmp());
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
        getcount();
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
                Log.d(TAG, "ERROR CODE -----> " + t.toString());
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


    private void initView_class_count() {
        mRvClasscount = (RecyclerView) findViewById(R.id.rv_classcount);
        mRvClasscount.setLayoutManager(new LinearLayoutManager(this));
        mClassCountAdapter=new ClassCountAdapter();
        mRvClasscount.setAdapter(mClassCountAdapter);
    }



}
