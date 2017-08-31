package com.example.administrator.policeapp.ui.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.policeapp.R;
import com.example.administrator.policeapp.base.RxBaseFragment;
import com.flyco.tablayout.SlidingTabLayout;

import java.util.Arrays;
import java.util.List;

import butterknife.Bind;

/**
 * Created by Administrator on 2016/9/21.
 */
public class HomeFragment extends RxBaseFragment {
    @Bind(R.id.sliding_tabs)
    SlidingTabLayout mSlidingTabLayout;
    @Bind(R.id.tab_pager)
   public ViewPager mViewPager;
    private List<String> titles= Arrays.asList("  头条  ","  社会  ","  国内  ","  国际  ","  军事  ");
    public static HomeFragment newInstance(){
        return new HomeFragment();
    }
    @Override
    public int getLayoutId()
    {

        return R.layout.fragment_home;
    }

    @Override
    public void initViews()
    {


        mViewPager.setAdapter(new TabPagerAdapter(getChildFragmentManager()));


        mSlidingTabLayout.setViewPager(mViewPager);
        mViewPager.setCurrentItem(0);
    }


    private class TabPagerAdapter extends FragmentStatePagerAdapter
    {


        public TabPagerAdapter(FragmentManager fm)
        {

            super(fm);

        }

        @Override
        public Fragment getItem(int position)
        {
            Log.i("po",position+"");
            return nFragment.newInstance(position);
        }

        @Override
        public CharSequence getPageTitle(int position)
        {

            return titles.get(position);
        }

        @Override
        public int getCount()
        {

            return titles.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
            View view=((nFragment)object).getView();
            mViewPager.removeView(view);
            container.removeView(view);
            ((nFragment)object).onDestroy();
        }
    }
}
