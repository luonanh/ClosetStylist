package com.example.ui;

import com.example.closetstylist.ItemDataAdapter;
import com.example.closetstylist.ItemDatabaseHelper;
import com.example.closetstylist.R;
import com.example.closetstylist.R.id;
import com.example.closetstylist.R.layout;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class LaundryBagActivity extends Activity {
	private final static String LOG_TAG = LaundryBagActivity.class
			.getCanonicalName();

	private ItemDatabaseHelper itemDatabaseHelper;
	private ItemDataAdapter itemDataAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_closet);
		
		// Setup CursorAdapter
		itemDatabaseHelper = new ItemDatabaseHelper(this);
		ListView listView = (ListView) findViewById(R.id.my_closet_list);
		itemDataAdapter = new ItemDataAdapter(this, itemDatabaseHelper.queryDirtyItem(true));
		listView.setAdapter(itemDataAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick (AdapterView<?> parent, View view, int position, long id) {
				Cursor c = (Cursor) itemDataAdapter.getItem(position);
				if (null != c) {
					Log.i(LOG_TAG, "Cursor is NOT null");
					/*
					ItemData itemData = ItemDatabaseHelper.getItemDataFromCursor(c);
					Intent i1 = new Intent(LaundryBagActivity.this, ViewItemActivity.class);
					i1.putExtra(ItemData.INTENT, itemData);
					startActivityForResult(i1, VIEW_ITEM_REQUEST);
					*/									
				} else {
					Log.i(LOG_TAG, "Cursor is null");
				}
			}
		});
	}
}
