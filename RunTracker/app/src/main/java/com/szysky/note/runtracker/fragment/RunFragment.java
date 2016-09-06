package com.szysky.note.runtracker.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.szysky.note.runtracker.R;
import com.szysky.note.runtracker.RunManager;

/**
 * Author :  suzeyu
 * Time   :  2016-09-06  上午10:14
 * Blog   :  http://szysky.com
 * GitHub :  https://github.com/suzeyu1992
 * ClassDescription :   启动页的管理Fragment
 */
public class RunFragment extends Fragment implements View.OnClickListener {

    private TextView mStartedTextView;
    private TextView mLatitudeTextView;
    private TextView mLongitudeTextView;
    private TextView mAltitudeTextView;
    private TextView mDurationTextView;
    private Button mStartButton;
    private Button mStopButton;
    private RunManager mRunManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        //  获取地理位置的管理者的类
        mRunManager = RunManager.getInstance(getActivity());
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

    private void updateUI(){
        //  获得目前获取位置的广播的开启状态
        boolean trackingRunState = mRunManager.isTrackingRun();

        //  根据状态, 设置开启和关闭的按钮在同一个时间点只会有一个生效
        mStartButton.setEnabled(!trackingRunState);
        mStopButton.setEnabled(trackingRunState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_start:
                mRunManager.startLocationUpdates();
                updateUI();
                break;

            case R.id.btn_stop:
                mRunManager.stopLocationUpdates();
                updateUI();
                break;
        }
    }
}
