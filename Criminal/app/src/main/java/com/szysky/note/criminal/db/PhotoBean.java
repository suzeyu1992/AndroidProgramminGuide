package com.szysky.note.criminal.db;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Author :  suzeyu
 * Time   :  2016-09-09  下午3:27
 * Blog   :  http://szysky.com
 * GitHub :  https://github.com/suzeyu1992
 * ClassDescription :
 */

public class PhotoBean {

    private static final String JSON_FINENAME = "file_name";
    private String mFilename;


    // 此构造是根据给定文件名来创建
    public PhotoBean(String filename){
        mFilename = filename;
    }

    // 此构造是从本地反序列化的使用直接调用
    public PhotoBean(JSONObject json) throws JSONException {
        mFilename = json.getString(JSON_FINENAME);
    }

    public JSONObject toJSON() throws JSONException{
        JSONObject json = new JSONObject();
        json.put(JSON_FINENAME, mFilename);
        return json;
    }

    public String getmFilename(){
        return mFilename;
    }


}
