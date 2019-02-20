package com.example.feverfinder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onStartSurvey(View view) {
        //Ensure you cannot spawn multiple surveys
        findViewById(R.id.startBtn).setClickable(false);
        Intent intent = new Intent(this, SurveyActivity.class);
        startActivity(intent);
        findViewById(R.id.startBtn).setClickable(true);
    }
}
