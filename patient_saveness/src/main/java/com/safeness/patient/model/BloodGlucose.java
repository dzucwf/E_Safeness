package com.safeness.patient.model;

import com.safeness.e_saveness_common.dao.Table;
import com.safeness.e_saveness_common.dao.Table.Column;

/**
 * BloodGlucose
 * Project name: com.safeness.patient
 *
 * @author wuwenyi1213@163.com on 2015-01-25 21:01:02.
 *         Copyright (c) 2015年 wuwenyi1213@163.com. All rights reserved.
 */

@Table(name = "bloodGlucose")
public class BloodGlucose {

    @Column(name = "_id", type = Column.TYPE_INTEGER, isPrimaryKey = true, isUnique = true)
    private Integer _id;
    @Column(name = "server_id", type = Column.TYPE_INTEGER, isNull = false, isUnique = true)
    private Long server_id;
    @Column(name = "bloodGlucose", type = Column.TYPE_DOUBLE, isNull = false)
    private double bloodGlucose;
    @Column(name = "takeTag", type = Column.TYPE_INTEGER, isNull = false)
    private Integer takeTag;
    @Column(name = "takeDate", type = Column.TYPE_TIMESTAMP, isNull = false)
    private String takeDate;
    @Column(name = "life_status", type = Column.TYPE_INTEGER, isNull = false, defaultValue = "1")
    private Integer life_status;
    @Column(name = "create_datetime", type = Column.TYPE_TIMESTAMP, defaultValue = "CURRENT_TIMESTAMP")
    private String create_datetime;
    @Column(name = "update_datetime", type = Column.TYPE_TIMESTAMP, defaultValue = "CURRENT_TIMESTAMP")
    private String update_datetime;
    @Column(name = "memo", type = Column.TYPE_STRING)
    private String memo;

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public BloodGlucose() {

    }

    /**
     * 根据所有不允许为null的列，初始化对象
     *
     * @param server_id    参数解释
     * @param bloodGlucose 参数解释
     * @param takeTag      参数解释
     * @param takeDate     参数解释
     * @return 对象
     */
    public BloodGlucose(Long server_id, double bloodGlucose, Integer takeTag, String takeDate) {
        this.server_id = server_id;
        this.bloodGlucose = bloodGlucose;
        this.takeTag = takeTag;
        this.takeDate = takeDate;
    }

    public Integer get_id() {
        return _id;
    }

    public void set_id(Integer _id) {
        this._id = _id;
    }

    public Long getServer_id() {
        return server_id;
    }

    public void setServer_id(Long server_id) {
        this.server_id = server_id;
    }

    public double getBloodGlucose() {
        return bloodGlucose;
    }

    public void setBloodGlucose(double bloodGlucose) {
        this.bloodGlucose = bloodGlucose;
    }

    public Integer getTakeTag() {
        return takeTag;
    }

    public void setTakeTag(Integer takeTag) {
        this.takeTag = takeTag;
    }

    public String getTakeDate() {
        return takeDate;
    }

    public void setTakeDate(String takeDate) {
        this.takeDate = takeDate;
    }

    public Integer getLife_status() {
        return life_status;
    }

    public void setLife_status(Integer life_status) {
        this.life_status = life_status;
    }

    public String getCreate_datetime() {
        return create_datetime;
    }

    public void setCreate_datetime(String create_datetime) {
        this.create_datetime = create_datetime;
    }

    public String getUpdate_datetime() {
        return update_datetime;
    }

    public void setUpdate_datetime(String update_datetime) {
        this.update_datetime = update_datetime;
    }


}
