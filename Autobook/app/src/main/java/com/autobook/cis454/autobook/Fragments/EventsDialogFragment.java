package com.autobook.cis454.autobook.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.autobook.cis454.autobook.Activities.CalendarActivity;
import com.autobook.cis454.autobook.Activities.EventActivity;
import com.autobook.cis454.autobook.Adapters.EventRecyclerAdapter;
import com.autobook.cis454.autobook.Event.Event;
import com.autobook.cis454.autobook.Helpers.Autobook;
import com.autobook.cis454.autobook.Helpers.Storage;
import com.autobook.cis454.autobook.R;

import java.util.Date;

/**
 * Created by Sebastian on 11-04-2015.
 */
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
            public void onItemLongClick(View v, int pos) {
                Toast.makeText(getActivity(),"DELETE",Toast.LENGTH_SHORT).show();
            }
        });

        recyclerView.setAdapter(recyclerAdapter);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        return alertDialogBuilder
                .setView(rootView)
                .create();
    }
}
