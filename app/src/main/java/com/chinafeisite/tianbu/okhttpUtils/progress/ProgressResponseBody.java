package com.chinafeisite.tianbu.okhttpUtils.progress;

import com.chinafeisite.tianbu.okhttpUtils.listener.ProgressListener;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.ResponseBody;

import java.io.IOException;

import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * Created by Admin on 2016/6/7.
 */
public class ProgressResponseBody extends ResponseBody {

    private ResponseBody responseBody;

    private ProgressListener listener;

    private BufferedSource bufferedSource;

    public ProgressResponseBody(ResponseBody responseBody, ProgressListener listener) {

        this.responseBody = responseBody;
        this.listener = listener;

    }

    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    @Override
    public long contentLength() throws IOException {
        return responseBody.contentLength();
    }

    @Override
    public BufferedSource source() throws IOException {

        if (bufferedSource == null){

            bufferedSource = Okio.buffer(source(responseBody.source()));

        }

        return bufferedSource;
    }




    private Source source(Source source) {

        return new ForwardingSource(source) {
            long totalBytes = 0;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {


                long byteRead = super.read(sink, byteCount);

                totalBytes += byteRead !=-1?byteRead:0;

                listener.onProgress(totalBytes,byteRead,byteRead == -1);

                return byteRead;
            }
        };

    }
}