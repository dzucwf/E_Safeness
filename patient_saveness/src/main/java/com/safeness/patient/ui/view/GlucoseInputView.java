package com.safeness.patient.ui.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.safeness.patient.R;

/**
 * Created by EISAVISA on 2015/1/23.
 */
public class GlucoseInputView extends LinearLayout {


    private WebView chartWebView;
    public GlucoseInputView(final Context context) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
       View v =  inflater.inflate(R.layout.input_glucose_view, this);
        View beforeImageButton = v.findViewById(R.id.before_ibt);
        View afterImageButton = v.findViewById(R.id.after_ibt);
        beforeImageButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                icallBack.onClickButton(v.getTag().toString());
            }
        });
        afterImageButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                icallBack.onClickButton(v.getTag().toString());
            }
        });




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
