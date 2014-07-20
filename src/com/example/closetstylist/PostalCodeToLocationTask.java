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
	
	public PostalCodeToLocationTask(RegisterActivity parent) {
		super();
		mParent = new WeakReference<RegisterActivity>(parent);
	}

	@Override
	protected PlaceRecord doInBackground(Integer... zip) {
		PlaceRecord place = null;
		String url = generateURL(USERNAME, zip[0]);
		place = getPlaceFromURL(url);

		if (HAS_NETWORK) {
			
		} else {
			
		}
		return place;
	}

	@Override
	protected void onPostExecute(PlaceRecord result) {
		if (null != result && null != mParent.get()) {
			mParent.get().setPlaceRecord(result);
		}
	}
	
	private PlaceRecord getPlaceFromURL(String... params) {
		String result = null;
		BufferedReader in = null;

		if (HAS_NETWORK) {
			try {

				URL url = new URL(params[0]);
				mHttpUrl = (HttpURLConnection) url.openConnection();
				in = new BufferedReader(new InputStreamReader(
						mHttpUrl.getInputStream()));

				StringBuffer sb = new StringBuffer("");
				String line = "";
				while ((line = in.readLine()) != null) {
					sb.append(line + "\n");
				}
				result = sb.toString();

			} catch (MalformedURLException e) {

			} catch (IOException e) {

			} finally {
				try {
					if (null != in) {
						in.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				mHttpUrl.disconnect();
			}			
		} else {
			result = GeonamesPostalCodeToLocationMockFeed.rawText();
		}

		return placeDataFromXml(result);
	}

	private static PlaceRecord placeDataFromXml(String xmlString) {
		DocumentBuilder builder;
		String stateName = "";
		String placeName = "";
		String countryCode = "";
		int postalCode = 0;
		double lng;
		double lat;
		Location tempLocation = new Location(LocationManager.NETWORK_PROVIDER);

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		try {
			builder = factory.newDocumentBuilder();
			Document document = builder.parse(new InputSource(new StringReader(
					xmlString)));
			NodeList list = document.getDocumentElement().getChildNodes();
			for (int i = 0; i < list.getLength(); i++) {
				Node curr = list.item(i);

				NodeList list2 = curr.getChildNodes();

				for (int j = 0; j < list2.getLength(); j++) {

					Node curr2 = list2.item(j);
					if (curr2.getNodeName() != null) {

						if (curr2.getNodeName().equals("postalcode")) {
							postalCode = Integer.parseInt(curr2.getTextContent());
						} else if (curr2.getNodeName().equals("name")) {
							placeName = curr2.getTextContent();
						} else if (curr2.getNodeName().equals("countryCode")) {
							countryCode = curr2.getTextContent();
						} else if (curr2.getNodeName().equals("lat")) {
							lat = Double.parseDouble(curr2.getTextContent());
							tempLocation.setLatitude(lat);;
						} else if (curr2.getNodeName().equals("lng")) {
							lng = Double.parseDouble(curr2.getTextContent());
							tempLocation.setLongitude(lng);
						} else if (curr2.getNodeName().equals("adminName1")) {
							stateName = curr2.getTextContent();
						}
					}
				}
			}
		} catch (DOMException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return new PlaceRecord(countryCode, stateName, placeName, postalCode, tempLocation);
	}

	private static String generateURL(String username, int zip) {
		// http://api.geonames.org/postalCodeSearch?postalcode=78758&maxRows=10&username=anhpopeye
		return "http://api.geonames.org/postalCodeSearch?postalcode=" + zip 
				+ "&maxRows=2" + "&username=" + username;
	}

}
