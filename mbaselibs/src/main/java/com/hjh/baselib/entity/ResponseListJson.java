package com.hjh.baselib.entity;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

public final class ResponseListJson<T> implements Serializable {

	private static final long serialVersionUID = -4389432837873704874L;

	@Expose
	private int status;//
	
	@Expose
	private String msg;//
	
	@Expose
	private  List<T> data;

	
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}

	public static  ResponseListJson fromJson(String json, Class clazz) {
        Gson gson = new Gson();
        Type objectType = type(ResponseListJson.class, clazz);
        return gson.fromJson(json, objectType);
    }

    public  String toJson(Class<T> clazz) {
        Gson gson = new Gson();
        Type objectType = type(ResponseListJson.class, clazz);
        return gson.toJson(this, objectType);
    }

   private static ParameterizedType type(final Class raw, final Type... args) {
        return new ParameterizedType() {
            public Type getRawType() {
                return raw;
            }

            public Type[] getActualTypeArguments() {
                return args;
            }

            public Type getOwnerType() {
                return null;
            }
        };
    }
}
