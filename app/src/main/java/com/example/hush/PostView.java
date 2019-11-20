package com.example.hush;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class PostView extends AppCompatActivity {

    String id = "";
    String title = "";
    String post = "";
    String time = "";
    String likes = "";
    String comments = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_view);


        Intent intent = getIntent();
        id = intent.getStringExtra(NewsFeed.KEY_ID);
        title = intent.getStringExtra(NewsFeed.KEY_TITLE);
        post = intent.getStringExtra(NewsFeed.KEY_POST);
        time = intent.getStringExtra(NewsFeed.KEY_TIME);
        likes = intent.getStringExtra(NewsFeed.KEY_LIKES);
        comments = intent.getStringExtra(NewsFeed.KEY_COMMENTS);

        TextView titleTV = findViewById(R.id.post_title);
        TextView postTV  = findViewById(R.id.post_text);
        TextView timeTV = findViewById(R.id.time_stamp);

        titleTV.setText(title);
        postTV.setText((post));
        timeTV.setText(time);


    }
}
