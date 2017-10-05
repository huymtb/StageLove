package jp.stage.stagelovemaker.fragment;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import at.markushi.ui.CircleButton;
import jp.stage.stagelovemaker.MyApplication;
import jp.stage.stagelovemaker.R;
import jp.stage.stagelovemaker.adapter.InstagramImagePageAdapter;
import jp.stage.stagelovemaker.base.BaseFragment;
import jp.stage.stagelovemaker.base.UserPreferences;
import jp.stage.stagelovemaker.model.InstagramPhoto;
import jp.stage.stagelovemaker.model.UserInfo;
import jp.stage.stagelovemaker.model.UserInfoModel;
import jp.stage.stagelovemaker.network.IHttpResponse;
import jp.stage.stagelovemaker.network.NetworkManager;
import jp.stage.stagelovemaker.utils.Constants;
import jp.stage.stagelovemaker.utils.Utils;
import jp.stage.stagelovemaker.views.PageControl;
import jp.stage.stagelovemaker.views.TitleBar;
import jp.stage.stagelovemaker.views.UserImageSlide;

/**
 * Created by congn on 8/4/2017.
 */

public class DetailProfileFragment extends BaseFragment implements View.OnClickListener {
    public static final String TAG = "DetailProfileFragment";

    NetworkManager networkManager;
    UserImageSlide userImageSlide;
    TextView tvName;
    TextView tvAge;
    TextView tvWork;
    TextView tvSchool;
    TextView tvAbout;
    TextView tvDistance;

    CircularImageView ivBack;
    CircleButton ivNope;
    CircleButton ivLike;
    CircleButton ivSuperLike;

    ArrayList<InstagramPhoto> photoModel;
    String instagramUser;
    PageControl pageControl;
    RelativeLayout instagramLayout;
    LinearLayout feelingButtonLayout;
    UserInfoModel userInfoModel;
    DetailProfileCallback delegate;

    ViewPager instagramPager;
    InstagramImagePageAdapter instagramImagePageAdapter;
    int numberColumn = 0;
    int widthButton;

    boolean init;

    public static DetailProfileFragment newInstance(UserInfoModel user, Boolean isLoadFeel) {
        Bundle args = new Bundle();
        args.putParcelable("user", user);
        args.putBoolean(Constants.KEY_DATA, isLoadFeel);
        DetailProfileFragment fragment = new DetailProfileFragment();
        fragment.setArguments(args);
        return fragment;
    }

    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            pageControl.setCurrentPage(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        networkManager = new NetworkManager(context, iHttpResponse);
    }

    public IHttpResponse iHttpResponse = new IHttpResponse() {
        @Override
        public void onHttpComplete(String response, int idRequest) {

        }

        @Override
        public void onHttpError(String response, int idRequest, int errorCode) {

        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_user, container, false);
        userImageSlide = (UserImageSlide) view.findViewById(R.id.user_image_slide);
        tvName = (TextView) view.findViewById(R.id.tv_name);
        tvAge = (TextView) view.findViewById(R.id.tv_age);
        tvWork = (TextView) view.findViewById(R.id.tv_work);
        tvSchool = (TextView) view.findViewById(R.id.tv_school);
        tvAbout = (TextView) view.findViewById(R.id.tv_about);
        tvDistance = (TextView) view.findViewById(R.id.tv_location);
        ivBack = (CircularImageView) view.findViewById(R.id.iv_back);
        ivNope = (CircleButton) view.findViewById(R.id.circleButtonClose);
        ivLike = (CircleButton) view.findViewById(R.id.circleButtonHeart);
        ivSuperLike = (CircleButton) view.findViewById(R.id.circleButtonStar);

        feelingButtonLayout = (LinearLayout) view.findViewById(R.id.layout_feeling_button);
        instagramPager = (ViewPager) view.findViewById(R.id.view_pager);
        pageControl = (PageControl) view.findViewById(R.id.instagram_pagecontrol);
        instagramLayout = (RelativeLayout) view.findViewById(R.id.instagram_profile_layout);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        userInfoModel = getArguments().getParcelable("user");
        init = getArguments().getBoolean(Constants.KEY_DATA);
        if (getArguments().getBoolean(Constants.KEY_DATA)) {
            feelingButtonLayout.setVisibility(View.VISIBLE);
        } else {
            feelingButtonLayout.setVisibility(View.GONE);
        }
        if (userInfoModel.getAvatars() != null && !userInfoModel.getAvatars().isEmpty()) {
            ArrayList<String> lstAvatar = new ArrayList<>();
            for (int i = 0; i < userInfoModel.getAvatars().size(); i++) {
                lstAvatar.add(userInfoModel.getAvatars().get(i).getUrl());
            }
            userImageSlide.setAvatar(getChildFragmentManager(), lstAvatar);
        }

        tvName.setText(userInfoModel.getFirstName() + ",");
        tvAge.setText(String.valueOf(userInfoModel.getAge()));
        if (!TextUtils.isEmpty(userInfoModel.getMeta().getSchool())) {
            tvSchool.setText(userInfoModel.getMeta().getSchool());
        }

        if (!TextUtils.isEmpty(userInfoModel.getMeta().getCurrentWork())) {
            tvWork.setText(userInfoModel.getMeta().getCurrentWork());
        }
        if (!TextUtils.isEmpty(userInfoModel.getMeta().getAboutMe())) {
            tvAbout.setText(userInfoModel.getMeta().getAboutMe());
        }

        if (Utils.getApplication(getActivity()).getLocation() != null) {
            Location myLocation = Utils.getApplication(getActivity()).getLocation();
            double myLat = myLocation.getLatitude();
            double myLng = myLocation.getLongitude();

            double otherLat = userInfoModel.getLatitude();
            double otherLng = userInfoModel.getLongitude();

            double kmDistance = Utils.distance(myLat, myLng, otherLat, otherLng);

            if (UserPreferences.getPrefDistanceUnit().equals(Constants.MILE)) {
                kmDistance *= 0.621371;
                tvDistance.setText(String.valueOf(Utils.roundDistance(kmDistance)) + " mile away");
            } else {
                tvDistance.setText(String.valueOf(Utils.roundDistance(kmDistance)) + " km away");
            }

        }

        ivBack.setOnClickListener(this);
        ivNope.setOnClickListener(this);
        ivLike.setOnClickListener(this);
        ivSuperLike.setOnClickListener(this);
        calculateLayout();
        if (photoModel == null || photoModel.size() == 0) {
            if (!TextUtils.isEmpty(userInfoModel.getInstagramUser())) {
                requestInstagramImage(userInfoModel.getInstagramUser());
                instagramLayout.setVisibility(View.VISIBLE);
            } else {
                instagramLayout.setVisibility(View.GONE);
            }
        } else {
            loadInstagramImage();
        }

    }

