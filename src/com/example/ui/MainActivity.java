package com.example.ui;

import com.example.closetstylist.ItemDatabaseHelper;
import com.example.closetstylist.R;
import com.example.closetstylist.R.id;
import com.example.closetstylist.R.layout;
import com.example.closetstylist.R.menu;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.Button;

public class MainActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		Button loginBtn = (Button) findViewById(R.id.login_btn_laundry_bag);
		loginBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i1 = new Intent(MainActivity.this, LaundryBagActivity.class);
				startActivity(i1);
			}
		});
		
		Button myClosetBtn = (Button) findViewById(R.id.main_btn_my_closet);
		myClosetBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i2 = new Intent(MainActivity.this, MyClosetActivity.class);
				startActivity(i2);				
			}
			
		});
		
		Button clearMyClosetBtn = (Button) findViewById(R.id.main_btn_clear_my_closet);
		Context context = this;
		clearMyClosetBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ItemDatabaseHelper itemDatabaseHelper = 
						new ItemDatabaseHelper(MainActivity.this);
				itemDatabaseHelper.deleteDatabase();
			}
			
		});
		
		Button outfitBtn = (Button) findViewById(R.id.main_btn_outfit);
		outfitBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i3 = new Intent(MainActivity.this, OutfitActivity.class);
				startActivity(i3);
			}
			
		});

		Button viewUserProfileBtn = (Button) findViewById(R.id.main_btn_view_user_profile);
		viewUserProfileBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i4 = new Intent(MainActivity.this, ViewUserProfileActivity.class);
				startActivity(i4);
			}
			
		});
		
		Button registerUserProfileBtn = (Button) findViewById(R.id.main_btn_register_user_profile);
		registerUserProfileBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i5 = new Intent(MainActivity.this, RegisterActivity.class);
				startActivity(i5);				
			}
			
		});

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

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}

}
