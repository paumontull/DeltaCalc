package com.example.pau.deltacalc;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class CalculatorFragment extends Fragment {

    private static final String RESULT_HINT = "RESULT_HINT";
    private static final String TEXT_SIZE = "TEXT_SIZE";
    private static final String DISPLAY_STATE = "DISPLAY_STATE";
    private EditText formulaEditText, resultEditText;
    private RelativeLayout gif;
    private LinearLayout display;
    private View v;
    private int displayState;
    private int notMode = 0;

    public CalculatorFragment(){
        // Required empty public constructor
    }

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState != null) {
            resultEditText.setHint(savedInstanceState.getString(RESULT_HINT));
            resultEditText.setTextSize(TypedValue.COMPLEX_UNIT_PX, savedInstanceState.getFloat(TEXT_SIZE));
            displayState = savedInstanceState.getInt(DISPLAY_STATE);
            setDisplay(displayState);
            eval();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_calculator, container, false);
        formulaEditText = (EditText) v.findViewById(R.id.display_formula);
        resultEditText = (EditText) v.findViewById(R.id.display_result);

        display = (LinearLayout) v.findViewById(R.id.display);
        gif = (RelativeLayout) v.findViewById(R.id.gif);

        setDisplay(0);

        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(resultEditText.getHint() != null) outState.putString("RESULT_HINT", resultEditText.getHint().toString());
        outState.putFloat("TEXT_SIZE", resultEditText.getTextSize());
        outState.putInt("DISPLAY_STATE", displayState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.calc_notification_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_not_snack:
                item.setChecked(true);
                notMode = 0;
                return true;
            case R.id.nav_not_toast:
                item.setChecked(true);
                notMode = 1;
                return true;
            case R.id.nav_not_state:
                item.setChecked(true);
                notMode = 2;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onClick(View v){
        setDisplay(0);
        resultEditText.setText("");
        resultEditText.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.result_hint));

        switch(v.getId()){
            case R.id.op_mul:
            case R.id.op_div:
            case R.id.op_sum:
            case R.id.op_sub:
            case R.id.op_exp:
            case R.id.l_par:
                formulaEditText.append(((Button) v).getText());
                break;
            case R.id.r_par:
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
                formulaEditText.append(((Button) v).getText());
                eval();
                break;
            case R.id.dec_point:
                formulaEditText.append(".");
                break;
            case R.id.del:
                onDelete();
                break;
            case R.id.clr:
                onClear();
                break;
            case R.id.eq:
                onEqual();
                break;
            case R.id.call:
                onDial();
                break;
        }
    }

    private void eval(){
        try {
            String eval = ShuntingYard.eval(formulaEditText.getText().toString());
            while (eval.endsWith("0") || eval.endsWith(".")) {
                eval = eval.substring(0, eval.length() - 1);
            }
            if(eval.equals("0E-15")) resultEditText.setHint("0");
            else resultEditText.setHint(eval);
        }
            catch(RuntimeException e){
                switch (e.getMessage()){
                    case "Division by zero":
                        setDisplay(1);
                    case "Bad expression":
                    case "Mismatched parentheses":
                    case "Bad exponent":
                        resultEditText.setHint("NaN");
                        if(notMode == 0) Snackbar.make(v, e.getMessage(),Snackbar.LENGTH_SHORT).show();
                        else if (notMode == 1) Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        else{
                            NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
                            NotificationCompat.Builder mBuilder =
                                    new NotificationCompat.Builder(getActivity())
                                            .setSmallIcon(R.drawable.ic_clear)
                                            .setContentTitle("Math error: NaN")
                                            .setContentText(e.getMessage());

                            Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                            intent.putExtra(SearchManager.QUERY, e.getMessage());
                            PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 0, intent, 0);
                            mBuilder.setContentIntent(pendingIntent);
                            mBuilder.setAutoCancel(true);

                            notificationManager.notify(1, mBuilder.build());
                        }
                        break;
                    default:
                        throw e;
                }
        }
    }

    private void onDial(){
        String phone_num = formulaEditText.getText().toString();
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone_num));
        startActivity(intent);
    }

    private void onClear(){
        formulaEditText.setText("");
        resultEditText.setHint("");
    }

    private void onDelete(){
        String text = formulaEditText.getText().toString();
        if(text.length() != 0) formulaEditText.setText(text.substring(0, text.length() - 1));
        eval();
    }

    private void onEqual(){
        eval();
        resultEditText.setText(resultEditText.getHint());
        resultEditText.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.result_text));
    }

    private void setDisplay(int state){
        displayState = state;
        switch (state){
            case 0:
                display.setVisibility(View.VISIBLE);
                gif.setVisibility(View.GONE);
                break;
            case 1:
                display.setVisibility(View.GONE);
                gif.setVisibility(View.VISIBLE);
                break;
        }
    }
}
