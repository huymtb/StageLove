package jp.stage.stagelovemaker.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import jp.stage.stagelovemaker.fragment.MainStageFragment;
import jp.stage.stagelovemaker.fragment.MatchesFragment;
import jp.stage.stagelovemaker.fragment.ProfileFragment;

/**
 * Created by congn on 7/11/2017.
 */

public class FeaturesPagerAdapter extends FragmentPagerAdapter {
    ProfileFragment profileFragment;
    MainStageFragment mainStageFragment;
    MatchesFragment matchesFragment;

    public FeaturesPagerAdapter(FragmentManager fm) {
        super(fm);
        profileFragment = ProfileFragment.newInstance();
        mainStageFragment = MainStageFragment.newInstance();
        matchesFragment = MatchesFragment.newInstance();
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return profileFragment;
            case 1:
                return mainStageFragment;
            case 2:
                return matchesFragment;
        }
        return null;
    }

    public void getLocation(Boolean b) {
        mainStageFragment.allowAccessLocation(b);
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (position >= getCount()) {
            View view = (View) object;
            container.removeView(view);
        }
    }
}
