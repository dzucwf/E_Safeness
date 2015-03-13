package com.safeness.doctor.model;

import com.safeness.e_saveness_common.dao.Table;
import com.safeness.e_saveness_common.dao.Table.Column;

/**
* Schedule_remind
* Project name: com.safeness.patient
* 
* @author wuwenyi1213@163.com on 2015-03-11 23:03:45.
* Copyright (c) 2015年 wuwenyi1213@163.com. All rights reserved.
*/

@Table(name = "schedule_remind")
public class Schedule_remind {

	@Column(name="_id",type=Column.TYPE_INTEGER,isPrimaryKey=true,isNull = false,isUnique = true) 
 private Integer _id;
public Integer get_id() {
    return _id;
}
public void set_id(Integer _id) {
    this._id = _id;
}
@Column(name="server_id",type=Column.TYPE_INTEGER,isNull = false,isUnique = true) 
 private Integer server_id;
public Integer getServer_id() {
    return server_id;
}
public void setServer_id(Integer server_id) {
    this.server_id = server_id;
}
@Column(name="schedule_date",type=Column.TYPE_TIMESTAMP,isNull = false) 
 private String schedule_date;
public String getSchedule_date() {
    return schedule_date;
}
public void setSchedule_date(String schedule_date) {
    this.schedule_date = schedule_date;
}
@Column(name="title",type=Column.TYPE_STRING,isNull = false) 
 private String title;
public String getTitle() {
    return title;
}
public void setTitle(String title) {
    this.title = title;
}
@Column(name="content",type=Column.TYPE_STRING,isNull = false) 
 private String content;
public String getContent() {
    return content;
}
public void setContent(String content) {
    this.content = content;
}
@Column(name="before_or_after",type=Column.TYPE_INTEGER,isNull = false,defaultValue = "0") 
 private Integer before_or_after;
public Integer getBefore_or_after() {
    return before_or_after;
}
public void setBefore_or_after(Integer before_or_after) {
    this.before_or_after = before_or_after;
}
@Column(name="life_status",type=Column.TYPE_INTEGER,defaultValue = "1") 
 private Integer life_status;
public Integer getLife_status() {
    return life_status;
}
public void setLife_status(Integer life_status) {
    this.life_status = life_status;
}
@Column(name="create_datetime",type=Column.TYPE_TIMESTAMP,defaultValue = "CURRENT_TIMESTAMP") 
 private String create_datetime;
public String getCreate_datetime() {
    return create_datetime;
}
public void setCreate_datetime(String create_datetime) {
    this.create_datetime = create_datetime;
}
@Column(name="update_datetime",type=Column.TYPE_TIMESTAMP,defaultValue = "CURRENT_TIMESTAMP") 
 private String update_datetime;
public String getUpdate_datetime() {
    return update_datetime;
}
public void setUpdate_datetime(String update_datetime) {
    this.update_datetime = update_datetime;
}


	public Schedule_remind() {}
	
	   /**
    *根据所有不允许为null的列，初始化对象
    *
    * @param server_id 参数解释
    * @param schedule_date 参数解释
    * @param title 参数解释
    * @param content 参数解释
    *
    *  @return 对象
    */
    public Schedule_remind(Integer server_id,String schedule_date,String title,String content) {
       this.server_id = server_id;
       this.schedule_date = schedule_date;
       this.title = title;
       this.content = content;
    }

	
}
