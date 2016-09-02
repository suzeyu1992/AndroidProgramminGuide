package com.szysky.note.criminal.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
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
    private AppCompatActivity mActivity;
    private MyCrimeAdapter myCrimeAdapter;

    /**
     *  设置接口回调
     */
    private Callbacks mCallbacks;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (AppCompatActivity) getActivity();
        mCallbacks = (Callbacks)getActivity();
    }

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
        myCrimeAdapter = new MyCrimeAdapter(mCrimes);


        //  是ListFragment类提供的一个便利方法, 可以管理内置的listView设置adapter
        setListAdapter(myCrimeAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);

        // 为上下文菜单登记一个视图
        ListView lv_main = (ListView) rootView.findViewById(android.R.id.list);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB){
            // 在低版本使用浮动上下文菜单
            registerForContextMenu(lv_main);
        }else{
            // 在11版本以上使用多选的菜单功能
            lv_main.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
            //  设置列表视图操作模式回调
            lv_main.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
                // 视图中的选项在选中或者撤销的时候会触发
                @Override
                public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
//                    getListView().setItemChecked(position, checked);
                    Log.e(TAG, "posi:"+position +"  checked:"+checked);
                }
                // 在ActionMode对象创建后调用, 也是实例化上下文菜单资源, 并显示在上下文操作栏上的任务完成的地方
                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    MenuInflater menuInflater = mode.getMenuInflater();
                    menuInflater.inflate(R.menu.crime_list_item_context, menu);
                    return true;
                }
                // 在上个回调之后调用, 以及当前上下文操作栏需要刷新显示新数据时调用
                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                // 在用户选中某个菜单项操作时调用. 是相应上下文菜单项操作的地方
                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    switch (item.getItemId()){
                        case R.id.menu_item_delete_crime:
                            for (int i = myCrimeAdapter.getCount()-1; i >= 0; i--) {
                                if (getListView().isItemChecked(i)){
                                    mCrimes.remove(myCrimeAdapter.getItem(i));
                                }
                            }
                            mode.finish();
                            myCrimeAdapter.notifyDataSetChanged();
                            return true;
                    }
                    return false;
                }

                // 在用户退出上下文操作模式或所选菜单项操作已被相应, 从而导致ActionMode对象将要销毁时调用.
                // 默认的实现会导致已选视图被反选, 也可以完成上下文操作模式下, 响应菜单项操作而引起的响应fragment刷新
                @Override
                public void onDestroyActionMode(ActionMode mode) {

                }
            });
        }

        return rootView;
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

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    /**
     *  覆盖ListFragment类的此方法, 可以方便的响应用户对列表的点击事件
     */
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        //  通过adapter 获得对应角标的陋习对对象
        CrimeBean item = (CrimeBean) getListAdapter().getItem(position);
        Log.d(TAG, "onListItemClick: 点击的内容"+item.getTitle());

//        Intent intent = new Intent(getActivity().getApplicationContext(), CrimePagerActivity.class);
//        // 传入陋习的id
//        intent.putExtra(CrimeFragment.EXTRA_CRIME_ID, item.getId());
//        startActivity(intent);

        mCallbacks.onCrimeSelected(item);
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
//                Intent intent = new Intent();
//                intent.setClass(getActivity().getApplicationContext(), CrimePagerActivity.class);
//                intent.putExtra(CrimeFragment.EXTRA_CRIME_ID, crimeBean.getId());
//                startActivityForResult(intent, 0);

                mCallbacks.onCrimeSelected(crimeBean);
                ((MyCrimeAdapter)getListAdapter()).notifyDataSetChanged();

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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        // 为操作栏创建浮动的上下文菜单
        mActivity.getMenuInflater().inflate(R.menu.crime_list_item_context, menu);
    }

    // 浮动上下文的点击监听回调
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()){
            //  显示删除的浮动上下文
            case R.id.menu_item_delete_crime:
                // 获得点击的具体listview的item位置, 并得到对象
                AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                int position = menuInfo.position;
                CrimeBean clickItemBean = ((MyCrimeAdapter) getListAdapter()).getItem(position);

                //
                mCrimes.remove(clickItemBean);
                ((MyCrimeAdapter) getListAdapter()).notifyDataSetChanged();
                return true;
            default:
                return super.onContextItemSelected(item);

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

    /**
     *  Required interface for hosting activities
     */
    public interface Callbacks{
        void onCrimeSelected(CrimeBean item);

        // 更新UI
        void onCrimeUI(CrimeBean crimeBean);
    }

    public void updateUI(){
        ((MyCrimeAdapter)getListAdapter()).notifyDataSetChanged();
    }
}
