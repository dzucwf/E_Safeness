package com.safeness.patient.ui.view;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.safeness.patient.R;
import com.safeness.patient.ui.util.GlucoseUtil;

/**
 * Created by EISAVISA on 2015/1/23.
 */
public class GlucoseInputView extends LinearLayout {



    private static  final  String TAG = "GlucoseInputView";
    private ImageButton before_ibt;

    private ImageButton after_ibt;

    private TextView glucose_value_before_tv;

    private TextView glucose_value_after_tv;

    private  Context mContext;

    GlucoseUtil glucoseUtil;
    public GlucoseInputView(final Context context) {
        super(context);
        mContext = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
       View v =  inflater.inflate(R.layout.input_glucose_view, this);
        before_ibt = (ImageButton)v.findViewById(R.id.before_ibt);
        after_ibt = (ImageButton)v.findViewById(R.id.after_ibt);

        glucose_value_before_tv = (TextView)v.findViewById(R.id.glucose_value_before_tv);
        glucose_value_after_tv = (TextView)v.findViewById(R.id.glucose_value_after_tv);

        before_ibt.setOnClickListener(listener);
        after_ibt.setOnClickListener(listener);

        glucose_value_after_tv.setOnClickListener(listener);
        glucose_value_before_tv.setOnClickListener(listener);



        glucoseUtil = new GlucoseUtil(context);

    }

    private OnClickListener listener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            icallBack.onClickButton(v.getTag().toString());
        }
    };


    /**
     * 设置选择框的值
     * @param value 传入值
     * @param visibility 圆圈是否显示
     */
    public void fillBeforeGlucose(String value,int visibility){
        before_ibt.setVisibility(visibility);

        if(visibility != View.INVISIBLE){
            glucose_value_before_tv.setText(mContext.getString(R.string.input_glucose));
            glucose_value_before_tv.setTextSize(25);
            glucose_value_before_tv.setTextColor(mContext.getResources().getColor(R.color.glucoseblue));
        }else{
            Log.d(TAG,value);
            glucose_value_before_tv.setVisibility(View.VISIBLE);
           glucose_value_before_tv.setText(value);


            try {
                glucose_value_before_tv.setTextColor(glucoseUtil.setTextColorByValue(Double.parseDouble(value)));
                glucose_value_before_tv.setTextSize(35);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 设置选择框的值
     * @param value 传入值
     * @param visibility 圆圈是否显示
     */
    public void fillAfterGlucose(String value,int visibility){



        after_ibt.setVisibility(visibility);

        if(visibility != View.INVISIBLE){
            glucose_value_after_tv.setText(mContext.getString(R.string.input_glucose));
            glucose_value_after_tv.setTextSize(25);
            glucose_value_after_tv.setTextColor(mContext.getResources().getColor(R.color.glucoseblue));

        }else{
            Log.d(TAG,value);
            glucose_value_after_tv.setVisibility(View.VISIBLE);
            glucose_value_after_tv.setText(value);


            try {
                glucose_value_after_tv.setTextColor(glucoseUtil.setTextColorByValue(Double.parseDouble(value)));
                glucose_value_after_tv.setTextSize(35);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }

    //录入点击事件
    public interface  ICallBack{

        public void onClickButton(String s);
    }

    ICallBack icallBack;

    //设置点击事件方法
    public void setCallBack(ICallBack iBack){

        icallBack = iBack;
    }
}
