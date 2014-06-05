package com.main.sportsapp;

import com.actionbarsherlock.app.SherlockFragment;
import com.webileapps.navdrawer.R;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class SettingsFragment extends SherlockFragment {
	public static final String PREFS_NAME = "gitSportsettings";
	String favoriteSport;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		SharedPreferences settings = getActivity().getSharedPreferences(PREFS_NAME, 0);
		favoriteSport = settings.getString("favorite", "");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View settingView = inflater.inflate(R.layout.setting_fragment, container,false);
		final Spinner spinner = (Spinner)settingView.findViewById(R.id.favoritePicker);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
		        R.array.sports, android.R.layout.simple_spinner_item);
		
		Integer pos =adapter.getPosition(favoriteSport);
		Log.i("settings", favoriteSport);
		Log.i("settings", pos.toString());
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		spinner.setAdapter(adapter);
		
		spinner.setSelection(pos);
		
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				SharedPreferences settings = getActivity().getSharedPreferences(PREFS_NAME, 0);
				SharedPreferences.Editor editor = settings.edit();
				editor.putString("favorite", spinner.getSelectedItem().toString());
				editor.commit();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		    
		});
		
	
		
		
		return settingView;
	}
	
	

}
