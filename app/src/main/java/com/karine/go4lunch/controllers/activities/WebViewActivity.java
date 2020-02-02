package com.karine.go4lunch.controllers.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.karine.go4lunch.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import models.PlaceDetailsAPI.PlaceDetailsResult;

public class WebViewActivity extends AppCompatActivity {

    @BindView(R.id.webView)
    WebView mWebView;
    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        ButterKnife.bind(this);

        String url = getIntent().getStringExtra("website");
        MyWebViewClient myWebViewClient = new MyWebViewClient();
        myWebViewClient.shouldOverrideUrlLoading(mWebView, url);
        //for progressBar
        mWebView.setWebChromeClient(new WebChromeClient() {

            public void onProgressChanged(WebView view, int progress) {
                mProgressBar.setProgress(progress);
                if (progress == 100) {
                    mProgressBar.setVisibility(View.GONE);
                } else {
                    mProgressBar.setVisibility(View.VISIBLE);
                }
            }
        });

        //For Hide Action Bar
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();

    }

    /**
     * For return with arrow endPage
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.mWebView.destroy();
    }

    private class MyWebViewClient extends WebViewClient {
        @SuppressLint("SetJavaScriptEnabled")
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url != null) {
                view.loadUrl(url);
                WebSettings webSettings = mWebView.getSettings();
                // Access to dom to avoid the bug, in the webview activity
                webSettings.setDomStorageEnabled(true);
//             Enable Javascript
                webSettings.setJavaScriptEnabled(true);

                // Force links and redirects to open in the WebView instead of in a browser
                mWebView.setWebViewClient(new WebViewClient());


            }
            return true;
        }
    }
}
