package com.uniteitsolution.chatup;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.facebook.Session;


public class MainActivity extends Activity {	
	public static SharedPreferences preferences;
	public static final String PREFS_ACCOUNT = "account";
	public static String name, username, avatar, locationLat, locationLng, locationName;
	public static long locationTime;
	private static final int refreshLocationTime = 10000;
	private static final int REQUEST_LOCATION = 1;
	private static final int REQUEST_LOGIN = 2;
	private static final int REQUEST_LOGOUT = 3;
	private static final int REQUEST_CHAT = 4;
	private static final int REQUEST_ROOM = 5;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Log.d("Loguser","onCreate");
		getUserShardPreferance();
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.d("Loguser","onStart");
	}
	
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
				finish();
			}
		})
		.setNegativeButton(R.string.alert_exit_button_no,null)
		.show();	
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu);
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.d("Loguser","onResume");
		
		this.checkLogin();
		this.checkLocation();
		this.startChat();
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		Log.d("Loguser",String.valueOf(requestCode));
		if(resultCode==RESULT_OK){
			switch(requestCode){
				case REQUEST_LOGIN:
					Log.d("Loguser","REQUEST_LOGIN");
					name = setUserShardPreferance("name",data.getStringExtra("name"));
					username = setUserShardPreferance("username",data.getStringExtra("username"));
					avatar = setUserShardPreferance("avatar",data.getStringExtra("avatar"));
				break;
				case REQUEST_LOCATION:
					locationLat = setUserShardPreferance("locationLat",data.getStringExtra("locationLat"));
					locationLng = setUserShardPreferance("locationLng",data.getStringExtra("locationLng"));
					locationName = setUserShardPreferance("locationName",data.getStringExtra("locationName"));
					locationTime = setUserShardPreferance("locationTime",data.getLongExtra("locationTime",0));
					break;
			}
		}
		
		if(resultCode==RESULT_OK){
			if(data.getBooleanExtra("exit",false)==true){
				finish();
			}
			if(data.getBooleanExtra("logout",false)==true){
				logout();
			}
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		//new MenuManagement(item);
		switch(item.getItemId()){
		case R.id.menu_logout:
			logout();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void getUserShardPreferance(){
		name = this.getSharedPreferences(PREFS_ACCOUNT,MODE_PRIVATE).getString("name", "");
		username = this.getSharedPreferences(PREFS_ACCOUNT,MODE_PRIVATE).getString("username", "");
		avatar = this.getSharedPreferences(PREFS_ACCOUNT,MODE_PRIVATE).getString("avatar", "");
		locationLat = this.getSharedPreferences(PREFS_ACCOUNT,MODE_PRIVATE).getString("locationLat", "");
		locationLng = this.getSharedPreferences(PREFS_ACCOUNT,MODE_PRIVATE).getString("locationLng", "");
		locationName = this.getSharedPreferences(PREFS_ACCOUNT,MODE_PRIVATE).getString("locationName", "");
		locationTime = this.getSharedPreferences(PREFS_ACCOUNT,MODE_PRIVATE).getLong("locationTime", 0);
	}
	private String setUserShardPreferance(String key, String value){
		this.getSharedPreferences(PREFS_ACCOUNT,MODE_PRIVATE)
		.edit()
		.putString(key,value )
		.commit();
		return value;
	}
	private Long setUserShardPreferance(String key,Long value){
		this.getSharedPreferences(PREFS_ACCOUNT,MODE_PRIVATE)
		.edit()
		.putLong(key,value )
		.commit();
		return value;
	}
	private void removeUserShardPreferance(String key){
		this.getSharedPreferences(PREFS_ACCOUNT, MODE_PRIVATE)
		.edit()
		.remove(key)
		.commit();
	}
	private void checkLocation(){
		Log.d("Loguser","Checklocation");
		//
		if(locationLng == "" || locationLat=="" || locationTime < Calendar.getInstance().getTimeInMillis() - refreshLocationTime){
			Intent goLocation = new Intent(this.getApplicationContext(),LocationAppActivity.class);
			startActivityForResult(goLocation,REQUEST_LOCATION);
		}
	}
	private void checkLogin(){
		Log.d("Loguser","Checklogin:"+username);
		
		if(username==""){
			Intent goFacebookLogin = new Intent(this.getApplicationContext(),LoginAppActivity.class);
			startActivityForResult(goFacebookLogin,REQUEST_LOGIN);
		}
	}
	
	private void logout(){
		name = username = avatar = locationLat = locationLng = locationName =  null;
		locationTime = 0;
		removeUserShardPreferance("name");
		removeUserShardPreferance("username");
		removeUserShardPreferance("avatar");
		removeUserShardPreferance("locationLat");
		removeUserShardPreferance("locationLng");
		removeUserShardPreferance("locationName");
		removeUserShardPreferance("locationTime");
		Intent goFacebookLogin = new Intent(this.getApplicationContext(),LoginAppActivity.class);
		goFacebookLogin.putExtra("logout", true);
		startActivityForResult(goFacebookLogin,REQUEST_LOGIN);
		
	}
	
	private void startChat(){
		Intent goChat = new Intent(this.getApplicationContext(),ChatActivity.class);
		goChat.putExtra("username", username)
		.putExtra("name", name)
		.putExtra("avatar", avatar)
		.putExtra("roomName", "CentralChaegwattana");
		startActivityForResult(goChat,REQUEST_CHAT);
	}
}

