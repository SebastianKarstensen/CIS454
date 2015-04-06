package com.autobook.cis454.autobook.Adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.autobook.cis454.autobook.Helpers.Autobook;
import com.autobook.cis454.autobook.Helpers.Converters;
import com.autobook.cis454.autobook.Notifications.Receiver;
import com.autobook.cis454.autobook.R;

import java.io.InputStream;
import java.util.List;

import twitter4j.User;

/**
 * Created by Sebastian on 06-04-2015.
 */
public class TwitterFriendsRecyclerAdapter extends RecyclerView.Adapter<TwitterFriendsRecyclerAdapter.ViewHolder> {

    private List<User> users;
    private OnItemClickListener listOnClickListener;

    public TwitterFriendsRecyclerAdapter(List<User> users) {
        this.users = users;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_row_face_twit, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindData(users.get(position));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView name, username;
        private ImageView profilePic;

        public ViewHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.textView_row_name);
            username = (TextView) itemView.findViewById(R.id.textView_row_username);
            profilePic = (ImageView) itemView.findViewById(R.id.imageView_face_twit_profile);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listOnClickListener != null) {
                        listOnClickListener.onItemClick(v,getPosition());
                    }
                }
            });
        }

        public void bindData(User user) {
            name.setText(user.getName());
            username.setText("@" + user.getScreenName());
            new DownloadImageTask(profilePic).execute(user.getBiggerProfileImageURL());
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View v, int pos);
    }

    public void setOnItemClickListener(final OnItemClickListener listener) {
        listOnClickListener = listener;
    }

    public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String imageURL = urls[0];
            Bitmap image = null;
            try {
                InputStream in = new java.net.URL(imageURL).openStream();
                image = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return image;
        }

        protected void onPostExecute(Bitmap result) {
            Drawable icon = new BitmapDrawable(Autobook.getAppContext().getResources(),result);
            bmImage.setBackground(icon);
        }
    }
}