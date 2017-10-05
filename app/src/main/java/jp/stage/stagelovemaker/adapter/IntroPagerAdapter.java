package jp.stage.stagelovemaker.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import jp.stage.stagelovemaker.fragment.IntroSlideFragment;

/**
 * Created by congn on 7/11/2017.
 */

public class IntroPagerAdapter extends FragmentPagerAdapter {
    ArrayList<IntroSlideFragment> slideFragments = new ArrayList<>();

    public IntroPagerAdapter(FragmentManager fm) {
        super(fm);
        slideFragments.add(IntroSlideFragment.newInstance(0));
        slideFragments.add(IntroSlideFragment.newInstance(1));
        slideFragments.add(IntroSlideFragment.newInstance(2));
        slideFragments.add(IntroSlideFragment.newInstance(3));
    }

    @Override
    public Fragment getItem(int position) {
        if (position >= 0 && position <= 3) {
            return slideFragments.get(position);
        }
        return null;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override

    public void destroyItem(ViewGroup container, int position, Object object) {
        if (position >= getCount()) {
            View view = (View) object;
            container.removeView(view);
        }
    }
}
