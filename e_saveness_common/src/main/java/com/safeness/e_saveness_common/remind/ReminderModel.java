package com.safeness.e_saveness_common.remind;

import java.io.Serializable;



/**
 * Created by EISAVISA on 2015/3/17.
 */
public class ReminderModel implements Serializable {

    private  int rowId;

    private String title;

    public int getRowId() {
        return rowId;
    }

    public void setRowId(int rowId) {
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

    public boolean isCanReminde() {
        return canReminde;
    }

    public void setCanReminde(boolean canReminde) {
        this.canReminde = canReminde;
    }

    private boolean canReminde;
}
