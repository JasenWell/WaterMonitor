package com.hjh.baselib.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;


import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;


/**
 * 文件处理
 * @author hjh
 * 2015-1-24下午5:06:21
 *
 */
public final class FileTools {

	private static String imagePath;
	private static String tempPath;

	public static void init(String image,String temp){
		imagePath = image;
		tempPath = temp;
	}

	private final static File getExternalCacheDir(Context context) {

		if(context == null){
			return null ;
		}

		final String cacheDir = "/Android/data/" + context.getPackageName() + "/";

		return new File(Environment.getExternalStorageDirectory().getPath() + cacheDir);
	}

	public static String getAbsolutePathFromNoStandardUri(Uri mUri) {

		String filePath = null;
		String mUriString = mUri.toString();
		mUriString = Uri.decode(mUriString);

		String pre1 = "file://" + "/sdcard" + File.separator;
		String pre2 = "file://" + "/mnt/sdcard" + File.separator;

		if (mUriString.startsWith(pre1)) {
			filePath = Environment.getExternalStorageDirectory().getPath()
					+ File.separator + mUriString.substring(pre1.length());
		} else if (mUriString.startsWith(pre2)) {
			filePath = Environment.getExternalStorageDirectory().getPath()
					+ File.separator + mUriString.substring(pre2.length());
		}

		return filePath;
	}

	public static String getAbsoluteImagePath(Activity context, Uri uri) {

		String imagePath = null;

		String[] proj = { Images.Media.DATA };
		Cursor cursor = context.getContentResolver().query(uri, proj, null,null, null);

		if (cursor != null) {

			int column_index = cursor.getColumnIndexOrThrow(Images.Media.DATA);

			if (cursor.getCount() > 0 && cursor.moveToFirst()) {
				imagePath = cursor.getString(column_index);
			}
		}

		return imagePath;
	}

	/** 得到设备可用路径: */
	public final static String createLocalDevicePath(Context context,String folderName) {

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

	public static final String writeBitmapToFile(Context context, String name,Bitmap bitmap) {

		if (bitmap != null && context != null && name != null) {

			try {

				String dir = createLocalDevicePath(context,imagePath);

				if (dir == null) {
					return null;
				}

				File file = new File(dir, name);

				if (file.exists()) {
					file.delete();
				}

				FileOutputStream out = new FileOutputStream(file);

				bitmap.compress(CompressFormat.JPEG, 100, out);

				return dir + name;
			} catch (Exception e) {
				return null;
			}
		}

		return null;
	}

	public static final String getBitmapTempDirectory(Context context){

		Date date 		= DateTools.getCurrtentTimes() ;
		String current 	= DateTools.toDateString(date, DateTools.DATE_FORMAT);
		StringBuffer folder = new StringBuffer() ;

		folder.append(tempPath);

		if(!StringTools.isEmpty(current)){
			folder.append(current) ;
			folder.append("/") ;
		}

		String dir = createLocalDevicePath(context,folder.toString());

		if (dir == null) {
			return null;
		}

		return dir;
	}

	public static final void clearTempFile(final Context context){

		new Thread(new Runnable() {

			@Override
			public void run() {

				try{

					Date date 		= DateTools.getCurrtentTimes() ;
					String current 	= DateTools.toDateString(date, DateTools.DATE_FORMAT);

					String dir = FileTools.createLocalDevicePath(context,tempPath);

					if(StringTools.isEmpty(dir)){
						return ;
					}

					File root = new File(dir) ;

					if(root.isDirectory()){

						File[] list = root.listFiles() ;

						for(File file : list){

							// 清楚历史记录
							if(!file.getName().equals(current)){
								file.delete() ;
							}
						}
					} else {
						root.delete() ;
					}

				}catch(Exception e){
					e.printStackTrace() ;
				}
			}
		}).start() ;
	}

	public static final String createBitmapTempFile(Context context){

		String dir = getBitmapTempDirectory(context);

		if (dir == null) {
			return null;
		}

		return dir + String.valueOf(System.currentTimeMillis());
	}

	/** 创建压缩图片 */
	public final static File createCompressBitmap(Context context,String path){
		return createCompressBitmap(context,80,path);
	}

	/** 创建压缩图片 */
	public final static File createCompressBitmap(Context context,int quality,String path){

		if(StringTools.isEmpty(path)){
			return null ;
		}

		try{
			Bitmap bitmap = BitmapTools.createBitmap(path, 0, 0);
			if(bitmap == null){
				return null ;
			}

			String tempPath = createBitmapTempFile(context);

			return createTempBitmap(bitmap,quality,tempPath);

		}catch(Exception e){
			e.printStackTrace() ;
		}

		return null ;
	}

	/** 创建临时图片文件 */
	public final static File createTempBitmap(Bitmap bitmap,int quality ,String outPath){

		if(bitmap == null || StringTools.isEmpty(outPath)){
			return null ;
		}

		File file = null;
		FileOutputStream out = null;

		try {

			file	= new File(outPath);
			out 	= new FileOutputStream(file);
			bitmap.compress(CompressFormat.JPEG, 80, out);
		} catch (IOException e) {
			return null;
		} finally {

			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
				}
			}
		}

