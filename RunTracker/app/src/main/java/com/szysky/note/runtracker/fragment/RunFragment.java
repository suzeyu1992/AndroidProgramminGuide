package com.szysky.note.runtracker.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.szysky.note.runtracker.R;
import com.szysky.note.runtracker.RunManager;
import com.szysky.note.runtracker.broadcast.LocationReceiver;
import com.szysky.note.runtracker.db.Run;

/**
 * Author :  suzeyu
 * Time   :  2016-09-06  上午10:14
 * Blog   :  http://szysky.com
 * GitHub :  https://github.com/suzeyu1992
 * ClassDescription :   启动页的管理Fragment
 */
public class RunFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_RUN_ID = "RUN_ID";
    private TextView mStartedTextView;
    private TextView mLatitudeTextView;
    private TextView mLongitudeTextView;
    private TextView mAltitudeTextView;
    private TextView mDurationTextView;
    private Button mStartButton;
    private Button mStopButton;
    private RunManager mRunManager;

    private Location mLastLocation;
    private Run mRun;

    private BroadcastReceiver mLocationReceiver = new LocationReceiver(){
        @Override
        protected void onLocationReceived(Context context, Location loc) {
            if (!mRunManager.isTrackingRun()){
                return ;
            }

            mLastLocation = loc;
            if (isVisible())    updateUI();
        }

        @Override
        protected void onProviderEnabledChanged(boolean enabled) {
            String toastText = enabled ? "开启" : "关闭";
            Toast.makeText(getActivity().getApplicationContext(), toastText, Toast.LENGTH_SHORT).show();


        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        //  获取地理位置的管理者的类
        mRunManager = RunManager.getInstance(getActivity());

        // Check for a Run ID as an argument, and find the run
        Bundle arguments = getArguments();
        if (arguments != null){
            long runId = arguments.getLong(ARG_RUN_ID, -1);
            if (runId != -1){
                mRun = mRunManager.getRun(runId);
                mLastLocation = mRunManager.getLastLocationForRun(runId);
            }
        }



    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_run, container, false);

        mStartedTextView = (TextView) rootView.findViewById(R.id.tv_row_start);
        mLatitudeTextView = (TextView) rootView.findViewById(R.id.tv_row_latitude);
        mLongitudeTextView = (TextView) rootView.findViewById(R.id.tv_row_longitude);
        mAltitudeTextView = (TextView) rootView.findViewById(R.id.tv_row_altitude);
        mDurationTextView = (TextView) rootView.findViewById(R.id.tv_row_duration);

        mStartButton= (Button) rootView.findViewById(R.id.btn_start);
        mStopButton= (Button) rootView.findViewById(R.id.btn_stop);
        mStartButton.setOnClickListener(this);
        mStopButton.setOnClickListener(this);


        updateUI();


        return  rootView;


    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().registerReceiver(mLocationReceiver, new IntentFilter(RunManager.ACTION_LOCATION));
    }

    @Override
    public void onStop() {
        getActivity().unregisterReceiver(mLocationReceiver);
        super.onStop();
    }


    public static RunFragment newInstance(long runId){
        Bundle bundle = new Bundle();
        bundle.putLong(ARG_RUN_ID, runId);
        RunFragment runFragment = new RunFragment();
        runFragment.setArguments(bundle);
        return runFragment;
    }

    private void updateUI(){
        //  获得目前获取位置的广播的开启状态
        boolean trackingRunState = mRunManager.isTrackingRun();

        if (mRun != null){
            mStartedTextView.setText(mRun.getStartDate().toString());
        }

        int durationSeconds = 0;

        if (mRun != null && mLastLocation != null){
            durationSeconds = mRun.getDurationSeconds(mLastLocation.getTime());
            mLatitudeTextView.setText(Double.toString(mLastLocation.getLatitude()));
            mLongitudeTextView.setText(Double.toString(mLastLocation.getLongitude()));
            mAltitudeTextView.setText(Double.toString(mLastLocation.getAltitude()));
        }

        mDurationTextView.setText(Run.formatDuration(durationSeconds));

        //  根据状态, 设置开启和关闭的按钮在同一个时间点只会有一个生效
        mStartButton.setEnabled(!trackingRunState);
        mStopButton.setEnabled(trackingRunState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_start:
                if (mRun == null){
                    mRun = mRunManager.startNewRun();
                }else{
                    mRunManager.startTrackingRun(mRun);
                }

                updateUI();
                break;

            case R.id.btn_stop:
                mRunManager.stopRun();
                updateUI();
                break;
        }
    }
}
