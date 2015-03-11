package com.safeness.doctor.model;

import com.safeness.e_saveness_common.dao.Table;
import com.safeness.e_saveness_common.dao.Table.Column;

/**
* Scheme
* Project name: com.safeness.patient
* 
* @author wuwenyi1213@163.com on 2015-03-11 22:03:19.
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
@Column(name="dateBegin",type=Column.Unknown type) 
 private Unknown type dateBegin;
public Unknown type getDateBegin() {
    return dateBegin;
}
public void setDateBegin(Unknown type dateBegin) {
    this.dateBegin = dateBegin;
}
@Column(name="dateEnd",type=Column.Unknown type) 
 private Unknown type dateEnd;
public Unknown type getDateEnd() {
    return dateEnd;
}
public void setDateEnd(Unknown type dateEnd) {
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
