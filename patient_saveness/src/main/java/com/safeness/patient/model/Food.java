package com.safeness.patient.model;

import com.safeness.e_saveness_common.dao.Table;
import com.safeness.e_saveness_common.dao.Table.Column;

/**
 * Food
 * Project name: com.safeness.patient
 *
 * @author wuwenyi1213@163.com on 2015-03-26 10:03:26.
 * Copyright (c) 2015年 wuwenyi1213@163.com. All rights reserved.
 */

@Table(name = "food")
public class Food {

    @Column(name="_id",type=Column.TYPE_STRING,isNull = false,isUnique = true)
    private String _id;
    public String get_id() {
        return _id;
    }
    public void set_id(String _id) {
        this._id = _id;
    }
    @Column(name="foodname",type=Column.TYPE_STRING,isNull = false)
    private String foodname;
    public String getFoodname() {
        return foodname;
    }
    public void setFoodname(String foodname) {
        this.foodname = foodname;
    }
    @Column(name="calorie",type=Column.TYPE_DOUBLE,isNull = false)
    private double calorie;
    public double getCalorie() {
        return calorie;
    }
    public void setCalorie(double calorie) {
        this.calorie = calorie;
    }
    @Column(name="sugar",type=Column.TYPE_DOUBLE)
    private double sugar;
    public double getSugar() {
        return sugar;
    }
    public void setSugar(double sugar) {
        this.sugar = sugar;
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


    public Food() {}

    /**
     *根据所有不允许为null的列，初始化对象
     *
     * @param _id 参数解释
     * @param foodname 参数解释
     * @param calorie 参数解释
     *
     *  @return 对象
     */
    public Food(String _id,String foodname,double calorie) {
        this._id = _id;
        this.foodname = foodname;
        this.calorie = calorie;
    }


}
