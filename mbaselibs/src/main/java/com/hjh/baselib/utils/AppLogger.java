package com.hjh.baselib.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.util.Log;


public class AppLogger {

	private  DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");  
	private  String TAG = AppLogger.class.getSimpleName();
	private  Map<String, String> infos = new HashMap<String, String>(); 
	private  String path ;
	private  String fileName;
	private  int count = 0;
	private  static AppLogger instance;
	private  boolean debug = false;
	private  static String logDirName;
	private  SDCardManagerTools sdCardManager;
	private  File  logFileDir;
	
	private AppLogger(Context context){
		sdCardManager = SDCardManagerTools.getInstance();
		init(context);
	}
	
	private static synchronized void syncInit(Context context,String path){
		if(instance == null){
			logDirName = path;
			instance = new AppLogger(context);
		}
	}
	
	public static AppLogger init(Context context,String logDirName){
		if(instance == null){
			syncInit(context,logDirName);
		}
		
		return instance;
	}
	
	public static AppLogger getInstance(){
		return instance;
	}
	
	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public  void d(String tag , String message){
		
		if(isDebug()){
			Log.d(tag, message);
			saveFile(tag+" : "+message);
		}
	}
	
	public  void w(String tag , String message){
		
		if(isDebug()){
			Log.w(tag, message);
			saveFile(tag+" : "+message);
		}
	}
	
	public  void e(String tag , String message){
		
		if(isDebug()){
			Log.e(tag, message);
			saveFile(tag+" : "+message);
		}
	}
	
	private  void init(Context context){
		StringBuffer logPath = new StringBuffer();
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			logPath.append(Environment.getExternalStorageDirectory().getAbsolutePath());
			logPath.append(File.separator).append("Android/data/");
			logPath.append(context.getPackageName()).append("/");
			logPath.append(logDirName).append("/");
			path = logPath.toString();
		}else {
			logPath.append(context.getCacheDir().getAbsolutePath()).append("/").append("Android/data/");
			logPath.append(context.getPackageName()).append("/");
			logPath.append(logDirName).append("/");
			path = logPath.toString();
		}
		
		//
		logFileDir =new File(path);
		if(!logFileDir.exists()){
			logFileDir.mkdirs();
		}
		
