package com.safeness.doctor.model;

import com.safeness.e_saveness_common.dao.Table;
import com.safeness.e_saveness_common.dao.Table.Column;

/**
* Forum_item_evaluate
* Project name: com.safeness.patient
* 
* @author wuwenyi1213@163.com on 2015-03-11 22:03:18.
* Copyright (c) 2015年 wuwenyi1213@163.com. All rights reserved.
*/

@Table(name = "forum_item_evaluate")
public class Forum_item_evaluate {

	@Column(name="_id",type=Column.TYPE_INTEGER,isPrimaryKey=true,isNull = false,isUnique = true) 
 private Integer _id;
public Integer get_id() {
    return _id;
}
public void set_id(Integer _id) {
    this._id = _id;
}
@Column(name="item_id",type=Column.TYPE_INTEGER,isNull = false) 
 private Integer item_id;
public Integer getItem_id() {
    return item_id;
}
public void setItem_id(Integer item_id) {
    this.item_id = item_id;
}
@Column(name="user_id",type=Column.TYPE_INTEGER,isNull = false) 
 private Integer user_id;
public Integer getUser_id() {
    return user_id;
}
public void setUser_id(Integer user_id) {
    this.user_id = user_id;
}
@Column(name="item_evaluate",type=Column.TYPE_INTEGER,isNull = false,defaultValue = "0") 
 private Integer item_evaluate;
public Integer getItem_evaluate() {
    return item_evaluate;
}
public void setItem_evaluate(Integer item_evaluate) {
    this.item_evaluate = item_evaluate;
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


	public Forum_item_evaluate() {}
	
	   /**
    *根据所有不允许为null的列，初始化对象
    *
    * @param item_id 参数解释
    * @param user_id 参数解释
    *
    *  @return 对象
    */
    public Forum_item_evaluate(Integer item_id,Integer user_id) {
       this.item_id = item_id;
       this.user_id = user_id;
    }

	
}
