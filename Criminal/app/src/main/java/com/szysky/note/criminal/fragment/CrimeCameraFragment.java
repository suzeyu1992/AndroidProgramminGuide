package com.szysky.note.criminal.fragment;

import android.content.Context;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.szysky.note.criminal.R;

/**
 * Author :  suzeyu
 * Time   :  2016-08-08  下午6:42
 * Blog   :  http://szysky.com
 * GitHub :  https://github.com/suzeyu1992
 *
 * ClassDescription :   显示相机的Fragment
 */

public class CrimeCameraFragment extends Fragment implements View.OnClickListener {

    private SurfaceView mV_sfv_camera;
    private AppCompatActivity mActivity;
    private Context mContext;
    private Camera mCamera;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (AppCompatActivity) getActivity();
        mContext = mActivity.getApplicationContext();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_crime_camera, container, false);

        Button mV_btn_take = (Button) rootView.findViewById(R.id.btn_camera_take);
        mV_btn_take.setOnClickListener(this);

        // 初始化SurfaceView
        mV_sfv_camera = (SurfaceView) rootView.findViewById(R.id.sfv_camera_display);
        SurfaceHolder holder = mV_sfv_camera.getHolder();
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        // 0为打开后置摄像头, 如果没有后置摄像头那么就打开前置摄像头
        mCamera = Camera.open(0);
    }

    @Override
    public void onPause() {
        super.onPause();
        mCamera.release();
        mCamera = null;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btn_camera_take:
                getActivity().finish();
                break;
        }

    }
}
