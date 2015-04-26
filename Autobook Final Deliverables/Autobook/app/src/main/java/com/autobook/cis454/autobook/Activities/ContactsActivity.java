package com.autobook.cis454.autobook.Activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.autobook.cis454.autobook.Fragments.ContactsFragment;
import com.autobook.cis454.autobook.Fragments.EventFragment;
import com.autobook.cis454.autobook.R;

/**
 * Activity for the contacts
 */
public class ContactsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int id = getIntent().getIntExtra(EventFragment.INTENT_ARGUMENT_CONTACTS,0);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, ContactsFragment.newInstance(id))
                    .commit();
        }


    }
}
