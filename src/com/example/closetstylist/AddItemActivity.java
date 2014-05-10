package com.example.closetstylist;

import java.io.File;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AddItemActivity extends Activity {
	private final static String LOG_TAG = AddItemActivity.class.getCanonicalName();
	private ItemDatabaseHelper itemDatabaseHelper;
	
	public static final int MEDIA_TYPE_IMAGE = 1;
	
	static final int CAMERA_PIC_REQUEST = 1;
	
	private Uri imagePath = null;
	TextView imageLocation = null;
	Uri imagePathFinal = null;
	Button buttonReset = null;
	Button buttonRegister = null;
	Button buttonDiscard = null;
	EditText name = null;
	EditText description = null;
	ImageView image = null;
	Button buttonAddImage = null;
	Context context = null;
	Spinner color = null;
	Spinner tempMin = null;
	Spinner tempMax = null;
	Spinner category = null;
	Spinner brand = null;
	Spinner age = null;
	Spinner material = null;
	ArrayList<String> temperatureArray = new ArrayList<String>();//new ArrayList<String>(Arrays.asList("10", "11", "12"));
	ArrayList<String> categoryArray = new ArrayList<String>(Arrays.asList("dress", "jacket", "jeans", "shirt", "short", "t-shirt"));
	ArrayList<String> brandArray = new ArrayList<String>(Arrays.asList("Banana", "Express", "RalphLauren", "CK", "Adiddas", "Nike"));
	ArrayList<String> ageArray = new ArrayList<String>();
	ArrayList<String> materialArray = new ArrayList<String>(Arrays.asList("wool", "cotton", "nylon"));
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_item);
		
		context = getApplicationContext();
		itemDatabaseHelper = new ItemDatabaseHelper(this);
		
		imageLocation = (TextView) findViewById(
				R.id.add_item_value_image_location);
		name = (EditText) findViewById(R.id.add_item_value_name);
		description = (EditText) findViewById(R.id.add_item_value_description);
		image = (ImageView) findViewById(R.id.add_item_image);
		
		color = (Spinner) findViewById(R.id.add_item_spinner_color);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> colorAdapter = ArrayAdapter.createFromResource(this,
		        R.array.add_item_spinner_color, R.layout.color_dropdown_item);
		// Apply the adapter to the spinner
		color.setAdapter(colorAdapter);
		
		// initialize temperature array for tempMin and tempMax spinner
		for (int i=-30; i<=120; i++) {
			temperatureArray.add(String.valueOf(i));
		}
		
		tempMin = (Spinner) findViewById(R.id.add_item_spinner_temp_min);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<String> tempMinAdapter = new ArrayAdapter<String>(this,
		        R.layout.temperature_dropdown_item, temperatureArray);
		// Apply the adapter to the spinner
		tempMin.setAdapter(tempMinAdapter);
		
		tempMax = (Spinner) findViewById(R.id.add_item_spinner_temp_max);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<String> tempMaxAdapter = new ArrayAdapter<String>(this,
		        R.layout.temperature_dropdown_item, temperatureArray);
		// Apply the adapter to the spinner
		tempMax.setAdapter(tempMaxAdapter);

		category = (Spinner) findViewById(R.id.add_item_spinner_category);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(this,
		        R.layout.temperature_dropdown_item, categoryArray);
		// Apply the adapter to the spinner
		category.setAdapter(categoryAdapter);
		
		brand = (Spinner) findViewById(R.id.add_item_spinner_brand);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<String> brandAdapter = new ArrayAdapter<String>(this,
		        R.layout.temperature_dropdown_item, brandArray);
		// Apply the adapter to the spinner
		brand.setAdapter(brandAdapter);

		// initialize temperature array for tempMin and tempMax spinner
		for (int i=0; i<=20; i++) {
			ageArray.add(String.valueOf(i));
		}
		age = (Spinner) findViewById(R.id.add_item_spinner_age);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<String> ageAdapter = new ArrayAdapter<String>(this,
		        R.layout.temperature_dropdown_item, ageArray);
		// Apply the adapter to the spinner
		age.setAdapter(ageAdapter);
		

		material = (Spinner) findViewById(R.id.add_item_spinner_material);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<String> materialAdapter = new ArrayAdapter<String>(this,
		        R.layout.temperature_dropdown_item, materialArray);
		// Apply the adapter to the spinner
		material.setAdapter(materialAdapter);

		buttonReset = (Button) findViewById(R.id.add_item_btn_reset);
		buttonReset.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				name.setText("");// ALDBG why somebody does ("" + "") instead of just ("")
				description.setText("");
			}			
		});
		
		buttonRegister = (Button) findViewById(R.id.add_item_btn_register);
		buttonRegister.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				/*
				 * Make sure all the required fields are filled in 
				 */
				
				// image field
				if (null == imagePath) {
					Toast.makeText(context, 
							R.string.add_item_message_no_image, 
							Toast.LENGTH_SHORT)
							.show();
					return;
				}
				
				/*
				 * Create a new ItemData instance.
				 * Hard code color, tempMin, tempMax, category for now
				 */
				ItemData.ItemDataBuilder itemDataBuilder = new ItemData.ItemDataBuilder(
						imagePath.toString(), 
						color.getSelectedItem().toString(), 
						Integer.valueOf(tempMin.getSelectedItem().toString()), 
						Integer.valueOf(tempMax.getSelectedItem().toString()), 
						category.getSelectedItem().toString())
						.brand(brand.getSelectedItem().toString())
						.age(Double.valueOf(age.getSelectedItem().toString()))
						.material(material.getSelectedItem().toString());
						//.build();
				
				if (!name.getText().toString().isEmpty()) {
					itemDataBuilder.name(name.getText().toString());
				}
				
				if (!description.getText().toString().isEmpty()) {
					itemDataBuilder.description(description.getText().toString());
				}

				/* 
				 * brand, age, material are not optional anymore because they are spinner
				 * will change later if convert them from Spinner to EditText
				 *
				if (!material.getSelectedItem().toString().isEmpty()) {
					itemDataBuilder.material(material.getSelectedItem().toString());
				}
				 */

				/*
				 * Save this newly added item to database
				 */
				ItemData itemData = itemDataBuilder.build();
				itemDatabaseHelper.saveRecord(itemData);
				
				// return to previous activity
				finish();
			}
		});
		
		buttonDiscard = (Button) findViewById(R.id.add_item_btn_discard);
		buttonDiscard.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				deleteImageOnSd();
				finish(); // go back to previous activity
			}
			
		});
		
		buttonAddImage = (Button) findViewById(R.id.add_item_value_image_link);
		buttonAddImage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (Environment.MEDIA_MOUNTED.equals(Environment
						.getExternalStorageState())) {
					launchCameraIntent();
				} else {
					Toast.makeText(context, 
							R.string.add_item_message_no_external_storage, 
							Toast.LENGTH_SHORT)
							.show();	
				}
			}
		});
	}
	
	/*
	 * Delete the image that was taken AND stored on SD card. 
	 */
	private void deleteImageOnSd() {
		if (null != imagePath) { // only delete if the image was taken
			Log.d(LOG_TAG, "Search for " + imagePath.toString());
			// File file = new File(imagePath.toString()) ALDBG doesn't work,
			// don't know detail
			File file = new File(URI.create(imagePath.toString()));
			if (file.exists()) { // only delete if the image was stored on SD
									// card
				Log.d(LOG_TAG, imagePath.toString() + " exists");
				/*
				 * ALDBG follows stackoverFlow but doesn't work
				 * sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
				 * Uri.parse(imagePath.toString())));
				 */
				if (file.delete()) {
					Log.d(LOG_TAG,
							"Successfully to delete file "
									+ imagePath.toString());
				} else {
					Log.d(LOG_TAG, "Fail delete file " + imagePath.toString());
				}
			} else {
				Log.d(LOG_TAG, "Cannot find file " + imagePath.toString());
			}
		}
	}

	private void launchCameraIntent() {
		
		// TODO - Create a new intent to launch the MediaStore, Image capture function
		// Hint: use standard Intent from MediaStore class
		// See: http://developer.android.com/reference/android/provider/MediaStore.html
		Intent i1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		
		// TODO - Set the imagePath for this image file using the pre-made function
		// getOutputMediaFile to create a new filename for this specific image;
		//ALDBG we must use the whole getOutputMediaFileUri and Uri here, otherwise, image will not be displayed File newFile = getOutputMediaFile(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE);
		Uri newFileUri = getOutputMediaFileUri(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE);

		
		// TODO - Add the filename to the Intent as an extra. Use the Intent-extra name
		// from the MediaStore class, EXTRA_OUTPUT
		i1.putExtra(MediaStore.EXTRA_OUTPUT, newFileUri); // ALDBG this will not be used in CreateStoryFragment.java due to some bug - imagePath is never assigned a value
		//ALDBG we must use the whole Uri, otherwise, image will not be displayed  fragment.imagePath = Uri.parse(newFile.getPath());
		imagePath = newFileUri;
		
		
		// TODO - Start a new activity for result, using the new intent and the request
		// code CAMERA_PIC_REQUEST
		startActivityForResult(i1, AddItemActivity.CAMERA_PIC_REQUEST);
		// ALDBG should  we finish activity here finish();
	}
	
	private static Uri getOutputMediaFileUri(int type) {
		return Uri.fromFile(getOutputMediaFile(type));
	}
	
	private static File getOutputMediaFile(int type) {
		Log.d(LOG_TAG, "getOutputMediaFile() type:" + type);
		// To be safe, you should check that the SDCard is mounted
		// using Environment.getExternalStorageState() before doing this.

		// For future implementation: store videos in a separate directory
		File mediaStorageDir = new File(
				Environment
						.getExternalStorageDirectory(),
				"ClosetStylist");
		// This location works best if you want the created images to be shared
		// between applications and persist after your app has been uninstalled.

		// Create the storage directory if it does not exist
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				Log.d("MyCameraApp", "failed to create directory");
				return null;
			}
		}

		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US)
				.format(new Date());
		File mediaFile;
		if (type == MEDIA_TYPE_IMAGE) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator
					+ "IMG_" + timeStamp + ".jpg");
		} else {
			Log.e(LOG_TAG, "typ of media file not supported: type was:" + type);
			return null;
		}

		return mediaFile;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d(LOG_TAG, "CreateFragment onActivtyResult called. requestCode: "
				+ requestCode + " resultCode:" + resultCode + "data:" + data);
		if (requestCode == AddItemActivity.CAMERA_PIC_REQUEST) {
			if (resultCode == AddItemActivity.RESULT_OK) {
				// Image captured and saved to fileUri specified in the Intent
				imagePathFinal = imagePath;
				imageLocation.setText(imagePathFinal.toString());
				image.setImageURI(imagePathFinal);
			} else if (resultCode == AddItemActivity.RESULT_CANCELED) {
				// User cancelled the image capture
			} else {
				// Image capture failed, advise user
			}
		}
	}


}

