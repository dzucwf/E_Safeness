package com.safeness.doctor.model;

import com.safeness.e_saveness_common.dao.Table;
import com.safeness.e_saveness_common.dao.Table.Column;

/**
* Schedule_shift
* Project name: com.safeness.patient
* 
* @author wuwenyi1213@163.com on 2015-03-11 23:03:46.
* Copyright (c) 2015年 wuwenyi1213@163.com. All rights reserved.
*/

@Table(name = "schedule_shift")
public class Schedule_shift {

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
@Column(name="shift_date",type=Column.TYPE_TIMESTAMP,isNull = false) 
 private String shift_date;
public String getShift_date() {
    return shift_date;
}
public void setShift_date(String shift_date) {
    this.shift_date = shift_date;
}
@Column(name="shift_name",type=Column.TYPE_STRING,isNull = false) 
 private String shift_name;
public String getShift_name() {
    return shift_name;
}
public void setShift_name(String shift_name) {
    this.shift_name = shift_name;
}
@Column(name="shift_begin",type=Column.TYPE_TIMESTAMP) 
 private String shift_begin;
public String getShift_begin() {
    return shift_begin;
}
public void setShift_begin(String shift_begin) {
    this.shift_begin = shift_begin;
}
@Column(name="shift_end",type=Column.TYPE_TIMESTAMP) 
 private String shift_end;
public String getShift_end() {
    return shift_end;
}
public void setShift_end(String shift_end) {
    this.shift_end = shift_end;
}
@Column(name="desc",type=Column.TYPE_STRING) 
 private String desc;
public String getDesc() {
    return desc;
}
public void setDesc(String desc) {
    this.desc = desc;
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


	public Schedule_shift() {}
	
	   /**
    *根据所有不允许为null的列，初始化对象
    *
    * @param server_id 参数解释
    * @param shift_date 参数解释
    * @param shift_name 参数解释
    *
    *  @return 对象
    */
    public Schedule_shift(Integer server_id,String shift_date,String shift_name) {
       this.server_id = server_id;
       this.shift_date = shift_date;
       this.shift_name = shift_name;
    }

	
}
