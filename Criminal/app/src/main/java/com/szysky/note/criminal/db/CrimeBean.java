package com.szysky.note.criminal.db;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
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
    private  SimpleDateFormat mSimpleDateFormat;

    /**
     *  定义进行JSON转换的时候存储的键值
     */
    private static final String JSON_ID = "id";
    private static final String JSON_TITLE = "title";
    private static final String JSON_SOLVED = "solved";
    private static final String JSON_DATE = "date";

    {
        mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }


    public CrimeBean(){
        // 给每个实例生成一个为一个ID
        mId = UUID.randomUUID();
        mDate = new Date();
    }

    // 用来在文件中读取数据时, 获取的JSONObject转换成一个实例并赋值属性值
    public CrimeBean(JSONObject jsonObj) throws JSONException{
        mId = UUID.fromString(jsonObj.getString(JSON_ID));
        if (jsonObj.has(JSON_TITLE)){
            mTitle = jsonObj.getString(JSON_TITLE);
        }
        mSolved = jsonObj.getBoolean(JSON_SOLVED);
        mDate = new Date(jsonObj.getString(JSON_DATE));

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

    public String getDate() {
        return mSimpleDateFormat.format(mDate);
    }

    public Date getRawDate(){
        return mDate;
    }


    public void setDate(Date mDate) {
        this.mDate = mDate;
    }

    @Override
    public String toString() {
        return mTitle;
    }

    /**
     *  将实例对象创建为转成JSONObject对象, 并返回
     *
     * @return  封装了本实例的属性值成一个 JSONObject
     */
    public JSONObject toJSON() throws JSONException{
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(JSON_ID, mId.toString());
        jsonObject.put(JSON_TITLE, mTitle);
        jsonObject.put(JSON_SOLVED, mSolved);
        jsonObject.put(JSON_DATE, getRawDate());
        return jsonObject;

    }

}
