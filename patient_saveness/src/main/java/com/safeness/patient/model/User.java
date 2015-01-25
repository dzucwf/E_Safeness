package com.safeness.patient.model;

import com.safeness.patient.dao.Table;
import com.safeness.patient.dao.Table.Column;

import java.sql.Date;

/**
 * User
 * Project name: com.safeness.patient
 *
 * @author wuwenyi1213@163.com on 2015-01-25 16:01:20.
 * Copyright (c) 2015年 wuwenyi1213@163.com. All rights reserved.
 */

@Table(name = "user")
public class User {

    @Column(name="_id",type=Column.TYPE_INTEGER,isPrimaryKey=true,isUnique = true)
    private Integer _id;
    @Column(name="server_id",type=Column.TYPE_INTEGER,isNull = false,isUnique = true)
    private Integer server_id;
    @Column(name="username",type=Column.TYPE_STRING,isNull = false,isUnique = true)
    private String username;
    @Column(name="password",type=Column.TYPE_STRING)
    private String password;
    @Column(name="gender",type=Column.TYPE_INTEGER)
    private Integer gender;
    @Column(name="birthday",type=Column.TYPE_TIMESTAMP)
    private long birthday;
    @Column(name="height",type=Column.TYPE_DOUBLE)
    private double height;
    @Column(name="weight",type=Column.TYPE_DOUBLE)
    private double weight;
    @Column(name="mail",type=Column.TYPE_STRING,isUnique = true)
    private String mail;
    @Column(name="tel",type=Column.TYPE_STRING,isUnique = true)
    private String tel;
    @Column(name="photo",type=Column.TYPE_STRING)
    private String photo;
    @Column(name="parents_tel",type=Column.TYPE_STRING)
    private String parents_tel;
    @Column(name="life_status",type=Column.TYPE_INTEGER,isNull = false,defaultValue = "1")
    private Integer life_status;
    @Column(name="create_datetime",type=Column.TYPE_TIMESTAMP,defaultValue = "CURRENT_TIMESTAMP")
    private long create_datetime;
    @Column(name="update_datetime",type=Column.TYPE_TIMESTAMP,defaultValue = "CURRENT_TIMESTAMP")
    private long update_datetime;
    public User() {}
    /**
     *根据所有不允许为null的列，初始化对象
     *
     * @param server_id 参数解释
     * @param username 参数解释
     *
     *  @return 对象
     */
    public User(Integer server_id,String username) {
        this.server_id = server_id;
        this.username = username;
    }

    public Integer get_id() {
        return _id;
    }

    public void set_id(Integer _id) {
        this._id = _id;
    }

    public Integer getServer_id() {
        return server_id;
    }

    public void setServer_id(Integer server_id) {
        this.server_id = server_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public long getBirthday() {
        return birthday;
    }

    public void setBirthday(long birthday) {
        this.birthday = birthday;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getParents_tel() {
        return parents_tel;
    }

    public void setParents_tel(String parents_tel) {
        this.parents_tel = parents_tel;
    }

    public Integer getLife_status() {
        return life_status;
    }

    public void setLife_status(Integer life_status) {
        this.life_status = life_status;
    }

    public long getCreate_datetime() {
        return create_datetime;
    }

    public void setCreate_datetime(long create_datetime) {
        this.create_datetime = create_datetime;
    }

    public long getUpdate_datetime() {
        return update_datetime;
    }

    public void setUpdate_datetime(long update_datetime) {
        this.update_datetime = update_datetime;
    }


}
