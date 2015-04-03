package com.safeness.patient.remind;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import com.safeness.e_saveness_common.util.DateTimeUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ReminderManager {

	private Context mContext; 
	private AlarmManager mAlarmManager;
    private Long mRowId;
    private RemindersDbAdapter mDbHelper;
    private static final int INTERVAL = 1000 * 60 * 60 * 24;
   // private static final int INTERVAL = 1000;
    public ReminderManager(Context context) {
		mContext = context; 
		mAlarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        mDbHelper = new RemindersDbAdapter(mContext);
	}
	
	 void setReminder(Long taskId, Calendar when) {
		
        Intent i = new Intent(mContext, OnAlarmReceiver.class);
         i.setAction(taskId+"");
        i.putExtra(RemindersDbAdapter.KEY_ROWID, (long)taskId); 

         PendingIntent pi = PendingIntent.getBroadcast(mContext, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
         //mAlarmManager.set(AlarmManager.RTC_WAKEUP, when.getTimeInMillis(), pi);
         mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, when.getTimeInMillis(),INTERVAL, pi);



	}

    public void setTempReminder(Long taskId,Calendar when){
        Intent i = new Intent(mContext, OnAlarmReceiver.class);
        i.setAction(taskId+"");
        i.putExtra(RemindersDbAdapter.KEY_ROWID, (long)taskId);

        PendingIntent pi = PendingIntent.getBroadcast(mContext, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        mAlarmManager.set(AlarmManager.RTC_WAKEUP, when.getTimeInMillis(), pi);
    }

    public long saveState(String title,String body,String mCalendar,String user,String type,boolean canRemind,Calendar StartCal,Calendar endCal){
        mDbHelper.open();
        String[] timeStr = mCalendar.split(":");
        if(timeStr != null && timeStr.length>1){
            StartCal.set(Calendar.HOUR_OF_DAY,Integer.parseInt(timeStr[0]));
            StartCal.set(Calendar.MINUTE,Integer.parseInt(timeStr[1]));
        }

        SimpleDateFormat dateTimeFormat = new SimpleDateFormat(DateTimeUtil.DATE_TIME_FORMAT);
        String reminderDateTime = dateTimeFormat.format(StartCal.getTime());

        String endDateTime = dateTimeFormat.format(endCal.getTime());
        if (mRowId == null) {

            long id = mDbHelper.createReminder(title, body, reminderDateTime,user,type,endDateTime);
            if (id > 0) {
                mRowId = id;
            }
        } else {
            mDbHelper.updateReminder(mRowId, title, body, reminderDateTime,user,type,canRemind,endDateTime);
        }

        setReminder(mRowId, StartCal);
        mDbHelper.close();
        return mRowId;
    }



    public void cancelReminder(long id){

        Intent i = new Intent(mContext, OnAlarmReceiver.class);
        i.setAction(id+"");
        PendingIntent pi = PendingIntent.getBroadcast(mContext, 0, i, 0);
        //mAlarmManager.set(AlarmManager.RTC_WAKEUP, when.getTimeInMillis(), pi);
        mAlarmManager.cancel(pi);
        mDbHelper.open();
        mDbHelper.updateReminderCanOrEnable(id,false);
        mDbHelper.close();
    }

    public void reSetReminder(long id,Calendar mCalendar){

        setReminder(mRowId, mCalendar);
        mDbHelper.open();
        mDbHelper.updateReminderCanOrEnable(id,true);
        mDbHelper.close();
    }

    public void deleteReminder(long id ){

        mDbHelper.open();
        mDbHelper.deleteReminder(id);
        mDbHelper.close();

    }

    public List<ReminderModel> getReminderByUser(String user){
        List<ReminderModel> returnList = new ArrayList<ReminderModel>();

        mDbHelper.open();
        Cursor remindersCursor = mDbHelper.fetchReminderByUser(user);
        fillList(returnList,remindersCursor);
        mDbHelper.close();
        return returnList;
    }

    public List<ReminderModel> getReminderByUserAndType(String user,String type){
        List<ReminderModel> returnList = new ArrayList<ReminderModel>();

        mDbHelper.open();
        Cursor remindersCursor = mDbHelper.fetchReminderByUserAndType(user,type);
        fillList(returnList,remindersCursor);
        mDbHelper.close();
        return returnList;
    }

    public ReminderModel getReminderByID(long id){
        ReminderModel model = null;
        mDbHelper.open();
        Cursor remindersCursor = mDbHelper.fetchReminder(id);
        remindersCursor.moveToFirst();

       model = getReminderByCursor(remindersCursor);

        mDbHelper.close();
        return model;
    }


    private  void fillList(List<ReminderModel> list,Cursor remindersCursor){
        while (remindersCursor.moveToNext()){
            ReminderModel model = getReminderByCursor(remindersCursor);

            list.add(model);

        }
    }

    /**
     * 根据游标查询数据
     * @param remindersCursor
     * @return
     */
    private ReminderModel getReminderByCursor(Cursor remindersCursor){
        ReminderModel model = new ReminderModel();
        long rowID =remindersCursor.getLong(remindersCursor.getColumnIndex(RemindersDbAdapter.KEY_ROWID));
        String title =remindersCursor.getString(remindersCursor.getColumnIndex(RemindersDbAdapter.KEY_TITLE));
        String body =remindersCursor.getString(remindersCursor.getColumnIndex(RemindersDbAdapter.KEY_BODY));
        String type =remindersCursor.getString(remindersCursor.getColumnIndex(RemindersDbAdapter.KEY_TYPE));
        String user =remindersCursor.getString(remindersCursor.getColumnIndex(RemindersDbAdapter.KEY_USER));
        String remindTime =remindersCursor.getString(remindersCursor.getColumnIndex(RemindersDbAdapter.KEY_DATE_TIME));

        int canReminder = remindersCursor.getInt(remindersCursor.getColumnIndex(RemindersDbAdapter.KEY_CAN_REMIND));
        model.setRowId(rowID);
        model.setTitle(title);
        model.setBody(body);
        model.setType(type);
        model.setUser(user);
        model.setDate_time(remindTime);
        model.setCanReminde(canReminder==1?true:false);
        return model;
    }


}
