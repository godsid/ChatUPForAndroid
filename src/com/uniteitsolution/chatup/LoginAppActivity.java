package com.uniteitsolution.chatup;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.facebook.widget.ProfilePictureView;

public class LoginAppActivity extends Activity{
	public static final String PREFS_ACCOUNT = "account";
	private LoginButton fbLoginButton;
	private ProfilePictureView fbProfilePicture;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		fbLoginButton = (LoginButton)findViewById(R.id.facebook_button_login);
		fbProfilePicture = (ProfilePictureView)findViewById(R.id.facebook_image);
		
		//logKeyHash();
		
		
		// start Facebook Login
		Session.openActiveSession(LoginAppActivity.this, true, new Session.StatusCallback() {
		    // callback when session changes state
		    @Override
		    public void call(Session session, SessionState state, Exception exception) {
		    	
		    	/*if(session.isOpened() && getIntent().getBooleanExtra("logout", false)){
					logout();
				}*/
		    	
		    	if (session.isOpened()) {
		    		Request.newMeRequest(session, new Request.GraphUserCallback() {
						
						@Override
						public void onCompleted(final GraphUser user, final Response response) {
							if(user!=null){
								
								fbProfilePicture.setProfileId(user.getId());
								Intent intent = new Intent ()
								.putExtra("username", user.getUsername())
								.putExtra("avatar", getFacebookAvatar(user.getId()))
								.putExtra("name", user.getName());
								
								setResult(RESULT_OK, intent);
								finish();
								
								//getSharedPreferences(PREFS_ACCOUNT, MODE_PRIVATE).edit().putString("username", user.getUsername()).commit();
								//getSharedPreferences(PREFS_ACCOUNT, MODE_PRIVATE).edit().putString("avatar", "http://graph.facebook.com/"+user.getId()+"/picture?type=square").commit();
								//finish();
							}
						}
					}).executeAsync();
		    	}
		    }
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(LoginAppActivity.this, requestCode, resultCode, data);
		//Log.d("Loguser","Activity:"+String.valueOf(requestCode));
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
	
	private String getFacebookAvatar(String facebookID){
		return "http://graph.facebook.com/"+facebookID+"/picture?type=square";
	}
	
	private void logout(){
		Session.getActiveSession().closeAndClearTokenInformation();
	}
	
	public void logKeyHash() {
		try {
			PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
			for (Signature signature : info.signatures) {
				MessageDigest md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
				Log.i("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT));
			}
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
}
