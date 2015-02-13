package com.safeness.doctor.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.safeness.doctor.ui.fragment.ContactFragment;
import com.safeness.doctor.ui.fragment.MainFragment;
import com.safeness.doctor.ui.fragment.MessageFragment;
import com.safeness.doctor.ui.fragment.MineFragment;

import java.util.ArrayList;

public class DoctorNaviSwitchAdapter extends FragmentPagerAdapter{
	private ArrayList<Fragment> mFragments;

	public DoctorNaviSwitchAdapter(FragmentManager fm) {
		super(fm);
		mFragments = new ArrayList<>();
		mFragments.add(new MainFragment());
        mFragments.add(new MessageFragment());
		mFragments.add(new ContactFragment());
		mFragments.add(new MineFragment());


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
