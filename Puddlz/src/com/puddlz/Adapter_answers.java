package com.puddlz;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class Adapter_answers extends BaseAdapter
{
    private ArrayList<Details_answers> data;
    Context c;
   
	
	public Adapter_answers(ArrayList<Details_answers> details, Context globalContext) {
		
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
           v = vi.inflate(R.layout.answer_element, null);
        }
		TextView t1=(TextView) v.findViewById(R.id.textViewx2);
	    TextView name=(TextView) v.findViewById(R.id.textViewy2);
		Details_answers msg= data.get(arg0);
	    name.setText(msg.get_name());
		t1.setText(msg.get_comment());
		
		return v;
	}
	
}