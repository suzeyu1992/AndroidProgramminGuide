package com.szysky.note.criminal.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.szysky.note.criminal.R;
import com.szysky.note.criminal.activity.CrimePagerActivity;
import com.szysky.note.criminal.db.CrimeBean;
import com.szysky.note.criminal.db.CrimeLab;

import java.sql.Time;
import java.util.ArrayList;

import static com.szysky.note.criminal.R.string.sub_description;

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
        // 开启自定义导航栏的组件 告诉FragmentManager需要接受选项菜单的方法
        setHasOptionsMenu(true);

        // 获得所有集合
        mCrimes = CrimeLab.getInstance(getActivity().getApplicationContext()).getCrimes();

        // 创建 ArrayAdapter 的实例
        // 创建系统预定义的实例
        // ArrayAdapter<CrimeBean> myAdapter = new ArrayAdapter<>(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1, mCrimes);
        //  --使用自定义的列表项
        MyCrimeAdapter myCrimeAdapter = new MyCrimeAdapter(mCrimes);


        //  是ListFragment类提供的一个便利方法, 可以管理内置的listView设置adapter
        setListAdapter(myCrimeAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();

        int count = getListAdapter().getCount();
        if (count>0){
            // 当界面发送切换的时候进行 adapter的刷新
            ((MyCrimeAdapter)getListAdapter()).notifyDataSetChanged();
        }else{
        }
    }

    /**
     *  覆盖ListFragment类的此方法, 可以方便的响应用户对列表的点击事件
     */
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        //  通过adapter 获得对应角标的陋习对对象
        CrimeBean item = (CrimeBean) getListAdapter().getItem(position);
        Log.d(TAG, "onListItemClick: 点击的内容"+item.getTitle());

        Intent intent = new Intent(getActivity().getApplicationContext(), CrimePagerActivity.class);
        // 传入陋习的id
        intent.putExtra(CrimeFragment.EXTRA_CRIME_ID, item.getId());
        startActivity(intent);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_item_new_crime:
                // 创建一个陋习实例, 并保存到数据层中
                CrimeBean crimeBean = new CrimeBean();
                CrimeLab.getInstance(getActivity().getApplicationContext()).addCrime(crimeBean);

                // 创建Intent, 并传入传递的实例id, 打开一个CrimePagerActivity
                Intent intent = new Intent();
                intent.setClass(getActivity().getApplicationContext(), CrimePagerActivity.class);
                intent.putExtra(CrimeFragment.EXTRA_CRIME_ID, crimeBean.getId());
                startActivityForResult(intent, 0);

                return true;

            case R.id.menu_item_show_subtitle:
                //  设置操作栏的子标题
                ActionBar supportActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();

                if (supportActionBar.getSubtitle() == null){
                    supportActionBar.setSubtitle(sub_description);
                    item.setTitle(R.string.hide_subtitle);
                }else{
                    supportActionBar.setSubtitle(null);
                    item.setTitle(R.string.show_subtitle);
                }

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     *  定制list的列表项, 继承adapter 重写getView方法
     */
    private  class MyCrimeAdapter extends ArrayAdapter<CrimeBean>{

        public MyCrimeAdapter(ArrayList<CrimeBean> crimes) {
            //  由于这里不打算使用预定义的布局, 所以参数2传递了资源id为0,
            super(getActivity().getApplicationContext(), 0, crimes);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder ;
            if (convertView == null){
                convertView = getActivity().getLayoutInflater().inflate(R.layout.item_list_crime, null);
                holder = new ViewHolder();
                holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
                holder.tv_title  = (TextView) convertView.findViewById(R.id.tv_title);
                holder.cb_solved = (CheckBox) convertView.findViewById(R.id.cb_solved);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }

            //  获得item对应的javabean对象
            CrimeBean item = getItem(position);

            // 开始赋值数据
            holder.tv_time.setText(item.getDate());
            holder.tv_title.setText(item.getTitle());
            holder.cb_solved.setChecked(item.isSolved());

            return convertView;
        }

        private class ViewHolder{
            private TextView tv_title;
            private TextView tv_time;
            private CheckBox cb_solved;


        }
    }
}
