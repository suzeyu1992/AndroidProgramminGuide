package com.szysky.note.criminal.db;

import android.content.Context;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Author :  suzeyu
 * Time   :  2016-08-08  上午9:13
 * Blog   :  http://szysky.com
 * GitHub :  https://github.com/suzeyu1992
 *
 * ClassDescription : 一个保存全部 Crime 实例的集合. 此类为单例模式
 */

public class CrimeLab {
    /**
     * 本类实例对象
     */
    private static volatile CrimeLab mInstance ;
    private Context mContext;

    /**
     *  存储所有陋习的集合
     */
    private ArrayList<CrimeBean> mCrimes;


    private CrimeLab(Context context){
        mContext = context.getApplicationContext();
        mCrimes = new ArrayList<>();


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
}
