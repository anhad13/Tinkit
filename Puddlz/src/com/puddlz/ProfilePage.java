package com.puddlz;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

public class ProfilePage extends Activity {

	protected ParseRelation<ParseUser> mFriendsRelation;
	protected ParseUser mCurrentUser;
	protected ParseUser thisUser;
	
	private TextView userNameView;
	private TextView userGenderView;
	private TextView userEmailView;
	private TextView userSecret;
	private TextView labelSecret;
	private ToggleButton toggle;
	private String ObjectId;
	private Button myQuestionButton;
	Context mcontext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile_page);
		
		userNameView = (TextView) findViewById(R.id.p_name);
		userGenderView = (TextView) findViewById(R.id.gender);
		userEmailView = (TextView) findViewById(R.id.email);
		userSecret = (TextView) findViewById(R.id.secret);
		labelSecret = (TextView) findViewById(R.id.my_secret);
		toggle =   (ToggleButton) findViewById(R.id.toggle_follow);
		myQuestionButton = (Button) findViewById(R.id.my_question);
		mcontext =this;
		userNameView.setVisibility(View.GONE);
		userSecret.setVisibility(View.GONE);
		labelSecret.setVisibility(View.GONE);
		toggle.setVisibility(View.GONE);
		userGenderView.setVisibility(View.GONE);
		userEmailView.setVisibility(View.GONE);
		myQuestionButton.setVisibility(View.GONE);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	//Checking internet connection	
		
		
		ConnectionDetector cd = new ConnectionDetector(this);
		Boolean isInternet = cd.isConnectingToInternet();
		if(isInternet==false)
		{
			Toast.makeText(this, "Couldn't be posted. Please Check your network connection.", Toast.LENGTH_LONG).show();
			Intent intent = new Intent(this, Home.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			return;
		}
		else
		{
		myQuestionButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				my_questions();
			}
		});

		
		 ObjectId = getIntent().getExtras().getString("object_id");
		
		ParseQuery<ParseUser> query = ParseQuery.getQuery("_User");
		query.whereEqualTo("objectId", ObjectId);
		query.getFirstInBackground(new GetCallback<ParseUser>() {
			  public void done(ParseUser user, ParseException e) {
			    if (user == null) {
			      Log.d("score", "User does not exist");
			 // 	userNameView.setText(ObjectId);
			    } else {
			    	thisUser = user;
			    	userNameView.setText(user.getString("name"));
			       	userGenderView.setText(user.getString("gender"));
			       	userEmailView.setText(user.getString("email"));
			       	userSecret.setText(ObjectId);
			       	getActionBar().setTitle(user.getString("name"));
			       	try {
			       		ImageView question_image = (ImageView) findViewById(R.id.profile_image);
			       		question_image.setVisibility(View.GONE);
			       		ParseFile file = user.getParseFile("profile_pic");
						byte[] fil;
						fil = file.getData();
						Bitmap bitmap = BitmapFactory.decodeByteArray(fil, 0, fil.length);
						question_image.setImageBitmap(bitmap);
						question_image.setVisibility(View.VISIBLE);
					} catch (ParseException e1) {
						// TODO Auto-generated catch block
						Intent intent=new Intent(mcontext, MainActivity.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(intent);
						e1.printStackTrace();
					}
					
					 
			    }
			  }
			});

        mCurrentUser= ParseUser.getCurrentUser();
		
		mFriendsRelation = mCurrentUser.getRelation("friendsRelation");
        ParseQuery<ParseUser> query_f=mFriendsRelation.getQuery();
		query_f.findInBackground(new FindCallback<ParseUser>() {
			@Override
			public void done(List<ParseUser> friends, ParseException e) {
				int c =0;
				if(e==null)
				{
					for(ParseUser user : friends) {
						if(ObjectId.equals(user.getObjectId()))
						{
						c=1;
						Log.d("Friend",user.getObjectId());
						}
					}
					
					if(c==1)
						toggle.setChecked(true);
					else
						toggle.setChecked(false);
		
					userNameView.setVisibility(View.VISIBLE);
					userGenderView.setVisibility(View.VISIBLE);
					userEmailView.setVisibility(View.VISIBLE);
					myQuestionButton.setVisibility(View.VISIBLE);
					userSecret.setVisibility(View.VISIBLE);
					labelSecret.setVisibility(View.VISIBLE);
					
if(!ObjectId.equals(mCurrentUser.getObjectId()))					
				toggle.setVisibility(View.VISIBLE);
else
	toggle.setVisibility(View.GONE);
					
				}				
					}
		});
		
		
		}
		
	
	}
	
	public void onToggleClicked(View view) {
	    // Is the toggle on?
       /*  mCurrentUser= ParseUser.getCurrentUser();
		
		mFriendsRelation = mCurrentUser.getRelation("friendsRelation");
	
		ParseQuery<ParseUser> query = ParseQuery.getQuery("_User");
		query.whereEqualTo("objectId", ObjectId);
		query.getFirstInBackground(new GetCallback<ParseUser>() {
			  public void done(ParseUser user, ParseException e) {
			    if (user == null) {
			      Log.d("score", "User does not exist");
			  	 } else {
			    	thisUser = user;
			    }
			  }});
		
		*/
		
		boolean on = ((ToggleButton) view).isChecked();
	    Log.d("Tag","Called");
	    if (on) {
	    	Log.d("Tag","Friend added");
        	mFriendsRelation.add(thisUser);
        	SaveData.add_user(thisUser,this);
        	List<ParseUser> fb_users=new ArrayList<ParseUser>();
        	fb_users.add(thisUser);
        	Notify.notify_following(fb_users); 
        	
  
        	
        	    } else {
        	    	Log.d("Tag","Friend removed");
        	    	mFriendsRelation.remove(thisUser);
        	       	SaveData.delete_user(thisUser,this);
        	        
        		    }
	    mCurrentUser.saveInBackground();
	}

	public void my_questions()
	{
		Intent intent1 = new Intent(this, Questions.class);
		Bundle extras1 = new Bundle();                       		
		extras1.putString("object_id",ObjectId);
		intent1.putExtras(extras1);
		startActivity(intent1);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.profile_page, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.animator.animation7, R.animator.animation8);
	}

	

}