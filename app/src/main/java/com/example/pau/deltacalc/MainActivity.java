package com.example.pau.deltacalc;


import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private static final String CURRENT_FRAG = "CURRENT_FRAG";
    int currentFrag;
    DrawerLayout drawer;
    NavigationView navigationView;

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

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
                CalculatorFragment calculatorFragment = new CalculatorFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, calculatorFragment).commit();
                break;
            case R.id.nav_example:
                ExampleFragment exampleFragment = new ExampleFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, exampleFragment).commit();
                break;
            case R.id.nav_settings:
                break;
        }
    }
}
