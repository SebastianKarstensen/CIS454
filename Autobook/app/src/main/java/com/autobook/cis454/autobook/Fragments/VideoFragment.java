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
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import com.autobook.cis454.autobook.R;

public class VideoFragment extends Fragment{

    private WebView mWebView;
    private LinearLayout mContentView;
    private FrameLayout mCustomViewContainer;
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

        mContentView = (LinearLayout) rootView.findViewById(R.id.linearlayout);
        mWebView = (WebView) rootView.findViewById(R.id.webView);
        mCustomViewContainer = (FrameLayout) rootView.findViewById(R.id.fullscreen_custom_content);

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setPluginState(WebSettings.PluginState.ON);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);

        mWebView.loadUrl("https://www.youtube.com/watch?v=wZZ7oFKsKzY");
        mWebView.setWebViewClient(new HelloWebViewClient());

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
