package com.hjh.baselib.entity;

import java.io.Serializable;



/**
 * 具体请求数据
 * @author hjh
 *
 */
public class RequestData<T> implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int requestType;
	private T requestParameters;

	public RequestData(int type) {
		setRequestType(type);
	}

	public int getRequestType() {
		return requestType;
	}

	public void setRequestType(int requestType) {
		this.requestType = requestType;
	}

	public T getRequestParameters() {
		return requestParameters;
	}

	public void setRequestParameters(T requestParameters) {
		this.requestParameters = requestParameters;
	}
}
