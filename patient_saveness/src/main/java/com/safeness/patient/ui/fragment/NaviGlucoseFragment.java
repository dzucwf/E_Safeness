package com.safeness.patient.ui.fragment;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.safeness.e_saveness_common.base.AppBaseFragment;
import com.safeness.patient.R;
import com.safeness.patient.ui.activity.GlucoseInputActivity;
import com.safeness.patient.ui.view.GlucoseInputView;

import java.util.ArrayList;
import java.util.Calendar;

//血糖
public class NaviGlucoseFragment extends AppBaseFragment implements ViewPager.OnPageChangeListener{



    private static final  String TAG = "NaviGlucoseFragment";
    private LinearLayout mImageIndex;
    private ViewPager mViewPager;

    ImageButton after_ibt;
    ImageButton before_ibt;

    TextView dateTextViw;
    TextView glucose_time_tv;

    private int selectTime = 0;

    private int server_id;


    private void getViews(View view) {

        dateTextViw = (TextView)getActivity().findViewById(R.id.date_glucose_tv);
        glucose_time_tv = (TextView)getActivity().findViewById(R.id.glucose_time_tv);
        glucose_time_tv.setText(getString(R.string.breakfast));
        Calendar c = Calendar.getInstance();

        String strNow = c.get(Calendar.YEAR)+"年"+c.get(Calendar.MONTH)+1+"月"+c.get(Calendar.DATE)+"日";
        dateTextViw.setText(strNow);

        mImageIndex = (LinearLayout) view.findViewById(R.id.imageNavi);
        mImageIndex.removeAllViews();
        mViewPager = (ViewPager) view.findViewById(R.id.glucose_view_pager);
        ArrayList<View> list = new ArrayList<View>();
        //添加输入血糖控件
        for(int i=0; i<4; i++) {

            GlucoseInputView glucoseinputview = new GlucoseInputView(this.getActivity());
            //得到点击的是哪个输入框
            glucoseinputview.setCallBack(new GlucoseInputView.ICallBack() {
                @Override
                public void onClickButton(String s) {
                    Intent it  = new Intent(getActivity(), GlucoseInputActivity.class);

                    getActivity().startActivity(it);

                }
            });
            glucoseinputview.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            list.add(glucoseinputview);
            // add for index container
            ImageView index = new ImageView(getActivity());
            index.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            index.setPadding(8, 8, 8, 8);
            index.setImageResource(R.drawable.shelf_circle_selector);
            index.setSelected(i ==0 ? true : false);
            mImageIndex.addView(index);
        }
        mViewPager.setAdapter(new ViewPagerAdapter(list));
        mViewPager.setOnPageChangeListener(this);

    }


    private View.OnClickListener glucoseListenser = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(getActivity(), "之后", Toast.LENGTH_LONG).show();
        }
    };

    /*
    * 设置选中的输入血糖时间
    * */
    private void setGlucoseTxt(int position){
        selectTime = position;
        switch (position){
            case 0:
                glucose_time_tv.setText(getString(R.string.breakfast));
                break;
            case 1:
                glucose_time_tv.setText(getString(R.string.lunch));
                break;
            case 2:
                glucose_time_tv.setText(getString(R.string.supper));
                break;
            case 3:
                glucose_time_tv.setText(getString(R.string.other));
                break;
            default:break;


        }
    }

    @Override
    public void onPageScrollStateChanged(int arg0) { }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) { }

    @Override
    public void onPageSelected(int index) {

        setGlucoseTxt(index);
        int cnt = mImageIndex.getChildCount();
        for(int i=0; i<cnt; i++) {
            mImageIndex.getChildAt(i).setSelected(i == index ? true : false);
        }

    }

    private class ViewPagerAdapter extends PagerAdapter {
        private ArrayList<View> mList;

        public ViewPagerAdapter(ArrayList<View> views) {
            mList = views;
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object instantiateItem(View container, int position) {
            View view = mList.get(position);
            ((ViewPager)container).addView(view, 0);
            return view;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return (arg0 == arg1);
        }

        @Override
        public Parcelable saveState() {
            return super.saveState();
        }

        @Override
        public void startUpdate(View container) {
        }

        @Override
        public void finishUpdate(View container) {
        }

        @Override
        public void restoreState(Parcelable state, ClassLoader loader) {
            super.restoreState(state, loader);
        }

        @Override
        public void destroyItem(View container, int position, Object object) {
            ((ViewPager)container).removeView(mList.get(position));
        }
    }


    @Override
    protected int getLayoutId() {
        return R.layout.navi_hp_glucose;
    }

    @Override
    protected void setupView() {

        getViews(this.getView());

    }

    @Override
    protected void initializedData() {

    }

}
