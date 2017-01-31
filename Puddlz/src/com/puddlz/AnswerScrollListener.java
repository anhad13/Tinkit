package com.puddlz;

import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;

abstract class AnswerScrollListener implements OnScrollListener
{
	//abstract class is one having abstract methods
private int lastposition;


	 @Override
	 public void onScroll(AbsListView view, int firstVisibleItem,int visibleItemCount, int totalItemCount) {
	   int currentPosition = view.getFirstVisiblePosition();
	         if(currentPosition > lastposition) {
	      
	          //scroll down
	     scroll_down();
	  
	         }
	         if(currentPosition < lastposition) {
	             //scroll up
	         
	     scroll_up();
	      
	         }
	         lastposition = currentPosition;
	 }
   
public abstract void scroll_down();
public abstract void scroll_up();
//abstract method is one having no implementation
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // Don't take any action on changed
    }
	
}