package com.szysky.note.runtracker.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

/**
 * Author :  suzeyu
 * Time   :  2016-09-06  上午11:52
 * Blog   :  http://szysky.com
 * GitHub :  https://github.com/suzeyu1992
 *
 * ClassDescription :   接收定位数据更新的BroadCast
 */

public class LocationReceiver extends BroadcastReceiver {
    private static final String TAG = LocationReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        //  Key_location_changed可以指定一个表示最新更新的Location实例
        Location loc = intent.getParcelableExtra(LocationManager.KEY_LOCATION_CHANGED);
        if (loc != null) {
            onLocationReceived(context, loc);
            return ;
        }

        // If you get here, something else has happened
        if (intent.hasExtra(LocationManager.KEY_PROVIDER_ENABLED)){
            boolean enabled = intent.getBooleanExtra(LocationManager.KEY_PROVIDER_ENABLED, false);
            onProviderEnabledChanged(enabled);
        }
    }

    protected void onLocationReceived(Context context, Location loc) {
        Log.d(TAG, "Got location from "+ loc.getProvider() + " : " +loc.getLatitude()+", "+loc.getLongitude() );
    }

    protected void onProviderEnabledChanged(boolean enabled) {
        Log.d(TAG, "Provider "+ (enabled ? "enabled":"disabled") );
    }

}
