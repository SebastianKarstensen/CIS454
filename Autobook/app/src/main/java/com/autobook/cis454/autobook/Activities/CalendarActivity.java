package com.autobook.cis454.autobook.Activities;

import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.autobook.cis454.autobook.Calendar.EventRecyclerAdapter;
import com.autobook.cis454.autobook.Event.Event;
import com.autobook.cis454.autobook.Event.EventType;
import com.autobook.cis454.autobook.Notifications.Receiver;
import com.autobook.cis454.autobook.R;
import com.roomorama.caldroid.CaldroidFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class CalendarActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new CalendarFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_calendar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class CalendarFragment extends Fragment {

        private RecyclerView recyclerView;
        private EventRecyclerAdapter recyclerAdapter;
        private ScrollView scrollView;

        public CalendarFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_calendar, container, false);

            CaldroidFragment caldroidFragment = new CaldroidFragment();
            Bundle args = new Bundle();
            Calendar cal = Calendar.getInstance();
            args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
            args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
            args.putInt(CaldroidFragment.START_DAY_OF_WEEK, CaldroidFragment.MONDAY);
            caldroidFragment.setArguments(args);

            getFragmentManager().beginTransaction()
                    .replace(R.id.container_calendar, caldroidFragment)
                    .commit();

            recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_widget);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            makeMyScrollSmart();

            List<Event> events = new ArrayList<>();
            HomeActivity.dbHandler.updateEventList();
            ArrayList<HashMap<String,?>> eventList = HomeActivity.dbHandler.getEventList();
            //Event event = eventList.get(0).get((String) "id");


            List<Receiver> receivers = new ArrayList<>();
            for(int i = 0; i < 100; i++) {
                Event event = new Event(i,"Event #" + i, new Date(), EventType.American_Holiday, receivers);
                events.add(event);
            }

            recyclerAdapter = new EventRecyclerAdapter(events);
            recyclerView.setAdapter(recyclerAdapter);

            return rootView;
        }

        private void makeMyScrollSmart() {
            recyclerView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View __v, MotionEvent __event) {
                    if (__event.getAction() == MotionEvent.ACTION_DOWN) {
                        //  Disallow the touch request for parent scroll on touch of child view
                        requestDisallowParentInterceptTouchEvent(__v, true);
                    } else if (__event.getAction() == MotionEvent.ACTION_UP || __event.getAction() == MotionEvent.ACTION_CANCEL) {
                        // Re-allows parent events
                        requestDisallowParentInterceptTouchEvent(__v, false);
                    }
                    return false;
                }
            });
        }

        private void requestDisallowParentInterceptTouchEvent(View __v, Boolean __disallowIntercept) {
            while (__v.getParent() != null && __v.getParent() instanceof View) {
                if (__v.getParent() instanceof ScrollView) {
                    __v.getParent().requestDisallowInterceptTouchEvent(__disallowIntercept);
                }
                __v = (View) __v.getParent();
            }
        }
    }
}
