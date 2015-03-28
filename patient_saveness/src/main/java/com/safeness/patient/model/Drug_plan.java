package com.safeness.patient.model;

import com.safeness.e_saveness_common.dao.Table;
import com.safeness.e_saveness_common.dao.Table.Column;

/**
 * Drug_plan
 * Project name: com.safeness.patient
 *
 * @author wuwenyi1213@163.com on 2015-03-27 20:03:50.
 * Copyright (c) 2015年 wuwenyi1213@163.com. All rights reserved.
 */

@Table(name = "drug_plan")
public class Drug_plan {

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
    @Column(name="F_sid",type=Column.TYPE_STRING,isNull = false)
    private String F_sid;
    public String getF_sid() {
        return F_sid;
    }
    public void setF_sid(String F_sid) {
        this.F_sid = F_sid;
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
    @Column(name="everytime",type=Column.TYPE_INTEGER,isNull = false)
    private Integer everytime;
    public Integer getEverytime() {
        return everytime;
    }
    public void setEverytime(Integer everytime) {
        this.everytime = everytime;
    }
    @Column(name="medtime",type=Column.TYPE_INTEGER,isNull = false)
    private Integer medtime;
    public Integer getMedtime() {
        return medtime;
    }
    public void setMedtime(Integer medtime) {
        this.medtime = medtime;
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


    public Drug_plan() {}

    /**
     *根据所有不允许为null的列，初始化对象
     *
     * @param u_sid 参数解释
     * @param F_sid 参数解释
     * @param startdate 参数解释
     * @param enddate 参数解释
     * @param everytime 参数解释
     * @param medtime 参数解释
     *
     *  @return 对象
     */
    public Drug_plan(String u_sid,String F_sid,String startdate,String enddate,Integer everytime,Integer medtime) {
        this.u_sid = u_sid;
        this.F_sid = F_sid;
        this.startdate = startdate;
        this.enddate = enddate;
        this.everytime = everytime;
        this.medtime = medtime;
    }


}
