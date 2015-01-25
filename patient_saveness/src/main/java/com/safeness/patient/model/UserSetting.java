package com.safeness.patient.model;

import com.safeness.patient.dao.Table;
import com.safeness.patient.dao.Table.Column;

import java.sql.Date;

/**
 * UserSetting
 * Project name: com.safeness.patient
 *
 * @author wuwenyi1213@163.com on 2015-01-25 20:01:05.
 *         Copyright (c) 2015年 wuwenyi1213@163.com. All rights reserved.
 */

@Table(name = "userSetting")
public class UserSetting {

    @Column(name = "_id", type = Column.TYPE_INTEGER, isPrimaryKey = true, isNull = false, isUnique = true)
    private Integer _id;

    public Integer get_id() {
        return _id;
    }

    public void set_id(Integer _id) {
        this._id = _id;
    }

    @Column(name = "s_id", type = Column.TYPE_INTEGER, isNull = false, isUnique = true)
    private Integer s_id;

    public Integer getS_id() {
        return s_id;
    }

    public void setS_id(Integer s_id) {
        this.s_id = s_id;
    }

    @Column(name = "language", type = Column.TYPE_INTEGER, defaultValue = "10")
    private Integer language;

    public Integer getLanguage() {
        return language;
    }

    public void setLanguage(Integer language) {
        this.language = language;
    }

    @Column(name = "hourFormat", type = Column.TYPE_BOOLEAN, defaultValue = "0")
    private boolean hourFormat;

    public boolean getHourFormat() {
        return hourFormat;
    }

    public void setHourFormat(boolean hourFormat) {
        this.hourFormat = hourFormat;
    }

    @Column(name = "funnyFeature", type = Column.TYPE_BOOLEAN, defaultValue = "0")
    private boolean funnyFeature;

    public boolean getFunnyFeature() {
        return funnyFeature;
    }

    public void setFunnyFeature(boolean funnyFeature) {
        this.funnyFeature = funnyFeature;
    }

    @Column(name = "sleepTimeBg", type = Column.TYPE_TIMESTAMP)
    private String sleepTimeBg;

    public String getSleepTimeBg() {
        return sleepTimeBg;
    }

    public void setSleepTimeBg(String sleepTimeBg) {
        this.sleepTimeBg = sleepTimeBg;
    }

    @Column(name = "sleepTimeEd", type = Column.TYPE_TIMESTAMP)
    private String sleepTimeEd;

    public String getSleepTimeEd() {
        return sleepTimeEd;
    }

    public void setSleepTimeEd(String sleepTimeEd) {
        this.sleepTimeEd = sleepTimeEd;
    }

    @Column(name = "skin", type = Column.TYPE_STRING, defaultValue = "('Orange')")
    private String skin;

    public String getSkin() {
        return skin;
    }

    public void setSkin(String skin) {
        this.skin = skin;
    }

    @Column(name = "life_status", type = Column.TYPE_INTEGER, defaultValue = "1")
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


    public UserSetting() {
    }

    /**
     * 根据所有不允许为null的列，初始化对象
     *
     * @param s_id 参数解释
     * @return 对象
     */
    public UserSetting(Integer s_id) {
        this.s_id = s_id;
    }


}
