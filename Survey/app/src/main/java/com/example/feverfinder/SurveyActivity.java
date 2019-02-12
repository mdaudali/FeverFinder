package com.example.feverfinder;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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

import com.example.feverfinder.questions.QuestionParser;
import com.example.feverfinder.questions.Section;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SurveyActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SurveySection.OnFragmentInteractionListener {

    private SparseArray<SurveySection> surveySections;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        List<Section> sections = QuestionParser.getSections(getResources().openRawResource(R.raw.choices),
                getResources().openRawResource(R.raw.questions));

        setContentView(R.layout.activity_survey);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        surveySections = new SparseArray<>();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu m = navigationView.getMenu();
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

            //TODO: actually build the correct SurveySection
            surveySections.append(id, SurveySection.newInstance(s.getName(), s.getQuestions()));
        }

        //TODO: setup the default first page
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        //TODO: sort out back button presses - maybe add to the back stack???

        fragmentTransaction.add(R.id.fragment_container, surveySections.get(firstId));
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

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        //TODO: sort out back button presses - maybe add to the back stack???

        fragmentTransaction.replace(R.id.fragment_container, surveySections.get(id));
        fragmentTransaction.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //TODO: deal with fragment interaction
    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
