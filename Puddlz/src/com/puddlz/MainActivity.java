package com.puddlz;

import java.util.Arrays;
import java.util.List;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.facebook.FacebookRequestError;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.PushService;

public class MainActivity extends ActionBarActivity {

	private Button loginButton;
	private Dialog progressDialog;
	ParseUser currentUser;
	Context mcontext=this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		PushService.setDefaultPushCallback((Context)this, Notifications.class);
		super.onCreate(savedInstanceState);
		getSupportActionBar().hide();
		setContentView(R.layout.activity_main);
	    

		loginButton = (Button) findViewById(R.id.loginButton);
		loginButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onLoginButtonClicked();
			}
		});

		// Check if there is a currently logged in user
		// and they are linked to a Facebook account.
		currentUser = ParseUser.getCurrentUser();

		if ((currentUser != null) && ParseFacebookUtils.isLinked(currentUser)
				&& currentUser.getNumber("check") != null) {

			// Go to the user info activity
			home();
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
	//	getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
	}

	private void onLoginButtonClicked() {
		MainActivity.this.progressDialog = ProgressDialog.show(
				MainActivity.this, "", "Logging in...", true);
		List<String> permissions = Arrays.asList("public_profile",
				"user_friends", "email");

		ParseFacebookUtils.logIn(permissions, this, new LogInCallback() {
			@Override
			public void done(ParseUser user, ParseException err) {
				MainActivity.this.progressDialog.dismiss();
				if (user == null) {
					Log.d(PuddlzApplication.TAG,
							"Uh oh. The user cancelled the Facebook login.");
				} else if (user.isNew()) {
					Log.d(PuddlzApplication.TAG,
							"User signed up and logged in through Facebook!");

					Session session = ParseFacebookUtils.getSession();
					if (session != null && session.isOpened()) {
						makeMeRequest();
					}		
					
				} else {
					Log.d(PuddlzApplication.TAG,
							"User logged in through Facebook!");
					ParseUser currentUser = ParseUser.getCurrentUser();
					
					if (currentUser != null) {
						Log.d(PuddlzApplication.TAG, "User IN THE GAME"
								+ currentUser.getObjectId());
					}
					if (currentUser.getNumber("check") != null) {
						PuddlzApplication.add_installation_user();
						if (PuddlzApplication.is_first()) {
							Parse.enableLocalDatastore(mcontext);
							PuddlzApplication.set_first(false);
						}
						 SaveData.save_pic_and_list(mcontext);
						//home();
					} else {
						Session session = ParseFacebookUtils.getSession();
						if (session != null && session.isOpened()) {
							makeMeRequest();
						}
						
						}
				}
			}
		});
	}
	private void showUserDetailsActivity() {

		Intent intent = new Intent(this, UserDetailsActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}

	private void home() {

		Intent intent = new Intent(this, Home.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
		overridePendingTransition(R.animator.animation9,R.animator.animation10);
	}

	private void start_again() {

		Intent intent = new Intent(this, MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		finish();
		startActivity(intent);
	}

	private void makeMeRequest() {
		Request request = Request.newMeRequest(ParseFacebookUtils.getSession(),
				new Request.GraphUserCallback() {
					@Override
					public void onCompleted(GraphUser user, Response response) {
						if (user != null) {
							ParseUser currentUser = ParseUser
									.getCurrentUser();
					//		userProfilePictureView.setProfileId(user.getId());
							currentUser.put("name", user.getName());
							currentUser.put("facebookId", user.getId());
							currentUser.put("gender",
									(String) user.getProperty("gender"));
//						ParseObject user_not = new ParseObject("user_not");
//					
//							user_not.put("user_id", currentUser);
//							try {
//								user_not.save();
//								currentUser.put("user_not", user_not);
//
//							} catch (ParseException e1) {
//								// TODO Auto-generated catch block
//								e1.printStackTrace();
//							}
							if(user.asMap().get("email")!=null)
							{ currentUser.setEmail((String) user.asMap().get("email") );}
														// Save the user profile info in a user property
							try{
								currentUser.save();
							}catch(ParseException e)
							{
								Log.d("TAG","Error in saving");
								
							}
							showUserDetailsActivity();

								// Show the user info
							//	updateViewsWithProfileInfo();
							

						} else if (response.getError() != null) {
							if ((response.getError().getCategory() == FacebookRequestError.Category.AUTHENTICATION_RETRY)
									|| (response.getError().getCategory() == FacebookRequestError.Category.AUTHENTICATION_REOPEN_SESSION)) {
								Log.d(PuddlzApplication.TAG,
										"The facebook session was invalidated.");
								onLogoutButtonClicked();
							} else {
								Log.d(PuddlzApplication.TAG,
										"Some other error: "
												+ response.getError()
														.getErrorMessage());
							}
						}
					}
					
					
				});
		request.executeAsync();

	
	}
	
	private void onLogoutButtonClicked() {
		// Log the user out
		ParseUser.logOut();
		
		if (Session.getActiveSession() != null) {
			Session.getActiveSession().closeAndClearTokenInformation();
			}

			Session.setActiveSession(null);

		// Go to the login view
		start_again();
	}
	
}
