package jp.stage.stagelovemaker.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import jp.stage.stagelovemaker.R;
import jp.stage.stagelovemaker.base.BaseFragment;
import jp.stage.stagelovemaker.utils.Constants;
import jp.stage.stagelovemaker.utils.Utils;

/**
 * Created by congn on 8/7/2017.
 */

public class InstagramSlideImageFragment extends BaseFragment {

    ImageView imageView;

    public static InstagramSlideImageFragment newInstance(String link) {
        InstagramSlideImageFragment fragment = new InstagramSlideImageFragment();
        Bundle args = new Bundle();
        args.putString(Constants.KEY_DATA, link);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_instagram_slide, container, false);
        imageView = (ImageView) view.findViewById(R.id.image_instagram);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        String link = getArguments().getString(Constants.KEY_DATA);
        if (!TextUtils.isEmpty(link)) {
            Utils.setAvatar(getContext(), imageView, link);
        }

    }
}
