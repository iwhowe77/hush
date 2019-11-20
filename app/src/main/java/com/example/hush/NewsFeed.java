package com.example.hush;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class NewsFeed extends AppCompatActivity {

    ListView listPosts;
    static final String KEY_ID = "id";
    static final String KEY_TITLE = "title";
    static final String KEY_POST = "post";
    static final String KEY_TIME = "time";
    static final String KEY_LIKES = "likes";
    static final String KEY_COMMENTS = "comments";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsfeed);

        listPosts = findViewById(R.id.list_view);
        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset(getApplicationContext()));
            JSONArray posts_array = obj.getJSONArray("posts");
            final ArrayList<HashMap<String, String>> formList = new ArrayList<HashMap<String, String>>();
            HashMap<String, String> m_li;

            for (int i = 0; i < posts_array.length(); i++) {
                JSONObject jo_inside = posts_array.getJSONObject(i);
                Log.d("Details-->", jo_inside.getString("post"));
                String id = jo_inside.getString("id");
                String title = jo_inside.getString("title");
                String post = jo_inside.getString("post");
                String time = jo_inside.getString("time");
                String likes = jo_inside.getString("likes");
                String comments = jo_inside.getString("comments");

                //Add your values in your `ArrayList` as below:
                m_li = new HashMap<String, String>();
                m_li.put("id", id);
                m_li.put("title", title);
                m_li.put("post", post);
                m_li.put("time", time);
                m_li.put("likes", likes);
                m_li.put("comments", comments);


                formList.add(m_li);

                PostAdapter adapter = new PostAdapter(NewsFeed.this, formList);
                listPosts.setAdapter(adapter);

                listPosts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        Intent i = new Intent(NewsFeed.this, PostView.class);
                        i.putExtra("id", formList.get(+position).get(KEY_ID));
                        i.putExtra("title", formList.get(+position).get(KEY_TITLE));
                        i.putExtra("post", formList.get(+position).get(KEY_POST));
                        i.putExtra("time", formList.get(+position).get(KEY_TIME));
                        i.putExtra("likes", formList.get(+position).get(KEY_LIKES));
                        i.putExtra("comments", formList.get(+position).get(KEY_COMMENTS));
                        startActivity(i);
                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
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

