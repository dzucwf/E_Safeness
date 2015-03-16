package com.safeness.doctor.model;

import com.safeness.e_saveness_common.dao.Table;
import com.safeness.e_saveness_common.dao.Table.Column;

/**
* Medical_record_detail
* Project name: com.safeness.patient
* 
* @author wuwenyi1213@163.com on 2015-03-16 09:03:49.
* Copyright (c) 2015年 wuwenyi1213@163.com. All rights reserved.
*/

@Table(name = "medical_record_detail")
public class Medical_record_detail {

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
@Column(name="medical_record_id",type=Column.TYPE_INTEGER,isNull = false) 
 private Integer medical_record_id;
public Integer getMedical_record_id() {
    return medical_record_id;
}
public void setMedical_record_id(Integer medical_record_id) {
    this.medical_record_id = medical_record_id;
}
@Column(name="recode_number",type=Column.TYPE_INTEGER,isNull = false) 
 private Integer recode_number;
public Integer getRecode_number() {
    return recode_number;
}
public void setRecode_number(Integer recode_number) {
    this.recode_number = recode_number;
}
@Column(name="medical_history",type=Column.TYPE_STRING) 
 private String medical_history;
public String getMedical_history() {
    return medical_history;
}
public void setMedical_history(String medical_history) {
    this.medical_history = medical_history;
}
@Column(name="clinical",type=Column.TYPE_STRING) 
 private String clinical;
public String getClinical() {
    return clinical;
}
public void setClinical(String clinical) {
    this.clinical = clinical;
}
@Column(name="diagnosis",type=Column.TYPE_STRING) 
 private String diagnosis;
public String getDiagnosis() {
    return diagnosis;
}
public void setDiagnosis(String diagnosis) {
    this.diagnosis = diagnosis;
}
@Column(name="treatment",type=Column.TYPE_STRING) 
 private String treatment;
public String getTreatment() {
    return treatment;
}
public void setTreatment(String treatment) {
    this.treatment = treatment;
}
@Column(name="operation",type=Column.TYPE_STRING) 
 private String operation;
public String getOperation() {
    return operation;
}
public void setOperation(String operation) {
    this.operation = operation;
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


	public Medical_record_detail() {}
	
	   /**
    *根据所有不允许为null的列，初始化对象
    *
    * @param server_id 参数解释
    * @param medical_record_id 参数解释
    * @param recode_number 参数解释
    *
    *  @return 对象
    */
    public Medical_record_detail(Integer server_id,Integer medical_record_id,Integer recode_number) {
       this.server_id = server_id;
       this.medical_record_id = medical_record_id;
       this.recode_number = recode_number;
    }

	
}
