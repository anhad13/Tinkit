
package com.puddlz;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

    public class TabsPagerAdapter extends FragmentPagerAdapter
    {


		public TabsPagerAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Fragment getItem(int index) {
			// TODO Auto-generated method stub
			switch(index)
			{
			case 0:
				return new Frag1();

			case 1:
				return new Frag4();
			}
			return null;
		}

		@Override
		public int getCount() {
			//returns no of tabs to be implemented
			return 2;
		}
    	
    }