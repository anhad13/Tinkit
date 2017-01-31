package com.puddlz;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomerAdapter extends BaseAdapter
{
    private ArrayList<Details> data;
    Context c;
   
	
	public CustomerAdapter(ArrayList<Details> details, Context globalContext) {
		
    data=details;	
		
	c=globalContext;
	}
	
	@Override
	public int getCount() {
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
           v = vi.inflate(R.layout.feed_element, null);
        }
		
		TextView t1=(TextView) v.findViewById(R.id.textViewx1);
		TextView t2=(TextView) v.findViewById(R.id.textViewx2);
		TextView t3=(TextView) v.findViewById(R.id.textView2);
		ImageView loc =(ImageView) v.findViewById(R.id.loc_image);
		Details msg= data.get(arg0);
	
	//Bitmap bmp = BitmapFactory.decodeByteArray(msg.get_image(), 0, msg.get_image().length);
	ImageView image = (ImageView) v.findViewById(R.id.imageView1);
	BitmapDrawable bitmapDrawable = new BitmapDrawable(c.getResources(), c.getFileStreamPath(msg.get_image()).getAbsolutePath());
	image.setImageDrawable(bitmapDrawable);	
		//t1.setText(msg.get_image().length);
	
		t1.setText(msg.get_question());
		t2.setText(msg.get_location().replaceAll("null,",""));
		t3.setText(msg.get_name());
		//image.setImageBitmap(bmp);
		String a = msg.get_location();
		try{
		if(!a.equals("random"))
		{
			t2.setVisibility(View.VISIBLE);
			loc.setVisibility(View.VISIBLE);
//	Log.d("Bug",msg.get_question());
		}
		else
		{
			t2.setVisibility(View.GONE);
			loc.setVisibility(View.GONE);
		}
		} catch(NullPointerException e)
		{
			Log.d("exception","tada");
		}
		
		return v;
	}
	
}