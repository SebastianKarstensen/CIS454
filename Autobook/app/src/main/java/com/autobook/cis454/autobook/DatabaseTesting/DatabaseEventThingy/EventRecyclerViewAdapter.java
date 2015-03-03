package com.autobook.cis454.autobook.DatabaseTesting.DatabaseEventThingy;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.autobook.cis454.autobook.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EventRecyclerViewAdapter extends RecyclerView.Adapter<EventRecyclerViewAdapter.ViewHolder>{

    private ArrayList<HashMap<String, ?>> dataSet;
    private Context context;


    public EventRecyclerViewAdapter(Context myContext, ArrayList<HashMap<String, ?>> myDataSet){
        context = myContext;
        dataSet = myDataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_event, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Map<String, ?> entry = dataSet.get(position);
        holder.date.setText((String) entry.get("date"));
        holder.facebookMessage.setText((String) entry.get("facebookMessage"));
        holder.twitterMessage.setText((String) entry.get("twitterMessage"));
        holder.textMessage.setText((String) entry.get("textMessage"));
        holder.eventType.setText((String) entry.get("eventType"));
        holder.title.setText((String) entry.get("title"));
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView date;
        TextView facebookMessage;
        TextView twitterMessage;
        TextView textMessage;
        TextView eventType;
        TextView title;

        public ViewHolder(View v) {
            super(v);
            date = (TextView) v.findViewById(R.id.dateText);
            facebookMessage = (TextView) v.findViewById(R.id.facebookMessageText);
            twitterMessage = (TextView) v.findViewById(R.id.twitterMessageText);
            textMessage = (TextView) v.findViewById(R.id.textMessageText);
            eventType = (TextView) v.findViewById(R.id.eventTypeText);
            title = (TextView) v.findViewById(R.id.titleText);

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

}
