package com.autobook.cis454.autobook.DatabaseTesting.FrontPage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.autobook.cis454.autobook.DatabaseTesting.DatabaseEventThingy.EventActivity;
import com.autobook.cis454.autobook.DatabaseTesting.Message.MessageActivity;
import com.autobook.cis454.autobook.DatabaseTesting.Receiver.ReceiverActivity;
import com.autobook.cis454.autobook.R;

public class FrontpageFragment extends Fragment {

    public FrontpageFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_front_page, container, false);
        Button events = (Button) rootView.findViewById(R.id.event_button);
        Button receivers = (Button) rootView.findViewById(R.id.receiver_button);
        Button messages = (Button) rootView.findViewById(R.id.messages_button);

        events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EventActivity.class);
                startActivity(intent);
            }
        });

        receivers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ReceiverActivity.class);
                startActivity(intent);
            }
        });

        messages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MessageActivity.class);
                startActivity(intent);
            }
        });
        return rootView;
    }
}





