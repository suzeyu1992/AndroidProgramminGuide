package com.szysky.note.runtracker;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Author :  suzeyu
 * Time   :  2016-09-06  上午10:14
 * Blog   :  http://szysky.com
 * GitHub :  https://github.com/suzeyu1992
 * ClassDescription :   启动页的管理Fragment
 */
public class RunFragment extends Fragment {

    private TextView mStartedTextView;
    private TextView mLatitudeTextView;
    private TextView mLongitudeTextView;
    private TextView mAltitudeTextView;
    private TextView mDurationTextView;
    private Button mStartButton;
    private Button mStopButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
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

        return  rootView;


    }
}
