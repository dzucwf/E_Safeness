package com.safeness.doctor.ui.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * Created by EISAVISA on 2015/2/12.
 */
@SuppressLint({"ClickableViewAccessibility"})
public class DoctorDataGrid extends GridView {

    private static final int speed = 80;
    private long dragResponseMS = 800L;
    private boolean isDrag = false;
    private int itemsize = -1;
    private int mDownScrollBorder;
    private int mDownX;
    private int mDownY;
    private Bitmap mDragBitmap;
    private ImageView mDragImageView;
    private int mDragPosition;
    private Handler mHandler = new Handler();
    private Runnable mLongClickRunnable = new Runnable()
    {
        public void run()
        {
            DoctorDataGrid.this.isDrag = true;
            DoctorDataGrid.this.mVibrator.vibrate(50L);
            DoctorDataGrid.this.mStartDragItemView.setVisibility(View.INVISIBLE);
            DoctorDataGrid.this.createDragImage(DoctorDataGrid.this.mDragBitmap, DoctorDataGrid.this.mDownX, DoctorDataGrid.this.mDownY);
        }
    };
    private int mOffset2Left;
    private int mOffset2Top;
    private int mPoint2ItemLeft;
    private int mPoint2ItemTop;
    private Runnable mScrollRunnable = new Runnable()
    {
        public void run()
        {
           int  i = 0;
            if (DoctorDataGrid.this.moveY > DoctorDataGrid.this.mUpScrollBorder)
            {

                DoctorDataGrid.this.mHandler.postDelayed(DoctorDataGrid.this.mScrollRunnable, 25L);
            }else{
                i = 80;
                DoctorDataGrid.this.onSwapItem(DoctorDataGrid.this.moveX, DoctorDataGrid.this.moveY);
                View localView = DoctorDataGrid.this.getChildAt(DoctorDataGrid.this.mDragPosition - DoctorDataGrid.this.getFirstVisiblePosition());
                DoctorDataGrid.this.smoothScrollToPositionFromTop(DoctorDataGrid.this.mDragPosition, i + localView.getTop());
                //return;
                if (DoctorDataGrid.this.moveY < DoctorDataGrid.this.mDownScrollBorder)
                {

                    DoctorDataGrid.this.mHandler.postDelayed(DoctorDataGrid.this.mScrollRunnable, 25L);

                }
                DoctorDataGrid.this.mHandler.removeCallbacks(DoctorDataGrid.this.mScrollRunnable);

            }

        }
    };
    private View mStartDragItemView = null;
    private int mStatusHeight;
    private int mUpScrollBorder;
    private Vibrator mVibrator;
    private WindowManager.LayoutParams mWindowLayoutParams;
    private WindowManager mWindowManager;
    private int moveX;
    private int moveY;
    private OnChanageListener onChanageListener;
    private boolean spec = false;

    public DoctorDataGrid(Context paramContext)
    {
        this(paramContext, null);
    }

    public DoctorDataGrid(Context paramContext, AttributeSet paramAttributeSet)
    {
        this(paramContext, paramAttributeSet, 0);
    }

    public DoctorDataGrid(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
    {
        super(paramContext, paramAttributeSet, paramInt);
        this.mVibrator = ((Vibrator)paramContext.getSystemService(Service.VIBRATOR_SERVICE));
        this.mWindowManager = ((WindowManager)paramContext.getSystemService(Context.WINDOW_SERVICE));
        this.mStatusHeight = getStatusHeight(paramContext);
    }

    private void createDragImage(Bitmap paramBitmap, int paramInt1, int paramInt2)
    {
        this.mWindowLayoutParams = new WindowManager.LayoutParams();
        this.mWindowLayoutParams.format = -3;
        this.mWindowLayoutParams.gravity = 51;
        this.mWindowLayoutParams.x = (paramInt1 - this.mPoint2ItemLeft + this.mOffset2Left);
        this.mWindowLayoutParams.y = (paramInt2 - this.mPoint2ItemTop + this.mOffset2Top - this.mStatusHeight);
        this.mWindowLayoutParams.alpha = 0.55F;
        this.mWindowLayoutParams.width = -2;
        this.mWindowLayoutParams.height = -2;
        this.mWindowLayoutParams.flags = 24;
        this.mDragImageView = new ImageView(getContext());
        this.mDragImageView.setImageBitmap(paramBitmap);
        this.mWindowManager.addView(this.mDragImageView, this.mWindowLayoutParams);
    }

    private static int getStatusHeight(Context paramContext)
    {
        Rect localRect = new Rect();
        ((Activity)paramContext).getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        int i = localRect.top;
        if (i == 0);
        try
        {
            Class localClass = Class.forName("com.android.internal.R$dimen");
            Object localObject = localClass.newInstance();
            int j = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString());
            int k = paramContext.getResources().getDimensionPixelSize(j);
            i = k;
            return i;
        }
        catch (Exception localException)
        {
            localException.printStackTrace();
        }
        return i;
    }

