package com.example.hush;

import android.os.Bundle;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;
import android.util.Log;
import android.net.Uri;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

public class EmailAddress extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_address);

        final Button button = findViewById(R.id.next);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText simpleEditText = (EditText) findViewById(R.id.rectangle);
                String address = simpleEditText.getText().toString();
                if (!checkEmail(address)){
                    Context context = getApplicationContext();
                    CharSequence text = "Please enter an illinois.edu email.";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();

                } else {
                    Intent intent = new Intent(EmailAddress.this, VerificationCode.class);
                    startActivity(intent);
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

    protected boolean checkEmail(String address){
        boolean is_valid = false;
        if (address.length()-13 <= 0){
            return is_valid;
        }
        String[] parts = address.split("@");
        if (parts.length < 2){
            return is_valid;
        }
        String suffix = parts[1];
        if (suffix.equals("illinois.edu")){
            is_valid = true;
        }
        return is_valid;
    }
/*
    protected void sendEmail(String address) {
        Log.i("Send email", "");

        String[] TO = {address};
        String[] CC = {""};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");


        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "test");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "code");

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
            Log.i("Finished sending email.", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(EmailAddress.this,
                    "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }
    */

}
