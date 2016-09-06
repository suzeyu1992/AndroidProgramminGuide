package com.szysky.note.runtracker;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.szysky.note.runtracker.db.Run;
import com.szysky.note.runtracker.db.RunDatabaseHelper;

/**
 * Author :  suzeyu
 * Time   :  2016-09-06  上午11:07
 * Blog   :  http://szysky.com
 * GitHub :  https://github.com/suzeyu1992
 *
 * ClassDescription :   为管理与LocationManager的通讯, 所以创建一个单例管理类专门管理
 */

public class RunManager {
    private static final String TAG =   RunManager.class.getSimpleName() ;

    public static final String ACTION_LOCATION = "com.szysky.note.runtracker.ACTION_LOCATION";
    /**
     *  本地sp文件名称
     */
    private static final String PREFS_FILE = "runs";
    private static final String PREF_CURRENT_RUN_ID = "RunManager.currentRunId";
    private static volatile RunManager sRunManager;

    /**
     *  应用进程的上下文对象
     */
    private final Context mAppContext;
    private final LocationManager mLocationManager;
    private final RunDatabaseHelper mHelper;
    private final SharedPreferences mPrefs;
    private long mCurrentRunId;

    private RunManager(Context context) {
        mAppContext = context.getApplicationContext();
        mLocationManager = (LocationManager) mAppContext.getSystemService(Context.LOCATION_SERVICE);

        //  初始化数据库
        mHelper = new RunDatabaseHelper(mAppContext);
        mPrefs = mAppContext.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);
        mCurrentRunId = mPrefs.getLong(PREF_CURRENT_RUN_ID, -1);
    }

    public static RunManager getInstance(Context context) {
        if (sRunManager == null) {
            synchronized (RunManager.class) {
                if (sRunManager == null) {
                    sRunManager = new RunManager(context);
                }
            }
        }

        return sRunManager;
    }

    /**
     *  用于创建广播, 使用一个定制操作名来识别应用内的事件.
     *  @param shouldCreate 标识参数, 告诉PendingIntent.getBroadcast()方法是否应该在系统内创建新的PendingIntent
     */
    private PendingIntent getLocationPendingIntent(boolean shouldCreate) {
        Intent broadcast = new Intent(ACTION_LOCATION);
        int flags = shouldCreate ? 0 : PendingIntent.FLAG_NO_CREATE;
        return PendingIntent.getBroadcast(mAppContext, 0, broadcast, flags);
    }

    /**
     *  方法功能: 开启地理位置
     *  要求LocationManager通过GPS定位装置提供实时的定位数据更新.
     *  其中的requestLocationUpdates()方法中参数2和3, 表示最小等待时间(毫秒)以及最短移动距离(米)
     *  可用于发送下一次定位数据更新要移动的距离和要等待的时间.
     */
    public void startLocationUpdates() {
        String gpsProvider = LocationManager.GPS_PROVIDER;

        //  获得上一次定位的信息
        Location lastKnow = mLocationManager.getLastKnownLocation(gpsProvider);
        if (lastKnow != null){
            //  reset the time to now
            lastKnow.setTime(System.currentTimeMillis());
            broadcastLocation(lastKnow);
        }

        //  start updates from the location manager
        PendingIntent pi = getLocationPendingIntent(true);
        if (ActivityCompat.checkSelfPermission(
                mAppContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mAppContext, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLocationManager.requestLocationUpdates(gpsProvider, 0, 1, pi);
    }

    private void broadcastLocation(Location lastKnow) {
        Intent intent = new Intent(ACTION_LOCATION);
        intent.putExtra(LocationManager.KEY_LOCATION_CHANGED, lastKnow);
        mAppContext.sendBroadcast(intent);
    }

    public void stopLocationUpdates(){
        PendingIntent pi = getLocationPendingIntent(false);

        if (pi != null){
            mLocationManager.removeUpdates(pi);
            pi.cancel();
        }
    }

    /**
     *  通过getLocationPendingIntent()方法的返回值, 可以确定PendingIntent是否已在操作系统中登记
     */
    public boolean isTrackingRun(){
        return getLocationPendingIntent(false)!= null;
    }


    public Run startNewRun(){
        // Insert a run into the db
        Run run = insertRun();

        // Start tracking the run
        startTrackingRun(run);
        return run;
    }

    private void startTrackingRun(Run run) {
        mCurrentRunId = run.getId();

        mPrefs.edit().putLong(PREF_CURRENT_RUN_ID, mCurrentRunId).commit();

        startLocationUpdates();

    }

    public void stopRun(){
        stopLocationUpdates();
        mCurrentRunId = -1;
        mPrefs.edit().remove(PREF_CURRENT_RUN_ID).commit();
    }

    private Run insertRun(){
        Run run = new Run();
        run.setId(mHelper.insertRun(run));
        return run;
    }

    public void insertLocation(Location loc){
        if (mCurrentRunId != -1){
            mHelper.insertLocation(mCurrentRunId, loc);
        }else{
            Log.i(TAG, "location received with no tracking run ; ignoring");
        }
    }

    public RunDatabaseHelper.RunCursor queryRuns(){
        return mHelper.queryRuns();
    }




}
