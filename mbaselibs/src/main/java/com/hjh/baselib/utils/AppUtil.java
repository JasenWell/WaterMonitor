package com.hjh.baselib.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;


import com.hjh.baselib.constants.IConfig;
import com.hjh.baselib.constants.ModuleConfig;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by hjh on 2015/8/26.
 */
public class AppUtil {

    static final String TAG = AppUtil.class.getSimpleName();

    public static final SimpleDateFormat PUBLISHFORMAT = new SimpleDateFormat(
            "yyyy-M-dd");
    public static final SimpleDateFormat NAMEFORMAT = new SimpleDateFormat(
            "yyyyMMdd_HHmmss");
    public static final SimpleDateFormat SHORTFORMAT = new SimpleDateFormat(
            "HH:mm");
    public static final SimpleDateFormat IMGNAMEFORMAT = new SimpleDateFormat(
            "HHmmss");
    public static final SimpleDateFormat FORMAT_5 = new SimpleDateFormat(
            "yyyy-MM-dd hh:mm:ss");
    public static SimpleDateFormat mDateFormat = new SimpleDateFormat("MM-dd HH:mm");

    /**
     * control of color drawable background deep
     */
    private static final int COLOR_DEEP = 25;
    /**
     * control of bitmap drawable background deep
     */
    private static final int DRAW_DEEP = 25;
    private static Context mContext;
    public  static AppUtil instance;
    private AppNotifier appNotifier;


    public static void init(Context context){
        if (instance == null) {
            mContext = context;
            instance = new AppUtil();
        }
    }

    private AppUtil(){//数据库和http未初始化
        PathUtil.init(mContext);
        FileTools.init(IConfig.PATH_IMAGE, IConfig.PATH_TEMP);
        AppPresences.init(mContext);
        AppVersionManager.init(mContext);
        NetworkUtil.getInstance().setContext(mContext);
        AppLogger.init(mContext, ModuleConfig.PATH_LOG).setDebug(true);//false则不保存调试信息
    }

    public static AppUtil getInstance(){
        return instance;
    }

    public AppNotifier getDefaultNotifier(){
        if(appNotifier == null) {
            return new AppNotifier(mContext);
        }else {
            return appNotifier;
        }
    }

    public String formatDateTime(long time) {
        if (0 == time) {
            return "";
        }

        return mDateFormat.format(new Date(time));
    }

    public String getRecordName() {

        Date curDate = new Date(System.currentTimeMillis());

        return "rc" + NAMEFORMAT.format(curDate) + ".3gp";
    }

    public String getImgFrontName() {

        Date curDate = new Date(System.currentTimeMillis());
        return "img" + IMGNAMEFORMAT.format(curDate) + "_";
    }

    public File getImgFilePath(Context context, String fileName) {

        File file = null;
        String path = getBitmapTempDirectory(context);

        File dir = new File(path);

        if(!dir.exists()){
            dir.mkdir() ;
        }

        if (!StringTools.isEmpty(path)) {
            file = new File(path + fileName + ".jpg");
        }

        return file;
    }

    /** 创建压缩图片 */
    public final File createCompressBitmap(String path, Context context) {
        return FileTools.createCompressBitmap(context, path);
    }

    public  final String createBitmapTempFile(Context context) {

        String dir = getBitmapTempDirectory(context);

        if (dir == null) {
            return null;
        }

        return dir + String.valueOf(System.currentTimeMillis());
    }

    public  final String getBitmapTempDirectory(Context context) {
        return FileTools.getBitmapTempDirectory(context);
    }

    /**
     * 删除了除今天外的所有文件
     * @param context
     */
    public  final void clearTempFile(Context context) {
        FileTools.clearTempFile(context);
    }

    // 分享图片路径
    public String getShareImagePath(Context context) {

        String dir = FileTools.createLocalDevicePath(context,
                "");

        File file = new File(dir, "");

        if (!file.exists()) {
            return null;
        }

        return file.getAbsolutePath();
    }

