package com.autobook.cis454.autobook.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.autobook.cis454.autobook.Adapters.EventRecyclerAdapter;
import com.autobook.cis454.autobook.Adapters.ReceiverRecyclerAdapter;
import com.autobook.cis454.autobook.Event.Event;
import com.autobook.cis454.autobook.Helpers.Storage;
import com.autobook.cis454.autobook.Notifications.Receiver;
import com.autobook.cis454.autobook.R;

import java.util.ArrayList;

/**
 * Created by Sebastian on 07-04-2015.
 */
public class AgendaFragment extends Fragment {

    private RecyclerView recyclerView;
    private EventRecyclerAdapter recyclerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        recyclerAdapter = new EventRecyclerAdapter(Storage.getEventsFromDatabase());

        View rootView = inflater.inflate(R.layout.fragment_agenda, container, false);

        Button buttonSwitchView = (Button) rootView.findViewById(R.id.btn_agenda_switch_calendar);
        buttonSwitchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.container, new CalendarFragment())
                        .commit();
            }
        });

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_widget);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerAdapter.setOnItemClickListener(new EventRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                Event toClone = recyclerAdapter.getEventList().get(pos);
                Event eventCopy = new Event(toClone.getID(),
                        toClone.getTitle(),
                        toClone.getDate(),
                        toClone.getType(),
                        toClone.getReceivers(),
                        toClone.getFacebookMessage(),
                        toClone.getTwitterMessage(),
                        toClone.getTextMessage());

                getFragmentManager().beginTransaction()
                        .replace(R.id.container, EventFragment.newInstance(eventCopy))
                        .addToBackStack(null)
                        .commit();
            }
        });
        recyclerView.setAdapter(recyclerAdapter);

        return rootView;
    }
}
