package com.example.closetstylist;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class OutfitActivity extends Activity {
	private final static String LOG_TAG = OutfitActivity.class.getCanonicalName();
	
	// Location constants
	private static final long ONE_MIN = 1000 * 60;
	private static final long TWO_MIN = ONE_MIN * 2;
	private static final long FIVE_MIN = ONE_MIN * 5;
	private static final long MEASURE_TIME = 1000 * 30;
	private static final long POLLING_FREQ = 1000 * 10;
	private static final float MIN_ACCURACY = 25.0f;
	private static final float MIN_LAST_READ_ACCURACY = 500.0f;
	private static final float MIN_DISTANCE = 10.0f;
	private static final String Celsius = "\u2103";  
	private static final String Fahrenheit = "\u2109";
	
	private ItemDatabaseHelper itemDatabaseHelper = null;
	private Context context = null;
	
	private ImageView top = null;
	private ImageView bottom = null;
	private ItemData topItem = null;
	private ItemData bottomItem = null;
	private ImageButton buttonPrev = null;
	private ImageButton buttonNext = null;
	private TextView temperature = null;
	private WeatherInfo weatherInfo = null;
	private ArrayList<ItemData> topList = null;
	private ArrayList<ItemData> bottomList = null;
	
	// Current best location estimate
	private Location mBestReading;

	// Reference to the LocationManager and LocationListener
	private LocationManager mLocationManager;
	private LocationListener mLocationListener;
	
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
		temperature = (TextView) findViewById(R.id.outfit_value_temperature);
		
		// Acquire reference to the LocationManager
		if (null == (mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE)))
			finish();

		// Get best last location measurement
		mBestReading = bestLastKnownLocation(MIN_LAST_READ_ACCURACY, FIVE_MIN);
		temperature.setTextColor(Color.CYAN);

		// Display last reading information
		if (null != mBestReading) {
			updateDisplay(mBestReading);
		} else {
			temperature.setText("Cannot find current location");
		}

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
					updateDisplay(location);

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
		
		// Get all the "top" items from our database
		topList = itemDatabaseHelper.getAllTop();
		if (0 == topList.size()) {
			Toast.makeText(context, R.string.outfit_message_no_top, 
					Toast.LENGTH_SHORT).show();						
		} else {
			// Haven't created an algorithm to pick a "top" item yet.
			// Temporarily pick a random item
			int indexTop = 0;
			if (topList.size() > 1) {
				indexTop = new Random().nextInt(topList.size());
			}
			topItem = topList.get(indexTop);
			new ImageSubSampler(context).subSampleCroppedUri(topItem, top, context);
		}
		
		// obtain WeatherInfo from OpenWeatherMap.
		// Can change to another provider like Yahoo by passing another instance
		// to WeatherServiceTask. 
		WeatherServiceTask task = new WeatherServiceTask(new OpenWeatherMapProvider());
		task.execute(String.valueOf(mBestReading.getLatitude()), 
				String.valueOf(mBestReading.getLongitude()));

		// Get all the "bottom" items from our database
		bottomList = itemDatabaseHelper.getAllBottom();
		if (0 == bottomList.size()) {
			Toast.makeText(context, R.string.outfit_message_no_bottom, 
					Toast.LENGTH_SHORT).show();
		} else {
			// Haven't created an algorithm to pick a "bottom" item yet.
			// Temporarily pick a random item
			int indexbottom = 0;
			if (bottomList.size() > 1) {
				indexbottom = new Random().nextInt(bottomList.size());
			}
			bottomItem = bottomList.get(indexbottom);
			new ImageSubSampler(context).subSampleCroppedUri(bottomItem, bottom, context);
		}
		
		buttonPrev = (ImageButton) findViewById(R.id.outfit_btn_prev);
		buttonPrev.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			}			
		});
		buttonNext = (ImageButton) findViewById(R.id.outfit_btn_next);
		buttonNext.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			}			
		});
	}
	
	@Override
	protected void onResume() {
		super.onResume();

		// Determine whether initial reading is
		// "good enough"
		if (mBestReading.getAccuracy() > MIN_LAST_READ_ACCURACY
				|| mBestReading.getTime() < System.currentTimeMillis()
						- TWO_MIN) {

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
	
	// Unregister location listeners
	@Override
	protected void onPause() {
		super.onPause();
		mLocationManager.removeUpdates(mLocationListener);
	}
	
	// Get the last known location from all providers
	// return best reading is as accurate as minAccuracy and
	// was taken no longer then minTime milliseconds ago
	private Location bestLastKnownLocation(float minAccuracy, long minTime) {

		Location bestResult = null;
		float bestAccuracy = Float.MAX_VALUE;
		long bestTime = Long.MIN_VALUE;

		List<String> matchingProviders = mLocationManager.getAllProviders();

		for (String provider : matchingProviders) {
			Location location = mLocationManager.getLastKnownLocation(provider);

			if (location != null) {
				float accuracy = location.getAccuracy();
				long time = location.getTime();

				if (accuracy < bestAccuracy) {
					bestResult = location;
					bestAccuracy = accuracy;
					bestTime = time;
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
	
	// Update display when the next location is obtained.
	private void updateDisplay(Location location) {
	}
	
	public class WeatherServiceTask extends AsyncTask<String, Void, WeatherInfo>{
		private WeatherProviderInterface weatherProvider; // an interface can be swapped at run time
		
		public WeatherServiceTask(WeatherProviderInterface wp) {
			super();
			weatherProvider = wp;
		}

		@Override
		protected WeatherInfo doInBackground(String... params) {
			double lat = Double.parseDouble(params[0]);
			double lon = Double.parseDouble(params[1]);
			String data = weatherProvider.getWeatherDataFromLatLong(lat, lon);
			return weatherProvider.getWeatherInfoFromWeatherData(data);
			// use mock to avoid reaching the query limit of OpenWeatherMap 
			// return weatherProvider.getWeatherInfoFromWeatherData(OpenWeatherMapMockFeed.rawText());
		}

		@Override
		protected void onPostExecute(WeatherInfo result) {
			weatherInfo = result;
			temperature.setText(String.valueOf(result.getTempCurrent()) + " " + Fahrenheit);
		}
	}

}
