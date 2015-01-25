package com.safeness.patient.ui.activity;

import android.content.Context;
import android.view.View;

import com.safeness.e_saveness_common.base.AppBaseActivity;
import com.safeness.patient.R;

/**
 * Created by EISAVISA on 2015/1/24.
 */
public class GlucoseInputActivity extends AppBaseActivity {


    Context mContext;
    @Override
    protected int getLayoutId() {
        return R.layout.glucose_input_dialog;
    }

    @Override
    protected void setupView() {
        mContext = this;
    }

    @Override
    protected void initializedData() {

    }

    public void cancel(View view){

        this.finish();
    }
}
