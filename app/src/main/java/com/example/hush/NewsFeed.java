package com.example.hush;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

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

public class NewsFeed extends AppCompatActivity {

    ListView listPosts;
    static final String KEY_ID = "id";
    static final String KEY_TITLE = "title";
    static final String KEY_POST = "post";
    static final String KEY_TIME = "time";
    static final String KEY_LIKES = "likes";
    static final String KEY_COMMENTS = "comments";

    @Override
    public void onResume(){
        super.onResume();
        reloadPostList();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed);

        FloatingActionButton write_btn = findViewById(R.id.write_button);
        write_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NewsFeed.this, WritePost.class));
            }
        });

        final EditText searchbox = findViewById(R.id.search_box);
        searchbox.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                Log.d("hi", "hi");
                if ((keyCode == EditorInfo.IME_ACTION_SEARCH) || ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER))) {
                    // Perform action on key press
                    //Toast.makeText(NewsFeed.this, searchbox.getText(), Toast.LENGTH_SHORT).show();
                    filterPostList(searchbox.getText().toString());
                    return true;
                }
                return false;
            }
        });


        reloadPostList();


    }

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

    protected void filterPostList(String query){
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

                //Check for search query
                String[] words = query.split(" ");
                boolean searchFound = false;
                for (String ss : words) {
                    if(title.contains(ss)) {
                        searchFound = true;
                    }
                    else if(post.contains(ss)) {
                        searchFound = true;
                    }
                }
                if(!searchFound) {
                    continue;
                }

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


    protected void reloadPostList(){
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
}

