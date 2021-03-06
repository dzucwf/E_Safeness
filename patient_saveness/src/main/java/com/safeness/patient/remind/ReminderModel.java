package com.safeness.patient.remind;

import java.io.Serializable;



/**
 * Created by EISAVISA on 2015/3/17.
 */
public class ReminderModel implements Serializable {

    private  long rowId;

    private String title;

    public long getRowId() {
        return rowId;
    }

    public void setRowId(long rowId) {
        this.rowId = rowId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    private  String body;
    private  String type;
    private  String user;
    private  String date_time;

    public String getEnd_date_time() {
        return end_date_time;
    }

    public void setEnd_date_time(String end_date_time) {
        this.end_date_time = end_date_time;
    }

    private  String end_date_time;

    public boolean isCanReminde() {
        return canReminde;
    }

    public void setCanReminde(boolean canReminde) {
        this.canReminde = canReminde;
    }

    private boolean canReminde;

    public String getUnique_id() {
        return unique_id;
    }

    public String getRemindTime() {
        return remindTime;
    }

    public void setRemindTime(String remindTime) {
        this.remindTime = remindTime;
    }

    public void setUnique_id(String unique_id) {
        this.unique_id = unique_id;
    }

    private String unique_id;

    private String remindTime;
}
