package com.safeness.doctor.model;

import com.safeness.e_saveness_common.dao.Table;
import com.safeness.e_saveness_common.dao.Table.Column;

/**
* Scheme_detail
* Project name: com.safeness.patient
* 
* @author wuwenyi1213@163.com on 2015-03-11 22:03:19.
* Copyright (c) 2015年 wuwenyi1213@163.com. All rights reserved.
*/

@Table(name = "scheme_detail")
public class Scheme_detail {

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
@Column(name="scheme_id",type=Column.TYPE_INTEGER,isNull = false) 
 private Integer scheme_id;
public Integer getScheme_id() {
    return scheme_id;
}
public void setScheme_id(Integer scheme_id) {
    this.scheme_id = scheme_id;
}
@Column(name="drug_values",type=Column.TYPE_STRING) 
 private String drug_values;
public String getDrug_values() {
    return drug_values;
}
public void setDrug_values(String drug_values) {
    this.drug_values = drug_values;
}
@Column(name="food_values",type=Column.TYPE_STRING) 
 private String food_values;
public String getFood_values() {
    return food_values;
}
public void setFood_values(String food_values) {
    this.food_values = food_values;
}
@Column(name="sports_values",type=Column.TYPE_STRING) 
 private String sports_values;
public String getSports_values() {
    return sports_values;
}
public void setSports_values(String sports_values) {
    this.sports_values = sports_values;
}
@Column(name="blood_values",type=Column.TYPE_STRING) 
 private String blood_values;
public String getBlood_values() {
    return blood_values;
}
public void setBlood_values(String blood_values) {
    this.blood_values = blood_values;
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


	public Scheme_detail() {}
	
	   /**
    *根据所有不允许为null的列，初始化对象
    *
    * @param server_id 参数解释
    * @param scheme_id 参数解释
    *
    *  @return 对象
    */
    public Scheme_detail(Integer server_id,Integer scheme_id) {
       this.server_id = server_id;
       this.scheme_id = scheme_id;
    }

	
}
