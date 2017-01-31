package com.puddlz;

import java.util.Arrays;
import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Request.GraphUserListCallback;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class EditFriendsActivity extends ListActivity {
	
	protected ParseRelation<ParseUser> mFriendsRelation;
	protected ParseUser mCurrentUser;
	protected List<GraphUser> mUsers;
	
	public static final String TAG = EditFriendsActivity.class.getSimpleName();
	public Context mcontext=this;
	protected List<ParseUser> mFriends;
	private Button Button1;
	ProgressDialog progress;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_edit_friends);
		getActionBar().setTitle("Friends");   
		getActionBar().setDisplayHomeAsUpEnabled(true);

		
 		
		getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		
		Button1 = (Button) findViewById(R.id.button1);
		Button1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					get_all_friends();
				} catch (Exception e)
				{Log.d("EF","found user null");}
					
				}
		});
	}
	
	@Override
	protected void onResume() {
		super.onResume();
        ConnectionDetector cd = new ConnectionDetector(this);
		Boolean isInternet = cd.isConnectingToInternet();
	
		if(isInternet==false)
		{
			Toast.makeText(this, "Couldn't load. Please Check your network connection.", Toast.LENGTH_LONG).show();
			Intent intent = new Intent(this, Home.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			return;
		}
		else
		{
		mCurrentUser = ParseUser.getCurrentUser();
		mFriendsRelation = mCurrentUser.getRelation("friendsRelation");
		final String user_id =  mCurrentUser.getObjectId();
		setProgressBarIndeterminateVisibility(true);
		Log.d("tt", user_id);
		
		mFriendsRelation.getQuery().findInBackground(new FindCallback<ParseUser>() {
			@Override
			public void done(List<ParseUser> friends, ParseException e) {
				if(e==null)
				{
					mFriends = friends;
					int j = 0;
					for(ParseUser exclude_self : mFriends)
					{
						if(user_id.equals(exclude_self.getObjectId()))
						{mFriends.remove(j);
						
							break;
						}
						else
						j++;
					
					}
					
				
				setProgressBarIndeterminateVisibility(false);
				
				String[] fnames = new String[mFriends.size()];
				int i = 0;
				for(ParseUser user : mFriends) {
					fnames[i] = user.getString("name");
					i++;
				}
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(
						EditFriendsActivity.this, 
						android.R.layout.simple_list_item_1,
						fnames);
				setListAdapter(adapter);
	 
	/*			int j =0;
				for (j = 0; j < mFriends.size(); j++) {
					
						getListView().setItemChecked(j, true);
					
				}
				*/
				}
					else {
						Log.e(TAG, e.getMessage());
						AlertDialog.Builder builder = new AlertDialog.Builder(EditFriendsActivity.this);
						builder.setMessage(e.getMessage())
							.setTitle(R.string.error_title)
							.setPositiveButton(android.R.string.ok, null);
						AlertDialog dialog = builder.create();
						dialog.show();
					}
				
			}
			
	
		});
		
		
		
				
		
		
		
	}}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_friends, menu);
		return true;
	}


	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
	/*	if (getListView().isItemChecked(position)) {
			
			// add the friend
			mFriendsRelation.add(mFriends.get(position));
			
		}
		else
		{// remove the friend
			
			mFriendsRelation.remove(mFriends.get(position));
		}
		
		mCurrentUser.saveInBackground(new SaveCallback() {
			@Override
			public void done(ParseException e) {
				if (e != null) {
					Log.e(TAG, e.getMessage());
				}
			}
		});
		
		*/
		
		final Intent profile_page = new Intent(EditFriendsActivity.this,ProfilePage.class);
		final Bundle profile = new Bundle();
	    profile.putString("object_id", mFriends.get(position).getObjectId());
		profile_page.putExtras(profile);
		startActivity(profile_page);
		
		
		
		Log.d("MainActivity","banana");
		
		
		
	}
	
		
	public void get_all_friends()
	{
		progress = new ProgressDialog(this);
		progress.setMessage("Updating friends from Facebook!");
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
	                	Log.d("musers","aa");
	                	progress.dismiss();
	                	
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
							
							public void done(List<ParseUser> fb_users, ParseException e) {
							    // results has the list of users with a hometown team with a winning record
							if(e==null)
							{
								mFriendsRelation.add(mCurrentUser);
								Log.d("MainActivity","success");
								int i=0;
								for(ParseUser user  : fb_users){
									
									mFriendsRelation.add(user);
									Log.d("b",Integer.toString(i));
									i++;
								}
                                
								Log.d("MainActivity",Integer.toString(i));
                        
								mCurrentUser.saveInBackground(new SaveCallback() {
								
									
									@Override
									public void done(ParseException e) {
										onResume();
										 
										// TODO Auto-generated method stub
										if(e != null)
										{
											Log.d(PuddlzApplication.TAG, e.getMessage());
										}
										else
										{
											SaveData.save_pic_and_list_without_intent(mcontext,progress);
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
