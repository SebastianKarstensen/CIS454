package com.autobook.cis454.autobook.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.autobook.cis454.autobook.Helpers.Storage;
import com.autobook.cis454.autobook.Notifications.Receiver;
import com.autobook.cis454.autobook.R;

/**
 * Created by Sebastian on 31-03-2015.
 */
public class ContactsDetailFragment extends Fragment {

    private static final String ARG_RECEIVER = "ARGUMENT_RECEIVER";
    private Receiver receiver;
    private boolean isNewContact;

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
            isNewContact = false;
        }
        else {
            receiver = new Receiver("","","","",0);
            isNewContact = true;
        }

        View rootView = inflater.inflate(R.layout.fragment_contact_details, container, false);

        final EditText name = (EditText) rootView.findViewById(R.id.editText_contactDetails_name);
        final Button buttonFacebook = (Button) rootView.findViewById(R.id.btn_contactDetails_facebook);
        final Button buttonTwitter = (Button) rootView.findViewById(R.id.btn_contactDetails_twitter);
        final Button buttonText = (Button) rootView.findViewById(R.id.btn_contactDetails_contact);
        Button buttonSave = (Button) rootView.findViewById(R.id.btn_contactDetails_save);
        Button buttonCancel = (Button) rootView.findViewById(R.id.btn_contactDetails_cancel);

        if(!isNewContact) {
            name.setText(receiver.getName());
            if(receiver.getFacebookAccount() != "") {
                buttonFacebook.setText(receiver.getFacebookAccount());
            }
            if(receiver.getTwitterAccount() != "") {
                buttonTwitter.setText(receiver.getTwitterAccount());
            }
            if(receiver.getPhoneNumber() != "") {
                buttonText.setText(receiver.getPhoneNumber());
            }
        }

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(name.getText().toString() == "") {
                    makeToast(getActivity(),"Please input the name of the contact");
                    return;
                }
                receiver.setName(name.getText().toString());
                if(!isNewContact) {
                    Storage.updateReceiver(receiver);
                }
                else{
                    Storage.insertReceiver(receiver);
                }
                getActivity().onBackPressed();
            }
        });
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        return rootView;
    }

    public void makeToast(Context context, String message) {
        Toast toast = Toast.makeText(context,message,Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}