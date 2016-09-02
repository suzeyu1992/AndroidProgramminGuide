package com.szysky.note.criminal.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.szysky.note.criminal.R;
import com.szysky.note.criminal.SingleFragmentActivity;
import com.szysky.note.criminal.db.CrimeBean;
import com.szysky.note.criminal.fragment.CrimeFragment;
import com.szysky.note.criminal.fragment.CrimeListFragment;

/**
 * Author :  suzeyu
 * Time   :  2016-08-08  上午9:48
 * Blog   :  http://szysky.com
 * GitHub :  https://github.com/suzeyu1992
 * ClassDescription :   展示所有陋习的列表
 */

public class CrimeListActivity extends SingleFragmentActivity  implements CrimeListFragment.Callbacks, CrimeFragment.Callbacks{
    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_masterdetail;
    }

    @Override
    public void onCrimeSelected(CrimeBean item) {
        //  说明是手机
        if (findViewById(R.id.fl_detail_container) == null){
            Intent intent = new Intent(getApplicationContext(), CrimePagerActivity.class);
            // 传入陋习的id
            intent.putExtra(CrimeFragment.EXTRA_CRIME_ID, item.getId());
            startActivity(intent);
        }else{
            //说明是平板
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();

            //  判断一下要添加在右边的Fragment是否已经存在, 存在的话就清除,重新添加
            Fragment detail = fm.findFragmentById(R.id.fl_detail_container);
            if (detail != null){
                transaction.remove(detail);
            }

            CrimeFragment crimeFragment = CrimeFragment.newInstance(item.getId());
            transaction.add(R.id.fl_detail_container, crimeFragment);
            transaction.commit();
        }
    }

    @Override
    public void onCrimeUI(CrimeBean crimeBean) {

    }

    //  进行陋习详情页面的刷新
    @Override
    public void onCrimeUpdated(CrimeBean crimeBean) {
        FragmentManager fm = getSupportFragmentManager();
        CrimeListFragment listFragment = (CrimeListFragment) fm.findFragmentById(R.id.activity_main);

        listFragment.updateUI();

    }
}
