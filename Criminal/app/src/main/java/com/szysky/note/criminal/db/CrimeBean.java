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

    /**
     *  拍照陋习的图片
     */
    private PhotoBean mPhoto;

    private  SimpleDateFormat mSimpleDateFormat;

    /**
     *  定义进行JSON转换的时候存储的键值
     */
    private static final String JSON_ID = "id";
    private static final String JSON_TITLE = "title";
    private static final String JSON_SOLVED = "solved";
    private static final String JSON_DATE = "date";
    private static final String JSON_PHOTO = "photo";


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

        // 对图片的数据进行判断和反序列化恢复
        if (jsonObj.has(JSON_PHOTO)){
            mPhoto = new PhotoBean(jsonObj.getJSONObject(JSON_PHOTO));
        }

    }

    /**
     *  将实例对象创建为转成JSONObject对象, 并返回
     */
    public JSONObject toJSON() throws JSONException{
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(JSON_ID, mId.toString());
        jsonObject.put(JSON_TITLE, mTitle);
        jsonObject.put(JSON_SOLVED, mSolved);
        jsonObject.put(JSON_DATE, getRawDate());
        if (mPhoto != null) jsonObject.put(JSON_PHOTO, mPhoto.toJSON());
        return jsonObject;

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

    public PhotoBean getmPhoto() {
        return mPhoto;
    }

    public void setmPhoto(PhotoBean mPhoto) {
        this.mPhoto = mPhoto;
    }

    @Override
    public String toString() {
        return mTitle;
    }



}
