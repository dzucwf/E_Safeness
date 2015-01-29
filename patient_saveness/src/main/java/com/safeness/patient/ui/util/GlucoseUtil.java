package com.safeness.patient.ui.util;

import android.content.Context;

import com.safeness.patient.R;

/**
 * Created by EISAVISA on 2015/1/29.
 */
public class GlucoseUtil {

    private Context mContext;
    public GlucoseUtil(Context context){

        mContext = context;

    }

    public  int setTextColorByValue(double value){

        if(value >9 || value <3){
            return mContext.getResources().getColor(R.color.red);
        }else{
            return mContext.getResources().getColor(R.color.blue);
        }
    }
}
