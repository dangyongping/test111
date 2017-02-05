package com.chinafeisite.tianbu;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

/*
 *  @项目名：  RK3188-BP 
 *  @包名：    com.chinafeisite.tianbu
 *  @文件名:   WebViewActivity
 *  @创建者:   Administrator
 *  @创建时间:  2017/1/4 15:32
 *  @描述：    TODO
 */
public class WebViewActivity extends Activity {
    private static final String TAG = "WebViewActivity";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
init();
    }

    private void init() {
      Button  web_back = (Button) findViewById(R.id.web_back);
        web_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
      WebView webView  = (WebView) findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);

        //webView.getSettings().setPluginsEnabled(true);

        //webView.getSettings().setPluginState(PluginState.ON);

        webView.setVisibility(View.VISIBLE);

        webView.getSettings().setUseWideViewPort(true);

        webView.loadUrl("http://7xqi17.com1.z0.glb.clouddn.com/BM10KM3.mp4");


    }
}
