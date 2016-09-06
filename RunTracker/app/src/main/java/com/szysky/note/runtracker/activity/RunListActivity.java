package com.szysky.note.runtracker.activity;

import android.support.v4.app.Fragment;

import com.szysky.note.runtracker.fragment.RunListFragment;

/**
 * Author :  suzeyu
 * Time   :  2016-09-06  下午6:14
 * Blog   :  http://szysky.com
 * GitHub :  https://github.com/suzeyu1992
 *
 * ClassDescription :   显示旅程列表
 */

public class RunListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new RunListFragment();
    }
}
