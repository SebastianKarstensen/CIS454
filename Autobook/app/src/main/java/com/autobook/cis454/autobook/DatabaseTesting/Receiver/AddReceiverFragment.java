package com.autobook.cis454.autobook.DatabaseTesting.Receiver;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.autobook.cis454.autobook.DatabaseTesting.Database.MyDatabaseHandler;
import com.autobook.cis454.autobook.R;

import java.util.ArrayList;
import java.util.HashMap;

public class AddReceiverFragment extends Fragment {

    MyDatabaseHandler db;
    public static final int PICK_CONTACT_REQUEST = 5;
    TextView phoneResult;
    EditText nameResult;

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
        Button facebookButton = (Button) rootView.findViewById(R.id.facebookButton);
        Button twitterButton = (Button) rootView.findViewById(R.id.twitterButton);
        Button phoneButton = (Button) rootView.findViewById(R.id.phoneButton);
        final EditText nametextField = (EditText) rootView.findViewById(R.id.nameTextField);
        nameResult = (EditText) rootView.findViewById(R.id.nameTextField);
        phoneResult = (TextView) rootView.findViewById(R.id.phonenumbertextiview);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nametextField.getText().toString();


                nametextField.setText("");

//                db.insertReceiver(name, facebook, twitter, phone);

                nametextField.setText("");
            }
        });

        phoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
                intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                startActivityForResult(intent, PICK_CONTACT_REQUEST);
            }
        });



        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){

        if(resultCode != Activity.RESULT_OK) return;

        if(requestCode == PICK_CONTACT_REQUEST) {
            Uri contactUri = data.getData();

            String[] projection = {
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                    ContactsContract.CommonDataKinds.Phone.NUMBER};

            Cursor cursor = getActivity().getContentResolver()
                    .query(contactUri, projection, null, null, null);
            cursor.moveToFirst();

            int column = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            String number = cursor.getString(column);
            column = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            String name = cursor.getString(column);

            phoneResult.setText(number);
            nameResult.setText(name);
        }
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


