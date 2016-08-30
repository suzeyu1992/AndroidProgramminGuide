package com.szysky.note.criminal.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.szysky.note.criminal.SingleFragmentActivity;
import com.szysky.note.criminal.fragment.CrimeFragment;

/**
 * Author : suzeyu
 * Time   : 16/8/29  下午5:29
 * Blog   : http://szysky.com
 * GitHub : https://github.com/suzeyu1992
 *
 * ClassDescription : 单个陋习操作的具体类
 */


public class CrimeActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new CrimeFragment();
    }
}
