package com.uniteitsolution.chatup;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.koushikdutta.async.http.socketio.Acknowledge;
import com.koushikdutta.async.http.socketio.ConnectCallback;
import com.koushikdutta.async.http.socketio.EventCallback;
import com.koushikdutta.async.http.socketio.SocketIOClient;


public class ChatActivity extends Activity {

	static final String PREFS_ACCOUNT = "account";
	public String username;
	public String userAvatar; 
	private ListView listChat;
	private ArrayList<ChatEntry> chatEntries;
	
	public SocketIOClient chatClient;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_chat);
		
		final EditText chatBox = (EditText)findViewById(R.id.chat_box);
		final Button chatSend = (Button)findViewById(R.id.chat_send);
		final TextView chatHead = (TextView)findViewById(R.id.chat_head);
		
		 username = getSharedPreferences(PREFS_ACCOUNT,MODE_PRIVATE).getString("username", "");
		 userAvatar = getSharedPreferences(PREFS_ACCOUNT,MODE_PRIVATE).getString("avatar", "");
		
		listChat = (ListView)findViewById(R.id.chatList);
		chatEntries = new ArrayList<ChatEntry>();
		
		ChatEntry listChatEntry = new ChatEntry();
		listChatEntry.setName("tui");
		listChatEntry.setMessage("Message data");
		chatEntries.add(listChatEntry);
		
		this.setTitle(R.string.page_chat_title);
		
		listChat.setAdapter(new ListViewAdapter());
		
		this.connect();
		
		chatSend.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.d("Loguser","click");
				if(chatBox.getText().toString()!=""){
					//chatClient.emit(chatBox.getText().toString());
					try {
						chatClient.emit(new JSONObject("{msg:\""+chatBox.getText().toString()+"\",user:{name:\""+username+"\",avatar:\""+userAvatar+"\"}}"));
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						Log.d("Loguser",e.getMessage());
					}
					chatBox.setText("");
				}
			}
		});	
	}
	
	private class ListViewAdapter extends BaseAdapter{

		private ListViewHolder holder;
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return chatEntries.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			
			convertView = LayoutInflater.from(ChatActivity.this).inflate(R.layout.chat_listview,null);
			holder = new ListViewHolder();
			holder.name = (TextView) convertView.findViewById(R.id.chat_list_name);
			holder.message = (TextView) convertView.findViewById(R.id.chat_list_message);
			holder.avatar = (ImageView) convertView.findViewById(R.id.chat_list_avatar);
			if(chatEntries.get(position).getName()!=null){
				holder.name.setText(chatEntries.get(position).getName());
				holder.message.setText(chatEntries.get(position).getMessage());
				//holder.avatar.setImageURI();
				
				//holder.avatar.setImageBitmap(fetchImage("http://graph.facebook.com/534460549/picture?type=square"));
				//fetchImage
			}
			return convertView;
		}
	}
	
	private class ListViewHolder{
		public TextView name,message;
		public ImageView avatar;
	}
	private void connect(){
		SocketIOClient.connect("http://api.srihawong.info:8080",new ConnectCallback() {
			
			@Override
			public void onConnectCompleted(Exception ex, SocketIOClient client) {
				// TODO Auto-generated method stub
				if (ex != null) {
					Log.d("Loguser","Error:"+ex.getMessage());
		            return;
		        }
				
				
				Log.d("Loguser","Connect Complate");
				
				client.of("/chat",new ConnectCallback() {
					
					@Override
					public void onConnectCompleted(Exception ex, SocketIOClient client) {
						// TODO Auto-generated method stub
						 if (ex != null) {
			                    ex.printStackTrace();
			                    Log.d("Loguser","Error:"+ex.getMessage());
			                    return;
			             }
						chatClient = client;
						findRoom(client);
						joinRoom(client);
						client.addListener("message", new EventCallback() {
							
							@Override
							public void onEvent(String event, JSONArray argument,
									Acknowledge acknowledge) {
								// TODO Auto-generated method stub
								try {
									Toast.makeText(getBaseContext(),"Message:"+argument.getString(0).toString(),Toast.LENGTH_SHORT).show();
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
									Log.d("Loguser",e.getMessage());
								}
							}
						});
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

	private void findRoom(SocketIOClient client){
		client.addListener("find room",new EventCallback() {
			
			@Override
			public void onEvent(String event, JSONArray argument,
					Acknowledge acknowledge) {
				// TODO Auto-generated method stub
				Log.d("Loguser","Reccive find room");
				try {
					Log.d("Loguser",argument.get(0).toString());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Log.d("Loguser",e.getMessage());
				}
			}
		});
		Log.d("Loguser","Find room");
		try {
			client.emit("find room", new JSONArray("[{lat: '100.5287415',lng: '13.9042641',dist:'5000',palce:'place'}]"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d("Loguser",e.getMessage());
		}
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
			client.emit("join room", new JSONArray("[{name:'CentralChaegwattana',user:{name:'"+username+"',avatar:'"+userAvatar+"'}}]"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void send(SocketIOClient client){
		
	}
	
	private void recv(SocketIOClient client){
		
	}
	
	public Bitmap fetchImage(String url) {
		try {
			URL imageUrl = new URL(url.trim());
			InputStream input = null;
			URLConnection conn = imageUrl.openConnection();
			
			HttpURLConnection httpConn = (HttpURLConnection)conn;
			httpConn.setRequestMethod("GET");
			httpConn.setReadTimeout(40000); // ตั้งเวลา  connect timeout
			httpConn.connect(); // connection
	 
			if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				input = httpConn.getInputStream(); // จับใส่ InputStream
			}
	      Bitmap bitmap = BitmapFactory.decodeStream(input); //แปลงเป็น Bitmap
	      input.close();
	      httpConn.disconnect();
	        return bitmap;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d("Loguser",e.getMessage());
		}catch ( IOException e ){
			Log.d("Loguser",e.getMessage());
			Log.d("fetchImage","IO exception: " + e);
	}catch(Exception e){
		Log.d("Loguser",e.getMessage());
			Log.d("fetchImage","Exception: " + e);
	}
		
		return null;
	}
}
