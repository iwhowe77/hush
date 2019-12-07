package com.example.hush;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
            final ImageButton like_button = (ImageButton) convertView.findViewById(R.id.like_button);


            like_button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (like_button.getTag().toString().equals("outline")){
                        like_button.setImageResource(R.drawable.ic_thumb_up_24px_filled);
                        like_button.setTag("filled");
                    } else {
                        like_button.setImageResource(R.drawable.ic_thumb_up_24px);
                        like_button.setTag("outline");
                    }

                    Log.d("hi", "YAHOOO");
                }
            });

            final ImageButton dislike_button = (ImageButton) convertView.findViewById(R.id.dislike_button);
            dislike_button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (dislike_button.getTag().toString().equals("outline")){
                        dislike_button.setImageResource(R.drawable.ic_thumb_down_24px_filled);
                        dislike_button.setTag("filled");

                        Log.d("hi", "YAHOOO");
                    } else{
                        dislike_button.setImageResource(R.drawable.ic_thumb_down_24px);
                        dislike_button.setTag("outline");
                    }

                }
            });

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