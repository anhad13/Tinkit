package com.puddlz;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Session;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;
import com.parse.GetCallback;
import com.parse.ParseQuery;
import com.parse.ParseUser;
//import the parse class
public class Frag4 extends Fragment {

	  Context context; 
	 
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		context = getActivity();
	
		
		final ParseUser current_user=ParseUser.getCurrentUser();


		View rootView = inflater.inflate(R.layout.activity_frag4, container, false);        
		ImageView image = (ImageView) rootView.findViewById(R.id.image);
		BitmapDrawable bitmapDrawable = new BitmapDrawable(getActivity().getResources(), getActivity().getFileStreamPath(current_user.getObjectId()).getAbsolutePath());
		image.setImageDrawable(bitmapDrawable);
		TextView name = (TextView) rootView.findViewById(R.id.textView1);
		name.setText(current_user.getString("name"));
		final ListView list=(ListView) rootView.findViewById(R.id.listView1);
		Adapter_settings adp=new Adapter_settings(getActivity());
		list.setAdapter(adp);
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {

				switch(position)
				{
				case 0:
					if(isInternet())
					{Intent intent0 = new Intent(getActivity(), ProfilePage.class);
					Bundle extras = new Bundle();                       		
					extras.putString("object_id",current_user.getObjectId());
					intent0.putExtras(extras);
					startActivity(intent0);}
					break;
				case 1:
					if(isInternet())
					{Intent intent1 = new Intent(getActivity(), Questions.class);
					Bundle extras1 = new Bundle();                       		
					extras1.putString("object_id",current_user.getObjectId());
					intent1.putExtras(extras1);
					startActivity(intent1);}
					break;
				case 2:
					if(isInternet())
					{Intent intent2 = new Intent(getActivity(), EditFriendsActivity.class);
					startActivity(intent2);}
					break;			    
				case 3:
					if(isInternet())
					{Intent intent3= new Intent(Intent.ACTION_SEND);
					intent3.setType("text/plain");
					intent3.putExtra(android.content.Intent.EXTRA_TEXT, "Hey, I've been using the Puddlz app and it's awesome! You should check it out:\nwww.puddlz.com\nAdd me using my Secret: "+current_user.getObjectId());					
					startActivity(intent3);} 
					break;
				case 4:
					if(isInternet())
					{AlertDialog.Builder alert = new AlertDialog.Builder(context);
		            alert.setTitle("Enter your friends Secret:"); //Set Alert dialog title here
		            //alert.setMessage("Enter Your Name Here"); //Message here
		 
		            // Set an EditText view to get user input 
		            final EditText input = new EditText(context);
		            alert.setView(input);
		 
		            alert.setPositiveButton("SEARCH", new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog, int whichButton) {
		             //You will get as string input data in this variable.
		             // here we convert the input to a string and show in a toast.
		             final String ObjectId = input.getEditableText().toString();
	                 
		             ParseQuery<ParseUser> query = ParseQuery.getQuery("_User");
		     		query.whereEqualTo("objectId", ObjectId);
		     		query.getFirstInBackground(new GetCallback<ParseUser>() {
		     	   
						@Override
						public void done(ParseUser user,
								com.parse.ParseException arg1) {
							// TODO Auto-generated method stub
							 if (user == null) {
			     			    	
		     		             Toast.makeText(context,"User Not found. Please retry!",Toast.LENGTH_LONG).show(); 
		     		             
		     			  	
		     			    } else {
		     			    gotoProfilePage(ObjectId);	 
		     			    }
						}
		     			});

	            
		             
		            } // End of onClick(DialogInterface dialog, int whichButton)
		        }); //End of alert.setPositiveButton
		            alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
		              public void onClick(DialogInterface dialog, int whichButton) {
		                // Canceled.
		                  dialog.cancel();
		              }
		        }); //End of alert.setNegativeButton
		            AlertDialog alertDialog = alert.create();
		            alertDialog.show();
					}
					break;
				case 5:
					if(isInternet())
					{sendRequestDialog();}
					break;
				case 6:
					if(isInternet())
					{
						
						ParseUser.logOut();

					if (Session.getActiveSession() != null) {
						Session.getActiveSession().closeAndClearTokenInformation();
					}

					Session.setActiveSession(null);
					System.exit(0);	}				
					break;

					
				}
				
			}


		});  

		
		return rootView;
	}
	
	private void gotoProfilePage(String ObjectId) {
		final Intent profile_page = new Intent(context,ProfilePage.class);
		final Bundle profile = new Bundle();
		profile.putString("object_id", ObjectId);
		profile_page.putExtras(profile);
		startActivity(profile_page);
		
		
	}
	
	private void sendRequestDialog() {
        Bundle params = new Bundle();
        params.putString("message", "I just started using Puddlz!");

        WebDialog requestsDialog = (
            new WebDialog.RequestsDialogBuilder(context,
                Session.getActiveSession(),
                params)).setOnCompleteListener(new OnCompleteListener() {

                    @Override
                    public void onComplete(Bundle values,
                        FacebookException error) {
                        if (error != null) {
                            if (error instanceof FacebookOperationCanceledException) {
                                Toast.makeText(context, 
                                    "Request cancelled", 
                                    Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, 
                                    "Network Error", 
                                    Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            final String requestId = values.getString("request");
                            if (requestId != null) {
                                Toast.makeText(context, 
                                    "Request sent",  
                                    Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, 
                                    "Request cancelled", 
                                    Toast.LENGTH_SHORT).show();
                            }
                        }   
                    }

                

                })
                .build();
        requestsDialog.show();
    }
	
	private Boolean isInternet()
	{	ConnectionDetector cd = new ConnectionDetector(context);
	Boolean isInternet = cd.isConnectingToInternet();
	if(!isInternet) 
	{Toast.makeText(context, 
            "Internet is not working...", 
            Toast.LENGTH_SHORT).show();}
    return isInternet;
	}

} 