package com.example.feverfinder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class PersonalInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);
    }

    public void onSubmit(View view) {
        //TODO: actually store the data

        Intent intent = new Intent(this, SurveyStatus.class);
        startActivity(intent);
    }
}
