package com.example.ui;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.text.format.DateUtils;

import com.example.closetstylist.ItemDatabaseHelper;
import com.example.closetstylist.LocationToPostalCodeTask;
import com.example.closetstylist.PlaceRecord;
import com.example.closetstylist.PostalCodeToLocationTask;
import com.example.closetstylist.R;
import com.example.closetstylist.MockLocationProvider;

/*
 * Modified PlaceViewActivity in ContentProviderLab
 */
public class RegisterActivity extends Activity implements LocationListener {
	private final static String LOG_TAG = RegisterActivity.class.getCanonicalName();
	
	private Context context = null;
	private ItemDatabaseHelper itemDatabaseHelper = null;

	private Spinner gender;
	private EditText zip;
	private EditText lng;
	private EditText lat;
	private Button buttonLocationFromZip;
	private Button buttonLoc;
	private Button buttonRegister;
	private PlaceRecord place;
	private Location location;
	private LocationManager mLocationManager;
	private Location mLastLocationReading;
	// A fake location provider used for testing, used with mMockLocationProvider.pushLocation(37.422, -122.084);
	private MockLocationProvider mMockLocationProvider;
	// default minimum time between new location readings
	private long mMinTime = 5000;
	// default minimum distance between old and new readings.
	private float mMinDistance = 1000.0f;
	private final long FIVE_MINS = 5 * 60 * 1000;
	
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
		
		buttonLocationFromZip = (Button) findViewById(R.id.register_btn_location_from_zip);
		buttonLocationFromZip.setOnClickListener(new OnClickListener() {
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

		buttonLoc = (Button) findViewById(R.id.register_btn_zip_from_location);
		buttonLoc.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				place = null;
				String zipCode = zip.getText().toString();
				if (zipCode.equalsIgnoreCase("")) {
					zipCode = "78758";
				}
				if (null != mLastLocationReading) {
					new LocationToPostalCodeTask(RegisterActivity.this)
					.execute(mLastLocationReading);					
				}
			}
		});

		buttonRegister = (Button) findViewById(R.id.register_btn_register);
		buttonRegister.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		});
	}
	
	@Override
	protected void onResume() {
		super.onResume();

		mMockLocationProvider = new MockLocationProvider(
				LocationManager.NETWORK_PROVIDER, this);

		// Check NETWORK_PROVIDER for an existing location reading.
		// Only keep this last reading if it is NOT obtained today.
		mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		List<String> matchingProviders = mLocationManager.getAllProviders();
		Location newLocation = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		for (String provider : matchingProviders) {
			newLocation = mLocationManager.getLastKnownLocation(provider);
			if (null != newLocation) {
				break;
			}
		}
		
		if (null != newLocation) {
			if ((null == mLastLocationReading) || ((null != mLastLocationReading) 
					&& (!DateUtils.isToday(mLastLocationReading.getTime())))) {
				mLastLocationReading = newLocation;
			}
			
		}
		
		// Register to receive location updates from NETWORK_PROVIDER
		mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, mMinTime, mMinDistance, this);
	}

	@Override
	protected void onPause() {
		mMockLocationProvider.shutdown();
		// Unregister for location updates
		mLocationManager.removeUpdates(this);
		super.onPause();
	}

	@Override
	public void onLocationChanged(Location currentLocation) {
		// Handle location updates
		// Cases to consider
		// 1) If there is no last location, keep the current location.
		// 2) If the current location is older than the last location, ignore
		// the current location
		// 3) If the current location is newer than the last locations, keep the
		// current location.
		if (null == mLastLocationReading) {
			mLastLocationReading = currentLocation; // case 1
		} else {
			if (age(currentLocation) < age(mLastLocationReading)) {
				mLastLocationReading = currentLocation; // case 2
			}
		}
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}
	
	public void setPlaceRecord(PlaceRecord place) {
		if (null != place) {
			this.place = place;
			lng.setText(String.valueOf(place.getLocation().getLongitude()));
			lat.setText(String.valueOf(place.getLocation().getLatitude()));
			zip.setText(String.valueOf(place.getPostalCode()));
		}
	}

	private long age(Location location) {
		return System.currentTimeMillis() - location.getTime();
	}
}
