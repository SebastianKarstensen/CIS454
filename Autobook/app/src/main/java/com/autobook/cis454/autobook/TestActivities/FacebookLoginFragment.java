package com.autobook.cis454.autobook.TestActivities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.SharedPreferences;

import com.autobook.cis454.autobook.R;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareContent;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.SendButton;

import com.google.gson.Gson;

import org.json.JSONObject;
import org.w3c.dom.Text;

public class FacebookLoginFragment extends Fragment
{
    LoginButton loginButton;
    Button logoutButton;
    CallbackManager callbackManager;
    Button statusUpdateButton;
    /*SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
    String json = mSharedPreferences.getString("fb_access_token","");
    Gson gson = new Gson();
    AccessToken accessToken = gson.fromJson(json, AccessToken.class);*/
    AccessToken accessToken = AccessToken.getCurrentAccessToken();
    AccessTokenTracker accessTokenTracker;
    ShareContent shareContent;
    ShareContent.Builder builder;
    Uri uri = Uri.parse("google.com");
    EditText fbMsgTextView;

    public FacebookLoginFragment()
    {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        callbackManager = CallbackManager.Factory.create();
        shareContent = new ShareLinkContent.Builder().setContentUrl(uri).build();
        


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_facebook_login, container, false);
        fbMsgTextView = (EditText) view.findViewById(R.id.editTextFBMsg);

        loginButton = (LoginButton) view.findViewById(R.id.login_button);
        //loginButton.setReadPermissions("user_friends");
        // If using in a fragment
        loginButton.setFragment(this);
        // Other app specific specialization
        loginButton.setPublishPermissions("publish_actions");
        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                accessToken = loginResult.getAccessToken();

                Toast.makeText(getActivity(),"Login Success!", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancel() {
                // App code
                Toast.makeText(getActivity(),"Login canceled", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Toast.makeText(getActivity(), exception.toString(), Toast.LENGTH_LONG).show();
            }
        });

        logoutButton = (Button) view.findViewById(R.id.button_fb_logout);
        logoutButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                /*GraphRequest request = GraphRequest.newMeRequest(
                        accessToken,
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {
                                // Application code
                               Toast.makeText(getActivity(),response.getRawResponse(),Toast.LENGTH_LONG).show();
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,link");
                request.setParameters(parameters);
                request.executeAsync();*/
                GraphRequest.GraphJSONObjectCallback callback = new GraphRequest.GraphJSONObjectCallback()
                {
                    @Override
                    public void onCompleted(JSONObject object,GraphResponse response)
                    {
                        Toast.makeText(getActivity(), response.getRawResponse(),Toast.LENGTH_LONG).show();
                    }
                };



                Bundle params = new Bundle();
                params.putString("message", fbMsgTextView.getText().toString());
/* make the API call */
                new GraphRequest(
                        accessToken,
                        "/me/feed",
                        params,
                        HttpMethod.POST,
                        new GraphRequest.Callback() {
                            public void onCompleted(GraphResponse response) {
            /* handle the result */
                                Toast.makeText(getActivity(),response.getRawResponse(),Toast.LENGTH_LONG).show();
                            }
                        }
                ).executeAsync();

            }
        });


        SendButton sendButton = (SendButton) view.findViewById(R.id.button_status_update);
        sendButton.setFragment(this);
        sendButton.setShareContent(shareContent);
        sendButton.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>()
        {
            @Override
            public void onSuccess(Sharer.Result result)
            {
                Toast.makeText(getActivity(),"Send success", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancel()
            {
                Toast.makeText(getActivity(),"Send cancel", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException e)
            {
                Toast.makeText(getActivity(),e.toString(), Toast.LENGTH_LONG).show();
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

}
