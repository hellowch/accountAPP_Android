package com.example.finalproject.Utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import com.example.finalproject.R;


public class MyRecyclerViewItem extends HorizontalScrollView {

    public MyRecyclerViewItem(Context context) {
        super(context);
        init(context,null);
    }

    public MyRecyclerViewItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MyRecyclerViewItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public static final String TAG=MyRecyclerViewItem.class.getSimpleName();
    private boolean isLeft = true;//默认左边
    private int rightLayoutWidth;
    private int leftLayoutWidth;
    private int range;

    public void setRightLayoutWidth(int rightLayoutWidth) {
        this.rightLayoutWidth = rightLayoutWidth;
    }

    public void setLeftLayoutWidth(int leftLayoutWidth) {
        this.leftLayoutWidth = leftLayoutWidth;
    }

    public void setRange(int range) {
        this.range = range;
    }

    private void init(Context context, AttributeSet attrs) {
        leftLayoutWidth = getScreenSize(getContext()).widthPixels;// recyclerview 宽度
        rightLayoutWidth = dp2px(getContext(),200);// 右边布局的宽度
        range = dp2px(getContext(), 30);// 移动多少开始切换阈值
        if (attrs!=null){
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyRecyclerViewItem);
            int indexCount = typedArray.getIndexCount();
            for (int i = 0; i < indexCount; i++) {
                int index = typedArray.getIndex(i);
                if (index==R.styleable.MyRecyclerViewItem_left_width){
                    leftLayoutWidth = typedArray.getInteger(index, 0)==0? leftLayoutWidth : dp2px(context, typedArray.getInteger(index, 0));
                }
                if (index==R.styleable.MyRecyclerViewItem_right_width){
                    rightLayoutWidth = typedArray.getInteger(index, 0)==0? rightLayoutWidth : dp2px(context, typedArray.getInteger(index, 0));
                }
                if (index==R.styleable.MyRecyclerViewItem_move_range){
                    range = typedArray.getInteger(index, 0)==0? range : dp2px(context, typedArray.getInteger(index, 0));
                }
            }
            typedArray.recycle();
        }
    }

    //适配器 bind 方法中调用
    public void apply() {
        isLeft = true;
        changeLayout();
        scrollTo(0, 0);
    }

    private void changeLayout() {
        try {
            ViewGroup mainLayout= (ViewGroup) getChildAt(0);
            ViewGroup left= (ViewGroup) mainLayout.getChildAt(0);
            ViewGroup right= (ViewGroup) mainLayout.getChildAt(1);
            if (left.getMeasuredWidth()== leftLayoutWidth && right.getMeasuredWidth()==rightLayoutWidth){
                Log.i(TAG, "changeLayout: no change");
                return;
            }
            ViewGroup.LayoutParams layoutParams = left.getLayoutParams();
            layoutParams.width = leftLayoutWidth;
            left.setLayoutParams(layoutParams);
            ViewGroup.LayoutParams layoutParams2 = right.getLayoutParams();
            layoutParams2.width = rightLayoutWidth;
            left.setLayoutParams(layoutParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static DisplayMetrics getScreenSize(Context context){
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager=(WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);
        return dm;
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
//            Log.i(getClass().getSimpleName(), "down");
            return true;
        }
        if (ev.getAction() == MotionEvent.ACTION_CANCEL || ev.getAction() == MotionEvent.ACTION_UP) {
//            Log.i(getClass().getSimpleName(), "up");
            if (isLeft) {
                if (getScrollX() > range) {
                    isLeft = false;
                    smoothScrollTo(rightLayoutWidth, 0);
                } else {
                    smoothScrollTo(0, 0);
                }
            } else {
                if (getScrollX() < (rightLayoutWidth - range)) {
                    isLeft = true;
                    smoothScrollTo(0, 0);
                } else {
                    smoothScrollTo(rightLayoutWidth, 0);
                }
            }
            return true;
        }
//        Log.i(getClass().getSimpleName(), "end");
        return super.onTouchEvent(ev);
    }


    public static int dp2px(Context context,float dpValue) {
        DisplayMetrics scale = context.getResources().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, scale);
    }
}
