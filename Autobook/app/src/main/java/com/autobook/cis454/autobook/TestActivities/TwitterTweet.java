package com.autobook.cis454.autobook.TestActivities;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.SharedPreferences.Editor;

import com.autobook.cis454.autobook.R;

public class TwitterTweet extends ActionBarActivity
{
    //region Variables
    // Constants
    /*
     * Register your here app https://dev.twitter.com/apps/new and get your
     * consumer key and secret
     */

    static String TWITTER_CONSUMER_KEY = "ljTAGW8VXet3qGw08JQ8yjhVJ";
    static String TWITTER_CONSUMER_SECRET = "ShPObDg7rPohIqXTVhs2BxArrao2kbwMEj4koGZrge9Xx58KQt";

    // Preference Constants
    static String PREFERENCE_NAME = "twitter_oauth";
    static String PREF_KEY_OAUTH_TOKEN = "oauth_token";
    static String PREF_KEY_OAUTH_SECRET = "oauth_token_secret";
    static String PREF_KEY_TWITTER_LOGIN = "isTwitterLoggedIn";

    static final String TWITTER_CALLBACK_URL = "app://callback";

    // Twitter oauth urls
    static final String URL_TWITTER_AUTH = "auth_url";
    static final String URL_TWITTER_OAUTH_VERIFIER = "oauth_verifier";
    static final String URL_TWITTER_OAUTH_TOKEN = "oauth_token";

    // Progress dialog
    ProgressDialog pDialog;

    // Twitter
    private static Twitter twitter;
    private static RequestToken requestToken;
    private static AccessToken accessToken;

    // Shared Preferences
    private static SharedPreferences mSharedPreferences;

    // Internet Connection detector
    //private ConnectionDetector cd;

    // Alert Dialog Manager
    AlertDialogManager alert = new AlertDialogManager();


