package com.safeness.patient.remind;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class OnAlarmReceiver extends BroadcastReceiver {

	private static final String TAG = "OnAlarmReceiver";
	
	
	@Override	
	public void onReceive(Context context, Intent intent) {
		Log.d(TAG, "Received wake up from alarm manager.");
		
		String uniqueID = intent.getExtras().getString(RemindersDbAdapter.KEY_ROWID);
		
		WakeReminderIntentService.acquireStaticLock(context);
		
		Intent i = new Intent(context, ReminderService.class); 
		i.putExtra(RemindersDbAdapter.KEY_ROWID, uniqueID);
		context.startService(i);
		 
	}
}
