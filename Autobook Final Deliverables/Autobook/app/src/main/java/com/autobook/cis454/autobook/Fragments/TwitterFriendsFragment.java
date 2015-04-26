package com.autobook.cis454.autobook.Fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.autobook.cis454.autobook.Adapters.TwitterFriendsRecyclerAdapter;
import com.autobook.cis454.autobook.Helpers.Autobook;
import com.autobook.cis454.autobook.Helpers.Sorters;
import com.autobook.cis454.autobook.Helpers.TwitterHelper;
import com.autobook.cis454.autobook.R;
import com.autobook.cis454.autobook.Scheduler.AlarmManagerBroadcastReceiver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import twitter4j.PagableResponseList;
import twitter4j.User;

public class TwitterFriendsFragment extends DialogFragment {

    public static final String ARG_TWITTER_HANDLE = "ARGUMENT_TWITTER_HANDLE";
    public static final String ARG_TWITTER_PROFILE_PIC = "ARGUMENT_TWITTER_PROFILE_PIC";

    private RecyclerView recyclerView;
    private TwitterFriendsRecyclerAdapter recyclerAdapter;
    private List<User> users;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View rootView = getActivity().getLayoutInflater().inflate(R.layout.fragment_twitter_friends, null, false);

        users = new ArrayList<>();

        PagableResponseList<User> friends = null;

        if(AlarmManagerBroadcastReceiver.isNetworkAvailable(Autobook.getAppContext()) && TwitterHelper.isTwitterLoggedIn()) {
            try {
                friends = new TwitterHelper.GetTwitterFriends().execute().get();
                for (User user : friends) {
                    users.add(user);
                }
                Collections.sort(users, new Sorters.SortBasedOnTwitterName());
                recyclerAdapter = new TwitterFriendsRecyclerAdapter(users);
                recyclerAdapter.setOnItemClickListener(new TwitterFriendsRecyclerAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View v, int pos) {
                        if (getTargetFragment() != null) {
                            Intent i = new Intent();
                            i.putExtra(ARG_TWITTER_HANDLE, users.get(pos).getScreenName());
                            i.putExtra(ARG_TWITTER_PROFILE_PIC, users.get(pos).getBiggerProfileImageURL());
                            getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, i);
                            dismiss();
                        }
                    }
                });

                recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_twitter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerView.setAdapter(recyclerAdapter);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        return alertDialogBuilder
                .setView(rootView)
                .create();
    }
}