package com.safeness.patient.model;

import com.safeness.patient.dao.Table;
import com.safeness.patient.dao.Table.Column;

import java.sql.Date;

/**
* U_s
* Project name: com.safeness.patient
* 
* @author wuwenyi1213@163.com on 2015-01-25 16:01:20.
* Copyright (c) 2015年 wuwenyi1213@163.com. All rights reserved.
*/

@Table(name = "u_s")
public class U_s {

	@Column(name="_id",type=Column.TYPE_INTEGER,isPrimaryKey=true,isUnique = true) 
 private Integer _id;
public Integer get_id() {
    return _id;
}
public void set_id(Integer _id) {
    this._id = _id;
}
@Column(name="u_sid",type=Column.TYPE_INTEGER,isNull = false) 
 private Integer u_sid;
public Integer getU_sid() {
    return u_sid;
}
public void setU_sid(Integer u_sid) {
    this.u_sid = u_sid;
}
@Column(name="s_id",type=Column.TYPE_INTEGER,isNull = false) 
 private Integer s_id;
public Integer getS_id() {
    return s_id;
}
public void setS_id(Integer s_id) {
    this.s_id = s_id;
}
@Column(name="count",type=Column.TYPE_DOUBLE,isNull = false) 
 private double count;
public double getCount() {
    return count;
}
public void setCount(double count) {
    this.count = count;
}
@Column(name="calorie",type=Column.TYPE_DOUBLE,isNull = false) 
 private double calorie;
public double getCalorie() {
    return calorie;
}
public void setCalorie(double calorie) {
    this.calorie = calorie;
}
@Column(name="finishedDate",type=Column.TYPE_TIMESTAMP,isNull = false) 
 private long finishedDate;
public long getFinishedDate() {
    return finishedDate;
}
public void setFinishedDate(long finishedDate) {
    this.finishedDate = finishedDate;
}
@Column(name="life_status",type=Column.TYPE_INTEGER,isNull = false,defaultValue = "1") 
 private Integer life_status;
public Integer getLife_status() {
    return life_status;
}
public void setLife_status(Integer life_status) {
    this.life_status = life_status;
}
@Column(name="create_datetime",type=Column.TYPE_TIMESTAMP,defaultValue = "CURRENT_TIMESTAMP") 
 private long create_datetime;
public long getCreate_datetime() {
    return create_datetime;
}
public void setCreate_datetime(long create_datetime) {
    this.create_datetime = create_datetime;
}
@Column(name="update_datetime",type=Column.TYPE_TIMESTAMP,defaultValue = "CURRENT_TIMESTAMP") 
 private long update_datetime;
public long getUpdate_datetime() {
    return update_datetime;
}
public void setUpdate_datetime(long update_datetime) {
    this.update_datetime = update_datetime;
}


	public U_s() {}
	
	   /**
    *根据所有不允许为null的列，初始化对象
    *
    * @param u_sid 参数解释
    * @param s_id 参数解释
    * @param count 参数解释
    * @param calorie 参数解释
    * @param finishedDate 参数解释
    *
    *  @return 对象
    */
    public U_s(Integer u_sid,Integer s_id,double count,double calorie,long finishedDate) {
       this.u_sid = u_sid;
       this.s_id = s_id;
       this.count = count;
       this.calorie = calorie;
       this.finishedDate = finishedDate;
    }

	
}
