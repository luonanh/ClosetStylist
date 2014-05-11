package com.example.closetstylist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class ViewItemActivity extends Activity {
	private final static String LOG_TAG = AddItemActivity.class.getCanonicalName();
	private ItemDatabaseHelper itemDatabaseHelper = null;
	private Context context = null;
	
	private Uri imagePath = null;
	private TextView imageLocation = null;
	private Button buttonEdit = null;
	private Button buttonDelete = null;
	private TextView name = null;
	private TextView description = null;
	private ImageView image = null;
	private Button buttonAddImage = null;
	private TextView color = null;
	private TextView tempMin = null;
	private TextView tempMax = null;
	private TextView category = null;
	private TextView brand = null;
	private TextView age = null;
	private TextView material = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_item);
		
		context = getApplicationContext();
		
		// Restore ItemData passed in by MyCloset activity
		itemDatabaseHelper = new ItemDatabaseHelper(this);
		ItemData itemData = getIntent().getExtras().getParcelable(ItemData.INTENT);
		Log.i(LOG_TAG, itemData.toString());

		imageLocation = (TextView) findViewById(
				R.id.view_item_value_image_location);
		imageLocation.setText(itemData.getImageLink());
		
		name = (TextView) findViewById(R.id.view_item_value_name);
		name.setText(itemData.getName());
		
		description = (TextView) findViewById(R.id.view_item_value_description);
		description.setText(itemData.getDescription());
		
		image = (ImageView) findViewById(R.id.view_item_image);
		image.setImageURI(Uri.parse(itemData.getImageLink()));
		
		color = (TextView) findViewById(R.id.view_item_value_color);
		color.setText(itemData.getColor());
		
		tempMin = (TextView) findViewById(R.id.view_item_value_temp_min);
		tempMin.setText(Integer.toString(itemData.getTempMin()));

		tempMax = (TextView) findViewById(R.id.view_item_value_temp_max);
		tempMax.setText(Integer.toString(itemData.getTempMax()));

		category = (TextView) findViewById(R.id.view_item_value_category);
		category.setText(itemData.getCategory());

		brand = (TextView) findViewById(R.id.view_item_value_brand);
		brand.setText(itemData.getBrand());
		
		age = (TextView) findViewById(R.id.view_item_value_age);
		age.setText(Double.toString(itemData.getAge()));

		material = (TextView) findViewById(R.id.view_item_value_material);
		material.setText(itemData.getMaterial());

		buttonEdit = (Button) findViewById(R.id.view_item_btn_edit);
		buttonEdit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}			
		});
		
		buttonDelete = (Button) findViewById(R.id.view_item_btn_delete);
		buttonDelete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}			
		});

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		// Set Activity's result with result code RESULT_OK to make MyCloset
		// activity refresh cursor if changed.
		setResult(Activity.RESULT_OK);
		finish();
	}

}
