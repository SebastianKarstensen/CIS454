package com.autobook.cis454.autobook.Fragments;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.autobook.cis454.autobook.R;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

/**
 * Created by Sebastian on 02-03-2015.
 */
public class TwitterWebFragment extends Fragment {

    Twitter twitter;
    private String oauth_url, oauth_verifier;
    private RequestToken requestToken;
    View rootView;
    WebView web;
    private AccessToken accessToken;
    private SharedPreferences sharedPreferences;

    static String TWITTER_CONSUMER_KEY = "ljTAGW8VXet3qGw08JQ8yjhVJ";
    static String TWITTER_CONSUMER_SECRET = "ShPObDg7rPohIqXTVhs2BxArrao2kbwMEj4koGZrge9Xx58KQt";

    public static String PREF_KEY_OAUTH_TOKEN = "oauth_token";
    public static String PREF_KEY_OAUTH_SECRET = "oauth_token_secret";
    static String PREF_KEY_TWITTER_LOGIN = "isTwitterLoggedIn";


    public TwitterWebFragment() {
        twitter = new TwitterFactory().getInstance();
        twitter.setOAuthConsumer(TWITTER_CONSUMER_KEY, TWITTER_CONSUMER_SECRET);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_twitter_webview, container, false);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext()); //.getSharedPreferences("MyPref", 0);

        new TokenGet().execute();

        return rootView;
    }

    private class TokenGet extends AsyncTask<String, String, String> {

        ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(getActivity());
            progress.setMessage("Loading page ...");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.show();
        }

        @Override
        protected String doInBackground(String... args) {
            try {
                requestToken = twitter.getOAuthRequestToken();
                oauth_url = requestToken.getAuthorizationURL();
            } catch (TwitterException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return oauth_url;
        }

        @Override
        protected void onPostExecute(String oauth_url) {
            web = (WebView) rootView.findViewById(R.id.webView_twitter_login);
            web.loadUrl(oauth_url);
            web.setWebViewClient(new WebViewClient() {
                boolean authComplete = false;
                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon){
                    super.onPageStarted(view, url, favicon);
                    progress.dismiss();
                }
                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    if (url.contains("oauth_verifier") && authComplete == false){
                        authComplete = true;
                        Log.e("Url", url);
                        Uri uri = Uri.parse(url);
                        oauth_verifier = uri.getQueryParameter("oauth_verifier");
                        new AccessTokenGet().execute();
                    }else if(url.contains("denied")){
                        //If it doesn't succeed
                        Toast.makeText(getActivity(), "Sorry !, Permission Denied", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private class AccessTokenGet extends AsyncTask<String, String, Boolean> {

        ProgressDialog progress;
        User user = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(getActivity());
            progress.setMessage("Fetching authentication ...");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setIndeterminate(true);
            progress.show();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                // Get the access token
                accessToken = twitter.getOAuthAccessToken(
                        requestToken, oauth_verifier);

                SharedPreferences.Editor e = sharedPreferences.edit();

                // After getting access token & access token secret,
                // store them in application preferences
                e.putString(PREF_KEY_OAUTH_TOKEN, accessToken.getToken());
                e.putString(PREF_KEY_OAUTH_SECRET,
                        accessToken.getTokenSecret());

                // Store login status - true
                e.putBoolean(PREF_KEY_TWITTER_LOGIN, true);
                e.commit(); // save changes


                long userID = accessToken.getUserId();
                user = twitter.showUser(userID);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean response) {
            if(response){
                progress.dismiss();

                getActivity().getSupportFragmentManager().popBackStack();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                        .replace(R.id.container, new SettingsFragment())
                        .commit();
            }
        }
    }
}
