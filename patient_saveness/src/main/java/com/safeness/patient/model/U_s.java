package com.safeness.patient.model;

import com.safeness.e_saveness_common.dao.Table;
import com.safeness.e_saveness_common.dao.Table.Column;

/**
* U_s
* Project name: com.safeness.patient
* 
* @author wuwenyi1213@163.com on 2015-03-26 10:03:27.
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
@Column(name="u_sid",type=Column.TYPE_STRING,isNull = false) 
 private String u_sid;
public String getU_sid() {
    return u_sid;
}
public void setU_sid(String u_sid) {
    this.u_sid = u_sid;
}
@Column(name="s_id",type=Column.TYPE_STRING,isNull = false) 
 private String s_id;
public String getS_id() {
    return s_id;
}
public void setS_id(String s_id) {
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
@Column(name="suggestTime",type=Column.TYPE_TIMESTAMP) 
 private String suggestTime;
public String getSuggestTime() {
    return suggestTime;
}
public void setSuggestTime(String suggestTime) {
    this.suggestTime = suggestTime;
}
@Column(name="finishedDate",type=Column.TYPE_TIMESTAMP,isNull = false) 
 private String finishedDate;
public String getFinishedDate() {
    return finishedDate;
}
public void setFinishedDate(String finishedDate) {
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
    public U_s(String u_sid,String s_id,double count,double calorie,String finishedDate) {
       this.u_sid = u_sid;
       this.s_id = s_id;
       this.count = count;
       this.calorie = calorie;
       this.finishedDate = finishedDate;
    }

	
}
