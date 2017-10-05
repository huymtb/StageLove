package jp.stage.stagelovemaker.views;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import jp.stage.stagelovemaker.R;
import jp.stage.stagelovemaker.adapter.AvatarPagerAdapter;
import jp.stage.stagelovemaker.utils.Constants;

/**
 * Created by congn on 8/4/2017.
 */

public class UserImageSlide extends RelativeLayout {
    View rootView;
    ViewPager viewPager;
    PageControl pageControl;
    ImageView holderImageView;

    public UserImageSlide(Context context) {
        super(context);
        init(context);
    }

    public UserImageSlide(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        rootView = inflate(context, R.layout.view_user_image_slide, this);
        viewPager = (ViewPager) rootView.findViewById(R.id.slide_avatar);
        pageControl = (PageControl) rootView.findViewById(R.id.pagecontrol_avatar);
        holderImageView = (ImageView) rootView.findViewById(R.id.image_holder_avatar);
        pageControl.setHintPageColor(ContextCompat.getColor(getContext(), R.color.gray_dark));
    }

    public void setAvatar(FragmentManager fragment, ArrayList<String> avatarModels) {

        if (avatarModels != null && avatarModels.size() > 0) {
            holderImageView.setVisibility(INVISIBLE);
            viewPager.setVisibility(VISIBLE);
            AvatarPagerAdapter adapter = new AvatarPagerAdapter(fragment, avatarModels);
            viewPager.setAdapter(adapter);
            if (avatarModels.size() > 1) {
                pageControl.setNumberOfPages(avatarModels.size());
                pageControl.setCurrentPageProfile(0);
                pageControl.setVisibility(VISIBLE);
                viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        pageControl.setCurrentPageProfile(position);
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });
            } else {
                pageControl.setVisibility(GONE);
            }
        } else {
            pageControl.setVisibility(GONE);
            setAvatar(null);
        }
    }

    public void setAvatar(String model) {
        holderImageView.setVisibility(VISIBLE);
        viewPager.setVisibility(INVISIBLE);
        String avatarUrl = Constants.LINK_FAIL;
        if (model != null) {
            avatarUrl = model;
        }
        holderImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        Glide.with(getContext()).load(model).centerCrop().into(holderImageView);
    }
}
