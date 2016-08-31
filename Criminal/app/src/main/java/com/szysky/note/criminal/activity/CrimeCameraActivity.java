package com.szysky.note.criminal.activity;

import android.support.v4.app.Fragment;

import com.szysky.note.criminal.SingleFragmentActivity;
import com.szysky.note.criminal.fragment.CrimeCameraFragment;

/**
 * Author :  suzeyu
 * Time   :  2016-08-08  下午6:49
 * Blog   :  http://szysky.com
 * GitHub :  https://github.com/suzeyu1992
 * ClassDescription :
 */

public class CrimeCameraActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new CrimeCameraFragment();
    }
}
