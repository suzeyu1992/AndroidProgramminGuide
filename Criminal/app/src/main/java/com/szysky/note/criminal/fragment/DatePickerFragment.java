package com.szysky.note.criminal.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;

import com.szysky.note.criminal.R;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Author :  suzeyu
 * Time   :  2016-08-08  下午2:46
 * Blog   :  http://szysky.com
 * GitHub :  https://github.com/suzeyu1992
 *
 * ClassDescription :   在CrimeActivity 中添加一个日期选择的Fragment的
 */

public class DatePickerFragment extends DialogFragment {

    /**
     *  为设定argument创建key
     */
    public static final String EXTRA_DATE = "DatePicker_date";

    /**
     *  Fragment中的argument中保存时间
     */
    private Date mDate;


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // 首先获得创建本实例时候设置的 argument
        mDate = (Date)getArguments().getSerializable(EXTRA_DATE);
        // 需要把data对象 利用 Calendar对象转换成年月日
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mDate);
        int t_year = calendar.get(Calendar.YEAR);
        int t_month = calendar.get(Calendar.MONTH);
        int t_day = calendar.get(Calendar.DAY_OF_MONTH);


        //  inflate要显示的时间控件布局
        View inflate = getActivity().getLayoutInflater().inflate(R.layout.layout_dialog_date, null);
        DatePicker v_dp_date = (DatePicker) inflate.findViewById(R.id.view_date_picker);
        v_dp_date.init(t_year, t_month, t_day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // 使用calendar生成一个改变后的date时间戳
                mDate = new GregorianCalendar(year, monthOfYear, dayOfMonth).getTime();
                Log.e("haha", "data改变--> "+mDate.toString() );
                //  update argument  防止屏幕旋转销毁重建
                getArguments().putSerializable(EXTRA_DATE, mDate);
            }

        });

        // 直接返回在Activity中创建Dialog的方式生成的对象
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                //.setTitle(R.string.crime_date_picker)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendResult(Activity.RESULT_OK);
                    }
                })
                .setView(inflate);

        AlertDialog alertDialog = builder.create();

        return alertDialog;

    }

    /**
     *  为了给本实例设定argument 对创建实例,添加argument这一流程进行封装并对外暴露创建后的实例方法
     */
    public static DatePickerFragment newInstance(Date date){
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_DATE, date);

        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.setArguments(args);
        return datePickerFragment;

    }


    /**
     *  封装当前Fragment, 需要向目标Fragment传递的数据操作 ,
     */
    private void sendResult(int resultCode){
        if (getTargetFragment() == null){
            return ;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_DATE, mDate);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
