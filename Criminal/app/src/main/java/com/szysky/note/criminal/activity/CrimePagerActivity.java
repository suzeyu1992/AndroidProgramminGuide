package com.szysky.note.criminal.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.szysky.note.criminal.R;
import com.szysky.note.criminal.db.CrimeBean;
import com.szysky.note.criminal.db.CrimeLab;
import com.szysky.note.criminal.fragment.CrimeFragment;
import com.szysky.note.criminal.fragment.CrimeListFragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Author :  suzeyu
 * Time   :  2016-08-08  上午11:47
 * Blog   :  http://szysky.com
 * GitHub :  https://github.com/suzeyu1992
 * ClassDescription :
 */

public class CrimePagerActivity extends AppCompatActivity {

    /**
     *  作为对fragment进行托管的容器
     */
    private ViewPager mViewPager;

    /**
     *  提供给ViewPager的数据集合
     */
    private ArrayList<CrimeBean> mCrimes;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  动态创建ViewPager并设置到setContent
        mViewPager = new ViewPager(getApplicationContext());
        // 如果需要对Fragment进行托管, 那么要求其容器视图必须具备资源ID
        mViewPager.setId(R.id.vp_main);
        setContentView(mViewPager);

        mCrimes = CrimeLab.getInstance(getApplicationContext()).getCrimes();

        // 利用ViewPager的子类 FragmentStatesPagerAdapter可以简单的处理并是实现
        // 当使用fragment 还有一个选择FragmentPagerAdapter 两者的区别是在卸载不再需要的fragment的时候有所不容
        // FragmentPagerAdapter创建的fragment只是有销毁试图的动作, 没有将其从FragmentManager里面删除
        FragmentManager fm = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public Fragment getItem(int position) {
                // 通过集合和角标获得Crime的实例, 得到uuid并构建fragment返回
                CrimeBean crimeBean = mCrimes.get(position);
                CrimeFragment crimeFragment = CrimeFragment.newInstance(crimeBean.getId());

                return crimeFragment;
            }

            @Override
            public int getCount() {
                return mCrimes.size();
            }
        });


        // 设置初始分页的显示
        UUID uuid = (UUID) getIntent().getSerializableExtra(CrimeFragment.EXTRA_CRIME_ID);
        for (int i = 0; i < mCrimes.size(); i++) {
            if (uuid.equals(mCrimes.get(i).getId())){
                // 找到匹配的实例所在的下标, 其对应的也是ViewPager中item的显示的下标
                mViewPager.setCurrentItem(i);
                setTitle(mCrimes.get(i).getTitle());
                break;
            }
        }


        //  为ViewPager设置监听 并改变标题
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                CrimeBean crimeBean = mCrimes.get(position);
                if (crimeBean.getTitle() != null){
                    setTitle(crimeBean.getTitle());
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


















}
