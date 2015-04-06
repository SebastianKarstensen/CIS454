package com.autobook.cis454.autobook.Fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.autobook.cis454.autobook.Helpers.TwitterHelper;
import com.autobook.cis454.autobook.R;

public class SettingsFragment extends Fragment {

    public SettingsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        Button buttonGetFriends = (Button) rootView.findViewById(R.id.btn_settings_loginFacebook);
        buttonGetFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TwitterHelper.UpdateTwitterStatus().execute("It's a tweet!");
            }
        });


        Button loginTwitter = (Button) rootView.findViewById(R.id.btn_settings_loginTwitter);
        Typeface face = Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/HPSimplified_Rg.ttf");
        loginTwitter.setTypeface(face);

        loginTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                        .replace(R.id.container, new TwitterWebFragment())
                        .addToBackStack(null)
                        .commit();
            }
        });

        return rootView;
    }
}