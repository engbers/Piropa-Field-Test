package com.piropa_field_test.services;

import java.util.Date;
import java.util.List;

import com.piropa_field_test.MainActivity;
import com.piropa_field_test.R;
import com.piropa_field_test.fragments.MeasureFragment;

import android.app.AlarmManager;
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
import com.google.android.gms.internal.br;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.model.LatLng;

public class MeasureService extends Service implements LocationListener,
		ConnectionCallbacks, OnConnectionFailedListener {
	public static final String NOTIFICATION = "resultFromService";
	public static final String STRENGTH = "strength";
	public static final String LAC = "lac";
	public static final String CID = "cid";
	public static final String LAC2 = "lac2";
	public static final String CID2 = "cid2";
	public static final String STR2 = "str2";
	public static final String LATITUDE = "latitude";
	public static final String LONGITUDE = "longitude";
	public static final String TIMESTAMP = "timestamp";
	public static final String COUNT = "count";
	
	private boolean measureTower;
	private int modeLac;
	private int modeCid;
	int count=0;

	private boolean serviceWorks;
	private final Handler handler = new Handler();
	private Context myContext;

	private LocationManager locationManager;
	private NotificationManager notificationManager;

	private LatLng myPosition;
	private long timestamp;
	private int myStrength = 0;

	private LocationClient locationClient;

	private static final LocationRequest REQUEST = LocationRequest.create()
			.setInterval(1000).setFastestInterval(16)
			.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		myContext = getApplicationContext();

		serviceWorks = true;
		myPosition = new LatLng(0, 0);
		timestamp = new Date().getTime();

		setupLocationClientIfNeeded();
		locationClient.connect();
		
	
		
		modeLac = intent.getExtras().getInt("LAC");
		modeCid = intent.getExtras().getInt("CID");
		Log.e("mySignal", "" + modeLac);
		if (modeLac!=0 && modeCid!=0) measureTower=true;
		//Intent in = new Intent();
		Intent intent2 = new Intent(this, MainActivity.class);
		intent2.setAction("OPEN_TAB_1");
		PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent2, 0);
		notificationManager =(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
			.setSmallIcon(R.drawable.ic_stat_notification)
		    .setContentTitle("Messen")
		    .setContentText("Piropa Test Messung aktiv")
		    .setContentIntent(pIntent)
			.setOngoing(true)
			.setAutoCancel(false);
		//	PendingIntent pendingIntent = /* your intent */;
		//	notification.setLatestEventInfo(this, /* your content */, pendingIntent);
			//notificationManager.notify();
		notificationManager.notify(1, mBuilder.build());
		
		MyPhoneStateListener myListener = new MyPhoneStateListener();
		TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

		telephonyManager.listen(myListener,
				PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);

		locationManager = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);

		boolean netWorkEnabled = locationManager
				.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		boolean gpsEnabled = locationManager
				.isProviderEnabled(LocationManager.GPS_PROVIDER);

		if (!(netWorkEnabled && gpsEnabled)) {
			handler.post(new Runnable() {

				@Override
				public void run() {
					Toast.makeText(
							myContext,
							"GPS oder Netwerk-Provider ist nicht eingeschaltet",
							Toast.LENGTH_LONG).show();
				}
			});
		} else {
			handler.postDelayed(runnable, 1000);
		}
		return Service.START_NOT_STICKY;
	}

	private void setupLocationClientIfNeeded() {
		if (locationClient == null) {
			locationClient = new LocationClient(myContext, this, this);
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onDestroy() {
		serviceWorks = false;
		notificationManager.cancelAll();
		if (locationClient != null) {
			locationClient.disconnect();
		}

	}


	
	private Runnable runnable = new Runnable() {


			@Override
			public void run() {
				if (serviceWorks){
					 int[] _lac2={0,0,0,0,0,0,0,0,0,0};
					  int[] _cid2={0,0,0,0,0,0,0,0,0,0};
					  int[] _str2={0,0,0,0,0,0,0,0,0,0};
				int mySignal=0;
					// retrieve a reference to an instance of TelephonyManager
					TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
					GsmCellLocation cellLocation = (GsmCellLocation) telephonyManager
							.getCellLocation();
					
				    if (measureTower==true)	cellLocation.setLacAndCid(modeLac, modeCid);
				    int cid = cellLocation.getCid();
					int lac = cellLocation.getLac();
					List<NeighboringCellInfo> neighborCells = telephonyManager.getNeighboringCellInfo();
					if (neighborCells.size()==0) {
				    	Log.e("mySignal", "Keine Benachbarten Zellen");
				    } else {
				    	for (int i = 0; i < neighborCells.size();i++){
				    		if (i<10){
				    			_lac2[i]=neighborCells.get(i).getLac();
				    			_cid2[i]=neighborCells.get(i).getCid();
				    			int asu = neighborCells.get(i).getRssi();
				    			if (asu != 99) {
				    				_str2[i] = -113 + (2 * asu);
				    	        } else {
				    	        	_str2[i] =  0;
				    	        }
				    			
				    		}
				    	}
				    }
					if (measureTower==true){
						//List<CellInfo> neighborCells2 = telephonyManager.getAllCellInfo();
					    if (neighborCells.size()==0) {
					    	mySignal=0;
					    	Log.e("mySignal", "Keine Benachbarten Zellen");
					    } else {
					    	mySignal=0;
					    	for (int i = 0; i < neighborCells.size();i++){
					    		if (neighborCells.get(i).getCid()==cid && neighborCells.get(i).getLac()==lac){
					    			
					    			int asu = neighborCells.get(i).getRssi();
					    			if (asu != 99) {
					    				mySignal = -113 + (2 * asu);
					    	        } else {
					    	        	mySignal =  0;
					    	        }
					    			
					    		}
					    	}
					    	if (mySignal == 0 ) Log.e("mySignal", "Zelle nicht gefunden");
					    }
					}
					
				    
				    
				    
					Intent intent = new Intent(NOTIFICATION);
					if (measureTower==true){
						intent.putExtra(STRENGTH, mySignal);
					} else {
						intent.putExtra(STRENGTH, myStrength);
					}
					intent.putExtra(LAC, lac);
					intent.putExtra(CID, cid);
					intent.putExtra(LATITUDE, myPosition.latitude);
					intent.putExtra(LONGITUDE, myPosition.longitude);
					intent.putExtra(TIMESTAMP, timestamp + "");
					intent.putExtra(STR2, _str2);
					intent.putExtra(LAC2, _lac2);
					intent.putExtra(CID2, _cid2);
					count++;
					intent.putExtra(COUNT, count);
					Log.e("mySignal", "sent: " + count);
					sendBroadcast(intent);
				//	intent.wait(1000);

				//	endingTime = System.currentTimeMillis() + 1500;
			     //   remainingTime = 1500;
			     //   while (remainingTime > 0) {
			     //       try {
			     //       	Thread.sleep(remainingTime);
			    //        } catch (InterruptedException ignore) {
			    //        }
			    //        remainingTime = endingTime - System.currentTimeMillis();
			    //    }
					handler.postDelayed(this, 400);
			}
			}
	};


	@Override
	public void onConnectionFailed(ConnectionResult result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onConnected(Bundle connectionHint) {
		locationClient.requestLocationUpdates(REQUEST, this);
	}

	@Override
	public void onLocationChanged(Location location) {
		myPosition = new LatLng(location.getLatitude(), location.getLongitude());
		timestamp = new Date().getTime();
	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub

	}

	private class MyPhoneStateListener extends PhoneStateListener {
		/*
		 * Get the Signal strength from the provider, each time there is an
		 * update
		 */
		@Override
		public void onSignalStrengthsChanged(final SignalStrength signalStrength) {
			super.onSignalStrengthsChanged(signalStrength);
			int asu = signalStrength.getGsmSignalStrength();
			if (asu != 99) {
				int dbm = -113 + (2 * asu);
				myStrength = dbm;
	        } else {
	        	myStrength =  0;
	        }
		}

	};
}
