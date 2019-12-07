package com.example.hush;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class PostAdapter extends BaseAdapter {
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;

    public PostAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
        activity = a;
        data=d;
    }
    public int getCount() {
        return data.size();
    }
    public Object getItem(int position) {
        return position;
    }
    public long getItemId(int position) {
        return position;
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        ListPostViewHolder holder = null;
        if (convertView == null) {
            holder = new ListPostViewHolder();
            convertView = LayoutInflater.from(activity).inflate(
                    R.layout.post, parent, false);


            holder.galleryImage = (ImageView) convertView.findViewById(R.id.galleryImage);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.details = (TextView) convertView.findViewById(R.id.details);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            holder.likes = (TextView) convertView.findViewById(R.id.likes);
            holder.dislikes = (TextView) convertView.findViewById(R.id.dislikes);
            holder.comments = (TextView) convertView.findViewById(R.id.comments);
            convertView.setTag(holder);
        } else {
            holder = (ListPostViewHolder) convertView.getTag();
        }



        holder.galleryImage.setId(position);
        holder.title.setId(position);
        holder.details.setId(position);
        holder.time.setId(position);
        holder.likes.setId(position);
        holder.dislikes.setId(position);
        holder.comments.setId(position);

        HashMap<String, String> song = new HashMap<String, String>();
        song = data.get(position);

        try{
            holder.title.setText(song.get(NewsFeed.KEY_TITLE));
            holder.time.setText(song.get(NewsFeed.KEY_TIME));
            holder.details.setText(song.get(NewsFeed.KEY_POST));
            holder.likes.setText(song.get(NewsFeed.KEY_LIKES) + " Likes");
            holder.dislikes.setText(song.get("dislikes") + " Dislikes");
            holder.comments.setText(song.get(NewsFeed.KEY_COMMENTS) + " Comments");

//            if(song.get(MainActivity.KEY_URLTOIMAGE).toString().length() < 5)
//            {
//                holder.galleryImage.setVisibility(View.GONE);
//            }else{
//                Picasso.get()
//                        .load(song.get(MainActivity.KEY_URLTOIMAGE))
//                        .resize(300, 200)
//                        .centerCrop()
//                        .into(holder.galleryImage);
//            }
        }catch(Exception e) {}
        return convertView;
    }
}

class ListPostViewHolder{
    ImageView galleryImage;
    TextView title, likes, dislikes, comments, time, details;
}