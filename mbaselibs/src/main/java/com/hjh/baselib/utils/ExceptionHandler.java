package com.hjh.baselib.utils;

import android.content.Context;
import android.os.Looper;
import android.widget.Toast;


import com.hjh.baselib.constants.ModuleConfig;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.HashMap;
import java.util.Map;

/**
 * 异常处理
 * @author hjh
 * 2016-5-20下午11:56:34
 */
public class ExceptionHandler implements UncaughtExceptionHandler {

	private Context mContext;
	
	public ExceptionHandler(Context context){
		mContext = context;
	}
	
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		new Thread(new Runnable() {
				
				@Override
				public void run() {
					Looper.prepare();//异常日志 type Android, content
					Toast.makeText(mContext, "很抱歉,程序出现异常,即将退出.", Toast.LENGTH_LONG).show();
					Looper.loop();
				}
			}).start();
		Map<String, Object> map = new HashMap<>();
		map.put("type", "Android");
		map.put("errorInfo", AppUtil.getInstance().collectBasicInfo()+"  ===>> "+ AppLogger.getInstance().getErrorMsg(thread,ex));
		AppLogger logger =	AppLogger.getInstance();
		if(logger != null){
			String path  = logger.createErrorPath(mContext, ModuleConfig.PATH_ERROR);
			if(path != null){
				logger.saveErrorFile(thread, ex, path);
			}
		}
		
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		android.os.Process.killProcess(android.os.Process.myPid());
		System.exit(10);
		
	}
	
	
}
