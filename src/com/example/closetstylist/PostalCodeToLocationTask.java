package com.example.closetstylist;

import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.example.ui.PlaceRecordContainerInterface;

/*
 * Modified based on PlaceDownloaderTask.java in iRemember
 */
public class PostalCodeToLocationTask extends AsyncTask<Integer, Void, PlaceRecord> {
	// Change to false if you don't have network access
	private static final boolean HAS_NETWORK = true;

    // put your www.geonames.org account name here.
    private static String USERNAME = "anhpopeye";

	private HttpURLConnection mHttpUrl;
	private WeakReference<PlaceRecordContainerInterface> mParent;
	private Context context;
	private ProgressDialog dialog;
	
	public PostalCodeToLocationTask(PlaceRecordContainerInterface parent, Context context) {
		super();
		this.mParent = new WeakReference<PlaceRecordContainerInterface>(parent);
		this.dialog = new ProgressDialog(context);
		this.context = context;
	}

	@Override
	protected void onPreExecute() {
		dialog.setMessage("Obtaining location from your zip code");
		dialog.show();
	}
	
	@Override
	protected PlaceRecord doInBackground(Integer... zip) {
		return LocationInfoProvider.getPlaceFromPostalCode(zip[0]);
	}

	@Override
	protected void onPostExecute(PlaceRecord result) {
		if (this.dialog.isShowing()) {
			dialog.dismiss();
		}
		
		if (null != result && null != mParent.get()) {
			mParent.get().setPlaceRecord(result);
		}
	}
}
