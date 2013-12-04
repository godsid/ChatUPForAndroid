package com.uniteitsolution.chatup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.ActionMode;
import android.view.Menu;

public class LoginActivity extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_login);
		this.setTitle(R.string.page_register_title);
		
		checkLogin();
		
		
		//Intent mIntent = new Intent();
		//mIntent.putExtra("facebookUser", "tui");
		
		//setResult(RESULT_OK,mIntent);
		
		
		/*
		Session.openActiveSession(this, true, new Session.StatusCallback() {
			
			@Override
			public void call(Session session, SessionState state, Exception exception) {
				// TODO Auto-generated method stub
				if(session.isOpened()){
					Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {
						
						@Override
						public void onCompleted(final GraphUser user, final Response response) {
							// TODO Auto-generated method stub
							if(user!=null){
								TextView welcome = (TextView) findViewById(R.id.welcome);
                				welcome.setText("Hello " + user.getName() + "!");
							}
						}
					});
					
				}
			}
		});
		*/
		
	}
	
	public void checkLogin(){
		Intent goRoomPage = new Intent(getApplicationContext(),RoomActivity.class);
		startActivity(goRoomPage);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	}
	
}
