package com.safeness.patient.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.safeness.e_saveness_common.remind.RemindersDbAdapter;
import com.safeness.patient.ui.activity.MainActivity;

public class PatientRemindReceiver extends BroadcastReceiver {
    public PatientRemindReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        Long rowId = intent.getExtras().getLong(RemindersDbAdapter.KEY_ROWID);

        NotificationManager mgr = (NotificationManager)context.getSystemService(context.NOTIFICATION_SERVICE);

        Intent notificationIntent = new Intent(context, MainActivity.class);

        notificationIntent.putExtra(RemindersDbAdapter.KEY_ROWID, rowId);

        PendingIntent pi = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_ONE_SHOT);

        Notification note=new Notification(android.R.drawable.stat_sys_warning,context. getString(com.safeness.e_saveness_common.R.string.notify_new_task_message), System.currentTimeMillis());
        note.setLatestEventInfo(context, context.getString(com.safeness.e_saveness_common.R.string.notify_new_task_title), context.getString(com.safeness.e_saveness_common.R.string.notify_new_task_message), pi);
        note.defaults |= Notification.DEFAULT_SOUND;
        note.flags |= Notification.FLAG_AUTO_CANCEL;

        // An issue could occur if user ever enters over 2,147,483,647 tasks. (Max int value).
        // I highly doubt this will ever happen. But is good to note.
        int id = (int)((long)rowId);
        mgr.notify(id, note);

       // throw new UnsupportedOperationException("Not yet implemented");
    }
}
