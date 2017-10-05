package jp.stage.stagelovemaker.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import jp.stage.stagelovemaker.R;
import jp.stage.stagelovemaker.adapter.FeaturesPagerAdapter;
import jp.stage.stagelovemaker.base.CommonActivity;
import jp.stage.stagelovemaker.base.EventDistributor;
import jp.stage.stagelovemaker.base.NotificationEvent;
import jp.stage.stagelovemaker.base.UserPreferences;
import jp.stage.stagelovemaker.fragment.DetailProfileFragment;
import jp.stage.stagelovemaker.fragment.MessageFragment;
import jp.stage.stagelovemaker.fragment.NewMatchFragment;
import jp.stage.stagelovemaker.model.NotificationModel;
import jp.stage.stagelovemaker.model.UserInfoModel;
import jp.stage.stagelovemaker.model.UserTokenModel;
import jp.stage.stagelovemaker.network.IHttpResponse;
import jp.stage.stagelovemaker.network.NetworkManager;
import jp.stage.stagelovemaker.utils.Constants;
import jp.stage.stagelovemaker.utils.GPSTracker;
import jp.stage.stagelovemaker.utils.Utils;
import jp.stage.stagelovemaker.views.MainTabBar;
import jp.stage.stagelovemaker.views.NonSwipeableViewPager;
import timber.log.Timber;

