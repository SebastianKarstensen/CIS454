package com.autobook.cis454.autobook.Fragments;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.autobook.cis454.autobook.Activities.TwitterLogin;
import com.autobook.cis454.autobook.R;
import com.autobook.cis454.autobook.TestActivities.TwitterTweet;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Created by Sebastian on 02-03-2015.
 */
public class TwitterTweetFragment extends Fragment {

    private static final String ARG_FRAGMENT_USER = "ARGUMENT_TWITTER_USER";
    private User currentUser;
    private SharedPreferences sharedPreferences;
    private EditText tweet;

    public TwitterTweetFragment() {

    }

    /*
     * Creates a new Fragment based on the users profile
     */
    public static Fragment newInstance(User user) {
        TwitterTweetFragment fragment = new TwitterTweetFragment();
        Bundle arguments = new Bundle();

        arguments.putSerializable(ARG_FRAGMENT_USER, user);

        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());

        currentUser = (User) getArguments().getSerializable(ARG_FRAGMENT_USER);

        View rootView = inflater.inflate(R.layout.fragment_tweet, container, false);

        TextView textView_username = (TextView) rootView.findViewById(R.id.textView_twitter_username);
        textView_username.setText(currentUser.getName());

        tweet = (EditText) rootView.findViewById(R.id.editText_tweetInput);

        Button buttonTweet = (Button) rootView.findViewById(R.id.button_twitter_tweet);
        Button buttonLogout = (Button) rootView.findViewById(R.id.button_twitter_logout);
        Button buttonHome = (Button) rootView.findViewById(R.id.button_twitter_goHome);

        buttonTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = tweet.getText().toString();
                new updateTwitterStatus().execute(message);
            }
        });

        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        return rootView;
    }

    /*
     * AsyncTask used for sending a POST-request to the logged-in Twitter account
     */
    class updateTwitterStatus extends AsyncTask<String, String, String> {

        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Updating to Twitter...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... args) {
            String status = args[0];
            try {
                ConfigurationBuilder builder = new ConfigurationBuilder();
                builder.setOAuthConsumerKey(TwitterWebFragment.TWITTER_CONSUMER_KEY);
                builder.setOAuthConsumerSecret(TwitterWebFragment.TWITTER_CONSUMER_SECRET);
                //builder.setOAuthAccessToken(mSharedPreferences.getString(PREF_KEY_OAUTH_TOKEN,""));
                //builder.setOAuthAccessTokenSecret(mSharedPreferences.getString(PREF_KEY_OAUTH_SECRET, ""));
                // Access Token
                String access_token = sharedPreferences.getString(TwitterWebFragment.PREF_KEY_OAUTH_TOKEN, "");
                // Access Token Secret
                String access_token_secret = sharedPreferences.getString(TwitterWebFragment.PREF_KEY_OAUTH_SECRET, "");

                AccessToken accessToken = new AccessToken(access_token, access_token_secret);
                Twitter twitter = new TwitterFactory(builder.build()).getInstance(accessToken);

                twitter.updateStatus(status);

                //Log.d("*** Update Status: ",response.getText());
            } catch (TwitterException e) {
                Log.d("*** Twitter Error: ", e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result)
        {
            pDialog.dismiss();
        }
    }
}
