package com.android.zht.waterwatch.bean;

import java.io.Serializable;

/**
 * 版本信息
 * @author hjh
 *
 */
public class VersionInfo implements Serializable{

	
	private static final long serialVersionUID = 3565818392998974546L;
	private int id;
	private String updateText;//更新文本
	private String updateTime;//更新时间
	private String apkPath;//apk下载链接
	private int majorCode;//主版本号

	public VersionInfo(){
		
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}


	public String getUpdateText() {
		return updateText;
	}

	public void setUpdateText(String updateText) {
		this.updateText = updateText;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getApkPath() {
		return apkPath;
	}

	public void setApkPath(String apkPath) {
		this.apkPath = apkPath;
	}

	public int getMajorCode() {
		return majorCode;
	}

	public void setMajorCode(int majorCode) {
		this.majorCode = majorCode;
	}



}
