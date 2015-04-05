package com.safeness.patient.remind;

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
		String uniqueId = intent.getExtras().getString(RemindersDbAdapter.KEY_ROWID);


       
        NotificationManager mgr = (NotificationManager)this.getSystemService(this.NOTIFICATION_SERVICE);

        Intent notificationIntent = new Intent();
        notificationIntent.setClassName(this,"com.safeness.patient.ui.activity.MainActivity");
        notificationIntent.putExtra(RemindersDbAdapter.KEY_ROWID, uniqueId);

        PendingIntent pi = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_ONE_SHOT);
        ReminderManager manager = new ReminderManager(this);
        ReminderModel model =  manager.getReminderByUniqueId(uniqueId);


        try {
                Notification note=new Notification(android.R.drawable.stat_sys_warning,model.getTitle(), System.currentTimeMillis());
                note.setLatestEventInfo(this, model.getTitle(), model.getBody(), pi);
                note.defaults |= Notification.DEFAULT_SOUND;
                note.flags |= Notification.FLAG_AUTO_CANCEL;

                // An issue could occur if user ever enters over 2,147,483,647 tasks. (Max int value).
                // I highly doubt this will ever happen. But is good to note.

                mgr.notify((int)model.getRowId(), note);

                Intent it = new Intent("com.safeness.patient.receiver.PatientRemindReceiver");

                it.putExtra("reminder",model);
                this.sendBroadcast(it);
           // }
        } catch (Exception e) {
            e.printStackTrace();
        }



		
		
	}


}
