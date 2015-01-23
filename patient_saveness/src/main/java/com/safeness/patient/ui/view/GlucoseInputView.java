package com.safeness.patient.ui.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.safeness.patient.R;

/**
 * Created by EISAVISA on 2015/1/23.
 */
public class GlucoseInputView extends LinearLayout {


    public GlucoseInputView(Context context) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.input_glucose_view, this);


    }
}
