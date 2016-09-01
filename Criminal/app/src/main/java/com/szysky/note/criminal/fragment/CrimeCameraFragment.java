package com.szysky.note.criminal.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

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
    public static final String EXTRA_PHOTO_FILENAME = "file_name";
    private SurfaceView mV_sfv_camera;
    private AppCompatActivity mActivity;
    private Context mContext;

    /**
     *  相机类, 打开摄像头
     */
    private Camera mCamera;

    /**
     *  用户进行拍照的时候进行进度条显示的View
     */
    private View mVFlProgress;

    /**
     *  在进行快门的时候进行进度条View的显示
     */
    private Camera.ShutterCallback mShutterCallback = new Camera.ShutterCallback() {
        @Override
        public void onShutter() {
            mVFlProgress.setVisibility(View.VISIBLE);
        }
    };

    /**
     *  Camera如果快门之后有数据处理生成成功,  那么会调此回调, 可以进行把数据写入到本地的动作
     */
    private Camera.PictureCallback mJpegCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            // create a filename
            String filename = UUID.randomUUID().toString()+".jpg";
            //  save the jpeg date to disk
            FileOutputStream out = null;
            boolean success = true;

            try {
                // 获得输出流进行数据的写入
                out = mContext.openFileOutput(filename, Context.MODE_PRIVATE);
                out.write(data);
            } catch (IOException e) {
                Log.e(TAG, "onPictureTaken: @@-> 图片写入disk失败, 失败文件地址:"+filename, e );
            }finally {
                // 进行清扫动作, 关流
                if (out != null){
                    try {
                        out.close();
                    } catch (IOException e) {
                        Log.e(TAG, "onPictureTaken: @@-> 流文件关流失败, 失败文件地址:"+filename, e );
                        success = false;
                    }
                }
            }

           // Log.i(TAG, "onPictureTaken(): 照片 JPEG 保存成功, 即将关闭activity, 保存地址:"+filename);
            if(success){
                // 设置保存文件的信息返回
                Intent intent = new Intent();
                intent.putExtra(EXTRA_PHOTO_FILENAME, filename);
                mActivity.setResult(Activity.RESULT_OK, intent);
            }else{
                mActivity.setResult(Activity.RESULT_CANCELED);
            }

            mActivity.finish();
        }
    };




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

        //  查找控件
        Button mV_btn_take = (Button) rootView.findViewById(R.id.btn_camera_take);
        mVFlProgress = rootView.findViewById(R.id.fl_camera_progress);
        mVFlProgress.setVisibility(View.INVISIBLE);

        mV_btn_take.setOnClickListener(this);

        // 初始化SurfaceView
        initSurfaceView(rootView);

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
     *  对SurfaceView控件进行一些初始化和绑定客户端Camera
     */
    private void initSurfaceView(View rootView) {
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
                //  设置图片尺寸大小
                parameters.setPictureSize(size.width, size.height);

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
                //  进行拍照
                if (mCamera != null){
                    // 从Camera预览中 捕获一帧的图像数据, 参数如果不需要可以置为null
                    // 但第三个参数, 最好去实现, 要不此方法不具备什么意义
                    mCamera.takePicture(mShutterCallback, null, mJpegCallback);
                }
                break;
        }

    }


}
