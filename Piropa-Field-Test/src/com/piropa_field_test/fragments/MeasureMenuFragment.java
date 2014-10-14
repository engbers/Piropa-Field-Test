package com.piropa_field_test.fragments;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Fragment;
import android.app.ListFragment; 
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.FragmentManager;  
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;  
import android.support.v4.app.FragmentTransaction;  
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.piropa_field_test.R;
import com.piropa_field_test.data.DatabaseHandler;
import com.piropa_field_test.services.MeasureService;
import com.piropa_field_test.services.TowerService;

public class MeasureMenuFragment extends ListFragment {
	int[] _lac={0,0,0,0,0,0};
	int[] _cid={0,0,0,0,0,0};
	private static MeasureMenuFragment instance;
	ArrayAdapter<String> adapter;
	private View rootView; 
	String[] TowerString ={"","","","","",""};
	Button b1, b2;

	public MeasureMenuFragment() {
	}

	public static MeasureMenuFragment getInstance() {
		if (instance == null) {
			instance = new MeasureMenuFragment();
		}
		return instance;
	}
	
	//@Override
	//public void onDestroyView (){
	//	Intent i = new Intent(getActivity(), TowerService.class);
	//    getActivity().stopService(i);
	//}
	
	@Override
	public void onDestroyView(){
		Intent i = new Intent(getActivity(), TowerService.class);
		getActivity().stopService(i);
		super.onDestroyView();
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_measure_menu, container, false);
		initLayout();
		return rootView;
	}

	private void initLayout() {
		
		
	    adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1);
	    setListAdapter(adapter);
	    
	    Intent i = new Intent(getActivity(), TowerService.class);
	    getActivity().startService(i);
	    
		b1 = (Button) rootView.findViewById(R.id.button1);
		b1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(getActivity(), TowerService.class);
				getActivity().stopService(i);
				getActivity().getFragmentManager().beginTransaction()
						.replace(R.id.container, MeasureFragment.Instance(0,0))
						.addToBackStack(null).commit();
			}
		});
		b2 = (Button) rootView.findViewById(R.id.button2);
		b2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(getActivity(), TowerService.class);
				getActivity().stopService(i);
				EditText et1 = (EditText)rootView.findViewById(R.id.editText1);
				EditText et2 = (EditText)rootView.findViewById(R.id.editText2);
				if ( et1.getText().toString().isEmpty()==false && et2.getText().toString().isEmpty()==false){
					int _lac = Integer.parseInt(et1.getText().toString());
					int _cid = Integer.parseInt(et2.getText().toString());
					getActivity().getFragmentManager().beginTransaction()
						.replace(R.id.container, MeasureFragment.Instance(_lac,_cid))
						.addToBackStack(null).commit();
				}
				
			}
		});

	}
	
	@Override
	  public void onListItemClick(ListView l, View v, int position, long id) {
		EditText et1 = (EditText)rootView.findViewById(R.id.editText1);
		EditText et2 = (EditText)rootView.findViewById(R.id.editText2);
		et1.setText("" + _lac[position]);
		et2.setText("" + _cid[position]);
		Object o = l.getItemAtPosition(position);
        String str=(String)o;//As you are using Default String Adapter
        Toast.makeText(getActivity(),str,Toast.LENGTH_SHORT).show();
	  }
	
	private BroadcastReceiver br = new BroadcastReceiver() {

	    @Override
	    public void onReceive(Context context, Intent intent) {
	      Bundle bundle = intent.getExtras();
	      if (bundle != null) {
	    	  boolean change=false;
	    	  int[] _str={0,0,0,0,0,0};
	    	  
		      _lac = intent.getExtras().getIntArray(TowerService.LAC);
			  _cid = intent.getExtras().getIntArray(TowerService.CID);
			  _str = intent.getExtras().getIntArray(TowerService.STR);
			  
	        
			  for (int i =0;i<6;i++){
				  if(_lac[i]!=0 && _cid[i]!=0 && _str[i]!=0){
					  TowerString[i] = "Lac: " + _lac[i] + ", Cid: " + _cid[i] + ", Stärke: " + _str[i];
					  change=true;
				  }
				  
			  }
			  if (change==true){
				  adapter.addAll(TowerString);
				change=false;
			  }
	      }
	    }
	  };
	  
	  @Override
		public void onStart() {
			super.onStart();
			// create filter for BroadcastReceiver
			IntentFilter intFilt = new IntentFilter(MeasureService.NOTIFICATION);
			// enable BroadcastReceiver
			getActivity().registerReceiver(br, intFilt);
		}
	  
	  @Override
		public void onStop() {
			super.onStop();
			getActivity().unregisterReceiver(br);
		}
	
}
