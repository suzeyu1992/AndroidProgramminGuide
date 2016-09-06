package com.szysky.note.runtracker.activity;

import android.support.v4.app.Fragment;

import com.szysky.note.runtracker.fragment.RunFragment;

public class RunActivity extends SingleFragmentActivity {


    public  static final String EXTRA_RUN_ID = "open_run_activity";

    @Override
    protected Fragment createFragment() {
        //  这里分情形使用不同的构造方法
        long runId = getIntent().getLongExtra(EXTRA_RUN_ID, -1);

        if (runId == -1){
            return new RunFragment();

        }else{
            return RunFragment.newInstance(runId);
        }


    }


}
