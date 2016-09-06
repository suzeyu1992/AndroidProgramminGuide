package com.szysky.note.runtracker.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;

import java.util.Date;

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


    private static final String COLUMN_RUN_ID = "_id";

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

    /**
     * 方法内部首先执行SQL查询语句, 提供未经处理的cursor给新的RunCursor, 然后返回给queryRuns()方法的调用者.
     */
    public RunCursor queryRuns(){
        Cursor query = getReadableDatabase().query(TABLE_RUN, null, null, null, null, null, COLUMN_RUN_START_DATE + " asc");
        return new RunCursor(query);
    }



    /**
     * a convenience class to wrap a cursor that return rows from the "run" table.
     * 主要负责将run表中的各个记录转化为Run实例, 并按要求对结果进行组织排序
     */
    public static class RunCursor extends CursorWrapper{
        public RunCursor(Cursor cursor){
            super(cursor);
        }

        /**
         *  基于当前查询记录的列值创建Run实例并为其赋值.
         *  对于本类的使用者可遍历结果集里面的行数, 并对每一行调用getRun()方法,得到一个对象,而不是数据
         * @return
         */
        public Run getRun(){
            //  确认cursor是否出现越界
            if (isBeforeFirst() || isAfterLast()){
                return null;
            }

            Run run = new Run();
            long runId = getLong(getColumnIndex(COLUMN_RUN_ID));
            run.setId(runId);
            long startDate = getLong(getColumnIndex(COLUMN_RUN_START_DATE));
            run.setStartDate(new Date(startDate));
            return run;

        }
    }

    public RunCursor queryRun(long id){
        //  通过where子句. 限制查询只能返回一条记录, 然后封装到RunCursor中并返回
        Cursor wrapped = getReadableDatabase().query(TABLE_RUN,
                null,   // All columns
                COLUMN_RUN_ID + " = ?",           // look for a run ID
                new String[]{String.valueOf(id)}, // with the value
                null,   // group by
                null,   // having
                null,   // order by
                "1"     //  limit 1 row
        );
        return new RunCursor(wrapped);
    }

    /**
     *  与RunCursor使用目的相同, 但这个类用于封装location数据表中返回记录的cursor.
     *  并将记录各字段转换为Location对象属性
     */
    public static class LocationCursor extends CursorWrapper{

        /**
         * Creates a cursor wrapper.
         *
         * @param cursor The underlying cursor to wrap.
         */
        public LocationCursor(Cursor cursor) {
            super(cursor);
        }

        public Location getLocation(){
            if (isBeforeFirst() || isAfterLast()){
                return null;
            }

            // First get the provider out so you can use the constructor
            String provider = getString(getColumnIndex(COLUMN_LOCATION_PROVIDE));
            Location loc = new Location(provider);

            // Populate the remaining properties
            loc.setLongitude(getDouble(getColumnIndex(COLUMN_LOCATION_LONGITUDE)));
            loc.setLatitude(getDouble(getColumnIndex(COLUMN_LOCATION_LATITUDE)));
            loc.setAltitude(getDouble(getColumnIndex(COLUMN_LOCATION_ALTITUDE)));
            loc.setTime(getLong(getColumnIndex(COLUMN_LOCATION_TIMESTAMP)));

            return loc;
        }

    }

    public LocationCursor queryLastLocationForRun(long runId){
        Cursor wrapped = getReadableDatabase().query(TABLE_LOCATION,
                null,
                COLUMN_LOCATION_RUN_ID + "= ?",
                new String[]{String.valueOf(runId)},
                null,
                null,
                COLUMN_LOCATION_ALTITUDE + " desc",  // order by latest first
                "1"
        );

        // 把查询的结果集转成对应的对象, 方便后续操作
        return new LocationCursor(wrapped);


    }

}
