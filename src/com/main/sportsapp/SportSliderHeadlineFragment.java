package com.main.sportsapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockListFragment;
import com.astuetz.PagerSlidingTabStrip;
import com.webileapps.navdrawer.R;

public class SportSliderHeadlineFragment extends Fragment {
	public static final String PREFS_NAME = "gitSportsettings";
	

	public static final String TAG = SportSliderHeadlineFragment.class
			.getSimpleName();

	public static SportSliderHeadlineFragment newInstance() {
		return new SportSliderHeadlineFragment();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.pager, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		SharedPreferences settings = getActivity().getSharedPreferences(PREFS_NAME, 0);
		String favoriteSport = settings.getString("favorite", "");

		PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) view
				.findViewById(R.id.tabs);
		ViewPager pager = (ViewPager) view.findViewById(R.id.pager);
		MyPagerAdapter adapter = new MyPagerAdapter(getChildFragmentManager());
		pager.setAdapter(adapter);
		tabs.setViewPager(pager);
		
		if(favoriteSport.equalsIgnoreCase("NBA")){
			pager.setCurrentItem(1);
		}else if(favoriteSport.equalsIgnoreCase("NHL")){
			pager.setCurrentItem(2);
		}else if(favoriteSport.equalsIgnoreCase("MLB")){
			pager.setCurrentItem(3);
		}else if(favoriteSport.equalsIgnoreCase("NFL")){
			pager.setCurrentItem(4);
		}else{
			pager.setCurrentItem(0);
		}

	}

	public class MyPagerAdapter extends FragmentPagerAdapter {

		public MyPagerAdapter(android.support.v4.app.FragmentManager fm) {
			super(fm);
		}

		private final String[] TITLES = { "TOP","NBA", "NHL", "MLB",
				"NFL" };

		@Override
		public CharSequence getPageTitle(int position) {
			return TITLES[position];
		}

		@Override
		public int getCount() {
			return TITLES.length;
		}

		@Override
		public SherlockListFragment getItem(int position) {
			return HeadlinesFragment.newInstance(position);
		}

	}


}
