package com.safeness.e_saveness_common.net;

import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class MBaseActivity extends Activity{
	
	private boolean TouchClose = true;
	public void SetTouchClose(boolean TouchClose)
	{
		this.TouchClose = TouchClose;
	}
	
	public OnTouchListener movingEventListener = new OnTouchListener() { 
		int lastX, lastY; 
		
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if(TouchClose)
			{
				switch (event.getAction()) {
				 case MotionEvent.ACTION_DOWN: 
					 lastX = (int) event.getRawX();  
					 lastY = (int) event.getRawY();  
					 break;
				 case MotionEvent.ACTION_MOVE: 
					 int dx = (int) event.getRawX() - lastX;  
					 int dy = (int) event.getRawY() - lastY;  
					 
					 if((dx - lastX) > 50)
					 {
						 finish();
						 return true;
					 }
					 
					 break;
				 case MotionEvent.ACTION_UP: 
					 
					 break;
				 }
			}
			return false;
		}
	};
	
	int lastX, lastY; 
	public boolean onTouchEvent(MotionEvent event) {
		if(TouchClose)
		{
			switch (event.getAction()) {
			 case MotionEvent.ACTION_DOWN: 
				 lastX = (int) event.getRawX();  
				 lastY = (int) event.getRawY();  
				 break;
			 case MotionEvent.ACTION_MOVE: 
				 int dx = (int) event.getRawX() - lastX;  
				 int dy = (int) event.getRawY() - lastY;  
				 
				 if((dx - lastX) > 50)
				 {
					 finish();
					 return true;
				 }
				 
				 break;
			 case MotionEvent.ACTION_UP: 
				 
				 break;
			 }
		}
		return false;
	};
	
}
