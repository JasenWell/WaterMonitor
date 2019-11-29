package com.android.zht.waterwatch.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class UserInfo implements Serializable{
	private static final long serialVersionUID = -2240252680350204069L;
	
	private int id;
	private String userAccount;
	private String userPassword;
	private int userType;//类型权限控制  (3.超级管理员(可以查看所有单位所有校区) 2.单位总管(可以看该单位所有校区) 1.普通用户)
	private String userName;
	private long openTime;//账号启用时间
	private String userPhone;
	private DepartmentInfo departmentInfo;
	private List<DepartmentInfo> departmentInfoList; //超管返回

	private String extendKey1 ;/*备用字段1*/
	private String extendKey2 ;/*备用字段2*/
	private String extendKey3 ;/*备用字段3*/
	private String extendKey4 ;/*备用字段4*/
	
	
	public UserInfo(){
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public int getUserType() {
		return userType;
	}

	public void setUserType(int userType) {
		this.userType = userType;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public long getOpenTime() {
		return openTime;
	}

	public void setOpenTime(long openTime) {
		this.openTime = openTime;
	}

	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	public DepartmentInfo getDepartmentInfo() {
		return departmentInfo;
	}

	public void setDepartmentInfo(DepartmentInfo departmentInfo) {
		this.departmentInfo = departmentInfo;
	}

	public List<DepartmentInfo> getDepartmentInfoList() {
		return departmentInfoList;
	}

	public void setDepartmentInfoList(List<DepartmentInfo> departmentInfoList) {
		this.departmentInfoList = departmentInfoList;
	}

	public String getExtendKey1() {
		return extendKey1;
	}

	public void setExtendKey1(String extendKey1) {
		this.extendKey1 = extendKey1;
	}

	public String getExtendKey2() {
		return extendKey2;
	}

	public void setExtendKey2(String extendKey2) {
		this.extendKey2 = extendKey2;
	}

	public String getExtendKey3() {
		return extendKey3;
	}

	public void setExtendKey3(String extendKey3) {
		this.extendKey3 = extendKey3;
	}

	public String getExtendKey4() {
		return extendKey4;
	}

	public void setExtendKey4(String extendKey4) {
		this.extendKey4 = extendKey4;
	}
}
