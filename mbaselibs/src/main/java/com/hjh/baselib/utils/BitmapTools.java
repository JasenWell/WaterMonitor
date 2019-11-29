package com.hjh.baselib.utils;

import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;

public final class BitmapTools {

	  public static int MAX_WIDTH = 480;
	  public static int MAX_HEIGHT = 360;
	  
	  public static final Bitmap createBitmap(byte[] data, int target_width, int target_height){
	    try{
	      int width = target_width <= 0 ? MAX_WIDTH : target_width;
	      int height = target_height <= 0 ? MAX_HEIGHT : target_height;
	      int minSideLength = 0;
	      BitmapFactory.Options opts = new BitmapFactory.Options();
	      opts.inJustDecodeBounds = true;
	      BitmapFactory.decodeByteArray(data, 0, data.length, opts);
	      
	      minSideLength = Math.min(width, height);
	      opts.inSampleSize = computeSampleSize(opts, minSideLength, width * height);
	      opts.inJustDecodeBounds = false;
	      opts.inInputShareable = true;
	      opts.inPurgeable = true;
	      opts.inPreferredConfig = Bitmap.Config.RGB_565;
	      
	      return BitmapFactory.decodeByteArray(data, 0, data.length, opts);
	    }catch (Exception e){
	      return null;
	    }catch (OutOfMemoryError e){
	      e.printStackTrace();
	    }
	    
	    return null;
	  }
	  
	  public static final Bitmap createBitmap(String path, int target_width, int target_height){
	    return createBitmap(path, target_width, target_height, Bitmap.Config.RGB_565);
	  }
	  
	  public static final Bitmap createBitmap(String path, int target_width, int target_height, Bitmap.Config config){
	    try{
	      int width = target_width <= 0 ? MAX_WIDTH : target_width;
	      int height = target_height <= 0 ? MAX_HEIGHT : target_height;
	      int minSideLength = 0;
	      BitmapFactory.Options opts = new BitmapFactory.Options();
	      opts.inJustDecodeBounds = true;

	      int degree = parserBitmapDegree(path);
	      BitmapFactory.decodeFile(path, opts);
	      minSideLength = Math.min(width, height);
	      opts.inSampleSize = computeSampleSize(opts, minSideLength, width * height);
	      opts.inJustDecodeBounds = false;
	      opts.inInputShareable = true;
	      opts.inPurgeable = true;
	      opts.inPreferredConfig = (config != null ? config : Bitmap.Config.RGB_565);

	      Bitmap out = BitmapFactory.decodeFile(path, opts);
	      if (out != null) {
	        return RotateBitmap(degree, out);
	      }
	      return out;
	    }catch (Exception e){
	      return null;
	    }catch (OutOfMemoryError e){
	      e.printStackTrace();
	    }
	    return null;
	  }
	  
	  public static int parserBitmapDegree(String path){
	    int degree = 0;
	    try{
	      ExifInterface exifInterface = new ExifInterface(path);
	      int orientation = exifInterface.getAttributeInt("Orientation", 1);
	      switch (orientation){
	      case 6: 
	        degree = 90;
	        break;
	      case 3: 
	        degree = 180;
	        break;
	      case 8: 
	        degree = 270;
	      }
	    }catch (IOException e){
	      e.printStackTrace();
	    }
	    
	    return degree;
	  }
	  
	  public static Bitmap RotateBitmap(int angle, Bitmap bitmap){
	    if (bitmap == null) {
	      return null;
	    }
	    Matrix matrix = new Matrix();
	    matrix.postRotate(angle);
	    
	    Bitmap out = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
	    
	    return out;
	  }
	  
	  private static final int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels){
	    int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);
	    int roundedSize = 1;
	    if (initialSize <= 8){
	      while (roundedSize < initialSize) {
	        roundedSize <<= 1;
	      }
	    }else{
	      roundedSize = (initialSize + 7) / 8 * 8;
	    }
	    
	    return roundedSize;
	  }
	  
	  private static final int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels){
	    double w = options.outWidth;
	    double h = options.outHeight;
	    
	    int lowerBound = maxNumOfPixels == -1 ? 1 : (int)Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
	    int upperBound = minSideLength == -1 ? 128 : (int)Math.min(Math.floor(w / minSideLength), Math.floor(h / minSideLength));
	    if (upperBound < lowerBound) {
	      return lowerBound;
	    }
	    
	    if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
	      return 1;
	    }
	    
	    if (minSideLength == -1) {
	      return lowerBound;
	    }
	    
	    return upperBound;
	  }
}