public class MainActivity extends CommonActivity implements MainTabBar.MainTabBarCallback,
        ViewPager.OnPageChangeListener {
    public static final String TAG = "MainActivity";
    MainTabBar mainTabBar;
    NonSwipeableViewPager viewPager;
    FeaturesPagerAdapter featuresPagerAdapter;
    int indexTab;

    UserInfoModel loginModel;
    NetworkManager networkManager;
    Gson gson = new Gson();

    private static final int EVENTS = EventDistributor.MY_PROFILE_CHANGE;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        if (checkLogin() && intent != null && intent.getExtras() != null) {
            NotificationModel model = intent.getExtras().getParcelable(Constants.NOTI_DATA);
            handleNotification(model);
        }
    }

    private void handleNotification(NotificationModel model) {
        Utils.hideSoftKeyboard(this);
        if (model != null) {
            switch (model.getType()) {
                case Constants.NOTIFY_MESSAGES:
                    networkManager.requestApiNoProgress(networkManager.getProfile(model.getSenderId()), Constants.ID_SEND_CHAT);
                    break;
                case Constants.NOTIFY_MESSAGE_LIKES:
                    networkManager.requestApiNoProgress(networkManager.getProfile(model.getSenderId()), Constants.ID_SEND_CHAT);
                    break;
                case Constants.NOTIFY_SUPER_LIKES:
                    networkManager.requestApiNoProgress(networkManager.getProfile(model.getSenderId()), Constants.ID_SEND_CHAT);
                    break;
                case Constants.NOTIFY_NEW_MATCHES:
                    networkManager.requestApiNoProgress(
                            networkManager.getProfile(model.getSenderId()), Constants.ID_NEW_MATCH);
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (checkLogin()) {
            if (isTaskRoot()) {
                Intent intent = getIntent();
                String action = intent.getAction();
                if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && action.equals(Intent.ACTION_MAIN)) {
                    finish();
                    return;
                }
            }
            setContentView(R.layout.activity_main);
            mainTabBar = (MainTabBar) findViewById(R.id.main_tabbar);
            viewPager = (NonSwipeableViewPager) findViewById(R.id.view_pager);

            mainTabBar.setCallback(this);
            mainTabBar.changeTab(MainTabBar.TAB_STAGE);
            networkManager = new NetworkManager(this, iHttpResponse);
            viewPager.addOnPageChangeListener(this);

            requestSelfProfile(true);
        } else {
            startNewActivity(LoginActivity.class, null);
            ActivityCompat.finishAffinity(this);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventDistributor.getInstance().register(contentUpdate);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventDistributor.getInstance().unregister(contentUpdate);
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(NotificationEvent event) {
        int id = UserPreferences.getCurrentUserId();
        Timber.d("onEvent(", event);
        if (id != event.notificationModel.getSenderId()) {
            Toast.makeText(this, event.notificationModel.getBody(), Toast.LENGTH_SHORT).show();
        }

        switch (event.action) {
            case NEW_MESSAGE:
                //EventDistributor.getInstance().sendListMatchUpdateBroadcast();
                break;
            case NEW_MATCH:
                if (id != event.notificationModel.getSenderId()) {
                    networkManager.requestApiNoProgress(
                            networkManager.getProfile(event.notificationModel.getSenderId()), Constants.ID_NEW_MATCH);
                }
                EventDistributor.getInstance().sendListMatchUpdateBroadcast();
                break;
            case NEW_LIKE:
                break;
            case NEW_SUPER_LIKE:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private EventDistributor.EventListener contentUpdate = new EventDistributor.EventListener() {
        @Override
        public void update(EventDistributor eventDistributor, Integer arg) {
            if ((EVENTS & arg) != 0) {
                Log.d(TAG, "Received contentUpdate Intent.");
                requestSelfProfile(false);
            }
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                featuresPagerAdapter.getLocation(true);
            } else {
                featuresPagerAdapter.getLocation(false);
            }
        }
    }

    @Override
    public void onTabChanged(int index) {
        Utils.hideSoftKeyboard(this);
        viewPager.setCurrentItem(index - 1, true);
        if (index == MainTabBar.TAB_CHAT) {
            EventDistributor.getInstance().sendListMatchUpdateBroadcast();
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mainTabBar.changeTab(position + 1);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private boolean checkLogin() {
        String accessToken = UserPreferences.getPrefUserAccessToken();
        return !TextUtils.isEmpty(accessToken);
    }

    public void requestSelfProfile(boolean init) {
        int id = UserPreferences.getCurrentUserId();
        if (init) {
            networkManager.requestApiNoProgress(networkManager.getProfile(id), Constants.ID_SELF_INFO);
        } else {
            networkManager.requestApiNoProgress(networkManager.getProfile(id), Constants.ID_UPDATE_USER);
        }

    }

    void loadDataProfile() {
        int id = UserPreferences.getCurrentUserId();
        if (Utils.getLocation(this) == null) {
            GPSTracker gps = new GPSTracker(this);
            gps.getLocation();
            if (gps.canGetLocation()) {
                double latitude = gps.getLatitude();
                double longitude = gps.getLongitude();
                Utils.setLocation(this, latitude, longitude);
                if (networkManager == null) {
                    networkManager = new NetworkManager(this, iHttpResponse);
                }
                networkManager.requestApiNoProgress(networkManager.updateLocation(id, latitude, longitude),
                        Constants.ID_UPDATED_LOCATION);
            }
        }

        loadFeatures();
        final Handler handler = new Handler();
        handler.postDelayed(() -> mainTabBar.changeTab(MainTabBar.TAB_STAGE), 100);
    }

    private void loadFeatures() {
        featuresPagerAdapter = new FeaturesPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(featuresPagerAdapter);
    }

    public IHttpResponse iHttpResponse = new IHttpResponse() {
        @Override
        public void onHttpComplete(String response, int idRequest) {
            switch (idRequest) {
                case Constants.ID_SELF_INFO:
                    UserTokenModel model = gson.fromJson(response, UserTokenModel.class);
                    if (model != null) {
                        loginModel = model.getUserInfo();
                        FirebaseMessaging.getInstance().subscribeToTopic(loginModel.getNotificationTopic());
                        UserPreferences.setPrefUserData(loginModel);
                        loadDataProfile();
                    }
                    break;
                case Constants.ID_UPDATE_USER:
                    UserTokenModel update = gson.fromJson(response, UserTokenModel.class);
                    if (update != null) {
                        UserPreferences.setPrefUserData(loginModel);
                        loginModel = update.getUserInfo();
                    }
                    break;
                case Constants.ID_NEW_MATCH:
                    UserTokenModel matchUserToken = gson.fromJson(response, UserTokenModel.class);
                    if (matchUserToken != null) {
                        UserInfoModel matchUser = matchUserToken.getUserInfo();
                        NewMatchFragment newMatchFragment = NewMatchFragment.newInstance(loginModel, matchUser);
                        newMatchFragment.setDelegate(newMatchDelegate);
                        add(newMatchFragment, NewMatchFragment.TAG, true, false);
                    }
                    break;
                case Constants.ID_SEND_CHAT:
                    UserTokenModel sendUserToken = gson.fromJson(response, UserTokenModel.class);
                    if (sendUserToken != null) {
                        UserInfoModel detailUser = sendUserToken.getUserInfo();
                        DetailProfileFragment detailProfileFragment = DetailProfileFragment.newInstance(detailUser, true);
                        detailProfileFragment.setCallback(detailProfileCallback);
                        addNoneSlideIn(detailProfileFragment, DetailProfileFragment.TAG, true, true, R.id.flContainer);
                    }
                    break;
            }
        }

        @Override
        public void onHttpError(String response, int idRequest, int errorCode) {
        }
    };

    public DetailProfileFragment.DetailProfileCallback detailProfileCallback = new DetailProfileFragment.DetailProfileCallback() {
        @Override
        public void onNopeClicked() {
            onBackPressed();
        }

        @Override
        public void onLikeClicked() {
            onBackPressed();
        }

        @Override
        public void onStarClicked() {
            onBackPressed();
        }
    };

    public NewMatchFragment.NewMatchDelegate newMatchDelegate = (matchUser, chatRoom) -> {
        onBackPressed();
        MessageFragment messageFragment = MessageFragment.newInstance(matchUser, chatRoom);
        addNoneSlideIn(messageFragment, MessageFragment.TAG, true, true, R.id.flContainer);
    };

    public UserInfoModel getLoginModel() {
        return loginModel;
    }
}
