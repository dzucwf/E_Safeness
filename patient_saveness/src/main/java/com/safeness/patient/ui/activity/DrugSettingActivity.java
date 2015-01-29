package com.safeness.patient.ui.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.safeness.e_saveness_common.base.AppBaseActivity;
import com.safeness.patient.R;
import com.safeness.patient.adapter.BtmNaviSwitchAdapter;

/**
 * Created by Lionnd on 2015/1/29.
 */
public class DrugSettingActivity extends AppBaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    protected int getLayoutId() {
        return R.layout.drug_setting;
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


    }
}
