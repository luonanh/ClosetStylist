package com.example.closetstylist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

public class MyClosetActivity extends Activity {
	private final static String LOG_TAG = MyClosetActivity.class
			.getCanonicalName();
	static final int ADD_ITEM_REQUEST = 1;
	
	private ItemDatabaseHelper itemDatabaseHelper;
	private ItemDataAdapter itemDataAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_closet);

		// AddItem button
		Button addItemBtn = (Button) findViewById(R.id.my_closet_btn_add_item);
		addItemBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i1 = new Intent(MyClosetActivity.this, AddItemActivity.class);
				startActivityForResult(i1, ADD_ITEM_REQUEST);
			}
		});
		
		// Back button
		Button backBtn = (Button) findViewById(R.id.my_closet_btn_back);
		backBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		// Setup CursorAdapter
		itemDatabaseHelper = new ItemDatabaseHelper(this);
		ListView listView = (ListView) findViewById(R.id.my_closet_list);
		itemDataAdapter = new ItemDataAdapter(this, itemDatabaseHelper.getAllItemRecords());
		listView.setAdapter(itemDataAdapter);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		Log.i(LOG_TAG, "Entered onActivityResult()");
		
		// RESULT_OK result code and a recognized request code
		// If so, update the Textview showing the user-entered text.
		if (ADD_ITEM_REQUEST == requestCode) {	// Check which request we're responding to
			if (Activity.RESULT_OK == resultCode) {	// Make sure the request was successful
				ItemData itemData = data.getExtras().getParcelable(ItemData.INTENT);
				itemDatabaseHelper.saveRecord(itemData);
				itemDataAdapter.changeCursor(itemDatabaseHelper.getAllItemRecords());
			}
		}
	}

}
