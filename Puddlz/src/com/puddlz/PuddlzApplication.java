package com.puddlz;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseInstallation;
import com.parse.ParseUser;

public class PuddlzApplication extends Application {
	  public static String last_element=null;
	  public static boolean first=true;
	  public static int no_loaded=0;
	  public static boolean is_first()
	  {
		  return first;
	  }
	  public static void set_first(boolean val)
	  {
		 first=val;
	  }

	  public String get_last(){
		  return last_element;
	  }
	  
	  public void set_last(String element)
	  {
		  last_element=element;
	  }
	  
	  
    
	static final String TAG = "MyApp";
public static void add_installation_user()
{
	ParseInstallation parse=ParseInstallation.getCurrentInstallation();
	if(parse==null)
	{
		Log.d(PuddlzApplication.TAG,"WE ARE SO FUCKED");
	}

	ParseUser user_id=ParseUser.getCurrentUser();
	parse.put("user_id",user_id);
	if(user_id==null)
	{
		Log.d(PuddlzApplication.TAG,"USER_ID IS NULL");
	}
	parse.saveInBackground();
	
}
	
	
	@Override
	public void onCreate() {
		
		final Context mc=this;
		super.onCreate();
			//Parse.enableLocalDatastore(this);
	    Parse.initialize(this, "YBMV7qZ0oCYvHcw7WXDI6htAL0Z5nJXICWY7MSz4", "lXMItdfT8cHjafF6NBHtCzD5kvYy1RnwVULc8QOj");	    
	    //PushService.setDefaultPushCallback(mc, Notifications.class);	        

	    ParseFacebookUtils.initialize("316842738487675");
	    //PushService.setDefaultPushCallback((Context)this, Notifications.class);
	    ParseInstallation.getCurrentInstallation().saveInBackground();
	   
}


	
}
