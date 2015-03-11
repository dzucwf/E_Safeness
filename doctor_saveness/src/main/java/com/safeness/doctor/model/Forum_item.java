package com.safeness.doctor.model;

import com.safeness.e_saveness_common.dao.Table;
import com.safeness.e_saveness_common.dao.Table.Column;

/**
* Forum_item
* Project name: com.safeness.patient
* 
* @author wuwenyi1213@163.com on 2015-03-11 22:03:18.
* Copyright (c) 2015年 wuwenyi1213@163.com. All rights reserved.
*/

@Table(name = "forum_item")
public class Forum_item {

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
@Column(name="parent_id",type=Column.TYPE_INTEGER,isNull = false) 
 private Integer parent_id;
public Integer getParent_id() {
    return parent_id;
}
public void setParent_id(Integer parent_id) {
    this.parent_id = parent_id;
}
@Column(name="user_id",type=Column.TYPE_INTEGER,isNull = false) 
 private Integer user_id;
public Integer getUser_id() {
    return user_id;
}
public void setUser_id(Integer user_id) {
    this.user_id = user_id;
}
@Column(name="user_name",type=Column.TYPE_STRING,isNull = false) 
 private String user_name;
public String getUser_name() {
    return user_name;
}
public void setUser_name(String user_name) {
    this.user_name = user_name;
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
@Column(name="photos",type=Column.TYPE_STRING) 
 private String photos;
public String getPhotos() {
    return photos;
}
public void setPhotos(String photos) {
    this.photos = photos;
}
@Column(name="life_status",type=Column.TYPE_INTEGER,defaultValue = "1") 
 private Integer life_status;
public Integer getLife_status() {
    return life_status;
}
public void setLife_status(Integer life_status) {
    this.life_status = life_status;
}
@Column(name="create_datetime",type=Column.Unknown type,defaultValue = "CURRENT_TIMESTAMP") 
 private Unknown type create_datetime;
public Unknown type getCreate_datetime() {
    return create_datetime;
}
public void setCreate_datetime(Unknown type create_datetime) {
    this.create_datetime = create_datetime;
}
@Column(name="update_datetime",type=Column.Unknown type,defaultValue = "CURRENT_TIMESTAMP") 
 private Unknown type update_datetime;
public Unknown type getUpdate_datetime() {
    return update_datetime;
}
public void setUpdate_datetime(Unknown type update_datetime) {
    this.update_datetime = update_datetime;
}


	public Forum_item() {}
	
	   /**
    *根据所有不允许为null的列，初始化对象
    *
    * @param server_id 参数解释
    * @param parent_id 参数解释
    * @param user_id 参数解释
    * @param user_name 参数解释
    * @param title 参数解释
    * @param content 参数解释
    *
    *  @return 对象
    */
    public Forum_item(Integer server_id,Integer parent_id,Integer user_id,String user_name,String title,String content) {
       this.server_id = server_id;
       this.parent_id = parent_id;
       this.user_id = user_id;
       this.user_name = user_name;
       this.title = title;
       this.content = content;
    }

	
}
