package com.autobook.cis454.autobook.DatabaseTesting.Receiver;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.autobook.cis454.autobook.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ReceiverRecyclerViewAdapter extends RecyclerView.Adapter<ReceiverRecyclerViewAdapter.ViewHolder> {

    private ArrayList<HashMap<String, ?>> dataSet;
    private Context context;


    public ReceiverRecyclerViewAdapter(Context myContext, ArrayList<HashMap<String, ?>> myDataSet){
        context = myContext;
        dataSet = myDataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_receiver, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Map<String, ?> entry = dataSet.get(position);
        holder.name.setText((String) entry.get("name"));
        holder.facebook.setText((String) entry.get("facebook"));
        holder.twitter.setText((String) entry.get("twitter"));
        holder.phone.setText((String) entry.get("phone"));
        holder.colorPallet.setBackgroundResource(R.color.card_view_background);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView facebook;
        TextView twitter;
        TextView phone;
        LinearLayout colorPallet;



        public ViewHolder(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.nameText);
            facebook = (TextView) v.findViewById(R.id.facebookText);
            twitter = (TextView) v.findViewById(R.id.twitterText);
            phone = (TextView) v.findViewById(R.id.phoneText);
            colorPallet = (LinearLayout) v.findViewById(R.id.cardlinearmain);

            v.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                     System.out.println("item click");
                }
            });

            v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    System.out.println("item long click");
                    return true;
                }
            });

        }
    }

    public Object getItem(int position) {
        return dataSet.get(position);
    }

}