    // 重构文件
    public  final void copyDrawableToSdcard(final Context context,
                                                  final int resid) {

        if (context == null) {
            return;
        }

        new Thread(new Runnable() {

            @Override
            public void run() {

                String path = getShareImagePath(context);

                if (!StringTools.isEmpty(path)) {
                    return;
                }

                Bitmap bitmap = BitmapFactory.decodeResource(
                        context.getResources(), resid);

//				FileTools.writeBitmapToFile(context,
//						TotooleConfig.SHARED_IMAGE_NAME, bitmap);

            }
        }).start();
    }

    public Date toDate(long milliseconds) {

        try {
            return new Date(milliseconds);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getTotooleMsg(String from, String sendUser) {

        // message 1
        StringBuilder message = new StringBuilder();
        message.append("Totoole号:");
        message.append(sendUser);
        message.append(" 邀请参加");
        message.append(from);

        message.append("途图乐下载地址:");
        message.append("http://www.totoole.cn:8080/Totoole.apk");

        return message.toString();
    }

    public  int getHeight(View view) {

        int w = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);

        view.measure(w, h);

        return view.getMeasuredHeight();
    }

    public String parseMS(Long ms) {

        long curms = System.currentTimeMillis();
        long result = curms - ms;

        if (result < 60000) {
            return "刚刚";
        } else if (result < 3600000) {
            result /= 60000;
            return result + " 分钟前";
        } else if (result < 86400000) {
            if (result > curms % 86400000) {
                return "昨天";
            }
            result /= 3600000;
            return result + " 小时前";
        } else if (result < 608400000) {
            result /= 86400000;
            return result + " 天前";
        } else {
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(ms);
            return SHORTFORMAT.format(c.getTime());
        }
    }

    public String getPublishFrontTime(Date publishDate) {

        long curms = System.currentTimeMillis();
        long result = curms - publishDate.getTime();

        if (result < 86400000) {

            if (result > curms % 86400000) {
                return PUBLISHFORMAT.format(publishDate);
            }

            return SHORTFORMAT.format(publishDate);
        } else {
            return PUBLISHFORMAT.format(publishDate);
        }

    }

    public String getShortTime(Date date) {
        return date != null ? SHORTFORMAT.format(date) : "";
    }

    /** 获得年月日 */
    public String getYearMonthDay(Date date) {
        return date != null ? PUBLISHFORMAT.format(date) : "";
    }

    public  int findIndex(String[] content, String element) {

        int count = 0;

        for (String data : content) {

            if (element.equals(data)) {
                return count;
            }

            count++;
        }

        return -1;
    }

    public  int toInt(Object obj) {

        if (obj == null)

            return 0;

        return toInt(obj.toString(), 0);
    }

    public  int toInt(String str, int defValue) {

        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
        }

        return defValue;
    }

