package com.example.ui;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.closetstylist.ItemDatabaseHelper;
import com.example.closetstylist.PlaceRecord;
import com.example.closetstylist.PostalCodeToLocationTask;
import com.example.closetstylist.R;

public class RegisterActivity extends Activity {
	private final static String LOG_TAG = RegisterActivity.class.getCanonicalName();
	
	private Context context = null;
	private ItemDatabaseHelper itemDatabaseHelper = null;

	private Spinner gender;
	private EditText zip;
	private EditText lng;
	private EditText lat;
	private Button buttonLoc;
	private Button buttonRegister;
	private PlaceRecord place;
	private Location location;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		
		context = getApplicationContext();
		
		gender = (Spinner) findViewById(R.id.register_spinner_gender);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, 
				R.array.register_spinner_gender, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		gender.setAdapter(adapter);
		
		zip = (EditText) findViewById(R.id.register_value_zip);
		lng = (EditText) findViewById(R.id.register_value_longtitude);
		lat = (EditText) findViewById(R.id.register_value_latitude);
		
		buttonLoc = (Button) findViewById(R.id.register_btn_location_from_zip);
		buttonLoc.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				place = null;
				String zipCode = zip.getText().toString();
				if (zipCode.equalsIgnoreCase("")) {
					zipCode = "78758";
				}
				new PostalCodeToLocationTask(RegisterActivity.this)
						.execute(Integer.parseInt(zip.getText().toString()));
			}
		});
		
		buttonRegister = (Button) findViewById(R.id.register_btn_register);
		buttonRegister.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		});
	}
	
	public void setPlaceRecord(PlaceRecord place) {
		this.place = place;
		lng.setText(String.valueOf(place.getLocation().getLongitude()));
		lat.setText(String.valueOf(place.getLocation().getLatitude()));
	}
}