    private void calculateLayout() {
        int width = Utils.getiWidthScreen(getActivity());
        int margin = (int) getResources().getDimension(R.dimen.form_margin);
        int paddingButton = (int) getResources().getDimension(R.dimen.padding_interest_button);
        widthButton = (width - (margin * 2) - (paddingButton * 2)) / 3;
    }

    private void loadInstagramImage() {
        if (TextUtils.isEmpty(userInfoModel.getInstagramUser())) {
            return;
        }
        if (instagramImagePageAdapter == null) {
            numberColumn = 3;
            int paddingButton = (int) getResources().getDimension(R.dimen.padding_interest_button);
            ViewGroup.LayoutParams paramsAdapter = instagramPager.getLayoutParams();
            if (photoModel.size() > 2) {
                paramsAdapter.height = widthButton * 2 + paddingButton;
            } else {
                paramsAdapter.height = widthButton;
            }
        }

        int iAdd = 0;
        if ((photoModel.size() % (numberColumn * 2)) != 0) {
            iAdd = 1;
        }
        int numberFragment = (photoModel.size() / (numberColumn * 2)) + iAdd;
        if (photoModel.size() > 4) {
            pageControl.setNumberOfPages(numberFragment);
            pageControl.setCurrentPageColor(ContextCompat.getColor(getContext(), R.color.very_light_gray));
            pageControl.setHintPageColor(ContextCompat.getColor(getContext(), R.color.dark_gray));
            pageControl.setCurrentPage(0);
        } else {
            pageControl.setVisibility(View.GONE);
        }
        instagramImagePageAdapter = new InstagramImagePageAdapter(getChildFragmentManager(),
                photoModel, numberColumn, numberFragment, userInfoModel.getInstagramUser());
        instagramPager.setAdapter(instagramImagePageAdapter);
        instagramPager.addOnPageChangeListener(viewPagerPageChangeListener);
        instagramImagePageAdapter.notifyDataSetChanged();
    }

    void requestInstagramImage(String userName) {
        Gson gson = new Gson();
        String response = "";
        try {
            response = Utils.loadJSONFromAsset(getActivity(), Constants.SEED_INSTAGRAM);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!TextUtils.isEmpty(response)) {
            InstagramPhoto[] models = gson.fromJson(response, InstagramPhoto[].class);
            if (models != null) {
                photoModel = new ArrayList<>(Arrays.asList(models));
                loadInstagramImage();
            }
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                getActivity().onBackPressed();
                break;
            case R.id.circleButtonClose:
                if (delegate != null) {
                    delegate.onNopeClicked();
                }
                break;
            case R.id.circleButtonHeart:
                if (delegate != null) {
                    delegate.onLikeClicked();
                }
                break;
            case R.id.circleButtonStar:
                if (delegate != null) {
                    delegate.onStarClicked();
                }
                break;
        }
    }

    private void setFeelings() {
        //networkManager.requestApiNoProgress(networkManager.setFeeling(userId, userFriend, type), Constants.ID_UPDATE_FEELING);
    }

    public void setCallback(DetailProfileCallback callback) {
        delegate = callback;
    }

    public interface DetailProfileCallback {
        void onNopeClicked();

        void onLikeClicked();

        void onStarClicked();
    }
}