    private TextView header;
    private TextView userName;
    private TextView tweet_message;
    private Button tweet_login;
    private Button tweet_logout;
    private Button tweet_button;

    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter_tweet);

        header = (TextView) this.findViewById(R.id.textView_twitter_headline);
        userName = (TextView) this.findViewById(R.id.textView_username);
        tweet_message = (TextView) this.findViewById(R.id.textArea_tweet_message);
        tweet_login = (Button) this.findViewById(R.id.button_login_twitter);
        tweet_logout = (Button) this.findViewById(R.id.button_logout_twitter);
        tweet_button = (Button) this.findViewById(R.id.button_tweet);

        //cd = new ConnectionDetector(getApplicationContext());

        //region CheckInternetCode
        // Check if Internet present
        //if (!cd.isConnectingToInternet()) {
        //    // Internet Connection is not present
        //    alert.showAlertDialog(TwitterTweet.this, "Internet Connection Error",
        //            "Please connect to working Internet connection", false);
        //    // stop executing code by return
        //    return;
        //}
        //endregion

        // Shared Preferences
        mSharedPreferences = getApplicationContext().getSharedPreferences("MyPref", 0);

        tweet_login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                loginToTwitter();
            }

        });

        tweet_logout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                logoutFromTwitter();
            }

        });

        //code for "Tweet!" button
        tweet_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                /* This if conditions is tested once is
                 * redirected from twitter page. Parse the uri to get oAuth
                 * Verifier
                 */

                checkLogin();
                String message = tweet_message.getText().toString();
                new updateTwitterStatus().execute(message);
            }
        });


    }

    private void checkLogin() {
        if (!isTwitterLoggedInAlready()) {
            Uri uri = getIntent().getData();
            if (uri != null && uri.toString().startsWith(TWITTER_CALLBACK_URL)) {
                // oAuth verifier
                final String verifier = uri
                        .getQueryParameter(URL_TWITTER_OAUTH_VERIFIER);

                try {

                    Thread thread = new Thread(new Runnable(){
                        @Override
                        public void run() {
                            try {

                                // Get the access token
                                TwitterTweet.this.accessToken = twitter.getOAuthAccessToken(
                                        requestToken, verifier);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    thread.start();

                    Toast.makeText(getApplicationContext(),"Ran the Thread",Toast.LENGTH_SHORT).show();

                    // Shared Preferences
                    Editor e = mSharedPreferences.edit();

                    // After getting access token, access token secret
                    // store them in application preferences
                    e.putString(PREF_KEY_OAUTH_TOKEN, accessToken.getToken());
                    e.putString(PREF_KEY_OAUTH_SECRET,
                            accessToken.getTokenSecret());
                    // Store login status - true
                    e.putBoolean(PREF_KEY_TWITTER_LOGIN, true);
                    e.commit(); // save changes

                    // Hide login button
                    tweet_login.setVisibility(View.GONE);


                    // Getting user details from twitter
                    // For now i am getting his name only
                    long userID = accessToken.getUserId();
                    User user = twitter.showUser(userID);
                    String username = user.getName();

                    // Displaying in xml ui
                    userName.setText(username);
                } catch (Exception e) {
                    // Check log for login errors
                    Log.e("Twitter Login Error", "> " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    class updateTwitterStatus extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(TwitterTweet.this);
            pDialog.setMessage("Updating to Twitter...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... args) {
            String status = args[0];
            try {
                ConfigurationBuilder builder = new ConfigurationBuilder();
                builder.setOAuthConsumerKey(TWITTER_CONSUMER_KEY);
                builder.setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET);
//                builder.setOAuthAccessToken(mSharedPreferences.getString(PREF_KEY_OAUTH_TOKEN,""));
//                builder.setOAuthAccessTokenSecret(mSharedPreferences.getString(PREF_KEY_OAUTH_SECRET, ""));
                // Access Token
                String access_token = mSharedPreferences.getString(PREF_KEY_OAUTH_TOKEN, "");
                // Access Token Secret
                String access_token_secret = mSharedPreferences.getString(PREF_KEY_OAUTH_SECRET, "");

                AccessToken accessToken = new AccessToken(access_token, access_token_secret);
                Twitter twitter = new TwitterFactory(builder.build()).getInstance(accessToken);//.getInstance(accessToken);

                twitter.updateStatus(status);


//          Log.d("*** Update Status: ",response.getText());
            } catch (TwitterException e) {
                Log.d("*** Twitter Error: ", e.getMessage());
            }
            return null;
        }


        //@Override
        protected void onPostExecute(String result)
        {
            pDialog.dismiss();
        }
    }

    private void loginToTwitter() {
        // Check if already logged in
        if (!isTwitterLoggedInAlready()) {
            ConfigurationBuilder builder = new ConfigurationBuilder();
            builder.setOAuthConsumerKey(TWITTER_CONSUMER_KEY);
            builder.setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET);
            Configuration configuration = builder.build();

            TwitterFactory factory = new TwitterFactory(configuration);
            twitter = factory.getInstance();


            Thread thread = new Thread(new Runnable(){
                @Override
                public void run() {
                    try {
                        requestToken = twitter
                                .getOAuthRequestToken(TWITTER_CALLBACK_URL);

                        String auth_url = requestToken.getAuthenticationURL();

                        TwitterTweet.this.startActivity(new Intent(Intent.ACTION_VIEW, Uri
                                .parse(auth_url)));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
        } else {
            // user already logged into twitter
            Toast.makeText(getApplicationContext(),
                    "Already Logged into twitter", Toast.LENGTH_LONG).show();
        }
    }


    private void logoutFromTwitter() {
        // Clear the shared preferences
        Editor e = mSharedPreferences.edit();
        e.remove(PREF_KEY_OAUTH_TOKEN);
        e.remove(PREF_KEY_OAUTH_SECRET);
        e.remove(PREF_KEY_TWITTER_LOGIN);
        e.commit();
    }



/*
     * Check user already logged in your application using twitter Login flag is
     * fetched from Shared Preferences
     */

    private boolean isTwitterLoggedInAlready() {
        // return twitter login status from Shared Preferences
        return mSharedPreferences.getBoolean(PREF_KEY_TWITTER_LOGIN, false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_twitter_tweet, menu);
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        Toast.makeText(getApplicationContext(),"Resumed",Toast.LENGTH_SHORT).show();
        checkLogin();
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
}



