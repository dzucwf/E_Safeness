package com.safeness.patient.ui.fragment;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.safeness.e_saveness_common.base.AppBaseFragment;
import com.safeness.patient.R;
import com.safeness.patient.ui.view.GlucoseInputView;

import java.util.ArrayList;

//血糖
public class NaviGlucoseFragment extends AppBaseFragment implements ViewPager.OnPageChangeListener{


    private LinearLayout mImageIndex;
    private ViewPager mViewPager;

    private void getViews(View view) {
        mImageIndex = (LinearLayout) view.findViewById(R.id.imageNavi);
        mImageIndex.removeAllViews();
        mViewPager = (ViewPager) view.findViewById(R.id.glucose_view_pager);
        ArrayList<View> list = new ArrayList<View>();
        //添加输入血糖控件
        for(int i=0; i<4; i++) {

            GlucoseInputView glucoseinputview = new GlucoseInputView(this.getActivity());
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

    @Override
    public void onPageScrollStateChanged(int arg0) { }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) { }

    @Override
    public void onPageSelected(int index) {
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
