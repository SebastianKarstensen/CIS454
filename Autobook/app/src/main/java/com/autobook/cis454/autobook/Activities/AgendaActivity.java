package com.autobook.cis454.autobook.Activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.autobook.cis454.autobook.Fragments.AgendaFragment;
import com.autobook.cis454.autobook.Fragments.CalendarFragment;
import com.autobook.cis454.autobook.R;

/**
 * Created by Sebastian on 11-04-2015.
 */
public class AgendaActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new AgendaFragment())
                    .commit();
        }
    }
}
