package com.safeness.patient.model;

import com.safeness.e_saveness_common.dao.Table;
import com.safeness.e_saveness_common.dao.Table.Column;

/**
 * Sports
 * Project name: com.safeness.patient
 *
 * @author wuwenyi1213@163.com on 2015-01-25 21:01:02.
 *         Copyright (c) 2015年 wuwenyi1213@163.com. All rights reserved.
 */

@Table(name = "sports")
public class Sports {

    @Column(name = "_id", type = Column.TYPE_INTEGER, isPrimaryKey = true, isUnique = true)
    private Integer _id;

    public Integer get_id() {
        return _id;
    }

    public void set_id(Integer _id) {
        this._id = _id;
    }

    @Column(name = "sportsName", type = Column.TYPE_STRING, isNull = false, isUnique = true)
    private String sportsName;

    public String getSportsName() {
        return sportsName;
    }

    public void setSportsName(String sportsName) {
        this.sportsName = sportsName;
    }

    @Column(name = "calorie", type = Column.TYPE_DOUBLE, isNull = false)
    private double calorie;

    public double getCalorie() {
        return calorie;
    }

    public void setCalorie(double calorie) {
        this.calorie = calorie;
    }

    @Column(name = "suggestTime", type = Column.TYPE_INTEGER, isNull = false)
    private Integer suggestTime;

    public Integer getSuggestTime() {
        return suggestTime;
    }

    public void setSuggestTime(Integer suggestTime) {
        this.suggestTime = suggestTime;
    }

    @Column(name = "desc1", type = Column.TYPE_STRING)
    private String desc1;

    public String getDesc1() {
        return desc1;
    }

    public void setDesc1(String desc1) {
        this.desc1 = desc1;
    }

    @Column(name = "desc2", type = Column.TYPE_STRING)
    private String desc2;

    public String getDesc2() {
        return desc2;
    }

    public void setDesc2(String desc2) {
        this.desc2 = desc2;
    }

    @Column(name = "desc3", type = Column.TYPE_STRING)
    private String desc3;

    public String getDesc3() {
        return desc3;
    }

    public void setDesc3(String desc3) {
        this.desc3 = desc3;
    }

    @Column(name = "desc4", type = Column.TYPE_STRING)
    private String desc4;

    public String getDesc4() {
        return desc4;
    }

    public void setDesc4(String desc4) {
        this.desc4 = desc4;
    }

    @Column(name = "desc5", type = Column.TYPE_STRING)
    private String desc5;

    public String getDesc5() {
        return desc5;
    }

    public void setDesc5(String desc5) {
        this.desc5 = desc5;
    }

    @Column(name = "desc6", type = Column.TYPE_STRING)
    private String desc6;

    public String getDesc6() {
        return desc6;
    }

    public void setDesc6(String desc6) {
        this.desc6 = desc6;
    }

    @Column(name = "life_status", type = Column.TYPE_INTEGER, isNull = false, defaultValue = "1")
    private Integer life_status;

    public Integer getLife_status() {
        return life_status;
    }

    public void setLife_status(Integer life_status) {
        this.life_status = life_status;
    }

    @Column(name = "create_datetime", type = Column.TYPE_TIMESTAMP, defaultValue = "CURRENT_TIMESTAMP")
    private String create_datetime;

    public String getCreate_datetime() {
        return create_datetime;
    }

    public void setCreate_datetime(String create_datetime) {
        this.create_datetime = create_datetime;
    }

    @Column(name = "update_datetime", type = Column.TYPE_TIMESTAMP, defaultValue = "CURRENT_TIMESTAMP")
    private String update_datetime;

    public String getUpdate_datetime() {
        return update_datetime;
    }

    public void setUpdate_datetime(String update_datetime) {
        this.update_datetime = update_datetime;
    }


    public Sports() {
    }

    /**
     * 根据所有不允许为null的列，初始化对象
     *
     * @param sportsName  参数解释
     * @param calorie     参数解释
     * @param suggestTime 参数解释
     * @return 对象
     */
    public Sports(String sportsName, double calorie, Integer suggestTime) {
        this.sportsName = sportsName;
        this.calorie = calorie;
        this.suggestTime = suggestTime;
    }


}
