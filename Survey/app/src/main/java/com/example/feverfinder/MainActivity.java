package com.example.feverfinder;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.service.media.MediaBrowserService;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.feverfinder.questions.GPSLoc;
import com.example.feverfinder.questions.QuestionParser;
import com.example.feverfinder.questions.Section;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private List<Section> mSections;

    // TODO Move this GPS CODE ------------------------
    LocationListener mLocListener;
    LocationManager mLocManager;
    Button gpsButton;
    // TODO Move this GPS CODE -----------------------

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSections = QuestionParser.getSections(getResources().openRawResource(R.raw.choices),
                getResources().openRawResource(R.raw.questions));
        setContentView(R.layout.activity_main);

        // TODO Move this GPS CODE ------------------------
        mLocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mLocListener = new GPSLoc();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.INTERNET
            }, 10);
            return;
        }
        gpsButton = (Button) findViewById(R.id.GPSButton);
        gpsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLocListener = new GPSLoc();
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                mLocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocListener);
                // Remove the listener you previously added
                mLocManager.removeUpdates(mLocListener);
            }
        });
        // TODO Move this GPS CODE ------------------------
    }

    // TODO Move this GPS CODE ------------------------
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case 10:
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    return;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        findViewById(R.id.startBtn).setClickable(true);
    }

    public void onSubmitResponses(View view) {
        //TODO: replace this with actual code!!
        try {
            SurveyStore.saveSurvey("HELLO", getApplicationContext());
            SurveyStore.submitSavedSurveys(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onStartSurvey(View view) {
        findViewById(R.id.startBtn).setClickable(false);
        Intent intent = new Intent(this, SurveyActivity.class);
        intent.putParcelableArrayListExtra(SurveyActivity.SECTIONS, new ArrayList<>(mSections));
        startActivity(intent);
    }
}
