package com.example.feverfinder;

import android.content.Context;
import android.icu.util.Output;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.feverfinder.questions.Question;
import com.example.feverfinder.questions.QuestionParser;
import com.example.feverfinder.questions.Section;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class SurveyActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SurveySection.OnFragmentInteractionListener {

    private SparseArray<Section> sectionMap;
    private int currentFragment;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        List<Section> sections = QuestionParser.getSections(getResources().openRawResource(R.raw.choices),
                getResources().openRawResource(R.raw.questions));

        setContentView(R.layout.activity_survey);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("Fever Finder");

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //sectionMap is a map from ids to their associated Section
        sectionMap = new SparseArray<>();

        NavigationView navigationView = findViewById(R.id.nav_view);
        Menu m = navigationView.getMenu();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        int firstId = -1;
        for (Section s: sections) {
            int id = View.generateViewId();
            MenuItem menuItem = m.add(Menu.NONE, id, Menu.NONE, s.getName());
            menuItem.setCheckable(true);

            //If this is the default section
            if (firstId < 0) {
                firstId = id;
                menuItem.setChecked(true);
            }
            fragmentTransaction.add(R.id.fragment_container, s.getSurveySectionFragment());
            fragmentTransaction.hide(s.getSurveySectionFragment());

            //TODO: actually build the correct SurveySection
            sectionMap.append(id, s);
        }

        //TODO: sort out back button presses - maybe add to the back stack???
        fragmentTransaction.show(sectionMap.get(firstId).getSurveySectionFragment());
        currentFragment = firstId;
        fragmentTransaction.commit();

        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.survey, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_settings:
                return true;
            case R.id.action_save:
                Context context = getApplicationContext();

                Toast.makeText(context, "Saving survey...", Toast.LENGTH_SHORT).show();

                // Try to save answers
                // TODO: if we cannot upload them, then we should save
                try {
                    saveAnswers();
                    // Toast.makeText(context, "Survey successfully saved", Toast.LENGTH_SHORT).show();
                    return true;
                } catch (Exception e) {
                    Toast.makeText(context, e.getClass().getCanonicalName() + ": " + e.getMessage(), Toast.LENGTH_LONG).show();
                    return false;
                }
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();

        fragmentTransaction.hide(sectionMap.get(currentFragment).getSurveySectionFragment());
        fragmentTransaction.show(sectionMap.get(id).getSurveySectionFragment());
        currentFragment = id;

        fragmentTransaction.commit();

        return true;
    }

    //TODO: deal with fragment interaction
    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private void saveAnswers() throws SaveException, JSONException, IOException {
        // TODO: test for internet connection;
        // TODO: if there isn't one save to file, or throw exception?
        if(!isNetworkAvailable()) throw new SaveException("Network is not available.");

        // Create JSON object to send
        JSONObject obj = new JSONObject();

        // Iterate through all questions and get their content
        for(int i = 0; i < sectionMap.size(); ++i) {
            Section s = sectionMap.valueAt(i);
            for(Question q : s.getQuestions()) {
                obj.put(q.getName().toLowerCase(), q.getJSONOutput());
            }
        }

        // Convert JSON to string
        String strToSend = obj.toString();

        Log.d("JSON", strToSend);

        // Send it with creating a new thread
        SendSurveyThread sst = new SendSurveyThread(strToSend);
        sst.start();
    }

    /* Test if network is available */
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
