package com.example.closetstylist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

public class MyClosetActivity extends Activity {
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
				startActivity(i1);
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
		itemDataAdapter = new ItemDataAdapter(this, itemDatabaseHelper.getAllTimeRecords());
		listView.setAdapter(itemDataAdapter);
	}

}
