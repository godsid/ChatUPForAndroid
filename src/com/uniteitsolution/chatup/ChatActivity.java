package com.uniteitsolution.chatup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.koushikdutta.async.http.socketio.Acknowledge;
import com.koushikdutta.async.http.socketio.ConnectCallback;
import com.koushikdutta.async.http.socketio.EventCallback;
import com.koushikdutta.async.http.socketio.SocketIOClient;


public class ChatActivity extends Activity {

	static final String PREFS_ACCOUNT = "account";
	static final String SERVER_URL = "http://api.srihawong.info:8080";
	static final String SERVER_NAMESPACE = "/chat";
	
	static private String name, username, avatar, roomName;
	
	
	private ViewGroup messagesContainer;
	private ScrollView scrollContainer;
	private EditText messageBox;
	private Button sendButton;
	
	
	public SocketIOClient chatClient;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.layout_chat);
		this.setTitle(R.string.page_chat_title);
		
		messageBox = (EditText)findViewById(R.id.chat_message_box);
		sendButton = (Button)findViewById(R.id.chat_send_button);
		scrollContainer = (ScrollView)findViewById(R.id.chat_scroll_container);
		messagesContainer = (ViewGroup)findViewById(R.id.chat_message_container);
		
		Intent intent = getIntent();
		name = intent.getStringExtra("name");
		username = intent.getStringExtra("username");
		avatar = intent.getStringExtra("avatar");
		roomName = intent.getStringExtra("roomName");
		
		this.connect();
				
		sendButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.d("Loguser","click");
				if(messageBox.getText().toString()!=""){
					//chatClient.emit(chatBox.getText().toString());
					try {
						chatClient.emit(new JSONObject("{msg:\""+messageBox.getText().toString()+"\",user:{name:\""+username+"\",avatar:\""+avatar+"\"}}"));
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						Log.d("Loguser",e.getMessage());
					}
					messageBox.setText("");
				}
			}
		});	
	}
	
	private void connect(){
		SocketIOClient.connect(SERVER_URL,new ConnectCallback() {
			
			@Override
			public void onConnectCompleted(Exception ex, SocketIOClient client) {
				// TODO Auto-generated method stub
				if (ex != null) {
					Log.d("Loguser","Error:"+ex.getMessage());
		            return;
		        }
				
				Log.d("Loguser","Connect Complate");
				
				client.of(SERVER_NAMESPACE,new ConnectCallback() {
					
					@Override
					public void onConnectCompleted(Exception ex, SocketIOClient client) {
						// TODO Auto-generated method stub
						 if (ex != null) {
			                    ex.printStackTrace();
			                    Log.d("Loguser","Error:"+ex.getMessage());
			                    return;
			             }
						chatClient = client;
						joinRoom(client);
						recv(client);
					}
				});
			}
		}, new Handler()); 
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

	private void joinRoom(SocketIOClient client) {
		client.addListener("join room", new EventCallback() {
			
			@Override
			public void onEvent(String event, JSONArray argument,
					Acknowledge acknowledge) {
				// TODO Auto-generated method stub
				try {
					Log.d("Loguser","Joinded "+argument.get(0).toString());
					
					//chatHead.setText("CentralChaegwattana[1]");
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Log.d("Loguser",e.getMessage());
				}
			}
		});
		try {
			client.emit("join room", new JSONArray("[{name:'"+roomName+"',user:{name:'"+username+"',avatar:'"+avatar+"'}}]"));
			setTitle(getString(R.string.page_chat_title)+":"+roomName);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void send(SocketIOClient client){
		
	}
	
	private void recv(SocketIOClient client){
		client.addListener("message", new EventCallback() {
			
			@Override
			public void onEvent(String event, JSONArray msg,
					Acknowledge acknowledge) {
				// TODO Auto-generated method stub
				try {
					Toast.makeText(getBaseContext(),"Message:"+msg.getString(0).toString(),Toast.LENGTH_SHORT).show();
					showMessage(msg);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Log.d("Loguser",e.getMessage());
				}
			}
		});
	}
	
	private void showMessage(JSONArray jsonMessage){
		
		try {
			final JSONObject msg = (JSONObject) jsonMessage.getJSONObject(0);
			
			//Create layout
			LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			final View layoutMessage = inflater.inflate(R.layout.layout_chat_message, null);
			
			ImageView userAvatarImageView = (ImageView)layoutMessage.findViewById(R.id.chat_message_avatar);
			TextView nameTextView = (TextView)layoutMessage.findViewById(R.id.chat_message_name);
			TextView messageTextView = (TextView)layoutMessage.findViewById(R.id.chat_message_text);
			
			//Assige data
			new ImageViewAsync(msg.getJSONObject("user").getString("avatar"),userAvatarImageView);
			nameTextView.setText(msg.getJSONObject("user").getString("name"));
			messageTextView.setText(msg.getString("msg"));
			
			
			runOnUiThread(new Runnable() {
	            @Override
	            public void run() {
	            	messagesContainer.addView(layoutMessage);
	    			
	                // Scroll to bottom
	            	
	            	
	            	//scrollContainer.fullScroll(ScrollView.FOCUS_DOWN);
	            	
	                //if (scrollContainer.getChildAt(0) != null) {
	                //    scrollContainer.scrollTo(scrollContainer.getScrollX(), scrollContainer.getChildAt(0).getHeight());
	                //}
	                scrollContainer.fullScroll(View.FOCUS_DOWN);
	            }
	        });
			
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			
		}
		
		
	}
}
