package com.example.closetstylist;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
	static final int EDIT_ITEM_REQUEST = 1;
	
	private ItemDatabaseHelper itemDatabaseHelper = null;
	private ItemData itemData = null;
	private Context context = null;
	
	private Uri imagePath = null;
	private TextView imageLocation = null;
	private TextView cropImageLocation = null;
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
		itemData = getIntent().getExtras().getParcelable(ItemData.INTENT);
		Log.i(LOG_TAG, itemData.toString());

		imageLocation = (TextView) findViewById(
				R.id.view_item_value_image_location);
		imageLocation.setText(itemData.getImageLink());
		
		cropImageLocation = (TextView) findViewById(
				R.id.view_item_value_crop_image_location);
		cropImageLocation.setText(itemData.getCropImageLink());
		
		name = (TextView) findViewById(R.id.view_item_value_name);
		//name.setText(itemData.getName());
		
		description = (TextView) findViewById(R.id.view_item_value_description);
		//description.setText(itemData.getDescription());
		
		image = (ImageView) findViewById(R.id.view_item_image);
		//image.setImageURI(Uri.parse(itemData.getImageLink()));
		
		color = (TextView) findViewById(R.id.view_item_value_color);
		//color.setText(itemData.getColor());
		
		tempMin = (TextView) findViewById(R.id.view_item_value_temp_min);
		//tempMin.setText(Integer.toString(itemData.getTempMin()));

		tempMax = (TextView) findViewById(R.id.view_item_value_temp_max);
		//tempMax.setText(Integer.toString(itemData.getTempMax()));

		category = (TextView) findViewById(R.id.view_item_value_category);
		//category.setText(itemData.getCategory());

		brand = (TextView) findViewById(R.id.view_item_value_brand);
		//brand.setText(itemData.getBrand());
		
		age = (TextView) findViewById(R.id.view_item_value_age);
		//age.setText(Double.toString(itemData.getAge()));

		material = (TextView) findViewById(R.id.view_item_value_material);
		//material.setText(itemData.getMaterial());
		
		setViewItemActivityFromItemData(itemData); // set all the fields in this activity

		buttonEdit = (Button) findViewById(R.id.view_item_btn_edit);
		buttonEdit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i1 = new Intent(ViewItemActivity.this, EditItemActivity.class);
				i1.putExtra(ItemData.INTENT, itemData);
				startActivityForResult(i1, EDIT_ITEM_REQUEST);
			}			
		});
		
		buttonDelete = (Button) findViewById(R.id.view_item_btn_delete);
		buttonDelete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String message;

				message = context.getString(
						R.string.view_item_dialog_deletion_message);

				//new AlertDialog.Builder(context) --> android alertdialog unable to add window token null is not for an application
				new AlertDialog.Builder(ViewItemActivity.this)
						.setIcon(android.R.drawable.ic_dialog_alert)
						.setTitle(R.string.view_item_dialog_deletion_title)
						.setMessage(message)
						.setPositiveButton(R.string.view_item_dialog_deletion_yes,
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										try {
											// delete original image and cropped image from SD card
											AddItemActivity.deleteItemImagesFromSd(itemData);
											
											// delete the entry in the item database
											itemDatabaseHelper.deleteItemDataRecord(itemData);
										} catch (Exception e) {
											Log.e(LOG_TAG, "Exception Caught => "
													+ e.getMessage());
											e.printStackTrace();
										} 
										setResult(Activity.RESULT_OK);
										finish();
									}

								})
						.setNegativeButton(R.string.view_item_dialog_deletion_no, null)
						.create()
						.show();
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
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i(LOG_TAG, "Entered onActivityResult()");
		
		// RESULT_OK result code and a recognized request code
		// If so, update the Textview showing the user-entered text.
		if (EDIT_ITEM_REQUEST == requestCode) {	// Check which request we're responding to
			if (Activity.RESULT_OK == resultCode) {	// Make sure the request was successful
				itemData = data.getExtras().getParcelable(ItemData.INTENT);
				setViewItemActivityFromItemData(itemData);
			}
		}
	}

	private void setViewItemActivityFromItemData(ItemData item) {
		name.setText(item.getName());
		description.setText(item.getDescription());
		imageLocation.setText(item.getImageLink());
		//image.setImageURI(Uri.parse(item.getImageLink()));
		new ImageSubSampler(context).subSampleOriginalUri(itemData, image, context);
		cropImageLocation.setText(item.getCropImageLink());
		color.setText(item.getColor());
		tempMin.setText(Integer.toString(item.getTempMin()));
		tempMax.setText(Integer.toString(item.getTempMax()));
		category.setText(item.getCategory());
		brand.setText(item.getBrand());
		age.setText(Double.toString(item.getAge()));
		material.setText(item.getMaterial());
	}

}
