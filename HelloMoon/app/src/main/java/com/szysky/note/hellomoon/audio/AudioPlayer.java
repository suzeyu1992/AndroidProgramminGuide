package com.szysky.note.hellomoon.audio;

import android.content.Context;
import android.media.MediaPlayer;

import com.szysky.note.hellomoon.R;

/**
 * Author :  suzeyu
 * Time   :  2016-09-07  上午10:17
 * Blog   :  http://szysky.com
 * GitHub :  https://github.com/suzeyu1992
 *
 * ClassDescription : 主要用来管理MediaPlayer的实例, 和对该实例的播放和停止等功能
 */

public class AudioPlayer {

    private MediaPlayer mPlayer;

    public void stop(){
        if (mPlayer != null){
            // 销毁, 否则MediaPlayer将一直占用着音频解码硬件及其他系统资源
            mPlayer.release();
            mPlayer = null;
        }
    }

    public void play(Context context){
        // 防止过多的创建MediaPlayer实例, 第一步先销毁已经存在的
        stop();

        //  设置要播放的音频文件, 如果音频来自其他渠道如网络或者URI, 则使用其他的create(...)函数
        mPlayer = MediaPlayer.create(context, R.raw.ss_small);
        mPlayer.start();

        // 防止过多的创建MediaPlayer实例, 第二步设置监听, 存活时间为音频的时长
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                // 音频播放完成
                stop();
            }
        });

    }



}
