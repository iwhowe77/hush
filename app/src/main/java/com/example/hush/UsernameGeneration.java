package com.example.hush;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;

import android.widget.TextView;

public class UsernameGeneration extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_username_generation);

        EditText editText = (EditText)findViewById(R.id.username);
        String name = getUserName();
        editText.setText(name);

        final Button button = findViewById(R.id.next);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UsernameGeneration.this, PasswordCreation.class);
                startActivity(intent);
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


    protected String getUserName() {
        String[] adj_arr = new String[]{"pretty", "green", "floppy", "large", "hungry", "active", "bright", "pampered", "plush", "soggy"};
        String[] noun_arr = new String[]{"Burrito", "Cactus", "Jackolantern", "Apricot", "Robot", "Shoe", "Octopus", "Otter", "Snail", "Flower"};

        int adj = (int) (Math.random() * 10);
        int noun = (int) (Math.random() * 10);

        return (adj_arr[adj] + noun_arr[noun]);

    }
}