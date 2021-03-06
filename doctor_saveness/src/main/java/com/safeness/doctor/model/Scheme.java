package com.safeness.doctor.model;

import com.safeness.e_saveness_common.dao.Table;
import com.safeness.e_saveness_common.dao.Table.Column;

/**
* Scheme
* Project name: com.safeness.patient
* 
* @author wuwenyi1213@163.com on 2015-03-16 09:03:49.
* Copyright (c) 2015年 wuwenyi1213@163.com. All rights reserved.
*/

@Table(name = "scheme")
public class Scheme {

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
@Column(name="p_id",type=Column.TYPE_INTEGER,isNull = false) 
 private Integer p_id;
public Integer getP_id() {
    return p_id;
}
public void setP_id(Integer p_id) {
    this.p_id = p_id;
}
@Column(name="title",type=Column.TYPE_STRING) 
 private String title;
public String getTitle() {
    return title;
}
public void setTitle(String title) {
    this.title = title;
}
@Column(name="desc",type=Column.TYPE_STRING) 
 private String desc;
public String getDesc() {
    return desc;
}
public void setDesc(String desc) {
    this.desc = desc;
}
@Column(name="dateBegin",type=Column.TYPE_TIMESTAMP) 
 private String dateBegin;
public String getDateBegin() {
    return dateBegin;
}
public void setDateBegin(String dateBegin) {
    this.dateBegin = dateBegin;
}
@Column(name="dateEnd",type=Column.TYPE_TIMESTAMP) 
 private String dateEnd;
public String getDateEnd() {
    return dateEnd;
}
public void setDateEnd(String dateEnd) {
    this.dateEnd = dateEnd;
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


	public Scheme() {}
	
	   /**
    *根据所有不允许为null的列，初始化对象
    *
    * @param server_id 参数解释
    * @param p_id 参数解释
    *
    *  @return 对象
    */
    public Scheme(Integer server_id,Integer p_id) {
       this.server_id = server_id;
       this.p_id = p_id;
    }

	
}
