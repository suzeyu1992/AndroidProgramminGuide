package com.szysky.note.criminal.fragment;

import android.content.Context;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.szysky.note.criminal.R;

import java.io.IOException;
import java.util.List;

/**
 * Author :  suzeyu
 * Time   :  2016-08-08  下午6:42
 * Blog   :  http://szysky.com
 * GitHub :  https://github.com/suzeyu1992
 *
 * ClassDescription :   显示相机的Fragment
 */

public class CrimeCameraFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = CrimeCameraFragment.class.getSimpleName();
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

        //  对SurfaceView的声明周期进行关联
        holder.addCallback(new SurfaceHolder.Callback() {

            /** SurfaceView的视图层级结构被放在屏幕上时候被调用, 这里也是SurfaceView与客户端(Camera)进行关联的地方*/
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                    try {
                        if (mCamera != null) {
                            mCamera.setPreviewDisplay(holder);
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "@@-> Camera设置关联SurfaceView预览显示失败!" );
                    }
            }

            /**  Surface首次显示在屏幕上的时候被动调用的方法, 通过此参数可以知道Surface的像素格式以及他的宽高.
             通过此方法可以通知Surface客户端, 有多大的绘制区域可以使用. */
            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                if (mCamera == null) return;
                // The surface has change size, update the camera preview size
                Camera.Parameters parameters = mCamera.getParameters();
                Camera.Size size = getSupportedSize(parameters.getSupportedPreviewSizes(), width, height);
                parameters.setPreviewSize(size.width, size.height);
                mCamera.setParameters(parameters);

                try {
                    //  开始在Surface上绘制
                    mCamera.startPreview();
                }catch (Exception e){
                    //  如果开始预览绘制失败那么我们通过这里来释放相机资源
                    Log.e(TAG, "@@-> startPreview()启动失败, 准备释放相机资源!" );
                    mCamera.release();
                    mCamera = null;

                }
            }

            /** SurfaceView从屏幕上移除时, Surface也随之被销毁, 通过客户端停止使用Surface*/
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                if (mCamera != null){
                    mCamera.stopPreview();
                }
            }
        });

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


    /**
     *  找出具有最大数目的像素的尺寸
     */
    private Camera.Size getSupportedSize(List<Camera.Size> sizes, int width, int height){
        Camera.Size bestSize = sizes.get(0);
        int largestArea = bestSize.width * bestSize.height;
        for (Camera.Size s : sizes) {
            int area = s.width * s.height;
            if (area > largestArea){
                bestSize = s;
                largestArea = area;
            }
        }

        return bestSize;

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
