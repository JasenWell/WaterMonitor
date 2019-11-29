package com.hjh.baselib.utils;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.widget.RemoteViews;


import com.hjh.baselib.R;

import org.apache.http.conn.ConnectTimeoutException;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Descroption:版本管理
 * Created by hjh on 2015/10/23.
 */
public class AppVersionManager {

	private final  int TIME_OUT = 30000;
    private final  int BUFFER_SIZE = 1024 * 8;
    private Context mContext;
    private ProgressDialog progressDialog;
    private static final String TEMP_SUFFIX = ".download";
    /*最终要保存的文件*/
    private File file;
    /*临时生成的文件*/
    private File tempFile;

    private String path;
    /*对文件进行操作的输出流*/
    private RandomAccessFile outputStream;
    /*已经下载到的文件大小*/
    private long downloadSize;
    /*以前下载到的文件大小，downloadSize=previousFileSize+此次复制到的大小*/
    private long previousFileSize;
    /*文件的总大小*/
    private long totalSize;
    private long networkSpeed;
    private long previousTime;
    private long totalTime;
    private boolean interrupt = false;
    private boolean timerInterrupt = false;
    private Timer timer = new Timer();
    private static final int TIMERSLEEPTIME = 100;
    private static final int MSG_DOWN_FAILED = 1;
    private static final int MSG_DOWN_PROGRESS = 2;
    private static final int MSG_DOWN_SUCCESS = 3;
    private static final int MSG_DOWN_PAUSE = 4;
    private static final int MSG_UPDATE_NOTIFY = 5;
    private OnUpdateListener listener;
    private boolean backgroundDownLoad = false;
    private int notifyId = 1;
    private Notification notification;
    private NotificationCompat.Builder notificationCompatBuilder;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case MSG_DOWN_SUCCESS:
                    onDownLoadSuccess(file.getAbsolutePath());
                    break;
                case MSG_DOWN_PROGRESS:
                    onDownLoadProgress(totalSize, getDownloadSize(), networkSpeed);
                    break;
                case MSG_DOWN_FAILED:
                    if(progressDialog!=null&&progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    listener.onDownLoadFailed();
                    break;
                case MSG_DOWN_PAUSE:
                    if(progressDialog!=null&&progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    listener.onPause();
                    break;
                case MSG_UPDATE_NOTIFY:
                    RemoteViews contentView = notificationCompatBuilder.getContentView();
                    contentView.setTextViewText(R.id.tv,mContext.getResources().getString(R.string.app_name)+"下载进度："+msg.arg1+"%");
                    contentView.setProgressBar(R.id.pb,100,msg.arg1,false);
                    NotificationManagerCompat.from(mContext.getApplicationContext()).notify(notifyId,notification);
                    break;
            }
        }
    };

    private static AppVersionManager instance;

    public static void  init(Context context){
        instance = new AppVersionManager(context);
    }

    public static AppVersionManager getInstance(){
        return instance;
    }

    private AppVersionManager(Context context){
        mContext = context;
    }

    public void configApkPath(File rootFile, String fileName){
        this.file = new File(rootFile, fileName);
        this.tempFile = new File(rootFile, fileName + TEMP_SUFFIX);
        //删除原来的临时文件，否则可能安装出问题
        if(tempFile.exists()){
            tempFile.delete();
        }

        this.tempFile = new File(rootFile, fileName + TEMP_SUFFIX);
    }

    public String getAppVersionName(Context context) {
        String versionName = "1.0.0";
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionName;
    }

    public  int getAppVersionCode(Context context) {
        int versionCode = 1;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionCode = pi.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return versionCode;
    }

    private void onDownLoadProgress(long totalSize, long currentSize, long speed) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.setProgress((int) (currentSize * 100 / totalSize));
        }
        if(backgroundDownLoad){
            Message message = handler.obtainMessage();
            message.what = MSG_UPDATE_NOTIFY;
            message.arg1 = (int) (currentSize * 100 / totalSize);
            handler.sendMessage(message);
        }
    }

    private void onDownLoadSuccess(String apkPath) {
        if(backgroundDownLoad){
            backgroundDownLoad = false;
            NotificationManagerCompat.from(mContext.getApplicationContext()).cancel(notifyId);
        }

        installApk(mContext, apkPath);
        if(progressDialog!=null&&progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    public void updateApk(final String url, final OnUpdateListener listener) {
        this.listener = listener;
        new Thread(new Runnable() {
            @Override
            public void run() {
                downloadApkFile(url);
            }
        }).start();


        progressDialog = new ProgressDialog(mContext);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("正在更新中...");
        progressDialog.setCancelable(false);
        progressDialog.setProgress(0);
        progressDialog.setButton(DialogInterface.BUTTON_POSITIVE, "停止", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                pauseDownload();
            }
        });

        progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "后台", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                progressDialog.dismiss();
                backgroundDownLoad = true;
                showNotice();
                //通知栏显示
                if(listener != null){
                    listener.onClickBackground();
                }
            }
        });
        progressDialog.show();
    }

    /** 下载文件 */
    private  void downloadApkFile(String path) {

        try {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            // apk length
            final int length = conn.getContentLength();

            long result = -1;
            // 获取已下载到的文件的大小
            previousFileSize = 0;
            AppUtil.getInstance().deleteTempImgs();

            File tempFile = this.getTempFile();

            if (tempFile.exists()) {
                previousFileSize = this.getTempFile().length();
            }
            totalSize = length + previousFileSize;
            if (file.exists() && totalSize == file.length()) {//存在不用下载
                onDownLoadSuccess(file.getAbsolutePath());
                return;
            } else if (tempFile.exists()) {
                previousFileSize = tempFile.length();
            }

            outputStream = new ProgressReportingRandomAccessFile(tempFile, "rw");
            startTimer();
            int bytesCopied = copy(conn.getInputStream(), outputStream);
            if ((previousFileSize + bytesCopied) != totalSize && totalSize != -1 && !interrupt) {
                Message message = handler.obtainMessage();
                message.what = MSG_DOWN_FAILED;
                handler.sendMessage(message);
                throw new IOException("Download incomplete: " + bytesCopied + " != " + totalSize);
            } else if (interrupt) {
                Message message = handler.obtainMessage();
                message.what = MSG_DOWN_PAUSE;
                handler.sendMessage(message);
                throw new Exception("download has been paused");
            }
            result = bytesCopied;

            // 停止打印
            stopTimer();
            // 保证timer被关闭
            try {
                Thread.sleep(TIMERSLEEPTIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            tempFile.renameTo(file);
            Message message = handler.obtainMessage();
            message.what = MSG_DOWN_SUCCESS;
            handler.sendMessage(message);

        } catch (Exception e) {
            e.printStackTrace();
            Message message = handler.obtainMessage();
            message.what = MSG_DOWN_FAILED;
            handler.sendMessage(message);
        }
    }

    public  void installApk(Context context, String apkPath) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//7.0以上
            uri = FileProvider.getUriForFile(mContext, "com.android.zht.waterwatch.fileprovider", new File(apkPath));
        }else {
            uri = Uri.fromFile(new File(apkPath));
        }
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(intent);
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    public interface OnUpdateListener{
        void onClickBackground();//后台
        void onDownLoadFailed();
        void onPause();
        Activity getActivity();
    }

    public boolean isInterrupt() {
        return interrupt;
    }

    public void setInterrupt(boolean interrupt) {
        this.interrupt = interrupt;
    }

    public long getDownloadSize() {
        return downloadSize;
    }

    public long getTotalSize() {
        return totalSize;
    }

    public double getDownloadSpeed() {
        return this.networkSpeed;
    }

    public void setPreviousFileSize(long previousFileSize) {
        this.previousFileSize = previousFileSize;
    }

    public File getFile() {
        return file;
    }

    public long getTotalTime() {
        return this.totalTime;
    }

    public File getTempFile() {
        return tempFile;
    }

    private void stopTimer() {
        timerInterrupt = true;
        timer.cancel();
    }

    private void stopCopy() {
        interrupt = true;
    }

    private void startTimer() {
        timerInterrupt = false;
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                while (!timerInterrupt) {
                    Message message = handler.obtainMessage();
                    message.what = MSG_DOWN_PROGRESS;
                    handler.sendMessage(message);

                    try {
                        Thread.sleep(TIMERSLEEPTIME);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, 0, 1000);

    }

    public void pauseDownload() {
        stopCopy();
        stopTimer();
    }

    private int copy(InputStream input, RandomAccessFile out) throws IOException {
        interrupt = false;
        if (input == null || out == null) {
            return -1;
        }
        byte[] buffer = new byte[BUFFER_SIZE];
        BufferedInputStream in = new BufferedInputStream(input, BUFFER_SIZE);
        int count = 0, n = 0;
        long errorBlockTimePreviousTime = -1, expireTime = 0;
        try {
            out.seek(out.length());
            previousTime = System.currentTimeMillis();
            while (!interrupt) {
                n = in.read(buffer, 0, BUFFER_SIZE);
                if (n == -1) {
                    break;
                }
                out.write(buffer, 0, n);
                count += n;
                if (networkSpeed == 0) {
                    if (errorBlockTimePreviousTime > 0) {
                        expireTime = System.currentTimeMillis()-errorBlockTimePreviousTime;
                        if (expireTime > TIME_OUT) {
                            throw new ConnectTimeoutException("connection time out.");
                        }
                    } else {
                        errorBlockTimePreviousTime = System.currentTimeMillis();
                    }
                } else {
                    expireTime = 0;
                    errorBlockTimePreviousTime = -1;
                }
            }
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return count;
    }

    private class ProgressReportingRandomAccessFile extends RandomAccessFile {
        private int progress = 0;

        public ProgressReportingRandomAccessFile(File file, String mode)
                throws FileNotFoundException {
            super(file, mode);
        }

        @Override
        public void write(byte[] buffer, int offset, int count) throws IOException {
            super.write(buffer, offset, count);
            progress += count;
            totalTime = System.currentTimeMillis() - previousTime;
            downloadSize = progress + previousFileSize;
            if (totalTime > 0) {
                networkSpeed = (long) ((progress / totalTime) / 1.024);
            }
        }
    }

    public void showNotice(){
        RemoteViews contentView=new RemoteViews(mContext.getPackageName(),R.layout.notification_layout);
        contentView.setTextViewText(R.id.tv,mContext.getResources().getString(R.string.app_name)+"下载进度：");
        contentView.setProgressBar(R.id.pb,100,0,false);
        Intent intent = new Intent(mContext, listener.getActivity().getClass());
        PendingIntent notifyPendingIntent = PendingIntent.getActivity(mContext,1,intent,PendingIntent.FLAG_CANCEL_CURRENT);
        notificationCompatBuilder = new NotificationCompat.Builder(mContext.getApplicationContext());
        notification = notificationCompatBuilder
                // Title for API <16 (4.0 and below) devices.
                .setContentTitle("标题")
                // Content for API <24 (7.0 and below) devices.
                .setContentText("内容")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(
                        mContext.getResources(),
                        R.mipmap.ic_launcher))
                .setContentIntent(notifyPendingIntent)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setColor(ContextCompat.getColor(mContext.getApplicationContext(), R.color.colorPrimary))
                .setCategory(Notification.CATEGORY_REMINDER)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setCustomContentView(contentView)
                .build();
        NotificationManagerCompat.from(mContext.getApplicationContext()).notify(notifyId, notification);
    }

    public  String createNotificationChannel(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//8.0以上
            String channelId = "channelId";
            CharSequence channelName = "channelName";
            String channelDescription ="channelDescription";
            int channelImportance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, channelImportance);
            // 设置描述 最长30字符
            notificationChannel.setDescription(channelDescription);
            // 该渠道的通知是否使用震动
            notificationChannel.enableVibration(true);
            // 设置显示模式
            notificationChannel.setLockscreenVisibility(NotificationCompat.VISIBILITY_PUBLIC);

            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);

            return channelId;
        } else {
            return null;
        }
    }

}
