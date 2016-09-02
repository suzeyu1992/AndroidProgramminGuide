package com.szysky.note.remotecontrol;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class RemoteControlActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new RemoteControlFragment();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 隐藏操作栏
        getSupportActionBar().hide();

        super.onCreate(savedInstanceState);
    }
}
