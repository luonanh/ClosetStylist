package com.example.closetstylist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.app.ProgressDialog;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;

import com.example.ui.RegisterActivity;

/*
 * Modified based on PlaceDownloaderTask.java in iRemember
 */
public class PostalCodeToLocationTask extends AsyncTask<Integer, Void, PlaceRecord> {
	// Change to false if you don't have network access
	private static final boolean HAS_NETWORK = true;

    // put your www.geonames.org account name here.
    private static String USERNAME = "anhpopeye";

	private HttpURLConnection mHttpUrl;
	private WeakReference<RegisterActivity> mParent;
	private ProgressDialog dialog;
	
	public PostalCodeToLocationTask(RegisterActivity parent) {
		super();
		mParent = new WeakReference<RegisterActivity>(parent);
		dialog = new ProgressDialog(parent);
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
