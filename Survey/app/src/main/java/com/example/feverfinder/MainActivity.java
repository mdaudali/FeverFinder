package com.example.feverfinder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.feverfinder.questions.QuestionParser;
import com.example.feverfinder.questions.Section;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private List<Section> mSections;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSections = QuestionParser.getSections(getResources().openRawResource(R.raw.choices),
                getResources().openRawResource(R.raw.questions));
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        findViewById(R.id.startBtn).setClickable(true);
    }

    public void onStartSurvey(View view) {

        //TODO: Ensure you cannot spawn multiple surveys
        findViewById(R.id.startBtn).setClickable(false);
        Intent intent = new Intent(this, SurveyActivity.class);
        intent.putParcelableArrayListExtra(SurveyActivity.EXTRA_SECTIONS, new ArrayList<>(mSections));
        startActivity(intent);
    }
}
