package com.android.zht.waterwatch.constants;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;

import com.hjh.baselib.utils.AppUtil;
import com.hjh.baselib.utils.ExceptionHandler;
import com.hjh.baselib.utils.SDCardManagerTools;
import com.nostra13.universalimageloader.cache.disc.impl.LimitedAgeDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.hjh.baselib.utils.NetworkUtil;

import java.io.File;



public class ToolsApplication extends Application {

    private static Context mContext;

    public static Context getContext() {
        return mContext;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        initImageLoader();
        AppUtil.init(this);
    }


    public static ImageLoader getImageLoader(){
        return ImageLoader.getInstance();

    }

    /**
     * 初始化图片加载器
     */
    public void initImageLoader() {
        File cacheDir = StorageUtils.getCacheDirectory(getApplicationContext());
        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(getApplicationContext())
//                .memoryCacheExtraOptions(480, 800)
                .memoryCache(new LruMemoryCache(2*1024*1024))
                .memoryCacheSize(2*1024*1024)
                .diskCache(new LimitedAgeDiskCache(cacheDir,24*60*60))//new UnlimitedDiscCache(cacheDir)
                .diskCacheSize(50*1024*1024)
                .diskCacheFileCount(100)
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
                .build();
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().init(configuration);
    }

    /**
     * 获取图片加载选项
     * @param resId 未加载成功时需显示资源的id
     * @return
     */
    public static DisplayImageOptions getImageLoadOption(int resId) {
        return new DisplayImageOptions.Builder()
                .showImageOnLoading(resId)
                .showImageForEmptyUri(resId)
                .showImageOnFail(resId)
                .resetViewBeforeLoading(false)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }

}
