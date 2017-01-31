package com.puddlz;


import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.Session;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

public class Home extends FragmentActivity implements
ActionBar.TabListener {
	public void get_pics(View view)
	{
		//final ImageView image=(ImageView) findViewById(R.id.image);
		final ParseUser current_user=ParseUser.getCurrentUser();
		//final Context context=(Context)this.getApplicationContext();
		ParseRelation<ParseUser> relation=current_user.getRelation("friendsRelation");
		ParseQuery<ParseUser> query=relation.getQuery();
		//query.include("profile_pic");
		query.findInBackground(new FindCallback<ParseUser>() {
			@Override
			public void done(final List<ParseUser> friend_list, ParseException arg1) {
				for(int i=0;i<friend_list.size();i++)
				{
					ParseFile file=(ParseFile)friend_list.get(i).get("profile_pic");	
					final int ind=i;
					if(fileExistance(friend_list.get(ind).getObjectId()))
						continue;
					file.getDataInBackground(new GetDataCallback() {
						public void done(byte[] data, ParseException e) {
							if (e == null) {
								FileOutputStream outputStream;
								try {
									outputStream = openFileOutput(friend_list.get(ind).getObjectId(), Context.MODE_PRIVATE);
									//Bitmap bitmap=BitmapFactory.decodeByteArray(data, 0, data.length);
									outputStream.write(data);
									outputStream.close();									  
								} catch (Exception e1) {
									e1.printStackTrace();
								}
							} else {
								// something went wrong
							}
						}
					});
				}
			}

			private boolean fileExistance(String objectId) {
				 File file = getBaseContext().getFileStreamPath(objectId);
				    return file.exists();
			}
		});

	}
	public void show_questions(View view)
	{
		BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), getFileStreamPath("9OCRZ95CeZ").getAbsolutePath());
		ImageView image=(ImageView) findViewById(R.id.image);
		image.setImageDrawable(bitmapDrawable);
//		Intent intent = new Intent(this, Questions.class);
//		Bundle extras = new Bundle();                       		
//
//		extras.putString("object_id",ParseUser.getCurrentUser().getObjectId());
//		intent.putExtras(extras);
//		startActivity(intent);
//		overridePendingTransition(R.animator.animation3,R.animator.animation4);	
//	}
	}
	public void ask(View view)
	{
		Intent intent = new Intent(this, Ask.class);
		startActivity(intent);
		overridePendingTransition(R.animator.animation1,R.animator.animation2);		 
	}
	public void notification(View view)
	{
		ConnectionDetector cd = new ConnectionDetector(this);
		Boolean isInternet = cd.isConnectingToInternet();
		if(isInternet==false)
		{
			Toast.makeText(this, "Couldn't be opened. Please Check your network connection.", Toast.LENGTH_LONG).show();
			return;
		}
		Intent intent = new Intent(this, Notifications.class);
		startActivity(intent);
		overridePendingTransition(R.animator.animation1,R.animator.animation2);		 
	}

	private ViewPager viewPager;
	private TabsPagerAdapter mAdapter;
	private ActionBar actionBar;
	// Tab titles
	private String[] tabs = {"Feed","Settings" };
	String[] menutitles;




	@Override
	protected void onCreate(Bundle savedInstanceState) {

		if(PuddlzApplication.is_first())
		{
			Parse.enableLocalDatastore(this);
			PuddlzApplication.set_first(false);

		}
		


		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);



		viewPager = (ViewPager) findViewById(R.id.pager);
		actionBar = getActionBar();
		mAdapter = new TabsPagerAdapter(getSupportFragmentManager());

		viewPager.setAdapter(mAdapter);
		actionBar.setHomeButtonEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS); 
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowTitleEnabled(false);


//		// Adding Tabs
//		for (String tab_name : tabs) {
//			actionBar.addTab(actionBar.newTab().setText(tab_name)
//					.setTabListener(this));
//		}
		 final int[] ICONS = new int[] {
                 R.drawable.ic_action_action_home,
                 R.drawable.ic_action_person
         };
		for (int i=0;i<2;i++) {
			Drawable icon=getResources().getDrawable(ICONS[i]);
			icon.setColorFilter( 0xffff0000, Mode.MULTIPLY );
			actionBar.addTab(actionBar.newTab()
					.setIcon(icon)
					.setTabListener(this));
		}

		/**
		 * on swiping the viewpager make respective tab selected
		 * */
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// on changing the page
				// make respected tab selected
				actionBar.setSelectedNavigationItem(position);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}



	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// on tab selected
		// show respected fragment view
		viewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	}

	private void onLogoutButtonClicked() {
		// Log the user out
		ParseUser.logOut();

		if (Session.getActiveSession() != null) {
			Session.getActiveSession().closeAndClearTokenInformation();
		}

		Session.setActiveSession(null);
		System.exit(0);
        //finish();
		// Go to the login view
		//ParseQuery.clearAllCachedResults(); 
		//startLoginActivity();
	}

	private void startLoginActivity() {
		Intent intent = new Intent(this, MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}
	public void logout(View v) {
		
		onLogoutButtonClicked();
	}

}