package com.puddlz;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class Adapter_settings extends BaseAdapter {
    
	String s[]={"My Profile","My Questions", "Friends","Share","Find User Using Secret","Invite Facebook Friends","LogOut"};
	Context c;
	public Adapter_settings(Context globalContext) {
		
	c=globalContext;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return s.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return s[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v=convertView;
		if (v == null)
        {		   
           LayoutInflater vi = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
           //layout inflater is used to inflate layout XML files in a particular view.
           v = vi.inflate(R.layout.setting_element, null);
        }
		TextView text=(TextView) v.findViewById(R.id.text);
		String msg= s[position];
		text.setText(msg);
		return v;
	}
	
	
	

}
