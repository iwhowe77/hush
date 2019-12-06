package com.example.hush;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class WritePost extends AppCompatActivity {

    EditText titleText;
    EditText bodyText;
    EditText tagText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_post);

        Button publish_btn = findViewById(R.id.publish);
        publish_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                titleText = findViewById(R.id.add_a_title);
                bodyText = findViewById(R.id.body);
                tagText = findViewById(R.id.tags);


                try {
                    JSONObject obj = new JSONObject(loadJSONFromAsset(getApplicationContext()));
                    JSONArray posts_array = obj.getJSONArray("posts");

                    JSONObject new_entry = new JSONObject();
                    //{"id": "1", "title": "Title 1", "post": "this is text for the first post title 1 blah blah",
                    //    "time": "2014-03-12T13:37:27+00:00", "likes": "4", "comments":"2"}
                    new_entry.put("id",  Integer.toString(posts_array.length() + 2));
                    new_entry.put("title", titleText.getText());
                    new_entry.put("post", bodyText.getText());
                    new_entry.put("time", "2014-03-12T13:37:27+00:00");
                    new_entry.put("likes", "0");
                    new_entry.put("comments", "0");

                    posts_array.put(new_entry);

                    String filename = "posts.json";
                    String fileContents = "post content..";
                    FileOutputStream outputStream;

                    try {
                        outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                        outputStream.write(obj.toString().getBytes());
                        outputStream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //startActivity(new Intent(WritePost.this, NewsFeed.class));
                    finish();
                } catch (
                        JSONException e) {
                    e.printStackTrace();
                }

            }

        });

        Button backButton = (Button) findViewById(R.id.back_white_);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    public String loadJSONFromAsset(Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open("posts.json");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }
}
