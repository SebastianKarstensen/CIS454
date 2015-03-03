package com.autobook.cis454.autobook.DatabaseTesting.Message;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.autobook.cis454.autobook.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MessageReceiverRecyclerAdapter extends RecyclerView.Adapter<MessageReceiverRecyclerAdapter.ViewHolder> {

    private ArrayList<HashMap<String, ?>> dataSet;
    private ArrayList<HashMap<String, ?>> messageList;
    private Context context;
    OnItemClickListener mItemClickListener;

    public MessageReceiverRecyclerAdapter(Context myContext, ArrayList<HashMap<String, ?>> myDataSet){
        context = myContext;
        dataSet = myDataSet;
        }

    public void setMessageList(ArrayList<HashMap<String, ?>> list){
        messageList = list;
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
        System.out.println("messageList length: " + messageList.size());
        for(int i = 0; i < messageList.size(); i++){
            HashMap<String, ?> messageEntry = messageList.get(i);
            int receiverID = (Integer) messageEntry.get("receiverid");
            if(receiverID == position){
                holder.itemView.setBackgroundColor(Color.GREEN);
                return;
            }
        }
        holder.itemView.setBackgroundColor(Color.WHITE);
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


    public ViewHolder(View v) {
        super(v);
        name = (TextView) v.findViewById(R.id.nameText);
        facebook = (TextView) v.findViewById(R.id.facebookText);
        twitter = (TextView) v.findViewById(R.id.twitterText);
        phone = (TextView) v.findViewById(R.id.phoneText);


        v.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(mItemClickListener != null){
                    mItemClickListener.onItemClick(v, getPosition());
                }
            }
        });

        v.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(mItemClickListener != null){
                    mItemClickListener.onItemLongClick(v, getPosition());
                }
                return true;
            }
        });

    }
}

    public interface OnItemClickListener{
        public void onItemClick(View v, int position);
        public void onItemLongClick(View v, int position);
    }

    public void SetOnItemClickListener(final OnItemClickListener itemClickListener){
        this.mItemClickListener = itemClickListener;
    }

    public Object getItem(int position) {
        return dataSet.get(position);
    }

}
