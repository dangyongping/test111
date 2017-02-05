package com.chinafeisite.tianbu.okhttpUtils.listener;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;


import com.chinafeisite.tianbu.okhttpUtils.impl.ProgressModel;

import java.lang.ref.WeakReference;

/**
 * Created by Admin on 2016/6/7.
 */
public abstract class UIProgressResponseListener implements ProgressListener {

    ProgressModel progressModel;
    private static final int RESPONSE_UPDATE = 0;

    private static class UIhandler extends Handler {

        private WeakReference<UIProgressResponseListener> mWeakReference;


        public UIhandler(Looper looper, UIProgressResponseListener listener) {
            super(looper);

            mWeakReference = new WeakReference<UIProgressResponseListener>(listener);

        }


        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {

                case RESPONSE_UPDATE:
                    UIProgressResponseListener listener = mWeakReference.get();

                    if (listener != null) {

                        ProgressModel progressModel = (ProgressModel) msg.obj;
                        listener.onUIprogress(progressModel.getContentLength(), progressModel.getCurrentBytes(), progressModel.isDone());

                    }

                    break;

                default:
                    super.handleMessage(msg);
                    break;
            }

        }
    }


    private UIhandler handler = new UIhandler(Looper.getMainLooper(), this);


    @Override
    public void onProgress(long totalBytes, long currentBytes, boolean isDone) {

        Message message = Message.obtain();

        if (progressModel == null) {

            System.out.println("progressModel");

            progressModel = new ProgressModel(currentBytes, totalBytes, isDone);
        }


        progressModel.setContentLength(totalBytes);
        progressModel.setCurrentBytes(currentBytes);
        progressModel.setDone(isDone);


        message.obj = progressModel;

        message.what = RESPONSE_UPDATE;

        handler.sendMessage(message);

    }


    public abstract void onUIprogress(long totalBytes, long currentBytes, boolean isDone);
}
