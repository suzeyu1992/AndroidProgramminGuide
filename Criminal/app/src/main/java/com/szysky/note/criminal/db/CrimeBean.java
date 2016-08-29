package com.szysky.note.criminal.db;

import java.util.Date;
import java.util.UUID;

/**
 * Author :  suzeyu
 * Time   :  2016-08-08  下午5:13
 * Blog   :  http://szysky.com
 * GitHub :  https://github.com/suzeyu1992
 *
 * ClassDescription : 记录陋习的JavaBean
 */

public class CrimeBean {
    /**
     * 每个实例的唯一标示
     */
    private UUID mId;

    /**
     * 实例代表的内容
     */
    private String mTitle;

    /**
     *  实例被创建的时间
     */
    private Date mDate;


    /**
     *  代表此陋习是否已经被处理
     */
    private boolean mSolved;

    public CrimeBean(){
        // 给每个实例生成一个为一个ID
        mId = UUID.randomUUID();
        mDate = new Date();
    }

    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean mSolved) {
        this.mSolved = mSolved;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date mDate) {
        this.mDate = mDate;
    }
}
