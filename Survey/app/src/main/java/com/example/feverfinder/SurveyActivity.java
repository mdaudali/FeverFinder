package com.example.feverfinder;

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
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.feverfinder.questions.Section;

import java.util.LinkedList;
import java.util.List;

public class SurveyActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static final String EXTRA_SECTIONS = "com.example.feverfinder.EXTRA_SECTIONS";

    private SparseArray<SurveySection> sectionMap;
    private List<Integer> sectionOrder;
    private int currentFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        List<Section> sections;
        sections = getIntent().getExtras().getParcelableArrayList(EXTRA_SECTIONS);


        setContentView(R.layout.activity_survey);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //sectionMap is a map from ids to their associated Section
        sectionMap = new SparseArray<>();
        //sectionOrder is a list of IDs in the correct order
        sectionOrder = new LinkedList<>();

        NavigationView navigationView = findViewById(R.id.nav_view);
        Menu m = navigationView.getMenu();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        boolean first = true;
        int id = 1;
        for (Section s : sections) {
            sectionOrder.add(id);
            SurveySection surveySectionFragment = s.getSurveySectionFragment();
            MenuItem menuItem = m.add(Menu.NONE, id, Menu.NONE, s.getName()).setCheckable(true);
            fragmentTransaction.add(R.id.fragment_container, surveySectionFragment);

            //If this is the first section
            if (first) {
                first = false;
                setTitle(s.getName());
                currentFragment = id;
                menuItem.setChecked(true);
            } else {
                fragmentTransaction.hide(surveySectionFragment);
            }

            sectionMap.append(id, surveySectionFragment);
            id++;
        }

        //Set the submit button to be a different colour
        SpannableString spannableString = new SpannableString("Submit Survey");
        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary)),
                0, spannableString.length(), 0);
        m.add(Menu.NONE, 0, Menu.NONE, spannableString).setCheckable(true);


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

        if (id == 0) submitSurvey();
        else showFragment(id);

        return true;
    }

    public void nextSectionClick(View view) {
        int id = sectionOrder.get((sectionOrder.indexOf(currentFragment) + 1) % sectionOrder.size());
        showFragment(id);
    }

    public void submitSurveyClick(View view) {
        submitSurvey();
    }

    public void submitSurvey() {
        //TODO: submit survey
    }

    private void showFragment(int id) {
        setTitle(sectionMap.get(id).getSection().getName());

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();

        fragmentTransaction.hide(sectionMap.get(currentFragment));
        fragmentTransaction.show(sectionMap.get(id));
        currentFragment = id;

        fragmentTransaction.commit();
    }
}
