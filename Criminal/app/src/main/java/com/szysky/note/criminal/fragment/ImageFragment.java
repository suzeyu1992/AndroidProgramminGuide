package com.szysky.note.criminal.fragment;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.szysky.note.criminal.utils.MyPictureUtils;

import java.io.Serializable;

/**
 * Author :  suzeyu
 * Time   :  2016-09-09  下午4:36
 * Blog   :  http://szysky.com
 * GitHub :  https://github.com/suzeyu1992
 *
 * ClassDescription :   显示一个大图的Fragment
 */

public class ImageFragment extends DialogFragment {
    public static final String EXTRA_IMAGE_PATH = "image_path";
    private ImageView mImageView;

    public static ImageFragment newInstance(String imagePath){
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_IMAGE_PATH, imagePath);

        ImageFragment imageFragment = new ImageFragment();
        imageFragment.setArguments(bundle);
        imageFragment.setStyle(DialogFragment.STYLE_NO_TITLE, 0);

        return imageFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        mImageView = new ImageView(getActivity().getApplicationContext());

        //  获得要显示的图片路径
        String path = (String) getArguments().getSerializable(EXTRA_IMAGE_PATH);

        BitmapDrawable image = MyPictureUtils.getScaleDrawable(getActivity(), path);

        mImageView.setImageDrawable(image);

        return mImageView;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MyPictureUtils.cleanImageView(mImageView);
    }
}
