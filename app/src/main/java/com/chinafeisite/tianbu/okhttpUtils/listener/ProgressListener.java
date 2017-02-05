package com.chinafeisite.tianbu.okhttpUtils.listener;

/**
 * Created by Admin on 2016/6/7.
 */
public interface ProgressListener {

    void onProgress(long totalBytes, long currentBytes, boolean isDone);
}
