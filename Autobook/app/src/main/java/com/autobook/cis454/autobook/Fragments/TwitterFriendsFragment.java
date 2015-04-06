package com.autobook.cis454.autobook.Fragments;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.autobook.cis454.autobook.Adapters.EventRecyclerAdapter;
import com.autobook.cis454.autobook.Adapters.ReceiverRecyclerAdapter;
import com.autobook.cis454.autobook.Adapters.TwitterFriendsRecyclerAdapter;
import com.autobook.cis454.autobook.Helpers.Sorters;
import com.autobook.cis454.autobook.Helpers.Storage;
import com.autobook.cis454.autobook.Helpers.TwitterHelper;
import com.autobook.cis454.autobook.Notifications.Receiver;
import com.autobook.cis454.autobook.R;
import com.roomorama.caldroid.CaldroidFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import twitter4j.PagableResponseList;
import twitter4j.User;

/**
 * Created by Sebastian on 06-04-2015.
 */
public class TwitterFriendsFragment extends Fragment {

    private RecyclerView recyclerView;
    private TwitterFriendsRecyclerAdapter recyclerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_twitter_friends, container, false);

        List<User> users = new ArrayList<>();

        PagableResponseList<User> friends = null;
        try {
            friends = new TwitterHelper.GetTwitterFriends().execute().get();
            for(User user : friends) {
                users.add(user);
            }
            Collections.sort(users,new Sorters.SortBasedOnTwitterName());
            recyclerAdapter = new TwitterFriendsRecyclerAdapter(users);

            recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_twitter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(recyclerAdapter);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return rootView;
    }
}