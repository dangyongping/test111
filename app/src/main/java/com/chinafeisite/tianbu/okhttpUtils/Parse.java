package com.chinafeisite.tianbu.okhttpUtils;

import com.squareup.okhttp.Response;

/**
 * Created by Admin on 2016/4/19.
 */

//解析 接口
public interface Parse {
    String parse(Response response);
}
