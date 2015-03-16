package com.safeness.patient.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.safeness.e_saveness_common.base.AppBaseActivity;
import com.safeness.patient.R;

/**
 * Created by Lionnd on 2015/2/2.
 */
public class HistoryActivity  extends AppBaseActivity {

    private ImageView btn_back;
    private ImageView btn_chartType;

    private String chart_type = "all";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*取出Intent中附加的数据*/
        Intent intent =getIntent();
        chart_type = intent.getStringExtra("chart_type");


        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HistoryActivity.this.finish();
            }
        });
        btn_chartType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(R.id.drug_setting_remind_list_label_time);
            }
        });

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_history;
    }

    @Override
    protected void setupView() {
        getViews();
    }

    @Override
    protected void initializedData() {

    }

    //初始化下层切换
    private void getViews() {
        btn_back = (ImageView)findViewById(R.id.btn_history_nav_back);
        btn_chartType = (ImageView)findViewById(R.id.btn_history_nav_item);

    }
    @Override
    protected Dialog onCreateDialog(int id)
    {
        switch (id) {/*
        case R.id.drug_setting_remind_list_label_time:
            Date curDate   =   new   Date(System.currentTimeMillis());
            return  new TimePickerDialog(
                    this, mTimeSetListener, curDate.getHours(), curDate.getMinutes(), true);*/
    }
        return null;
    }
}

