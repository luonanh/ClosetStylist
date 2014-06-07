package com.example.closetstylist;

import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class OutfitActivity extends Activity {
	private final static String LOG_TAG = OutfitActivity.class.getCanonicalName();
	private ItemDatabaseHelper itemDatabaseHelper = null;
	private Context context = null;
	
	private ImageView top = null;
	private ImageView bottom = null;
	private ItemData topItem = null;
	private ItemData bottomItem = null;
	private ImageButton buttonPrev = null;
	private ImageButton buttonNext = null;
	
	private ArrayList<ItemData> topList = null;
	private ArrayList<ItemData> bottomList = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_outfit_of_the_day_3);
		
		// collect common instance of helper class
		itemDatabaseHelper = new ItemDatabaseHelper(this);
		context = getApplicationContext();
		
		// collect ImageView on outfit page
		top = (ImageView) findViewById(R.id.outfit_label_top);
		bottom = (ImageView) findViewById(R.id.outfit_label_bottom);
		
		// Get all the "top" items from our database
		topList = itemDatabaseHelper.getAllTop();
		if (0 == topList.size()) {
			Toast.makeText(context, R.string.outfit_message_no_top, 
					Toast.LENGTH_SHORT).show();						
		} else {
			// Haven't created an algorithm to pick a "top" item yet.
			// Temporarily pick a random item
			int indexTop = 0;
			if (topList.size() > 1) {
				indexTop = new Random().nextInt(topList.size());
			}
			topItem = topList.get(indexTop);
			ImageSubSampler.subSampleCroppedUri(topItem, top, context);
		}
		

		// Get all the "bottom" items from our database
		bottomList = itemDatabaseHelper.getAllBottom();
		if (0 == bottomList.size()) {
			Toast.makeText(context, R.string.outfit_message_no_bottom, 
					Toast.LENGTH_SHORT).show();
		} else {
			// Haven't created an algorithm to pick a "bottom" item yet.
			// Temporarily pick a random item
			int indexbottom = 0;
			if (bottomList.size() > 1) {
				indexbottom = new Random().nextInt(bottomList.size());
			}
			bottomItem = bottomList.get(indexbottom);
			ImageSubSampler.subSampleCroppedUri(bottomItem, bottom, context);
		}
		
		buttonPrev = (ImageButton) findViewById(R.id.outfit_btn_prev);
		buttonPrev.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			}			
		});
		buttonNext = (ImageButton) findViewById(R.id.outfit_btn_next);
		buttonNext.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			}			
		});
	}
}
