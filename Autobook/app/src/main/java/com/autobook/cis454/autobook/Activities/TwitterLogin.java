package com.autobook.cis454.autobook.Activities;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.autobook.cis454.autobook.Fragments.TwitterWebFragment;
import com.autobook.cis454.autobook.R;

public class TwitterLogin extends ActionBarActivity {

    public static final String ARG_FRAGMENT_ID = "ARGUMENT_FRAGMENT_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter_login);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, TwitterLoginFragment.newInstance(1))
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_twitter_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class TwitterLoginFragment extends Fragment {

        private int fragmentId;

        public TwitterLoginFragment() {
        }

        public static Fragment newInstance(int fragmentId) {
            TwitterLoginFragment fragment = new TwitterLoginFragment();
            Bundle arguments = new Bundle();
            arguments.putSerializable(ARG_FRAGMENT_ID,fragmentId);
            fragment.setArguments(arguments);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            fragmentId = getArguments().getInt(ARG_FRAGMENT_ID);

            View rootView = inflater.inflate(R.layout.fragment_twitter_login, container, false);

            Button loginTwitter = (Button) rootView.findViewById(R.id.button_twitter_login);
            loginTwitter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                            .replace(R.id.container, new TwitterWebFragment())
                            .addToBackStack(null)
                            .commit();
                }
            });

            TextView header = (TextView) rootView.findViewById(R.id.textView_loginToTwitter);
            header.setText(header.getText() + ": " + fragmentId);

            return rootView;
        }
    }
}
