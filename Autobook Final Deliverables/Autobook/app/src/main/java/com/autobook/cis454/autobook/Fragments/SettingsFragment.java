package com.autobook.cis454.autobook.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.autobook.cis454.autobook.Activities.HomeActivity;
import com.autobook.cis454.autobook.Helpers.TwitterHelper;
import com.autobook.cis454.autobook.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.Gson;

public class SettingsFragment extends Fragment {

    LoginButton buttonLoginFacebook;
    CallbackManager callbackManager;
    AccessToken accessToken;
    Button buttonLogoutFacebook;
    Button buttonLoginTwitter;
    Button buttonLogoutTwitter;
    Button buttonWipeDatabase;
    Button buttonHelp;
    Button buttonCredits;
    SharedPreferences mSharedPreferences;

    public SettingsFragment() {
    }

    public void onCreate(Bundle savedInstance)
    {
        super.onCreate(savedInstance);
        mSharedPreferences = getActivity().getSharedPreferences("MyPref", 0);
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
        buttonWipeDatabase = (Button) rootView.findViewById(R.id.btn_settings_wipeDatabase);
        buttonHelp = (Button) rootView.findViewById(R.id.btn_settings_help);
        buttonCredits = (Button) rootView.findViewById(R.id.btn_settings_credits);

        //buttonLoginFacebook.setReadPermissions("user_friends");
        // If using in a fragment
        buttonLoginFacebook.setFragment(this);
        // Other app specific specialization
        buttonLoginFacebook.setPublishPermissions("publish_actions");
        // Callback registration
        buttonLoginFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>()
        {
            @Override
            public void onSuccess(LoginResult loginResult)
            {
                // App code
                accessToken = loginResult.getAccessToken();
                Editor e = mSharedPreferences.edit();
                Gson gson = new Gson();
                String json = gson.toJson(accessToken);
                e.putString("fb_access_token", json);
                e.commit();
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

        buttonLogoutFacebook.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v){
                LoginManager.getInstance().logOut();
                Editor e = mSharedPreferences.edit();
                e.putString("fb_access_token", null);
                e.commit();
                buttonLogoutFacebook.setVisibility(View.GONE);
                buttonLoginFacebook.setVisibility(View.VISIBLE);
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
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                        .replace(R.id.container, new VideoFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });

        buttonCredits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                        .replace(R.id.container, new CreditsFragment())
                        .addToBackStack(null)
                        .commit();
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