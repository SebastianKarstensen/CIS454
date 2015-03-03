package com.autobook.cis454.autobook.DatabaseTesting.Message;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.autobook.cis454.autobook.DatabaseTesting.Database.MyDatabaseHandler;
import com.autobook.cis454.autobook.R;

public class MessageEventFragment extends Fragment {

    RecyclerView.LayoutManager mLayoutManager;
    MyDatabaseHandler db;

    public MessageEventFragment() {
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

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        setRetainInstance(true);

        db.updateEventList();
        final MessageEventRecyclerAdapter adapter = new MessageEventRecyclerAdapter(getActivity(), db.getEventList());
        recyclerView.setAdapter(adapter);

        adapter.SetOnItemClickListener(new MessageEventRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                db.updateEventList();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, DetailedEventFragment.newInstance(position))
                        .commit();
            }

            @Override
            public void onItemLongClick(View v, int position) {

            }
        });

        return rootView;
    }
}