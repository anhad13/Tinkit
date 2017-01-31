package com.puddlz;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class Notification_adapter extends BaseAdapter
{
    private ArrayList<Details_notification> data;
    Context c;
   
	
	public Notification_adapter(ArrayList<Details_notification> details, Context globalContext) {
		
    data=details;	
		
	c=globalContext;
	}
	
	@Override
	public int getCount() {
		//how many elements are in the dataset represented by this adapter.
		return data.size();
	}

	@Override
	public Object getItem(int arg0) {
		return data.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	//arg0-position:
	//arg1-view jisme populate karna hai
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		View v=arg1;
		if (v == null)
        {		   
           LayoutInflater vi = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
           //layout inflater is used to inflate layout XML files in a particular view.
           v = vi.inflate(R.layout.notification_element, null);
        }
		TextView name=(TextView) v.findViewById(R.id.textView1);
		ImageView image=(ImageView) v.findViewById(R.id.notification_image);
		Details_notification msg= data.get(arg0);
	    name.setText(msg.get_text());
if (msg.get_type().equals("new_answer"))
	image.setImageResource(R.drawable.answer);
else if(msg.get_type().equals("new_expert"))
	image.setImageResource(R.drawable.expert);
else
	image.setImageResource(R.drawable.friend);
	    return v;
	}
	
}