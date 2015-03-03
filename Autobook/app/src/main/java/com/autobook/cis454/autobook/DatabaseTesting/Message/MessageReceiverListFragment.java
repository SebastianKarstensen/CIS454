package com.autobook.cis454.autobook.DatabaseTesting.Message;


import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.autobook.cis454.autobook.DatabaseTesting.Database.MyDatabaseHandler;
import com.autobook.cis454.autobook.R;


public class MessageReceiverListFragment extends android.support.v4.app.Fragment {

    RecyclerView.LayoutManager mLayoutManager;
    MyDatabaseHandler db;
    int eventID;

    public static MessageReceiverListFragment newInstance(String event) {

        MessageReceiverListFragment fragment = new MessageReceiverListFragment();
        Bundle args = new Bundle();
        args.putString("event", event);
        fragment.setArguments(args);
        return fragment;
    }

    public MessageReceiverListFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        db = new MyDatabaseHandler(activity);
    }

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setRetainInstance(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recycler_view, container, false);
        String eventid = getArguments().getString("event");
        eventID = Integer.parseInt(eventid);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        setRetainInstance(true);

        final MessageReceiverRecyclerAdapter adapter = new MessageReceiverRecyclerAdapter(getActivity(), db.getReceiverList());
        recyclerView.setAdapter(adapter);

        //tell the adapter which people are already in the database for this event
        db.updateMessageList();
        adapter.setMessageList(db.getReceiversForEvent(eventID));

        adapter.SetOnItemClickListener(new MessageReceiverRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
               //System.out.println("EventID: " + eventID + " ReceiverID: " + position);
               System.out.println("Does this already exist?: " + db.messageExist(eventID, position));
               if(db.messageExist(eventID, position)){
                   //receiver is already at even therefore delete
                   db.deleteMessage(eventID, position);
                   db.updateMessageList();
                   adapter.setMessageList(db.getReceiversForEvent(eventID));
                   adapter.notifyDataSetChanged();
               } else {
                   db.insertMessage(eventID, position);
                   db.updateMessageList();
                   adapter.setMessageList(db.getReceiversForEvent(eventID));
                   adapter.notifyDataSetChanged();
               }
               int maxEventID = db.maxEventId();
               System.out.println("@@@ maxeventid " + maxEventID);

            }

            @Override
            public void onItemLongClick(View v, int position) {

            }
        });
        return rootView;
    }
}