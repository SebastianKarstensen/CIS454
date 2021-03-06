package com.autobook.cis454.autobook.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.autobook.cis454.autobook.Activities.EventActivity;
import com.autobook.cis454.autobook.Adapters.EventRecyclerAdapter;
import com.autobook.cis454.autobook.Event.Event;
import com.autobook.cis454.autobook.Helpers.Sorters;
import com.autobook.cis454.autobook.Helpers.Storage;
import com.autobook.cis454.autobook.R;
import com.autobook.cis454.autobook.Scheduler.AlarmManagerBroadcastReceiver;

import java.util.Collections;

public class AgendaFragment extends Fragment {

    private RecyclerView recyclerView;
    private EventRecyclerAdapter recyclerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        recyclerAdapter = new EventRecyclerAdapter(Storage.getEventsFromDatabase());
        Collections.sort(recyclerAdapter.getEventList(), new Sorters.SortBasedOnDate());

        View rootView = inflater.inflate(R.layout.fragment_agenda, container, false);

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

                Intent intent = new Intent(getActivity(),EventActivity.class);
                intent.putExtra(EventsDialogFragment.INTENT_EXTRA_EVENT, eventCopy);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View v, final int pos) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Delete entry")
                        .setMessage("Are you sure you want to delete this entry?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Event thisEvent = recyclerAdapter.getEventList().get(pos);
                                Storage.deleteEvent(thisEvent);
                                AlarmManagerBroadcastReceiver alarm = new AlarmManagerBroadcastReceiver();
                                alarm.cancelAlarm(getActivity().getApplicationContext(), thisEvent);
                                recyclerAdapter.notifyItemRemoved(pos);
                                recyclerAdapter.setEventList(Storage.getEventsFromDatabase());
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
        recyclerView.setAdapter(recyclerAdapter);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        recyclerAdapter.setEventList(Storage.getEventsFromDatabase());
        recyclerAdapter.notifyDataSetChanged();
    }
}
