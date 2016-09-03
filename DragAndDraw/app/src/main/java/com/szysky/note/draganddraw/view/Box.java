package com.szysky.note.draganddraw.view;

import android.graphics.PointF;

/**
 * Author :  suzeyu
 * Time   :  2016-09-03  下午12:43
 * Blog   :  http://szysky.com
 * GitHub :  https://github.com/suzeyu1992
 *
 * ClassDescription :   用来表示一个矩形框的定义数据, 当用户触摸视图界面的时候, 新的Box对象将会创建并添加到
 *                      现有的矩形框数组中
 */

public class Box {
    private PointF mOrigin;
    private PointF mCurrent;


    public Box(PointF origin){
        mOrigin = origin;
    }

    public PointF getOrigin() {
        return mOrigin;
    }



    public PointF getCurrent() {
        return mCurrent;
    }

    public void setCurrent(PointF mCurrent) {
        this.mCurrent = mCurrent;
    }
}
