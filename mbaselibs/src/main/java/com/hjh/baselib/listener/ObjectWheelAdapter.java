package com.hjh.baselib.listener;

import java.util.List;


public class ObjectWheelAdapter <T> implements WheelAdapter {

	
	private List<T> data;
	private int length = 20;
	
	public ObjectWheelAdapter(List<T> data){
		this.data = data;
	}
	
	
	@Override
	public int getItemsCount() {
		return data.size();
	}

	@Override
	public String getItem(int index) {
		if (index >= 0 && index < data.size()) {
            return data.get(index).toString();//调用对象重写toString()
        }
        return null;
	}

	@Override
	public int getMaximumLength() {
		return length;
	}

}
