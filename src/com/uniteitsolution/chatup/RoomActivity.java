package com.uniteitsolution.chatup;

//import com.google.android.gms.maps.MapView;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import com.uniteitsolution.chatup.MenuManagement;

public class RoomActivity extends Activity{

	public static SharedPreferences preferences;
	public static final String PREFS_ACCOUNT = "account";
	
	//private MapView maps;
	private GridView roomLists;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_room);
		this.setTitle(R.string.page_room_title);
		
		//maps = (MapView)findViewById(R.id.map);
	
		//roomLists = (GridView)findViewById(R.id.roomList);
		
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
		new MenuManagement(item);
		switch(item.getItemId()){
		case R.id.menu_logout:
			this.getSharedPreferences(PREFS_ACCOUNT, MODE_PRIVATE).edit().remove("username").commit();
			finish();
			break;
		}
		return super.onOptionsItemSelected(item);
		
	}
	
	
}
