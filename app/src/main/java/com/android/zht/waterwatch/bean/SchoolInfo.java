package com.android.zht.waterwatch.bean;

import java.io.Serializable;
import java.util.List;


/**
 * @date    on 2019/4/1
 * @author  hjh
 * @org     hjh
 * @version 
 * @description 学校信息
 */
public class SchoolInfo implements Serializable {

    private int id;
    private int departmentId;
    private String departmentName;
    private String name;
    private String address;
    private String phone;//联系电话
    private String contactUser;//联系人
    private List<WaterEffectInfo> effectInfoList;
    private List<WaterMeterInfo> meterInfoList;
    private String total;
    private String averavge;
    private int warningNumber;//未处理总告警
    private int todayWarningNumber;//今日告警数量

    public SchoolInfo(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getAveravge() {
        return averavge;
    }

    public void setAveravge(String averavge) {
        this.averavge = averavge;
    }

    public int getWarningNumber() {
        return warningNumber;
    }

    public void setWarningNumber(int warningNumber) {
        this.warningNumber = warningNumber;
    }

    public int getTodayWarningNumber() {
        return todayWarningNumber;
    }

    public void setTodayWarningNumber(int todayWarningNumber) {
        this.todayWarningNumber = todayWarningNumber;
    }

    public List<WaterEffectInfo> getEffectInfoList() {
        return effectInfoList;
    }

    public void setEffectInfoList(List<WaterEffectInfo> effectInfoList) {
        this.effectInfoList = effectInfoList;
    }

    public List<WaterMeterInfo> getMeterInfoList() {
        return meterInfoList;
    }

    public void setMeterInfoList(List<WaterMeterInfo> meterInfoList) {
        this.meterInfoList = meterInfoList;
    }

    public String getContactUser() {
        return contactUser;
    }

    public void setContactUser(String contactUser) {
        this.contactUser = contactUser;
    }
}
