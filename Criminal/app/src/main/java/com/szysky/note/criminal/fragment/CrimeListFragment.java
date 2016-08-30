package com.szysky.note.criminal.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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

    private static final String TAG = CrimeListFragment.class.getSimpleName();
    /**
     *  所有的陋习集合数据
     */
    private ArrayList<CrimeBean> mCrimes;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 获得所有集合
        mCrimes = CrimeLab.getInstance(getActivity().getApplicationContext()).getCrimes();

        // 创建 ArrayAdapter 的实例
        ArrayAdapter<CrimeBean> myAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, mCrimes);

        //  是ListFragment类提供的一个便利方法, 可以管理内置的listView设置adapter
        setListAdapter(myAdapter);
    }


    /**
     *  覆盖ListFragment类的此方法, 可以方便的响应用户对列表的点击事件
     */
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        //  通过adapter 获得对应角标的陋习对对象
        CrimeBean item = (CrimeBean) getListAdapter().getItem(position);
        Log.d(TAG, "onListItemClick: 点击的内容"+item.getTitle());
    }
}
