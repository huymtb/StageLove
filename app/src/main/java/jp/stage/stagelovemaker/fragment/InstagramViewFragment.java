package jp.stage.stagelovemaker.fragment;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import jp.stage.stagelovemaker.R;
import jp.stage.stagelovemaker.adapter.InstagramAdapter;
import jp.stage.stagelovemaker.base.BaseFragment;
import jp.stage.stagelovemaker.model.InstagramPhoto;
import jp.stage.stagelovemaker.utils.Constants;
import jp.stage.stagelovemaker.views.TitleBar;

/**
 * Created by congn on 8/7/2017.
 */

public class InstagramViewFragment extends BaseFragment implements TitleBar.TitleBarCallback,
        View.OnClickListener {

    TitleBar titleBar;
    TextView nameTextView;
    String userNameInstagram;
    ViewPager viewPager;

    public static InstagramViewFragment newInstance(ArrayList<InstagramPhoto> photoModels, int index, String userName) {
        InstagramViewFragment fragment = new InstagramViewFragment();
        Bundle b = new Bundle();
        b.putInt(Constants.KEY_DATA, index);
        b.putParcelableArrayList(Constants.KEY_DATA_TWO, photoModels);
        b.putString(Constants.KEY_DATA_THREE, userName);
        fragment.setArguments(b);
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_instagram, container, false);
        titleBar = (TitleBar) view.findViewById(R.id.titleBar);
        viewPager = (ViewPager) view.findViewById(R.id.view_pager_instagram);
        nameTextView = (TextView) view.findViewById(R.id.instagram_name_txt);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        titleBar.setTitle(getString(R.string.instagram));
        titleBar.enableBackButton();
        titleBar.disableClickTitle();
        titleBar.disableClickDone();
        titleBar.setCallback(this);
        int index = getArguments().getInt(Constants.KEY_DATA);
        userNameInstagram = getArguments().getString(Constants.KEY_DATA_THREE);
        if (!TextUtils.isEmpty(userNameInstagram)) {
            nameTextView.setText(userNameInstagram);
            nameTextView.setOnClickListener(this);
        }
        ArrayList<InstagramPhoto> photoModels = getArguments().getParcelableArrayList(Constants.KEY_DATA_TWO);
        InstagramAdapter adapter = new InstagramAdapter(getChildFragmentManager(), photoModels);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(index);
    }


    @Override
    public void onClick(View view) {
        Uri uri = Uri.parse("http://instagram.com/_u/" + userNameInstagram);
        Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);
        likeIng.setPackage("com.instagram.android");
        try {
            startActivity(likeIng);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://instagram.com/" + userNameInstagram)));
        }
    }

    @Override
    public void onTitleBarClicked() {

    }

    @Override
    public void onRightButtonClicked() {

    }

    @Override
    public void onBackButtonClicked() {
        getActivity().onBackPressed();
    }

    @Override
    public void onMiddleButtonClicked() {

    }
}
