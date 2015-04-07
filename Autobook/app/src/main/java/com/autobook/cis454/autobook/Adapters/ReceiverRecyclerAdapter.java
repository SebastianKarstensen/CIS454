package com.autobook.cis454.autobook.Adapters;


import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.autobook.cis454.autobook.Notifications.Receiver;
import com.autobook.cis454.autobook.R;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Sebastian on 27-03-2015.
 */
public class ReceiverRecyclerAdapter extends RecyclerView.Adapter<ReceiverRecyclerAdapter.ViewHolder> {

    private List<Receiver> receiverList;
    private OnItemClickListener listOnClickListener;

    public ReceiverRecyclerAdapter(List<Receiver> receiverList) {
        this.receiverList = receiverList;
    }

    public List<Receiver> getReceiverList() {
        return receiverList;
    }

    public void setReceiverList(List<Receiver> newList) { receiverList = newList; }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_row_contact, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindData(receiverList.get(position));
    }

    @Override
    public int getItemCount() {
        return receiverList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView name, number, facebook, twitter;

        public ViewHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.txt_receiverRow_name);
            number = (TextView) itemView.findViewById(R.id.txt_receiverRow_phone);
            facebook = (TextView) itemView.findViewById(R.id.txt_receiverRow_facebook);
            twitter = (TextView) itemView.findViewById(R.id.txt_receiverRow_twitter);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listOnClickListener != null) {
                        listOnClickListener.onItemClick(v,getPosition());
                    }
                }
            });
        }

        public void bindData(Receiver receiver) {
            name.setText(receiver.getName());
            number.setText(receiver.getPhoneNumber());
            facebook.setText(receiver.getFacebookAccount());
            twitter.setText(receiver.getTwitterAccount());
            if(receiver.getSelected()) {
                itemView.setBackgroundColor(Color.GREEN);
            }
            else {
                itemView.setBackgroundResource(R.drawable.background_gradient_list_row);
            }
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View v, int pos);
    }

    public void setOnItemClickListener(final OnItemClickListener listener) {
        listOnClickListener = listener;
    }
}
