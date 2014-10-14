package com.piropa_field_test.fragments;

import java.util.ArrayList;  
import java.util.List;  

import android.app.Fragment;
import android.app.ListFragment;  
import android.content.res.Configuration;  
import android.os.Bundle;  
//import android.support.v4.app.Fragment;  
import android.support.v4.app.FragmentManager;  
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;  
import android.support.v4.app.FragmentTransaction;  
import android.util.Log;
import android.view.LayoutInflater;  
import android.view.View;  
import android.view.ViewGroup;  
import android.widget.AdapterView;  
import android.widget.AdapterView.OnItemClickListener;  
import android.widget.ArrayAdapter;  
import android.widget.Toast;  
import android.os.Handler;
import android.view.animation.AnimationUtils;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.piropa_field_test.DataSave;
import com.piropa_field_test.R;
import com.piropa_field_test.data.DatabaseHandler;
   
public class LogFragment extends ListFragment {  
	private static LogFragment instance;
	//private DatabaseHandler dataHandle;
	
	private View rootView;  
	private ArrayAdapter<String> adapter;
	private int[][]   laccid = new int[3][20];
    private List<String> mDataSourceList = new ArrayList<String>(); 

   
    public static LogFragment getInstance() {
		if (instance == null) {
			instance = new LogFragment();
		}
		return instance;
	}
   
    @Override 
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  
            Bundle savedInstanceState) {  
    	if (rootView == null) {
			rootView = inflater.inflate(R.layout.fragment_log, container,
					false);
		}
		return rootView; 
    }  
    
    @Override
	  public void onActivityCreated(Bundle savedInstanceState) {
	    super.onActivityCreated(savedInstanceState);
	    
	    
	    DatabaseHandler handle = new DatabaseHandler(getActivity());
	    String[] values={"","","","","","","","","","","","","","","","","","","",""};
	    int size = handle.getAllContacts().size();
	    if (size==0){
	    	
	    } else if (size ==1){
	    	laccid[0][0]=handle.getAllContacts().get(0).getLac();
	 	    laccid[1][0]=handle.getAllContacts().get(0).getCid();
	 	    laccid[2][0]=1;
	 	   values[0]="Lac: " + laccid[0][0] + ", cid: " + laccid[1][0]  + ", Messungen: " + laccid[2][0];
	    } else {
	    Log.v("asd2", "" + size);
	    boolean newrow= true;
	    int rows=1;
	    laccid[0][0]=handle.getAllContacts().get(0).getLac();
	    laccid[1][0]=handle.getAllContacts().get(0).getCid();
	    laccid[2][0]=1;
	    for(int i=1;i<size;i++){
	    	if(rows<20){
	    		DataSave value = handle.getAllContacts().get(i);
	    		newrow=true;
	    		for (int j = 0; j<rows;j++){
	    			if (value.getLac()==laccid[0][j] && value.getCid()==laccid[1][j]){
	    				newrow=false;
	    				laccid[2][j]++;
	    				break;
	    			}
	    		}
	    		if(newrow){
	    			laccid[0][rows]=value.getLac();
	    			laccid[1][rows]=value.getCid();
	    			laccid[2][rows]=1;
	    			rows++;
	    		}
	    	}
	    }
	    for(int i=0;i<rows;i++){
	    	values[i]="Lac: " + laccid[0][i] + ", cid: " + laccid[1][i]  + ", Messungen: " + laccid[2][i];
	    }
	    }
	    adapter = new ArrayAdapter<String>(getActivity(),
	        android.R.layout.simple_list_item_1, values);
	    setListAdapter(adapter);
	
	  }
   
    
    @Override
	  public void onListItemClick(ListView l, View v, int position, long id) {
    	if (adapter.getItem(position)!="" && laccid[0][position]!=0 && laccid[1][position]!=0){
    		getActivity().getFragmentManager().beginTransaction()
			.replace(R.id.container, MeasureFragment.Instance(laccid[0][position],laccid[1][position]))
			.addToBackStack(null).commit();
    	}
    }
}  