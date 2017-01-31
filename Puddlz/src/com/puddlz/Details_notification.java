package com.puddlz;

import com.parse.ParseObject;

public class Details_notification
{
	boolean is_read;
	String type;//what type of notification
	String text;
	String details;
	ParseObject object_id;
	String parent;
	int count=1;	
	boolean get_is_read()
	{
		return is_read;

	}

	void set_is_read(boolean read)
	{
		this.is_read=read;
	}
	String get_type()
	{
		return type;

	}

	void set_type(String type)
	{
		this.type=type;
	}

	String get_text()
	{
		return text;

	}

	void set_text(String text)
	{
		this.text=text;
	}
	String get_details()
	{
		return details;

	}

	void set_details(String details)
	{
		this.details=details;
	}

	ParseObject get_object()
	{
		return object_id;

	}

	void set_object(ParseObject object_id)
	{
		this.object_id=object_id;
	}
	void set_parent(String parse_object)
	{
		this.parent=parse_object;

	}
	String get_parent()
	{
		return parent;

	}

	int get_count()
	{
		return count;
	}
	void set_count(int count)
	{
		this.count=count;	
	}

}