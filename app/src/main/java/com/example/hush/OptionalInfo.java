package com.example.hush;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class OptionalInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_optional_info);

        final Button button = findViewById(R.id.next);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OptionalInfo.this, NewsFeed.class);
                startActivity(intent);
            }
        });

        final Button back_button = findViewById(R.id.back_white_);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OptionalInfo.this, PasswordCreation.class);
                startActivity(intent);
            }
        });
    }
    
}
