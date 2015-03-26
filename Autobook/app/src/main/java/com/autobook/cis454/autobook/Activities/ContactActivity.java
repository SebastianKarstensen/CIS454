package com.autobook.cis454.autobook.Activities;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.autobook.cis454.autobook.DatabaseTesting.Receiver.AddReceiverFragment;
import com.autobook.cis454.autobook.DatabaseTesting.Receiver.ReceiverListFragment;
import com.autobook.cis454.autobook.R;

public class ContactActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_contact_list);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.list_container, new ReceiverListFragment())
                    .commit();
        }

        Button newContactButton = (Button) findViewById(R.id.new_contact_button);
        newContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_container, new AddReceiverFragment())
                        .commit();
            }
        });


    }

   


}
