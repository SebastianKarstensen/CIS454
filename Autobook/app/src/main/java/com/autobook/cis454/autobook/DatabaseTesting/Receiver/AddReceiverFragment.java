package com.autobook.cis454.autobook.DatabaseTesting.Receiver;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import domain.databasetesting.Database.MyDatabaseHandler;
import domain.databasetesting.R;

public class AddReceiverFragment extends Fragment {

    MyDatabaseHandler db;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        db = new MyDatabaseHandler(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_receiver, container, false);
        Button saveButton = (Button) rootView.findViewById(R.id.savebutton);
        final EditText nametextField = (EditText) rootView.findViewById(R.id.nameTextField);
        final EditText facebooktextField = (EditText) rootView.findViewById(R.id.facebookTextField);
        final EditText twittertextField = (EditText) rootView.findViewById(R.id.twitterTextField);
        final EditText phonetextField = (EditText) rootView.findViewById(R.id.phoneTextField);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nametextField.getText().toString();
                String facebook = facebooktextField.getText().toString();
                String twitter = twittertextField.getText().toString();
                String phone = phonetextField.getText().toString();

                db.insertReceiver(name, facebook, twitter, phone);

                nametextField.setText("");
                facebooktextField.setText("");
                twittertextField.setText("");
                phonetextField.setText("");
            }
        });

        final Button displayButton = (Button) rootView.findViewById(R.id.displaybutton);
        displayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    db.updateReceiverList();
                    ArrayList<HashMap<String, ?>> receiverList  = db.getReceiverList();
                    for (int i = 0; i < receiverList.size(); i++){
                        HashMap<String, ?> receiver = receiverList.get(i);
                        displayReceiver(receiver, getActivity());
                    }

                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        return rootView;
    }

    public void displayReceiver(HashMap<String, ?> receiver, Context ctx)
    {

        String name = (String) receiver.get("name");
        String facebook = (String) receiver.get("facebook");
        String twitter = (String) receiver.get("twitter");
        String phone = (String) receiver.get("phone");

        Toast.makeText(ctx,
                "Name: " + name + "\n" +
                        " Facebook: " + facebook + "\n" +
                        " Twitter: " + twitter + "\n" +
                        " Phone: " + phone
                , Toast.LENGTH_SHORT).show();
    }
}


