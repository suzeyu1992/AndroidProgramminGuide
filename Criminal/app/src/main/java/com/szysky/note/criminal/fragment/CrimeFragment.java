package com.szysky.note.criminal.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.szysky.note.criminal.R;
import com.szysky.note.criminal.db.CrimeBean;

/**
 * Author :  suzeyu
 * Time   :  2016-08-08  下午5:19
 * Blog   :  http://szysky.com
 * GitHub :  https://github.com/suzeyu1992
 *
 * ClassDescription : 控制层用来显示 Crim 类实例中的信息
 */

public class CrimeFragment extends Fragment {

    private CrimeBean mCrimeBean;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCrimeBean =  new CrimeBean();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //  转换一个xml作为视图, 并返回给activity
        View rootView = inflater.inflate(R.layout.fragment_crime, container, false);

        // 查找控件并设置监听
        EditText et_crime_title = (EditText) rootView.findViewById(R.id.et_crime_title);
        et_crime_title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 当 editText 的内容发生改变的时候, 将值设置到bean对象中
                mCrimeBean.setTitle(s.toString());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return rootView;
    }
}
