package com.android.zht.waterwatch.util;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.MediaStore.Video.Thumbnails;

public class Utils
{
    
    public static Bitmap decodeSampledBitmap(String path, int sample) 
    {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        // Calculate inSampleSize
        options.inSampleSize = sample;

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }
    
    public static String secondToDate(long second)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date dt = new Date(second * 1000);
        return sdf.format(dt);
    }
    
    public static String getPath(Context context, Uri uri) {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = { MediaStore.Images.Media.DATA };
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver().query(uri, projection,null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
            	
            } finally {
            	if(cursor != null)
            		cursor.close();
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }
    
    public static void getThumbnailData(Context context,String videoId) {
        String[] projection = { Thumbnails._ID, Thumbnails.VIDEO_ID,
                Thumbnails.DATA };
        Cursor cursor = context.getContentResolver().query(Thumbnails.EXTERNAL_CONTENT_URI, projection,
                "_id=?", new String[]{videoId}, null);
        if (cursor.moveToFirst()) {
            int _id;
            int video_id;
            String image_path;
            int _idColumn = cursor.getColumnIndex(Thumbnails._ID);
            int image_idColumn = cursor.getColumnIndex(Thumbnails.VIDEO_ID);//
            int dataColumn = cursor.getColumnIndex(Thumbnails.DATA);
 
            do {
                // Get the field values
                _id = cursor.getInt(_idColumn);
                video_id = cursor.getInt(image_idColumn);
                image_path = cursor.getString(dataColumn);
                image_path.toString();
 
            } while (cursor.moveToNext());
 
        }
    }


    // 可逆的加密算法
    public static String KL(String inStr) {
        // String s = new String(inStr);
        char[] a = inStr.toCharArray();
        for (int i = 0; i < a.length; i++) {
            a[i] = (char) (a[i] ^ 't');
        }
        String s = new String(a);
        return s;
    }

}
