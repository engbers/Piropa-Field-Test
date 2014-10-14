package com.piropa_field_test.services;

import java.util.Date;
import java.util.List;

import com.piropa_field_test.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.telephony.CellInfo;
import android.telephony.NeighboringCellInfo;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.model.LatLng;

public class TowerService extends Service{
	
	public static final String NOTIFICATION = "resultFromService";
	public static final String STR = "str";
	public static final String LAC = "lac";
	public static final String CID = "cid";

	@Override
	 public IBinder onBind(Intent intent) {
	  throw new UnsupportedOperationException("Not yet implemented");
	 }
	 @Override
	 public void onCreate() {
	 }

	  @Override
	 public void onStart(Intent intent2, int startId) {
	  // Perform your long running operations here.
		  int[] _lac={0,0,0,0,0,0};
		  int[] _cid={0,0,0,0,0,0};
		  int[] _str={0,0,0,0,0,0};
		  TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
			
				List<NeighboringCellInfo> neighborCells = telephonyManager.getNeighboringCellInfo();
				//List<CellInfo> neighborCells2 = telephonyManager.getAllCellInfo();
			    if (neighborCells.size()==0) {
			    	Log.e("mySignal", "Keine Benachbarten Zellen");
			    } else {
			    	for (int i = 0; i < neighborCells.size();i++){
			    		if (i<neighborCells.size()){
			    			_lac[i]=neighborCells.get(i).getLac();
			    			_cid[i]=neighborCells.get(i).getCid();
			    			int asu = neighborCells.get(i).getRssi();
			    			if (asu != 99) {
			    				_str[i] = -113 + (2 * asu);
			    	        } else {
			    	        	_str[i] =  0;
			    	        }
			    			
			    		}
			    	}
			    }
			
		  
		  
		  
		  Intent intent = new Intent(NOTIFICATION);
		  intent.putExtra(STR, _str);
		  intent.putExtra(LAC, _lac);
		  intent.putExtra(CID, _cid);

		    sendBroadcast(intent);
	 }

}
