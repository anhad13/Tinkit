package com.puddlz;


import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Request.GraphUserListCallback;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class UserDetailsActivity extends ActionBarActivity {
	Context mcontext;
	private ImageView userProfilePictureView;
	private TextView userNameView;
	private TextView userGenderView;
	private TextView userEmail;
	private Button homeButton;
	protected ParseRelation<ParseUser> mFriendsRelation;
	protected ParseUser mCurrentUser;
	protected List<GraphUser> mUsers;
	ProgressDialog progress;
	ProgressDialog mProgressDialog;
	protected Bitmap fb_image=null;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Checking internet connection
		getActionBar().setTitle("Puddlz");
		getSupportActionBar().setTitle("Puddlz");
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
				ConnectionDetector cd = new ConnectionDetector(this);
				Boolean isInternet = cd.isConnectingToInternet();
				
				if(ConnectionDetector.isOnline()==false)
		        {
		        	Intent intent=new Intent(this,MainActivity.class);
		        	intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		    		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
					overridePendingTransition(R.animator.animation7,R.animator.animation8);
					Toast.makeText(this, "OOPS! No network access!", Toast.LENGTH_SHORT).show();
					return;	
		        }
								
				
				if(isInternet==false)
				{
					Toast.makeText(this, "Couldn't be posted. Please Check your network connection.", Toast.LENGTH_LONG).show();
					Intent intent = new Intent(this, MainActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
					return;
				}
				else
				{
		mcontext = this;
		if (PuddlzApplication.is_first()) {
			Parse.enableLocalDatastore(this);
			PuddlzApplication.set_first(false);
		}

		setContentView(R.layout.activity_user_details);
		getSupportActionBar().hide();

		ParseUser currentUser = ParseUser.getCurrentUser();

		new DownloadImage().execute("https://graph.facebook.com/"+currentUser.getString("facebookId")+"/picture?type=normal");


		userProfilePictureView = (ImageView) findViewById(R.id.imagep2);
		userNameView = (TextView) findViewById(R.id.userName);
		userGenderView = (TextView) findViewById(R.id.userGender);
		userEmail = (TextView) findViewById(R.id.userEmail);
		userNameView.setVisibility(View.GONE);
		userEmail.setVisibility(View.GONE);
		userGenderView.setVisibility(View.GONE);		

		homeButton = (Button) findViewById(R.id.logoutButton);
		homeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(fb_image!=null)
					try {
						get_all_friends();
					} catch (Exception e)
					{Log.d("EF","found user null");}

			}
		});
				}

	}

	@Override
	public void onResume() {
		super.onResume();

		if(ConnectionDetector.isOnline()==false)
        {
        	Intent intent=new Intent(this,MainActivity.class);
        	intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
    		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			overridePendingTransition(R.animator.animation7,R.animator.animation8);
			Toast.makeText(this, "OOPS! No network access!", Toast.LENGTH_SHORT).show();
			return;	
        }
		else
		{
		ParseUser currentUser = ParseUser.getCurrentUser();
		if (currentUser != null) {

			if (currentUser.getString("name") != null) {
				userNameView.setText(currentUser.getString("name"));
			} else {
				userNameView.setText("UnKnown");
			}
			if (currentUser.getString("gender") != null) {
				userGenderView.setText(currentUser.getString("gender"));
			} else {
				userGenderView.setText("Cant_fetch");
			}

			if (currentUser.getString("email") != null) {
				userEmail.setText(currentUser.getString("email"));
			} else {
				userEmail.setText("?");
			}


		} else {
			startLoginActivity();
		}}
	}


	private void home(){

        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        fb_image.compress(Bitmap.CompressFormat.JPEG, 75, stream);
        final byte[] image = stream.toByteArray();

        ParseUser currentUser=ParseUser.getCurrentUser();

        final ParseFile file = new ParseFile("profilepic.png", image);
        currentUser.put("profile_pic",file );
        currentUser.put("check", 1);
        ParseObject user_not = new ParseObject("user_not");
        user_not.put("user_id", currentUser);
        
        try {
			user_not.save();
			currentUser.put("user_not", user_not);

		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        currentUser.saveInBackground(new SaveCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    SaveData.save_pic_only(mcontext,progress);
                                  } 
            }
        });
    }
	private void startLoginActivity() {
		Intent intent = new Intent(this, MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}




	public void get_all_friends()
	{
		progress = new ProgressDialog(this);
		progress.setMessage("Creating you Profile!");
		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progress.setCancelable(false);
		progress.setIndeterminate(true);
		progress.show();
		mCurrentUser = ParseUser.getCurrentUser();
		mFriendsRelation = mCurrentUser.getRelation("friendsRelation");
		if(mFriendsRelation == null)
		{Log.d("xx","gottcha");}

		Session activeSession = Session.getActiveSession();
		if(activeSession.getState().isOpened()){
			Request friendRequest = Request.newMyFriendsRequest(activeSession, 
					new GraphUserListCallback(){
				@Override
				public void onCompleted(List<GraphUser> users,
						Response response) {
					mUsers = users;
					if(mUsers==null)
					{
						return;
					}
					String[] userids = new String[mUsers.size()];
					int i = 0;
					for(GraphUser user : mUsers){
						userids[i] = user.getId();
						//Log.d("a",Integer.toString(i));
						i++;


					}
					// run query to get list of parse fb friends in a list
					ParseQuery<ParseUser> query = ParseQuery.getQuery("_User");
					query.whereContainedIn("facebookId", Arrays.asList(userids));
					query.findInBackground(new FindCallback<ParseUser>() {

						public void done(final List<ParseUser> fb_users, ParseException e) {
							// results has the list of users with a hometown team with a winning record
							if(e==null)
							{
								ParseQuery<ParseObject> query1 = ParseQuery.getQuery("friends");
								query1.fromLocalDatastore();
								List<ParseObject> scoreList = new ArrayList<ParseObject>();

								try {
									scoreList = query1.find();
								} catch (ParseException e2) {
									// TODO Auto-generated catch block
									e2.printStackTrace();
								}
								for (int i = 0; i < scoreList.size(); i++) {
									try {
										scoreList.get(i).unpin();
									} catch (ParseException e1) {
										// TODO Auto-generated catch
										// block
										e1.printStackTrace();
									}// deletes each object in local
									// datastore.
								}
								List<ParseObject> users_all = new ArrayList<ParseObject>();


								mFriendsRelation.add(mCurrentUser);
								Log.d("MainActivity","success");
								int i=0;
								for(ParseUser user  : fb_users){
									ParseObject user_value = new ParseObject(
											"friends");
									user_value.put("user", user);
									users_all.add(user_value);
									mFriendsRelation.add(user);
									Log.d("b",Integer.toString(i));
									i++;
								}

								Log.d("MainActivity",Integer.toString(i));
                                
								ParseObject user_value = new ParseObject(
										"friends");
								user_value.put("user", mCurrentUser);
								users_all.add(user_value);
								ParseObject
								.pinAllInBackground(users_all);
								mCurrentUser.saveInBackground(new SaveCallback() {



									@Override
									public void done(ParseException e) {



										// TODO Auto-generated method stub
										if(e != null)
										{
											Log.d(PuddlzApplication.TAG, e.getMessage());
										}
										else
										{
											PuddlzApplication.add_installation_user();
											Notify.notify_following(fb_users);
	//										SaveData.save_pic_only(mcontext);// will save all data needed locall
											home();

										}
									}
								});			



							}
							else
							{
								Log.d("xyz",e.getMessage());
							}

						}  
					});



					Log.d("MainActivity",Integer.toString(users.size()));
					//Log.i("INFO", response.toString());

				}

			});
			friendRequest.executeAsync();
		}


	}



	private class DownloadImage extends AsyncTask<String, Void, Bitmap> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Create a progressdialog
			mProgressDialog = new ProgressDialog(UserDetailsActivity.this);
			mProgressDialog.setCanceledOnTouchOutside(false);
			mProgressDialog.setMessage("Loading...");
			mProgressDialog.setIndeterminate(false);
			// Show progressdialog
			mProgressDialog.show();
		}

		@Override
		protected Bitmap doInBackground(String... URL) {

			String imageURL = URL[0];

			Bitmap bitmap = null;
			try {
				// Download Image from URL
				InputStream input = new java.net.URL(imageURL).openStream();
				// Decode Bitmap
				bitmap = BitmapFactory.decodeStream(input);
			} catch (Exception e) {
				e.printStackTrace();
				
				
			}
			return bitmap;
		}

		

		@Override
		//called after. value in param is value returned from doinbackground 
		protected void onPostExecute(Bitmap result) {

			fb_image=result;
			
			// Set the bitmap into ImageView
			userProfilePictureView.setImageBitmap(result);
			// Close progressdialog
			mProgressDialog.dismiss();
			userNameView.setVisibility(View.VISIBLE);
			userEmail.setVisibility(View.VISIBLE);
			userGenderView.setVisibility(View.VISIBLE);

if(fb_image==null)
	start_main();

			
		}
	}

	private void start_main() {
		Toast.makeText(this, "Sign Up Failed! Please try again!", Toast.LENGTH_LONG).show();
	

		Intent intent=new Intent(this, MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	
		// TODO Auto-generated method stub
		
	}



}
