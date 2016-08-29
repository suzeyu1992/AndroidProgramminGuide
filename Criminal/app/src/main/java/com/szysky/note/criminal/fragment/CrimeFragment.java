package com.szysky.note.criminal.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
    private Button btn_crime_date;
    private CheckBox cb_crime_solved;

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

        // 查找控件
        EditText et_crime_title = (EditText) rootView.findViewById(R.id.et_crime_title);
        btn_crime_date = (Button) rootView.findViewById(R.id.btn_crime_date);
        cb_crime_solved = (CheckBox) rootView.findViewById(R.id.cb_crime_solved);

        // 为按钮设置创建陋习bean的时间
        btn_crime_date.setText(mCrimeBean.getDate());
        btn_crime_date.setEnabled(false);   // 禁用Button 会发现其样式也会发生改变

        //  EditText添加监听 用来改变陋习的内容
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


        //  checkBox进行初始化设置
        cb_crime_solved.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //  设置陋习的是否已经解决
                mCrimeBean.setSolved(isChecked);
            }
        });


        return rootView;
    }
}
