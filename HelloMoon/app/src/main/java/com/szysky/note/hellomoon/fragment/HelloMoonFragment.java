package com.szysky.note.hellomoon.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.szysky.note.hellomoon.R;
import com.szysky.note.hellomoon.audio.AudioPlayer;

/**
 * Author :  suzeyu
 * Time   :  2016-09-07  上午9:55
 * Blog   :  http://szysky.com
 * GitHub :  https://github.com/suzeyu1992
 *
 * ClassDescription :   首页面的Fragment控制层
 */

public class HelloMoonFragment extends Fragment implements View.OnClickListener {

    private Button mPlayButton;
    private Button mStopButton;
    private AudioPlayer mPlayer = new AudioPlayer();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 使用此方法可确保MediaPlayer实例一直存在, 主要是为了应对设备配置的变化
        // 设置为true之后fragment不会随activity销毁而销毁. 相反它会保留等待被再次被调用
        setRetainInstance(true);

        /**
         * fragment如果进入了保留状态, 那么通常需要满足两个条件
         * 1. 已经给Fragment设定了setRetainInstance(true)
         * 2. 因设备配置改变, 托管的activity正在被销毁
         *
         * * Fragment处于保留状态的事件非常短暂, 即fragment脱离旧的activity到重新附加给创建的新的activity之间的一段时间
         */
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_hello_moon, container, false);

        mPlayButton = (Button) rootView.findViewById(R.id.btn_play);
        mStopButton = (Button) rootView.findViewById(R.id.btn_stop);

        // 设置监听
        mPlayButton.setOnClickListener(this);
        mStopButton.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPlayer.stop();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_play:
                mPlayer.play(getActivity().getApplicationContext());
                break;

            case R.id.btn_stop:
                mPlayer.stop();
                break;
        }
    }
}
