package com.example.closetstylist;

import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
		Cursor cTop = itemDatabaseHelper.queryTop();
		int topCount = cTop.getCount();
		Log.i(LOG_TAG, "The number of rows in cTop is: " + topCount);
		if (cTop.getCount() == 0) {
			Toast.makeText(context, R.string.outfit_message_no_top, 
					Toast.LENGTH_SHORT).show();
		}
		
		// Haven't created an algorithm to pick a "top" item yet.
		// Temporarily pick a random item
		int indexTop = 0;
		if (cTop.getCount() > 1) {
			indexTop = new Random().nextInt(cTop.getCount());
		}
		if (null != cTop) {
			cTop.moveToFirst();
			for (int i = 0; i < cTop.getCount(); i++) {
				if (i == indexTop)
					break;
				cTop.moveToNext();
			}
			// Dump all the rows pointed to by cursor Log.i(LOG_TAG, DatabaseUtils.dumpCursorToString(cTop));
			topItem = ItemDatabaseHelper.getItemDataFromCursor(cTop); // build ItemData from cursor
			/* Debug log
			if (cTop.moveToFirst()) {
				int numOfColumns = cTop.getColumnCount();
				int id = cTop.getColumnIndex(Schema.Item.Cols.ID);
				int name = cTop.getColumnIndex(Schema.Item.Cols.NAME);
				int cropImage = cTop.getColumnIndex(Schema.Item.Cols.CROP_IMAGE_LINK);
				int material = cTop.getColumnIndex(Schema.Item.Cols.MATERIAL);
				Log.i(LOG_TAG, "The number of columns is: " + bottomCount + " - index of column name is: " + name
						+ " - index of column id is: " + id + " - index of column cropImage is: " + cropImage
						+ " - index of column material is: " + material);
				Log.i(LOG_TAG, DatabaseUtils.dumpCursorToString(cTop));
				topItem = ItemDatabaseHelper.getItemDataFromCursor(cTop);
			}
			*/
		}
		top.setImageURI(Uri.parse(topItem.getCropImageLink())); // display the image

		// Get all the "bottom" items from our database
		Cursor cBottom = itemDatabaseHelper.queryBottom();
		int bottomCount = cBottom.getCount();
		Log.i(LOG_TAG, "The number of rows in cTop is: " + bottomCount);
		if (cBottom.getCount() == 0) {
			Toast.makeText(context, R.string.outfit_message_no_bottom, 
					Toast.LENGTH_SHORT).show();
		}
	
		// Haven't created an algorithm to pick a "bottom" item yet.
		// Temporarily pick a random item
		int indexBottom = 0;
		if (cBottom.getCount() > 1) {
			indexBottom = new Random().nextInt(cBottom.getCount());
		}
		if (null != cBottom) {
			cBottom.moveToFirst();
			for (int i = 0; i < cBottom.getCount(); i++) {
				/* Debug log
				int numOfColumns = cBottom.getColumnCount();
				int id = cBottom.getColumnIndex(Schema.Item.Cols.ID);
				int name = cBottom.getColumnIndex(Schema.Item.Cols.NAME);
				int cropImage = cBottom.getColumnIndex(Schema.Item.Cols.CROP_IMAGE_LINK);
				int material = cBottom.getColumnIndex(Schema.Item.Cols.MATERIAL);
				Log.i(LOG_TAG, "The number of columns is: " + bottomCount + " - index of column name is: " + name
						+ " - index of column id is: " + id + " - index of column cropImage is: " + cropImage
						+ " - index of column material is: " + material);
				*/

				if (i == indexBottom)
					break;
				cBottom.moveToNext();
			}
			// Dump all the rows pointed to by cursor Log.i(LOG_TAG, DatabaseUtils.dumpCursorToString(cBottom));
			bottomItem = ItemDatabaseHelper.getItemDataFromCursor(cBottom); // build ItemData from cursor
		}
		bottom.setImageURI(Uri.parse(bottomItem.getCropImageLink())); // display the image

	}
}
