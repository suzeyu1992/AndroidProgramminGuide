package com.szysky.note.draganddraw;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

/**
 * Author : suzeyu
 * Time   : 16/9/2  下午5:29
 * Blog   : http://szysky.com
 * GitHub : https://github.com/suzeyu1992
 *
 * ClassDescription : 通用的Activity创建抽象类
 */


public abstract class SingleFragmentActivity extends AppCompatActivity {

    protected abstract Fragment createFragment();

    // 对子类提供一个方法, 抛弃之前的硬编码设置布局
    protected int getLayoutResId(){
        return R.layout.activity_main;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        // 获得Fragment 的管理者对象
        FragmentManager fm = getSupportFragmentManager();
        // 先查找管理者中是否已经存在, 如果不存在就new一个
        Fragment fragment = fm.findFragmentById(R.id.activity_main);

        if (fragment == null) {
            fragment = createFragment();
            fm.beginTransaction()
                    .add(R.id.activity_main, fragment)
                    .commit();
        }


    }
}
