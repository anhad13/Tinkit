package com.puddlz;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Session;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;

public class Invite extends ActionBarActivity {

	private Button skip;
	private Button invite;
	Context mcontext;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_invite);
		getSupportActionBar().hide();
		
		mcontext=this;
		skip = (Button) findViewById(R.id.skip);
		invite = (Button) findViewById(R.id.invite);
		
		Log.d("Invite","Class started");
		
		invite.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				sendRequestDialog();
			}
		});
		
		
		skip.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				home();
				// TODO Auto-generated method stub
				
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.invite, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	private void sendRequestDialog() {
        Bundle params = new Bundle();
        params.putString("message", "I just started using Puddlz!");

        WebDialog requestsDialog = (
            new WebDialog.RequestsDialogBuilder(mcontext,
                Session.getActiveSession(),
                params)).setOnCompleteListener(new OnCompleteListener() {

                    @Override
                    public void onComplete(Bundle values,
                        FacebookException error) {
                        if (error != null) {
                            if (error instanceof FacebookOperationCanceledException) {
                                Toast.makeText(mcontext, 
                                    "Request cancelled", 
                                    Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(mcontext, 
                                    "Network Error", 
                                    Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            final String requestId = values.getString("request");
                            if (requestId != null) {
                                Toast.makeText(mcontext, 
                                    "Request sent",  
                                    Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(mcontext, 
                                    "Request cancelled", 
                                    Toast.LENGTH_SHORT).show();
                            }
                        }   
                    }

                

                })
                .build();
        requestsDialog.show();
    }

	private void home() {

		Intent intent = new Intent(this, Home.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
		overridePendingTransition(R.animator.animation9,R.animator.animation10);
	}


}
