package com.hjh.baselib.widget;

import android.app.Activity;
import android.content.Intent;
import com.hjh.baselib.R;
import java.util.Stack;

public final class AppActivityManager {

    private  Stack<Activity> activityStack = new Stack<Activity>();

    private  Stack<Activity> allActivityStack = new Stack<Activity>();

    private static AppActivityManager instance;

    public static AppActivityManager getInstance(){
        if(instance == null){
            init();
        }
        return instance;
    }

    private static synchronized void init(){
        if(instance == null){
            instance = new AppActivityManager();
        }
    }

    public  Stack<Activity> getActivityStack() {
        return activityStack;
    }

    public  void setActivityStack(Stack<Activity> activityStack) {
        this.activityStack = activityStack;
    }

    public  Stack<Activity> getAllActivityStack() {
        return allActivityStack;
    }

    public  void setAllActivityStack(Stack<Activity> allActivityStack) {
        this.allActivityStack = allActivityStack;
    }

    public  void addActivity(Activity activity){

        if(activity != null) {
            activityStack.add(activity);
            allActivityStack.add(activity);
        }
    }

    public  void removeActivity(Activity activity){

        if(activity != null){
            activityStack.remove(activity) ;
            allActivityStack.remove(activity) ;
        }
    }

    public  void finishAllActivity(){

        for(Activity activity : activityStack){

            if(activity != null && !activity.isFinishing()){
                activity.finish() ;
            }
        }

        // clear stack
        clearAllStack() ;
    }

    public  void clearAllActivity(){

        for(Activity activity : allActivityStack){

            if(activity != null && !activity.isFinishing()){
                activity.finish() ;
            }
        }

        allActivityStack.clear() ;
    }

    public  void clearAllStack(){
        activityStack.clear();
    }

    public  void AppExit() {
        try {
            finishAllActivity();
            //System.exit(0);
        } catch (Exception e) {	}
    }

    /** enter main activity*/
    public  void switchMainActivity(Activity activity,boolean finish){
        switchMainActivity(activity,finish,false);
    }

    /** reboot enter main activity */
    public  void switchMainActivity(Activity activity,boolean finish,boolean isRebootHomeActivity){

        if(activity == null){
            return ;
        }

    }

    /** start activity with left in anim */
    public  void startActivityWithAnim(Activity old,Intent intent){

        if(old == null || intent == null){
            return ;
        }

        // start activity
        old.startActivity(intent) ;
        //R.anim.push_left_in, R.anim.push_left_out
        // set adnim
        old.overridePendingTransition(R.anim.push_left_in, R.anim.push_not_move);
    }

    public  void startActivityWithAnim(Activity old,Intent intent,int requestCode){

        if(old == null || intent == null){
            return ;
        }

        // start activity
        old.startActivityForResult(intent, requestCode);
        // set adnim
        old.overridePendingTransition(R.anim.push_left_in, R.anim.push_not_move);
    }

    /** finish activity with left out anim */
    public  void finishWithAnim(Activity old){
        old.finish() ;
        old.overridePendingTransition(R.anim.push_not_move, R.anim.push_left_out);
    }

    public  void finishWithAnim(Activity old,int resultCode){
        old.setResult(resultCode);
        old.finish() ;
        old.overridePendingTransition(R.anim.push_not_move, R.anim.push_left_out);
    }
}
