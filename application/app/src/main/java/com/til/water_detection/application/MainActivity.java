package com.til.water_detection.application;

import android.annotation.SuppressLint;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import static com.til.water_detection.application.R.*;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.createWebView();
    }

    /* 创建 WebView 实例 */
    @SuppressLint("SetJavaScriptEnabled")
    private void createWebView() {
        final WebView webView = findViewById(id.webView);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());

        String url = "https://113.219.237.106:42481";
        webView.loadUrl(url);
    }




}