    private boolean isTouchInItem(View paramView, int paramInt1, int paramInt2)
    {
        if (paramView == null);
        int i;
        int j;
        do
        {

            i = paramView.getLeft();
            j = paramView.getTop();
        }
        while ((paramInt1 < i) || (paramInt1 > i + paramView.getWidth()) || (paramInt2 < j) || (paramInt2 > j + paramView.getHeight()));
        return true;
    }

    private void onDragItem(int paramInt1, int paramInt2)
    {
        this.mWindowLayoutParams.x = (paramInt1 - this.mPoint2ItemLeft + this.mOffset2Left);
        this.mWindowLayoutParams.y = (paramInt2 - this.mPoint2ItemTop + this.mOffset2Top - this.mStatusHeight);
        this.mWindowManager.updateViewLayout(this.mDragImageView, this.mWindowLayoutParams);
        onSwapItem(paramInt1, paramInt2);
        this.mHandler.post(this.mScrollRunnable);
    }

    private void onStopDrag()
    {
        getChildAt(this.mDragPosition - getFirstVisiblePosition()).setVisibility(View.VISIBLE);
        removeDragImage();
    }

    private void onSwapItem(int paramInt1, int paramInt2)
    {
        int i = pointToPosition(paramInt1, paramInt2);
        if (i == this.itemsize);

        while ((i == this.mDragPosition) || (i == -1));
        getChildAt(i - getFirstVisiblePosition()).setVisibility(View.INVISIBLE);
        getChildAt(this.mDragPosition - getFirstVisiblePosition()).setVisibility(View.VISIBLE);
        if (this.onChanageListener != null)
            this.onChanageListener.onChange(this.mDragPosition, i);
        this.mDragPosition = i;
    }

    private void removeDragImage()
    {
        if (this.mDragImageView != null)
        {
            this.mWindowManager.removeView(this.mDragImageView);
            this.mDragImageView = null;
        }
    }

    public boolean dispatchTouchEvent(MotionEvent paramMotionEvent)
    {
        switch (paramMotionEvent.getAction())
        {

            case 0:
                this.mDownX = (int)paramMotionEvent.getX();
                this.mDownY = (int)paramMotionEvent.getY();
                this.mDragPosition = pointToPosition(this.mDownX, this.mDownY);
                if (this.mDragPosition == -1)
                    return super.dispatchTouchEvent(paramMotionEvent);
                if (this.mDragPosition == this.itemsize)
                    return super.dispatchTouchEvent(paramMotionEvent);
                this.mHandler.postDelayed(this.mLongClickRunnable, this.dragResponseMS);
                this.mStartDragItemView = getChildAt(this.mDragPosition - getFirstVisiblePosition());
                this.mPoint2ItemTop = (this.mDownY - this.mStartDragItemView.getTop());
                this.mPoint2ItemLeft = (this.mDownX - this.mStartDragItemView.getLeft());
                this.mOffset2Top = (int)(paramMotionEvent.getRawY() - this.mDownY);
                this.mOffset2Left = (int)(paramMotionEvent.getRawX() - this.mDownX);
                this.mDownScrollBorder = (getHeight() / 4);
                this.mUpScrollBorder = (3 * getHeight() / 4);
                this.mStartDragItemView.setDrawingCacheEnabled(true);
                this.mDragBitmap = Bitmap.createBitmap(this.mStartDragItemView.getDrawingCache());
                this.mStartDragItemView.destroyDrawingCache();
                break;
            case 2:
                int i = (int)paramMotionEvent.getX();
                int j = (int)paramMotionEvent.getY();
                if (isTouchInItem(this.mStartDragItemView, i, j))
                    break;
            case 1:
                this.mHandler.removeCallbacks(this.mLongClickRunnable);
                this.mHandler.removeCallbacks(this.mScrollRunnable);
                break;
            default:
        }
        return super.dispatchTouchEvent(paramMotionEvent);

    }

    public void onMeasure(int paramInt1, int paramInt2)
    {
        if (this.spec)
        {
            super.onMeasure(paramInt1, View.MeasureSpec.makeMeasureSpec(536870911, -2147483648));
            return;
        }
        super.onMeasure(paramInt1, paramInt2);
    }

    public boolean onTouchEvent(MotionEvent paramMotionEvent)
    {
        if ((this.isDrag) && (this.mDragImageView != null))
        {
            switch (paramMotionEvent.getAction())
            {
                default:
                case 2:
                    this.moveX = (int)paramMotionEvent.getX();
                    this.moveY = (int)paramMotionEvent.getY();
                    onDragItem(this.moveX, this.moveY);
                case 1:
                    onStopDrag();
                    this.isDrag = false;
            }

        }
        return super.onTouchEvent(paramMotionEvent);
    }

    public void setDragResponseMS(long paramLong)
    {
        this.dragResponseMS = paramLong;
    }

    public void setItemsize(int paramInt)
    {
        this.itemsize = paramInt;
    }

    public void setOnChangeListener(OnChanageListener paramOnChanageListener)
    {
        this.onChanageListener = paramOnChanageListener;
    }

    public void setSpec(boolean paramBoolean)
    {
        this.spec = paramBoolean;
    }

    public static abstract interface OnChanageListener
    {
        public abstract void onChange(int paramInt1, int paramInt2);
    }
}
