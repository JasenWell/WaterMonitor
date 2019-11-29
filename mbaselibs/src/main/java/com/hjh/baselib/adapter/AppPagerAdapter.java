package com.hjh.baselib.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;


import java.util.List;


/**
 * 分页数据装载器
 * @author hjh
 *	2015-3-16下午4:48:48
 *
 */
public final class AppPagerAdapter extends PagerAdapter {

	private List<View> mList;
	private IOnloadPagerImage onLoadImage;
	
	public AppPagerAdapter(Context context, List list, IOnloadPagerImage loadImage){
		mList= list;
		onLoadImage = loadImage;
	}
	
	@Override
	public int getCount() {
		return mList.size();//Integer.MAX_VALUE
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		try {
			container.removeView(mList.get(position % mList.size()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {

		try {
			View view  =	mList.get(position % mList.size());
			if(view.getParent() != null){
				((ViewGroup)view.getParent()).removeView(view);
			}
			container.addView(view);
			if(onLoadImage != null){
				onLoadImage.loadImage(view);
			}

			return view;
		} catch (Exception e) {
			return null;
		}
		
	}


	/**
	 * 分页框图片加载回调
	 * @author hjh
	 *	2015-3-16下午5:26:56
	 *
	 */
	public interface IOnloadPagerImage {
		void loadImage(View view);
	}
	

}
