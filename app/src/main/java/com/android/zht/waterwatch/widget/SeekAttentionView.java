package com.android.zht.waterwatch.widget;

import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;

import com.android.zht.waterwatch.R;

/**
 * 对控件的抖动
 * 对view的x轴和y轴进行0.9倍到1.1倍的缩放，同时对view进行一定角度的上下旋转。
 * Created by Administrator on 2017/11/13 0013.
 */

public class SeekAttentionView {
    private static ObjectAnimator animator;

    /**
     * view抖动
     * @param iv
     */
    @RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public static void viewShaking(View iv){
        ObjectAnimator animator = SeekAttentionView.tada(iv);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.start();
    }

    /**
     * view晃动
     * @param iv
     */
    @RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public  static void viewSloshing(View iv){
        ObjectAnimator nopeAnimator = SeekAttentionView.nope(iv);
        nopeAnimator.setRepeatCount(ValueAnimator.INFINITE);
        nopeAnimator.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public static ObjectAnimator tada(View view) {
        return tada(view, 1f);
    }

    /**
     * 控件抖动的方法
     *
     * @param view
     * @param shakeFactor
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public static ObjectAnimator tada(View view, float shakeFactor) {

        PropertyValuesHolder pvhScaleX = PropertyValuesHolder.ofKeyframe(View.SCALE_X,
                Keyframe.ofFloat(0f, 1f),
                Keyframe.ofFloat(.1f, .9f),
                Keyframe.ofFloat(.2f, .9f),
                Keyframe.ofFloat(.3f, 1.1f),
                Keyframe.ofFloat(.4f, 1.1f),
                Keyframe.ofFloat(.5f, 1.1f),
                Keyframe.ofFloat(.6f, 1.1f),
                Keyframe.ofFloat(.7f, 1.1f),
                Keyframe.ofFloat(.8f, 1.1f),
                Keyframe.ofFloat(.9f, 1.1f),
                Keyframe.ofFloat(1f, 1f)
        );

        PropertyValuesHolder pvhScaleY = PropertyValuesHolder.ofKeyframe(View.SCALE_Y,
                Keyframe.ofFloat(0f, 1f),
                Keyframe.ofFloat(.1f, .9f),
                Keyframe.ofFloat(.2f, .9f),
                Keyframe.ofFloat(.3f, 1.1f),
                Keyframe.ofFloat(.4f, 1.1f),
                Keyframe.ofFloat(.5f, 1.1f),
                Keyframe.ofFloat(.6f, 1.1f),
                Keyframe.ofFloat(.7f, 1.1f),
                Keyframe.ofFloat(.8f, 1.1f),
                Keyframe.ofFloat(.9f, 1.1f),
                Keyframe.ofFloat(1f, 1f)
        );

        PropertyValuesHolder pvhRotate = PropertyValuesHolder.ofKeyframe(View.ROTATION,
                Keyframe.ofFloat(0f, 0f),
                Keyframe.ofFloat(.1f, -3f * shakeFactor),
                Keyframe.ofFloat(.2f, -3f * shakeFactor),
                Keyframe.ofFloat(.3f, 3f * shakeFactor),
                Keyframe.ofFloat(.4f, -3f * shakeFactor),
                Keyframe.ofFloat(.5f, 3f * shakeFactor),
                Keyframe.ofFloat(.6f, -3f * shakeFactor),
                Keyframe.ofFloat(.7f, 3f * shakeFactor),
                Keyframe.ofFloat(.8f, -3f * shakeFactor),
                Keyframe.ofFloat(.9f, 3f * shakeFactor),
                Keyframe.ofFloat(1f, 0)
        );

        return ObjectAnimator.ofPropertyValuesHolder(view, pvhScaleX, pvhScaleY, pvhRotate).
                setDuration(1000);
    }

    /**
     * 左右摇晃的效果
     *
     * @param view
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public static ObjectAnimator nope(View view) {
        int delta = view.getResources().getDimensionPixelOffset(R.dimen.margin_0_5normal_size);

        PropertyValuesHolder pvhTranslateX = PropertyValuesHolder.ofKeyframe(View.TRANSLATION_X,
                Keyframe.ofFloat(0f, 0),
                Keyframe.ofFloat(.10f, -delta),
                Keyframe.ofFloat(.26f, delta),
                Keyframe.ofFloat(.42f, -delta),
                Keyframe.ofFloat(.58f, delta),
                Keyframe.ofFloat(.74f, -delta),
                Keyframe.ofFloat(.90f, delta),
                Keyframe.ofFloat(1f, 0f)
        );

        return ObjectAnimator.ofPropertyValuesHolder(view, pvhTranslateX).
                setDuration(500);
    }

    /**
     * 闪烁方法
     */
    @RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private static ObjectAnimator flicker(View context) {
        PropertyValuesHolder pvhScaleX = PropertyValuesHolder.ofKeyframe(View.SCALE_X,
                Keyframe.ofFloat(0f, 1f),
                Keyframe.ofFloat(.3f, .9f),
                Keyframe.ofFloat(.4f, .9f),
                Keyframe.ofFloat(.5f, 1.3f),
                Keyframe.ofFloat(.6f, 1.3f),
                Keyframe.ofFloat(.7f, 1.3f),
                Keyframe.ofFloat(.8f, 1.3f),
                Keyframe.ofFloat(.9f, 1.3f),
                Keyframe.ofFloat(1f, 1.3f),
                Keyframe.ofFloat(1.2f, 1.3f),
                Keyframe.ofFloat(1f, 1.3f)
        );

        PropertyValuesHolder pvhScaleY = PropertyValuesHolder.ofKeyframe(View.SCALE_Y,
                Keyframe.ofFloat(0f, 1f),
                Keyframe.ofFloat(.3f, .9f),
                Keyframe.ofFloat(.4f, .9f),
                Keyframe.ofFloat(.5f, 1.3f),
                Keyframe.ofFloat(.6f, 1.3f),
                Keyframe.ofFloat(.7f, 1.3f),
                Keyframe.ofFloat(.8f, 1.3f),
                Keyframe.ofFloat(.9f, 1.3f),
                Keyframe.ofFloat(1f, 1.3f),
                Keyframe.ofFloat(1.2f, 1.3f),
                Keyframe.ofFloat(1f, 1.3f)
        );
        return ObjectAnimator.ofPropertyValuesHolder(context, pvhScaleX, pvhScaleY).
                setDuration(600);
    }

    /**
     * 闪耀的textview
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    public static void flickerText(View context) {
        animator = flicker(context);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.start();
    }

    /**
     * 关掉闪烁
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    public static void notFlickerText(View context) {

        if (animator == null) {
            animator = flicker(context);
        }
        animator.cancel();
    }

    /**
     * View渐隐动画效果
     */
    @RequiresApi(api = Build.VERSION_CODES.FROYO)
    public static void setHideAnimation(AlphaAnimation mHideAnimation, View view, int duration) {
        if (null == view || duration < 0) {
            return;
        }
        if (null != mHideAnimation) {
            mHideAnimation.cancel();
        }
        mHideAnimation = new AlphaAnimation(1.0f, 0.0f);
        mHideAnimation.setDuration(duration);
        mHideAnimation.setFillAfter(true);
        view.startAnimation(mHideAnimation);
    }

    /**
     * textview 高亮处理
     * @param context：上下文
     * @param iv_backgroundl：textView后面放一个ImageView，当背景
     * @param text:需要高亮处理的文字控件
     * @param mHideAnimation：传个AlphaAnimation的对象，不用初始化，在setHideAnimation方法中判断是否为空进行初始化
     */
    @RequiresApi(api = Build.VERSION_CODES.FROYO)
    public static void TextViewHeightLight(Context context, View iv_backgroundl, TextView text, AlphaAnimation mHideAnimation) {
        text.setTextColor(context.getResources().getColor(R.color.white));
        iv_backgroundl.setBackgroundResource(R.mipmap.circle_red);
        setHideAnimation(mHideAnimation, iv_backgroundl, 1000);
    }
}




