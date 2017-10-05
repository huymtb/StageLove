package jp.stage.stagelovemaker.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import jp.stage.stagelovemaker.R;

import static jp.stage.stagelovemaker.utils.Constants.KEY_DATA;

/**
 * Created by congn on 7/11/2017.
 */

public class IntroSlideFragment extends Fragment {
    TextView tvIntro;
    SubsamplingScaleImageView ivIntro;
    int position;

    public static IntroSlideFragment newInstance(int position) {
        Bundle args = new Bundle();
        args.putInt(KEY_DATA, position);
        IntroSlideFragment fragment = new IntroSlideFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_intro_slide, container, false);
        tvIntro = (TextView) view.findViewById(R.id.tv_intro);
        ivIntro = (SubsamplingScaleImageView) view.findViewById(R.id.iv_intro);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Typeface typeFace = Typeface.createFromAsset(getActivity().getAssets(), "fonts/proximanovasoft-regular.otf");
        tvIntro.setTypeface(typeFace);

        if (savedInstanceState == null) {
            position = getArguments().getInt(KEY_DATA);
        } else {
            position = savedInstanceState.getInt("position");
        }

        switch (position){
            case 0:
                tvIntro.setText(R.string.intro_1);
                ivIntro.setImage(ImageSource.resource(R.drawable.intro_v2_slide_1));
                break;
            case 1:
                tvIntro.setText(R.string.intro_2);
                ivIntro.setImage(ImageSource.resource(R.drawable.intro_v2_slide_2));
                break;
            case 2:
                tvIntro.setText(R.string.intro_3);
                ivIntro.setImage(ImageSource.resource(R.drawable.intro_v2_slide_3));
                break;
            case 3:
                tvIntro.setText(R.string.intro_4);
                ivIntro.setImage(ImageSource.resource(R.drawable.intro_v2_slide_4));
                break;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("position", position);
        super.onSaveInstanceState(outState);
    }
}
