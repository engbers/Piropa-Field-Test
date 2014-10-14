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

public class MainFragment extends Fragment {
	View rootView;
	Button b1, b2, b3, b4;

	public MainFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_main, container, false);
		initLayout();
		return rootView;
	}

	private void initLayout() {
		b1 = (Button) rootView.findViewById(R.id.button1);
		b1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				getActivity().getFragmentManager().beginTransaction()
						.replace(R.id.container, MeasureFragment.Instance(0,0))
						.addToBackStack(null).commit();
						
						
				
			}
		});
		b2 = (Button) rootView.findViewById(R.id.button2);
		b2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				getActivity().getFragmentManager().beginTransaction()
						.replace(R.id.container, LogFragment.getInstance())
						.addToBackStack(null).commit();
			}
		});
		b3 = (Button) rootView.findViewById(R.id.button3);
		b3.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				getActivity().getFragmentManager().beginTransaction()
				.replace(R.id.container, ExportFragment.getInstance())
				.addToBackStack(null).commit();
			}
		});
		b4 = (Button) rootView.findViewById(R.id.button4);
		b4.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				getActivity().getFragmentManager().beginTransaction()
						.replace(R.id.container, InfoFragment.getInstance())
						.addToBackStack(null).commit();
			}
		});
	}
}
