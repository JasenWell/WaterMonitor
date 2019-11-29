package com.hjh.baselib.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;


import com.hjh.baselib.constants.IConfig;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;

/**
 * Descroption:
 * Created by hjh on 2015/9/28.
 */
public class AppNotifier {

    private final static String TAG = AppNotifier.class.getSimpleName();
    Ringtone ringtone = null;

    protected final static String[] msg_eng = { "sent a message", "sent a picture", "sent a voice",
            "sent location message", "sent a video", "sent a file", "%1 contacts sent %2 messages"
    };
    protected final static String[] msg_ch = { "", "", "", "", "", "",
            ""
    };

//    protected static int notifyID = 0125; // start notification id
//    protected static int foregroundNotifyID = 0555;//前台

    protected NotificationManager notificationManager = null;

    protected HashSet<String> fromUsers = new HashSet<String>();
    protected int notificationNum = 0;

    protected Context appContext;
    protected String packageName;
    protected String[] msgs;
    protected long lastNotifiyTime;
    protected AudioManager audioManager;
    protected Vibrator vibrator;
    protected NotificationInfoProvider notificationInfoProvider;
    private AppPresences presences = AppPresences.getInstance();

    public AppNotifier(Context context){
        init(context);
    }

    private  AppNotifier init(Context context){
        appContext = context;
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        packageName = appContext.getApplicationInfo().packageName;
        if (Locale.getDefault().getLanguage().equals("zh")) {
            msgs = msg_ch;
        } else {
            msgs = msg_eng;
        }

        audioManager = (AudioManager) appContext.getSystemService(Context.AUDIO_SERVICE);
        vibrator = (Vibrator) appContext.getSystemService(Context.VIBRATOR_SERVICE);

        return this;
    }

    public <T>void reset(T message){
        resetNotificationCount();
        cancelNotificaton(message);
    }

    public void resetNotificationCount() {
        notificationNum = 0;
        fromUsers.clear();
    }

    public void cancelAll(){
        if(notificationManager != null){
            notificationManager.cancelAll();
        }
    }

    public <T> void cancelNotificaton(T message) {
        if (notificationManager != null)
            notificationManager.cancel(notificationInfoProvider.getNotifaicationId(message));
    }

    /**
     * 处理新收到的消息，然后发送�?�?
     * @param message
     */
    public synchronized <T>void onNewMsg(T message) {
        // 判断app是否在后台
        if (ActivityManagerTools.isBackgroundRunning(appContext)) {
            sendNotification(message, false);
        } else {
            sendNotification(message, true);

        }

        viberateAndPlayTone(message);
    }

    public synchronized <T>void onNewMesg(List<T> messages) {

        // 判断app是否在后�?
        if (ActivityManagerTools.isBackgroundRunning(appContext)) {
            sendNotification(messages, false);
        } else {
            sendNotification(messages, true);
        }
        viberateAndPlayTone(messages.get(messages.size() - 1));
    }

    /**
     * 发�?通知栏提�?
     * This can be override by subclass to provide customer implementation
     * @param messages
     * @param isForeground
     */
    protected <T>void sendNotification (List<T> messages, boolean isForeground){
        for(T message : messages){
            if(!isForeground){
                notificationNum++;
                if(notificationInfoProvider != null){
                    fromUsers.add(notificationInfoProvider.getSenderName(message));
                }
            }
        }
        sendNotification(messages.get(messages.size()-1), isForeground, false);
    }

    protected <T>void sendNotification (T message, boolean isForeground){
        sendNotification(message, isForeground, true);
    }

