package com.uniteitsolution.chatup;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class LocationAppActivity extends Activity {

	public static SharedPreferences preferences;
	public static final String PREFS_ACCOUNT = "account";
	private Location location;
	private LocationManager locManager;
	private LocationListener locListener;
	private static String locationLat;
	private static String locationLng;
	private static String locationName;
	private static int locationTime;
	
	static private int locationRetryCount = 3;
	static final private int maxLocationRetryCount = 3;
	
	static final int getLocationTimeout = 5000;
	static final long getLocationMinTime = 10000;
	static final int getLocationMinDistance = 10;
	static private String provider = LocationManager.GPS_PROVIDER;
	
	//private MapView maps;
	//private GridView roomLists;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_location);
		this.setTitle(R.string.page_room_title);
		
		Toast.makeText(getBaseContext(),"onCreate",Toast.LENGTH_SHORT).show();
		
		this.locManager = (LocationManager)getSystemService(LOCATION_SERVICE);
		this.location = locManager.getLastKnownLocation(this.provider);
		
		locListener = new LocationListener() {
			
			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void onLocationChanged(Location locationUpdate) {
				// TODO Auto-generated method stub
				location = locationUpdate;
				if(location!=null){
					
					Toast.makeText(getBaseContext(),
	                        "New location latitude [" + 
	                        location.getLatitude() +
	                        "] longitude [" + 
	                        location.getLongitude()+"]",
	                        Toast.LENGTH_SHORT).show();
					locationLat = String.valueOf(location.getLatitude());
					locationLat = String.valueOf(location.getLongitude());
					
					Intent intent = new Intent();
					intent.putExtra("locationLat", locationLat)
					.putExtra("locationLng", locationLng);
					setResult(RESULT_OK,intent);
					finish();
				}
			}
		};
		
		//maps = (MapView)findViewById(R.id.map);
		//roomLists = (GridView)findViewById(R.id.roomList);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Toast.makeText(getBaseContext(),"onResume",Toast.LENGTH_SHORT).show();
		
		/*
		if(this.location==null){
			Toast.makeText(getBaseContext(),"getLastKnownLocation is null",Toast.LENGTH_SHORT).show();
		}else{
			Toast.makeText(getBaseContext(),"This Location "+location.getLatitude() ,Toast.LENGTH_SHORT).show();
		}*/
		
		if (!locManager.isProviderEnabled(this.provider)) {
			new AlertDialog.Builder(this)
			.setTitle(R.string.alert_location_title)
			.setMessage(R.string.alert_location_message)
			.setPositiveButton(R.string.alert_location_yes,new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					Intent intentSetting = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
					startActivity(intentSetting);
				}
			})
			.setNegativeButton(R.string.alert_location_no,null)
			.create()
			.show();
		}else{
			this.locManager.requestLocationUpdates(this.provider, getLocationMinTime, getLocationMinDistance, this.locListener);
			getLocation();
		}
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Toast.makeText(getBaseContext(),"onPause",Toast.LENGTH_SHORT).show();
		this.locManager.removeUpdates(this.locListener);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		//new MenuManagement(item);
		switch(item.getItemId()){
		case R.id.menu_logout:
			this.getSharedPreferences(PREFS_ACCOUNT, MODE_PRIVATE).edit().remove("username").commit();
			finish();
			break;
		case R.id.menu_exit:
			new AlertDialog.Builder(this)
			.setTitle(R.string.alert_exit_title)
			//.setMessage(R.string.alert_exit_message)
			.setPositiveButton(R.string.alert_exit_button_yes,new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					setResult(RESULT_OK, new Intent().putExtra("exit", true));
					finish();
				}
			})
			.setNegativeButton(R.string.alert_exit_button_no,null)
			.show();
		}
		return super.onOptionsItemSelected(item);
	}

	private void changeLocationProvider(){
		this.locationRetryCount = this.maxLocationRetryCount;
		if(this.provider==LocationManager.GPS_PROVIDER){
			this.provider = LocationManager.NETWORK_PROVIDER;
			Log.d("Loguser","Chane Provider to Network");
		}else{
			this.provider = LocationManager.GPS_PROVIDER;
			Log.d("Loguser","Chane Provider to GPS");
		}
		this.location = locManager.getLastKnownLocation(this.provider);
		
	}
	private void getLocation(){
		Log.d("Loguser","Waitting Timeout");
		
		if(--this.locationRetryCount<=0){
			changeLocationProvider();
		}
		
		if(location==null){
			Timer autoUpdate = new Timer();
			autoUpdate.schedule(new TimerTask() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					getLocation();
				}
			},getLocationTimeout);
		}else{
			locationLat = String.valueOf(location.getLatitude());
			locationLng = String.valueOf(location.getLongitude());
			
			Intent intent = new Intent();
			intent.putExtra("locationLat", locationLat)
			.putExtra("locationLng", locationLng)
			.putExtra("locationTime", Calendar.getInstance().getTimeInMillis());
			setResult(RESULT_OK,intent);
			locManager.removeUpdates(locListener);
			finish();
		}
	}
}
