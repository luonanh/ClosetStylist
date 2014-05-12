package com.example.closetstylist;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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

	private Uri imagePath = null;
	private TextView imageLocation = null;
	private Uri imagePathFinal = null;
	private Button buttonReset = null;
	private Button buttonSave = null;
	private EditText name = null;
	private EditText description = null;
	private ImageView image = null;
	private Button buttonEditImage = null;
	private Spinner color = null;
	private Spinner tempMin = null;
	private Spinner tempMax = null;
	private Spinner category = null;
	private Spinner brand = null;
	private Spinner age = null;
	private Spinner material = null;
	private ArrayList<String> colorArray = new ArrayList<String>(Arrays.asList("black", "blue", "brown", "gray", "green", "orange", "pink", "purple", "red", "white", "yellow"));
	private ArrayList<String> temperatureArray = new ArrayList<String>();//new ArrayList<String>(Arrays.asList("10", "11", "12"));
	private ArrayList<String> categoryArray = new ArrayList<String>(Arrays.asList("dress", "jacket", "jeans", "shirt", "short", "t-shirt"));
	private ArrayList<String> brandArray = new ArrayList<String>(Arrays.asList("Banana", "Express", "RalphLauren", "CK", "Adiddas", "Nike"));
	private ArrayList<String> ageArray = new ArrayList<String>();
	private ArrayList<String> materialArray = new ArrayList<String>(Arrays.asList("wool", "cotton", "nylon"));

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
		name = (EditText) findViewById(R.id.edit_item_value_name);
		description = (EditText) findViewById(R.id.edit_item_value_description);
		image = (ImageView) findViewById(R.id.edit_item_image);
		
		color = (Spinner) findViewById(R.id.edit_item_spinner_color);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<String> colorAdapter = new ArrayAdapter<String>(this,
		        R.layout.color_dropdown_item, colorArray);
		// Apply the adapter to the spinner
		color.setAdapter(colorAdapter);
		
		// initialize temperature array for tempMin and tempMax spinner
		for (int i=-30; i<=120; i++) {
			temperatureArray.add(String.valueOf(i));
		}
		
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

		category = (Spinner) findViewById(R.id.edit_item_spinner_category);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(this,
		        R.layout.temperature_dropdown_item, categoryArray);
		// Apply the adapter to the spinner
		category.setAdapter(categoryAdapter);
		
		brand = (Spinner) findViewById(R.id.edit_item_spinner_brand);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<String> brandAdapter = new ArrayAdapter<String>(this,
		        R.layout.temperature_dropdown_item, brandArray);
		// Apply the adapter to the spinner
		brand.setAdapter(brandAdapter);

		// initialize temperature array for tempMin and tempMax spinner
		for (int i=0; i<=20; i++) {
			ageArray.add(String.valueOf(i));
		}
		age = (Spinner) findViewById(R.id.edit_item_spinner_age);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<String> ageAdapter = new ArrayAdapter<String>(this,
		        R.layout.temperature_dropdown_item, ageArray);
		// Apply the adapter to the spinner
		age.setAdapter(ageAdapter);
		

		material = (Spinner) findViewById(R.id.edit_item_spinner_material);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<String> materialAdapter = new ArrayAdapter<String>(this,
		        R.layout.temperature_dropdown_item, materialArray);
		// Apply the adapter to the spinner
		material.setAdapter(materialAdapter);
		
		setEditItemActivityFromItemData(itemData);
		
		buttonReset = (Button) findViewById(R.id.edit_item_btn_reset);
		buttonReset.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setEditItemActivityFromItemData(itemData);
			}	
		});

		buttonEditImage = (Button) findViewById(R.id.edit_item_btn_edit_image);
		buttonEditImage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// start ViewImage activity - should I use another program to edit image
				// save the image to the same file or different file on SD card ???
			}	
		});

		buttonSave = (Button) findViewById(R.id.edit_item_btn_save);
		buttonSave.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// create a new ItemData based on the current fields
				// newItemData = new ItemData.ItemDataBuilder...id(itemData.getId())
				
				/*
				 * Make sure all the required fields are valid 
				 */
				
				// image field cannot be null
				/* itemData must already contain an imagePath
				if (null == imagePath) {
					Toast.makeText(context, 
							R.string.add_item_message_no_image, 
							Toast.LENGTH_SHORT)
							.show();
					return;
				}
				*/
				
				// tempMax >= tempMin
				if (Integer.valueOf(tempMax.getSelectedItem().toString()) 
						< Integer.valueOf(tempMin.getSelectedItem().toString())) {
					Toast.makeText(context, 
							R.string.edit_item_message_tempMax_smaller_tempMin, 
							Toast.LENGTH_SHORT)
							.show();
					return;					
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
						category.getSelectedItem().toString())
						.brand(brand.getSelectedItem().toString())
						.age(Double.valueOf(age.getSelectedItem().toString()))
						.material(material.getSelectedItem().toString())
						.name(name.getText().toString())
						.description(description.getText().toString());
				itemData = itemDataBuilder.build();
				
				// update the database with the newly create ItemData
				itemDatabaseHelper.updateRecord(itemData);
				
				onBackPressed(); // same as hit back key
			}	
		});

	}
	
	private void setEditItemActivityFromItemData(ItemData item) {
		name.setText(item.getName());
		description.setText(item.getDescription());
		imageLocation.setText(item.getImageLink());
		image.setImageURI(Uri.parse(item.getImageLink()));
		//color.setSelection(colorAdapter.getPosition(itemData.getColor()));
		color.setSelection(colorArray.indexOf(item.getColor()));
		tempMin.setSelection(temperatureArray.indexOf(Integer.toString(item.getTempMin())));
		category.setSelection(categoryArray.indexOf(item.getCategory()));
		brand.setSelection(brandArray.indexOf(item.getBrand()));
		age.setSelection(ageArray.indexOf(Double.toString(item.getAge())));
		material.setSelection(materialArray.indexOf(item.getMaterial()));
	}

	@Override
	public void onBackPressed() {
		Intent i1 = new Intent();
		i1.putExtra(ItemData.INTENT, itemData);
		
		// Set Activity's result with result code RESULT_OK
		setResult(Activity.RESULT_OK, i1);
		
		// Finish the Activity
		finish();
	}
	
	
}
