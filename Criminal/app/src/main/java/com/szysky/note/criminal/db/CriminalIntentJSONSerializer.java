package com.szysky.note.criminal.db;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

/**
 * Author :  suzeyu
 * Time   :  2016-08-08  上午9:09
 * Blog   :  http://szysky.com
 * GitHub :  https://github.com/suzeyu1992
 * <p>
 * ClassDescription :   负责读取Crime数组列表中的数据, 并进行数据格式转换,然后写入JSON文件.
 */

public class CriminalIntentJSONSerializer {

    private static final String TAG = CriminalIntentJSONSerializer.class.getSimpleName();
    private Context mContext;

    /**
     * 要保存的文件路径
     */
    private String mFilePath;

    public CriminalIntentJSONSerializer(Context context, String filePath) {
        mContext = context;
        mFilePath = filePath;
    }

    /**
     * 把实参接收到的Crime集合中的所有对象转换成JSON格式的, 并存入本地文件中.
     *
     * @param crimes 需要进行本地存储的CrimeBean数据集合
     * @throws JSONException,IOException
     */
    public void saveCrimes(ArrayList<CrimeBean> crimes) throws JSONException, IOException {
        JSONArray jsonArray = new JSONArray();
        for (CrimeBean entry : crimes) {
            jsonArray.put(entry.toJSON());
        }
        Log.i(TAG, "@@-> 准备进行存储的数据为:" + jsonArray.toString());
        //  Write the file to disk
        Writer writer = null;

        try {
            //  打开一个字节流
            FileOutputStream out = mContext.openFileOutput(mFilePath, Context.MODE_PRIVATE);

            //  创建一个转换流, 把字节流转换成字符流, 方便后面直接传入字符操作
            writer = new OutputStreamWriter(out);

            // 向字符流中写入 JsonArray 的json字符串形式数据
            writer.write(jsonArray.toString());


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }


    /**
     * 提供 从本地文件中获取内容并转换成Arraylist集合的形式返回
     */
    public ArrayList<CrimeBean> loadCrimes() throws IOException, JSONException {
        ArrayList<CrimeBean> crimes = new ArrayList<>();
        BufferedReader reader = null;

        try {

            // 打开并读取文件到StringBuilder
            FileInputStream in = mContext.openFileInput(mFilePath);
            reader = new BufferedReader(new InputStreamReader(in));

            // 创建一个缓冲流用来读取内存中读到的数据
            StringBuilder sb = new StringBuilder();
            String line = null;

            // 开始循环读取输入流 并添加到缓冲流中
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            // 使用JSONTokener 来解析JSON字符串
            JSONArray jsonArray = (JSONArray) new JSONTokener(sb.toString()).nextValue();

            // 把jsonArray数组的每个元素放入到ArrayList中去
            for (int i = 0; i < jsonArray.length(); i++) {
                crimes.add(new CrimeBean(jsonArray.getJSONObject(i)));
            }
        }finally {
            if (reader != null) {
                reader.close();
            }
        }

        return crimes;
    }
}
