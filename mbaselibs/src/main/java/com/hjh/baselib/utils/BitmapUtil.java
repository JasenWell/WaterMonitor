package com.hjh.baselib.utils;

import android.graphics.Bitmap;
import android.view.View;

/**
 * Created by zlang on 2017/12/20.
 */

public class BitmapUtil {

    public static Bitmap viewToBitmap(View view){
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        return bitmap;
    }

}
