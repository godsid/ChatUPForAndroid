package com.uniteitsolution.chatup;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.socketio.Acknowledge;
import com.koushikdutta.async.http.socketio.ConnectCallback;
import com.koushikdutta.async.http.socketio.JSONCallback;
import com.koushikdutta.async.http.socketio.SocketIOClient;

public class ChatActivity extends Activity{

	static final String PREFS_ACCOUNT = "account";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_chat);
		this.setTitle(R.string.page_chat_title);
		this.connect();
	}

	private void connect(){
		SocketIOClient.connect(AsyncHttpClient.getDefaultInstance(), "http://api.srihawong.info:8080/chat", new ConnectCallback() {
			
			@Override
			public void onConnectCompleted(Exception ex, SocketIOClient client) {
				// TODO Auto-generated method stub
				 if (ex != null){
			            ex.printStackTrace();
			            return;
				 }
				Log.d("Loguser","Connect Completed");
				client.setJSONCallback(new JSONCallback() {
					
					@Override
					public void onJSON(JSONObject json, Acknowledge acknowledge) {
						// TODO Auto-generated method stub
						Log.d("Loguser","JSON Callback");
					}
				});
				try {//5::/chat:{"name":"find room","args":[{"lat":13.9042306,"lng":100.5299558,"palce":"place"}]}
					//client.emit("Hello World");
					Log.d("Loguser","send find room");
					client.emit(new JSONObject("name:'find room',args:[{lat: 100.5287415,lng: '13.9042641',dist:'5000',palce:'place'}]"));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				client.disconnect();
			}
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
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
