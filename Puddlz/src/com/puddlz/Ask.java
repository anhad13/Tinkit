package com.puddlz;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class Ask extends ListActivity {

	// protected List<ParseObject> mAsk;

	protected List<Ask_Fields> askFields;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ask);

		askFields = new ArrayList<Ask_Fields>();
		getActionBar().setDisplayHomeAsUpEnabled(true);
		Ask_Fields a = new Ask_Fields("Electronic Gadgets",
				"Trying to figure out which electronic device to buy? Ask friends and experts!");
		Ask_Fields b = new Ask_Fields("Movies",
				"Ask your friends what you should be watching!");
		Ask_Fields c = new Ask_Fields("Restaurants",
				"Ask for restaurant recommendations & reviews.");
		Ask_Fields d = new Ask_Fields("Doctors",
				"Need trusted recommendations for a doctor?");
		Ask_Fields e = new Ask_Fields("Mobile Apps",
				"Discover cool apps from your friends and Puddlz experts.");
		Ask_Fields f = new Ask_Fields("Other",
				"Get trusted recommendations from your friends.");
		askFields.add(a);
		askFields.add(b);
		askFields.add(c);
		askFields.add(d);
		askFields.add(e);
		askFields.add(f);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		// String[] askFields =
		// {"Electronic Gadgets","Movies","Restaurants","Doctors","Apparels","Others"};

		AskAdapter adapter = new AskAdapter(Ask.this, askFields);

		setListAdapter(adapter);

		adapter.notifyDataSetChanged();

		final ListView mylist1 = (ListView) findViewById(android.R.id.list);
		mylist1.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				if (position == 0)
					electronics(view);
				else if (position == 1)
					movies(view);
				else if (position == 2)
					restaurant(view);
				else if (position == 3)
					doctor(view);
				else if (position == 4)
					apps(view);
				else if (position == 5)
					other(view);




			}
		});

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			return true;
		}

		return (super.onOptionsItemSelected(item));
	}

	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.animator.animation5, R.animator.animation6);
	}

	public void electronics(View view) {
		Intent intent = new Intent(this, New_Electronic.class);
		startActivity(intent);
		// intent.putExtra("type","Electronics");
		overridePendingTransition(R.animator.animation3, R.animator.animation4);
	}

	public void doctor(View view) {
		Intent intent = new Intent(this, Doctor.class);
		startActivity(intent);
		// intent.putExtra("type","Doctor");
		overridePendingTransition(R.animator.animation3, R.animator.animation4);

	}
	
	public void movies(View view)
	   {
			Intent intent = new Intent(this, Movies.class);
		 	startActivity(intent);
			//intent.putExtra("type","Electronics");
		    overridePendingTransition(R.animator.animation3,R.animator.animation4);
	   }
	
	public void other(View view)
	   {
			Intent intent = new Intent(this, Other.class);
		 	startActivity(intent);
			//intent.putExtra("type","Electronics");
		    overridePendingTransition(R.animator.animation3,R.animator.animation4);
	   }

	public void apps(View view)
	   {
			Intent intent = new Intent(this, Apps.class);
		 	startActivity(intent);
			//intent.putExtra("type","Electronics");
		    overridePendingTransition(R.animator.animation3,R.animator.animation4);
	   }
	
	public void restaurant(View view)
	   {
			Intent intent = new Intent(this, Restaurant.class);
		 	startActivity(intent);
			//intent.putExtra("type","Electronics");
		    overridePendingTransition(R.animator.animation3,R.animator.animation4);
	   }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ask, menu);
		return true;
	}

}
