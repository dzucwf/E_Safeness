package com.safeness.patient.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.safeness.patient.ui.activity.fragment.NaviDoctorFragment;
import com.safeness.patient.ui.activity.fragment.NaviDrugFragment;
import com.safeness.patient.ui.activity.fragment.NaviFoodFragment;
import com.safeness.patient.ui.activity.fragment.NaviGlucoseFragment;
import com.safeness.patient.ui.activity.fragment.NaviSportsFragment;

import java.util.ArrayList;

public class BtmNaviSwitchAdapter extends FragmentPagerAdapter{
	private ArrayList<Fragment> mFragments;
	
	public BtmNaviSwitchAdapter(FragmentManager fm) {
		super(fm);
		mFragments = new ArrayList<>();
		mFragments.add(new NaviFoodFragment());
        mFragments.add(new NaviDrugFragment());
		mFragments.add(new NaviGlucoseFragment());
		mFragments.add(new NaviSportsFragment());
        mFragments.add(new NaviDoctorFragment());
	}

	@Override
	public int getCount() {
		return mFragments.size();
	}

	@Override
	public Fragment getItem(int position) {
		return mFragments.get(position);
	}

}