    public String getDistance(double lat1, double lot1, double lat2,
                              double lot2) {

        try {
            double radLat1 = lat1 * Math.PI / 180.0;
            double radLat2 = lat2 * Math.PI / 180.0;
            double a = radLat1 - radLat2;
            double b = lot1 * Math.PI / 180.0 - lot2 * Math.PI / 180.0;
            double distance = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2),
                    2)
                    + Math.cos(radLat1)
                    * Math.cos(radLat2)
                    * Math.pow(Math.sin(b / 2), 2)));
            distance = distance * 6378.137;
            distance = Math.round(distance * 10000);
            // if (distance / 1000 > 0) {
            return (distance / 1000) + "km";
            // }
            // return distance + "m";
        } catch (Exception e) {
            return "data errer";
        }
    }


    @SuppressWarnings("deprecation")
    public ArrayList<String> getGalleryPhotos(Activity act) {
        ArrayList<String> galleryList = new ArrayList<String>();
        try {
            final String[] columns = { MediaStore.Images.Media.DATA,
                    MediaStore.Images.Media._ID };
            final String orderBy = MediaStore.Images.Media._ID;
            Cursor imagecursor = act.managedQuery(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns,
                    null, null, orderBy);
            if (imagecursor != null && imagecursor.getCount() > 0) {
                while (imagecursor.moveToNext()) {
                    String item = new String();
                    int dataColumnIndex = imagecursor
                            .getColumnIndex(MediaStore.Images.Media.DATA);
                    item = imagecursor.getString(dataColumnIndex);
                    galleryList.add(item);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Collections.reverse(galleryList);
        return galleryList;
    }

    public Bitmap convertViewToBitmap(View view) {
        Bitmap bitmap = null;
        try {
            int width = view.getWidth();
            int height = view.getHeight();
            if (width != 0 && height != 0) {
                bitmap = Bitmap.createBitmap(width, height,
                        Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                view.layout(0, 0, width, height);
                view.setBackgroundColor(Color.WHITE);
                view.draw(canvas);
            }
        } catch (Exception e) {
            bitmap = null;
            e.getStackTrace();
        }
        return bitmap;

    }

    public String saveImageToGallery(Context context, Bitmap bmp,
                                     boolean isPng) {
        if (bmp == null) {
            return null;
        }

        String fileName;
        if (isPng) {
            fileName = System.currentTimeMillis() + ".png";
        } else {
            fileName = System.currentTimeMillis() + ".jpg";
        }
        File file = new File(PathUtil.getInstance().getImagePath(), fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            if (isPng) {
                bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
            } else {
                bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            }
            bmp.recycle();
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                Uri.fromFile(PathUtil.getInstance().getImagePath())));
        return file.getAbsolutePath();
    }

    /*
    * 保存照片并更新图库
    */
    public  boolean saveImageToGallery(Context context, File oldFile) {

        if (oldFile != null && oldFile.exists()) {

            File newFile = new File(PathUtil.getInstance().getImagePath(),oldFile.getName() + ".jpg");
            InputStream inStream = null;
            FileOutputStream fs = null;
            try {

                //copy file
                int byteread = 0;
                inStream = new FileInputStream(oldFile);
                fs = new FileOutputStream(newFile);
                byte[] buffer = new byte[1024];
                while ((byteread = inStream.read(buffer)) != -1) {
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
                fs.close();

                // 通知图库更新
                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(newFile)));

                return true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public  void showToast(Context context, String text) {
        CustomToast.showToast(context, text, 1000,false);
    }

    public  void addClickedEffect(View view) {
        final Drawable backDrawable = view.getBackground();
        if(backDrawable instanceof BitmapDrawable) {
            view.setBackgroundDrawable(getStateListDrawable(view.getBackground()));
        } else {
            view.setOnTouchListener(new View.OnTouchListener() {

                @SuppressLint("ClickableViewAccessibility")
                @Override
                public boolean onTouch(View view, MotionEvent event) {
                    if(event.getAction()== MotionEvent.ACTION_DOWN) {
                        Drawable selecteDrawable = getSelectedDrawable(view.getBackground());
                        view.setBackgroundDrawable(selecteDrawable);
                    } else if (event.getAction()== MotionEvent.ACTION_UP) {
                        view.setBackgroundDrawable(backDrawable);
                    }
                    return false;
                }
            });
        }
    }

    private StateListDrawable getStateListDrawable(Drawable normal) {
        StateListDrawable listDrawable = new StateListDrawable();
        Drawable pressed = getSelectedDrawable(normal);
        listDrawable.addState(new int[] { android.R.attr.state_pressed }, pressed);
        listDrawable.addState(new int[] { android.R.attr.state_selected }, pressed);
        listDrawable.addState(new int[] { android.R.attr.state_enabled }, normal);
        return listDrawable;
    }

    /**
     * 传入一个drawable，将此drawable颜色加深组成一个drawable返回，此函数经常用于为button设置按下效果
     * @param mDrawable 传入的drawable，类型可以为bitmapDrawable/ColorDrawable/GradientDrawable
     * 		/ninePatchDrawable/insetDrawable/clipDrawable/layerDrawable/AnimationDrawable/
     *     levelListDrawable/PaintDrawable/PictureDrawable/RotateDrawable/未设置
     * @return 返回的drawable，将其直接赋值给button的backgroundDrawable
     */
    private Drawable getSelectedDrawable(Drawable mDrawable) {
        Drawable resultDrawable = null;
        if(mDrawable instanceof BitmapDrawable) {
            resultDrawable = getSelectedDrawable((BitmapDrawable) mDrawable);
        } else if (mDrawable instanceof ColorDrawable) {
            resultDrawable = getSelectedDrawable((ColorDrawable) mDrawable);
        } else if (mDrawable instanceof GradientDrawable) {
            resultDrawable = getSelectedDrawable((GradientDrawable) mDrawable);
        } else if (mDrawable instanceof ShapeDrawable) {
            resultDrawable = getSelectedDrawable((ShapeDrawable) mDrawable);
        }
        return resultDrawable;
    }

    /**
     * 传入一个drawable，将此drawable颜色加深为另一个drawable返回，此函数经常用于为button设置按下效果
     * @param mDrawable 传入的drawable，类型为bitmapDrawable
     * @return 返回的drawable，将其直接赋值给button的backgroundDrawable
     */
    @SuppressWarnings("deprecation")
    private Drawable getSelectedDrawable(BitmapDrawable mDrawable) {
        Bitmap srcBitmap = mDrawable.getBitmap();
        Bitmap bmp = Bitmap.createBitmap(srcBitmap.getWidth(),
                srcBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        ColorMatrix cMatrix = new ColorMatrix();
        cMatrix.set(new float[] {
                1, 0, 0, 0, -DRAW_DEEP,
                0, 1, 0, 0, -DRAW_DEEP,
                0, 0, 1, 0, -DRAW_DEEP,
                0, 0, 0, 1, 0 });
        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(cMatrix));
        Canvas canvas = new Canvas(bmp);
        canvas.drawBitmap(srcBitmap, 0, 0, paint);
        return new BitmapDrawable(bmp);
    }

    /**
     * 传入一个drawable，将此drawable颜色加深组成一个drawable返回，此函数经常用于为button设置按下效果
     * @param mDrawable 传入的drawable，类型为ColorDrawable
     * @return 返回的drawable，将其直接赋值给button的backgroundDrawable
     */
    @SuppressLint("NewApi")
    private Drawable getSelectedDrawable(ColorDrawable mDrawable) {
        return new ColorDrawable(deepColor(mDrawable.getColor()));
    }

    /**
     * 传入一个drawable，将此drawable颜色加深组成一个statelistdrawable返回，此函数经常用于为button设置按下效果
     * @param mDrawable 传入的drawable，类型为GradientDrawable
     * @return 返回的statelistdrawable，将其直接赋值给button的backgroundDrawable
     */
    private Drawable getSelectedDrawable(GradientDrawable mDrawable) {
        GradientDrawable gradientDrawable = (GradientDrawable) mDrawable.getConstantState().newDrawable();
        gradientDrawable.mutate();
        gradientDrawable.setColorFilter(new ColorMatrixColorFilter(new ColorMatrix(new float[] {
                1, 0, 0, 0, -DRAW_DEEP,
                0, 1, 0, 0, -DRAW_DEEP,
                0, 0, 1, 0, -DRAW_DEEP,
                0, 0, 0, 1, 0 })));
        return gradientDrawable;
    }

    /**
     * 将srcColor的颜色加深
     * @param srcColor 需要加深的颜色
     * @return
     */
    public  int deepColor(int srcColor) {
        return deepColor(srcColor, COLOR_DEEP);
    }

    /**
     * 将srcColor的颜色加深
     * @param srcColor 需要加深的颜色
     * @param color_deep 需要加深的程度
     * @return
     */
    private  int deepColor(int srcColor, final int color_deep) {
        int dstColor = 0;
        int srcAlpha = srcColor & 0xff000000;
        int srcRed = srcColor & 0x00ff0000;
        int srcGreen = srcColor & 0x0000ff00;
        int srcBlue = srcColor & 0x000000ff;
        int dstAlpha = srcAlpha;
        int dstRed = srcRed > COLOR_DEEP * 0x00010000 ? (srcRed - color_deep * 0x00010000) : 0;
        int dstGreen = srcGreen > COLOR_DEEP * 0x00000100 ? (srcGreen - color_deep * 0x00000100) : 0;
        int dstBlue = srcBlue > COLOR_DEEP * 0x00000001 ? (srcBlue - color_deep * 0x00000001) : 0;
        dstColor = dstAlpha+dstRed+dstGreen+dstBlue;
        return dstColor;
    }

    /*
     * 将图片依照参数转化为圆形
     */
    public Bitmap toRoundBitmap(Bitmap bitmap, int width, int height){
        try{
            Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);
            Paint paint = new Paint();
            int cx = width/2;
            int cy = height/2;
            int radius = Math.min(cx, cy);
            canvas.save();
            canvas.drawCircle(cx, cy, radius, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, 0, 0, paint);
            canvas.restore();
            return output;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }

    }

    /*
     * 将图片转化为圆形
     */
    public Bitmap toRoundBitmap(Bitmap bitmap) {
        try {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            float roundPx;
            float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
            if (width <= height) {
                roundPx = width / 2;
                top = 0;
                bottom = width;
                left = 0;
                right = width;
                height = width;
                dst_left = 0;
                dst_top = 0;
                dst_right = width;
                dst_bottom = width;
            } else {
                roundPx = height / 2;
                float clip = (width - height) / 2;
                left = clip;
                right = width - clip;
                top = 0;
                bottom = height;
                width = height;
                dst_left = 0;
                dst_top = 0;
                dst_right = height;
                dst_bottom = height;
            }
            Bitmap output = Bitmap
                    .createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);
            final int color = 0xff424242;
            final Paint paint = new Paint();
            final Rect src = new Rect((int) left, (int) top, (int) right,
                    (int) bottom);
            final Rect dst = new Rect((int) dst_left, (int) dst_top,
                    (int) dst_right, (int) dst_bottom);
            final RectF rectF = new RectF(dst);
            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, src, dst, paint);
            bitmap.recycle();
            bitmap = null;
            return output;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //显示原生图片尺寸大小
    public Bitmap getPathBitmap(Uri imageFilePath, int dw, int dh) throws FileNotFoundException {
        // 获取屏幕的宽和高
        BitmapFactory.Options op = new BitmapFactory.Options();
        op.inJustDecodeBounds = true;
        // 由于使用了MediaStore存储，这里根据URI获取输入流的形式
        InputStream is = null;
        try {
            is = mContext.getContentResolver().openInputStream(imageFilePath);
            BitmapFactory.decodeStream(is, null, op);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ignored) {
                }
            }
        }

        int wRatio = (int) Math.ceil(op.outWidth / (float) dw); // 计算宽度比例
        int hRatio = (int) Math.ceil(op.outHeight / (float) dh); // 计算高度比例

        /**
         * 接下来，我们就需要判断是否需要缩放以及到底对宽还是高进行缩放。 如果高和宽不是全都超出了屏幕，那么无需缩放。
         * 如果高和宽都超出了屏幕大小，则如何选择缩放呢》 这需要判断wRatio和hRatio的大小
         * 大的一个将被缩放，因为缩放大的时，小的应该自动进行同比率缩放。 缩放使用的还是inSampleSize变量
         */
        if (wRatio > 1 && hRatio > 1) {
            if (wRatio > hRatio) {
                op.inSampleSize = wRatio;
            } else {
                op.inSampleSize = hRatio;
            }
        }
        op.inJustDecodeBounds = false; // 注意这里，一定要设置为false，因为上面我们将其设置为true来获取图片尺寸了
        op.inPreferredConfig = Bitmap.Config.ARGB_8888;
        op.inPurgeable = true;// 同时设置才会有效
        op.inInputShareable = true;// 当系统内存不够时候图片自动被回收

        Bitmap pic = null;
        is = null;
        try {
            is = mContext.getContentResolver().openInputStream(imageFilePath);
            for (int i = 1; i <= 3; i++) {
                try {
                    if(i == 3) {
                        op.inPreferredConfig = Bitmap.Config.RGB_565;
                    }
                    pic = BitmapFactory.decodeStream(is, null, op);
                    break;
                } catch (OutOfMemoryError e1) {
                    e1.printStackTrace();
                    op.inSampleSize = op.inSampleSize + i;
                }
            }
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return pic;
    }




    public void imgExcute(ImageView imageView, ImageCallBack icb, String... params) {
        LoadBitAsync loadBitAsynk = new LoadBitAsync(imageView, icb);
        loadBitAsynk.execute(params);
    }

    public class LoadBitAsync extends AsyncTask<String, Integer, Bitmap> {
        ImageView imageView;
        ImageCallBack icb;

        LoadBitAsync(ImageView imageView, ImageCallBack icb) {
            this.imageView = imageView;
            this.icb = icb;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap bitmap = null;
            try {
                if (params != null) {
                    for (int i = 0; i < params.length; i++) {
                        bitmap = getPathBitmap(Uri.fromFile(new File(params[i])), 200, 200);
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            if (result != null) {
                icb.displayImage(imageView, result);
            }
        }
    }

    public interface ImageCallBack{
        void displayImage(ImageView imageView, Bitmap bitmap);
    }

    /**
     * 调用系统电话
     * @param context
     * @param number
     */
    public  void callPhone(Context context, String number) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(number));//"tel:" + number
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public void callDial(Context context, String number){
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(number));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public void callQQ(Context context, String qq){
        String url="mqqwpa://im/chat?chat_type=wpa&uin="+qq;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
    /**
     * 根据原始路径压缩图片
     * @param originalPath
     * @return
     */
    public String getCompressPath(String originalPath){

        return compressImage(generateBitmap(originalPath));//压缩好比例大小后再进行质量压缩
    }

    public Bitmap generateBitmap(String originalPath){
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(originalPath,newOpts);//此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了

        for(int i = 0; i<= 3; i++){
            try {
                bitmap = BitmapFactory.decodeFile(originalPath, newOpts);
                break;
            }catch (OutOfMemoryError error){
                newOpts.inSampleSize += i;
            }
        }

        return bitmap;
    }

    //将bitmap压缩,返回压缩后的路径
    private String comp(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        if( baos.toByteArray().length / 1024>1024) {//判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, 50, baos);//这里压缩50%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        newOpts.inPreferredConfig = Bitmap.Config.RGB_565;//降低图片从ARGB888到RGB565
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
    }

    public String compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while ( baos.toByteArray().length / 1024 > 1024) {    //循环判断如果压缩后图片是否大于1024kb(1M),大于继续压缩
            baos.reset();//重置baos即清空baos
            options -= 10;//每次都减少10
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中

        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中

        try {
            File file = newCompressImgToSD(System.currentTimeMillis()+"");
            OutputStream out = new FileOutputStream(file);
            out.write(baos.toByteArray());
            out.flush();
            out.close();
            return file.getAbsolutePath();
        }catch (Exception e){
            return null;
        }

    }

    /**
     * 获取图片压缩后的字节流
     * @param originalPath
     * @return
     */
    public byte[] getbytes(String originalPath){

        Bitmap image = generateBitmap(originalPath);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while ( baos.toByteArray().length / 1024 > 1024) {    //循环判断如果压缩后图片是否大于1024kb(1M),大于继续压缩
            baos.reset();//重置baos即清空baos
            options -= 10;//每次都减少10
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中

        }

        return baos.toByteArray();
    }

    public File newCompressImgToSD(String imgName) {
        File imgFile = new File(PathUtil.getInstance().getTempPath(),imgName + ".jpg");
        if (imgFile.exists()) {
            imgFile.delete();
        }
        imgFile = new File(PathUtil.getInstance().getTempPath(),imgName + ".jpg");
        return imgFile;
    }

    public  void deleteTempImgs(){
        if (PathUtil.getInstance().getTempPath().exists()) {
            File[] files = PathUtil.getInstance().getTempPath().listFiles();
            for(File file : files){
                file.delete();
            }
        }
    }
    
    public String getTotalCacheSize(Context context) throws Exception {
        long cacheSize = getFolderSize(context.getCacheDir());
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            cacheSize += getFolderSize(context.getExternalCacheDir());
        }
        return getFormatSize(cacheSize);
    }

    public void clearAllCache(Context context) {
        deleteDir(context.getCacheDir());
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            deleteDir(context.getExternalCacheDir());
        }
    }

    private boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        if (dir != null){
            return dir.delete();
        }else {
            return false;
        }

    }

    // 获取文件
    //Context.getExternalFilesDir() --> SDCard/Android/data/你的应用的包名/files/ 目录，一般放一些长时间保存的数据
    //Context.getExternalCacheDir() --> SDCard/Android/data/你的应用包名/cache/目录，一般存放临时缓存数据
    public long getFolderSize(File file) throws Exception {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                // 如果下面还有文件
                if (fileList[i].isDirectory()) {
                    size = size + getFolderSize(fileList[i]);
                } else {
                    size = size + fileList[i].length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * 格式化单位
     * @param size
     */
    public String getFormatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return size + "B";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
                + "TB";
    }

    public String collectBasicInfo(){
        return "PHONE_NAME: " + Build.MODEL + ", SDK_VERSION:"
                + Build.VERSION.SDK + ", SYSTEM_VERSION:"
                + Build.VERSION.RELEASE;
    }
    
    public String collectDeviceInfo(){
    	Map<String, Object> infos = new HashMap<>();
		try {
            PackageManager fields = mContext.getPackageManager();
            PackageInfo field = fields.getPackageInfo(mContext.getPackageName(), PackageManager.GET_ACTIVITIES);
            if(field != null) {
                String versionName = field.versionName == null?"null":field.versionName;
                String versionCode = String.valueOf(field.versionCode);
                infos.put("versionName", versionName);
                infos.put("versionCode", versionCode);
            }
        } catch (Exception var9) {
            
        }

        Field[] var10 = Build.class.getDeclaredFields();
        Field[] var6 = var10;
        int var13 = var10.length;

        for(int var12 = 0; var12 < var13; ++var12) {
            Field var11 = var6[var12];

            try {
                var11.setAccessible(true);
                infos.put(var11.getName(), var11.get((Object)null).toString());
            } catch (Exception var8) {
                
            }
        }
        
        String device = infos.toString();
        return device;
	}
    
    public String getErrorMsg(Throwable ex) {
        StringWriter sw = new StringWriter();
        PrintWriter err = new PrintWriter(sw);
        ex.printStackTrace(err);
        return sw.toString();
    }

    /**
     * 从asset路径下读取对应文件转String输出,默认取地区数据
     * @param mContext
     * @param fileName region.json
     * @return
     */
    public String getJson(Context mContext, String fileName) {
        if(fileName == null)fileName = "region.json";
        StringBuilder sb = new StringBuilder();
        AssetManager am = mContext.getAssets();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(am.open(fileName)));
            String next = "";
            while (null != (next = br.readLine())) {
                sb.append(next);
            }
        } catch (IOException e) {
            e.printStackTrace();
            sb.delete(0, sb.length());
        }
        return sb.toString().trim();
    }


}
