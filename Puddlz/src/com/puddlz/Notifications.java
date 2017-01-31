package com.puddlz;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

public class Notifications extends Activity {

	ParseUser current_user;
	ParseRelation<ParseObject> relation;
	ParseObject not_user;
	Context mc;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notifications);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		current_user=ParseUser.getCurrentUser();
		if (!((current_user != null) && ParseFacebookUtils.isLinked(current_user))) {
			Intent intent=new Intent(this, MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
	//		return;
			}

	}

	@Override
	protected void onResume() {
		super.onResume();
		mc=this;
				
		ParseQuery<ParseObject> query=new ParseQuery<ParseObject>("user_not");
		query.whereEqualTo("user_id", current_user);
		query.getFirstInBackground(new GetCallback<ParseObject>() {
			@Override
	public void done(ParseObject object, ParseException e) {
		// TODO Auto-generated method stub
				if(e==null)
				{
					not_user=object;
					call_not();

				}
				else
				{
					Intent intent=new Intent(mc,Home.class);
					Log.d("In",":2");
					startActivity(intent);
					overridePendingTransition(R.animator.animation7,R.animator.animation8);
					Toast.makeText(mc, "OOPS! No network access!", Toast.LENGTH_SHORT).show();
					return;	
				}
		
	}
});
		
	}
	
	void call_not(){
		if(ConnectionDetector.isOnline()==false)
        {
        	Intent intent=new Intent(this,Home.class);
        	intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
    		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        	startActivity(intent);
			overridePendingTransition(R.animator.animation7,R.animator.animation8);
			Toast.makeText(this, "OOPS! No network access!", Toast.LENGTH_SHORT).show();
			return;	
        }
	try{			
		relation = not_user.getRelation("u_not");

		final ListView list_notifications=(ListView) findViewById(R.id.listView1);
		final ProgressBar progress_bar=(ProgressBar) findViewById(R.id.progressBar1);
		final ArrayList<Details_notification> details=new ArrayList<Details_notification>();
		final ArrayList<Details_notification> details_final=new ArrayList<Details_notification>();


		list_notifications.setOnScrollListener(new EndlessScrollListener(1) {
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				final int page1=(page-1)*20;
				relation.getQuery().orderByDescending("createdAt").setSkip(page1).setLimit(20).findInBackground(new FindCallback<ParseObject>()
						{

					@Override
					public void done(List<ParseObject> result_list, ParseException e) {
						if(result_list.isEmpty())
						{
							//Toast.makeText(Notifications.this, "No Unread notifications", Toast.LENGTH_SHORT).show();
							//list is empty, you can chose to do something.
						}
						else
						{
							for(int i=0;i<result_list.size();i++)
							{
								Details_notification detail=new Details_notification();
								if(result_list.get(i).getString("type").equals("new_answer"))
								{
									detail.set_text("New answer/s for -"+result_list.get(i).getString("text"));
									detail.set_parent(result_list.get(i).getString("question_object"));

								}
								else if(result_list.get(i).getString("type").equals("new_expert"))
								{
									detail.set_text("A Puddlz expert responded to -"+result_list.get(i).getString("text"));
									detail.set_parent(result_list.get(i).getString("question_object"));

								}
								else//go to profile page.
								{
									detail.set_text(result_list.get(i).getString("text"));
									detail.set_parent(result_list.get(i).getString("person"));									
								}

								detail.set_details(result_list.get(i).getString("details"));
								detail.set_type(result_list.get(i).getString("type"));
								detail.set_object(result_list.get(i));
								details.add(detail);				
							}
							String last_parent=null;
							int last_one=0;
							int last_count=1;
							for(int i=0;i<result_list.size();i++)
							{
								if(!details.get(i).get_type().equals("new_answer"))
								{
									details_final.add(details.get(i));
									last_parent=null;
									last_count=1;
								}
								else//if the type is new answer
								{

									if(last_parent==null || !last_parent.equals(details.get(i).get_parent()))
									{

										details_final.add(details.get(i));
										last_parent=details.get(i).get_parent();
										last_count=1;

										//last_one=i;
									}
									else//this is a repetition.
									{  
										last_count++;
										//details_final.get(last_one).set_count(last_count);
									}

								}

							}

							list_notifications.setAdapter(new Notification_adapter(details_final,Notifications.this));


							list_notifications.setOnItemClickListener(new OnItemClickListener() {
								@Override
								public void onItemClick(AdapterView<?> parent, View view,int position, long id) {

									String object_id;
									Intent intent = new Intent(Notifications.this, Answers.class);
									if(!details_final.get(position).get_type().equals("new_friend"))
									{
										object_id=details_final.get(position).get_details();
									}
									else
									{
										object_id=details_final.get(position).get_parent();											
										intent=new Intent(Notifications.this,ProfilePage.class);
									}

									final Bundle extras = new Bundle();                       		
									extras.putString("string",details_final.get(position).get_text());
									extras.putString("object_id",object_id);
									List<ParseQuery<ParseObject>> list=new ArrayList<ParseQuery<ParseObject>>();
									ParseQuery<ParseObject> q1=ParseQuery.getQuery("notifications");
									q1.whereEqualTo("question_object",object_id);
									list.add(q1);
									ParseQuery<ParseObject> q2=ParseQuery.getQuery("notifications");
									q2.whereEqualTo("person",object_id);
									list.add(q2);
									ParseQuery<ParseObject> delete_query=ParseQuery.getQuery("notifications");
									delete_query=ParseQuery.or(list);
									delete_query.findInBackground(new FindCallback<ParseObject>()
											{

										@Override
										public void done(
												List<ParseObject> results,
												ParseException arg1) {

											ParseObject.deleteAllInBackground(results,new DeleteCallback()
											{

												@Override
												public void done(ParseException arg0) {
												}

											});



										}
											});


									intent.putExtras(extras);

									startActivity(intent);
									overridePendingTransition(R.animator.animation3,R.animator.animation4);



								}


							});     


						}
						progress_bar.setVisibility(View.INVISIBLE);

					}


						});

			}
		});
	}catch(Exception ee)
	{}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.notifications, menu);
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