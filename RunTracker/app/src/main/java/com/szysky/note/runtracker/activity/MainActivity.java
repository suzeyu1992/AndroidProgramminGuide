package com.szysky.note.runtracker.activity;

import android.support.v4.app.Fragment;

import com.szysky.note.runtracker.fragment.RunFragment;

public class MainActivity extends SingleFragmentActivity {


    @Override
    protected Fragment createFragment() {
        return new RunFragment();
    }


}
