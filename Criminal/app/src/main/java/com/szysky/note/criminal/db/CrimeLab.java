package com.szysky.note.criminal.db;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Author :  suzeyu
 * Time   :  2016-08-08  上午9:13
 * Blog   :  http://szysky.com
 * GitHub :  https://github.com/suzeyu1992
 *
 * ClassDescription :  数据模型中的控制类.
 *                      一个保存全部 Crime 实例的集合. 此类为单例模式
 */

public class CrimeLab {
    private static final String TAG = CrimeLab.class.getSimpleName();
    /**
     * 本类实例对象
     */
    private static volatile CrimeLab mInstance ;
    private Context mContext;

    /**
     *  存储所有陋习的集合
     */
    private ArrayList<CrimeBean> mCrimes;

    /**
     *  本地文件存储的文件名称
     */
    private static final String FILENAME = "crimes.json";

    /**
     *  本地存储的操作类实例
     */
    private final CriminalIntentJSONSerializer mSerializer;


    private CrimeLab(Context context){
        mContext = context.getApplicationContext();

        // 创建进行本地文件存储的操作类
        mSerializer = new CriminalIntentJSONSerializer(mContext, FILENAME);

        // 加载本地文件并转成对象集合 赋值到mCrimes
        try {
            mCrimes = mSerializer.loadCrimes();
        } catch (Exception e) {
            mCrimes = new ArrayList<>();
            Log.e(TAG, "@@-> 加载本地数据失败!!");
            e.printStackTrace();
        }

    };

    /**
     * 提供本类实例的静态方法
     */
    public static CrimeLab getInstance(Context context){
        if (mInstance == null){
            synchronized (CrimeLab.class){
                if (mInstance == null){
                    mInstance = new CrimeLab(context);
                }
            }
        }
        return mInstance;
    }


    public ArrayList<CrimeBean> getCrimes() {
        return mCrimes;
    }

    public CrimeBean getCrime(UUID uuid){
        for (CrimeBean temp : mCrimes) {
            if (temp.getId().equals(uuid)){
                return temp;
            }
        }
        return null;
    }

    public void addCrime(CrimeBean cri){
        mCrimes.add(cri);
    }

    /**
     * 提供一个本地化的方法, 对已经存在的实例数据集合进行本地化
     *
     * @return  存储本地化结果的标识符
     */
    public boolean saveCrimes(){
        try {
            //  进行存储
            mSerializer.saveCrimes(mCrimes);
            Log.i(TAG, "@@-> crimes save to file is ok!" );
            return true;
        } catch (Exception e) {
            Log.e(TAG, "@@-> Error saving crimes: "+e );
            return false;
        }
    }
}
