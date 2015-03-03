package com.autobook.cis454.autobook.DatabaseTesting.Receiver;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.autobook.cis454.autobook.R;


public class ReceiverActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_receiver_front_page, container, false);

            Button receiverList = (Button) rootView.findViewById(R.id.all_receivers_button);
            Button newReceiver = (Button) rootView.findViewById(R.id.new_receiver_button);

            receiverList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, new ReceiverListFragment())
                            .addToBackStack(null)
                            .commit();
                }
            });

            newReceiver.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, new AddReceiverFragment())
                            .addToBackStack(null)
                            .commit();
                }
            });
            return rootView;
        }
    }



}
