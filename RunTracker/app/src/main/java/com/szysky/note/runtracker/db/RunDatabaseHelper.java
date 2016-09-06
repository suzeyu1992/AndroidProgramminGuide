package com.szysky.note.runtracker.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;

/**
 * Author :  suzeyu
 * Time   :  2016-09-06  下午4:34
 * Blog   :  http://szysky.com
 * GitHub :  https://github.com/suzeyu1992
 *
 * ClassDescription :   为应用的其他部分提供统一的API, 用于数据的插入,查询, 以及其他一些数据管理操作.
 *                      并且本类会提供各种方法RunManager调用. 实现其定义的大部分API
 */

public class RunDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "runs.sqlite";
    private static final int VERSION = 1;

    /**
     *  为run表进行需要的名称声明
     */
    private static final String TABLE_RUN = "run";
    private static final String COLUMN_RUN_START_DATE = "start_date";


    private static final String TABLE_LOCATION = "location";
    private static final String COLUMN_LOCATION_LATITUDE = "latitude";
    private static final String COLUMN_LOCATION_LONGITUDE = "longitude";
    private static final String COLUMN_LOCATION_ALTITUDE = "altitude";
    private static final String COLUMN_LOCATION_TIMESTAMP = "timestamp";
    private static final String COLUMN_LOCATION_PROVIDE = "provider";
    private static final String COLUMN_LOCATION_RUN_ID = "run_id";

    public RunDatabaseHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // create table
        db.execSQL(" create table run ( _id integer primary key autoincrement, start_date integer)");

        db.execSQL(" create table location ( timestamp integer , latitude real, longitude real, altitude real, " +
                "provider varchar(100), run_id integer references run(_id))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     *  用以在run数据表中插入一条新纪录并返回其ID
     * @param run   旅行记录实例,主要为了获取其时间
     * @return  返回新插入的记录的ID
     */
    public long insertRun(Run run){
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_RUN_START_DATE, run.getStartDate().getTime());
        return getWritableDatabase().insert(TABLE_RUN, null, cv);
    }

    /**
     *  用在location表中插入一行记录
     * @param runId 所属的旅程表的id
     * @param location  要进行数据库存储的column字段的数据
     * @return  返回一个插入的id
     */
    public long insertLocation(long runId, Location location){

        ContentValues cv = new ContentValues();
        cv.put(COLUMN_LOCATION_LATITUDE, location.getLatitude());   //添加维度
        cv.put(COLUMN_LOCATION_LONGITUDE, location.getLongitude()); //添加经度
        cv.put(COLUMN_LOCATION_ALTITUDE, location.getAltitude());   //添加高度
        cv.put(COLUMN_LOCATION_TIMESTAMP, location.getTime());
        cv.put(COLUMN_LOCATION_PROVIDE, location.getProvider());
        cv.put(COLUMN_LOCATION_RUN_ID, runId);

        return getWritableDatabase().insert(TABLE_LOCATION, null, cv);



    }
}
