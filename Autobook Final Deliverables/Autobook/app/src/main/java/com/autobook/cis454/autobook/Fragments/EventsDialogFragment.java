package com.autobook.cis454.autobook.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.autobook.cis454.autobook.Activities.EventActivity;
import com.autobook.cis454.autobook.Adapters.EventRecyclerAdapter;
import com.autobook.cis454.autobook.Event.Event;
import com.autobook.cis454.autobook.Helpers.Storage;
import com.autobook.cis454.autobook.R;
import com.autobook.cis454.autobook.Scheduler.AlarmManagerBroadcastReceiver;

import java.util.Date;

public class EventsDialogFragment extends DialogFragment {

    public static final String INTENT_EXTRA_EVENT = "EXTRA_EVENT";

    private RecyclerView recyclerView;
    private EventRecyclerAdapter recyclerAdapter;

    private Date chosenDate;
    public static final String ARG_DATE = "ARGUMENT_DATE";

    public EventsDialogFragment() {

    }

    public static EventsDialogFragment newInstance(Date date) {
        EventsDialogFragment fragment = new EventsDialogFragment();
        Bundle arguments = new Bundle();
        arguments.putSerializable(ARG_DATE,date);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        chosenDate = (Date) getArguments().getSerializable(ARG_DATE);

        View rootView = getActivity().getLayoutInflater().inflate(R.layout.fragment_event_dialog, null, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_widget);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        recyclerAdapter = new EventRecyclerAdapter(Storage.getEventsForDate(chosenDate));

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
                intent.putExtra(INTENT_EXTRA_EVENT, eventCopy);
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
                                Date date = thisEvent.getDate();
                                Storage.deleteEvent(thisEvent);
                                AlarmManagerBroadcastReceiver alarm = new AlarmManagerBroadcastReceiver();
                                alarm.cancelAlarm(getActivity().getApplicationContext(), thisEvent);
                                recyclerAdapter.getEventList().remove(pos);
                                recyclerAdapter.notifyItemRemoved(pos);
                                CalendarFragment.refreshEventCell(date);
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

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        return alertDialogBuilder
                .setView(rootView)
                .create();
    }

    @Override
    public void onResume() {
        super.onResume();
        recyclerAdapter.setEventList(Storage.getEventsForDate(chosenDate));
        recyclerAdapter.notifyDataSetChanged();
    }
}
