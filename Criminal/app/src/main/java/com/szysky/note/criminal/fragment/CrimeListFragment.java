package com.szysky.note.criminal.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;

import com.szysky.note.criminal.db.CrimeBean;
import com.szysky.note.criminal.db.CrimeLab;

import java.util.ArrayList;

/**
 * Author :  suzeyu
 * Time   :  2016-08-08  上午9:32
 * Blog   :  http://szysky.com
 * GitHub :  https://github.com/suzeyu1992
 *
 * ClassDescription :       展示所有的陋习记录
 */

public class CrimeListFragment extends ListFragment {

    /**
     *  所有的陋习集合数据
     */
    private ArrayList<CrimeBean> mCrimes;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 获得所有集合
        mCrimes = CrimeLab.getInstance(getActivity().getApplicationContext()).getCrimes();
    }
}
