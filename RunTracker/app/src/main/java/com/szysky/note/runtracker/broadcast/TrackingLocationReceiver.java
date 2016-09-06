package com.szysky.note.runtracker.broadcast;

import android.content.Context;
import android.location.Location;

import com.szysky.note.runtracker.RunManager;

/**
 * Author :  suzeyu
 * Time   :  2016-09-06  下午5:46
 * Blog   :  http://szysky.com
 * GitHub :  https://github.com/suzeyu1992
 *
 * ClassDescription :   用来调用insertLocation(). 创建一个独立的广播接收者, 并且在注册清单中声明
 *                      不关心应用使用正在运行.
 */

public class TrackingLocationReceiver extends LocationReceiver {

    @Override
    protected void onLocationReceived(Context context, Location loc) {
        RunManager.getInstance(context).insertLocation(loc);
    }
}
