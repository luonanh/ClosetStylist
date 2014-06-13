package com.example.closetstylist;

import java.util.ArrayList;
import java.util.Arrays;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class RegisterActivity extends Activity {
	private final static String LOG_TAG = RegisterActivity.class.getCanonicalName();
	private Context context = null;

	private Spinner sex;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		
		context = getApplicationContext();
		
		sex = (Spinner) findViewById(R.id.register_spinner_sex);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, 
				R.array.register_spinner_sex, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		sex.setAdapter(adapter);
	}
}
