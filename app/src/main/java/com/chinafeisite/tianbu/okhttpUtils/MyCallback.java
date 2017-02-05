package com.chinafeisite.tianbu.okhttpUtils;

/**
 * Created by Admin on 2016/4/19.
 */

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.lang.ref.WeakReference;


/**
 * 网络访问 回调
 */
public class MyCallback implements Callback {




    private static final int SUCESSFUL = 0;
    private static final int FAILED = 1;


    //response解析
    private StringPaser parse;

    private Handler handle = new UIhandler(this);

    public MyCallback(StringPaser parse ) {
        this.parse = parse;
    }

    //访问失败  --子线程
    @Override
    public void onFailure(Request request, IOException e) {
        handle.sendEmptyMessage(FAILED);
    }

    //访问成功  --子线程
    @Override
    public void onResponse(Response response) throws IOException {
        String result = parse.parse(response);
        Message message = Message.obtain();
        message.obj = result;
        message.what = SUCESSFUL;
        handle.sendMessage(message);
    }

    static class UIhandler extends Handler {

        private WeakReference weakReference ;

        UIhandler(MyCallback myCallback){
            super(Looper.getMainLooper());
            weakReference = new WeakReference(myCallback);
        }

        @Override
        public void handleMessage(Message msg) {

            MyCallback myCallback = (MyCallback) weakReference.get();

            if (myCallback==null) return;
            switch (msg.what){
                case SUCESSFUL:

                    String string = (String) msg.obj;

                    if (string==null) return;
                    //回调

                    myCallback.onSuccess(string);
                    break;

                case FAILED:

                    myCallback.onFailed();
                    break;
            }
            super.handleMessage(msg);
        }
    }


    /**
     *  成功 --UI线程 工作
     * @param string
     */
    public void onSuccess(String string){

    }

    /**
     * 失败 --UI线程
     */
    public void onFailed(){

    }

}
