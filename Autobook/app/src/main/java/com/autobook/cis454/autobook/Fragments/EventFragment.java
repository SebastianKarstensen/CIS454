package com.autobook.cis454.autobook.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.Toast;

import com.autobook.cis454.autobook.Activities.HomeActivity;
import com.autobook.cis454.autobook.Event.Event;
import com.autobook.cis454.autobook.Event.EventType;
import com.autobook.cis454.autobook.Event.MediaType;
import com.autobook.cis454.autobook.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Sebastian on 28-03-2015.
 */
public class EventFragment extends Fragment {

    public static final String BUNDLE_TAB_ARGUMENT = "ARGUMENT_TAB_EVENT";
    FragmentTabHost tabHost;
    FragmentTabHost.TabSpec facebookTab;
    FragmentTabHost.TabSpec twitterTab;
    FragmentTabHost.TabSpec textTab;

    CheckBox checkFacebook;
    CheckBox checkTwitter;
    CheckBox checkText;

    Date eventDate;
    EventType eventType;

    List<MediaType> mediaTypes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_event, container, false);

        if(getActivity().getIntent().getExtras() != null) {
            mediaTypes = (ArrayList<MediaType>) getActivity().getIntent().getSerializableExtra(HomeActivity.INTENT_EXTRA_LIST_OF_TYPES);
        }

        final EditText eventTitle = (EditText) rootView.findViewById(R.id.editText_event_name);

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

        Button saveButton = (Button) rootView.findViewById(R.id.btn_event_save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy");
                //String date = df.format(eventDate);

                TabEventFragment facebookFragment = (TabEventFragment) getChildFragmentManager().findFragmentByTag("tabFacebook");
                String facebookMessage;
                if(facebookFragment != null) {
                    facebookMessage = facebookFragment.getMessage();
                }

                TabEventFragment twitterFragment = (TabEventFragment) getChildFragmentManager().findFragmentByTag("tabTwitter");
                String twitterMessage;
                if(twitterFragment != null) {
                    twitterMessage = twitterFragment.getMessage();
                }

                TabEventFragment textFragment = (TabEventFragment) getChildFragmentManager().findFragmentByTag("tabText");
                String textMessage;
                if(textFragment != null) {
                    textMessage = textFragment.getMessage();
                }

                eventType = EventType.American_Holiday;
                String type = eventType.toString();

                Toast.makeText(getActivity(),type,Toast.LENGTH_SHORT).show();
                String title = eventTitle.getText().toString();

                //HomeActivity.dbHandler.insertEvent(date, facebookMessage, twitterMessage, textMessage, eventType, title);
            }
        });


        return rootView;
    }

    public void updateTabs() {
        tabHost.clearAllTabs();
        if(checkFacebook.isChecked()) {
            Bundle bundle = new Bundle();
            bundle.putInt(BUNDLE_TAB_ARGUMENT,0);
            tabHost.addTab(facebookTab,TabEventFragment.class,bundle);
        }
        if(checkTwitter.isChecked()) {
            Bundle bundle = new Bundle();
            bundle.putInt(BUNDLE_TAB_ARGUMENT,1);
            tabHost.addTab(twitterTab, TabEventFragment.class,bundle);
        }
        if(checkText.isChecked()) {
            Bundle bundle = new Bundle();
            bundle.putInt(BUNDLE_TAB_ARGUMENT,2);
            tabHost.addTab(textTab, TabEventFragment.class,bundle);
        }
    }
}