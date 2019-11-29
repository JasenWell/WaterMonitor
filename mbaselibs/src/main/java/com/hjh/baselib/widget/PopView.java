package com.hjh.baselib.widget;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.PopupWindow;

import com.hjh.baselib.listener.BasicViewInterface;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by hjh on 2015/8/18.
 */
public abstract class PopView extends PopupWindow implements BasicViewInterface {

    protected Activity mOwner ;
    protected LayoutInflater mInfalter ;
    Unbinder unbinder;

    public PopView(Activity owner, int width, int height){
        super(new FrameLayout(owner),width,height);

        mOwner		= owner ;
        mInfalter	= LayoutInflater.from(owner);
        if(getContentLayout() != 0) {
            View view = mInfalter.inflate(getContentLayout(),null, false);
            setContentView(view);
            unbinder = ButterKnife.bind(this, view);//绑定fragment
        }
        EventBus.getDefault().register(this);
        onAfterCreate(null);
    }

    @Subscribe
    public void onEventMainThread(Object object){
        //接收到发布者发布的事件后，进行相应的处理操作
    }

    public void onDestory(){
        EventBus.getDefault().unregister(this);
        if(unbinder != null) {
            unbinder.unbind();
        }
    }

    /**
     * @param id
     * @return
     */
    protected int getAppColor(int id){
        return mOwner.getResources().getColor(id);
    }

    protected  int getIdByName(String className, String name) {
        String packageName = mOwner.getPackageName();
        Class r = null;
        int id = 0;
        try {
            r = Class.forName(packageName + ".R");

            Class[] classes = r.getClasses();
            Class desireClass = null;

            for (int i = 0; i < classes.length; ++i) {
                if (classes[i].getName().split("\\$")[1].equals(className)) {
                    desireClass = classes[i];
                    break;
                }
            }

            if (desireClass != null)
                id = desireClass.getField(name).getInt(desireClass);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        return id;
    }

    @Override
    public void onAfterCreate(Bundle savedInstanceState) {

    }

    @Override
    public void onRefreshView() {

    }

    @Override
    public void onLoadDefaultTitle() {

    }

    @Override
    public void onLoadDefaultData() {

    }

    @Override
    public void onLoadDefaultView(Bundle savedInstanceState) {

    }
}
