package com.safeness.patient.model;

import com.safeness.e_saveness_common.dao.Table;
import com.safeness.e_saveness_common.dao.Table.Column;

/**
 * Sports_plan
 * Project name: com.safeness.patient
 *
 * @author wuwenyi1213@163.com on 2015-03-28 19:03:19.
 * Copyright (c) 2015年 wuwenyi1213@163.com. All rights reserved.
 */

@Table(name = "sports_plan")
public class Sports_plan {

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
    @Column(name="s_sid",type=Column.TYPE_STRING,isNull = false)
    private String s_sid;
    public String getS_sid() {
        return s_sid;
    }
    public void setS_sid(String s_sid) {
        this.s_sid = s_sid;
    }
    @Column(name="startdate",type=Column.TYPE_TIMESTAMP,isNull = false)
    private String startdate;
    public String getStartdate() {
        return startdate;
    }
    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }
    @Column(name="enddate",type=Column.TYPE_TIMESTAMP,isNull = false)
    private String enddate;
    public String getEnddate() {
        return enddate;
    }
    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }
    @Column(name="complications",type=Column.TYPE_STRING)
    private String complications;
    public String getComplications() {
        return complications;
    }
    public void setComplications(String complications) {
        this.complications = complications;
    }
    @Column(name="effect",type=Column.TYPE_STRING)
    private String effect;
    public String getEffect() {
        return effect;
    }
    public void setEffect(String effect) {
        this.effect = effect;
    }
    @Column(name="calori",type=Column.TYPE_STRING)
    private String calori;
    public String getCalori() {
        return calori;
    }
    public void setCalori(String calori) {
        this.calori = calori;
    }
    @Column(name="type",type=Column.TYPE_STRING)
    private String type;
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    @Column(name="attention",type=Column.TYPE_STRING)
    private String attention;
    public String getAttention() {
        return attention;
    }
    public void setAttention(String attention) {
        this.attention = attention;
    }
    @Column(name="drug",type=Column.TYPE_STRING)
    private String drug;
    public String getDrug() {
        return drug;
    }
    public void setDrug(String drug) {
        this.drug = drug;
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


    public Sports_plan() {}

    /**
     *根据所有不允许为null的列，初始化对象
     *
     * @param u_sid 参数解释
     * @param s_sid 参数解释
     * @param startdate 参数解释
     * @param enddate 参数解释
     *
     *  @return 对象
     */
    public Sports_plan(String u_sid,String s_sid,String startdate,String enddate) {
        this.u_sid = u_sid;
        this.s_sid = s_sid;
        this.startdate = startdate;
        this.enddate = enddate;
    }


}
