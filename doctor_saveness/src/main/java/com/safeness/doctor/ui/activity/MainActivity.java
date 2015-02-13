package com.safeness.doctor.ui.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.safeness.doctor.R;
import com.safeness.doctor.adapter.DoctorNaviSwitchAdapter;
import com.safeness.e_saveness_common.base.AppBaseActivity;


public class MainActivity extends AppBaseActivity {

    private static final String TAG = "MainActivity";

    private ViewPager mSearchVp;
    private final int CB_INDEX_MAIN = 0;
    private final int CB_INDEX_MESSAGE = 1;
    private final int CB_INDEX_CONTACT = 2;
    private final int CB_INDEX_MINE = 3;



    private ImageView navi_switcher_item_main;
    private ImageView navi_switcher_item_message;
    private ImageView navi_switcher_item_content;
    private ImageView navi_switcher_item_mine;


    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    private TextView textView4;

    DoctorNaviSwitchAdapter switchAdapter;
    //上次选中的
    int lastSelectIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    //初始化下层切换
    private void setViews() {
        switchAdapter = new DoctorNaviSwitchAdapter(getSupportFragmentManager());

        mSearchVp = (ViewPager) findViewById(R.id.navi_view_pager);
        mSearchVp.setAdapter(switchAdapter);
        mSearchVp.setOnPageChangeListener(mPageChgListener);

        navi_switcher_item_main = (ImageView)this.findViewById(R.id.navi_switcher_item_main);
        navi_switcher_item_message = (ImageView)this.findViewById(R.id.navi_switcher_item_message);
        navi_switcher_item_content = (ImageView)this.findViewById(R.id.navi_switcher_item_content);
        navi_switcher_item_mine = (ImageView)this.findViewById(R.id.navi_switcher_item_mine);
        textView1 = (TextView)this.findViewById(R.id.textView1);
        textView2 = (TextView)this.findViewById(R.id.textView2);
        textView3 = (TextView)this.findViewById(R.id.textView3);
        textView4 = (TextView)this.findViewById(R.id.textView4);



    }

    /*
   * 设置按钮的选择状态
   * */
    private void setSelectButtonState(int curId){


        Log.d(TAG, curId + "");
        switch(curId) {
            case CB_INDEX_MAIN:
                navi_switcher_item_main.setImageResource(R.drawable.icon01_pressed);
                textView1.setTextColor(getResources().getColor(R.color.selected_text_color));
                break;
            case CB_INDEX_MESSAGE:
                navi_switcher_item_message.setImageResource(R.drawable.icon02_pressed);
                textView2.setTextColor(getResources().getColor(R.color.selected_text_color));
                break;
            case CB_INDEX_CONTACT:
                navi_switcher_item_content.setImageResource(R.drawable.icon03_pressed);
                textView3.setTextColor(getResources().getColor(R.color.selected_text_color));
                break;
            case CB_INDEX_MINE:
                navi_switcher_item_mine.setImageResource(R.drawable.icon04_pressed);
                textView4.setTextColor(getResources().getColor(R.color.selected_text_color));

                break;

        }

        switch(lastSelectIndex) {
            case CB_INDEX_MAIN:
                navi_switcher_item_main.setImageResource(R.drawable.icon01_nomal);
                textView1.setTextColor(getResources().getColor(R.color.normal_text_color));
                break;
            case CB_INDEX_MESSAGE:
                navi_switcher_item_message.setImageResource(R.drawable.icon02_nomal);
                textView2.setTextColor(getResources().getColor(R.color.normal_text_color));
                break;
            case CB_INDEX_CONTACT:
                navi_switcher_item_content.setImageResource(R.drawable.icon03_nomal);
                textView3.setTextColor(getResources().getColor(R.color.normal_text_color));
                break;
            case CB_INDEX_MINE:
                navi_switcher_item_mine.setImageResource(R.drawable.icon04_nomal);
                textView4.setTextColor(getResources().getColor(R.color.normal_text_color));
                break;

        }
        lastSelectIndex = curId;
    }

    /*
      * 选中导航
      * */
    public void onNaviCheck(View view){
        int cur = CB_INDEX_MAIN;

        switch (view.getId()){


            case R.id.navi_switcher_item_main:
                cur = CB_INDEX_MAIN;

                break;
            case R.id.navi_switcher_item_message:
                cur = CB_INDEX_MESSAGE;
                break;
            case R.id.navi_switcher_item_content:
                cur = CB_INDEX_CONTACT;
                break;
            case R.id.navi_switcher_item_mine:
                cur = CB_INDEX_MINE;
                break;

        }

        if(mSearchVp.getCurrentItem() != cur) {
            mSearchVp.setCurrentItem(cur);

        }

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
                case CB_INDEX_MAIN:

                    break;
                case CB_INDEX_MESSAGE:

                    break;
                case CB_INDEX_CONTACT:

                    break;
                case CB_INDEX_MINE:

                    break;

            }
        }


    };


    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void setupView() {
        setViews();
    }

    @Override
    protected void initializedData() {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
