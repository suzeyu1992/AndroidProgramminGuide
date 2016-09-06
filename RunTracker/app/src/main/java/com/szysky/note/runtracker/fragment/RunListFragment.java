package com.szysky.note.runtracker.fragment;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.szysky.note.runtracker.R;
import com.szysky.note.runtracker.RunManager;
import com.szysky.note.runtracker.db.Run;
import com.szysky.note.runtracker.db.RunDatabaseHelper;

/**
 * Author :  suzeyu
 * Time   :  2016-09-06  下午6:16
 * Blog   :  http://szysky.com
 * GitHub :  https://github.com/suzeyu1992
 * ClassDescription :
 */
public class RunListFragment extends ListFragment {
    private RunDatabaseHelper.RunCursor mCursor;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //  Query the list of runs
        mCursor = RunManager.getInstance(getActivity()).queryRuns();

        // 准备为ListFragment创建adapter 并设置
        RunCursorAdapter runCursorAdapter = new RunCursorAdapter(getActivity(), mCursor);
        setListAdapter(runCursorAdapter);

    }

    @Override
    public void onDestroy() {
        mCursor.close();
        super.onDestroy();
    }

    /**
     *  为了配合RunCursor的用处并得到最大化, 使用CursorAdapter创建一个子类. 来实现高效开发
     */
    private static class RunCursorAdapter extends CursorAdapter{

        private final RunDatabaseHelper.RunCursor mRunCursor;

        public RunCursorAdapter(Context context, Cursor c) {
            // 为了提倡使用loader, 大多数的flag已被废弃或有一些问题的存在, 所以这里传递了0
            super(context, c, 0);
            mRunCursor = (RunDatabaseHelper.RunCursor) c;
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            //  Use a layout inflater to get a row View
            LayoutInflater inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            return inflate.inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            // Get the run for the current row(行)
            Run run = mRunCursor.getRun();

            // Set up the start date text view
            TextView startDateTextView = (TextView) view;
            String cellText = context.getString(R.string.cell_text, run.getStartDate());
            startDateTextView.setText(cellText);
        }
    }
}
