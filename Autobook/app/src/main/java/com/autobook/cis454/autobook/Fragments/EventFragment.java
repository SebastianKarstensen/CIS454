package com.autobook.cis454.autobook.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TabHost;

import com.autobook.cis454.autobook.Activities.HomeActivity;
import com.autobook.cis454.autobook.Event.MediaType;
import com.autobook.cis454.autobook.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sebastian on 28-03-2015.
 */
public class EventFragment extends Fragment {

    FragmentTabHost tabHost;
    FragmentTabHost.TabSpec facebookTab;
    FragmentTabHost.TabSpec twitterTab;
    FragmentTabHost.TabSpec textTab;

    CheckBox checkFacebook;
    CheckBox checkTwitter;
    CheckBox checkText;

    List<MediaType> mediaTypes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_event, container, false);

        if(getActivity().getIntent().getExtras() != null) {
            mediaTypes = (ArrayList<MediaType>) getActivity().getIntent().getSerializableExtra(HomeActivity.INTENT_EXTRA_LIST_OF_TYPES);
        }

        checkFacebook = (CheckBox) rootView.findViewById(R.id.checkbox_event_facebook);
        checkTwitter = (CheckBox) rootView.findViewById(R.id.checkbox_event_twitter);
        checkText = (CheckBox) rootView.findViewById(R.id.checkbox_event_text);

        if(mediaTypes != null) {
            for(MediaType type : mediaTypes) {
                switch(type) {
                    case Twitter:
                        checkTwitter.setChecked(true);
                        break;
                    case Facebook:
                        checkFacebook.setChecked(true);
                        break;
                    case TextMessaging:
                        checkText.setChecked(true);
                        break;
                    default:
                }
            }
        }

        tabHost = (FragmentTabHost) rootView.findViewById(R.id.tabHost);
        tabHost.setup(getActivity(), getChildFragmentManager(),android.R.id.tabcontent);

        facebookTab = tabHost.newTabSpec("tabFacebook").setIndicator("Facebook",null);
        twitterTab = tabHost.newTabSpec("tabTwitter").setIndicator("Twitter",null);
        textTab = tabHost.newTabSpec("tabText").setIndicator("Text",null);

        updateTabs();

        checkFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTabs();
            }
        });

        checkTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTabs();
            }
        });

        checkText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTabs();
            }
        });

        return rootView;
    }

    public void updateTabs() {
        tabHost.clearAllTabs();
        if(checkFacebook.isChecked()) {
            tabHost.addTab(facebookTab,TabEventFragment.class,null);
        }
        if(checkTwitter.isChecked()) {
            tabHost.addTab(twitterTab, TabEventFragment.class, null);
        }
        if(checkText.isChecked()) {
            tabHost.addTab(textTab, TabEventFragment.class,null);
        }
    }
}