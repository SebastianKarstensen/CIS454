package com.autobook.cis454.autobook;

import android.app.Dialog;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.autobook.cis454.autobook.TestActivities.TwitterTestActivity;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;


public class TwitterActivity extends ActionBarActivity {

    SharedPreferences pref;
    static final String CONSUMER_KEY = "PPdcTyXkb1yipqFAa4RdTnjux";
    static final String CONSUMER_SECRET = "bOwYdfmkQX2DsRgVeVxIrhtOUoWS8vlTn2ZXLP8qpnxRoEDC9u";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pref = getPreferences(0);
        SharedPreferences.Editor edit = pref.edit();
        edit.putString("CONSUMER_KEY", CONSUMER_KEY);
        edit.putString("CONSUMER_SECRET", CONSUMER_SECRET);
        edit.commit();

        setContentView(R.layout.activity_twitter);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_twitter, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        Button login;
        Twitter twitter;
        RequestToken requestToken = null;
        AccessToken accessToken;
        String oauth_url,oauth_verifier,profile_url;
        Dialog auth_dialog;
        WebView web;
        SharedPreferences pref;
        ProgressDialog progress;
        Bitmap bitmap;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_twitter_test, container, false);
            login = (Button) view.findViewById(R.id.button_login_twitter);
            pref = getActivity().getPreferences(0);
            twitter = new TwitterFactory().getInstance();
            twitter.setOAuthConsumer(pref.getString("CONSUMER_KEY", ""), pref.getString("CONSUMER_SECRET", ""));
            login.setOnClickListener(new LoginProcess());
            return view;
        }
        private class LoginProcess implements View.OnClickListener {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new TokenGet().execute();
            }}
        private class TokenGet extends AsyncTask<String, String, String> {
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
                if(oauth_url != null){
                    Log.e("URL", oauth_url);
                    auth_dialog = new Dialog(getActivity());
                    auth_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    auth_dialog.setContentView(R.layout.auth_dialog);
                    web = (WebView)auth_dialog.findViewById(R.id.webView);
                    web.getSettings().setJavaScriptEnabled(true);
                    web.loadUrl(oauth_url);
                    web.setWebViewClient(new WebViewClient() {
                        boolean authComplete = false;
                        @Override
                        public void onPageStarted(WebView view, String url, Bitmap favicon){
                            super.onPageStarted(view, url, favicon);
                        }
                        @Override
                        public void onPageFinished(WebView view, String url) {
                            super.onPageFinished(view, url);
                            if (url.contains("oauth_verifier") && authComplete == false){
                                authComplete = true;
                                Log.e("Url",url);
                                Uri uri = Uri.parse(url);
                                oauth_verifier = uri.getQueryParameter("oauth_verifier");
                                auth_dialog.dismiss();
                                new AccessTokenGet().execute();
                            }else if(url.contains("denied")){
                                auth_dialog.dismiss();
                                Toast.makeText(getActivity(), "Sorry !, Permission Denied", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    auth_dialog.show();
                    auth_dialog.setCancelable(true);
                }else{
                    Toast.makeText(getActivity(), "Sorry !, Network Error or Invalid Credentials", Toast.LENGTH_SHORT).show();
                }
            }
        }
        private class AccessTokenGet extends AsyncTask<String, String, Boolean> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progress = new ProgressDialog(getActivity());
                progress.setMessage("Fetching Data ...");
                progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progress.setIndeterminate(true);
                progress.show();
            }
            @Override
            protected Boolean doInBackground(String... args) {
                try {
                    accessToken = twitter.getOAuthAccessToken(requestToken, oauth_verifier);
                    SharedPreferences.Editor edit = pref.edit();
                    edit.putString("ACCESS_TOKEN", accessToken.getToken());
                    edit.putString("ACCESS_TOKEN_SECRET", accessToken.getTokenSecret());
                    User user = twitter.showUser(accessToken.getUserId());
                    profile_url = user.getOriginalProfileImageURL();
                    edit.putString("NAME", user.getName());
                    edit.putString("IMAGE_URL", user.getOriginalProfileImageURL());
                    edit.commit();
                } catch (TwitterException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return true;
            }
            @Override
            protected void onPostExecute(Boolean response) {
                if(response){
                    progress.hide();

                    getActivity().getSupportFragmentManager().beginTransaction()
                            .add(R.id.container, new PlaceholderFragment())
                            .commit();
                }
            }
        }
    }
}
