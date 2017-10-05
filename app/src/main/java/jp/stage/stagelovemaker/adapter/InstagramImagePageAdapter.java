package jp.stage.stagelovemaker.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import jp.stage.stagelovemaker.fragment.InstagramImageFragment;
import jp.stage.stagelovemaker.model.InstagramPhoto;

/**
 * Created by congn on 8/7/2017.
 */

public class InstagramImagePageAdapter extends FragmentStatePagerAdapter {

    int numColumn;
    int numberFragment;
    String userNameInstagram;
    ArrayList<InstagramPhoto> photoModels;

    public InstagramImagePageAdapter(FragmentManager fm, ArrayList<InstagramPhoto> models, int column, int nFragment,
                                     String userName) {
        super(fm);
        photoModels = models;
        numColumn = column;
        numberFragment = nFragment;
        userNameInstagram = userName;
    }

    @Override
    public Fragment getItem(int position) {
        ArrayList<InstagramPhoto> images = new ArrayList<>();
        for (int i = 0; i < (numColumn * 2); i++) {
            if ((position * numColumn * 2) + i >= photoModels.size()) {
                break;
            }
            images.add(photoModels.get((position * numColumn * 2) + i));
        }
        return InstagramImageFragment.newInstance(images, numColumn, userNameInstagram);
    }

    @Override
    public int getCount() {
        return numberFragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (position >= getCount()) {
            View view = (View) object;
            container.removeView(view);
        }
    }
}
