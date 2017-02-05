package com.chinafeisite.tianbu.okhttpUtils;

import com.chinafeisite.tianbu.okhttpUtils.listener.UIProgressResponseListener;
import com.chinafeisite.tianbu.okhttpUtils.progress.ProgressResponseBody;
import com.squareup.okhttp.CacheControl;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;


public class OkhttpTool {


    public static OkHttpClient client;

    static {
        client = new OkHttpClient();
        client.setConnectTimeout(20, TimeUnit.SECONDS);
        client.setReadTimeout(1000, TimeUnit.SECONDS);
        client.setWriteTimeout(1000, TimeUnit.SECONDS);

//        Cache cache = new Cache(MainActivity.cacheDirectory, 10 * 1024 * 1024);
//        client.setCache(cache);
    }


    public static void doget(String url, MyCallback myCallback) {
        if (client == null) {
            client = new OkHttpClient();
        }

        Request request = new Request.Builder().header("Cache-Control", "CACHED_ELSE_NETWORK").url(url).build();
        client.newCall(request).enqueue(myCallback);
    }


    public static void dopost(String url, MyCallback myCallback, File file, String user_name, String nick_name, String age, String sex, String height, String weight) {

        MediaType mediaType = MediaType.parse("image/png");

        MultipartBuilder builder = new MultipartBuilder().type(MultipartBuilder.FORM);

        if (file != null) {
            builder.addFormDataPart("logo", file.getName(), RequestBody.create(mediaType, file));
        }
        builder.addFormDataPart("user_name", user_name);
        builder.addFormDataPart("age", age);
        builder.addFormDataPart("name", nick_name);
        builder.addFormDataPart("sex", sex);
        builder.addFormDataPart("height", height);
        builder.addFormDataPart("weight", weight);

        RequestBody requestBody = builder.build();

        Request request = new Request.Builder().url(url).post(requestBody).build();

        client.newCall(request).enqueue(myCallback);
    }

    public static void send(String url, File file, String user_name, String mood, MyCallback myCallback) {

        MediaType mediaType = MediaType.parse("image/png");

        MultipartBuilder builder = new MultipartBuilder().type(MultipartBuilder.FORM);

        if (file != null) {
            builder.addFormDataPart("picture", file.getName(), RequestBody.create(mediaType, file));
        }
        builder.addFormDataPart("user_name", user_name);
        builder.addFormDataPart("mood", mood);

        RequestBody requestBody = builder.build();

        Request request = new Request.Builder().url(url).post(requestBody).build();

        client.newCall(request).enqueue(myCallback);
    }


    public static void addProgressResponseListener(String url, final UIProgressResponseListener listener, MyCallback myCallback) {

        client.networkInterceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response response = chain.proceed(chain.request());

                return response.newBuilder().body(new ProgressResponseBody(response.body(), listener)).build();
            }
        });


        Request request = new Request.Builder().url(url).build();

        try {
            client.newCall(request).enqueue(myCallback);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //拦截器
    private static final Interceptor interceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();

            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();


            Response response = chain.proceed(request);
//            if (isNetworkReachable(mContext)) {
//                int maxAge = 1 * 60; // read from cache for 1 minute
//                response.newBuilder()
//                        .removeHeader("Pragma")
//                        .header("Cache-Control", "public, max-age=" + maxAge)
//                        .build();
//
//            } else {
            int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale
            response.newBuilder()
                    .removeHeader("Pragma")
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                    .build();

            return response;
        }
    };

    public static void download(String url, DownloadCallback downloadCallback){

        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(downloadCallback);

    }
}