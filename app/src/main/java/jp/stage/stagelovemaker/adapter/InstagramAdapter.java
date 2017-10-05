package jp.stage.stagelovemaker.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import jp.stage.stagelovemaker.fragment.InstagramSlideImageFragment;
import jp.stage.stagelovemaker.model.InstagramPhoto;

/**
 * Created by congn on 8/7/2017.
 */

public class InstagramAdapter extends FragmentStatePagerAdapter {

    ArrayList<InstagramPhoto> avatarModels;

    public InstagramAdapter(FragmentManager fm, ArrayList<InstagramPhoto> models) {
        super(fm);
        avatarModels = models;
    }

    @Override
    public Fragment getItem(int position) {
        return InstagramSlideImageFragment.newInstance(avatarModels.get(position).getStandard_resolution_url());
    }

    @Override
    public int getCount() {
        return avatarModels.size();
    }
}
