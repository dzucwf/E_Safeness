package com.safeness.patient.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.widget.RadioGroup;

import com.safeness.patient.R;
import com.safeness.patient.adapter.BtmNaviSwitchAdapter;


public class MainActivity extends FragmentActivity {





    private RadioGroup mSwitcher;
    private ViewPager mSearchVp;
    private final int CB_INDEX_FOOD = 0;
    private final int CB_INDEX_DRUG = 1;
    private final int CB_INDEX_GLUCOSE = 2;
    private final int CB_INDEX_SPORT = 3;
    private final int CB_INDEX_DOCTOR = 4;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getViews();
    }

    private void getViews() {
        mSwitcher = (RadioGroup) findViewById(R.id.navi_switcher);
        mSwitcher.setOnCheckedChangeListener(mCheckedChgLitener);
        mSearchVp = (ViewPager) findViewById(R.id.navi_view_pager);
        mSearchVp.setAdapter(new BtmNaviSwitchAdapter(getSupportFragmentManager()));
        mSearchVp.setOnPageChangeListener(mPageChgListener);
    }

    private RadioGroup.OnCheckedChangeListener mCheckedChgLitener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            int cur = CB_INDEX_FOOD;
            switch(checkedId) {
                case R.id.navi_switcher_item_food:
                    cur = CB_INDEX_FOOD;
                    break;
                case R.id.navi_switcher_item_drug:
                    cur = CB_INDEX_DRUG;
                    break;
                case R.id.navi_switcher_item_glucose:
                    cur = CB_INDEX_GLUCOSE;
                    break;
                case R.id.navi_switcher_item_sports:
                    cur = CB_INDEX_SPORT;
                    break;
                case R.id.navi_switcher_item_doctor:
                    cur = CB_INDEX_DOCTOR;
                    break;
            }
            if(mSearchVp.getCurrentItem() != cur) {
                mSearchVp.setCurrentItem(cur);
            }
        }
    };

    private ViewPager.OnPageChangeListener mPageChgListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrollStateChanged(int arg0) { }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

        @Override
        public void onPageSelected(int position) {
            int vpItem = mSearchVp.getCurrentItem();
            switch(vpItem) {
                case CB_INDEX_FOOD:
                    mSwitcher.check(R.id.navi_switcher_item_food);
                    break;
                case CB_INDEX_DRUG:
                    mSwitcher.check(R.id.navi_switcher_item_drug);
                    break;
                case CB_INDEX_GLUCOSE:
                    mSwitcher.check(R.id.navi_switcher_item_glucose);
                    break;
                case CB_INDEX_SPORT:
                    mSwitcher.check(R.id.navi_switcher_item_sports);
                    break;
                case CB_INDEX_DOCTOR:
                    mSwitcher.check(R.id.navi_switcher_item_doctor);
                    break;
            }
        }

    };
}
