package com.piropa_field_test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.piropa_field_test.data.DatabaseHandler;
import com.piropa_field_test.fragments.GobackFragment;
import com.piropa_field_test.fragments.MainFragment;
import com.piropa_field_test.fragments.MeasureFragment;

//hello

public class MainActivity extends Activity {

	private MainFragment mainFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
	
		if(getIntent().getAction().equals("OPEN_TAB_1")) {
			getFragmentManager().beginTransaction()
			.replace(R.id.container, GobackFragment.getInstance())
			.commit();
			
		}else{
		if (mainFragment == null) {
			mainFragment = new MainFragment();
		}

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, mainFragment).commit();
		}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}

}
// kommentar blabla