package com.piropa_field_test.fragments;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.app.Fragment;
import android.app.ListFragment; 
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.piropa_field_test.DataSave;
import com.piropa_field_test.R;
import com.piropa_field_test.data.DatabaseHandler;
import com.piropa_field_test.services.MeasureService;
import com.piropa_field_test.services.TowerService;

public class MeasureFragment extends ListFragment {

	private static MeasureFragment instance;
	//private DatabaseHandler dataHandle;
	private View rootView;
	static final LatLng HAMBURG = new LatLng(53.558, 9.927);
	static final LatLng KIEL = new LatLng(53.551, 9.993);
	static int modeLac;
	static int modeCid;
	int currentdata=0;
	boolean saveing=false;
	boolean einzelmessung = false;
	boolean wiederholmessung = false;
	
	int counterPos;
	int counterStr;
	int[] strength={0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
	int lac, cid;
	Double[] latitude = {0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0};
	Double[] longitude = {0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0};
	
	int[] _lac2={0,0,0,0,0,0,0,0,0,0};
	int[] _cid2={0,0,0,0,0,0,0,0,0,0};
	int[] _str2={0,0,0,0,0,0,0,0,0,0};
	String[] TowerString ={"","","","","","","","","",""};
	ArrayAdapter<String> adapter;
	
	private GoogleMap map;
	private LatLng myLocation;

	private Button startMeasure;
	private Button singleMeasure;

	private TextView tStrength, tLac, tCid, tLatitude, tLongitude, tTimestamp;

	public static MeasureFragment Instance(int lac, int cid) {
		modeLac=lac;
		modeCid=cid;
		if (instance == null) {
			instance = new MeasureFragment();
		}
		return instance;
	}
	
	public void draw(){
		map.clear();
		DatabaseHandler handle = new DatabaseHandler(getActivity());
		if (modeLac==0 && modeCid==0){
		List<DataSave> data = handle.getAllContacts();
		for (int j=0;j<data.size();j++){
			myLocation = new LatLng(Double.parseDouble(data.get(j).getLatitude()),Double.parseDouble(data.get(j).getLongitude()));
			int col;
			int str=data.get(j).getStrength();
			if (str<-115){
				col = (Color.argb(60, 20, 20, 20));
			} else if (str < -105){
				col = (Color.argb(60, 50, 50, 50));
			} else if (str < -65){
				int red = (40-(str+105))*6;
				int green = ((str+105))*6;
				col = (Color.argb(60, red, green, 0));
			} else {
				col = (Color.argb(60, 0, 255, 0));
			}
			map.addCircle(new CircleOptions()
	        .center(myLocation)
	        .radius(15)
	        .strokeColor(col)
	        .fillColor(col));
		}
		}else {
			List<DataSave> data = handle.getSameCell(modeCid, modeLac);
			for (int j=0;j<data.size();j++){
				myLocation = new LatLng(Double.parseDouble(data.get(j).getLatitude()),Double.parseDouble(data.get(j).getLongitude()));
				int col;
				int str=data.get(j).getStrength();
				if (str<-115){
					col = (Color.argb(60, 20, 20, 20));
				} else if (str < -105){
					col = (Color.argb(60, 50, 50, 50));
				} else if (str < -65){
					int red = (40-(str+105))*6;
					int green = ((str+105))*6;
					col = (Color.argb(60, red, green, 0));
				} else {
					col = (Color.argb(60, 0, 255, 0));
				}
				map.addCircle(new CircleOptions()
		        .center(myLocation)
		        .radius(15)
		        .strokeColor(col)
		        .fillColor(col));
			}
		}
	}
	
	@Override
	public void onDestroy (){
		Intent i = new Intent(getActivity(), MeasureService.class);
		saveing=false;
		einzelmessung=false;
		wiederholmessung = false;
		singleMeasure.setVisibility(View.VISIBLE);
		startMeasure.setText("Wiederholte Messung");
		getActivity().stopService(i);
		super.onDestroy();
	}
	
	@Override
	public void onResume (){
		Intent i = new Intent(getActivity(), MeasureService.class);
		i.putExtra("LAC", modeLac);
		i.putExtra("CID", modeCid);
		for (int j =0; j<40; j++ ){
			latitude[j]=0.0;
			longitude[j]=0.0;
			strength[j] = 0;
		}
		counterPos=0;
		counterStr=0;
		lac=0;
		cid=0;
		getActivity().startService(i);
		super.onResume();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.fragment_measure, container,
					false);
		}
		if (map == null) {
			map = ((MapFragment) getFragmentManager()
					.findFragmentById(R.id.map)).getMap();
		}

