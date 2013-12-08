package com.uniteitsolution.chatup;

import android.app.Activity;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
//import com.google.android.gms.maps.*;

//import com.uniteitsolution.chatup.MenuManagement;

public class LocationAppActivity extends Activity implements LocationListener{

	public static SharedPreferences preferences;
	public static final String PREFS_ACCOUNT = "account";
	private LocationManager locManager;
	
	//private MapView maps;
	//private GridView roomLists;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_room);
		this.setTitle(R.string.page_room_title);
		locManager = (LocationManager)getSystemService(LOCATION_SERVICE);
		
		Log.d("Loguser","Location Create");
		//maps = (MapView)findViewById(R.id.map);
	
		//roomLists = (GridView)findViewById(R.id.roomList);
		
	}
	
	
	
	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}



	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.d("Loguser","OnResume ");
		this.locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1500, 1, this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onResume();
		//this.locManager.removeUpdates(this);
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
	
}
