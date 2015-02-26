package com.autobook.cis454.autobook.TestActivities;

import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.autobook.cis454.autobook.R;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

public class TwitterTestActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter_test);

        Uri uri = getIntent().getData();
        
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, PlaceholderFragment.newInstance(uri))
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_twitter_test, menu);
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

        public static final String ARG_URI = "ARGUMENT_URI";

        static String TWITTER_CONSUMER_KEY = "ljTAGW8VXet3qGw08JQ8yjhVJ";
        static String TWITTER_CONSUMER_SECRET = "ShPObDg7rPohIqXTVhs2BxArrao2kbwMEj4koGZrge9Xx58KQt";

        // Preference Constants
        static String PREFERENCE_NAME = "twitter_oauth";
        static final String PREF_KEY_OAUTH_TOKEN = "oauth_token";
        static final String PREF_KEY_OAUTH_SECRET = "oauth_token_secret";
        static final String PREF_KEY_TWITTER_LOGIN = "isTwitterLogedIn";

        static final String TWITTER_CALLBACK_URL = "oauth://t4jsample";

        // Twitter oauth urls
        static final String URL_TWITTER_AUTH = "auth_url";
        static final String URL_TWITTER_OAUTH_VERIFIER = "oauth_verifier";
        static final String URL_TWITTER_OAUTH_TOKEN = "oauth_token";

        // Twitter
        private static Twitter twitter;
        private static RequestToken requestToken;

        // Shared Preferences
        private static SharedPreferences mSharedPreferences;

        // Uri
        private static Uri uri;

        public PlaceholderFragment() {
        }

        public static Fragment newInstance(Uri uri) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle arguments = new Bundle();
            arguments.putSerializable(ARG_URI, (java.io.Serializable) uri);
            fragment.setArguments(arguments);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            uri = (Uri) getArguments().getSerializable(ARG_URI);

            View rootView = inflater.inflate(R.layout.fragment_twitter_test, container, false);

            mSharedPreferences = getActivity().getApplicationContext().getSharedPreferences(
                    "MyPref", 0);

            EditText tweet_message = (EditText) rootView.findViewById(R.id.textArea_tweet_message);
            String message = tweet_message.getText().toString();
            Button tweet_button = (Button) rootView.findViewById(R.id.button_login_twitter);


            tweet_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loginToTwitter();
                }
            });

            return rootView;
        }

        private void loginToTwitter() {
            
            if (uri != null && uri.toString().startsWith(TWITTER_CALLBACK_URL)) {
                // oAuth verifier
                String verifier = uri
                        .getQueryParameter(URL_TWITTER_OAUTH_VERIFIER);

                try {
                    // Get the access token
                    AccessToken accessToken = twitter.getOAuthAccessToken(
                            requestToken, verifier);

                    // Shared Preferences
                    SharedPreferences.Editor e = mSharedPreferences.edit();

                    // After getting access token, access token secret
                    // store them in application preferences
                    e.putString(PREF_KEY_OAUTH_TOKEN, accessToken.getToken());
                    e.putString(PREF_KEY_OAUTH_SECRET,
                            accessToken.getTokenSecret());
                    // Store login status - true
                    e.putBoolean(PREF_KEY_TWITTER_LOGIN, true);
                    e.commit(); // save changes

                    Log.e("Twitter OAuth Token", "> " + accessToken.getToken());

                    // Getting user details from twitter
                    // For now i am getting his name only
                    long userID = accessToken.getUserId();
                    User user = twitter.showUser(userID);
                    String username = user.getName();

                } catch (Exception e) {
                    // Check log for login errors
                    Log.e("Twitter Login Error", "> " + e.getMessage());
                }
            }
        }
    }

}
