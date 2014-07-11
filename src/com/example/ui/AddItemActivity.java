package com.example.ui;

import java.io.File;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.example.closetstylist.ImageSubSampler;
import com.example.closetstylist.ItemData;
import com.example.closetstylist.ItemDatabaseHelper;
import com.example.closetstylist.R;
import com.example.closetstylist.ItemData.ItemDataBuilder;
import com.example.closetstylist.R.id;
import com.example.closetstylist.R.layout;
import com.example.closetstylist.R.string;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
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
	private ItemDatabaseHelper itemDatabaseHelper = null;
	private Context context = null;
	
	public static final int MEDIA_TYPE_IMAGE = 1;
	
	static final int CAMERA_PIC_REQUEST = 1;
	static final int CROP_FROM_CAMERA = 2;
	
	private Uri imagePath = null;
	private Uri cropImagePath = null;
	private TextView imageLocation = null;
	private TextView cropImageLocation = null;
	private Button buttonReset = null;
	private Button buttonRegister = null;
	private Button buttonDiscard = null;
	private EditText name = null;
	private EditText description = null;
	private ImageView image = null;
	private ImageView cropImage = null;
	private Button buttonAddImage = null;
	private Spinner color = null;
	private Spinner tempMin = null;
	private Spinner tempMax = null;
	private Spinner category = null;
	private Spinner brand = null;
	private Spinner age = null;
	private Spinner material = null;
	private Spinner style = null;
	private ArrayList<String> colorArray = null;
	private ArrayList<String> temperatureArray = null;
	private ArrayList<String> categoryArray = null;
	private ArrayList<String> brandArray = null;
	private ArrayList<String> ageArray = null;
	private ArrayList<String> materialArray = null;
	private ArrayList<String> styleArray = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_item);
		
		context = getApplicationContext();
		itemDatabaseHelper = new ItemDatabaseHelper(this);
		
		imageLocation = (TextView) findViewById(
				R.id.add_item_value_image_location);
		cropImageLocation = (TextView) findViewById(
				R.id.add_item_value_crop_image_location);
		name = (EditText) findViewById(R.id.add_item_value_name);
		description = (EditText) findViewById(R.id.add_item_value_description);
		image = (ImageView) findViewById(R.id.add_item_image);
		cropImage = (ImageView) findViewById(R.id.add_item_crop_image);
		
		colorArray = ItemData.getColorArray();
		color = (Spinner) findViewById(R.id.add_item_spinner_color);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<String> colorAdapter = new ArrayAdapter<String>(this,
		        R.layout.color_dropdown_item, colorArray);
		// Apply the adapter to the spinner
		color.setAdapter(colorAdapter);
		
		temperatureArray = ItemData.getTemperatureArray();

		tempMin = (Spinner) findViewById(R.id.add_item_spinner_temp_min);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<String> tempMinAdapter = new ArrayAdapter<String>(this,
		        R.layout.temperature_dropdown_item, temperatureArray);
		// Apply the adapter to the spinner
		tempMin.setAdapter(tempMinAdapter);
		tempMin.setSelection(80);
		
		tempMax = (Spinner) findViewById(R.id.add_item_spinner_temp_max);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<String> tempMaxAdapter = new ArrayAdapter<String>(this,
		        R.layout.temperature_dropdown_item, temperatureArray);
		// Apply the adapter to the spinner
		tempMax.setAdapter(tempMaxAdapter);
		tempMax.setSelection(120);

		categoryArray = ItemData.getCategoryArray();
		category = (Spinner) findViewById(R.id.add_item_spinner_category);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(this,
		        R.layout.temperature_dropdown_item, categoryArray);
		// Apply the adapter to the spinner
		category.setAdapter(categoryAdapter);
		
		brandArray = ItemData.getBrandArray();
		brand = (Spinner) findViewById(R.id.add_item_spinner_brand);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<String> brandAdapter = new ArrayAdapter<String>(this,
		        R.layout.temperature_dropdown_item, brandArray);
		// Apply the adapter to the spinner
		brand.setAdapter(brandAdapter);

		ageArray = ItemData.getAgeArray();
		age = (Spinner) findViewById(R.id.add_item_spinner_age);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<String> ageAdapter = new ArrayAdapter<String>(this,
		        R.layout.temperature_dropdown_item, ageArray);
		// Apply the adapter to the spinner
		age.setAdapter(ageAdapter);

		materialArray = ItemData.getMaterialArray();
		material = (Spinner) findViewById(R.id.add_item_spinner_material);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<String> materialAdapter = new ArrayAdapter<String>(this,
		        R.layout.temperature_dropdown_item, materialArray);
		// Apply the adapter to the spinner
		material.setAdapter(materialAdapter);

		styleArray = ItemData.getStyleArray();
		style = (Spinner) findViewById(R.id.add_item_spinner_style);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<String> styleAdapter = new ArrayAdapter<String>(this,
		        R.layout.temperature_dropdown_item, styleArray);
		// Apply the adapter to the spinner
		style.setAdapter(styleAdapter);

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
				 * Make sure all the required fields are valid 
				 */
				
				// image field cannot be null
				if (null == imagePath) {
					Toast.makeText(context, 
							R.string.add_item_message_no_image, 
							Toast.LENGTH_SHORT)
							.show();
					return;
				}
				
				// tempMax >= tempMin
				if (Integer.valueOf(tempMax.getSelectedItem().toString()) 
						< Integer.valueOf(tempMin.getSelectedItem().toString())) {
					Toast.makeText(context, 
							R.string.add_item_message_tempMax_smaller_tempMin, 
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
						category.getSelectedItem().toString(),
						cropImagePath.toString())
						.brand(brand.getSelectedItem().toString())
						.age(Double.valueOf(age.getSelectedItem().toString()))
						.material(material.getSelectedItem().toString())
						.style(style.getSelectedItem().toString());
				
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
				 * Create a new intent containing an ItemData with all the 
				 * information and send it back to MyCloset activity. 
				 */
				Intent i1 = new Intent();
				i1.putExtra(ItemData.INTENT, itemDataBuilder.build());
				
				// Set Activity's result with result code RESULT_OK
				setResult(Activity.RESULT_OK, i1);
				
				// Finish the Activity
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
			
			if (null != cropImagePath) { // delete cropped image
				Log.d(LOG_TAG, "Search for " + cropImagePath.toString());
				file = new File(URI.create(cropImagePath.toString()));
				if (file.exists()) { // only delete if the image was stored on SD
										// card
					Log.d(LOG_TAG, cropImagePath.toString() + " exists");
					/*
					 * ALDBG follows stackoverFlow but doesn't work
					 * sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
					 * Uri.parse(imagePath.toString())));
					 */
					if (file.delete()) {
						Log.d(LOG_TAG,
								"Successfully to delete file "
										+ cropImagePath.toString());
					} else {
						Log.d(LOG_TAG, "Fail delete file " + cropImagePath.toString());
					}
				} else {
					Log.d(LOG_TAG, "Cannot find file " + cropImagePath.toString());
				}
				
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
		Uri newFileUri = getOutputMediaFileUri(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE, false);

		
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
	
	protected static Uri getOutputMediaFileUri(int type, boolean isCrop) {
		return Uri.fromFile(getOutputMediaFile(type, isCrop));
	}
	
	private static File getOutputMediaFile(int type, boolean isCrop) {
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
			if (isCrop) {
				mediaFile = new File(mediaStorageDir.getPath() + File.separator
						+ "CROP_IMG_" + timeStamp + ".jpg");				
			} else {
				mediaFile = new File(mediaStorageDir.getPath() + File.separator
						+ "IMG_" + timeStamp + ".jpg");				
			}
		} else {
			Log.e(LOG_TAG, "typ of media file not supported: type was:" + type);
			return null;
		}

		return mediaFile;
	}
	
	protected static void deleteItemImagesFromSd(ItemData item) {
		if (!item.getImageLink().isEmpty()) { // delete if the image exists
			Log.d(LOG_TAG, "Search for " + item.getImageLink());
			// File file = new File(imagePath.toString()) ALDBG doesn't work,
			// don't know detail
			File file = new File(URI.create(item.getImageLink()));
			if (file.exists()) { // only delete if the image was stored on SD
									// card
				Log.d(LOG_TAG, item.getImageLink() + " exists");
				/*
				 * ALDBG follows stackoverFlow but doesn't work
				 * sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
				 * Uri.parse(imagePath.toString())));
				 */
				if (file.delete()) {
					Log.d(LOG_TAG,
							"Successfully to delete file "
									+ item.getImageLink());
				} else {
					Log.d(LOG_TAG, "Fail delete file " + item.getImageLink());
				}
			} else {
				Log.d(LOG_TAG, "Cannot find file " + item.getImageLink());
			}
		}

		if (!item.getCropImageLink().isEmpty()) { // delete if the cropped image exists
			Log.d(LOG_TAG, "Search for " + item.getCropImageLink());
			File file = new File(URI.create(item.getCropImageLink()));
			if (file.exists()) { // only delete if the image was stored on SD
				// card
				Log.d(LOG_TAG, item.getCropImageLink() + " exists");
				/*
				 * ALDBG follows stackoverFlow but doesn't work
				 * sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
				 * Uri.parse(imagePath.toString())));
				 */
				if (file.delete()) {
					Log.d(LOG_TAG,
							"Successfully to delete file "
									+ item.getCropImageLink());
				} else {
					Log.d(LOG_TAG, "Fail delete file " + item.getCropImageLink());
				}
			} else {
				Log.d(LOG_TAG, "Cannot find file " + item.getCropImageLink());
			}

		}
	}

	protected static void deleteItemOriginalImageFromSd(ItemData item) {
		if (!item.getImageLink().isEmpty()) { // delete if the image exists
			Log.d(LOG_TAG, "Search for " + item.getImageLink());
			// File file = new File(imagePath.toString()) ALDBG doesn't work,
			// don't know detail
			File file = new File(URI.create(item.getImageLink()));
			if (file.exists()) { // only delete if the image was stored on SD
									// card
				Log.d(LOG_TAG, item.getImageLink() + " exists");
				/*
				 * ALDBG follows stackoverFlow but doesn't work
				 * sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
				 * Uri.parse(imagePath.toString())));
				 */
				if (file.delete()) {
					Log.d(LOG_TAG,
							"Successfully to delete file "
									+ item.getImageLink());
				} else {
					Log.d(LOG_TAG, "Fail delete file " + item.getImageLink());
				}
			} else {
				Log.d(LOG_TAG, "Cannot find file " + item.getImageLink());
			}
		}
	}
	
	protected static void deleteItemCropImageFromSd(ItemData item) {
		if (!item.getCropImageLink().isEmpty()) { // delete if the cropped image exists
			Log.d(LOG_TAG, "Search for " + item.getCropImageLink());
			File file = new File(URI.create(item.getCropImageLink()));
			if (file.exists()) { // only delete if the image was stored on SD
				// card
				Log.d(LOG_TAG, item.getCropImageLink() + " exists");
				/*
				 * ALDBG follows stackoverFlow but doesn't work
				 * sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
				 * Uri.parse(imagePath.toString())));
				 */
				if (file.delete()) {
					Log.d(LOG_TAG,
							"Successfully to delete file "
									+ item.getCropImageLink());
				} else {
					Log.d(LOG_TAG, "Fail delete file " + item.getCropImageLink());
				}
			} else {
				Log.d(LOG_TAG, "Cannot find file " + item.getCropImageLink());
			}
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d(LOG_TAG, "CreateFragment onActivtyResult called. requestCode: "
				+ requestCode + " resultCode:" + resultCode + "data:" + data);
		// create a temporary ItemData to display picture and avoid OutOfMemoryError
		ItemData.ItemDataBuilder itemDataBuilder = null;
		if (AddItemActivity.CAMERA_PIC_REQUEST == requestCode) {
			if (Activity.RESULT_OK == resultCode) {
				// Image captured and saved to fileUri specified in the Intent
				imageLocation.setText(imagePath.toString());
				//image.setImageURI(imagePath);
				/*
				 * Create a temporary a ItemData.ItemDataBuilder
				 */
				itemDataBuilder = new ItemData.ItemDataBuilder(
						imagePath.toString(), 
						color.getSelectedItem().toString(), 
						Integer.valueOf(tempMin.getSelectedItem().toString()), 
						Integer.valueOf(tempMax.getSelectedItem().toString()), 
						category.getSelectedItem().toString(),
						null)//cropImagePath.toString())
						.brand(brand.getSelectedItem().toString())
						.age(Double.valueOf(age.getSelectedItem().toString()))
						.material(material.getSelectedItem().toString())
						.style(style.getSelectedItem().toString());
				if (!name.getText().toString().isEmpty()) {
					itemDataBuilder.name(name.getText().toString());
				}
				if (!description.getText().toString().isEmpty()) {
					itemDataBuilder.description(description.getText().toString());
				}
				new ImageSubSampler(context).subSampleOriginalUri(itemDataBuilder.build(), image, context);

				launchCropIntent();
			} else if (resultCode == AddItemActivity.RESULT_CANCELED) {
				// User cancelled the image capture
			} else {
				// Image capture failed, advise user
			}
		} else if (AddItemActivity.CROP_FROM_CAMERA == requestCode) {
			if (resultCode == AddItemActivity.RESULT_OK) {
				cropImageLocation.setText(cropImagePath.toString());
				//cropImage.setImageURI(cropImagePath);
				/*
				 * Create a temporary a ItemData.ItemDataBuilder
				 */
				itemDataBuilder = new ItemData.ItemDataBuilder(
						imagePath.toString(), 
						color.getSelectedItem().toString(), 
						Integer.valueOf(tempMin.getSelectedItem().toString()), 
						Integer.valueOf(tempMax.getSelectedItem().toString()), 
						category.getSelectedItem().toString(),
						cropImagePath.toString())
						.brand(brand.getSelectedItem().toString())
						.age(Double.valueOf(age.getSelectedItem().toString()))
						.material(material.getSelectedItem().toString())
						.style(style.getSelectedItem().toString());
				if (!name.getText().toString().isEmpty()) {
					itemDataBuilder.name(name.getText().toString());
				}
				if (!description.getText().toString().isEmpty()) {
					itemDataBuilder.description(description.getText().toString());
				}
				new ImageSubSampler(context).subSampleCroppedUri(itemDataBuilder.build(), cropImage, context);
			}
		}
	}
	
	private void launchCropIntent() {
    	Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setType("image/*");
        
        List<ResolveInfo> list = getPackageManager().queryIntentActivities( intent, 0 );
        
        int size = list.size();
        
        if (size == 0) {	        
        	Toast.makeText(this, "Can not find image crop app", Toast.LENGTH_SHORT).show();
        	
            return;
        } else {
    		// generate a new Uri for the new cropped image
        	cropImagePath = getOutputMediaFileUri(
    				MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE, true);
        	
        	intent.setData(imagePath) // set the input to the picture taken by camera
        	.putExtra("outputX", 150) // equal to R.dimen.crop_bottom_width
        	.putExtra("outputY", 150) // equal to R.dimen.crop_bottom_height
        	.putExtra("aspectX", 1)
        	.putExtra("aspectY", 1)
        	.putExtra("scale", true)
        	.putExtra("return-data", false) // don't return bitmap data
        	.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, cropImagePath); // set the name of the output cropped image
        	
        	// Create the following intent to avoid the following  error 
        	// No Activity found to handle Intent { act=... (has extras) }
    		Intent i = new Intent(intent);
        	ResolveInfo res	= list.get(0); // ALDBG assume the first one is camera crop       	
        	i.setComponent( new ComponentName(res.activityInfo.packageName, 
        			res.activityInfo.name));        	
        	startActivityForResult(i, CROP_FROM_CAMERA);
        }
	}


}

