package com.example.closetstylist;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class ViewUserProfileActivity extends Activity {
	private final static String LOG_TAG = ViewUserProfileActivity.class.getCanonicalName();
	private ItemDatabaseHelper mItemDatabaseHelper = null;
	private Context mContext = null;

	private TextView usr;
	private TextView pwd;
	private TextView sex;
	private TextView zip;
	private TextView schedule;
	private TextView day;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_user_profile);
		
		mContext = getApplicationContext();
		mItemDatabaseHelper = new ItemDatabaseHelper(this);
		ArrayList<UserProfile> userList = mItemDatabaseHelper.getAllUserProfile();
		
		usr = (TextView) findViewById(R.id.view_user_profile_value_username);
		usr.setText(userList.get(0).getUsr());
		
		pwd = (TextView) findViewById(R.id.view_user_profile_value_password);
		pwd.setText(userList.get(0).getPwd());

		sex = (TextView) findViewById(R.id.view_user_profile_value_sex);
		sex.setText(userList.get(0).getSex());

		zip = (TextView) findViewById(R.id.view_user_profile_value_zip);
		zip.setText(Integer.toString(userList.get(0).getZip()));
		
		schedule = (TextView) findViewById(R.id.view_user_profile_value_laundry_schedule);
		schedule.setText(Integer.toString(userList.get(0).getLaundrySchedule()));

		day = (TextView) findViewById(R.id.view_user_profile_value_laundry_day);
		day.setText(userList.get(0).getLaundryDay());
	}
}
