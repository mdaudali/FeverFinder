package com.example.feverfinder;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ScrollView;

import com.example.feverfinder.questions.Question;
import com.example.feverfinder.questions.QuestionParser;
import com.example.feverfinder.questions.Response;
import com.example.feverfinder.questions.QuestionAdapter;

import java.util.LinkedHashMap;
import java.util.Map;

public class PersonalInfo extends AppCompatActivity {
    QuestionAdapter questionAdapter;
    Context thisContext;
    ListView myListView;
    Map<Question, Response> questions = new LinkedHashMap<>();


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);

//         set member variables
        myListView = (ListView) findViewById(R.id.personalInfoListView);
        thisContext = this;
        QuestionParser questionParser = new QuestionParser();
        questions = questionParser.getQuestions();
        questionAdapter = new QuestionAdapter(thisContext, questions);
        myListView.setAdapter(questionAdapter);
    }

    public void onSubmit(View view) {
        //TODO: actually store the data

        Intent intent = new Intent(this, SurveyStatus.class);
        startActivity(intent);
    }
} // end of main class
