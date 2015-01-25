package com.safeness.patient.ui.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.safeness.e_saveness_common.base.AppBaseActivity;
import com.safeness.patient.R;
import com.safeness.patient.adapter.BtmNaviSwitchAdapter;


public class MainActivity extends AppBaseActivity {




    private static final String TAG = "MainActivity";
    private LinearLayout mSwitcher;
    private ViewPager mSearchVp;
    private final int CB_INDEX_FOOD = 0;
    private final int CB_INDEX_DRUG = 1;
    private final int CB_INDEX_GLUCOSE = 2;
    private final int CB_INDEX_SPORT = 3;
    private final int CB_INDEX_DOCTOR = 4;

    private ImageButton food_imagebtn;
    private ImageButton drugbtn;
    private ImageButton glucosebtn;
    private ImageButton sportbtn;
    private ImageButton doctorbtn;
    //上次选中的
    int lastSelectIndex = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
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
        mSwitcher = (LinearLayout) findViewById(R.id.navi_switcher);

        mSearchVp = (ViewPager) findViewById(R.id.navi_view_pager);
        mSearchVp.setAdapter(new BtmNaviSwitchAdapter(getSupportFragmentManager()));
        mSearchVp.setOnPageChangeListener(mPageChgListener);
        food_imagebtn = (ImageButton)this.findViewById(R.id.navi_switcher_item_food);
        drugbtn = (ImageButton)this.findViewById(R.id.navi_switcher_item_drug);
        glucosebtn = (ImageButton)this.findViewById(R.id.navi_switcher_item_glucose);
        sportbtn = (ImageButton)this.findViewById(R.id.navi_switcher_item_sports);
        doctorbtn = (ImageButton)this.findViewById(R.id.navi_switcher_item_doctor);
    }

   /*
   * 选中导航
   * */
    public void onNaviCheck(View view){
        int cur = CB_INDEX_FOOD;

        switch (view.getId()){


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

    /*
    * 设置按钮的选择状态
    * */
    private void setSelectButtonState(int curId){


        Log.d(TAG,curId+"");
        switch(curId) {
            case CB_INDEX_FOOD:
                food_imagebtn.setImageResource(R.drawable.nav_myday_selected);
                break;
            case CB_INDEX_DRUG:
                drugbtn.setImageResource(R.drawable.medicine_selected);
                break;
            case CB_INDEX_GLUCOSE:
                glucosebtn.setImageResource(R.drawable.diabetes_nav_glucose_on);
                break;
            case CB_INDEX_SPORT:
                sportbtn.setImageResource(R.drawable.diabetes_nav_act_on);

                break;
            case CB_INDEX_DOCTOR:
                doctorbtn.setImageResource(R.drawable.navigation_refine_05);
                break;
        }

        switch(lastSelectIndex) {
            case CB_INDEX_FOOD:
                food_imagebtn.setImageResource(R.drawable.nav_myday_unselected);
                break;
            case CB_INDEX_DRUG:
                drugbtn.setImageResource(R.drawable.medicine_unselected);
                break;
            case CB_INDEX_GLUCOSE:
                glucosebtn.setImageResource(R.drawable.diabetes_nav_glucose_off);
                break;
            case CB_INDEX_SPORT:
                sportbtn.setImageResource(R.drawable.diabetes_nav_act_off);
                break;
            case CB_INDEX_DOCTOR:
                doctorbtn.setImageResource(R.drawable.navigation_refine_06);
                break;
        }
            lastSelectIndex = curId;
    }


    private ViewPager.OnPageChangeListener mPageChgListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrollStateChanged(int arg0) { }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

        @Override
        public void onPageSelected(int position) {
            int vpItem = mSearchVp.getCurrentItem();
            setSelectButtonState(vpItem);
            switch(vpItem) {
                case CB_INDEX_FOOD:

                    break;
                case CB_INDEX_DRUG:

                    break;
                case CB_INDEX_GLUCOSE:

                    break;
                case CB_INDEX_SPORT:

                    break;
                case CB_INDEX_DOCTOR:

                    break;
            }
        }

    };
}
