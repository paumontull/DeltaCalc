package com.example.pau.deltacalc;


import android.Manifest;
import android.content.pm.PackageManager;
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
    private static final String MUSIC = "MUSIC";
    private static final String MEMORY = "MEMORY";
    private static final String CURRENT_FRAG = "CURRENT_FRAG";
    int currentFrag;
    Toolbar toolbar;
    DrawerLayout drawer;
    NavigationView navigationView;
    CalculatorFragment calculatorFragment;
    MusicFragment musicFragment;
    MemoryFragment memoryFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(checkSelfPermission(Manifest.permission_group.STORAGE) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission_group.STORAGE}, 123);
            Log.v("PERM", "request");
        }

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.nav_drawer_open, R.string.nav_drawer_close);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if(savedInstanceState != null) {
            setFragment(savedInstanceState.getInt(CURRENT_FRAG));
        }
        else setFragment(R.id.nav_calc);
    }

    public void setFragment(int id){
        switch (id){
            case R.id.nav_calc:
                toMainFragment();
                break;
            case R.id.nav_music:
                toMusicFragment();
                break;
            case R.id.nav_memory:
                toMemoryFragment();
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
        getSupportActionBar().setTitle(R.string.nav_calc);
    }

    private void toMusicFragment(){
        if(getSupportFragmentManager().getBackStackEntryCount() == 0){
            //The current fragment is the main fragment
            musicFragment = new MusicFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, musicFragment, MUSIC).addToBackStack(null).commit();
        }
        else{
            //The current fragment is not the main fragment
            musicFragment = (MusicFragment) getSupportFragmentManager().findFragmentByTag(MUSIC);
            if(musicFragment == null){
                //The current fragment is neither the main fragment nor itself
                musicFragment = new MusicFragment();
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, musicFragment, MUSIC).commit();
        }
        currentFrag = R.id.nav_music;
        getSupportActionBar().setTitle(R.string.nav_music);
    }

    private void toMemoryFragment(){
        if(getSupportFragmentManager().getBackStackEntryCount() == 0){
            //The current fragment is the main fragment
            memoryFragment = new MemoryFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, memoryFragment, MEMORY).addToBackStack(null).commit();
        }
        else{
            //The current fragment is not the main fragment
            memoryFragment = (MemoryFragment) getSupportFragmentManager().findFragmentByTag(MEMORY);
            if(memoryFragment == null){
                //The current fragment is neither the main fragment nor itself
                memoryFragment = new MemoryFragment();
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, memoryFragment, MEMORY).commit();
        }
        currentFrag = R.id.nav_memory;
        getSupportActionBar().setTitle(R.string.nav_memory);
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
            case R.id.nav_memory:
                memoryFragment.onClick(v);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        switch (currentFrag){
            case R.id.nav_calc:
                getMenuInflater().inflate(R.menu.notification_menu, menu);
                break;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_not_snack:
                item.setChecked(true);
                calculatorFragment.setNotMode(true);
                return true;
            case R.id.nav_not_toast:
                item.setChecked(true);
                calculatorFragment.setNotMode(false);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
