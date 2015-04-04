package com.safeness.patient.remind;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ComponentInfo;
import android.database.Cursor;
import android.util.Log;

import com.safeness.e_saveness_common.util.DateTimeUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;


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
			
			int rowIdColumnIndex = cursor.getColumnIndex(RemindersDbAdapter.KEY_ROWID);
			int dateTimeColumnIndex = cursor.getColumnIndex(RemindersDbAdapter.KEY_DATE_TIME); 
			int canRemindIndex = cursor.getColumnIndex(RemindersDbAdapter.KEY_CAN_REMIND);
            int endTimeIndex = cursor.getColumnIndex(RemindersDbAdapter.KEY_END_DATE_TIME);
            while(cursor.isAfterLast() == false) {

				Log.d(TAG, "Adding alarm from boot.");
				Log.d(TAG, "Row Id Column Index - " + rowIdColumnIndex);
				Log.d(TAG, "Date Time Column Index - " + dateTimeColumnIndex);
				
				Long rowId = cursor.getLong(rowIdColumnIndex); 
				String dateTime = cursor.getString(dateTimeColumnIndex);
                String endTime = cursor.getString(endTimeIndex);
                try {
                    //过期不在提醒
                    Calendar endCal = DateTimeUtil.getSelectCalendar(endTime,"");
                    if(DateTimeUtil.compareCal(endCal,Calendar.getInstance())){
                        //是否需要提醒
                        boolean canRemind = cursor.getInt(canRemindIndex) == 1?true:false;
                        Calendar cal = Calendar.getInstance();
                        SimpleDateFormat format = new SimpleDateFormat(DateTimeUtil.DATE_TIME_FORMAT);
                        java.util.Date date = format.parse(dateTime);
                        cal.setTime(date);
                        if(canRemind){
                            reminderMgr.setReminder(rowId, cal);
                        }
                    }

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

