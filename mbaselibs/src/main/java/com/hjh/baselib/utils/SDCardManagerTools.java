package com.hjh.baselib.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore.Video;
import android.text.format.Formatter;
import android.util.Log;

/**
 * SD卡容量管理
 * @author hjh
 * 2014-10-24上午11:57:25
 */
public final class SDCardManagerTools {

	private final String TAG = SDCardManagerTools.class.getSimpleName();
	private static SDCardManagerTools instance;
	private long expireTime = 30*24*60*60*1000;//文件的过期时间,默认为1月
	private long cacheSize  = 10*1024*1024;//缓存大小,默认10MB
	private float percent = 0.4f;//在SD卡空间不够时需要删除的文件百分百,默认为40%

	private SDCardManagerTools(){

	}

	public static SDCardManagerTools getInstance(){
		if(instance == null){
			synchronized (SDCardManagerTools.class) {
				if(instance == null){
					instance = new SDCardManagerTools();
				}
			}
		}

		return instance;
	}

	public long getCacheSize() {
		return cacheSize;
	}

	public void setCacheSize(int cacheSize) {
		this.cacheSize = cacheSize*1024*1024;
	}

	public float getPercent() {
		return percent;
	}

	public void setPercent(float percent) {
		this.percent = percent;
	}

	public long getExpireTime() {
		return expireTime;
	}
	//传递的为天数
	public void setExpireTime(int expireTime) {
		this.expireTime = expireTime*24*60*60*1000;
	}

	/**
	 * 判斷sd卡是否存在
	 * @return
	 */
	public  boolean ExistSDCard() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 获取SD卡可用大小
	 * @return
	 */
	public  long getSDFreeSize(){
		//取得SD卡文件路径
		File path = Environment.getExternalStorageDirectory();
		StatFs sf = new StatFs(path.getPath());
		//获取单个数据块的大小(Byte)
		long blockSize = sf.getBlockSize();
		//空闲的数据块的数量
		long freeBlocks = sf.getAvailableBlocks();
		//返回SD卡空闲大小
		//return freeBlocks * blockSize;  //单位Byte
		//return (freeBlocks * blockSize)/1024;   //单位KB
		return (freeBlocks * blockSize)/1024 /1024; //单位MB
	}

	/**
	 * 获取SD卡总大小
	 * @return
	 */
	public  long getSDAllSize(){
		//取得SD卡文件路径
		File path = Environment.getExternalStorageDirectory();
		StatFs sf = new StatFs(path.getPath());
		//获取单个数据块的大小(Byte)
		long blockSize = sf.getBlockSize();
		//获取所有数据块数
		long allBlocks = sf.getBlockCount();
		//返回SD卡大小
		//return allBlocks * blockSize; //单位Byte
		//return (allBlocks * blockSize)/1024; //单位KB
		return (allBlocks * blockSize)/1024/1024; //单位MB
	}

	/**
	 * 更新文件最后修改时间
	 * @param file 需要修改的具体文件
	 */
	public void updateFileTime(File file){
		long currentTime = System.currentTimeMillis();
		file.setLastModified(currentTime);
	}
	/**
	 * 得到目录大小
	 * @return
	 */
	private long getDirSize(File[] files){
		long dirSize = 0; //文件大小 Byte
		for (int i = 0; i < files.length;i++) {
			dirSize += files[i].length();
		}
		return dirSize;
	}

	/**
	 * 本地缓存优化，在SD卡剩余空间不够时操作，例如缓存分配10MB
	 * @param dirFile
	 */
	public void removeCache(File dirFile){
		File [] files = dirFile.listFiles();
		if(files == null){
			return;
		}

		long dirSize = getDirSize(files); //文件大小 Byte
		if(dirSize > cacheSize || cacheSize/1024/1024 > getSDFreeSize()){
			for(File file : dirFile.listFiles()){
				removeExpiredCache(file);
			}
		}

//	    removeCache(dirFile);
		File [] files2 = dirFile.listFiles();
		if(files2 == null){
			return;
		}

		long size = getDirSize(files2); //文件大小 Byte

		if (size > cacheSize || cacheSize/1024/1024 > getSDFreeSize()) {
			int removeFactor = (int) ((percent *files.length) + 1);
			Arrays.sort(files, new FileLastModifySort());
			Log.i(TAG, " hjh ==> Clear some expiredcache files ");

			for (int i = 0; i < removeFactor; i++) {
				files[i].delete();
			}
		}
	}

	/**
	 * 删除过期文件
	 * @param file 检查文件是否过期(长时间未使用)，过期则删除
	 */
	public final void removeExpiredCache(File file){
		if((System.currentTimeMillis() - file.lastModified()) > expireTime){
			Log.i(TAG, "hjh ==> 删除过期文件");
			file.delete();
		}
	}

	private final  File getExternalCacheDir(Context context) {

		if(context == null){
			return null ;
		}

		final String cacheDir = "/Android/data/" + context.getPackageName() + "/";

		return new File(Environment.getExternalStorageDirectory().getPath() + cacheDir);
	}

	/** 得到设备可用路径: */
	public final  String createLocalDevicePath(Context context,String folderName) {

		final String dir = Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState()) ? getExternalCacheDir(context)
				.getPath() : context.getCacheDir().getPath();

		String out = dir != null ? (dir + File.separator + folderName) : null;

		// 路径是否存在
		if (out != null) {
			File f = new File(out);

			if (!f.exists()) {
				f.mkdirs();
			}
		}

		return out;
	}

	/**
	 * 获取手机总内存
	 * @return
	 */
	public  String getTotalMemory(Activity activity) {
		String str1 = "/proc/meminfo";// 系统内存信息文件
		String str2;
		String[] arrayOfString;
		long initial_memory = 0;
		try {
			FileReader localFileReader = new FileReader(str1);
			BufferedReader localBufferedReader = new BufferedReader(
					localFileReader, 8192);
			str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小

			arrayOfString = str2.split("//s+");
			for (String num : arrayOfString) {
				Log.i(str2, num + "/t");
			}

			initial_memory = Integer.valueOf(arrayOfString[1]).intValue() * 1024;// 获得系统总内存，单位是KB，乘以1024转换为Byte
			localBufferedReader.close();

		} catch (IOException e) {
		}
		return Formatter.formatFileSize(activity.getBaseContext(), initial_memory);// Byte转换为KB或者MB，内存大小规格化
	}

	/**
	 * 获取手机可用内存
	 * @param activity
	 * @return
	 */
	public  String getAvailMemory(Activity activity) {// 获取android当前可用内存大小

		ActivityManager am = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo mi = new MemoryInfo();
		am.getMemoryInfo(mi);
		//mi.availMem; 当前系统的可用内存

		return Formatter.formatFileSize(activity.getBaseContext(), mi.availMem);// 将获取的内存大小规格化
	}

	/**
	 * 对文件按修改时间的排序类
	 * @author hjh
	 * 2014-12-15上午10:28:55
	 */
	private class FileLastModifySort implements Comparator<File>{

		@Override
		public int compare(File arg0, File arg1) {
			if (arg0.lastModified() >arg1.lastModified()) {
				return 1;
			} else if (arg0.lastModified() ==arg1.lastModified()) {
				return 0;
			} else {
				return -1;
			}
		}

	}

}
