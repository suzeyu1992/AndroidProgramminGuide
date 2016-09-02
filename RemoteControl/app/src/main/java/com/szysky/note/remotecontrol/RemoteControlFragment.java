package com.szysky.note.remotecontrol;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * Author :  suzeyu
 * Time   :  2016-09-09  上午11:31
 * Blog   :  http://szysky.com
 * GitHub :  https://github.com/suzeyu1992
 *
 * ClassDescription :   主界面的Fragment作为控制层来控制UI
 */

public class RemoteControlFragment extends Fragment implements View.OnClickListener {

    private TextView mSelectedTextView;
    private TextView mWorkingTextView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_remote_control, container, false);

        //  找到需要的控件
        mSelectedTextView = (TextView) root.findViewById(R.id.tv_selected);
        mWorkingTextView = (TextView) root.findViewById(R.id.tv_working);


        //  创建9个数字键公用的监听对象
        View.OnClickListener numberClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String working = mWorkingTextView.getText().toString();
                TextView btnView = (TextView) v;
                String text = btnView.getText().toString();

                if (working.equals("0")) {
                    mWorkingTextView.setText(text);
                } else {
                    mWorkingTextView.setText(working + text);
                }
            }
        };


        TableLayout tableLayout = (TableLayout) root.findViewById(R.id.tabl_main);

        int number = 1;
        //  遍历根布局里面的孩子view, 去除掉最上面的两个非按钮控件 而最后一个孩子是操作键
        //  只留下9个数字键
        for (int i = 2; i < tableLayout.getChildCount()-1; i++) {
            TableRow row = (TableRow) tableLayout.getChildAt(i);
            for (int j = 0; j < row.getChildCount(); j++) {
                Button button = (Button) row.getChildAt(j);
                button.setText(number+"");
                button.setOnClickListener(numberClick);
                number++;
            }
        }

        //  对最后一排按钮进行特殊处理
        TableRow bottomRow = (TableRow) tableLayout.getChildAt(tableLayout.getChildCount() - 1);
        // 清除按钮
        Button deleteButton = (Button) bottomRow.getChildAt(0);
        deleteButton.setText("Delete");
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWorkingTextView.setText("0");
            }
        });

        // 0号键
        Button zeroButton = (Button) bottomRow.getChildAt(1);
        zeroButton.setText("0");
        zeroButton.setOnClickListener(numberClick);

        // 确定键
        Button enterButton = (Button) bottomRow.getChildAt(2);
        enterButton.setText("Enter");
        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        return root;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

        }
    }
}
