package com.safeness.patient.model;

import com.safeness.patient.dao.Table;
import com.safeness.patient.dao.Table.Column;

import java.sql.Date;

/**
* U_f
* Project name: com.safeness.patient
* 
* @author wuwenyi1213@163.com on 2015-01-25 05:01:47.
* Copyright (c) 2015年 wuwenyi1213@163.com. All rights reserved.
*/

@Table(name = "u_f")
public class U_f {

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
@Column(name="f_id",type=Column.TYPE_INTEGER,isNull = false) 
 private Integer f_id;
public Integer getF_id() {
    return f_id;
}
public void setF_id(Integer f_id) {
    this.f_id = f_id;
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
 private Date finishedDate;
public Date getFinishedDate() {
    return finishedDate;
}
public void setFinishedDate(Date finishedDate) {
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
 private Date create_datetime;
public Date getCreate_datetime() {
    return create_datetime;
}
public void setCreate_datetime(Date create_datetime) {
    this.create_datetime = create_datetime;
}
@Column(name="update_datetime",type=Column.TYPE_TIMESTAMP,defaultValue = "CURRENT_TIMESTAMP") 
 private Date update_datetime;
public Date getUpdate_datetime() {
    return update_datetime;
}
public void setUpdate_datetime(Date update_datetime) {
    this.update_datetime = update_datetime;
}


	public U_f() {}
	
	   /**
    *根据所有不允许为null的列，初始化对象
    *
    * @param u_sid 参数解释
    * @param f_id 参数解释
    * @param count 参数解释
    * @param calorie 参数解释
    * @param finishedDate 参数解释
    *
    *  @return 对象
    */
    public U_f(Integer u_sid,Integer f_id,double count,double calorie,Date finishedDate) {
       this.u_sid = u_sid;
       this.f_id = f_id;
       this.count = count;
       this.calorie = calorie;
       this.finishedDate = finishedDate;
    }

	
}