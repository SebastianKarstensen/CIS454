package com.autobook.cis454.autobook.Fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.autobook.cis454.autobook.R;

public class SettingsFragment extends Fragment {

    public SettingsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_twitter_login, container, false);

        Button loginTwitter = (Button) rootView.findViewById(R.id.button_twitter_login);
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