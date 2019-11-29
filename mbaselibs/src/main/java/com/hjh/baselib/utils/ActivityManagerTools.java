package com.hjh.baselib.utils;

import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.view.KeyEvent;

/**
 * 活动管理工具(将程序从后台到前台，主要是通过service中注册广播启动)
 * @author hjh
 * 2014-10-24上午11:42:19
 */
public final class ActivityManagerTools {

	/**
	 * if(arg1.getAction().equals("active_activity")){
	 Intent intent = new Intent(getBaseContext(), VisitorActivity.class);
	 intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	 Visitor visitor = (Visitor) arg1.getSerializableExtra(VisitorActivity.VISITOR_INFO);
	 intent.putExtra(VisitorActivity.VISITOR_INFO,visitor);
	 getApplication().startActivity(intent);
	 }
	 */

	/**
	 * 程序是否后台运行
	 */
	public static boolean isBackgroundRunning(Context context){
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
		for (RunningAppProcessInfo appProcess : appProcesses) {
			if (appProcess.processName.equals(context.getPackageName())) {
				if (appProcess.importance == RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
					return true;
				}else{
					return false;
				}
			}
		}
		return false;
	}

	/**
	 * 程序到前台
	 */
	public static void bringToForeground(Context context){
		ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> task_info = manager.getRunningTasks(20);
		String className = "";

		for (int i = 0; i < task_info.size(); i++){

			try {
				if (context.getPackageName().equals(task_info.get(i).topActivity.getPackageName())){
					className = task_info.get(i).topActivity.getClassName();
					Intent intent = new Intent();
					//这里是指从后台返回到前台  前两个的是关键
					intent.setAction(Intent.ACTION_MAIN);
					intent.addCategory(Intent.CATEGORY_LAUNCHER);
					intent.setComponent(new ComponentName(context, Class.forName(className)));//
					intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
							| Intent.FLAG_ACTIVITY_NEW_TASK
							| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
					context.startActivity(intent);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 点击返回键，切换到后台运行
	 * @param activity
	 * @param keyCode
	 * @return
	 */
	public static int onClickKeydown(Activity activity,int keyCode){
		PackageManager pm = activity.getPackageManager();
		ResolveInfo homeInfo =
				pm.resolveActivity(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME), 0);
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			ActivityInfo ai = homeInfo.activityInfo;
			Intent startIntent = new Intent(Intent.ACTION_MAIN);
			startIntent.addCategory(Intent.CATEGORY_LAUNCHER);
			startIntent.setComponent(new ComponentName(ai.packageName, ai.name));
			startActivitySafely(startIntent,activity);
		}

		return keyCode;
	}

	private static void startActivitySafely(Intent intent,Activity activity) {
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		try {
			activity.startActivity(intent);
		} catch (ActivityNotFoundException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 锁屏后弹出界面的设置
	 * 方法一:
	 * 在接收消息广播的onReceive里，跳转到你要显示的界面。如：
	 Intent intent = new Intent(arg0,MainActivity.class);
	 intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	 arg0.startActivity(intent);

	 在该activity的onCreate()方法里：

	 super.onCreate(savedInstanceState);
	 getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
	 setContentView(R.layout.activity_main);

	 * 设置activity的theme属性：

	 android:theme="@android:style/Theme.Wallpaper.NoTitleBar"

	 添加点击事件，进入app，突破锁屏：

	 KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
	 KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("");
	 keyguardLock.disableKeyguard();
	 在menifest中加入该权限：

	 <uses-permission android:name="android.permission.DISABLE_KEYGUARD"></uses-permission>

	 方法二：

	 Android开发在锁屏界面 弹出通知 通知响应后保持锁屏状态

	 注意事项：弹出的通知只能是activity，并且设置全屏，可以在activity中添加dialog或者自己想要的布局，
	 1，2个activity A--->B

	 A中主要设为 开启一个Alarm

	 具体代码：

	 AlarmManager am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

	 PendingIntent pi = PendingIntent.getActivity(FristActivity.this, 1, new Intent(FristActivity.this,MainActivity.class), 0);

	 am.set(AlarmManager.RTC_WAKEUP, 3000, pi);

	 这里我只是test，可以使用am。set里面发送广播的方法 控制是否有music播放等等。

	 2.B里面代码

	 在oncreate（）方法中进行设置

	 final Window win = getWindow();

	 win.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,

	 WindowManager.LayoutParams.FLAG_FULLSCREEN);

	 win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED

	 );//| WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD

	 win.addFlags(WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON

	 | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

	 setContentView(R.layout.activity_main);

	 其中:WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD

	 为解锁flag，不设置则为事件处理后依然为锁屏，

	 记得

	 onresume（）；

	 PowerManager pm = (PowerManager)getSystemService(Context.POWER_SERVICE);

	 mWakelock = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP |PowerManager.SCREEN_DIM_WAKE_LOCK, "SimpleTimer");

	 mWakelock.acquire();

	 onPause（）；

	 mWakelock.release();的释放

	 方法三：

	 怎样在安卓中实现在锁屏状态下弹出对话框，并可以震动和铃声，就像闹钟似的？

	 我想要在应用弹出对话框，程序在后台运行，当达到条件后弹出对话框并有震动和铃声
	 // 解锁
	 * KeyguardManager manager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
	 * if( manager.inKeyguardRestrictedInputMode() ){
	 *  //处于锁定界面,界面则通过KeyguardLock类方法来解锁
	 *  KeyguardLock keyguard = manager.newKeyguardLock(getLocalClassName());
	 *  keyguard.disableKeyguard(); }
	 *  解锁需要权限： <uses-permission android:name="android.permission.DISABLE_KEYGUARD"/>
	 *  //点亮屏幕
	 *  PowerManager pm = (PowerManager)getSystemService(POWER_SERVICE);
	 *  mWakelock = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP |PowerManager.SCREEN_DIM_WAKE_LOCK, "SimpleTimer");
	 *  mWakelock.acquire();
	 *  //....... mWakelock.release();
	 *  点亮屏幕需要权限： <uses-permission android:name="android.permission.WAKE_LOCK"/>
	 *  按照LS的方法 请求强制打开锁，会出现按Power键,系统也不会进入Sleep的现象
	 *  建议参考如下方法，让对话框在锁屏界面上方显示 onCreate方法时使用
	 *  requestWindowFeature(Window.FEATURE_NO_TITLE);
	 *  //hide title     Window win = getWindow();
	 *  WindowManager.LayoutParams winParams = win.getAttributes();
	 *  winParams.flags |= (WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
	 *         | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
	 *               | WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON
	 *                    | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
	 *                     setRequestedOrientation(0);
	 *
	 *  在OnResume时使用
	 *  protected void onResume() {
	 *  super.onResume();
	 *   acquireWakeLock();      }
	 *      private void acquireWakeLock() {
	 *        if (mWakelock == null) {
	 *         Log.i(TAG, "Activity begin start ");
	 *         PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
	 *         mWakelock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK,
	 *         this.getClass().getCanonicalName());
	 *         mWakelock.acquire();     }
	 *         在onPause
	 *         protected void onPause() {          super.onPause();          ......
	 *         releaseWakeLock();      }
	 *         private void releaseWakeLock() {
	 *         if (mWakelock != null && mWakelock.isHeld()) {
	 *         mWakelock.release();
	 *         mWakelock = null;      }

	 *
	 */
	
	
}
