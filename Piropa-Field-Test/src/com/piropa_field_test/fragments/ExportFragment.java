package com.piropa_field_test.fragments;

import android.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.app.FragmentManager;  
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;  
import android.support.v4.app.FragmentTransaction;  
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.piropa_field_test.Export;
import com.piropa_field_test.R;
import com.piropa_field_test.data.DatabaseHandler;

public class ExportFragment extends Fragment {
	private static ExportFragment instance;
	private View rootView;   
	Button b1, b2, b3;

	public ExportFragment() {
	}

	public static ExportFragment getInstance() {
		if (instance == null) {
			instance = new ExportFragment();
		}
		return instance;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_export, container, false);
		initLayout();
		return rootView;
	}

	private void initLayout() {
		b1 = (Button) rootView.findViewById(R.id.button1);
		b1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				DatabaseHandler handle = new DatabaseHandler(getActivity());
				Export exp = new Export();
				exp.exp(handle, handle.getAllContacts(),0);
				Toast.makeText(
						getActivity(),
						"Werte exportiert",
						Toast.LENGTH_LONG).show();
				getActivity().getFragmentManager().popBackStackImmediate();
			}
		});
		b2 = (Button) rootView.findViewById(R.id.button2);
		b2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				DatabaseHandler handle = new DatabaseHandler(getActivity());
				Export exp = new Export();
				exp.exp(handle, handle.getAllContacts(),1);
				Toast.makeText(
						getActivity(),
						"Werte exportiert",
						Toast.LENGTH_LONG).show();
				getActivity().getFragmentManager().popBackStackImmediate();
			}
		});
		
		b3 = (Button) rootView.findViewById(R.id.button3);
		b3.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				DatabaseHandler handle = new DatabaseHandler(getActivity());
				Export exp = new Export();
				exp.exp(handle, handle.getAllContacts(),2);
				Toast.makeText(
						getActivity(),
						"Werte exportiert",
						Toast.LENGTH_LONG).show();
				getActivity().getFragmentManager().popBackStackImmediate();
			}
		});

	}
}
