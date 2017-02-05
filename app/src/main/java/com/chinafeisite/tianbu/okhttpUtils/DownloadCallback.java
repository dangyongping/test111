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
public class DownloadCallback implements Callback {




    private static final int SUCESSFUL = 0;
    private static final int FAILED = 1;

    //下载的文件名字
    private String fileName;

    private Handler handle = new UIhandler(this);

    public DownloadCallback( String fileName) {
        this.fileName = fileName;
        System.out.println(fileName);
    }

    //访问失败  --子线程
    @Override
    public void onFailure(Request request, IOException e) {
        handle.sendEmptyMessage(FAILED);
    }

    //访问成功  --子线程
    @Override
    public void onResponse(Response response) throws IOException {
/*
        InputStream is = null;

        byte[] buf = new byte[1024];

        int len = 0;

        FileOutputStream fos = null;

        String path = MainActivity.memoryPath;

        try {
            is = response.body().byteStream();

            long total = response.body().contentLength();

            File file = new File(path,fileName);

            System.out.println(path);

            fos = new FileOutputStream(file);


            long sum = 0;

            while ((len = is.read(buf))!=-1){

                fos.write(buf,0,len);

                sum = sum+len;
                int progress = (int) (sum * 1.0f / total * 100);
                Message msg = handle.obtainMessage();
                msg.what = SUCESSFUL;
                msg.arg1 = progress;
                handle.sendMessage(msg);

            }

            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null)
                    is.close();
            } catch (IOException e) {
            }
            try {
                if (fos != null)
                    fos.close();
            } catch (IOException e) {
            }
        }*/

    }

    static class UIhandler extends Handler {

        private WeakReference weakReference ;

        UIhandler(DownloadCallback myCallback){
            super(Looper.getMainLooper());
            weakReference = new WeakReference(myCallback);
        }

        @Override
        public void handleMessage(Message msg) {

            DownloadCallback myCallback = (DownloadCallback) weakReference.get();

            if (myCallback==null) return;
            switch (msg.what){
                case SUCESSFUL:

                    int progress =  msg.arg1;

                    //回调
                   myCallback.onSuccess(progress);

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
     * @param progress
     */
    public void onSuccess(int progress ){

    }

    /**
     * 失败 --UI线程
     */
    public void onFailed(){

    }

}
