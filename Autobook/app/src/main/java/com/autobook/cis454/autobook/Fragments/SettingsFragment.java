package com.autobook.cis454.autobook.Fragments;

<<<<<<< HEAD
import android.app.AlertDialog;
import android.content.DialogInterface;
=======
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
>>>>>>> origin/master
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.autobook.cis454.autobook.Activities.HomeActivity;
import com.autobook.cis454.autobook.Helpers.TwitterHelper;
import com.autobook.cis454.autobook.R;
import com.autobook.cis454.autobook.Scheduler.AlarmManagerBroadcastReceiver;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.FacebookSdk;

import java.util.Date;
import java.util.concurrent.ExecutionException;

import twitter4j.PagableResponseList;
import twitter4j.User;

public class SettingsFragment extends Fragment {

    LoginButton buttonLoginFacebook;
    CallbackManager callbackManager;
    AccessToken accessToken;
    Button buttonLogoutFacebook;
    Button buttonLoginTwitter;
    Button buttonLogoutTwitter;
    Button buttonEditMessageTemplates;
    Button buttonWipeDatabase;
    Button buttonHelp;
    Button buttonCredits;

    public SettingsFragment() {
    }

    public void onCreate(Bundle savedInstance)
    {
        super.onCreate(savedInstance);
        callbackManager = CallbackManager.Factory.create();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        buttonLoginFacebook = (LoginButton) rootView.findViewById(R.id.btn_settings_loginFacebook);
        buttonLogoutFacebook = (Button) rootView.findViewById(R.id.btn_settings_logoutFacebook);
        buttonLoginTwitter = (Button) rootView.findViewById(R.id.btn_settings_loginTwitter);
        buttonLogoutTwitter = (Button) rootView.findViewById(R.id.btn_settings_logoutTwitter);
        buttonEditMessageTemplates = (Button) rootView.findViewById(R.id.btn_settings_editTemplates);
        buttonWipeDatabase = (Button) rootView.findViewById(R.id.btn_settings_wipeDatabase);
        buttonHelp = (Button) rootView.findViewById(R.id.btn_settings_help);
        buttonCredits = (Button) rootView.findViewById(R.id.btn_settings_credits);

        buttonLoginFacebook.setReadPermissions("user_friends");
        // If using in a fragment
        buttonLoginFacebook.setFragment(this);
        // Other app specific specialization
        //loginButton.setPublishPermissions();
        // Callback registration
        buttonLoginFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>()
        {
            @Override
            public void onSuccess(LoginResult loginResult)
            {
                // App code
                accessToken = loginResult.getAccessToken();
                Toast.makeText(getActivity(), "Login Success!", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancel()
            {
                // App code
                Toast.makeText(getActivity(), "Login canceled", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException exception)
            {
                // App code
                Toast.makeText(getActivity(), exception.toString(), Toast.LENGTH_LONG).show();
            }
        });

        buttonLoginTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                        .replace(R.id.container, new TwitterWebFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });

        buttonLogoutTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TwitterHelper.logoutTwitter();
                updateFragment();
            }
        });

        buttonWipeDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Reset Database")
                        .setMessage("Are you sure you want to reset the database?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                HomeActivity.dbHandler.deleteEverything();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });

        buttonHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    PagableResponseList<User> friends = new TwitterHelper.GetTwitterFriends().execute().get();
                    for(User user : friends) {
                        String message = user.getScreenName();
                        user.getOriginalProfileImageURL();
                        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });

        buttonCredits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(getActivity())
                                .setSmallIcon(R.drawable.autobook_logo_v01)
                                .setContentTitle("Failed to execute the event")
                                .setContentText("Event title: ");

                // Sets an ID for the notification
                int mNotificationId = 001;
                // Gets an instance of the NotificationManager service
                NotificationManager mNotifyMgr =
                        (NotificationManager) getActivity().getSystemService(getActivity().NOTIFICATION_SERVICE);
                // Builds the notification and issues it.
                mNotifyMgr.notify(mNotificationId, mBuilder.build());

            }
        });
        updateFragment();

        return rootView;
    }

    public void updateFragment() {
        if(TwitterHelper.isTwitterLoggedIn()) {
            buttonLoginTwitter.setVisibility(View.GONE);
            buttonLogoutTwitter.setVisibility(View.VISIBLE);
        }
        else {
            buttonLoginTwitter.setVisibility(View.VISIBLE);
            buttonLogoutTwitter.setVisibility(View.GONE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}