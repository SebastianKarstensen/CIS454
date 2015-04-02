package com.autobook.cis454.autobook.Activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.autobook.cis454.autobook.Fragments.CalendarFragment;
import com.autobook.cis454.autobook.Fragments.ContactsFragment;
import com.autobook.cis454.autobook.R;

/**
 * Created by Sebastian on 31-03-2015.
 */
public class ContactsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, ContactsFragment.newInstance(0))
                    .commit();
        }


    }
}
