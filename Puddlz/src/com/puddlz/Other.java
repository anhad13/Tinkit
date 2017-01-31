package com.puddlz;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
//import the parse class

public class Other extends Activity implements LocationListener {

	EditText text_question;
	Context c;
	String text1;
	TextView address;
	CheckBox check_address;
	CheckBox is_expert;
	ProgressDialog progress;
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			return true;
		}

		return(super.onOptionsItemSelected(item));
	}
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.animator.animation7, R.animator.animation8);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		c=this;
		setTitle("Other"); //change this
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_other);// change this
		address=(TextView) findViewById(R.id.text_location);
		check_address=(CheckBox) findViewById(R.id.check_location);
	//	is_expert=(CheckBox)findViewById(R.id.checkBox1);//remove at some places
	}
	public void goto_feed(View view)
	{
       
		text_question=(EditText) findViewById(R.id.editText1);
		if(text_question.getText().toString().equals(""))
		{
			Toast.makeText(this, "Please enter some text!", Toast.LENGTH_LONG).show();
			return;
		}
		//for internet conn
		
		ConnectionDetector cd = new ConnectionDetector(this);
		Boolean isInternet = cd.isConnectingToInternet();
		if(isInternet==false)
		{
			Toast.makeText(this, "Couldn't be posted. Please Check your network connection.", Toast.LENGTH_LONG).show();
			return;
		}//
		  progress=new ProgressDialog(this);
	      progress.setMessage("Posting Question!");
	      progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	      progress.setIndeterminate(true);
	      progress.show();
		final ParseObject newq = new ParseObject("Questions");	
		newq.put("question",text_question.getText().toString().replaceAll("  ", " "));
		newq.put("no_answers",0); //remove this
		newq.put("topic", "Other"); //change this
		newq.put("post_user", ParseUser.getCurrentUser());
		if(check_address.isChecked())
		{
		  newq.put("location",address.getText());		  
		}
		else
		{
			newq.put("location", "random");
		}
		//apps and electronics
		{
		  newq.put("is_expert", false);
		}
	newq.saveInBackground(new SaveCallback()
	{

		@Override
		public void done(ParseException arg0) {
	       if(arg0==null)
	       {
			Toast.makeText(c, "Yayyy! Your Question has been posted.", Toast.LENGTH_LONG).show();
			Intent intent = new Intent(c, Home.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);			
	       }
	       else
	       {
	    	   Toast.makeText(c, "Couldn't be posted. Please Check your network connection.", Toast.LENGTH_LONG).show();				   
	       }	                		
			// TODO Auto-generated method stub
			
		}
		
	});
	

		//overridePendingTransition(R.animator.animation3,R.animator.animation4);
	}
	public void radio1(View view)
	{
		text_question=(EditText) findViewById(R.id.editText1);
		Spannable text_spam = new SpannableString("Can I find any good   near me?\n");          
		text_spam.setSpan(new BackgroundColorSpan(0xFF35d0e4), 20, 21, Spannable.SPAN_INCLUSIVE_INCLUSIVE);    	
		text_question.setText(text_spam);
	}

	public void radio2(View view)
	{
		text_question=(EditText) findViewById(R.id.editText1);
		Spannable text_spam = new SpannableString("Can you suggest me some good books?\n");          
		text_question.setText(text_spam);	
	}
	public void radio3(View view)
	{
		text_question=(EditText) findViewById(R.id.editText1);
		Spannable text_spam = new SpannableString("Any cool music suggestions?\n");          
		text_question.setText(text_spam);
	}
	public void clear(View view)
	{
		text_question=(EditText) findViewById(R.id.editText1);
		text_question.setText("");
	}
	public void add_location(View view)
	{
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		if (location != null) 
		{
			onLocationChanged(location);  

		} 
		else 
		{       
			if(!locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
			{

				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						Other.this);

				alertDialogBuilder
				.setMessage("To add location, activate location in Settings. Once you're done, try again.")
				.setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						Intent viewIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
						startActivity(viewIntent);
						if(check_address.isChecked())
						{
							check_address.toggle();
						}

					}
				})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
						if(check_address.isChecked())
						{
							check_address.toggle();
						}
					}
				});

				// create an alert dialog
				AlertDialog alert = alertDialogBuilder.create();
				alert.show();

			}
			else
			{
				locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0, 0, this);
			}
		}

	}
	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		(new GetAddressTask(Other.this)).execute(location);

	}
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}
	private class GetAddressTask extends AsyncTask<Location,Void,String>{
		//3 paramrs-1st-input,2nd-progress,3rd-result, if none put void.

		Context mContext;
		//below we have a constructor that gets us the context.
		public GetAddressTask(Context context) {
			super();
			mContext = context;
		}

		protected String doInBackground(Location... params) {
			Geocoder geocoder =
					new Geocoder(mContext, Locale.getDefault());
			// Get the current location from the input parameter list
			Location loc = params[0];
			// Create a list to contain the result address
			List<Address> addresses = null;
			try {
				/*
				 * Return 1 address.
				 */
				addresses = geocoder.getFromLocation(loc.getLatitude(),
						loc.getLongitude(), 1);
			} catch (IOException e1) {
				Log.e("LocationSampleActivity",
						"IO Exception in getFromLocation()");
				e1.printStackTrace();

				return "";

			} catch (IllegalArgumentException e2) {
				// Error message to post in the log
				String errorString = "Illegal arguments " +
						Double.toString(loc.getLatitude()) +
						" , " +
						Double.toString(loc.getLongitude()) +
						" passed to address service";
				Log.e("LocationSampleActivity", errorString);
				e2.printStackTrace();

				return "";
				//return errorString;
			}


			if (addresses != null && addresses.size() > 0) {
				// Get the first address
				Address address = addresses.get(0);
				/*
				 * Format the first line of address (if available),
				 * city, and country name.
				 */
				String addressText = String.format(
						"%s,%s,%s",
						// If there's a street address, add it
						address.getSubLocality(),
						// Locality is usually a city
						address.getLocality(),
						// The country of the address
						address.getCountryName());
				// Return the text
				return addressText;
			} else {

				return "";
			}
		}

		protected void onPostExecute(String address2) {
			if(!address2.equals(""))
			{
				address.setText(address2);

				address.setVisibility(View.VISIBLE);
				check_address.setVisibility(View.GONE);
			}
			else
			{
				Toast.makeText(Other.this,"Location could not be loaded. Please check internet connection." , Toast.LENGTH_LONG).show();
				if(check_address.isChecked())
				{
					check_address.toggle();
				}
			}

		}




	}
	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}
	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

}
