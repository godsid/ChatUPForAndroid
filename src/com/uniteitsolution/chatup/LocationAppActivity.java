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
//import com.google.android.gms.maps.*;

//import com.uniteitsolution.chatup.MenuManagement;

public class LocationAppActivity extends Activity {

	public static SharedPreferences preferences;
	public static final String PREFS_ACCOUNT = "account";
	private Location location;
	private LocationManager locManager;
	private LocationListener locListener;
	private static String location_lat;
	private static String location_lng;
	private static String location_name;
	private static int location_time;
	
	static final int getLocationTimeout = 5000;
	
	
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
		this.location = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		
		
		locListener = new LocationListener() {
			
			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
				// TODO Auto-generated method stub
				Log.d("Loguser","onStatusChanged");
				Toast.makeText(getBaseContext(),"onStatusChanged",Toast.LENGTH_SHORT).show();
			}
			
			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub
				Log.d("Loguser","onProviderEnabled");
				Toast.makeText(getBaseContext(),"onProviderEnabled",Toast.LENGTH_SHORT).show();
			}
			
			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub
				Log.d("Loguser","onProviderDisabled");
				Toast.makeText(getBaseContext(),"onProviderDisabled",Toast.LENGTH_SHORT).show();
			}
			
			@Override
			public void onLocationChanged(Location locationUpdate) {
				// TODO Auto-generated method stub
				Log.d("Loguser","onLocationChanged");
				Toast.makeText(getBaseContext(),"onLocationChanged",Toast.LENGTH_SHORT).show();
				location = locationUpdate;
				if(location!=null){
					setLocationSharedPreferences(location);
					Toast.makeText(getBaseContext(),
	                        "New location latitude [" + 
	                        location.getLatitude() +
	                        "] longitude [" + 
	                        location.getLongitude()+"]",
	                        Toast.LENGTH_SHORT).show();
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
		Log.d("Loguser","Location OnResume ");
		Toast.makeText(getBaseContext(),"onResume",Toast.LENGTH_SHORT).show();
		
		Log.d("Loguser",String.valueOf(locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)));
		
		if(this.location==null){
			Log.d("Loguser","getLastKnownLocation is null");
			Toast.makeText(getBaseContext(),"getLastKnownLocation is null",Toast.LENGTH_SHORT).show();
		}else{
			this.setLocationSharedPreferences(this.location);
			Toast.makeText(getBaseContext(),"This Location "+location.getLatitude() ,Toast.LENGTH_SHORT).show();
		}
		
		if (!locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			new AlertDialog.Builder(this)
			.setTitle(R.string.alert_location_title)
			.setMessage(R.string.alert_location_message)
			.setPositiveButton(R.string.alert_location_yes,new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					Log.d("Loguser","Enable Setting");
					Intent intentSetting = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
					startActivity(intentSetting);
				}
			})
			.setNegativeButton(R.string.alert_location_no,null)
			.create()
			.show();
		}else{
			this.locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1500, 1, this.locListener);
			Log.d("Loguser","Waitting Location detect");
			Timer autoUpdate = new Timer();
			autoUpdate.schedule(new TimerTask() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					Log.d("Loguser","Waitting Timeout");
					locManager.removeUpdates(locListener);
					finish();
				}
			},getLocationTimeout);
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
		}
		return super.onOptionsItemSelected(item);
	}
	/*
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
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
	*/
	
	private void setLocationSharedPreferences(Location location){
		Log.d("Loguser","zzzzzz"+String.valueOf(location.getTime()));
		this.getSharedPreferences(PREFS_ACCOUNT, MODE_PRIVATE)
		.edit()
		.putString("location_lat", String.valueOf(location.getLatitude()))
		.putString("location_lng", String.valueOf(location.getLongitude()))
		.putString("location_time", String.valueOf(Calendar.getInstance().getTimeInMillis()))
		.commit();
	}
}
