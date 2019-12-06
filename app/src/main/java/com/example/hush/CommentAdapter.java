package com.example.hush;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class CommentAdapter extends BaseAdapter {
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;

    public CommentAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
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
        ListCommentViewHolder holder = null;
        if (convertView == null) {
            holder = new ListCommentViewHolder();
            convertView = LayoutInflater.from(activity).inflate(
                    R.layout.comment, parent, false);
            //holder.galleryImage = (ImageView) convertView.findViewById(R.id.galleryImage);
            holder.text = (TextView) convertView.findViewById(R.id.text);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            holder.likes = (TextView) convertView.findViewById(R.id.likes);
            holder.dislikes = (TextView) convertView.findViewById(R.id.dislikes);
            convertView.setTag(holder);
        } else {
            holder = (ListCommentViewHolder) convertView.getTag();
        }
        //holder.galleryImage.setId(position);
        holder.text.setId(position);
        holder.time.setId(position);
        holder.likes.setId(position);
        holder.dislikes.setId(position);

        HashMap<String, String> song = new HashMap<String, String>();
        song = data.get(position);

        try{
            holder.text.setText(song.get("text"));
            holder.time.setText(song.get("time"));
            holder.likes.setText(song.get("likes") + " Likes");
            holder.dislikes.setText(song.get("dislikes") + " Dislikes");

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

class ListCommentViewHolder {
    //ImageView galleryImage;
    TextView text, likes, dislikes, time;
}