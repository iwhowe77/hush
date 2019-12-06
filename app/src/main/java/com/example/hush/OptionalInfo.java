package com.example.hush;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

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

        ((TextView)findViewById(R.id.classes_list_signup)).setMovementMethod(new ScrollingMovementMethod());

        Spinner spinner = findViewById(R.id.major);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.custom_spinner,
                                                getResources().getStringArray(R.array.majors_array)
        );
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown);
        spinner.setAdapter(adapter);


        Spinner spinYear = findViewById(R.id.grad_year);

        ArrayList<String> years = new ArrayList<String>();
        int grad_year = Calendar.getInstance().get(Calendar.YEAR) + 5;
        for (int i = grad_year; i >= 1867; i--) {
            years.add(Integer.toString(i));
        }
        ArrayAdapter<String> year_adapter = new ArrayAdapter<String>(this, R.layout.custom_spinner, years);

        year_adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown);
        spinYear.setAdapter(year_adapter);


        Spinner GPA = findViewById(R.id.gpa);
        ArrayList<String> GPAs = new ArrayList<String>();
        for (double i = 4.0; i >= 0.0; i -= 0.1) {
            GPAs.add(Double.toString(Math.round(i * 10) / 10.0));
        }
        ArrayAdapter<String> GPA_adapter = new ArrayAdapter<String>(this, R.layout.custom_spinner, GPAs);
        GPA_adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown);
        GPA.setAdapter(GPA_adapter);
    }
    
}
