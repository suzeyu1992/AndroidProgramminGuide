package com.szysky.note.draganddraw.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

/**
 * Author :  suzeyu
 * Time   :  2016-09-03  下午12:34
 * Blog   :  http://szysky.com
 * GitHub :  https://github.com/suzeyu1992
 * ClassDescription :
 */

public class BoxDrawingView extends View {
    private static final String TAG = BoxDrawingView.class.getSimpleName();

    private Box mCurrentBox;
    /**
     *  用来保存一个所有轨迹点对象的数组
     */
    private ArrayList<Box> mBoxes = new ArrayList<Box>();

    private Paint mBoxPaint;
    private Paint mBackgroundPaint;

    public BoxDrawingView(Context context) {
        super(context);
    }

    public BoxDrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //  当在xml布局中进行加载的时候会回调此方法
        mBoxPaint = new Paint();
        mBoxPaint.setColor(0x22ff0000);

        //  设置画背景的画笔
        mBackgroundPaint = new Paint();
        mBackgroundPaint.setColor(0xfff8efe0);

    }

    public BoxDrawingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        PointF curr = new PointF(event.getX(), event.getY());

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                Log.i(TAG, "@@-> Down");
                // reset drawing state
                mCurrentBox = new Box(curr);
                mBoxes.add(mCurrentBox);
                break;

            case MotionEvent.ACTION_MOVE:
                Log.i(TAG, "@@-> Move");
                if (mCurrentBox != null){
                    mCurrentBox.setCurrent(curr);
                    invalidate();
                }
                break;

            case MotionEvent.ACTION_UP:
                Log.i(TAG, "@@-> Up");
                mCurrentBox = null;
                break;

            case MotionEvent.ACTION_CANCEL:
                Log.i(TAG, "@@-> Cancel");
                mCurrentBox = null;

                break;
        }

        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //  Fill the background
        canvas.drawPaint(mBackgroundPaint);

        for (Box box: mBoxes){
            float left = Math.min(box.getOrigin().x, box.getCurrent().x);
            float right = Math.max(box.getOrigin().x, box.getCurrent().x);
            float top = Math.min(box.getOrigin().y, box.getCurrent().y);
            float bottom = Math.max(box.getOrigin().y, box.getCurrent().y);

            canvas.drawRect(left, top, right, bottom, mBoxPaint);

        }
    }
}