		if (map != null) {
			map.setMyLocationEnabled(true);

			Location location = map.getMyLocation();
			
			if (location != null) {
				myLocation = new LatLng(location.getLatitude(),
						location.getLongitude());
				
				map.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation,
						14));
			}

		}
		
		
		
		adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1);
	    setListAdapter(adapter);
	    
	    Intent i = new Intent(getActivity(), MeasureService.class);
		i.putExtra("LAC", modeLac);
		i.putExtra("CID", modeCid);
		for (int j =0; j<40; j++ ){
			latitude[j]=0.0;
			longitude[j]=0.0;
			strength[j] = 0;
		}
		counterPos=0;
		counterStr=0;
		lac=0;
		cid=0;
		getActivity().startService(i);
		draw();
	
		startMeasure = (Button) rootView.findViewById(R.id.buttonStartMeasure);
		startMeasure.setText("Wiederholte Messung");
		singleMeasure = (Button) rootView.findViewById(R.id.buttonEinzelmessung);
		startMeasure.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// use this to start and trigger a service
				if (startMeasure.getText().equals("Wiederholte Messung")) {
					saveing=true;
					einzelmessung=false;
					wiederholmessung = true;
					singleMeasure.setVisibility(View.GONE);
					startMeasure.setText("Messung stoppen");
				} else {
					if (einzelmessung == false) SaveData();
					saveing=false;
					einzelmessung=false;
					wiederholmessung = false;
					singleMeasure.setVisibility(View.VISIBLE);
					startMeasure.setText("Wiederholte Messung");
				}

			}
		});
		
		singleMeasure.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// use this to start and trigger a service
					saveing=false;
					SaveData ();
					saveing=true;
					einzelmessung=true;
					wiederholmessung = false;
					singleMeasure.setVisibility(View.GONE);
					startMeasure.setText("Messung stoppen");
			}
		});
		

		tStrength = (TextView) rootView.findViewById(R.id.textView_strength);
		tLac = (TextView) rootView.findViewById(R.id.textView_lac);
		tCid = (TextView) rootView.findViewById(R.id.textView_cid);
		tLatitude = (TextView) rootView.findViewById(R.id.textView_degree);
		tLongitude = (TextView) rootView.findViewById(R.id.textView_minute);
		tTimestamp = (TextView) rootView.findViewById(R.id.textView_second);

		return rootView;
	}

	private void SaveData (){
		counterPos=0;
		counterStr=0;
		int count1=0;
		int count2=0;
		int str=0;
		if (saveing){
			double lat=0.0,lng=0.0;
			for (int j =0; j<40; j++ ){
				if (latitude[j]!=0.0 && longitude[j]!=0.0){
					count1++;
					lat=lat+latitude[j];
					lng=lng+longitude[j];
				}
				if (strength[j]!=0){
					count2++;
					str=str+strength[j];
				}
				strength[j]=0;
				latitude[j]=0.0;
				longitude[j]=0.0;
			}
			Log.v("data","" + count1);
			if (count1!=0)lat=lat/count1;
			if (count1!=0)lng=lng/count1;
			if (count2!=0)str=str/count2;
			lat = Math.round(lat*10000000)/10000000.0;
			lng = Math.round(lng*10000000)/10000000.0;
			DatabaseHandler handle = new DatabaseHandler(getActivity());
			if(str < 0 && lac != 0 && cid != 0 && lat>0 && lng> 0 && count1 >= 20 && count2 >= 25){
				Log.v("data","" + "saved");
				DataSave data = new DataSave(str,lat+"",lng + "",lac,cid);
				handle.addTestPunkt(data);
				Toast.makeText(
					getActivity(),
					"Werte werden gespeichert",
					Toast.LENGTH_SHORT).show();
				myLocation = new LatLng(lat,lng);
				int col;
				if (str<-115){
					col = (Color.argb(60, 20, 20, 20));
				} else if (str < -105){
					col = (Color.argb(60, 50, 50, 50));
				} else if (str < -65){
					int red = (40-(str+105))*6;
					int green = ((str+105))*6;
					col = (Color.argb(60, red, green, 0));
				} else {
					col = (Color.argb(60, 0, 255, 0));
				}
				map.addCircle(new CircleOptions()
		        .center(myLocation)
		        .radius(15)
		        .strokeColor(col)
		        .fillColor(col));
			}
			
		}
		lac=0;
		cid=0;
		if (einzelmessung == true){
			saveing=false;
			einzelmessung=false;
			wiederholmessung = false;
			singleMeasure.setVisibility(View.VISIBLE);
			startMeasure.setText("Wiederholte Messung");
		}
	}
	
	@Override
	  public void onListItemClick(ListView l, View v, int position, long id) {
		SaveData();
		saveing=false;
		einzelmessung=false;
		wiederholmessung = false;
		singleMeasure.setVisibility(View.VISIBLE);
		startMeasure.setText("Wiederholte Messung");
		modeLac=_lac2[position];
		modeCid=_cid2[position];
		draw();
		
      Intent i = new Intent(getActivity(), MeasureService.class);
		getActivity().stopService(i);
		i.putExtra("LAC", modeLac);
		i.putExtra("CID", modeCid);
		for (int j =0; j<40; j++ ){
			latitude[j]=0.0;
			longitude[j]=0.0;
			strength[j] = 0;
		}
		counterPos=0;
		counterStr=0;
		lac=0;
		cid=0;
		getActivity().startService(i);
		
	  }
	
	private BroadcastReceiver br = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle bundle = intent.getExtras();
			if (bundle != null) {
				
				//Liste::
				boolean change=false;
				/*
				  for (int i =0;i<10;i++){
					  if(_lac2[i]!=intent.getExtras().getIntArray(MeasureService.LAC2)[i]
							  && _cid2[i]!=intent.getExtras().getIntArray(MeasureService.CID2)[i] 
							  && _str2[i]!=intent.getExtras().getIntArray(MeasureService.STR2)[i])
					  {
						  Log.v("test", "ist drin");
						  adapter.clear();
						  change=true;
					  }
					  
				  }
				  */
				 // if (change) {
					_lac2 = intent.getExtras().getIntArray(MeasureService.LAC2);
					_cid2 = intent.getExtras().getIntArray(MeasureService.CID2);
					_str2 = intent.getExtras().getIntArray(MeasureService.STR2);
					Log.v("tower", "Lac: " + _lac2[0] + "\r Cid: "
									+ _cid2[0] + "\r Stärke: " + _str2[0]);
					for (int i = 0; i < 10; i++) {
						if (_lac2[i] != 0 && _cid2[i] != 0 && _str2[i] != 0) {
							TowerString[i] = "Lac: " + _lac2[i] + "\r Cid: "
									+ _cid2[i] + "\r Stärke: " + _str2[i];
							change=true;
						}

				//	}
					if(change){
						adapter.clear();
						adapter.addAll(TowerString);
					}
				}
				
				  
			
			// Daten::
			
				String  _timestamp;
				int _strength, _lac, _cid, _current;
				Double  _latitude, _longitude;
				
				_strength = intent.getExtras().getInt(MeasureService.STRENGTH, 0);
				_lac = intent.getExtras().getInt(MeasureService.LAC, 2);
				_cid = intent.getExtras().getInt(MeasureService.CID, 2);
				_latitude = intent.getExtras().getDouble(MeasureService.LATITUDE, 2);
				_longitude = intent.getExtras().getDouble(MeasureService.LONGITUDE, 2);
				_timestamp = intent.getExtras().getString(MeasureService.TIMESTAMP, "2");
				_current = intent.getExtras().getInt(MeasureService.COUNT, 0);
				tStrength.setText(_strength + "");
				tLac.setText(" " +_lac);
				tCid.setText(" " +_cid);
				tLatitude.setText(" " + _latitude);
				tLongitude.setText(" " + _longitude);
				Date date = new Date(Long.parseLong(_timestamp));
				if(_strength != 0 && _lac != 0 && _cid != 0 && _latitude > 0 && _longitude > 0 && currentdata != _current){
					currentdata=_current;
					strength[counterStr] = _strength;
					latitude[counterPos]=_latitude;
					longitude[counterPos]=_longitude;
					counterPos++;
					counterStr++;
					if (counterPos>=40||counterStr>=40){
						SaveData();
						
					}
					if (((lac!=0 && lac!=_lac) || (cid!=0 && cid!=_cid))&&saveing==true){
						SaveData();
						Toast.makeText(
								getActivity(),
								"Messstation nicht erreicht!",
								Toast.LENGTH_SHORT).show();
						saveing=false;
						einzelmessung=false;
						wiederholmessung = false;
						singleMeasure.setVisibility(View.VISIBLE);
						startMeasure.setText("Wiederholte Messung");
						
					}
					lac=_lac;
					cid=_cid;

					
				}
				SimpleDateFormat ft = new SimpleDateFormat(
						"yyyy.MM.dd 'at' HH:mm:ss");
				tTimestamp.setText(" " + ft.format(date));
			//	DataSave data= new DataSave(Integer.parseInt(_strength), _latitude, _longitude, Integer.parseInt(_lac), Integer.parseInt(_cid));
				
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
		// create filter for BroadcastReceiver
		if (wiederholmessung == false){
			saveing=false;
			einzelmessung=false;
			wiederholmessung = false;
			singleMeasure.setVisibility(View.VISIBLE);
			startMeasure.setText("Wiederholte Messung");
		getActivity().unregisterReceiver(br);
		Intent i = new Intent(getActivity(), MeasureService.class);
		getActivity().stopService(i);
		}
	}
	
}


