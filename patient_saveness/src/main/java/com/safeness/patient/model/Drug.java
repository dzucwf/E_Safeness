package com.safeness.patient.model;

import com.safeness.e_saveness_common.dao.Table;
import com.safeness.e_saveness_common.dao.Table.Column;

/**
 * Drug
 * Project name: com.safeness.patient
 *
 * @author wuwenyi1213@163.com on 2015-03-26 10:03:26.
 * Copyright (c) 2015年 wuwenyi1213@163.com. All rights reserved.
 */

@Table(name = "drug")
public class Drug {

    @Column(name="_id",type=Column.TYPE_STRING,isNull = false,isUnique = true)
    private String _id;
    public String get_id() {
        return _id;
    }
    public void set_id(String _id) {
        this._id = _id;
    }
    @Column(name="drugName",type=Column.TYPE_STRING,isNull = false,isUnique = true)
    private String drugName;
    public String getDrugName() {
        return drugName;
    }
    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }
    @Column(name="desc",type=Column.TYPE_STRING)
    private String desc;
    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
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


    public Drug() {}

    /**
     *根据所有不允许为null的列，初始化对象
     *
     * @param _id 参数解释
     * @param drugName 参数解释
     *
     *  @return 对象
     */
    public Drug(String _id,String drugName) {
        this._id = _id;
        this.drugName = drugName;
    }


}
