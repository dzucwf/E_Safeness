package com.safeness.patient.model;

import com.safeness.e_saveness_common.dao.Table;
import com.safeness.e_saveness_common.dao.Table.Column;

/**
* U_f
* Project name: com.safeness.patient
* 
* @author wuwenyi1213@163.com on 2015-03-26 10:03:26.
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
@Column(name="u_sid",type=Column.TYPE_STRING,isNull = false) 
 private String u_sid;
public String getU_sid() {
    return u_sid;
}
public void setU_sid(String u_sid) {
    this.u_sid = u_sid;
}
@Column(name="f_id",type=Column.TYPE_STRING,isNull = false) 
 private String f_id;
public String getF_id() {
    return f_id;
}
public void setF_id(String f_id) {
    this.f_id = f_id;
}
@Column(name="count",type=Column.TYPE_DOUBLE) 
 private double count;
public double getCount() {
    return count;
}
public void setCount(double count) {
    this.count = count;
}
@Column(name="suggestDate",type=Column.TYPE_TIMESTAMP,isNull = false) 
 private String suggestDate;
public String getSuggestDate() {
    return suggestDate;
}
public void setSuggestDate(String suggestDate) {
    this.suggestDate = suggestDate;
}
@Column(name="finishedDate",type=Column.TYPE_TIMESTAMP) 
 private String finishedDate;
public String getFinishedDate() {
    return finishedDate;
}
public void setFinishedDate(String finishedDate) {
    this.finishedDate = finishedDate;
}
@Column(name="type",type=Column.TYPE_INTEGER,isNull = false) 
 private Integer type;
public Integer getType() {
    return type;
}
public void setType(Integer type) {
    this.type = type;
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


	public U_f() {}
	
	   /**
    *根据所有不允许为null的列，初始化对象
    *
    * @param u_sid 参数解释
    * @param f_id 参数解释
    * @param suggestDate 参数解释
    * @param type 参数解释
    *
    *  @return 对象
    */
    public U_f(String u_sid,String f_id,String suggestDate,Integer type) {
       this.u_sid = u_sid;
       this.f_id = f_id;
       this.suggestDate = suggestDate;
       this.type = type;
    }

	
}
