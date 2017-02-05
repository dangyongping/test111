package com.chinafeisite.tianbu;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;

/**
 * Created by Admin on 2016/10/20.
 */

public class WifiConnectReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)){

            NetworkInfo networkInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            if (networkInfo.getState().equals( NetworkInfo.State.CONNECTED)){
                //wifi连接才建立推送
                System.out.println("连接了");
                //百度云推送初始化
                PushManager.startWork(context, PushConstants.LOGIN_TYPE_API_KEY,"jGHFP7suVWG627HZh10MAuAi");
            }

        }
    }
}