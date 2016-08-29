package com.szysky.note.criminal;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.szysky.note.criminal.fragment.CrimeFragment;

/**
 * Author : suzeyu
 * Time   : 16/8/29  下午5:29
 * Blog   : http://szysky.com
 * GitHub : https://github.com/suzeyu1992
 *
 * ClassDescription : Launcher页面, 具体处理逻辑交给其Fragment
 */


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 获得Fragment 的管理者对象
        FragmentManager fm = getSupportFragmentManager();
        // 先查找管理者中是否已经存在, 如果不存在就new一个
        Fragment fragment = fm.findFragmentById(R.id.activity_main);

        if (fragment == null) {
            fragment = new CrimeFragment();
            fm.beginTransaction()
                    .add(R.id.activity_main, fragment)
                    .commit();
        }


    }
}
