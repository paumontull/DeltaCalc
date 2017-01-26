package com.example.pau.deltacalc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class DeltaCalculator extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
    }

    public void onClick(View v){
        switch(v.getId()){
            case R.id.dig_7:
                break;
        }
    }
}
