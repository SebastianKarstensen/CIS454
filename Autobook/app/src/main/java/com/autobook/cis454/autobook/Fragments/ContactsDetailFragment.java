package com.autobook.cis454.autobook.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.autobook.cis454.autobook.Adapters.EventRecyclerAdapter;
import com.autobook.cis454.autobook.Adapters.ReceiverRecyclerAdapter;
import com.autobook.cis454.autobook.Helpers.Storage;
import com.autobook.cis454.autobook.Notifications.Receiver;
import com.autobook.cis454.autobook.R;
import com.roomorama.caldroid.CaldroidFragment;

import java.util.Calendar;

/**
 * Created by Sebastian on 31-03-2015.
 */
public class ContactsDetailFragment extends Fragment {

    private static final String ARG_RECEIVER = "ARGUMENT_RECEIVER";
    private Receiver receiver;
    private boolean isNewEvent;

    public static ContactsDetailFragment newInstance(Receiver receiver) {
        ContactsDetailFragment fragment = new ContactsDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_RECEIVER, receiver);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(getArguments().getSerializable(ARG_RECEIVER) != null) {
            receiver = (Receiver) getArguments().getSerializable(ARG_RECEIVER);
            isNewEvent = false;
        }
        else {
            isNewEvent = true;
        }

        View rootView = inflater.inflate(R.layout.fragment_contact_details, container, false);

        EditText name = (EditText) rootView.findViewById(R.id.editText_contactDetails_name);
        Button buttonFacebook = (Button) rootView.findViewById(R.id.btn_contactDetails_facebook);
        Button buttonTwitter = (Button) rootView.findViewById(R.id.btn_contactDetails_twitter);
        Button buttonText = (Button) rootView.findViewById(R.id.btn_contactDetails_contact);
        Button buttonCancel = (Button) rootView.findViewById(R.id.btn_contactDetails_cancel);

        if(!isNewEvent) {
            name.setText(receiver.getName());
            buttonFacebook.setText(receiver.getFacebookAccount());
            buttonTwitter.setText(receiver.getTwitterAccount());
            buttonText.setText(receiver.getPhoneNumber());
        }

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        return rootView;
    }
}