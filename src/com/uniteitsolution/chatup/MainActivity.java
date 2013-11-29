package com.uniteitsolution.chatup;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;

public class MainActivity extends Activity {

	private Button facebookLogin;
	private TextView registerLabel; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		facebookLogin = (Button)findViewById(R.id.facebookLogin);
		registerLabel = (TextView)findViewById(R.id.registerLabel);
		
		facebookLogin.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				registerLabel.setText("dddddd");
				Intent goRoom = new Intent(getApplicationContext(),RoomActivity.class);
				startActivity(goRoom);
				
				
			}
		});
			
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}

