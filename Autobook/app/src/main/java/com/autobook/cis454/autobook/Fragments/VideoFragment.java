package com.autobook.cis454.autobook.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.autobook.cis454.autobook.R;

//This class describes a fragment which displays a hardcoded URL.
public class VideoFragment extends Fragment{

    private View mCustomView;
    private WebChromeClient.CustomViewCallback mCustomViewCallback;
    FrameLayout.LayoutParams COVER_SCREEN_GRAVITY_CENTER = new FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER);

    private WebChromeClient mWebChromeClient;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_video, container, false);

        //Find and save layout entities
        LinearLayout mContentView = (LinearLayout) rootView.findViewById(R.id.linearlayout);
        WebView mWebView = (WebView) rootView.findViewById(R.id.webView);
        FrameLayout mCustomViewContainer = (FrameLayout) rootView.findViewById(R.id.fullscreen_custom_content);

        //Setup web settings
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setPluginState(WebSettings.PluginState.ON);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);

        //Set the url to our tutorial video
        mWebView.loadUrl("https://www.youtube.com/watch?v=IHuEwSJT3K8");
        //mWebView.setWebViewClient(new HelloWebViewClient());
        mWebView.setWebChromeClient(new WebChromeClient());

        return rootView;
    }


    private class HelloWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView webview, String url)
        {
            webview.setWebChromeClient(mWebChromeClient);
            webview.loadUrl(url);

            return true;
        }
    }




}