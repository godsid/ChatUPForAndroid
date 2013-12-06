package com.uniteitsolution.chatup;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoginAppActivity extends Activity{
	public static final String PREFS_ACCOUNT = "account";
	Button facebookLogin ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		facebookLogin = (Button)findViewById(R.id.facebookLogin);
		
		facebookLogin.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getSharedPreferences(PREFS_ACCOUNT, MODE_PRIVATE).edit().putString("username", "godsid").commit();
				finish();
			}
		});
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
				setResult(RESULT_OK, new Intent().putExtra("exit", true));
				finish();
			}
		})
		.setNegativeButton(R.string.alert_exit_button_no,null)
		.show();	
	}
	
}
