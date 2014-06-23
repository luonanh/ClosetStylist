package com.example.closetstylist;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
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

public class EditItemActivity extends Activity {
	private final static String LOG_TAG = EditItemActivity.class.getCanonicalName();

	private ItemDatabaseHelper itemDatabaseHelper = null;
	private ItemData itemData = null;
	private Context context = null;

	static final int CAMERA_PIC_REQUEST = 1;
	static final int CROP_FROM_CAMERA = 2;
	static final int EDIT_FROM_FILE = 3;
	static final int PICK_FROM_GALLERY = 4;
	static final int CROP_FROM_GALLERY = 5;

	private Uri newImagePath = null;
	private Uri newCropImagePath = null;
	private Uri newEditImagePath = null;
	private Uri newGalleryImagePath = null;
	private Uri newCropGalleryImagePath = null;
	private CropImageStatus cropStatus = CropImageStatus.NO_CHANGE;
	private TextView imageLocation = null;
	private TextView cropImageLocation = null;
	private Button buttonReset = null;
	private Button buttonSave = null;
	private Button buttonNewImage = null;
	private EditText name = null;
	private EditText description = null;
	private ImageView image = null;
	private ImageView cropImage = null;
	private Button buttonEditImage = null;
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
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_item);

		context = getApplicationContext();

		// Restore ItemData passed in by MyCloset activity
		itemDatabaseHelper = new ItemDatabaseHelper(this);
		itemData = getIntent().getExtras().getParcelable(ItemData.INTENT);
		Log.i(LOG_TAG, itemData.toString());

		imageLocation = (TextView) findViewById(
				R.id.edit_item_value_image_location);
		cropImageLocation = (TextView) findViewById(
				R.id.edit_item_value_crop_image_location);
		name = (EditText) findViewById(R.id.edit_item_value_name);
		description = (EditText) findViewById(R.id.edit_item_value_description);
		image = (ImageView) findViewById(R.id.edit_item_image);
		cropImage = (ImageView) findViewById(R.id.edit_item_crop_image);
		
		colorArray = ItemData.getColorArray();

		color = (Spinner) findViewById(R.id.edit_item_spinner_color);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<String> colorAdapter = new ArrayAdapter<String>(this,
				R.layout.color_dropdown_item, colorArray);
		// Apply the adapter to the spinner
		color.setAdapter(colorAdapter);

		temperatureArray = ItemData.getTemperatureArray();

		tempMin = (Spinner) findViewById(R.id.edit_item_spinner_temp_min);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<String> tempMinAdapter = new ArrayAdapter<String>(this,
				R.layout.temperature_dropdown_item, temperatureArray);
		// Apply the adapter to the spinner
		tempMin.setAdapter(tempMinAdapter);
		tempMin.setSelection(80);

		tempMax = (Spinner) findViewById(R.id.edit_item_spinner_temp_max);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<String> tempMaxAdapter = new ArrayAdapter<String>(this,
				R.layout.temperature_dropdown_item, temperatureArray);
		// Apply the adapter to the spinner
		tempMax.setAdapter(tempMaxAdapter);
		tempMax.setSelection(120);

		categoryArray = ItemData.getCategoryArray();
		category = (Spinner) findViewById(R.id.edit_item_spinner_category);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(this,
				R.layout.temperature_dropdown_item, categoryArray);
		// Apply the adapter to the spinner
		category.setAdapter(categoryAdapter);

		brandArray = ItemData.getBrandArray();
		brand = (Spinner) findViewById(R.id.edit_item_spinner_brand);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<String> brandAdapter = new ArrayAdapter<String>(this,
				R.layout.temperature_dropdown_item, brandArray);
		// Apply the adapter to the spinner
		brand.setAdapter(brandAdapter);

		ageArray = ItemData.getAgeArray();
		age = (Spinner) findViewById(R.id.edit_item_spinner_age);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<String> ageAdapter = new ArrayAdapter<String>(this,
				R.layout.temperature_dropdown_item, ageArray);
		// Apply the adapter to the spinner
		age.setAdapter(ageAdapter);

		materialArray = ItemData.getMaterialArray();
		material = (Spinner) findViewById(R.id.edit_item_spinner_material);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<String> materialAdapter = new ArrayAdapter<String>(this,
				R.layout.temperature_dropdown_item, materialArray);
		// Apply the adapter to the spinner
		material.setAdapter(materialAdapter);

		styleArray = ItemData.getStyleArray();
		style = (Spinner) findViewById(R.id.edit_item_spinner_style);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<String> styleAdapter = new ArrayAdapter<String>(this,
				R.layout.temperature_dropdown_item, styleArray);
		// Apply the adapter to the spinner
		style.setAdapter(styleAdapter);

		// Initialize temporary variables
		newImagePath = null;
		newCropImagePath = null;
		newEditImagePath = null;
		newGalleryImagePath = null;
		newCropGalleryImagePath = null;
		cropStatus = CropImageStatus.NO_CHANGE;

		setEditItemActivityFromItemData(itemData);

		// Create dialog for NewImage button
		final String [] items			= new String [] {"Take from camera", "Select from gallery"};				
		ArrayAdapter<String> adapter	= new ArrayAdapter<String> (this, android.R.layout.select_dialog_item,items);
		AlertDialog.Builder builder		= new AlertDialog.Builder(this);
		builder.setTitle("Select Image");
		builder.setAdapter( adapter, new DialogInterface.OnClickListener() {
			public void onClick( DialogInterface dialog, int item ) { //pick from camera
				if (item == 0) {
					if (Environment.MEDIA_MOUNTED.equals(Environment
							.getExternalStorageState())) {
						launchCameraIntent();
					} else {
						Toast.makeText(context, 
								R.string.edit_item_message_no_external_storage, 
								Toast.LENGTH_SHORT)
								.show();	
					}
				} else { // pick from gallery
					launchGalleryIntent();
				}
			}
		} );

		final AlertDialog dialog = builder.create();

		buttonReset = (Button) findViewById(R.id.edit_item_btn_reset);
		buttonReset.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setEditItemActivityFromItemData(itemData);
			}	
		});

		buttonNewImage = (Button) findViewById(R.id.edit_item_btn_new_image);
		buttonNewImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.show();
			}

		});

		buttonEditImage = (Button) findViewById(R.id.edit_item_btn_edit_image);
		buttonEditImage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (Environment.MEDIA_MOUNTED.equals(Environment
						.getExternalStorageState())) {
					launchEditIntent();
				} else {
					Toast.makeText(context, 
							R.string.edit_item_message_no_external_storage, 
							Toast.LENGTH_SHORT)
							.show();	
				}
			}
		});

		buttonSave = (Button) findViewById(R.id.edit_item_btn_save);
		buttonSave.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// create a new ItemData based on the current fields
				// newItemData = new ItemData.ItemDataBuilder...id(itemData.getId())
				if (Integer.valueOf(tempMax.getSelectedItem().toString()) 
						< Integer.valueOf(tempMin.getSelectedItem().toString())) {
					Toast.makeText(context, 
							R.string.edit_item_message_tempMax_smaller_tempMin, 
							Toast.LENGTH_SHORT)
							.show();
					return;					
				}

				//if ((null != newImagePath) && (isNewImagePathValid)) {
				if (null != newImagePath) {
					deleteFileIfExist(Uri.parse(itemData.getImageLink()));
					itemData.setImageLink(newImagePath.toString());
					newImagePath = null; // so that onBackPressed doesn't delete this
				}

				//if ((null != newCropImagePath) && (isNewCropImagePathValid)) {
				if (null != newCropImagePath) {
					if (CropImageStatus.NEW_IMAGE_NEWER == cropStatus) {
						deleteFileIfExist(Uri.parse(itemData.getCropImageLink()));
						itemData.setCropImageLink(newCropImagePath.toString());
						newCropImagePath = null; // so that onBackPressed doesn't delete this						
					}
				}

				if (null != newEditImagePath) {
					if (CropImageStatus.EDIT_IMAGE_NEWER == cropStatus) {
						deleteFileIfExist(Uri.parse(itemData.getCropImageLink()));
						itemData.setCropImageLink(newEditImagePath.toString());
						newEditImagePath = null; // so that onBackPressed doesn't delete this												
					}
				}
				
				if (null != newCropGalleryImagePath) {
					if (CropImageStatus.NEW_IMAGE_NEWER == cropStatus) {
						if (null != newGalleryImagePath) {
							deleteFileIfExist(Uri.parse(itemData.getImageLink()));
							itemData.setImageLink(newGalleryImagePath.toString());							
						}
						deleteFileIfExist(Uri.parse(itemData.getCropImageLink()));
						itemData.setCropImageLink(newCropGalleryImagePath.toString());
						newCropGalleryImagePath = null; // so that onBackPressed doesn't delete this												
					}
				}

				/*
				 * Create a new ItemData instance.
				 * Hard code color, tempMin, tempMax, category for now
				 */
				ItemData.ItemDataBuilder itemDataBuilder = new ItemData.ItemDataBuilder(
						itemData.getImageLink(), 
						color.getSelectedItem().toString(), 
						Integer.valueOf(tempMin.getSelectedItem().toString()), 
						Integer.valueOf(tempMax.getSelectedItem().toString()), 
						category.getSelectedItem().toString(),
						itemData.getCropImageLink())
				.id(itemData.getId())
				.brand(brand.getSelectedItem().toString())
				.age(Double.valueOf(age.getSelectedItem().toString()))
				.material(material.getSelectedItem().toString())
				.style(style.getSelectedItem().toString())
				.name(name.getText().toString())
				.description(description.getText().toString());
				itemData = itemDataBuilder.build();
				Log.i(LOG_TAG, "buttonSave" + itemData.toString());

				// update the database with the newly create ItemData
				itemDatabaseHelper.updateItemDataRecord(itemData);

				onBackPressed(); // same as hit back key
			}	
		});

	}
	
	private void launchGalleryIntent() {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(intent, PICK_FROM_GALLERY);
	}	

	private void launchCameraIntent() {
		Intent i1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		newImagePath = AddItemActivity.getOutputMediaFileUri(
				MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE, false);
		i1.putExtra(MediaStore.EXTRA_OUTPUT, newImagePath);
		startActivityForResult(i1, EditItemActivity.CAMERA_PIC_REQUEST);
	}

	private void setEditItemActivityFromItemData(ItemData item) {
		name.setText(item.getName());
		description.setText(item.getDescription());
		imageLocation.setText(item.getImageLink());
		//image.setImageURI(Uri.parse(item.getImageLink()));
		new ImageSubSampler(context).subSampleOriginalUri(itemData, image, context);
		cropImageLocation.setText(item.getCropImageLink());
		//cropImage.setImageURI(Uri.parse(item.getCropImageLink()));
		new ImageSubSampler(context).subSampleCroppedUri(itemData, cropImage, context);
		//color.setSelection(colorAdapter.getPosition(itemData.getColor()));
		color.setSelection(colorArray.indexOf(item.getColor()));
		tempMin.setSelection(temperatureArray.indexOf(Integer.toString(item.getTempMin())));
		category.setSelection(categoryArray.indexOf(item.getCategory()));
		brand.setSelection(brandArray.indexOf(item.getBrand()));
		age.setSelection(ageArray.indexOf(Integer.toString((int)item.getAge())));
		material.setSelection(materialArray.indexOf(item.getMaterial()));
		style.setSelection(styleArray.indexOf(item.getStyle()));
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == EditItemActivity.CAMERA_PIC_REQUEST) {
			if (resultCode == Activity.RESULT_OK) {
				// Image captured and saved to fileUri specified in the Intent
				if (null != newImagePath) {
					//image.setImageURI(newImagePath);
					launchCropIntent(CAMERA_PIC_REQUEST);
				}
			} else {
				deleteFileIfExist(newImagePath);
				newImagePath = null;
			}
		} else if (EditItemActivity.CROP_FROM_CAMERA == requestCode) {
			if (resultCode == Activity.RESULT_OK) {
				if (null != newCropImagePath) {
					cropImage.setImageURI(newCropImagePath);
					cropStatus = CropImageStatus.NEW_IMAGE_NEWER;
				}
			} else {
				deleteFileIfExist(newCropImagePath);
				newCropImagePath = null;
			}
		} else if (EditItemActivity.EDIT_FROM_FILE == requestCode) {
			if (resultCode == Activity.RESULT_OK) {
				if (null != newEditImagePath) {
					cropImage.setImageURI(newEditImagePath);
					cropStatus = CropImageStatus.EDIT_IMAGE_NEWER;
				}
			} else {
				deleteFileIfExist(newEditImagePath);
				newEditImagePath = null;
			}
		} else if (EditItemActivity.PICK_FROM_GALLERY == requestCode) {
			if (resultCode == Activity.RESULT_OK) {
				newGalleryImagePath = data.getData();
				//image.setImageURI(newGalleryImagePath);
				launchCropIntent(PICK_FROM_GALLERY);
			}
		} else if (EditItemActivity.CROP_FROM_GALLERY == requestCode) {
			if (resultCode == Activity.RESULT_OK) {
				if (null != newCropGalleryImagePath) {
					cropImage.setImageURI(newCropGalleryImagePath);
					cropStatus = CropImageStatus.NEW_IMAGE_NEWER;
				}
			} else {
				deleteFileIfExist(newCropGalleryImagePath);
				newCropGalleryImagePath = null;
				newGalleryImagePath = null;
			}			
		}
	}

	/*
	 * Helper function to determine if a file exist or not
	 */
	protected static boolean isFileExist(Uri uri) {
		if (null == uri) {
			return false;
		}

		File file = new File(URI.create(uri.toString()));
		if (file.exists()) {
			return true;
		} else {
			return false;
		}
	}

	protected static void deleteFileIfExist(Uri uri) {
		if (null != uri) {
			File file = new File(URI.create(uri.toString()));
			if (file.exists()) {
				file.delete();
			}
		}
	}

	private void launchCropIntent(int type) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setType("image/*");

		List<ResolveInfo> list = getPackageManager().queryIntentActivities( intent, 0 );

		int size = list.size();

		if (size == 0) {	        
			Toast.makeText(this, "Can not find image crop app", Toast.LENGTH_SHORT).show();

			return;
		} else {
			if (PICK_FROM_GALLERY == type) {
				newCropGalleryImagePath = AddItemActivity.getOutputMediaFileUri(
						MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE, true);
				intent.setData(newGalleryImagePath) // set the input to the picture in gallery
				.putExtra("outputX", 150) // equal to R.dimen.crop_bottom_width
				.putExtra("outputY", 150) // equal to R.dimen.crop_bottom_height
				.putExtra("aspectX", 1)
				.putExtra("aspectY", 1)
				.putExtra("scale", true)
				.putExtra("return-data", false) // don't return bitmap data
				.putExtra(MediaStore.EXTRA_OUTPUT, newCropGalleryImagePath); // set the name of the output cropped image
				
				// Create the following intent to avoid the following  error 
				// No Activity found to handle Intent { act=... (has extras) }
				Intent i = new Intent(intent);
				ResolveInfo res	= list.get(0); // ALDBG assume the first one is camera crop       	
				i.setComponent( new ComponentName(res.activityInfo.packageName, 
						res.activityInfo.name));        	
				startActivityForResult(i, CROP_FROM_GALLERY);
			} else if (CAMERA_PIC_REQUEST == type) {
				newCropImagePath = AddItemActivity.getOutputMediaFileUri(
						MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE, true);
				intent.setData(newImagePath) // set the input to the picture taken by camera
				.putExtra("outputX", 150) // equal to R.dimen.crop_bottom_width
				.putExtra("outputY", 150) // equal to R.dimen.crop_bottom_height
				.putExtra("aspectX", 1)
				.putExtra("aspectY", 1)
				.putExtra("scale", true)
				.putExtra("return-data", false) // don't return bitmap data
				.putExtra(MediaStore.EXTRA_OUTPUT, newCropImagePath); // set the name of the output cropped image
				
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

	private void launchEditIntent() {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setType("image/*");

		List<ResolveInfo> list = getPackageManager().queryIntentActivities( intent, 0 );

		int size = list.size();

		if (size == 0) {	        
			Toast.makeText(this, "Can not find image crop app", Toast.LENGTH_SHORT).show();

			return;
		} else {
			newEditImagePath = AddItemActivity.getOutputMediaFileUri(
					MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE, true);
			Uri oldUri = null;
			if (null == newImagePath) {
				oldUri = Uri.parse(itemData.getImageLink()); // user didn't take a new picture
			} else {
				oldUri = newImagePath; // user took a new picture, let's edit the new one
			}
			intent.setData(oldUri) // set the input to the picture taken by camera
			.putExtra("outputX", 150) // equal to R.dimen.crop_bottom_width
			.putExtra("outputY", 150) // equal to R.dimen.crop_bottom_height
			.putExtra("aspectX", 1)
			.putExtra("aspectY", 1)
			.putExtra("scale", true)
			.putExtra("return-data", false) // don't return bitmap data
			.putExtra(MediaStore.EXTRA_OUTPUT, newEditImagePath); // set the name of the output cropped image

			// Create the following intent to avoid the following  error 
			// No Activity found to handle Intent { act=... (has extras) }
			Intent i = new Intent(intent);
			ResolveInfo res	= list.get(0); // ALDBG assume the first one is camera crop       	
			i.setComponent( new ComponentName(res.activityInfo.packageName, 
					res.activityInfo.name));        	
			startActivityForResult(i, EDIT_FROM_FILE);
		}
	}

	@Override
	public void onBackPressed() {
		//if ((null != newImagePath) && (isNewImagePathValid)) {
		if (null != newImagePath) {
			// this mean a new image was taken, but user decides not to save
			deleteFileIfExist(newImagePath);
		}
		newImagePath = null;


		//if ((null != newCropImagePath) && (isNewCropImagePathValid)) {
		if (null != newCropImagePath) {
			deleteFileIfExist(newCropImagePath);
		}
		newCropImagePath = null;

		if (null != newEditImagePath) {
			deleteFileIfExist(newEditImagePath);
		}
		newEditImagePath = null;

		if (null != newCropGalleryImagePath) {
			// this mean a new image was taken, but user decides not to save
			deleteFileIfExist(newCropGalleryImagePath);
		}
		newCropGalleryImagePath = null;
		
		Intent i1 = new Intent();
		i1.putExtra(ItemData.INTENT, itemData);

		// Set Activity's result with result code RESULT_OK
		setResult(Activity.RESULT_OK, i1);

		// Finish the Activity
		finish();
	}

	private enum CropImageStatus {
		NO_CHANGE, NEW_IMAGE_NEWER, EDIT_IMAGE_NEWER
	}
}
