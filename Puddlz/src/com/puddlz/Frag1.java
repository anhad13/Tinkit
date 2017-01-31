package com.puddlz;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff.Mode;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Session;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
//import the parse class
public class Frag1 extends Fragment {
	Context mcontext;
	protected SwipeRefreshLayout mSwipeRefreshLayout;
	
	List<ParseObject> list_friends;
	boolean is_put=false;
	View foot,rootView;
	int page1;
	ArrayList<Details> details;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		final ListView mylist1;
		mcontext=getActivity();
		rootView = inflater.inflate(R.layout.activity_frag1, container, false);
		foot = inflater.inflate(R.layout.progress_bar_footer, null);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		final ProgressBar progress_bar=(ProgressBar) foot.findViewById(R.id.progressBar);
		progress_bar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.app_blue), Mode.SRC_IN);
		final ProgressBar text_top=(ProgressBar) rootView.findViewById(R.id.text_top);
		mSwipeRefreshLayout = (SwipeRefreshLayout)rootView.findViewById(R.id.swipeRefreshLayout);
		mSwipeRefreshLayout.setOnRefreshListener(mOnRefreshListener);
		ConnectionDetector cd = new ConnectionDetector(getActivity());
		Boolean isInternet = cd.isConnectingToInternet();
		//final PuddlzApplication g1= ((PuddlzApplication) getActivity().getApplicationContext());
		ParseUser current_user=ParseUser.getCurrentUser();
		if(current_user==null)
		{
			Log.d(PuddlzApplication.TAG,
					"STUCK AT FRAG!!");
		}
		List<ParseObject> list_friend1=new ArrayList<ParseObject>();
		ParseQuery<ParseObject> get_friends = ParseQuery.getQuery("friends");
		get_friends.fromLocalDatastore();
		try {
			list_friend1=get_friends.find();
		} catch (ParseException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		list_friends=new ArrayList<ParseObject>();
		for(int i=0;i<list_friend1.size();i++)
		{
			list_friends.add(list_friend1.get(i).getParseObject("user"));
			Log.d(PuddlzApplication.TAG,
					"GETTING VALUE FROM LOCAL!!");
		}
        if(list_friends.isEmpty())
        {
        	Log.d(PuddlzApplication.TAG,
					"Someone cleared the cache!");
        	ParseUser.logOut();

    		if (Session.getActiveSession() != null) {
    			Session.getActiveSession().closeAndClearTokenInformation();
    		}

    		Session.setActiveSession(null);
    		Intent intent = new Intent(getActivity(),MainActivity.class);
    		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    		startActivity(intent);
    		
        }
        Log.d(PuddlzApplication.TAG,
				"INSIDE FRAG1!");
		mylist1= (ListView) rootView.findViewById(R.id.listView1);
		mylist1.removeFooterView(foot);  
		mylist1.addFooterView(foot);    
        details=null;
		details=new ArrayList<Details>();

		final Context globalContext = getActivity();

		if(isInternet==true)
		{
			mylist1.setOnScrollListener(new EndlessScrollListener(1) {
				@Override
				public void onLoadMore(int page, int totalItemsCount) {
					progress_bar.setVisibility(View.VISIBLE);
					final int page1=(page-1)*10;
					ParseQuery<ParseObject> query = ParseQuery.getQuery("Questions");
					query.whereContainedIn("post_user",list_friends);
					query.setSkip((page-1)*10);
					query.include("post_user");
					query.setLimit(10);
					query.orderByDescending("updatedAt");
					query.findInBackground(new FindCallback<ParseObject>() {
						public void done(List<ParseObject> scoreList, ParseException e) {
							if (e == null) {
								int first_visible=mylist1.getFirstVisiblePosition();
								text_top.setVisibility(View.GONE);
								if(scoreList.isEmpty() && page1==0)
								{
									Toast.makeText(mcontext, "Feed is currently empty.\n We recommend you INVITE more friends to join!", Toast.LENGTH_LONG).show();
								}
								for (int i = 0; i < scoreList.size(); i++) {
								
//									ParseObject local=new ParseObject("Questions");
//									local.put("question",scoreList.get(i).getString("question")); 
//								local.put("object_id", scoreList.get(i).getObjectId());
									Log.d("a1","a1");
									ParseObject us=scoreList.get(i).getParseObject("post_user");
									Log.d("a2","a2");
									Details detail= new Details();
									Log.d("a3","a3");
									detail.set_question(scoreList.get(i).getString("question"));
									//detail.set_image(us.getBytes("profile_pic"));
									detail.setno("0");
									detail.set_objectid(scoreList.get(i).getObjectId());
									detail.set_name(us.getString("name"));
							//		if(!scoreList.get(i).getString("location").isEmpty())
										detail.set_location(scoreList.get(i).getString("location"));
								//		else
									//	detail.set_location("rockOn");	
									detail.set_image(us.getObjectId());
									Log.d("a4","a4");
									details.add(detail);
									detail=null;
								//	local=null;
									PuddlzApplication.no_loaded=page1+1;
									//fetch data from network
								}
								mylist1.setAdapter(new CustomerAdapter(details , globalContext));
								Log.d("a5","a5");
								if (mSwipeRefreshLayout.isRefreshing()) {
									mSwipeRefreshLayout.setRefreshing(false);
									}
								progress_bar.setVisibility(View.GONE);
								//	progress_first.setVisibility(View.GONE);
								mylist1.setSelection(first_visible);
								mylist1.setOnItemClickListener(new OnItemClickListener() {
									@Override
									public void onItemClick(AdapterView<?> parent, View view,int position, long id) {

										String object_id=details.get(position).get_objectid();
										TextView tx=(TextView) view.findViewById(R.id.textViewx1);
										String s=tx.getText().toString();
										Intent intent = new Intent(globalContext, Answers.class);
										Bundle extras = new Bundle();     
										Log.d("a7","a7");
										extras.putString("string",s);
										extras.putString("object_id",object_id);
										Log.d("a8","a8");
										intent.putExtras(extras);
										startActivity(intent);
										((Activity) globalContext).overridePendingTransition(R.animator.animation3,R.animator.animation4);

									}


								});                   
							} else {


								Log.d("score", "Error: " + e.getMessage());
							}
						}
					});   


				}

			});
		}
		else
		{
			Toast.makeText(mcontext, "Failed to reload!\n Please check your internet connection!", Toast.LENGTH_LONG).show();			
		}
		
		return rootView; 

	}
	protected OnRefreshListener mOnRefreshListener = new OnRefreshListener() {
		@Override
		public void onRefresh() {
			refresh_feed();
		}

		private void refresh_feed() {
			final ListView mylist1;
		    final ProgressBar progress_bar=(ProgressBar) foot.findViewById(R.id.progressBar);
			final ProgressBar text_top=(ProgressBar) rootView.findViewById(R.id.text_top);
			mSwipeRefreshLayout = (SwipeRefreshLayout)rootView.findViewById(R.id.swipeRefreshLayout);
			mSwipeRefreshLayout.setOnRefreshListener(mOnRefreshListener);
			ConnectionDetector cd = new ConnectionDetector(getActivity());
			Boolean isInternet = cd.isConnectingToInternet();
			//final PuddlzApplication g1= ((PuddlzApplication) getActivity().getApplicationContext());
			ParseUser current_user=ParseUser.getCurrentUser();
			if(current_user==null)
			{
				Log.d(PuddlzApplication.TAG,
						"STUCK AT FRAG!!");
			}
			List<ParseObject> list_friend1=new ArrayList<ParseObject>();
			ParseQuery<ParseObject> get_friends = ParseQuery.getQuery("friends");
			get_friends.fromLocalDatastore();
			try {
				list_friend1=get_friends.find();
			} catch (ParseException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			list_friends=new ArrayList<ParseObject>();
			for(int i=0;i<list_friend1.size();i++)
			{
				list_friends.add(list_friend1.get(i).getParseObject("user"));
				Log.d(PuddlzApplication.TAG,
						"GETTING VALUE FROM LOCAL!!");
			}

			Log.d(PuddlzApplication.TAG,
					"INSIDE FRAG1!");
			mylist1= (ListView) rootView.findViewById(R.id.listView1);
		  


			final Context globalContext = getActivity();
			
			if(isInternet==true && is_put==false)
			{   
				mylist1.removeFooterView(foot);  
				 
				details=new ArrayList<Details>();
				mylist1.setAdapter(new CustomerAdapter(details , globalContext));
				
				mylist1.setOnScrollListener(new EndlessScrollListener(1,0) {
					
					@Override
					public void onLoadMore(int page, int totalItemsCount) {
				       // if(page==2)
				       // 	mylist1.addFooterView(foot);
						Log.d("Page-number", Integer.toString(page));
						progress_bar.setVisibility(View.VISIBLE);
					    page1=(page-1)*10;
					    Log.d("Page-numberxx", Integer.toString(page1));
					    ParseQuery<ParseObject> query = ParseQuery.getQuery("Questions");
						Log.d(PuddlzApplication.TAG,
								"GPPDPSIDIP!!");
						query.whereContainedIn("post_user",list_friends);
						query.setSkip((page-1)*10);
						query.include("post_user");
						query.setLimit(10);
						is_put=true;
						query.orderByDescending("updatedAt");
						query.findInBackground(new FindCallback<ParseObject>() {
							
							public void done(List<ParseObject> scoreList, ParseException e) {
								if (e == null) {
									int first_visible=mylist1.getFirstVisiblePosition();
									if(scoreList.isEmpty() && page1==0)
									{
										Toast.makeText(mcontext, "Feed is currently empty.\n We recommend you INVITE more friends to join!", Toast.LENGTH_LONG).show();
									}
									for (int i = 0; i < scoreList.size(); i++) {
										
										ParseObject local=new ParseObject("Questions");
										local.put("question",scoreList.get(i).getString("question")); 
										local.put("object_id", scoreList.get(i).getObjectId());
										Log.d(PuddlzApplication.TAG,
												scoreList.get(i).getObjectId());
										ParseObject us=scoreList.get(i).getParseObject("post_user");
										Details detail= new Details();
										detail.set_question(scoreList.get(i).getString("question"));
										//detail.set_image(us.getBytes("profile_pic"));
										detail.setno("0");
										detail.set_objectid(scoreList.get(i).getObjectId());
										detail.set_name(us.getString("name"));
										//if(scoreList.get(i).getString("location").isEmpty())
										detail.set_location(scoreList.get(i).getString("location"));
										//else
										//detail.set_location("rockOn");	
										detail.set_image(us.getObjectId());
										details.add(detail);
										detail=null;
										text_top.setVisibility(View.GONE);
										PuddlzApplication.no_loaded=page1+1;
										//fetch data from network
									}
									if(is_put==true)
									{
									mylist1.setAdapter(new CustomerAdapter(details , globalContext));
									}
									else
									{
								    details=new ArrayList<Details>();
									}
									is_put=false;
									if (mSwipeRefreshLayout.isRefreshing()) {
										mSwipeRefreshLayout.setRefreshing(false);
										}
									progress_bar.setVisibility(View.GONE);
									//	progress_first.setVisibility(View.GONE);
									mylist1.setSelection(first_visible);
									mylist1.setOnItemClickListener(new OnItemClickListener() {
										@Override
										public void onItemClick(AdapterView<?> parent, View view,int position, long id) {

											String object_id=details.get(position).get_objectid();
											TextView tx=(TextView) view.findViewById(R.id.textViewx1);
											String s=tx.getText().toString();
											Intent intent = new Intent(globalContext, Answers.class);
											Bundle extras = new Bundle();                       		
											extras.putString("string",s);
											extras.putString("object_id",object_id);
											intent.putExtras(extras);
											startActivity(intent);
											((Activity) globalContext).overridePendingTransition(R.animator.animation3,R.animator.animation4);

										}


									});                   
								} else {


									Log.d("score", "Error: " + e.getMessage());
								}
							}
						});   


					}

				});
			}
			else if(isInternet==false)
			{
				Toast.makeText(mcontext, "Failed to reload!\n Please check your internet connection!", Toast.LENGTH_LONG).show();
				
			}
			
		}
	};



} 