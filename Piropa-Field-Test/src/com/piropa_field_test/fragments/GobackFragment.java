package com.piropa_field_test.fragments;

import android.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.app.FragmentManager;  
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;  
import android.support.v4.app.FragmentTransaction;  
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.piropa_field_test.Export;
import com.piropa_field_test.R;
import com.piropa_field_test.data.DatabaseHandler;

public class GobackFragment extends Fragment {
	private static GobackFragment instance;
	View rootView;

	public static GobackFragment getInstance() {
		if (instance == null) {
			instance = new GobackFragment();
		}
		return instance;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_goback, container, false);
		return rootView;
	}

	
}
