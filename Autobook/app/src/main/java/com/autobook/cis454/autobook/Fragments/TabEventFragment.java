package com.autobook.cis454.autobook.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.autobook.cis454.autobook.R;

public class TabEventFragment extends Fragment {

    EditText message;

    public String getMessage() {
        return message.getText().toString();
    }

    public void setMessage(String message) { this.message.setText(message); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_tab_event, container, false);

        int tabType = getArguments().getInt((EventFragment.BUNDLE_TAB_ARGUMENT),0);
        //Facebook = Type 0
        //Twitter = Type 1
        //TextMessage = Type 2

        TextView tip = (TextView) rootView.findViewById(R.id.textView_tab_tip);
        TextView maxCharacters = (TextView) rootView.findViewById(R.id.textView_tab_max_characters);
        message = (EditText) rootView.findViewById(R.id.editText_tab_message);

        int maxLength = 123;
        String tipMessage = "We put the Twitter handle of your receiver(s) in front of the message, so you don't need to tweet @ them!";

        switch (tabType) {
            //Facebook
            case 0:
                maxLength = 2000;
                tipMessage = "Remember to use emojis to express yourself! :) xD 0:-) <3";
                break;

            //Text
            case 2:
                tipMessage = "Proper grammar is key; words like 'cuz', 'gr8', '2nite' will not make people like you.";
                maxLength = 160;
                break;

            //Twitter
            default:
        }

        message.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
        message.setText(getArguments().getString(EventFragment.BUNDLE_MESSAGE_ARGUMENT));

        tip.setText("TIP: " + tipMessage);
        maxCharacters.setText("Max Characters: " + maxLength);

        return rootView;
    }
}