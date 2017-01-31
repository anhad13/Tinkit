package com.puddlz;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

public class SaveData {
	 static List<ParseObject> List1;
		
	static int count=0;

	static void save_pic_and_list(final Context mcontext) {
	
        count=0;
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
			}catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}//deletes each object in local datastore.
		}
		final ParseUser current_user=ParseUser.getCurrentUser();
		ParseRelation<ParseUser> relation=current_user.getRelation("friendsRelation");
		ParseQuery<ParseUser> query=relation.getQuery();
		final List<ParseObject> users_all=new ArrayList<ParseObject>();
		//query.include("profile_pic");
		query.findInBackground(new FindCallback<ParseUser>() {
			@Override
			public void done(final List<ParseUser> friend_list, ParseException arg1) {
				for(int i=0;i<friend_list.size();i++)
				{
					ParseObject user_value=new ParseObject("friends");
					user_value.put("user", friend_list.get(i));
					users_all.add(user_value);
						
					ParseFile file=(ParseFile)friend_list.get(i).get("profile_pic");	
					final int ind=i;
					
					if(friend_list.get(i).getNumber("check")==null || fileExistance(friend_list.get(ind).getObjectId()))
					{
						count++;
						if(count==friend_list.size())
						{
							Intent intent = new Intent(mcontext, Home.class);
							intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
							intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							mcontext.startActivity(intent);
						}
						continue;					
					}
                        					
					file.getDataInBackground(new GetDataCallback() {
						public void done(byte[] data, ParseException e) {
							
							count++;
							if(count==friend_list.size())
							{
								Intent intent = new Intent(mcontext, Home.class);
								intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
								intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								mcontext.startActivity(intent);
							}
							if (e == null) {
								
								try {
									
									FileOutputStream outputStream;
									outputStream = mcontext.openFileOutput(friend_list.get(ind).getObjectId(), Context.MODE_PRIVATE);
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
				try {
					ParseObject.pinAll(users_all);
			
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

			private boolean fileExistance(String objectId) {
				 File file = mcontext.getFileStreamPath(objectId);
				    return file.exists();
				
			}
		});


	}
	
	

	static void save_pic_and_list_without_intent(final Context mcontext, final ProgressDialog progress) {
	
        count=0;
       final ParseUser current_user=ParseUser.getCurrentUser();
		ParseRelation<ParseUser> relation=current_user.getRelation("friendsRelation");
		ParseQuery<ParseUser> query=relation.getQuery();
		final List<ParseObject> users_all=new ArrayList<ParseObject>();
		//query.include("profile_pic");
		query.findInBackground(new FindCallback<ParseUser>() {
			@Override
			public void done(final List<ParseUser> friend_list, ParseException arg1) {
				ParseQuery<ParseObject> query1 = ParseQuery.getQuery("friends");
				query1.fromLocalDatastore();
				

				try {
					List1 = query1.find();
				} catch (ParseException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				for(int i=0;i<friend_list.size();i++)
				{
					ParseObject user_value=new ParseObject("friends");
					user_value.put("user", friend_list.get(i));
					users_all.add(user_value);
					if(friend_list.get(i).getNumber("check")==null)
					{
						count++;
						if(count==friend_list.size())
						{
						    user_update(List1);
							progress.dismiss();
					    }
						continue;
					}	
					ParseFile file=(ParseFile)friend_list.get(i).get("profile_pic");	
					final int ind=i;
					
					if(fileExistance(friend_list.get(ind).getObjectId()))
					{
						count++;
						if(count==friend_list.size())
						{
						    user_update(List1);
							progress.dismiss();
					    }
						continue;					
					}
                        					
					file.getDataInBackground(new GetDataCallback() {
						public void done(byte[] data, ParseException e) {
							count++;
							if (e == null) {
								
								try {
									
									FileOutputStream outputStream;
									outputStream = mcontext.openFileOutput(friend_list.get(ind).getObjectId(), Context.MODE_PRIVATE);
									
									//Bitmap bitmap=BitmapFactory.decodeByteArray(data, 0, data.length);
									outputStream.write(data);
									outputStream.close();
									
								} catch (Exception e1) {
									e1.printStackTrace();
								}
								if(count==friend_list.size())
								{
								    user_update(List1);
									progress.dismiss();
							    }
							} else {
								if(count==friend_list.size())
								{
								    user_update(List1);
									progress.dismiss();
							    }
								// something went wrong
							}
						}
					});
					
				}

		
			}
			private void user_update(List <ParseObject> scoreList)
			{
			for (int i = 0; i < scoreList.size(); i++) {                            
				try {
					Log.d("deleting","user");
					scoreList.get(i).unpin();
				}catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}//deletes each object in local datastore.
			}
			try {
				Log.d("addiing","users");
				ParseObject.pinAll(users_all);
		
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			}
			private boolean fileExistance(String objectId) {
				 File file = mcontext.getFileStreamPath(objectId);
				    return file.exists();
				
			}
		});


	}
	
	
	
	static void save_list_only()
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
			}catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}//deletes each object in local datastore.
		}
		final ParseUser current_user=ParseUser.getCurrentUser();
		ParseRelation<ParseUser> relation=current_user.getRelation("friendsRelation");
		ParseQuery<ParseUser> query=relation.getQuery();
		final List<ParseObject> users_all=new ArrayList<ParseObject>();
		try {
			List<ParseUser> friend_list=query.find();
			for(int i=0;i<friend_list.size();i++)
			{
				ParseObject user_value=new ParseObject("friends");
				user_value.put("user", friend_list.get(i));
				users_all.add(user_value);
			}
			try {
				ParseObject.pinAll(users_all);
		
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}
	static void save_pic_only(final Context mcontext,final ProgressDialog progress) {
		
        count=0;
		final ParseUser current_user=ParseUser.getCurrentUser();
		ParseRelation<ParseUser> relation=current_user.getRelation("friendsRelation");
		ParseQuery<ParseUser> query=relation.getQuery();
		query.findInBackground(new FindCallback<ParseUser>() {
			@Override
			public void done(final List<ParseUser> friend_list, ParseException arg1) {
				for(int i=0;i<friend_list.size();i++)
				{
					if(friend_list.get(i).getNumber("check")==null)
					{
						if(i==friend_list.size()-1)
						{
							Intent intent = new Intent(mcontext, Invite.class);
							intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
							intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							progress.dismiss();
							mcontext.startActivity(intent);
						}
						count++;
						continue;
					}	
					ParseFile file=(ParseFile)friend_list.get(i).get("profile_pic");	
					final int ind=i;
					if(fileExistance(friend_list.get(ind).getObjectId())){
						if(i==friend_list.size()-1)
						{
							Intent intent = new Intent(mcontext, Invite.class);
							intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
							intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							progress.dismiss();
							mcontext.startActivity(intent);
						}
						count++;
						continue;
					}
					file.getDataInBackground(new GetDataCallback() {
						public void done(byte[] data, ParseException e) {
							if (e == null) {
								
								try {
									FileOutputStream outputStream;
									outputStream = mcontext.openFileOutput(friend_list.get(ind).getObjectId(), Context.MODE_PRIVATE);
									//Bitmap bitmap=BitmapFactory.decodeByteArray(data, 0, data.length);
									outputStream.write(data);
									outputStream.close();									  
								} catch (Exception e1) {
									e1.printStackTrace();
								}
								if(count==friend_list.size()-1)
								{
									Intent intent = new Intent(mcontext, Invite.class);
									intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
									intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
									progress.dismiss();
									mcontext.startActivity(intent);
								}
								count++;
							} else {
								// something went wrong
							}
						}
					});
					
				}
			}

			private boolean fileExistance(String objectId) {
				 File file = mcontext.getFileStreamPath(objectId);
				    return file.exists();
				
			}
		});


	}
	public static void add_user(final ParseUser thisUser,final Context mcontext) {
		
		
		
	            	
				ParseFile file=(ParseFile)thisUser.get("profile_pic");	
					if(mcontext.getFileStreamPath(thisUser.getObjectId()).exists())
					{
						//image already present. But add to friendlist.
                        ParseObject new_friend=new ParseObject("friends");
                        new_friend.put("user",thisUser);
                        try {
							new_friend.pin();
						} catch (ParseException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						return;
					}
					file.getDataInBackground(new GetDataCallback() {
						public void done(byte[] data, ParseException e) {
							if (e == null) {
								
								try {
									FileOutputStream outputStream;
									outputStream = mcontext.openFileOutput(thisUser.getObjectId(), Context.MODE_PRIVATE);
									//Bitmap bitmap=BitmapFactory.decodeByteArray(data, 0, data.length);
									outputStream.write(data);
									outputStream.close();
									   ParseObject new_friend=new ParseObject("friends");
				                        new_friend.put("user",thisUser);
				                        try {
											new_friend.pin();
										} catch (ParseException e1) {
											// TODO Auto-generated catch block
											e1.printStackTrace();
										}
								} catch (Exception e1) {
									e1.printStackTrace();
								}
							} else {
								// something went wrong
							}
						}
					});
				

					
			}
	public static void delete_user(ParseUser thisUser, ProfilePage profilePage) {
		// TODO Auto-generated method stub
		ParseQuery<ParseObject> del_f=new ParseQuery<ParseObject>("friends");
		del_f.fromLocalDatastore();
		del_f.whereEqualTo("user",thisUser);
		try {
			ParseObject fr=del_f.getFirst();
			fr.unpin();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


		
}
