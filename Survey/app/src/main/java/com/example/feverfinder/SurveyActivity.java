package com.example.feverfinder;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.feverfinder.questions.Question;
import com.example.feverfinder.questions.Section;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SurveyActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static final String SECTIONS = "com.example.feverfinder.SECTIONS";
    public static final String CURRENT_FRAGMENT = "CURRENT_FRAGMENT";
    public static final String SECTION_ORDER = "SECTION_ORDER";


    private ArrayList<String> sectionOrder;
    private String currentFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_survey);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        Menu m = navigationView.getMenu();

        if (savedInstanceState == null) {
            List<Section> sections = getIntent().getExtras().getParcelableArrayList(SECTIONS);

            //Generate sections
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            boolean first = true;
            int id = 1;
            sectionOrder = new ArrayList<>();

            for (Section s : sections) {
                sectionOrder.add(String.valueOf(id));
                SurveySection surveySectionFragment = s.getSurveySectionFragment();
                MenuItem menuItem = m.add(Menu.NONE, id, Menu.NONE, s.getName()).setCheckable(true);
                fragmentTransaction.add(R.id.fragment_container, surveySectionFragment, String.valueOf(id));

                //If this is the first section
                if (first) {
                    first = false;
                    setTitle(s.getName());
                    currentFragment = String.valueOf(id);
                    menuItem.setChecked(true);
                } else {
                    fragmentTransaction.hide(surveySectionFragment);
                }
                id++;
            }
            fragmentTransaction.commit();


        } else {
            sectionOrder = savedInstanceState.getStringArrayList(SECTION_ORDER);
            currentFragment = savedInstanceState.getString(CURRENT_FRAGMENT);

            //Sort out the menu
            for (String id : sectionOrder) {
                SurveySection surveySection = (SurveySection) getSupportFragmentManager()
                        .findFragmentByTag(id);
                MenuItem menuItem = m.add(Menu.NONE, Integer.valueOf(id), Menu.NONE,
                        surveySection.getSection().getName()).setCheckable(true);
                if (id.equals(currentFragment)) menuItem.setChecked(true);
            }
        }

        //Set the submit button to be a different colour
        SpannableString spannableString = new SpannableString("Submit Survey");
        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary)),
                0, spannableString.length(), 0);
        m.add(Menu.NONE, 0, Menu.NONE, spannableString).setCheckable(true);

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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == 0) submitSurvey();
        else showFragment(String.valueOf(id));

        return true;
    }

    public void nextSectionClick(View view) {
        String id = sectionOrder.get((sectionOrder.indexOf(currentFragment) + 1) % sectionOrder.size());
        showFragment(id);
    }

    public void submitSurveyClick(View view) {
        submitSurvey();
    }


    private void showFragment(String id) {
        SurveySection surveySection = (SurveySection) getSupportFragmentManager().findFragmentByTag(id);


        setTitle(surveySection.getSection().getName());

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();

        fragmentTransaction.hide(getSupportFragmentManager().findFragmentByTag(currentFragment));
        fragmentTransaction.show(surveySection);
        currentFragment = id;

        fragmentTransaction.commit();
    }

    private void submitSurvey() {
        try {
            // TODO: test for internet connection;
            // TODO: if there isn't one save to file, or throw exception?
            if (!isNetworkAvailable()) throw new SaveException("Network is not available.");

            // Create JSON object to send
            JSONObject obj = new JSONObject();

            // Iterate through all questions and get their content


            for (String id : sectionOrder) {
                SurveySection surveySection = (SurveySection) getSupportFragmentManager().findFragmentByTag(id);
                Section s = surveySection.getSection();
                for (Question q : s.getQuestions()) {
                    obj.put(q.getName().toLowerCase(), q.getJSONOutput());

                }
            }
            Log.d("JSON", obj.toString());

            // Convert JSON to string
            String strToSend = obj.toString();

            // Send it with creating a new thread
            SendSurveyThread sst = new SendSurveyThread(strToSend, getApplicationContext());
            sst.start();

            //TODO: sort out this message!
            //TODO: might be better if this waits for thread to finish!
            Toast.makeText(getApplicationContext(), "Survey sent", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            //If sending fails, display error message
            Toast.makeText(getApplicationContext(),
                    e.getClass().getCanonicalName() + ": " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Puts data in to bundle to be recreated on recreation of the survey
     *
     * @param out The bundle to store data in
     */
    @Override
    public void onSaveInstanceState(Bundle out) {
        super.onSaveInstanceState(out);
        out.putStringArrayList(SECTION_ORDER, sectionOrder);
        out.putString(CURRENT_FRAGMENT, currentFragment);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onRestoreInstanceState(savedInstanceState);
    }

    /* Test if network is available */
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
