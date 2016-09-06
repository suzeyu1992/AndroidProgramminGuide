package com.szysky.note.runtracker.db;

import java.util.Date;

/**
 * Author :  suzeyu
 * Time   :  2016-09-06  下午3:46
 * Blog   :  http://szysky.com
 * GitHub :  https://github.com/suzeyu1992
 *
 * ClassDescription :   一个实现保存开始日期的Run类, 并添加计算持续时间的方法
 */

public class Run {
    private Date mStartDate;

    /**
     *  为支持查询多张run数据库表并能在应用中加以区分,添加一个区分属性ID
     */
    private long mId;

    public Run(){
        mStartDate = new Date();
    }

    public Date getStartDate() {
        return mStartDate;
    }

    public void setStartDate(Date mStartDate) {
        this.mStartDate = mStartDate;
    }

    public int getDurationSeconds(long endMillis){
        return (int)((endMillis - mStartDate.getTime())/1000);
    }

    public static String formatDuration(int durationSeconds){
        int second = durationSeconds % 60;
        int minutes = ((durationSeconds - second)/60) % 60;
        int hours = ((durationSeconds - (minutes * 60) - second)/ 3600);
        return String.format("%02d:%02d:%02d", hours, minutes, second);
    }

    public long getId() {
        return mId;
    }

    public void setId(long mId) {
        this.mId = mId;
    }
}
