package com.szysky.note.newlauncher;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Author :  suzeyu
 * Time   :  2016-09-09  上午10:32
 * Blog   :  http://szysky.com
 * GitHub :  https://github.com/suzeyu1992
 * ClassDescription :
 */

public class NerdLauncherFragment extends ListFragment {

    private static final String TAG = NerdLauncherFragment.class.getSimpleName();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //  创建一个隐式Intent, 从PackageManager中获取匹配intent的activity列表
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        PackageManager pm = getActivity().getPackageManager();
        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(intent, 0);

        Log.i(TAG, "onCreate: 可以打开的数量:"+resolveInfos.size());

        //  进行排序
        Collections.sort(resolveInfos, new Comparator<ResolveInfo>() {
            @Override
            public int compare(ResolveInfo o1, ResolveInfo o2) {
                PackageManager pm = getActivity().getPackageManager();
                //  按照名称进行排序
                int result = String.CASE_INSENSITIVE_ORDER.compare(o1.loadLabel(pm).toString(), o2.loadLabel(pm).toString());
                return result;
            }
        });

        // 为集合创建一个适配器
        ArrayAdapter<ResolveInfo> myArrayAdapter = new ArrayAdapter<ResolveInfo>(getActivity(), android.R.layout.simple_list_item_1, resolveInfos) {
            @NonNull
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                PackageManager pm = getActivity().getPackageManager();
                TextView root = (TextView) super.getView(position, convertView, parent);

                ResolveInfo item = getItem(position);
                root.setText(item.loadLabel(pm));


                return root;
            }
        };


        //  给ListFragment中的内置ListView添加适配器
        setListAdapter(myArrayAdapter);


    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        ResolveInfo resolveInfo = (ResolveInfo) l.getAdapter().getItem(position);

        //  准备获取要打开activity的包名 类名等信息
        ActivityInfo activityInfo = resolveInfo.activityInfo;

        if (activityInfo == null) return;

        // 创建显示Intent来打开Activity
        //  先指定一个action
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setClassName(activityInfo.applicationInfo.packageName, activityInfo.name);
        //  添加新任务标识给intent
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(intent);


    }
}
