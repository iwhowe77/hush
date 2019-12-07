package com.example.hush;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

public class NewsFeed extends AppCompatActivity {

    ListView listPosts;
    static final String KEY_ID = "id";
    static final String KEY_TITLE = "title";
    static final String KEY_POST = "post";
    static final String KEY_TIME = "time";
    static final String KEY_LIKES = "likes";
    static final String KEY_DISLIKES = "dislikes";
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

        final Spinner spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String choice =  parentView.getItemAtPosition(position).toString();
                EditText searchbox2 = (EditText) findViewById(R.id.search_box);
                Log.d("choice", choice);
                Editable txt = searchbox2.getText();
                performSearch(txt.toString(), choice);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
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
                    String searchbox_txt = searchbox.getText().toString();
                    Toast.makeText(NewsFeed.this, searchbox_txt, Toast.LENGTH_SHORT).show();
                    performSearch(searchbox_txt, spinner.getSelectedItem().toString());

                    return true;
                }
                return false;
            }
        });


        performSearch("", spinner.getSelectedItem().toString());


    }


    private ArrayList<View> getAllChildren(View v) {

        if (!(v instanceof ViewGroup)) {
            ArrayList<View> viewArrayList = new ArrayList<View>();
            viewArrayList.add(v);
            return viewArrayList;
        }

        ArrayList<View> result = new ArrayList<View>();

        ViewGroup vg = (ViewGroup) v;
        for (int i = 0; i < vg.getChildCount(); i++) {

            View child = vg.getChildAt(i);

            ArrayList<View> viewArrayList = new ArrayList<View>();
            viewArrayList.add(v);
            viewArrayList.addAll(getAllChildren(child));

            result.addAll(viewArrayList);
        }
        return result;
    }

    static <K,V extends Comparable<? super V>>
    SortedSet<Map.Entry<K,V>> entriesSortedByValues(Map<K,V> map) {
        SortedSet<Map.Entry<K,V>> sortedEntries = new TreeSet<Map.Entry<K,V>>(
                new Comparator<Map.Entry<K,V>>() {
                    @Override public int compare(Map.Entry<K,V> e1, Map.Entry<K,V> e2) {
                        int res = e1.getValue().compareTo(e2.getValue());
                        return res != 0 ? res : 1;
                    }
                }
        );
        sortedEntries.addAll(map.entrySet());
        return sortedEntries;
    }


    public void performSearch(String query, String sorting){
        listPosts = findViewById(R.id.list_view);
        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset(getApplicationContext()));
            JSONArray posts_array = obj.getJSONArray("posts");
            final ArrayList<HashMap<String, String>> formList = new ArrayList<HashMap<String, String>>();
            HashMap<String, String> m_li;

            Map<Integer, String> sort_id = new TreeMap<>();
            String[] sort_arr = new String[posts_array.length()];
            Integer[] pos_arr = new Integer[posts_array.length()];
            List<Integer> pos_lst = new ArrayList<Integer>();
            for (int j = 0; j < posts_array.length(); j++) {
                JSONObject jo_inside = posts_array.getJSONObject(j);
                String sort_val = null;
                String id = jo_inside.getString("id");
                if(sorting.equals("Newest")){
                    sort_val = jo_inside.getString("time");
                } else if (sorting.equals("Oldest")){
                    sort_val = jo_inside.getString("time");
                } else if (sorting.equals("Hottest")){
                    sort_val = jo_inside.getString("likes");
                } else if (sorting.equals("Controversial")){
                    sort_val = jo_inside.getString("comments");
                }

                sort_id.put(j, sort_val);
//                pos_arr[j] = j;
//                sort_arr[j] = sort_val;
            }
            SortedSet<Map.Entry<Integer,String>> sortedset= entriesSortedByValues(sort_id);
            Integer ct = 0;
            for (Map.Entry<Integer , String> entry : sortedset)
            {
                sort_arr[ct] = entry.getValue();
                pos_arr[ct] = entry.getKey();
                ct += 1;
                System.out.println(entry);
            }