    /**
     * 发�?通知栏提�?
     * This can be override by subclass to provide customer implementation
     * @param message
     */
    protected <T> void sendNotification(T message, boolean isForeground, boolean numIncrease) {
        String notifyText =  " ";
        if(notificationInfoProvider != null){
            notifyText = notificationInfoProvider.getSenderName(message);
        }

        try {
            PackageManager packageManager = appContext.getPackageManager();
            String appname = (String) packageManager.getApplicationLabel(appContext.getApplicationInfo());

            // notification titile
            String contentTitle = appname;
            if (notificationInfoProvider != null) {
                String customNotifyText = notificationInfoProvider.getDisplayedText(message);
                String customCotentTitle = notificationInfoProvider.getTitle(message);
                if (customNotifyText != null){
                    // 设置自定义的状�?栏提示内�?
                    notifyText = customNotifyText;
                }

                if (customCotentTitle != null){
                    // 设置自定义的通知栏标�?
                    contentTitle = customCotentTitle;
                }
            }

            // create and send notificaiton
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(appContext)
                    .setSmallIcon(appContext.getApplicationInfo().icon)
                    .setWhen(System.currentTimeMillis())
                    .setAutoCancel(true);//点击清除可从任务栏清除

            Intent msgIntent = appContext.getPackageManager().getLaunchIntentForPackage(packageName);
            if (notificationInfoProvider != null) {
                // 设置自定义的notification点击跳转intent
                msgIntent = notificationInfoProvider.getLaunchIntent(message);
            }

            PendingIntent pendingIntent = PendingIntent.getActivity(appContext, notificationInfoProvider.getNotifaicationId(message), msgIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            if(numIncrease){
                // prepare latest event info section
                if(!isForeground){
                    notificationNum++;
                    if(notificationInfoProvider != null){
                        fromUsers.add(notificationInfoProvider.getSenderName(message));
                    }
                }
            }

            int fromUsersNum = fromUsers.size();
            String summaryBody = msgs[6].replaceFirst("%1", Integer.toString(fromUsersNum)).replaceFirst("%2", Integer.toString(notificationNum));

            if (notificationInfoProvider != null) {
                // lastest text
                String customSummaryBody = notificationInfoProvider.getLatestText(message, fromUsersNum,notificationNum);
                if (customSummaryBody != null){
                    summaryBody = customSummaryBody;
                }

                // small icon
                int smallIcon = notificationInfoProvider.getSmallIcon(message);
                if (smallIcon != 0){
                    mBuilder.setSmallIcon(smallIcon);
                }
            }

            mBuilder.setContentTitle(contentTitle);//设置通知栏标�?
            mBuilder.setTicker(notifyText);//通知首次出现在�?知栏，带上升动画效果�?
            mBuilder.setContentText(summaryBody);//设置通知栏显示内�?
            mBuilder.setContentIntent(pendingIntent);//设置通知栏点击意�?
            // mBuilder.setNumber(notificationNum);
            Notification notification = mBuilder.build();

            if (isForeground) {//如果是前台，通知了马上取�?
                notificationManager.notify(notificationInfoProvider.getNotifaicationId(message), notification);
                if(notificationInfoProvider != null && notificationInfoProvider.cancel()) {
                    notificationManager.cancel(notificationInfoProvider.getNotifaicationId(message));
                }
            } else {
                notificationManager.notify(notificationInfoProvider.getNotifaicationId(message), notification);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 手机震动和声音提示
     */
    public <T>void viberateAndPlayTone(T message) {

        if(!presences.getBoolean(IConfig.KEY_SWITCH_MSG,true)){//默认是接受消息的
            return;
        }

        if (System.currentTimeMillis() - lastNotifiyTime < 1000) {
            // received new messages within 2 seconds, skip play ringtone
            return;
        }

        try {
            lastNotifiyTime = System.currentTimeMillis();

            // 判断是否处于静音模式
            if (audioManager.getRingerMode() == AudioManager.RINGER_MODE_SILENT) {

                return;
            }

            if(presences.getBoolean(IConfig.KEY_SWITCH_VIBRATE, true)){
                long[] pattern = new long[] { 0, 180, 80, 120 };
                vibrator.vibrate(pattern, -1);
            }

            if(presences.getBoolean(IConfig.KEY_SWITCH_SOUND, true)){
                if (ringtone == null) {
                    Uri notificationUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                    ringtone = RingtoneManager.getRingtone(appContext, notificationUri);
                    if (ringtone == null) {
                        return;
                    }
                }

                if (!ringtone.isPlaying()) {
                    String vendor = Build.MANUFACTURER;

                    ringtone.play();
                    // for samsung S3, we meet a bug that the phone will
                    // continue ringtone without stop
                    // so add below special handler to stop it after 3s if
                    // needed
                    if (vendor != null && vendor.toLowerCase().contains("samsung")) {
                        Thread ctlThread = new Thread() {
                            public void run() {
                                try {
                                    Thread.sleep(3000);
                                    if (ringtone.isPlaying()) {
                                        ringtone.stop();
                                    }
                                } catch (Exception e) {
                                }
                            }
                        };
                        ctlThread.run();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 设置NotificationInfoProvider
     *
     * @param provider
     */
    public AppNotifier setNotificationInfoProvider(NotificationInfoProvider provider) {
        notificationInfoProvider = provider;
        return this;
    }

    public  interface NotificationInfoProvider {
        /**
         * 设置发�?notification时状态栏提示新消息的内容(比如Xxx发来了一条图片消�?
         *
         * @param message
         *            接收到的消息
         * @return null为使用默�?
         */
        <T>String getDisplayedText(T message);

        /**
         * 设置notification持续显示的新消息提示(比如2个联系人发来�?条消�?
         *
         * @param message
         *            接收到的消息
         * @param fromUsersNum
         *            发�?人的数量
         * @param messageNum
         *            消息数量
         * @return null为使用默�?
         */
        <T>String getLatestText(T message, int fromUsersNum, int messageNum);

        /**
         * 设置notification标题
         *
         * @param message
         * @return null为使用默�?
         */
        <T>String getTitle(T message);

        /**
         * 设置小图�?
         *
         * @param message
         * @return 0使用默认图标
         */
        <T>int getSmallIcon(T message);

        /**
         * 设置notification点击时的跳转intent
         *
         * @param message
         *            显示在notification上最近的�?��消息
         * @return null为使用默�?
         */
        <T>Intent getLaunchIntent(T message);

        /**
         * 收到消息后是否需要自动取消通知栏
         * @return
         */
        boolean cancel();

        /**
         * 发送者名字
         * @param message
         * @param <T>
         * @return
         */
        <T>String getSenderName(T message);

        <T> int getNotifaicationId(T message);
    }
}
