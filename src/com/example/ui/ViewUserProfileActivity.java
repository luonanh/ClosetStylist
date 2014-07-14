package com.example.ui;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.example.closetstylist.ItemDatabaseHelper;
import com.example.closetstylist.R;
import com.example.closetstylist.UserProfile;
import com.example.closetstylist.R.id;
import com.example.closetstylist.R.layout;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ViewUserProfileActivity extends Activity {
	private final static String LOG_TAG = ViewUserProfileActivity.class.getCanonicalName();
	
	private static final long ONE_MIN = 1000 * 60;
	private static final long ONE_HOUR = 1000 * 60 * 60;
	private static final long TWO_MIN = ONE_MIN * 2;
	private static final long FIVE_MIN = ONE_MIN * 5;
	private static final long MEASURE_TIME = 1000 * 30;
	private static final long POLLING_FREQ = 1000 * 10;
	private static final float MIN_ACCURACY = 32186.9f; // 20 miles
	private static final float MIN_LAST_READ_ACCURACY = 32186.9f; // 20 miles
	private static final float MIN_DISTANCE = 80467.2f; // 50 miles
	
	private ItemDatabaseHelper mItemDatabaseHelper = null;
	private Context mContext = null;
	private UserProfile up = null;

	private TextView usr;
	private TextView pwd;
	private TextView gender;
	private TextView zip;
	private TextView schedule;
	private TextView day;
	
	// Current best location estimate
	private Location mBestReading;
	private TextView latLocation;
	private TextView longLocation;
	
	// Reference to the LocationManager and LocationListener
	private LocationManager mLocationManager;
	private LocationListener mLocationListener;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_user_profile);
		
		mContext = getApplicationContext();
		mItemDatabaseHelper = new ItemDatabaseHelper(this);
		up = mItemDatabaseHelper.getCurrentUserProfile();
		
		usr = (TextView) findViewById(R.id.view_user_profile_value_username);
		usr.setText(up.getUsr());
		
		pwd = (TextView) findViewById(R.id.view_user_profile_value_password);
		pwd.setText(up.getPwd());

		gender = (TextView) findViewById(R.id.view_user_profile_value_gender);
		gender.setText(up.getGender().toString());

		zip = (TextView) findViewById(R.id.view_user_profile_value_zip);
		zip.setText(Integer.toString(up.getZip()));
		
		schedule = (TextView) findViewById(R.id.view_user_profile_value_laundry_schedule);
		schedule.setText(Integer.toString(up.getLaundrySchedule()));

		day = (TextView) findViewById(R.id.view_user_profile_value_laundry_day);
		day.setText(up.getLaundryDay());
		
		latLocation = (TextView) findViewById(R.id.view_user_profile_label_current_latitude);
		longLocation = (TextView) findViewById(R.id.view_user_profile_label_current_longtitude);
		int currentHour = Calendar.getInstance().get(Calendar.HOUR);
		getLocation(currentHour);
		mLocationListener = new LocationListener() {

			// Called back when location changes

			public void onLocationChanged(Location location) {
				// Determine whether new location is better than current best
				// estimate

				if (null == mBestReading
						|| location.getAccuracy() < mBestReading.getAccuracy()) {

					// Update best estimate
					mBestReading = location;

					// Update display
					makeUseOfNewLocation(location);

					if (mBestReading.getAccuracy() < MIN_ACCURACY)
						mLocationManager.removeUpdates(mLocationListener);

				}
			}

			public void onStatusChanged(String provider, int status,
					Bundle extras) {
				// NA
			}

			public void onProviderEnabled(String provider) {
				// NA
			}

			public void onProviderDisabled(String provider) {
				// NA
			}
		};

	}
	
	@Override
	protected void onResume() {
		super.onResume();

		// Determine whether initial reading is
		// "good enough"

		if (mBestReading.getAccuracy() > MIN_LAST_READ_ACCURACY
				|| mBestReading.getTime() < System.currentTimeMillis()
						- (Calendar.getInstance().get(Calendar.HOUR) * ONE_HOUR)) {

			// Register for network location updates
			mLocationManager.requestLocationUpdates(
					LocationManager.NETWORK_PROVIDER, POLLING_FREQ, MIN_DISTANCE,
					mLocationListener);

			// Register for GPS location updates
			mLocationManager.requestLocationUpdates(
					LocationManager.GPS_PROVIDER, POLLING_FREQ, MIN_DISTANCE,
					mLocationListener);

			// Schedule a runnable to unregister location listeners

			Executors.newScheduledThreadPool(1).schedule(new Runnable() {

				@Override
				public void run() {

					Log.i(LOG_TAG, "location updates cancelled");

					mLocationManager.removeUpdates(mLocationListener);

				}
			}, MEASURE_TIME, TimeUnit.MILLISECONDS);
		}
	}	

	@Override
	protected void onPause() {
		super.onPause();

		// Unregister location listeners
		mLocationManager.removeUpdates(mLocationListener);
	}

	public void getLocation(int hour) {
		// Acquire a reference to the system Location Manager
		mLocationManager = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);

		// Get current time in milliseconds
		long currentTime = System.currentTimeMillis();
		currentTime -= hour * ONE_HOUR;

		// Get best last location measurement
		mBestReading = bestLastKnownLocation(MIN_LAST_READ_ACCURACY, currentTime);
		
		if (null != mBestReading) {
			makeUseOfNewLocation(mBestReading);
		} else {
			Toast.makeText(getApplicationContext(), "Cannot obtain the current location",
					Toast.LENGTH_LONG).show();			
		}
	}
	
	/*
	 * Use this function to change to fragment if neccesary later.
	 */
	private void makeUseOfNewLocation(Location loc) {
		setLocation(loc);
	}
	
	public void setLocation(Location location) {
		Log.d(LOG_TAG, "setLocation =" + location);
		mBestReading = location;
		double latitude = mBestReading.getLatitude();
		double longitude = mBestReading.getLongitude();

		/*
		Date d = new Date(location.getTime());
		DateFormat f = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		String df = f.format(d);
		*/
		latLocation.setText("" + latitude);
		longLocation.setText("" + longitude + " obtained @ "
				+ new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale
						.getDefault()).format(new Date(mBestReading.getTime())));
	}

	// Get the last known location from all providers
	// return best reading is as accurate as minAccuracy and
	// was taken no longer then minTime milliseconds ago
	// ALDBG ported from LocationGetLocation example.

	private Location bestLastKnownLocation(float minAccuracy, long minTime) {

		Location bestResult = null;
		float bestAccuracy = Float.MAX_VALUE;
		long bestTime = Long.MIN_VALUE;

		List<String> matchingProviders = mLocationManager.getAllProviders();
		Log.i(LOG_TAG, "Number of provides " + matchingProviders.size());

		for (String provider : matchingProviders) {
			Log.i(LOG_TAG, "Provider is " + provider);

			Location location = mLocationManager.getLastKnownLocation(provider);

			if (location != null) {

				float accuracy = location.getAccuracy();
				long time = location.getTime();

				if (accuracy < bestAccuracy) {

					bestResult = location;
					bestAccuracy = accuracy;
					bestTime = time;

					// ALDBG add this code to return the location as soon as
					// it satisfies the requirements of accuracy and time
					if ((bestAccuracy <= minAccuracy) && (bestTime >= minTime)) {
						return bestResult;
					}
				}
			}
		}

		// Return best reading or null
		if (bestAccuracy > minAccuracy || bestTime < minTime) {
			return null;
		} else {
			return bestResult;
		}
	}
}
