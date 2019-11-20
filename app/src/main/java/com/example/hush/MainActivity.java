package com.example.hush;

import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import 	android.content.Intent;


import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button)findViewById(R.id.sign_up);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DataStatement.class);
                startActivity(intent);
            }
        });

    }


}
