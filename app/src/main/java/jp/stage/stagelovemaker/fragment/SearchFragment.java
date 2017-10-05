package jp.stage.stagelovemaker.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.stage.stagelovemaker.MyApplication;
import jp.stage.stagelovemaker.R;
import jp.stage.stagelovemaker.activity.MainActivity;
import jp.stage.stagelovemaker.base.BaseFragment;
import jp.stage.stagelovemaker.base.UserPreferences;
import jp.stage.stagelovemaker.model.UsersPageModel;
import jp.stage.stagelovemaker.network.IHttpResponse;
import jp.stage.stagelovemaker.network.NetworkManager;
import jp.stage.stagelovemaker.utils.AlertDialog;
import jp.stage.stagelovemaker.utils.Constants;
import jp.stage.stagelovemaker.utils.GPSTracker;
import jp.stage.stagelovemaker.utils.Utils;
import pl.bclogic.pulsator4droid.library.PulsatorLayout;

/**
 * Created by congn on 8/4/2017.
 */

public class SearchFragment extends BaseFragment implements AlertDialog.AlertDialogDelegate,
        GPSTracker.GPSTrackerDelegate {
    public static final String TAG = "SearchFragment";

    PulsatorLayout pulsatorLayout;
    TextView tvDescription;
    FloatingActionButton btRewind;
    FloatingActionButton btNope;
    FloatingActionButton btBoost;
    FloatingActionButton btLike;
    FloatingActionButton btSuperLike;
    CircleImageView ivAvatar;
    SearchFragmentCallback delegate;
    GPSTracker gps;
    NetworkManager networkManager;
    SharedPreferences prefs;

    public static SearchFragment newInstance() {
        Bundle args = new Bundle();
        SearchFragment fragment = new SearchFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        networkManager = new NetworkManager(getActivity(), iHttpResponse);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        pulsatorLayout = (PulsatorLayout) view.findViewById(R.id.pulsator);
        tvDescription = (TextView) view.findViewById(R.id.des_search_txt);
        btRewind = (FloatingActionButton) view.findViewById(R.id.bt_rewind);
        btNope = (FloatingActionButton) view.findViewById(R.id.bt_pass);
        btBoost = (FloatingActionButton) view.findViewById(R.id.bt_boost);
        btLike = (FloatingActionButton) view.findViewById(R.id.bt_like);
        btSuperLike = (FloatingActionButton) view.findViewById(R.id.bt_superlike);
        ivAvatar = (CircleImageView) view.findViewById(R.id.avatar_user_img);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        pulsatorLayout.start();
        Typeface mediumType = Typeface.createFromAsset(getActivity().getAssets(), "fonts/proximanovasoft-bold.otf");
        tvDescription.setTypeface(mediumType);
        btRewind.setEnabled(false);
        btNope.setEnabled(false);
        btBoost.setEnabled(false);
        btLike.setEnabled(false);
        btSuperLike.setEnabled(false);
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();

        loadAvatar();
        prefs.registerOnSharedPreferenceChangeListener(avatarChange);

        getLocation();
        getContext().registerReceiver(gpsReceiver,
                new IntentFilter("android.location.PROVIDERS_CHANGED"));
    }

    @Override
    public void onPause() {
        super.onPause();
        prefs.unregisterOnSharedPreferenceChangeListener(avatarChange);
        getContext().unregisterReceiver(gpsReceiver);
    }

    public void getLocation() {
        if (Utils.getLocation(getActivity()) == null) {
            if (gps == null) {
                gps = new GPSTracker(getActivity());
                gps.setDelegate(this);
            }
            gps.getLocation();
            final Handler handler = new Handler();
            handler.postDelayed(() -> {
                if (getActivity() != null && !getActivity().isFinishing() && !getActivity().isDestroyed()) {
                    getActivity().runOnUiThread(() -> {
                        if (gps != null && gps.getLocation() == null) {
                            tvDescription.setText(R.string.des_deny_location);
                        }
                    });
                }
            }, 1000);
            if (gps.canGetLocation()) {
                int id = UserPreferences.getCurrentUserId();
                double latitude = gps.getLatitude();
                double longitude = gps.getLongitude();
                Utils.setLocation(getActivity(), latitude, longitude);
                networkManager.requestApiNoProgress(networkManager.updateLocation(id, latitude, longitude),
                        Constants.ID_UPDATED_LOCATION);
            } else {
                if (gps.getTypeDenyLocation() == 0) {
                    AlertDialog dialog = new AlertDialog(getContext(), Utils.fromHtml(getString(R.string.request_access_location)), R.mipmap.icon_location);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.setDelegate(SearchFragment.this, Constants.GET_LOCATION);
                    dialog.show();
                } else if (gps.getTypeDenyLocation() == 2) {
                    showAnimationSearch(false);
                }
            }
        } else {
            handleRespone();
        }
    }

    public void handleRespone() {
        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> showAnimationSearch(true));
        }
    }

    public void showAnimationSearch(Boolean bsearch) {
        tvDescription.setText(R.string.search_people_match_you);
        if (delegate != null && bsearch) {
            final Handler handler = new Handler();
            handler.postDelayed(() -> delegate.onSearchFinished(), 1000);

        }
        pulsatorLayout.setVisibility(View.VISIBLE);
        pulsatorLayout.start();
    }

    public void setCallback(SearchFragmentCallback fragment) {
        delegate = fragment;
    }

    @Override
    public void clickAllow(Boolean bAllow, String type) {
        if (bAllow) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            getActivity().startActivity(intent);
        }
        tvDescription.setText(R.string.des_deny_location);
    }

    @Override
    public void setYourLocation(Location location) {
        int id = UserPreferences.getCurrentUserId();
        double latitude = gps.getLatitude();
        double longitude = gps.getLongitude();
        Utils.setLocation(getActivity(), latitude, longitude);
        networkManager.requestApiNoProgress(networkManager.updateLocation(id, latitude, longitude),
                Constants.ID_UPDATED_LOCATION);
    }

    public IHttpResponse iHttpResponse = new IHttpResponse() {
        @Override
        public void onHttpComplete(String response, int idRequest) {
            switch (idRequest) {
                case Constants.ID_UPDATED_LOCATION: {
                    handleRespone();
                    break;
                }
            }
        }

        @Override
        public void onHttpError(String response, int idRequest, int errorCode) {

        }
    };

    public interface SearchFragmentCallback {
        void onSearchFinished();
    }

    private BroadcastReceiver gpsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().matches("android.location.PROVIDERS_CHANGED")) {
                getLocation();
            }
        }
    };

    public void changeBackgroundNoPals(int text) {
        stopTimer();
        tvDescription.setText(text);
    }

    public void stopTimer() {
        if (pulsatorLayout != null) {
            pulsatorLayout.setVisibility(View.GONE);
            pulsatorLayout.stop();
        }
    }

    private void loadAvatar() {
        if (!TextUtils.isEmpty(UserPreferences.getPrefUserAvatar(0))) {
            String linkAvatar;
            linkAvatar = UserPreferences.getPrefUserAvatar(0);
            Utils.setAvatar(getContext(), ivAvatar, linkAvatar);
        }
    }

    private SharedPreferences.OnSharedPreferenceChangeListener avatarChange = (sharedPreferences, key) -> {
        if (key.equals(UserPreferences.PREF_USER_AVATAR1)) {
            loadAvatar();
        }
    };
}
