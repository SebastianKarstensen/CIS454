package com.autobook.cis454.autobook.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.autobook.cis454.autobook.Adapters.ReceiverRecyclerAdapter;
import com.autobook.cis454.autobook.Helpers.Storage;
import com.autobook.cis454.autobook.Notifications.Receiver;
import com.autobook.cis454.autobook.R;

import java.util.ArrayList;

public class ContactsFragment extends Fragment {

    public static final String ARG_EVENT = "ARGUMENT_EVENT";
    public static final String ARG_ID = "ARGUMENT_ID";

    //If id = 0, then previous screen was Home. If id = 1, previous screen was Event.
    private int id = 0;

    private RecyclerView recyclerView;
    private ReceiverRecyclerAdapter recyclerAdapter = new ReceiverRecyclerAdapter(Storage.getReceiversFromDatabase());

    private Button buttonAddNewContact;
    private Button buttonReturnReceivers;

    public static ContactsFragment newInstance(int previousId) {
        ContactsFragment fragment = new ContactsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_ID, previousId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        id = getArguments().getInt(ARG_ID,0);

        View rootView = inflater.inflate(R.layout.fragment_contacts, container, false);

        buttonAddNewContact = (Button) rootView.findViewById(R.id.btn_contacts_add_new);
        buttonReturnReceivers = (Button) rootView.findViewById(R.id.btn_contacts_choose_receivers);

        buttonAddNewContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, ContactsDetailFragment.newInstance(null))
                        .addToBackStack(null)
                        .commit();
            }
        });

        switch (id) {
            case 1:
                buttonReturnReceivers.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        returnContacts();
                    }
                });
                break;
            default:
                buttonReturnReceivers.setVisibility(View.GONE);
        }

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_contacts);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerAdapter.setOnItemClickListener(new ReceiverRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                Receiver receiver = recyclerAdapter.getReceiverList().get(pos);

                switch (id) {
                    case 1:
                        receiver.setSelected(!receiver.getSelected());
                        if(receiver.getSelected()) {
                            v.setBackgroundResource(R.drawable.background_gradient_list_row_selected);
                        }
                        else {
                            v.setBackgroundResource(R.drawable.background_gradient_list_row);
                        }
                        break;
                    default:
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, ContactsDetailFragment.newInstance(receiver))
                                .addToBackStack(null)
                                .commit();
                }
            }

            @Override
            public void onItemLongClick(View v, final int pos) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Delete entry")
                        .setMessage("Are you sure you want to delete this contact?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Receiver thisReceiver = recyclerAdapter.getReceiverList().get(pos);
                                Storage.deleteReceiver(thisReceiver);
                                recyclerAdapter.notifyItemRemoved(pos);
                                recyclerAdapter.setReceiverList(Storage.getReceiversFromDatabase());
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });

        recyclerView.setAdapter(recyclerAdapter);

        return rootView;
    }

    public void returnContacts() {
        ArrayList<Receiver> listOfReceivers = new ArrayList<>();
        for(Receiver receiver : recyclerAdapter.getReceiverList()) {
            if(receiver.getSelected()) {
                listOfReceivers.add(receiver);
            }
        }
        Intent i = new Intent();
        i.putExtra(ARG_EVENT,listOfReceivers);
        getActivity().setResult(Activity.RESULT_OK,i);
        getActivity().finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        recyclerAdapter.setReceiverList(Storage.getReceiversFromDatabase());
        recyclerAdapter.notifyDataSetChanged();
    }
}
