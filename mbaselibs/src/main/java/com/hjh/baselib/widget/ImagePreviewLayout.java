package com.hjh.baselib.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.hjh.baselib.R;
import com.hjh.baselib.image.zoom.PhotoView;


public class ImagePreviewLayout extends FrameLayout {

	static final int MSG_SHOW_ROATE			= 1 ;
    static final int MSG_HIDE_ROATE			= 2 ;
    static final int MSG_BITMAP_ERROR		= 3 ;
    static final int MSG_BITMAP_LOAD		= 4 ;

    private ImageView mRoateView ;
    private TextView mProgressView ;
    private PhotoView mImageView;

    private Handler mHanlder ;

    public ImagePreviewLayout(Context context) {
        this(context,null);
    }

    public ImagePreviewLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        try {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            inflater.inflate(R.layout.preimageview_layout, this);

            mImageView	= (PhotoView) findViewById(R.id.big_photo);
            mRoateView	= (ImageView) findViewById(R.id.rotate_view);
            mProgressView	= (TextView) findViewById(R.id.progress);
            mRoateView.setVisibility(View.GONE);
            mProgressView.setVisibility(View.GONE);

            mHanlder = new Handler() {

                @Override
                public void handleMessage(Message msg) {

                    switch(msg.what){
                        case MSG_SHOW_ROATE :
                            mRoateView.setVisibility(View.VISIBLE);
                            Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.loading_preimage_anim) ;
                            animation.setInterpolator(new LinearInterpolator());
                            mRoateView.startAnimation(animation);
                            break ;
                        case MSG_HIDE_ROATE :
                            mRoateView.setAnimation(null);
                            mRoateView.setVisibility(View.GONE);
                            break ;
                        case MSG_BITMAP_ERROR :
                            mProgressView.setVisibility(View.VISIBLE);
                            mProgressView.setText("加载失败");
                            break ;
                        case MSG_BITMAP_LOAD :

                            mRoateView.setVisibility(View.GONE);
                            mProgressView.setVisibility(View.GONE);
                            if(msg.obj instanceof Bitmap){

                            }
                            break ;
                    }
                }

            } ;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setData(Activity activity, String key){

        String url = null ;

        if (key.contains("/")) {
            url	= key ;
        } else {

        }

    }

    public void recycle(){

    }

    public void restore(){
//		mImageView.restore();
    }


    public void reload(int resid,Drawable drawable){
//		if(resid != 0){
//			mImageView.setBackgroundResource(resid);
//		}else{
//			mImageView.setBackgroundDrawable(drawable);
//		}
    }

    public PhotoView getmImageView() {
        return mImageView;
    }

    public void setmImageView(PhotoView mImageView) {
        this.mImageView = mImageView;
    }

}
