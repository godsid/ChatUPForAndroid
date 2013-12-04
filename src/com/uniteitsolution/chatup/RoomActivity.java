package com.uniteitsolution.chatup;

//import com.google.android.gms.maps.MapView;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.GridView;

public class RoomActivity extends Activity{

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

}
