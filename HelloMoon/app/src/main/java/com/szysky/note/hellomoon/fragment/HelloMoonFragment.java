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
