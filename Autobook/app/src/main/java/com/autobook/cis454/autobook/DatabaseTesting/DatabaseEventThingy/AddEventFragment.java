package com.autobook.cis454.autobook.DatabaseTesting.DatabaseEventThingy;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;

import com.autobook.cis454.autobook.DatabaseTesting.Database.MyDatabaseHandler;
import com.autobook.cis454.autobook.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddEventFragment extends Fragment {

    MyDatabaseHandler db;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        db = new MyDatabaseHandler(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_event, container, false);

        Button saveButton = (Button) rootView.findViewById(R.id.savebutton);
        final EditText eventTypeField = (EditText) rootView.findViewById(R.id.eventTypeTextField);
        final EditText facebookMessageField = (EditText) rootView.findViewById(R.id.facebookMessageTextField);
        final EditText twitterMessageField = (EditText) rootView.findViewById(R.id.twitterMessageTextField);
        final EditText phoneMessageField = (EditText) rootView.findViewById(R.id.textMessageTextField);
        final EditText titleField = (EditText) rootView.findViewById(R.id.titleMessageTextField);
        final CalendarView datePicker = (CalendarView) rootView.findViewById(R.id.datePicker);


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eventType = eventTypeField.getText().toString();
                String facebook = facebookMessageField.getText().toString();
                String twitter = twitterMessageField.getText().toString();
                String phone = phoneMessageField.getText().toString();
                String title = titleField.getText().toString();
                final long val = datePicker.getDate();
                Date date=new Date(val);
                SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yy");
                String dateText = df2.format(date);

                db.insertEvent(dateText,facebook, twitter, phone, eventType, title);

                eventTypeField.setText("");
                facebookMessageField.setText("");
                twitterMessageField.setText("");
                phoneMessageField.setText("");
                titleField.setText("");
            }
        });
        return rootView;
    }
}
