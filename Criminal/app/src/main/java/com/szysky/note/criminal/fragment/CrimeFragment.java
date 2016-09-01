package com.szysky.note.criminal.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.szysky.note.criminal.R;
import com.szysky.note.criminal.activity.CrimeCameraActivity;
import com.szysky.note.criminal.db.CrimeBean;
import com.szysky.note.criminal.db.CrimeLab;
import com.szysky.note.criminal.db.PhotoBean;
import com.szysky.note.criminal.utils.MyPictureUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * Author :  suzeyu
 * Time   :  2016-08-08  下午5:19
 * Blog   :  http://szysky.com
 * GitHub :  https://github.com/suzeyu1992
 *
 * ClassDescription : 控制层用来显示 Crim 类实例中的信息
 */

public class CrimeFragment extends Fragment {

    private static final String TAG = CrimeFragment.class.getSimpleName();
    /**
     *  Intent接收具体实例的id键值
     */
    public static final String EXTRA_CRIME_ID = "crime_id";

    /**
     *  请求代码常量 用于DialogFragment 设定请求目标的fragment
     */
    public static final int REQUEST_DATE = 0, REQUEST_PHOTO = 1;


    private CrimeBean mCrimeBean;
    private Button btn_crime_date;
    private CheckBox cb_crime_solved;
    private ImageView mPhotoView;

    /**
     *  给DialogPickerFragment声明一个字符串标识
     */
    private static final String DIALOG_DATE = "date";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");

        //  开启选项菜单处理
        setHasOptionsMenu(true);

        //  通过 argument 来获得uuid
        UUID crimeID = (UUID) getArguments().getSerializable(EXTRA_CRIME_ID);

        mCrimeBean = CrimeLab.getInstance(getActivity()).getCrime(crimeID);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        //  修改操作栏的返回键显示
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //  转换一个xml作为视图, 并返回给activity
        View rootView = inflater.inflate(R.layout.fragment_crime, container, false);

        // 查找控件
        EditText et_crime_title = (EditText) rootView.findViewById(R.id.et_crime_title);
        btn_crime_date = (Button) rootView.findViewById(R.id.btn_crime_date);
        cb_crime_solved = (CheckBox) rootView.findViewById(R.id.cb_crime_solved);
        ImageButton mVIBtnCamera =  (ImageButton) rootView.findViewById(R.id.ibtn_camera);
        mPhotoView = (ImageView) rootView.findViewById(R.id.iv_camera_result);

        // 为按钮设置创建陋习bean的时间
        updateDate();
        //btn_crime_date.setEnabled(false);   // 禁用Button 会发现其样式也会发生改变

        //  设置获取Intent中关联的陋习数据
        et_crime_title.setText(mCrimeBean.getTitle());
        cb_crime_solved.setChecked(mCrimeBean.isSolved());

        //  EditText添加监听 用来改变陋习的内容
        et_crime_title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 当 editText 的内容发生改变的时候, 将值设置到bean对象中
                mCrimeBean.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        //  checkBox进行初始化设置
        cb_crime_solved.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //  设置陋习的是否已经解决
                mCrimeBean.setSolved(isChecked);
            }
        });

        //  为按钮设置点击事件
        btn_crime_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mCrimeBean.getRawDate());

                // 给DialogFragment 设定一个目标Fragment的用于数据的返回
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);

                // 让DialogFragment显示有show()的重载两个方式, 一个是传入FragmentManager 和 FragmentTransaction
                // 如果传入FragmentManager那么事务可以自动创建并提交
                dialog.show(fm, DIALOG_DATE);
            }
        });

        mVIBtnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 对于设备没有相机的应该进行判断
                PackageManager pm = getActivity().getPackageManager();
                boolean hasACamera = pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)
                        || pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)
                        || Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD
                        || Camera.getNumberOfCameras() > 0;

                if (hasACamera){
                    //  相机可以使用
                    Intent intent = new Intent();
                    intent.setClass(getActivity().getApplicationContext(), CrimeCameraActivity.class);
                    startActivityForResult(intent, REQUEST_PHOTO);
                }else{
                    // 相机无法使用, Toast提示
                    Toast.makeText(getActivity(), "相机无法使用", Toast.LENGTH_SHORT).show();
                }


            }
        });

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        showPhoto();
    }

    @Override
    public void onPause() {
        super.onPause();
        // 在失去焦点的时候进行数据的存储
        CrimeLab.getInstance(getActivity().getApplicationContext()).saveCrimes();
    }

    @Override
    public void onStop() {
        super.onStop();
        MyPictureUtils.cleanImageView(mPhotoView);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) return;

        if (requestCode == REQUEST_DATE){
            //  判断是否是时间控件DatePicker所属的 DialogFragment打开的
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrimeBean.setDate(date);
            updateDate();
        } else if (requestCode == REQUEST_PHOTO){
            //  判断是否是打开照相Fragment所返回的

            //  判断是否携带了存储图片的路径信息
            if (resultCode == Activity.RESULT_OK){
                // Create n new Photo object and attach into the Crime
                String stringExtra = data.getStringExtra(CrimeCameraFragment.EXTRA_PHOTO_FILENAME);

                PhotoBean photoBean = new PhotoBean(stringExtra);
                mCrimeBean.setmPhoto(photoBean);
                Log.i(TAG, "onActivityResult: @@-> 图片保存的文件名: "+stringExtra +"   Crime: "+mCrimeBean.getTitle()+" 对象已经存在照片属性");
                showPhoto();

            }else {
                // 照相失败
                Toast.makeText(getActivity().getApplicationContext(), "照相失败", Toast.LENGTH_SHORT).show();
            }


        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                getActivity().finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);


        }


    }



    /**
     *  抽取常用方法, 更新按钮的显示时间
     */
    private void updateDate() {
        btn_crime_date.setText(mCrimeBean.getDate());
    }

    /**
     *  为了满足给 Fragment 添加 argument的两个条件
     *      1. 必须在fragment创建实例之后
     *      2. 必须在添加到activity之前
     *   对这一步骤进行内部封装, 提供一个静态方法, 接收一个uuid, 并直接添加到新的bundle上,
     *   然后创建本类实例并添加进去, 最后才把本类实例对象返回给Activity进行托管.
     */
    public static CrimeFragment newInstance(UUID crimeId){
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_CRIME_ID, crimeId);

        CrimeFragment crimeFragment = new CrimeFragment();
        crimeFragment.setArguments(bundle);
        return crimeFragment;
    }

    /**
     *  设置一个方法, 直接将缩放后的图片设置给ImageView视图
     */
    private void showPhoto(){
        PhotoBean photoBean = mCrimeBean.getmPhoto();
        BitmapDrawable bitmapDra = null;

        if (photoBean != null){
            String path = getActivity().getFileStreamPath(photoBean.getmFilename()).getAbsolutePath();
            BitmapDrawable scaleDrawable = MyPictureUtils.getScaleDrawable(getActivity(), path);

            mPhotoView.setImageDrawable(scaleDrawable);
        }
    }




}
