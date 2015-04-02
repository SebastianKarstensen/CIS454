package com.autobook.cis454.autobook.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.autobook.cis454.autobook.Event.Event;
import com.autobook.cis454.autobook.R;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Sebastian on 27-03-2015.
 */
public class EventRecyclerAdapter extends RecyclerView.Adapter<EventRecyclerAdapter.ViewHolder> {

    private List<Event> eventList;

    public EventRecyclerAdapter(List<Event> eventList) {
        this.eventList = eventList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_row_event, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindData(eventList.get(position));
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView name, date, time, receivers, type;

        public ViewHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.txt_eventRow_name);
            date = (TextView) itemView.findViewById(R.id.txt_eventRow_date);
            time = (TextView) itemView.findViewById(R.id.txt_eventRow_time);
            receivers = (TextView) itemView.findViewById(R.id.txt_eventRow_receivers);
            type = (TextView) itemView.findViewById(R.id.txt_eventRow_type);
        }

        public void bindData(Event event) {
            name.setText(event.getTitle());

            SimpleDateFormat dfDate = new SimpleDateFormat("MM/dd/yyyy");
            SimpleDateFormat dfTime = new SimpleDateFormat("h:mm a");
            String dateString = dfDate.format(event.getDate());
            String dateTime = dfTime.format(event.getDate());

            date.setText(dateString);
            time.setText(dateTime);
            //receivers.setText(0);
            //receivers.setText(event.getReceivers().size());
            type.setText(event.getType().toString());
        }
    }
}
