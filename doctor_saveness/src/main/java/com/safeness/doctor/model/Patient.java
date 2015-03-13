package com.safeness.doctor.model;

import com.safeness.e_saveness_common.dao.Table;
import com.safeness.e_saveness_common.dao.Table.Column;

/**
* Patient
* Project name: com.safeness.patient
* 
* @author wuwenyi1213@163.com on 2015-03-11 23:03:45.
* Copyright (c) 2015年 wuwenyi1213@163.com. All rights reserved.
*/

@Table(name = "patient")
public class Patient {

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
@Column(name="name",type=Column.TYPE_STRING,isNull = false) 
 private String name;
public String getName() {
    return name;
}
public void setName(String name) {
    this.name = name;
}
@Column(name="gender",type=Column.Unknown type,defaultValue = "2") 
 private Unknown type gender;
public Unknown type getGender() {
    return gender;
}
public void setGender(Unknown type gender) {
    this.gender = gender;
}
@Column(name="birthday",type=Column.TYPE_TIMESTAMP) 
 private String birthday;
public String getBirthday() {
    return birthday;
}
public void setBirthday(String birthday) {
    this.birthday = birthday;
}
@Column(name="patient_code",type=Column.TYPE_STRING) 
 private String patient_code;
public String getPatient_code() {
    return patient_code;
}
public void setPatient_code(String patient_code) {
    this.patient_code = patient_code;
}
@Column(name="tel",type=Column.TYPE_STRING,isUnique = true) 
 private String tel;
public String getTel() {
    return tel;
}
public void setTel(String tel) {
    this.tel = tel;
}
@Column(name="address",type=Column.TYPE_STRING) 
 private String address;
public String getAddress() {
    return address;
}
public void setAddress(String address) {
    this.address = address;
}
@Column(name="QQ",type=Column.TYPE_INTEGER,isUnique = true) 
 private Integer QQ;
public Integer getQQ() {
    return QQ;
}
public void setQQ(Integer QQ) {
    this.QQ = QQ;
}
@Column(name="weixin",type=Column.TYPE_STRING,isUnique = true) 
 private String weixin;
public String getWeixin() {
    return weixin;
}
public void setWeixin(String weixin) {
    this.weixin = weixin;
}
@Column(name="mail",type=Column.TYPE_STRING,isUnique = true) 
 private String mail;
public String getMail() {
    return mail;
}
public void setMail(String mail) {
    this.mail = mail;
}
@Column(name="other",type=Column.TYPE_STRING) 
 private String other;
public String getOther() {
    return other;
}
public void setOther(String other) {
    this.other = other;
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


	public Patient() {}
	
	   /**
    *根据所有不允许为null的列，初始化对象
    *
    * @param server_id 参数解释
    * @param name 参数解释
    *
    *  @return 对象
    */
    public Patient(Integer server_id,String name) {
       this.server_id = server_id;
       this.name = name;
    }

	
}
