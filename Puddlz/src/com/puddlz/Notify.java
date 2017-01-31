package com.puddlz;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

public class Notify {
	
	
	public static void notify_following(List<ParseUser> fb_users) {
        List<ParseObject> user_not=new ArrayList<ParseObject>();
		try {
			String username = ParseUser.getCurrentUser().getString("name");
			JSONObject data = new JSONObject();
			data.put("title", username);
			data.put("alert", "Hi! I just started following you.");
			List<ParseRelation> relation_not=new ArrayList<ParseRelation>();
		
			ParseUser current_user=ParseUser.getCurrentUser();
			ParseRelation rel=current_user.getRelation("friendsRelation");
			final List<String> user_s=new ArrayList<String>();
			ParseQuery<ParseObject> query=ParseQuery.getQuery("user_not");
			query.whereContainedIn("user_id",fb_users);
			query.include("user_id");
			List<ParseObject> scoreList=new ArrayList<ParseObject>();
			try {
				scoreList = query.find();
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}  
						for (int i = 0; i < scoreList.size(); i++) {
					//		Log.d("user_not", scoreList.get(i).getObjectId());
						    user_not.add(scoreList.get(i));
						    relation_not.add(scoreList.get(i).getRelation("u_not"));
						    user_s.add(scoreList.get(i).getParseObject("user_id").getObjectId());
						    //Log.d("Heyi",user_s.get(0));
						}
					

			ParseQuery<ParseInstallation> pushQuery = ParseInstallation.getQuery();
			pushQuery.whereContainedIn("user_id", fb_users);
			//Log.d("Notifications", fb_users.get(0).getString("name"));
			ParsePush push = new ParsePush();
			push.setQuery(pushQuery);

			push.setData(data);
			push.sendInBackground();
			int k;
			List<ParseObject> list_notifications = new ArrayList<ParseObject>();
			for (int i = 0; i < fb_users.size(); i++) {
				if(!user_s.contains(fb_users.get(i).getObjectId()))
				{
				//	Log.d("yrl",fb_users.get(i).getObjectId());
					continue;
				}
				else
				k=user_s.indexOf(fb_users.get(i).getObjectId());
			//	Log.d("Notifications", fb_users.get(i).getString("name"));
				//relation_not.add(user_not.get(k).getRelation("u_not"));
				ParseObject new_notification = new ParseObject("notifications");
				//new_notification.put("question_object", fb_users.get(i));
				new_notification.put("type", "new_friend");
				new_notification.put("details", username+ " is now following you.");
				new_notification.put("is_read", false);
				String p=fb_users.get(i).getObjectId();
				ParseObject p1=fb_users.get(i);
				new_notification.put("user_id", p1);
				new_notification.put("person",ParseUser.getCurrentUser().getObjectId());
				new_notification.put("text", username+ " is now following you.");
				list_notifications.add(new_notification);
			}

			try {
				ParseObject.saveAll(list_notifications);
				for(int i=0;i<user_not.size();i++)
				{
					if(relation_not.get(i)==null)
						Log.d("NULL","DETECT");
					relation_not.get(i).add(list_notifications.get(i));					
				}
				ParseObject.saveAll(user_not);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				//Log.d("NotificationsNotWorking","all not saved");
				e.printStackTrace();
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	

}
