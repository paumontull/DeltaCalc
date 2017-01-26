package com.example.pau.deltacalc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class DeltaCalculator extends AppCompatActivity {

private EditText formula, result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        formula = (EditText) findViewById(R.id.display_formula);
        result = (EditText) findViewById(R.id.display_result);
    }

    public void onClick(View v){
        switch(v.getId()){/*
            case R.id.dig_0:
                break;
            case R.id.dig_1:
                break;
            case R.id.dig_2:
                break;
            case R.id.dig_3:
                break;
            case R.id.dig_4:
                break;
            case R.id.dig_5:
                break;
            case R.id.dig_6:
                break;
            case R.id.dig_7:
                break;
            case R.id.dig_8:
                break;
            case R.id.dig_9:
                break;*/
            case R.id.del:
                onDelete();
                break;
            case R.id.clr:
                onClear();
                break;/*
            case R.id.op_mul:
                break;
            case R.id.op_div:
                break;
            case R.id.op_sum:
                break;
            case R.id.op_sub:
                break;
            case R.id.dec_point:
                break;
            case R.id.l_par:
                break;
            case R.id.r_par:
                break;*/
            case R.id.eq:
                onEqual();
                break;
            default:
                formula.append(((Button) v).getText());
                break;
        }
    }

    private void onEqual() {

        result.setText(formula.getText());
    }

    private void onClear() {
        formula.setText("");
        result.setText("");
    }

    private void onDelete() {
        String text = formula.getText().toString();
        if(text.length() != 0) formula.setText(text.substring(0, text.length() - 1));
    }
}
