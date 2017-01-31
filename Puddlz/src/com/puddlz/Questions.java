package com.puddlz;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class Questions extends Activity {
Context mc;
String user;
protected void onCreate(Bundle savedInstanceState) {
		mc=this;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_questions);
		//final ParseUser current_user=ParseUser.getCurrentUser();
		user =  getIntent().getExtras().getString("object_id");
		
	}
	
	@Override
		protected void onResume() {
			// TODO Auto-generated method stub
			super.onResume();
			final ProgressBar text_top=(ProgressBar) findViewById(R.id.text_top);
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
			
			Log.d(PuddlzApplication.TAG,
					"String brought over!"+user);
			ParseQuery<ParseUser> query = ParseUser.getQuery();
			query.whereEqualTo("objectId",user);
			query.setLimit(1);
			query.findInBackground(new FindCallback<ParseUser>() {

				@Override
				public void done(List<ParseUser> arg0, ParseException arg1) {
					
					get_question_list(arg0.get(0));
					
				}
			
			});

			
		}

			private void get_question_list(final ParseUser object) {
				if(object!=null)
				{
				Log.d(PuddlzApplication.TAG,
						"OBJECT ISN'T NULL!"+object.getObjectId());
				}	
				final ProgressBar text_top=(ProgressBar) findViewById(R.id.text_top);
				// TODO Auto-generated method stub
				final ListView list_questions=(ListView) findViewById(R.id.listView1);
				final ArrayList<Details_question> details=new ArrayList<Details_question>();
				list_questions.setOnScrollListener(new EndlessScrollListener(1) {
					@Override
					public void onLoadMore(int page, int totalItemsCount) {
						final int page1=(page-1)*20;
						ParseQuery<ParseObject> query_questions=new ParseQuery<ParseObject>("Questions");
						query_questions.setSkip(page1);
						query_questions.setLimit(20);
						query_questions.orderByDescending("updatedAt");
						query_questions.whereEqualTo("post_user", object);
						query_questions.findInBackground(new FindCallback<ParseObject>() {
							@Override
							public void done(List<ParseObject> result_list, ParseException arg1) {
								for(int i=0;i<result_list.size();i++)
								{
									Details_question detail=new Details_question();
									detail.set_question(result_list.get(i).getString("question"));
									detail.set_objectid(result_list.get(i).getObjectId());
									details.add(detail);
								}
                                text_top.setVisibility(View.GONE);
								list_questions.setAdapter(new Adapter_question(details,Questions.this));


							}
						});



					}
				});
				list_questions.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,int position, long id) {

						String object_id=details.get(position).get_objectid();
						Intent intent = new Intent(mc, Answers.class);
						Bundle extras = new Bundle();                       		
						extras.putString("object_id",object_id);
						extras.putString("string", details.get(position).get_question());
						intent.putExtras(extras);
						startActivity(intent);
						//((Activity) globalContext).overridePendingTransition(R.animator.animation3,R.animator.animation4);

					}


				});

			}



}
