package com.puddlz;


public class Details
{
	String no_answers;
	String question;
	String object_id;
	String name;
	String image_name;
	String location;
	
	public String getno()
	{
		return no_answers;
	}
	public void setno(String no_answers)
	{
		this.no_answers=no_answers;
	}
	public String get_question()
	{
		return question;
	
	}
	public void set_question(String question)
	{
		this.question=question;
	}
	public String get_name()
	{
		return name;
	
	}
	public void set_name(String name)
	{
		this.name=name;
	}
	public void set_objectid(String object_id1)
	{
		this.object_id=object_id1;
	}
	public String get_objectid()
	{
		return object_id;
	}
	public String get_image()
	{
		return this.image_name;
	}
	public void set_image(String image1)
	{
		this.image_name=image1;
	}
	public void set_location(String loc)
	{location=loc;
	
	}
	public String get_location()
	{
		return location;
	}
	
	
}