package com.safeness.patient.remind;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;

import com.safeness.app.PatientApplication;
import com.safeness.e_saveness_common.util.DateTimeUtil;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReminderManager {

	private Context mContext; 
	private AlarmManager mAlarmManager;
    private Long mRowId;
    private RemindersDbAdapter mDbHelper;
    private static final int INTERVAL = 1000 * 60 * 60 * 24;

    //这里是所有的提醒
    private static Map<String,ReminderModel> currentReminderMap;
   // private static final int INTERVAL = 1000;
    public ReminderManager(Context context) {
		mContext = context; 
		mAlarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        mDbHelper = new RemindersDbAdapter(mContext);


	}
	
	 void setReminder(ReminderModel model) throws ParseException {

         if(currentReminderMap == null){
             currentReminderMap = new HashMap<String, ReminderModel>();
         }
         if(canRemind(model)){
             //如果没有设置过才提醒
             if(!currentReminderMap.containsKey(model.getUnique_id())){
                 currentReminderMap.put(model.getUnique_id(),model);
                 Intent i = new Intent(mContext, OnAlarmReceiver.class);
                 i.setAction(model.getUnique_id());
                 i.putExtra(RemindersDbAdapter.KEY_ROWID, model.getUnique_id());

                 PendingIntent pi = PendingIntent.getBroadcast(mContext, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
                 //mAlarmManager.set(AlarmManager.RTC_WAKEUP, when.getTimeInMillis(), pi);
                 Calendar reminderDate = Calendar.getInstance();
                 String[] timeAndDate = model.getRemindTime().split(":");
                 if(timeAndDate!=null && timeAndDate.length>1){
                     reminderDate.set(Calendar.HOUR_OF_DAY,Integer.parseInt(timeAndDate[0]));
                     reminderDate.set(Calendar.MINUTE,Integer.parseInt(timeAndDate[1]));
                 }
                 mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, reminderDate.getTimeInMillis(),INTERVAL, pi);
             }

         }



	}

    /**
     * 是否可以添加到系统提醒中
     * @param model
     * @return true 可以，false 不可以
     * @throws ParseException
     */
    private boolean canRemind(ReminderModel model) throws ParseException {

        Calendar now = Calendar.getInstance();
        Calendar remindStartTime = DateTimeUtil.getSelectCalendar(model.getDate_time(),"");
        Calendar remindEndTime =  DateTimeUtil.getSelectCalendar(model.getEnd_date_time(),"");
        //如果该提醒是取消状态直接返回false
        if(!model.isCanReminde()){
            return false;
        }
        //如果提醒开始时间大于当前时间返回false
        if(remindStartTime.compareTo(now)>0){
            return false;
        }else{
            //如果提醒最晚时间小于当前时间返回false
            if(now.compareTo(remindEndTime)>0){
                return false;
            }else{
                Calendar compareRemind = Calendar.getInstance();
                String[] remindTime = model.getRemindTime().split(":");
                compareRemind.set(Calendar.HOUR_OF_DAY,Integer.parseInt(remindTime[0]));
                compareRemind.set(Calendar.MINUTE,Integer.parseInt(remindTime[1]));
                if(now.compareTo(compareRemind)>0) {
                    return false;
                }
            }
        }
        return true;
    }

    private static  final String SPLIT_STR = "_";
    public void createUniqueId(ReminderModel model,String drugId) throws ParseException {
        StringBuffer sb = new StringBuffer();
        //sb.append("'");
        sb.append(drugId+SPLIT_STR);
        Calendar start = DateTimeUtil.getSelectCalendar(model.getDate_time(),"");
        Calendar end = DateTimeUtil.getSelectCalendar(model.getEnd_date_time(),"");

        sb.append(DateTimeUtil.getSelectedDate(start,"yyyyMMdd")+SPLIT_STR);
        sb.append(DateTimeUtil.getSelectedDate(end, "yyyyMMdd")+SPLIT_STR);

        sb.append(model.getRemindTime().replace(":","_")+SPLIT_STR);
        sb.append(PatientApplication.getInstance().getUserName()+SPLIT_STR);
        sb.append(model.getType());
        //sb.append("'");
        model.setUnique_id(sb.toString());
    }

    public void setTempReminder(String uniqueId,Calendar when){
        Intent i = new Intent(mContext, OnAlarmReceiver.class);
        i.setAction(uniqueId);
        i.putExtra(RemindersDbAdapter.KEY_ROWID, uniqueId);

        PendingIntent pi = PendingIntent.getBroadcast(mContext, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);

        if(DateTimeUtil.compareCal(when,Calendar.getInstance())){
            mAlarmManager.set(AlarmManager.RTC_WAKEUP, when.getTimeInMillis(), pi);
        }else{

        }

    }



    public long saveState(ReminderModel reminder) throws ParseException {

        mDbHelper.open();
        long rowID = mDbHelper.CreateOrUpdateReminder(reminder);

        setReminder(reminder);
       // mDbHelper.close();
        return rowID;


    }



    public void cancelReminder(String uniqueId){

        Intent i = new Intent(mContext, OnAlarmReceiver.class);
        i.setAction(uniqueId+"");
        PendingIntent pi = PendingIntent.getBroadcast(mContext, 0, i, 0);
        //mAlarmManager.set(AlarmManager.RTC_WAKEUP, when.getTimeInMillis(), pi);
        mAlarmManager.cancel(pi);
        mDbHelper.open();
        mDbHelper.updateReminderCanOrEnable(uniqueId,false);
        mDbHelper.close();
    }

    public void reSetReminder(ReminderModel model) throws ParseException {

        setReminder(model);
        mDbHelper.open();
        mDbHelper.updateReminderCanOrEnable(model.getUnique_id(),true);
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
        Cursor remindersCursor = mDbHelper.fetchReminderByUserAndType(user, type);
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

    /**
     * 根据唯一id进行查询
     * @param id
     * @return
     */
    public ReminderModel getReminderByUniqueId(String id){
        ReminderModel model = null;
        mDbHelper.open();
        Cursor remindersCursor = mDbHelper.fetchReminderByUniqueId(id);
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
     ReminderModel getReminderByCursor(Cursor remindersCursor) throws CursorIndexOutOfBoundsException{
        ReminderModel model = new ReminderModel();
        try {
            long rowID =remindersCursor.getLong(remindersCursor.getColumnIndex(RemindersDbAdapter.KEY_ROWID));
            String title =remindersCursor.getString(remindersCursor.getColumnIndex(RemindersDbAdapter.KEY_TITLE));
            String body =remindersCursor.getString(remindersCursor.getColumnIndex(RemindersDbAdapter.KEY_BODY));
            String type =remindersCursor.getString(remindersCursor.getColumnIndex(RemindersDbAdapter.KEY_TYPE));
            String user =remindersCursor.getString(remindersCursor.getColumnIndex(RemindersDbAdapter.KEY_USER));
            String statTime =remindersCursor.getString(remindersCursor.getColumnIndex(RemindersDbAdapter.KEY_DATE_TIME));
            String endTime =remindersCursor.getString(remindersCursor.getColumnIndex(RemindersDbAdapter.KEY_END_DATE_TIME));
            int canReminder = remindersCursor.getInt(remindersCursor.getColumnIndex(RemindersDbAdapter.KEY_CAN_REMIND));
            String unique_id =remindersCursor.getString(remindersCursor.getColumnIndex(RemindersDbAdapter.KEY_UNIQUE_ID));
            String remindTime =remindersCursor.getString(remindersCursor.getColumnIndex(RemindersDbAdapter.KEY_REMIND_TIME));
            model.setRowId(rowID);
            model.setTitle(title);
            model.setBody(body);
            model.setType(type);
            model.setUser(user);
            model.setDate_time(statTime);
            model.setRemindTime(remindTime);
            model.setEnd_date_time(endTime);
            model.setUnique_id(unique_id);
            model.setCanReminde(canReminder==1?true:false);
         }catch (CursorIndexOutOfBoundsException ex){
            ex.printStackTrace();
         }


        return model;
    }


}
