package com.example.pau.deltacalc;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class CalculatorFragment extends Fragment {

    private EditText formulaEditText, resultEditText;
    private StringBuilder formula, result;

    public CalculatorFragment(){
        // Required empty public constructor

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_calculator, container, false);
        formulaEditText = (EditText) v.findViewById(R.id.display_formula);
        resultEditText = (EditText) v.findViewById(R.id.display_result);

        return v;
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
                formulaEditText.append(((Button) v).getText());
                break;
        }
    }

    private void onEqual() {
        resultEditText.setText(formulaEditText.getText());
    }

    private void onClear() {
        formulaEditText.setText("");
        resultEditText.setText("");
    }

    private void onDelete() {
        String text = formulaEditText.getText().toString();
        if(text.length() != 0) formulaEditText.setText(text.substring(0, text.length() - 1));
    }
}
