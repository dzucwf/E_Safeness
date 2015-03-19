package com.safeness.e_saveness_common.remind;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;

public class ReminderService extends WakeReminderIntentService {

	public ReminderService() {
		super("ReminderService");
			}

	@Override
	void doReminderWork(Intent intent) {
		Log.d("ReminderService", "Doing work.");
		Long rowId = intent.getExtras().getLong(RemindersDbAdapter.KEY_ROWID);


       
        NotificationManager mgr = (NotificationManager)this.getSystemService(this.NOTIFICATION_SERVICE);

        Intent notificationIntent = new Intent();
        notificationIntent.setClassName(this,"com.safeness.patient.ui.activity.MainActivity");
        notificationIntent.putExtra(RemindersDbAdapter.KEY_ROWID, rowId);

        PendingIntent pi = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_ONE_SHOT);

        Notification note=new Notification(android.R.drawable.stat_sys_warning,this. getString(com.safeness.e_saveness_common.R.string.notify_new_task_message), System.currentTimeMillis());
        note.setLatestEventInfo(this, this.getString(com.safeness.e_saveness_common.R.string.notify_new_task_title), this.getString(com.safeness.e_saveness_common.R.string.notify_new_task_message), pi);
        note.defaults |= Notification.DEFAULT_SOUND;
        note.flags |= Notification.FLAG_AUTO_CANCEL;

        // An issue could occur if user ever enters over 2,147,483,647 tasks. (Max int value).
        // I highly doubt this will ever happen. But is good to note.
        int id = (int)((long)rowId);
        mgr.notify(id, note);


        //以下代码不起作用
//        intent.setAction("com.safeness.patient.receiver.PatientRemindReceiver");
//        this.sendBroadcast(intent);


		
		
	}
}
