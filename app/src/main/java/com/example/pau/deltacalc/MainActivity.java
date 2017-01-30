package com.example.pau.deltacalc;


import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private static final String CALC = "CALC";
    private static final String EXAMPLE = "EXAMPLE";
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

        if(savedInstanceState != null) {
            setFragment(savedInstanceState.getInt(CURRENT_FRAG));
        }
        else setFragment(R.id.nav_calc);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.nav_drawer_open, R.string.nav_drawer_close);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void setFragment(int id){
        switch (id){
            case R.id.nav_calc:
                toMainFragment();
                break;
            case R.id.nav_example:
                toExFragment();
                break;
            case R.id.nav_settings:
                break;
        }
    }

    private void toMainFragment(){
        if(getSupportFragmentManager().getBackStackEntryCount() > 0){
            //The main fragment is in the backstack
            getSupportFragmentManager().popBackStack();
        }
        else{
            //The main fragment is not in the backstack
            calculatorFragment = (CalculatorFragment) getSupportFragmentManager().findFragmentByTag(CALC);
            if(calculatorFragment == null){
                //The main fragment has not been loaded yet
                calculatorFragment = new CalculatorFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, calculatorFragment, CALC).commit();
            }
        }
        currentFrag = R.id.nav_calc;
    }

    private void toExFragment(){
        if(getSupportFragmentManager().getBackStackEntryCount() == 0){
            //The current fragment is the main fragment
            exampleFragment = new ExampleFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, exampleFragment, EXAMPLE).addToBackStack(null).commit();
        }
        else{
            //The current fragment is not the main fragment
            exampleFragment = (ExampleFragment) getSupportFragmentManager().findFragmentByTag(EXAMPLE);
            if(exampleFragment == null){
                //The current fragment is neither the main fragment nor itself
                exampleFragment = new ExampleFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, exampleFragment, EXAMPLE).commit();
            }
        }
        currentFrag = R.id.nav_example;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("CURRENT_FRAG", currentFrag);
    }

    public void onClick(View v){
        switch (currentFrag){
            case R.id.nav_calc:
                calculatorFragment.onClick(v);
                break;
        }
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

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)) drawer.closeDrawer(GravityCompat.START);
        else if(getSupportFragmentManager().getBackStackEntryCount() > 0){
            getSupportFragmentManager().popBackStack();
            navigationView.setCheckedItem(R.id.nav_calc);
        }
        else super.onBackPressed();
    }
}
