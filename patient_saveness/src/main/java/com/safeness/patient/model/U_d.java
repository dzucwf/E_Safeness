package com.safeness.patient.model;

import com.safeness.e_saveness_common.dao.Table;
import com.safeness.e_saveness_common.dao.Table.Column;

/**
 * U_d
 * Project name: com.safeness.patient
 *
 * @author wuwenyi1213@163.com on 2015-03-27 22:03:18.
 * Copyright (c) 2015年 wuwenyi1213@163.com. All rights reserved.
 */

@Table(name = "u_d")
public class U_d {

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
    @Column(name="d_id",type=Column.TYPE_STRING,isNull = false)
    private String d_id;
    public String getD_id() {
        return d_id;
    }
    public void setD_id(String d_id) {
        this.d_id = d_id;
    }
    @Column(name="count",type=Column.TYPE_DOUBLE,isNull = false)
    private double count;
    public double getCount() {
        return count;
    }
    public void setCount(double count) {
        this.count = count;
    }
    @Column(name="hintDay",type=Column.TYPE_STRING,isNull = false)
    private String hintDay;
    public String getHintDay() {
        return hintDay;
    }
    public void setHintDay(String hintDay) {
        this.hintDay = hintDay;
    }
    @Column(name="hintTime",type=Column.TYPE_TIMESTAMP,isNull = false)
    private String hintTime;
    public String getHintTime() {
        return hintTime;
    }
    public void setHintTime(String hintTime) {
        this.hintTime = hintTime;
    }
    @Column(name="hintIndex",type=Column.TYPE_INTEGER,isNull = false)
    private Integer hintIndex;
    public Integer getHintIndex() {
        return hintIndex;
    }
    public void setHintIndex(Integer hintIndex) {
        this.hintIndex = hintIndex;
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


    public U_d() {}

    /**
     *根据所有不允许为null的列，初始化对象
     *
     * @param u_sid 参数解释
     * @param d_id 参数解释
     * @param count 参数解释
     * @param hintDay 参数解释
     * @param hintTime 参数解释
     * @param hintIndex 参数解释
     *
     *  @return 对象
     */
    public U_d(String u_sid,String d_id,double count,String hintDay,String hintTime,Integer hintIndex) {
        this.u_sid = u_sid;
        this.d_id = d_id;
        this.count = count;
        this.hintDay = hintDay;
        this.hintTime = hintTime;
        this.hintIndex = hintIndex;
    }


}
