package com.example.hush;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
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

public class PostView extends AppCompatActivity {

    String id = "";
    String title = "";
    String post = "";
    String time = "";
    String likes = "";
    String comments = "";

    ListView listPosts;

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

        Button backButton = (Button) findViewById(R.id.back_white_);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        reloadCommentList();
    }

    @Override
    public void onResume(){
        super.onResume();
        reloadCommentList();
    }

    protected void reloadCommentList(){
        listPosts = findViewById(R.id.list_view);
        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset(getApplicationContext()));
            JSONArray posts_array = obj.getJSONArray("posts");
            final ArrayList<HashMap<String, String>> formList = new ArrayList<HashMap<String, String>>();
            HashMap<String, String> m_li;

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

            JSONArray comments_array = relevantPost.getJSONArray("comments_list");

            for (int i = 0; i < comments_array.length(); i++) {
                JSONObject jo_inside = comments_array.getJSONObject(i);

                String text = jo_inside.getString("text");
                String time = jo_inside.getString("time");
                String likes = jo_inside.getString("likes");
                String dislikes = jo_inside.getString("dislikes");

                //Add your values in your `ArrayList` as below:
                m_li = new HashMap<String, String>();
                m_li.put("text", text);
                m_li.put("time", time);
                m_li.put("likes", likes);
                m_li.put("dislikes", dislikes);

                formList.add(m_li);

                CommentAdapter adapter = new CommentAdapter(PostView.this, formList);
                listPosts.setAdapter(adapter);

                listPosts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        Log.d("My View is --->", view.toString());
                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    //Copypasted code is bad habit but static errors are weird
    public String loadJSONFromAsset(Context context) {

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
        return json;
    }
}
