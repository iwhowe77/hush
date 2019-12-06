package com.example.hush;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.sql.Time;

public class WriteComment extends AppCompatActivity {

    EditText textText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_comment);

        Button backButton = (Button) findViewById(R.id.back_white_);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button publish_btn = findViewById(R.id.publish);
        publish_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                textText = findViewById(R.id.body);
                Intent intent = getIntent();
                String id = intent.getStringExtra("id");


                try {


                    JSONObject obj = loadJSONObj(getApplicationContext());
                    JSONArray posts_array = obj.getJSONArray("posts");

                    JSONObject relevantPost = null;
                    for (int i = 0; i < posts_array.length(); i++) {
                        JSONObject newPost = posts_array.getJSONObject(i);
                        //Log.d("Details-->", relevantPost.getString("post"));

                        if (id.equals(newPost.getString("id"))) {
                            relevantPost = newPost;
                            break;
                        }
                    }

                    if(relevantPost == null){
                        return;
                    }

                    JSONObject new_entry = new JSONObject();
                    //{"id": "1", "title": "Title 1", "post": "this is text for the first post title 1 blah blah",
                    //    "time": "2014-03-12T13:37:27+00:00", "likes": "4", "comments":"2"}

                    new_entry.put("text", textText.getText());
                    new_entry.put("time", "2014-03-12T13:37:27+00:00");
                    new_entry.put("likes", "0");
                    new_entry.put("dislikes", "0");
                    new_entry.put("user", "Anon");

                    JSONArray comments_list = relevantPost.getJSONArray("comments_list");
                    comments_list.put(new_entry);
                    writeJSONToFile(obj);

                    //startActivity(new Intent(WritePost.this, NewsFeed.class));
                    finish();
                } catch (
                        JSONException e) {
                    e.printStackTrace();
                }

            }

        });
    }

    public boolean writeJSONToFile(JSONObject obj){
        String filename = "posts.json";
        String fileContents = "post content..";
        FileOutputStream outputStream;

        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(obj.toString().getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public JSONObject loadJSONObj(Context context) {
        File file = new File(context.getFilesDir(), "posts.json");

        String json = null;
        if(file.exists()) {
            // use the config.
            InputStream inputStream = null;
            try {
                inputStream = openFileInput("posts.json");

                if ( inputStream != null ) {
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String receiveString = "";
                    StringBuilder stringBuilder = new StringBuilder();

                    while ( (receiveString = bufferedReader.readLine()) != null ) {
                        stringBuilder.append(receiveString);
                    }

                    json = stringBuilder.toString();
                }
            }
            catch (FileNotFoundException e) {
                Log.e("login activity", "File not found: " + e.toString());
            } catch (IOException e) {
                Log.e("login activity", "Can not read file: " + e.toString());
            }
            finally {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } else {
            // use the config from asset.
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
        }

        try {
            JSONObject retObj = new JSONObject(json);
            return retObj;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;





    }
}
