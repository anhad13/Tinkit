package com.puddlz;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class Adapter_question extends BaseAdapter
{
    private ArrayList<Details_question> data;
    Context c;
   
	
	public Adapter_question(ArrayList<Details_question> details, Context globalContext) {
		
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
           v = vi.inflate(R.layout.question_element, null);
        }
		TextView text_question=(TextView) v.findViewById(R.id.text_question);
		Details_question msg= data.get(arg0);
		text_question.setText(msg.get_question());
		return v;
	}
	
}