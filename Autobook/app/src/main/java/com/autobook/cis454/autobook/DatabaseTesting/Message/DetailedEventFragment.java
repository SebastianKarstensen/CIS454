package com.autobook.cis454.autobook.DatabaseTesting.Message;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.autobook.cis454.autobook.DatabaseTesting.Database.MyDatabaseHandler;
import com.autobook.cis454.autobook.R;

import java.util.HashMap;

public class DetailedEventFragment extends Fragment {

    MyDatabaseHandler db;
    String eventID;

    public DetailedEventFragment(){}

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        db = new MyDatabaseHandler(activity);
        db.updateEventList();
    }

    public static DetailedEventFragment newInstance(int event) {

        DetailedEventFragment fragment = new DetailedEventFragment();
        Bundle args = new Bundle();
        args.putInt("event", event);
        fragment.setArguments(args);
        return fragment;
    }

    public String getEventID(){
        return eventID;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
        HashMap<String, ?> event = db.getEventList().get(getArguments().getInt("event"));
        eventID = (String) event.get("id");
        View rootView;
        rootView = inflater.inflate(R.layout.fragment_detail_event, container, false);
        TextView title = (TextView) rootView.findViewById(R.id.title);
        TextView date = (TextView) rootView.findViewById(R.id.date);
        TextView facebook = (TextView) rootView.findViewById(R.id.facebookMessage);
        TextView twitter = (TextView) rootView.findViewById(R.id.twitterMessage);
        TextView textmessage = (TextView) rootView.findViewById(R.id.textMessage);
        TextView eventType = (TextView) rootView.findViewById(R.id.eventType);
        Button addButton = (Button) rootView.findViewById(R.id.addreceivers);
        title.setText((String) event.get("title"));
        date.setText((String) event.get("date"));
        facebook.setText((String) event.get("facebookMessage"));
        twitter.setText((String) event.get("twitterMessage"));
        textmessage.setText((String) event.get("textMessage"));
        eventType.setText((String) event.get("eventType"));
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.detail_list_container, MessageReceiverListFragment.newInstance(getEventID()))
                        .commit();
            }
        });
        return rootView;
    }





}