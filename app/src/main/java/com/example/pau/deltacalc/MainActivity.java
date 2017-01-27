package com.example.pau.deltacalc;


import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private static final String CURRENT_FRAG = "CURRENT_FRAG";
    int currentFrag;
    Toolbar toolbar;
    DrawerLayout drawer;
    NavigationView navigationView;
    CalculatorFragment calculatorFragment;
    ExampleFragment exampleFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState != null){
            currentFrag = savedInstanceState.getInt(CURRENT_FRAG);
        }
        else currentFrag = R.id.nav_calc;
        setFragment(currentFrag);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.nav_drawer_open, R.string.nav_drawer_close);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        setFragment(item.getItemId());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    public void setFragment(int id){
        switch (id){
            case R.id.nav_calc:
                calculatorFragment = new CalculatorFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, calculatorFragment).commit();
                break;
            case R.id.nav_example:
                exampleFragment = new ExampleFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, exampleFragment).commit();
                break;
            case R.id.nav_settings:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)) drawer.closeDrawer(GravityCompat.START);
        else super.onBackPressed();
    }

    public void onClick(View v){
        switch (currentFrag){
            case R.id.nav_calc:
                calculatorFragment.onClick(v);
                break;
        }
    }
}
