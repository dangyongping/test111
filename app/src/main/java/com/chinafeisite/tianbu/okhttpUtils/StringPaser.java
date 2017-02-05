package com.chinafeisite.tianbu.okhttpUtils;

import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by Admin on 2016/4/19.
 */


public class StringPaser implements Parse {
    @Override
    public String parse(Response response) {
        String string = null;
        try {
            string = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return string;
    }
}
