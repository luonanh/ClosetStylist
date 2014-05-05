package com.example.closetstylist;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

public class MyClosetActivity extends Activity {
	private ItemDatabaseHelper itemDatabaseHelper;
	private ItemDataAdapter itemDataAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_closet);
		/*
		 *         ListView listView = (ListView) findViewById(R.id.times_list);
        timeTrackerAdapter = new TimeTrackerAdapter(this,databaseHelper.getAllTimeRecords());
        listView.setAdapter(timeTrackerAdapter);
		 */
		itemDatabaseHelper = new ItemDatabaseHelper(this);
		ListView listView = (ListView) findViewById(R.id.my_closet_list);
		itemDataAdapter = new ItemDataAdapter(this, itemDatabaseHelper.getAllTimeRecords());
		listView.setAdapter(itemDataAdapter);
	}

}
