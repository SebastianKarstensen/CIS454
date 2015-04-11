package com.autobook.cis454.autobook.Fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.autobook.cis454.autobook.Activities.AgendaActivity;
import com.autobook.cis454.autobook.Activities.CalendarActivity;
import com.autobook.cis454.autobook.Adapters.EventRecyclerAdapter;
import com.autobook.cis454.autobook.Event.Event;
import com.autobook.cis454.autobook.Helpers.Converters;
import com.autobook.cis454.autobook.Helpers.Storage;
import com.autobook.cis454.autobook.R;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * A placeholder fragment containing a simple view.
 */
public class CalendarFragment extends Fragment {

    public CalendarFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_calendar, container, false);

        Button buttonSwitchView = (Button) rootView.findViewById(R.id.btn_calendar_switch_agenda);
        buttonSwitchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AgendaActivity.class);
                startActivity(intent);
            }
        });

        CaldroidFragment caldroidFragment = new CaldroidFragment();
        Bundle args = new Bundle();
        Calendar cal = Calendar.getInstance();
        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
        args.putInt(CaldroidFragment.START_DAY_OF_WEEK, CaldroidFragment.MONDAY);
        caldroidFragment.setArguments(args);

        caldroidFragment.setCaldroidListener(new CaldroidListener() {
            @Override
            public void onSelectDate(Date date, View view) {
                EventsDialogFragment dialog = EventsDialogFragment.newInstance(date);
                dialog.show(getFragmentManager(), Converters.convertDateToString(date));
            }
        });

        HashMap<Date,Integer> eventDatesBackground = new HashMap<>();
        HashMap<Date,Integer> eventsDatesText = new HashMap<>();
        for(Event event : Storage.getEventsFromDatabase()) {
            eventDatesBackground.put(event.getDate(),R.drawable.selected_cell_bg);
            eventsDatesText.put(event.getDate(),R.color.off_white);
        }
        caldroidFragment.setBackgroundResourceForDates(eventDatesBackground);
        caldroidFragment.setTextColorForDates(eventsDatesText);

        getFragmentManager().beginTransaction()
                .replace(R.id.container_calendar, caldroidFragment)
                .commit();

        return rootView;
    }
}