		String time = formatter.format(new Date());  
        fileName = time+".txt";  //ÿ���������д���һ���ı�
        collectDeviceInfo(context);
	}
	
	public  void getSDcardPath(Context context){
		String sd_default = Environment.getExternalStorageDirectory() .getAbsolutePath();  
		
		if (sd_default.endsWith("/")) {           
			sd_default = sd_default.substring(0, sd_default.length() - 1);        
		}         
		// �õ�·��        
		try {            
				Runtime runtime = Runtime.getRuntime();      
				Process proc = runtime.exec("mount");      
				InputStream is = proc.getInputStream();         
				InputStreamReader isr = new InputStreamReader(is);         
				String line;          
				BufferedReader br = new BufferedReader(isr);            
				while ((line = br.readLine()) != null) {               
					if (line.contains("secure"))                     
						continue;               
					if (line.contains("asec"))               
						continue;                 
					if (line.contains("fat") && line.contains("/mnt/")) {        
						String columns[] = line.split(" ");          
						if (columns != null && columns.length > 1) {            
							if (sd_default.trim().equals(columns[1].trim())) {            
								continue;                       
							}    
							
							path = columns[1];                 
						}                
					}else if (line.contains("fuse") && line.contains("/mnt/")) {       
							String columns[] = line.split(" ");       
							if (columns != null && columns.length > 1) {            
								if (sd_default.trim().equals(columns[1].trim())) {              
									continue;                      
								}  
								
								path = columns[1];                
							}            
					}          
				}       
			} catch (Exception e) { 
				e.printStackTrace();       
			}  
		File file =new File(path);
		if(!file.exists()){
			file.mkdirs();
		}
		String time = formatter.format(new Date());  
        fileName = time+".txt";  
        collectDeviceInfo(context);
	}
	
	 /** 
     * �ռ��豸������Ϣ 
     * @param ctx 
     */  
    private  void collectDeviceInfo(Context ctx) {  
        try {  
            PackageManager pm = ctx.getPackageManager();  
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);  
            if (pi != null) {  
                String versionName = pi.versionName == null ? "null" : pi.versionName;  
                String versionCode = pi.versionCode + "";  
                infos.put("versionName", versionName);  
                infos.put("versionCode", versionCode);  
            }  
        } catch (NameNotFoundException e) {  
            Log.e(TAG, "an error occured when collect package info");  
        }  
        
        Field[] fields = Build.class.getDeclaredFields();  
        for (Field field : fields) {  
            try {  
                field.setAccessible(true);  
                infos.put(field.getName(), field.get(null).toString());  
                d(TAG, field.getName() + " : " + field.get(null));  
            } catch (Exception e) {  
                e(TAG, "an error occured when collect crash info");  
            }  
        }  
    }  
    
	
	 private   synchronized String saveFile(String msg){
		 	count++;
			if(count == 10){
				count = 2;
			}
			
	    	StringBuffer sb = new StringBuffer();  
	        for (Map.Entry<String, String> entry : infos.entrySet()) {  
	            String key = entry.getKey();  
	            String value = entry.getValue();  
	            sb.append(key + "=" + value + "\n");  
	        }  
	          
	        try {  
	        	String content ="";
	        	String time = formatter.format(new Date())+"---"+System.currentTimeMillis();
	        	if(count == 1){
	        		content = sb.toString()+time+"==>"+msg+"\n";
	        	}else{
	        		content = time+"==>"+msg+"\n";
	        	}
	        	
	           sdCardManager.removeCache(logFileDir);//��־����ɾ��
	          
	            File file 		= new File(logFileDir,fileName);
	           
				if(file != null){
					try {
						sdCardManager.updateFileTime(file);
						if(!file.exists()){
			            	file.createNewFile();
			            }
						
						FileOutputStream	out = new FileOutputStream(file,true);
						out.write(content.getBytes());
						out.flush();
						out.close();
						out = null;
						
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
	            
	            return fileName;  
	        } catch (Exception e) {  
	            Log.e(TAG, "an error occured while writing file...");  
	        }  
	        return null;  
	    }
	 
	 /**
		 * ��ȡ�쳣������Ϣ
		 * @param exception
		 * @return
		 */
		public  String getErrorMsg(Exception exception){
			StringWriter sw = new StringWriter();
			PrintWriter err = new PrintWriter(sw);
			exception.printStackTrace(err);
			return	sw.toString()+"\n";
		}
		
		public  String getErrorMsg(Thread thread, Throwable ex){
			StringWriter sw = new StringWriter();
			PrintWriter err = new PrintWriter(sw);
			ex.printStackTrace(err);
			return	sw.toString()+"\n";
		}
		
		private final  File getExternalCacheDir(Context context) {
			
			if(context == null){
				return null ;
			}
			
			final String cacheDir = "/Android/data/" + context.getPackageName() + "/";
			
			return new File(Environment.getExternalStorageDirectory().getPath() + cacheDir);
		}
		

		public final  String createErrorPath(Context context,String folderName) {
			
			final String dir = Environment.MEDIA_MOUNTED.equals(Environment
					.getExternalStorageState()) ? getExternalCacheDir(context)
					.getPath() : context.getCacheDir().getPath();

			String out = dir != null ? (dir + File.separator + folderName) : null;

			// ·���Ƿ����
			if (out != null) {
				File f = new File(out);

				if (!f.exists()) {
					f.mkdirs();
				}
			}

			return out;
		}
		
		public  String formatDateTime(long time) {
	        if (0 == time) {
	            return "";
	        }
	        
	        return formatter.format(new Date(time));
		}
		
		/**
		 * 保存异常日志
		 */
		public  void saveErrorFile(Thread thread, Throwable ex,String path){
			long timestamp = System.currentTimeMillis();  
	        String time = formatter.format(new Date());  
	        String fileName = "crash-" + time + "-" + timestamp + ".txt";  
			File err 		= new File(path,fileName);
			if(err != null){
				try {
					FileOutputStream out = new FileOutputStream(err,true);
					out.write(getErrorMsg(thread, ex).getBytes());
					out.flush() ;
					out.close() ;
					out = null ;
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
}
