package com.zsp.jmessage;

import android.app.Application;
import android.util.Log;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;

/**
 * Created by Administrator on 2018/7/4 0004.
 */

public class MyApplication extends Application {
    public static String name = "yunhuakeji";
    public static String password = "Yh02368529599";
    private static final String TAG = "MyApplication";

    @Override
    public void onCreate() {
        super.onCreate();
        //Context context 应用程序上下文对象。
//boolean msgRoaming 是否启用消息漫游，true - 启用，false - 关闭。
        JMessageClient.init(this, true);
        //注册
        JMessageClient.register(name, password, new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                Log.d(TAG, "register:code="+i+"--gotResult: "+s);
                //登录
                JMessageClient.login(name, password, new BasicCallback() {
                    @Override
                    public void gotResult(int i, String s) {
                        Log.d(TAG, "login：code="+i+"--gotResult: "+s);
                    }
                });
            }
        });

    }
}
