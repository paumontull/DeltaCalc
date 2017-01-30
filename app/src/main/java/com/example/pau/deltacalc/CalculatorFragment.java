package com.example.pau.deltacalc;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

public class CalculatorFragment extends Fragment {

    private EditText formulaEditText, resultEditText;

    public CalculatorFragment(){
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_calculator, container, false);
        formulaEditText = (EditText) v.findViewById(R.id.display_formula);
        resultEditText = (EditText) v.findViewById(R.id.display_result);

        if(savedInstanceState != null){
            formulaEditText.setText(savedInstanceState.getString(FORMULA_TEXT));
            resultEditText.setText(savedInstanceState.getString(RESULT_TEXT));
        }

        return v;
    }

    public void onClick(View v){
        resultEditText.setText("");
        switch(v.getId()){/*
            case R.id.dig_0:
            case R.id.dig_1:
            case R.id.dig_2:
            case R.id.dig_3:
            case R.id.dig_4:
            case R.id.dig_5:
            case R.id.dig_6:
            case R.id.dig_7:
            case R.id.dig_8:
            case R.id.dig_9:
            case R.id.op_mul:
            case R.id.op_div:
            case R.id.op_sum:
            case R.id.op_sub:
            case R.id.l_par:
            case R.id.r_par:*/
            case R.id.dec_point:
                formulaEditText.append(".");
                break;
            case R.id.del:
                onDelete();
                break;
            case R.id.clr:
                formulaEditText.setText("");
                break;
            case R.id.eq:
                resultEditText.setText(resultEditText.getHint());
                break;
            case R.id.call:
                onCall();
                break;
            default:
                formulaEditText.append(((Button) v).getText());
                break;
        }
        String postfix = ShuntingYard.postfix(formulaEditText.getText().toString());
        if(postfix.length() != 0){
            while (postfix.charAt(postfix.length() - 1) == '0'){
                postfix = postfix.substring(0, postfix.length() - 1);
            }
            if(postfix.charAt(postfix.length() - 1) == '.') postfix = postfix.substring(0, postfix.length() - 1);
        }
        switch(postfix){
            case "Division by zero":
                Snackbar.make(v, "Division by zero: NaN",Snackbar.LENGTH_SHORT).show();
                resultEditText.setHint("NaN");
                break;
            case "Mismatched parenthesis":
                Snackbar.make(v, "Mismatched parenthesis",Snackbar.LENGTH_SHORT).show();
                resultEditText.setHint("NaN");
                break;
            default:
                resultEditText.setHint(postfix);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void onCall() {
        String phone_num = formulaEditText.getText().toString();
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone_num));
        startActivity(intent);
    }

    private void onDelete() {
        String text = formulaEditText.getText().toString();
        if(text.length() != 0) formulaEditText.setText(text.substring(0, text.length() - 1));
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstantState) {
        savedInstantState.putString("FORMULA_TEXT", formulaEditText.getText().toString());
        savedInstantState.putString("RESULT_TEXT", resultEditText.getText().toString());
        super.onSaveInstanceState(savedInstantState);
    }
}
