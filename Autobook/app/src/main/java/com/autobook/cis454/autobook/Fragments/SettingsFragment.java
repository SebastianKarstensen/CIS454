package com.autobook.cis454.autobook.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

import java.util.Date;
import java.util.concurrent.ExecutionException;

import twitter4j.PagableResponseList;
import twitter4j.User;

public class SettingsFragment extends Fragment {

    Button buttonLoginFacebook;
    Button buttonLogoutFacebook;
    Button buttonLoginTwitter;
    Button buttonLogoutTwitter;
    Button buttonEditMessageTemplates;
    Button buttonWipeDatabase;
    Button buttonHelp;
    Button buttonCredits;

    public SettingsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        buttonLoginFacebook = (Button) rootView.findViewById(R.id.btn_settings_loginFacebook);
        buttonLogoutFacebook = (Button) rootView.findViewById(R.id.btn_settings_logoutFacebook);
        buttonLoginTwitter = (Button) rootView.findViewById(R.id.btn_settings_loginTwitter);
        buttonLogoutTwitter = (Button) rootView.findViewById(R.id.btn_settings_logoutTwitter);
        buttonEditMessageTemplates = (Button) rootView.findViewById(R.id.btn_settings_editTemplates);
        buttonWipeDatabase = (Button) rootView.findViewById(R.id.btn_settings_wipeDatabase);
        buttonHelp = (Button) rootView.findViewById(R.id.btn_settings_help);
        buttonCredits = (Button) rootView.findViewById(R.id.btn_settings_credits);

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
}