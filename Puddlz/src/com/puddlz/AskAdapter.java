package com.puddlz;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AskAdapter extends BaseAdapter{

	protected Context mContext;
	protected List<Ask_Fields> mMessages;
	
	public AskAdapter(Context context, List<Ask_Fields> messages) {
	
		
		mContext = context;
		mMessages = messages;
		
	}
	
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		View v=arg1;
		if (v == null)
        {		   
           LayoutInflater vi = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
           //layout inflater is used to inflate layout XML files in a particular view.
           v = vi.inflate(R.layout.ask_item, null);
        }
		TextView text_question=(TextView) v.findViewById(R.id.ask_text);
		TextView text_desc=(TextView) v.findViewById(R.id.ask_desc);
		ImageView i = (ImageView) v.findViewById(R.id.ask_image);
		Ask_Fields msg= mMessages.get(arg0);
		SpannableString spanString = new SpannableString(msg.Name);
		spanString.setSpan(new StyleSpan(Typeface.BOLD), 0, spanString.length(), 0);
		text_question.setText(spanString);
		text_desc.setText(msg.Tag);
		text_desc.setTextColor(0xFF0284B4);
		text_question.setTextColor(0xFFFF0000);
		if(msg.Name.equals("Restaurants"))
		{
		i.setImageResource(R.drawable.restaurant);
		//text_desc.setTextColor(Color.RED);
		//text_question.setTextColor(0xFF0F00D2);
				}
		else if(msg.Name.equals("Movies"))
		{ 
			i.setImageResource(R.drawable.m1);
			//text_desc.setTextColor(0xFF844A00);
			//text_question.setTextColor(0xFFFFAE00);
        }
		else if(msg.Name.equals("Electronic Gadgets"))
		{
			i.setImageResource(R.drawable.m5_electronic);
			//text_question.setTextColor(Color.RED);
			//text_desc.setTextColor(0xFF8B1EFC);
		}
		else if(msg.Name.equals("Mobile Apps"))
		{
			i.setImageResource(R.drawable.m3_apps);
			//text_desc.setTextColor(0xFF8AA500);
			//text_question.setTextColor(0xFFFBA103);
		}
		else if(msg.Name.equals("Other"))
		{
			i.setImageResource(R.drawable.m7_others);
			//text_desc.setTextColor(0xFFFF0000);
			//text_question.setTextColor(0xFFF000FF);
		}
		else
		{
			i.setImageResource(R.drawable.doctor);
			//text_desc.setTextColor(Color.BLACK);
			//text_question.setTextColor(0xFFFF0000);
		}
		return v;
	}
	
	

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mMessages.size();
		
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return mMessages.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}
	
}
