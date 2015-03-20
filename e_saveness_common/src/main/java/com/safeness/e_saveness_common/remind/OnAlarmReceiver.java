package com.safeness.e_saveness_common.remind;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class OnAlarmReceiver extends BroadcastReceiver {

	private static final String TAG = "OnAlarmReceiver";
	
	
	@Override	
	public void onReceive(Context context, Intent intent) {
		Log.d(TAG, "Received wake up from alarm manager.");
		
		long rowid = intent.getExtras().getLong(RemindersDbAdapter.KEY_ROWID);
		
		WakeReminderIntentService.acquireStaticLock(context);
		
		Intent i = new Intent(context, ReminderService.class); 
		i.putExtra(RemindersDbAdapter.KEY_ROWID, rowid);  
		context.startService(i);
		 
	}
}
