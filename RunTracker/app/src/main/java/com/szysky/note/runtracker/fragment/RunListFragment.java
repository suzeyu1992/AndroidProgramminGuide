package com.szysky.note.runtracker.fragment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.szysky.note.runtracker.R;
import com.szysky.note.runtracker.RunManager;
import com.szysky.note.runtracker.activity.RunActivity;
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

    private static final int REQUEST_NEW_RUN = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        //  Query the list of runs
        mCursor = RunManager.getInstance(getActivity()).queryRuns();

        // 准备为ListFragment创建adapter 并设置
        RunCursorAdapter runCursorAdapter = new RunCursorAdapter(getActivity(), mCursor);
        setListAdapter(runCursorAdapter);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.run_list_options, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_new_run:
                Intent intent = new Intent(getActivity(), RunActivity.class);
                startActivityForResult(intent, REQUEST_NEW_RUN);
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (REQUEST_NEW_RUN == requestCode){
            mCursor.requery();
            ((RunCursorAdapter)getListAdapter()).notifyDataSetChanged();
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Intent intent = new Intent(getActivity(), RunActivity.class);

        // 因为指定了run_id表中的ID字段, CursorAdapter检测到该字段并将其作为id传递给了此方法中
        intent.putExtra(RunActivity.EXTRA_RUN_ID, id);
        startActivity(intent);
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
