package com.example.pau.deltacalc;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by Pau Montull i Jové on 21/1/17.
 */

public class SquareCardView extends CardView {

    public SquareCardView(Context context){
        super(context);
    }

    public SquareCardView(Context context, AttributeSet attrs){
        super(context, attrs);
    }

    public SquareCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Set a square layout.
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
