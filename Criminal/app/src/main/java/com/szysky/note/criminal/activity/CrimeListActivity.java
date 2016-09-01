package com.szysky.note.criminal.activity;

import android.support.v4.app.Fragment;

import com.szysky.note.criminal.R;
import com.szysky.note.criminal.SingleFragmentActivity;
import com.szysky.note.criminal.fragment.CrimeListFragment;

/**
 * Author :  suzeyu
 * Time   :  2016-08-08  上午9:48
 * Blog   :  http://szysky.com
 * GitHub :  https://github.com/suzeyu1992
 * ClassDescription :   展示所有陋习的列表
 */

public class CrimeListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }
}
