package com.safeness.doctor.model;

import com.safeness.e_saveness_common.dao.Table;
import com.safeness.e_saveness_common.dao.Table.Column;

/**
* User
* Project name: com.safeness.patient
* 
* @author wuwenyi1213@163.com on 2015-03-16 09:03:49.
* Copyright (c) 2015年 wuwenyi1213@163.com. All rights reserved.
*/

@Table(name = "user")
public class User {

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
@Column(name="username",type=Column.TYPE_STRING,isNull = false) 
 private String username;
public String getUsername() {
    return username;
}
public void setUsername(String username) {
    this.username = username;
}
@Column(name="mail",type=Column.TYPE_STRING,isNull = false,isUnique = true) 
 private String mail;
public String getMail() {
    return mail;
}
public void setMail(String mail) {
    this.mail = mail;
}
@Column(name="tel",type=Column.TYPE_STRING,isNull = false,isUnique = true) 
 private String tel;
public String getTel() {
    return tel;
}
public void setTel(String tel) {
    this.tel = tel;
}
@Column(name="birthday",type=Column.TYPE_TIMESTAMP) 
 private String birthday;
public String getBirthday() {
    return birthday;
}
public void setBirthday(String birthday) {
    this.birthday = birthday;
}
@Column(name="height",type=Column.TYPE_DOUBLE) 
 private double height;
public double getHeight() {
    return height;
}
public void setHeight(double height) {
    this.height = height;
}
@Column(name="weight",type=Column.TYPE_DOUBLE) 
 private double weight;
public double getWeight() {
    return weight;
}
public void setWeight(double weight) {
    this.weight = weight;
}
@Column(name="gender",type=Column.TYPE_INTEGER,defaultValue = "2") 
 private Integer gender;
public Integer getGender() {
    return gender;
}
public void setGender(Integer gender) {
    this.gender = gender;
}
@Column(name="photo",type=Column.TYPE_STRING) 
 private String photo;
public String getPhoto() {
    return photo;
}
public void setPhoto(String photo) {
    this.photo = photo;
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


	public User() {}
	
	   /**
    *根据所有不允许为null的列，初始化对象
    *
    * @param server_id 参数解释
    * @param username 参数解释
    * @param mail 参数解释
    * @param tel 参数解释
    *
    *  @return 对象
    */
    public User(Integer server_id,String username,String mail,String tel) {
       this.server_id = server_id;
       this.username = username;
       this.mail = mail;
       this.tel = tel;
    }

	
}
