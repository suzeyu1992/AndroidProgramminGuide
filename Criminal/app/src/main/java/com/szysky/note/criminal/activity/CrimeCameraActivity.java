package com.szysky.note.criminal.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Window;
import android.view.WindowManager;

import com.szysky.note.criminal.SingleFragmentActivity;
import com.szysky.note.criminal.fragment.CrimeCameraFragment;

/**
 * Author :  suzeyu
 * Time   :  2016-08-08  下午6:49
 * Blog   :  http://szysky.com
 * GitHub :  https://github.com/suzeyu1992
 * ClassDescription : 进行照相记录陋习
 */

public class CrimeCameraActivity extends SingleFragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Hide the Window title
        getSupportActionBar().hide();

        // 去掉最上层的状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
    }

    @Override
    protected Fragment createFragment() {
        return new CrimeCameraFragment();
    }
}
