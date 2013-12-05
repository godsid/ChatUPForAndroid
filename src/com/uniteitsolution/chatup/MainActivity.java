package com.uniteitsolution.chatup;

import com.facebook.LoginActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends Activity {
	
	public static SharedPreferences preferences;
	public static final String PREFS_ACCOUNT = "account";
	public static String username; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		this.checkLogin();
		
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
	
	protected void checkLogin(){
		username = this.getSharedPreferences(PREFS_ACCOUNT,MODE_PRIVATE).getString("username", "");
		if(username==""){
			Intent goFacebookLogin = new Intent(this.getApplicationContext(),LoginAppActivity.class);
			startActivityForResult(goFacebookLogin,1);
		}else{
			Intent goRoom = new Intent(this.getApplicationContext(),RoomActivity.class);
			startActivityForResult(goRoom,1);
		}
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		this.checkLogin();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		if(resultCode==RESULT_OK){
			if(data.getBooleanExtra("exit",false)==true){
				finish();
			}
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		new MenuManagement(item);
		switch(item.getItemId()){
		case R.id.menu_logout:
			this.getSharedPreferences(PREFS_ACCOUNT, MODE_PRIVATE).edit().remove("username").commit();
			break;
		}
		return super.onOptionsItemSelected(item);
		
	}
	
}