		return file;
	}

	public static final String insertImageToPhotos(Context context,
												   String imagePath, String name, String description){

		try{

			String out = insertImage(context.getContentResolver(),imagePath,name,description);

			if(!StringTools.isEmpty(out)){
				// context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"+ Environment.getExternalStorageDirectory() + out)));
				return out ;
			}
		} catch (Exception e){
			e.printStackTrace() ;
		}

		return null ;
	}

	/** 插入图片到相册中*/
	public static final String insertImage(ContentResolver cr,
										   String imagePath, String name, String description)
			throws FileNotFoundException {

		if(StringTools.isEmpty(imagePath)){
			return null ;
		}

		// Check if file exists with a FileInputStream
		FileInputStream stream = new FileInputStream(imagePath);
		try {
			Bitmap bm = BitmapFactory.decodeFile(imagePath);
			String ret = insertImage(cr, bm, name, description);
			bm.recycle();
			return ret;
		} finally {
			try {
				stream.close();
			} catch (IOException e) {
			}
		}
	}

	public static final String insertImage(ContentResolver cr, Bitmap source,
										   String title, String description) {

		ContentValues values = new ContentValues();
		values.put(Images.Media.TITLE, title);
		values.put(Images.Media.DESCRIPTION, description);
		values.put(Images.Media.MIME_TYPE, "image/jpeg");

		Uri url = null;
		String stringUrl = null; /* value to be returned */

		try {
			url = cr.insert(Images.Media.EXTERNAL_CONTENT_URI, values);

			if (source != null) {
				OutputStream imageOut = cr.openOutputStream(url);
				try {
					source.compress(CompressFormat.JPEG, 80, imageOut);
				} finally {
					imageOut.close();
				}

				long id = ContentUris.parseId(url);

				// Wait until MINI_KIND thumbnail is generated.
				Bitmap miniThumb = Images.Thumbnails.getThumbnail(cr, id,Images.Thumbnails.MINI_KIND, null);

				// This is for backward compatibility.
				StoreThumbnail(cr, miniThumb, id, 50F, 50F,Images.Thumbnails.MICRO_KIND);
			} else {
				cr.delete(url, null, null);
				url = null;
			}
		} catch (Exception e) {
			if (url != null) {
				cr.delete(url, null, null);
				url = null;
			}
		}

		if (url != null) {
			stringUrl = url.toString();
		}

		return stringUrl;
	}

	private static final Bitmap StoreThumbnail(
			ContentResolver cr,
			Bitmap source,
			long id,
			float width, float height,
			int kind) {
		// create the matrix to scale it
		Matrix matrix = new Matrix();

		float scaleX = width / source.getWidth();
		float scaleY = height / source.getHeight();

		matrix.setScale(scaleX, scaleY);

		Bitmap thumb = Bitmap.createBitmap(source, 0, 0,
				source.getWidth(),
				source.getHeight(), matrix,
				true);

		ContentValues values = new ContentValues(4);
		values.put(Images.Thumbnails.KIND,     kind);
		values.put(Images.Thumbnails.IMAGE_ID, (int)id);
		values.put(Images.Thumbnails.HEIGHT,   thumb.getHeight());
		values.put(Images.Thumbnails.WIDTH,    thumb.getWidth());

		Uri url = cr.insert(Images.Thumbnails.EXTERNAL_CONTENT_URI, values);

		try {
			OutputStream thumbOut = cr.openOutputStream(url);

			thumb.compress(CompressFormat.JPEG, 100, thumbOut);
			thumbOut.close();
			return thumb;
		}
		catch (FileNotFoundException ex) {
			return null;
		}
		catch (IOException ex) {
			return null;
		}
	}
}
