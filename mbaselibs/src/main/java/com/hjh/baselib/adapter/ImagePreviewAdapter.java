package com.hjh.baselib.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;

import android.view.View;
import android.view.ViewGroup;



import com.hjh.baselib.widget.ImagePreviewLayout;

import java.util.HashMap;
import java.util.List;


/**
 * Created by hjh on 2015/8/31.
 */
public class ImagePreviewAdapter<T> extends PagerAdapter {

    private List<T> mList;
    private Context mContext;
    private ReloadCallback mCallback;
    private HashMap<Integer, ImagePreviewLayout> map = new HashMap<Integer, ImagePreviewLayout>();

    public ImagePreviewAdapter(List<T> list, Context context, ReloadCallback callback){
        mList = list;
        mContext = context;
        mCallback = callback;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ImagePreviewLayout)object).recycle();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImagePreviewLayout layout;

        if(map.containsKey(position)){
            layout = map.get(position);
        }else{
            layout = new ImagePreviewLayout(mContext);
            map.put(position, layout);
            container.addView(layout);
        }
        mCallback.callBack(position, layout);
        return layout;
    }

    public static interface ReloadCallback{
        void callBack(int position, ImagePreviewLayout layout);
    }


}
