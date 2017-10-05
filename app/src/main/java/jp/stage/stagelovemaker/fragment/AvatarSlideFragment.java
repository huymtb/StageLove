package jp.stage.stagelovemaker.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import jp.stage.stagelovemaker.R;
import jp.stage.stagelovemaker.base.BaseFragment;
import jp.stage.stagelovemaker.utils.Constants;
import jp.stage.stagelovemaker.utils.Utils;

/**
 * Created by congn on 8/4/2017.
 */

public class AvatarSlideFragment extends BaseFragment {

    ImageView imageView;

    public static AvatarSlideFragment createInstance(String model) {
        AvatarSlideFragment fragment = new AvatarSlideFragment();
        Bundle args = new Bundle();
        args.putString(Constants.KEY_DATA, model);
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
        View view = inflater.inflate(R.layout.fragment_avatar_slide, container, false);
        imageView = (ImageView) view.findViewById(R.id.avatar_pal_img);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getArguments() != null) {
            String model = getArguments().getString(Constants.KEY_DATA);
            if (model != null) {
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                Glide.with(getActivity())
                        .load(model)
                        .centerCrop()
                        .placeholder(R.mipmap.ic_holder)
                        .error(R.mipmap.ic_holder)
                        .into(imageView);
            }
        }
    }
}
