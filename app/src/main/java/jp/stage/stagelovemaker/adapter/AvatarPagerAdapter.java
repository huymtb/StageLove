package jp.stage.stagelovemaker.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import jp.stage.stagelovemaker.fragment.AvatarSlideFragment;

/**
 * Created by congn on 8/4/2017.
 */

public class AvatarPagerAdapter extends FragmentStatePagerAdapter {
    ArrayList<String> avatarModels;

    public AvatarPagerAdapter(FragmentManager fm, ArrayList<String> models) {
        super(fm);
        avatarModels = models;
    }

    @Override
    public Fragment getItem(int position) {
        return AvatarSlideFragment.createInstance(avatarModels.get(position));
    }

    @Override
    public int getCount() {
        return avatarModels.size();
    }
}