//            Log.d("BEFORE SORT", Arrays.toString(sort_arr));
//            Arrays.sort(sort_arr);
//            Log.d("AFTER SORT", Arrays.toString(sort_arr));
            if (sorting.equals("Newest") || sorting.equals("Hottest") || sorting.equals("Controversial")) {
                Collections.reverse(Arrays.asList(sort_arr));
                Collections.reverse(Arrays.asList(pos_arr));
            }

            for (int i = 0; i < sort_arr.length; i++) {
                String first_elem = sort_arr[i];
                int JSONpos = pos_arr[i];
                JSONObject jo_inside = posts_array.getJSONObject(JSONpos);
                Log.d("Details-->", jo_inside.toString());
                String id = jo_inside.getString("id");
                String title = jo_inside.getString("title");
                String post = jo_inside.getString("post");
                String time = jo_inside.getString("time");
                String likes = jo_inside.getString("likes");
                String dislikes = jo_inside.getString("dislikes");
                String comments = jo_inside.getString("comments");

                JSONArray comments_list_json = jo_inside.getJSONArray("comments_list");
                String[] comments_text = new String[comments_list_json.length()];
                String[] comments_time  = new String[comments_list_json.length()];
                String[] comments_likes  = new String[comments_list_json.length()];
                String[] comments_dislikes = new String[comments_list_json.length()];
                String[] comments_users = new String[comments_list_json.length()];

                Boolean include_post = false;
                if (query.trim().equals("")){
                    include_post=true;
                }
                for (int j = 0; j < comments_list_json.length(); j++){
                    JSONObject comment = comments_list_json.getJSONObject(j);
                    comments_text[j] = comment.getString("text");
                    comments_time[j] = comment.getString("time");
                    comments_likes[j] = comment.getString("likes");
                    comments_dislikes[j] = comment.getString("dislikes");
                    comments_users[j] = comment.getString("user");

                    if(comments_text[j].contains(query)){
                        include_post = true;
                        Log.d("POST IS TRUE", post);
                    }
                }

                if (title.contains(query) || post.contains(query)){
                    include_post = true;
                    Log.d("POST IS TRUE", post);
                }

                if(include_post == true){

                    //Add your values in your `ArrayList` as below:
                    m_li = new HashMap<String, String>();
                    m_li.put("id", id);
                    m_li.put("title", title);
                    m_li.put("post", post);
                    m_li.put("time", time);
                    m_li.put("likes", likes);
                    m_li.put("dislikes", dislikes);

                    m_li.put("comments", comments);
                    for (int k = 0; k < comments_list_json.length(); k++){
                        m_li.put("comments_text"+Integer.toString(k), comments_text[k]);
                        m_li.put("comments_time"+Integer.toString(k), comments_time[k]);
                        m_li.put("comments_likes"+Integer.toString(k), comments_likes[k]);
                        m_li.put("comments_dislikes"+Integer.toString(k), comments_dislikes[k]);
                        m_li.put("comments_users"+Integer.toString(k), comments_users[k]);
                    }




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
                            i.putExtra("dislikes", formList.get(+position).get(KEY_DISLIKES));
                            i.putExtra("comments", formList.get(+position).get(KEY_COMMENTS));

                            startActivity(i);
                        }
                    });
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
                String dislikes = jo_inside.getString("dislikes");
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
                m_li.put("dislikes", dislikes);
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
                String dislikes = jo_inside.getString("dislikes");
                String comments = jo_inside.getString("comments");

                //Add your values in your `ArrayList` as below:
                m_li = new HashMap<String, String>();
                m_li.put("id", id);
                m_li.put("title", title);
                m_li.put("post", post);
                m_li.put("time", time);
                m_li.put("likes", likes);
                m_li.put("dislikes", dislikes);
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
                        i.putExtra("dislikes", formList.get(+position).get("dislikes"));
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

