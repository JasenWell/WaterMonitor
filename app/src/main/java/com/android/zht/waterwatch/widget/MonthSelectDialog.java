package com.android.zht.waterwatch.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.android.zht.waterwatch.R;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by wyl on 2016/8/9.
 */
public class MonthSelectDialog extends Dialog {
    Context context;
    private MonthSelectDialogListener listener;
    private GridView mMonthGrid;
    private GridAdapter mAdapter;
    private int mClickedMonth = 0;
    private int mPickedYear;
    private LinearLayout mLeftBtn;
    private LinearLayout mRightBtn;
    private ImageView mLeftImg;
    private ImageView mRightImg;
    private TextView mYear;
    private Calendar calendar = Calendar.getInstance();
    private ArrayList<String> mItem = new ArrayList<String>() {
        {
            add("1月");
            add("2月");
            add("3月");
            add("4月");
            add("5月");
            add("6月");
            add("7月");
            add("8月");
            add("9月");
            add("10月");
            add("11月");
            add("12月");
        }
    };

    public MonthSelectDialog(Context context) {
        super(context);
    }

    public MonthSelectDialog(Context context, int theme, int year, int monthId) {
        super(context, theme);
        this.context = context;
        mPickedYear = year;
        mClickedMonth = monthId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.monthselect_dialog);
        init();
    }

    public void init() {
        mLeftBtn = (LinearLayout) findViewById(R.id.left_arrow);
        mRightBtn = (LinearLayout) findViewById(R.id.right_arrow);
        mLeftImg = (ImageView) findViewById(R.id.left_img);
        mRightImg = (ImageView)findViewById(R.id.right_img);
        mYear = (TextView) findViewById(R.id.year_value);
        mMonthGrid = (GridView) findViewById(R.id.monthgridview);
        mAdapter = new GridAdapter(this.context, mItem);
        mMonthGrid.setAdapter(mAdapter);

        mYear.setText(String.valueOf(mPickedYear));
        setBtnEnable();
        mMonthGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int viewId,
                                    long position) {
                //arg1是当前item的view，通过它可以获得该项中的各个组件。
                //arg2是当前item的ID。这个id根据在适配器中的写法可以自己定义。
                //arg3是当前的item在listView中的相对位置！
                mAdapter.setSeclection(viewId);
                mAdapter.notifyDataSetChanged();
                listener.MonthSelectDialogListener(mPickedYear,viewId);
                dismiss();
            }
        });

        mLeftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPickedYear--;
                setBtnEnable();
                mYear.setText(String.valueOf(mPickedYear));

            }
        });
        mRightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPickedYear++;
                setBtnEnable();
                mYear.setText(String.valueOf(mPickedYear));
            }
        });


    }

    private void setBtnEnable() {
        int minYear = 2017;
        int currentYear = calendar.get(Calendar.YEAR);
        if((minYear == currentYear) || (minYear>currentYear)){
            mLeftBtn.setEnabled(false);
            mLeftImg.setImageResource(R.mipmap.btn_dosage1_leftno);
            mRightBtn.setEnabled(false);
            mRightImg.setImageResource(R.mipmap.btn_dosage1_rightno);
        } else if(mPickedYear <= minYear) {
            mLeftBtn.setEnabled(false);
            mLeftImg.setImageResource(R.mipmap.btn_dosage1_leftno);
            mRightBtn.setEnabled(true);
            mRightImg.setImageResource(R.mipmap.btn_dosage1_right);
        } else if((mPickedYear > minYear) && (mPickedYear < currentYear)) {
            mLeftBtn.setEnabled(true);
            mLeftImg.setImageResource(R.mipmap.btn_dosage1_left);
            mRightBtn.setEnabled(true);
            mRightImg.setImageResource(R.mipmap.btn_dosage1_right);
        }else if(mPickedYear == currentYear) {
            mLeftBtn.setEnabled(true);
            mLeftImg.setImageResource(R.mipmap.btn_dosage1_left);
            mRightBtn.setEnabled(false);
            mRightImg.setImageResource(R.mipmap.btn_dosage1_rightno);
        }
    }

    private class GridAdapter extends BaseAdapter {
        //https://blog.csdn.net/qq_32226379/article/details/78835454
        private Context context;
        private ArrayList<String> data = new ArrayList<String>();

        public GridAdapter(Context context, ArrayList<String> data) {
            this.context = context;
            this.data = data;
        }

        public void setSeclection(int position) {
            mClickedMonth = position;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int i) {
            return data.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context)
                        .inflate(R.layout.month_gridview_item, null);
                holder = new ViewHolder();
                holder.month_text = (TextView) convertView.findViewById(R.id.month_item);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            convertView.setTag(holder);
            for(int i=0; i<data.size(); i++) {
                holder.month_text.setText(data.get(position));
            }
            TextPaint tp = holder.month_text.getPaint();
            if (mClickedMonth == position) {
                GradientDrawable myGrad = (GradientDrawable)holder.month_text.getBackground();
                myGrad.setColor(context.getResources().getColor(R.color.title_bar_color));
                holder.month_text.setTextColor(Color.WHITE);
                tp.setFakeBoldText(true);
            } else {
                GradientDrawable myGrad = (GradientDrawable)holder.month_text.getBackground();
                myGrad.setColor(Color.TRANSPARENT);
                holder.month_text.setTextColor(Color.BLACK);
                tp.setFakeBoldText(false);
            }

            return convertView;
        }
    }

    static class ViewHolder {

        TextView month_text;
    }

    public void setMonthSelectDialogListener(MonthSelectDialogListener listener) {
        this.listener = listener;
    }

    public interface MonthSelectDialogListener {
        public void MonthSelectDialogListener(int year, int checkedId);
    }

}
