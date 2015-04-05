package com.safeness.patient.remind;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ComponentInfo;
import android.database.Cursor;
import android.util.Log;

import java.text.ParseException;


//TODO:这个接受器不应该是在启动的时候就打开，以后需要在配置文件里修改
public class OnBootReceiver extends BroadcastReceiver {
	
	private static final String TAG = ComponentInfo.class.getCanonicalName();  
	
	@Override
	public void onReceive(Context context, Intent intent) {

		ReminderManager reminderMgr = new ReminderManager(context);
		String user = intent.getStringExtra("user");
		RemindersDbAdapter dbHelper = new RemindersDbAdapter(context);
		dbHelper.open();
			
		Cursor cursor = dbHelper.fetchReminderByUser(user);
		
		if(cursor != null) {
			cursor.moveToFirst(); 
			

            while(cursor.isAfterLast() == false) {


                try {
                   ReminderModel reminder = reminderMgr.getReminderByCursor(cursor);
                   reminderMgr.setReminder(reminder);
                } catch (ParseException e) {
                    e.printStackTrace();
                    Log.e("OnBootReceiver", e.getMessage(), e);
                }

				

				cursor.moveToNext(); 
			}
			cursor.close() ;	
		}
		
		dbHelper.close(); 
	}
}

